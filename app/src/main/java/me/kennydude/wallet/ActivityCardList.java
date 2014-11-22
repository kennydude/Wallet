package me.kennydude.wallet;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.cocosw.bottomsheet.BottomSheet;
import com.github.mrengineer13.snackbar.SnackBar;
import com.roscopeco.ormdroid.Entity;

import java.util.List;

/**
 * @author kennydude
 */
public class ActivityCardList extends BaseActivity {
	private SwipeRefreshLayout mPullToRefreshLayout;
	public int refreshRemaining = 0;

	@Override
	public void onCreate(Bundle bis){
		super.onCreate(bis);
		setContentView(R.layout.activity_cardlist);

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		mPullToRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.ptr);
		mPullToRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				Utils.debug("Refreshing...");
				new Thread(new Runnable() {
					@Override
					public void run() {
						List<CardUtils.StoredCard> cards = Entity.query(CardUtils.StoredCard.class).executeMulti();
						for(CardUtils.StoredCard c : cards){
							refreshRemaining += 1;
							WalletApplication.jobcentre.addJobInBackground(new RefreshCardTask(c.id));
						}
					}
				}).start();
			}
		});

		refreshCards();
	}

	@Override
	public void onResume(){
		super.onResume();

		IntentFilter filter = new IntentFilter();
		filter.addAction(CardUtils.ACTION_CARD_REFRESHED);
		registerReceiver(broadcastReceiver, filter);
	}

	@Override
	public void onPause(){
		super.onPause();
		unregisterReceiver(broadcastReceiver);
	}

	public BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			refreshRemaining -= 1;
			if(refreshRemaining <= 0){
				mPullToRefreshLayout.setRefreshing(false);
				refreshCards();
			}
		}
	};

	LinearLayout cardList;

	public void refreshCards(){
		cardList = (LinearLayout) findViewById(R.id.cards);
		cardList.removeAllViews(); // Remove all children!

		final LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT
		);
		int m = getResources().getDimensionPixelSize(R.dimen.card_margin);
		lp.setMargins(m,m,m,0);

		new Thread(new Runnable() {
			@Override
			public void run() {
				// Get all cards stored
				List<CardUtils.StoredCard> cards = Entity.query(CardUtils.StoredCard.class).executeMulti();
				boolean empty = true;

				for(final CardUtils.StoredCard card : cards){
					final Card realCard = card.getCard();
					runOnUiThread(new Runnable() {
						@Override
						public void run() {

							View cv = realCard.getCardView(getLayoutInflater());
							cv.setClickable(true);
							cardList.addView(cv, lp);

							cv.setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View view) {
									Intent i = new Intent(ActivityCardList.this, realCard.getViewActivity());
									i.putExtra("id", card.id);
									startActivityForResult(i, 1);
								}
							});

						}
					});
					empty = false;
				}

				if(empty){
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							findViewById(R.id.empty).setVisibility(View.VISIBLE);
						}
					});
				} else{
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							findViewById(R.id.empty).setVisibility(View.GONE);
						}
					});
				}
			}
		}).start();
	}

	@Override
	public boolean onCreateOptionsMenu (Menu menu){
		getMenuInflater().inflate(R.menu.card_list, menu);
		return true;
	}

	@Override
	public boolean  onOptionsItemSelected (MenuItem item){
		switch(item.getItemId()){
			case R.id.new_card:
				//startActivityForResult(new Intent(this, ActivityCardAdd.class), 1);
				BottomSheet.Builder builder = new BottomSheet.Builder(this)
						.title(R.string.choose_card);

				int i = 0;
				for( CardUtils.CardDescription cd : CardUtils.cards ){
					builder.sheet(i, cd.stringId);
					i++;
				}

				builder.listener(new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialogInterface, int which) {
						try {
							Intent i = new Intent(ActivityCardList.this, CardUtils.cards.get(which).cls.newInstance().getEditActivity());
							i.setAction(CardUtils.ACTION_NEW_CARD);
							startActivityForResult(i, 1);
						} catch (Exception e){
							e.printStackTrace();
						}
					}

				}).show();
				return true;
			case R.id.open_source:
				startActivity(new Intent(this, ActivityOpenSource.class));
				return true;
		}
		return false;
	}

	@Override
	protected void onActivityResult (int requestCode, int resultCode, Intent data){
		if(resultCode == RESULT_OK){
			if(data.hasExtra("msg")){
				SnackBar sb = new SnackBar(this);
				sb.show(getString(data.getIntExtra("msg", -1)));
			}
			refreshCards();
		}
	}

}
