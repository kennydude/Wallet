package me.kennydude.wallet;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import me.kennydude.wallet.R;

import me.kennydude.wallet.CardUtils;

/**
 * Deals with editing cards
 *
 * @author kennydude
 */
public abstract class ActivityEditCard<T extends Card> extends BaseActivity {
	public boolean newCardMode = false;
	public T theCard;

	public T getCard(){
		return theCard;
	}

	public void setCard(T newcard){
		theCard = newcard;
	}

	@Override
	public void onCreate(Bundle bis){
		super.onCreate(bis);

		setContentView(getEditorLayout());

		if(getIntent().getAction().equals(CardUtils.ACTION_EDIT_CARD)){
			// TODO: Load card and stuff

		} else{
			setTitle(R.string.new_card);
			newCardMode = true;
			createNewCard();
		}
	}

	@Override
	public boolean onCreateOptionsMenu (Menu menu){
		getMenuInflater().inflate(R.menu.card_edit, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected (MenuItem item){
		if(item.getItemId() == R.id.save){
			// Validate it
			validateCardAndSave();
			return true;
		}
		return false;
	}

	ProgressDialog pd;

	public void validateCardAndSave(){
		pd = new ProgressDialog(this);
		pd.setMessage(getString(R.string.verifying_card));
		pd.show();

		setCardData();

		new Thread(new Runnable() {

			@Override
			public void run() {

				try{
					if(theCard.refreshData()){
						// If we could refresh, we'll save it
						// aka: good refresh = save

						CardUtils.StoredCard sc;
						if(newCardMode){
							sc = new CardUtils.StoredCard();
						} else {
							sc = new CardUtils.StoredCard();
							// TODO: implement correctly
						}

						sc.setCard(theCard);
						sc.save();

						if(newCardMode){
							setResult(RESULT_OK, new Intent().putExtra("msg", R.string.card_created));
						}

						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								pd.dismiss();
								finish();
							}
						});

						return;
					}
				} catch (Exception e){}

				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						pd.dismiss();
						Crouton.makeText(ActivityEditCard.this, R.string.card_details_incorrect, Style.ALERT).show();
					}
				});

			}

		}).start();
	}

	/**
	 * Get the layout required for this activity
	 */
	public abstract int getEditorLayout();

	/**
	 * Create a new card object
	 */
	public abstract void createNewCard();

	/**
	 * Implement this and set the fields of your card from the on-screen UI
	 */
	public abstract void setCardData();

}
