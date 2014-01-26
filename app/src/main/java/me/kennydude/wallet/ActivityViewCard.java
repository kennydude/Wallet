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
import android.view.Menu;
import android.view.MenuItem;

import com.roscopeco.ormdroid.Entity;

import static com.roscopeco.ormdroid.Query.eql;

/**
 * @author kennydude
 */
public abstract class ActivityViewCard<T extends Card> extends BaseActivity {

	public T myCard;
	int cardid;
	CardUtils.StoredCard storedCard;

	public T getCard(){
		return myCard;
	}

	public BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			intGetCard();
		}
	};

	void intGetCard(){
		if(getIntent().getIntExtra("id", -1) > 0){
			cardid = getIntent().getIntExtra("id", -1);
			storedCard = Entity.query(CardUtils.StoredCard.class).where(eql("id", cardid)).execute();
			myCard = (T) storedCard.getCard(); // should work :)

			showCard();
		} else{
			finish();
		}
	}

	@Override
	public void onCreate(Bundle bis){
		super.onCreate(bis);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		intGetCard();

		setTitle(myCard.getName());
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
	public boolean  onOptionsItemSelected (MenuItem item){
		switch(item.getItemId()){
			case android.R.id.home:
				// Up we go!
				Intent up = new Intent(this, ActivityCardList.class);
				up.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(up); finish();
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
