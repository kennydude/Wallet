package me.kennydude.wallet;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.preference.DialogPreference;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.github.mrengineer13.snackbar.SnackBar;
import com.roscopeco.ormdroid.Entity;
import static com.roscopeco.ormdroid.Query.eql;

/**
 * @author kennydude
 */
public abstract class ActivityViewCard<T extends Card> extends BaseActivity {

	public T myCard;
	int cardid;
	CardUtils.StoredCard storedCard;

	SwipeRefreshLayout swipeRefreshLayout;

	public T getCard(){
		return myCard;
	}

	public BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			intGetCard();
		}
	};

	@SuppressWarnings("unchecked")
	void intGetCard(){
		if(getIntent().getIntExtra("id", -1) > 0){
			cardid = getIntent().getIntExtra("id", -1);
			storedCard = Entity.query(CardUtils.StoredCard.class).where(eql("id", cardid)).execute();
			myCard = (T) storedCard.getCard(); // should work :)

			showCard();
			if(swipeRefreshLayout != null) {
				setTitle(myCard.getName());

				swipeRefreshLayout.setRefreshing(false);
			}
		} else{
			finish();
		}
	}

	public void saveCard(){
		storedCard.setCard( getCard() );
		storedCard.save();
	}

	@Override
	public void onCreate(Bundle bis){
		super.onCreate(bis);

		intGetCard();

		setTitle(myCard.getName());
	}

	@Override
	public void setContentView(int resId){
		setContentView(getLayoutInflater().inflate(resId, null));
	}

	@Override
	public void setContentView(View v){
		View lv = getLayoutInflater().inflate(R.layout.activity_view, null);

		swipeRefreshLayout = (SwipeRefreshLayout) lv.findViewById(R.id.ptr);
		swipeRefreshLayout.addView(v);
		swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				WalletApplication.jobcentre.addJobInBackground(new RefreshCardTask(storedCard.id));
			}
		});

		super.setContentView(lv);

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
		toolbar.setNavigationContentDescription(R.string.back);
		toolbar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent up = new Intent(ActivityViewCard.this, ActivityCardList.class);
				up.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				finish();
			}
		});
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

	@Override
	public boolean onCreateOptionsMenu (Menu menu){
		getMenuInflater().inflate(R.menu.card_view, menu);
		return true;
	}

	@Override
	protected void onActivityResult (int requestCode, int resultCode, Intent data){
		if(resultCode == RESULT_OK){
			if(data.hasExtra("msg")){
				SnackBar sb = new SnackBar(this);
				sb.show(getString(data.getIntExtra("msg", -1)));
			}
			intGetCard(); // Reload
		}
	}

	@Override
	public boolean  onOptionsItemSelected (MenuItem item){
		switch(item.getItemId()){
			case android.R.id.home:
				// Up we go!
				Intent up = new Intent(this, ActivityCardList.class);
				up.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(up); finish();
				break;
			case R.id.edit:
				Intent x = new Intent(this, getCard().getEditActivity());
				x.setAction(CardUtils.ACTION_EDIT_CARD);
				x.putExtra("id", storedCard.id);
				startActivityForResult(x, 924);
				break;
			case R.id.refresh:
				WalletApplication.jobcentre.addJobInBackground(new RefreshCardTask(storedCard.id));
				break;
			case R.id.delete:
				AlertDialog.Builder ab = new AlertDialog.Builder(this);
				ab.setTitle(R.string.delete);
				ab.setMessage(R.string.delete_confirm);

				ab.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int i) {
						dialogInterface.dismiss();
					}
				});

				ab.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener(){

					@Override
					public void onClick(DialogInterface dialogInterface, int i) {
						dialogInterface.dismiss();
						storedCard.delete();
						setResult(RESULT_OK, new Intent().putExtra("msg", R.string.deleted));
						finish();
					}
				});

				ab.show();
				break;
		}
		return false;
	}

	/**
	 * Show your card details here only
	 */
	public abstract void showCard();
}
