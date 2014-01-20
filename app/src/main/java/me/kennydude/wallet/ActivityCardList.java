package me.kennydude.wallet;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.roscopeco.ormdroid.Entity;

import java.util.List;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

/**
 * @author kennydude
 */
public class ActivityCardList extends BaseActivity {
	//private PullToRefreshLayout mPullToRefreshLayout;

	@Override
	public void onCreate(Bundle bis){
		super.onCreate(bis);
		setContentView(R.layout.activity_cardlist);

		/*mPullToRefreshLayout = (PullToRefreshLayout) findViewById(R.id.ptr);

		// Now setup the PullToRefreshLayout
		ActionBarPullToRefresh.from(this)
				// Mark All Children as pullable
				.allChildrenArePullable()
						// Set the OnRefreshListener
				.listener(this)
						// Finally commit the setup to our PullToRefreshLayout
				.setup(mPullToRefreshLayout);*/

		refreshCards();
	}

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
		if(item.getItemId() == R.id.new_card){
			startActivityForResult(new Intent(this, ActivityCardAdd.class), 1);
			return true;
		}
		return false;
	}

	@Override
	protected void onActivityResult (int requestCode, int resultCode, Intent data){
		if(resultCode == RESULT_OK){
			if(data.hasExtra("msg")){
				Crouton.makeText(this, data.getIntExtra("msg", -1), Style.CONFIRM).show();
			}
			refreshCards();
		}
	}

}
