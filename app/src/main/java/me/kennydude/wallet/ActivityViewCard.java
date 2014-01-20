package me.kennydude.wallet;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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

	@Override
	public void onCreate(Bundle bis){
		super.onCreate(bis);

		if(getIntent().getIntExtra("id", -1) > 0){
			cardid = getIntent().getIntExtra("id", -1);
			storedCard = Entity.query(CardUtils.StoredCard.class).where(eql("id", cardid)).execute();
			myCard = (T) storedCard.getCard(); // should work :)
		} else{
			finish();
		}

	}

	@Override
	public boolean onCreateOptionsMenu (Menu menu){
		getMenuInflater().inflate(R.menu.card_view, menu);
		return true;
	}

	@Override
	public boolean  onOptionsItemSelected (MenuItem item){
		switch(item.getItemId()){
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

}
