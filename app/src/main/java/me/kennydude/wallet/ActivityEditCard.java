package me.kennydude.wallet;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.github.mrengineer13.snackbar.SnackBar;
import com.roscopeco.ormdroid.Entity;

import me.kennydude.wallet.R;

import me.kennydude.wallet.CardUtils;

import static com.roscopeco.ormdroid.Query.eql;

/**
 * Deals with editing cards
 *
 * @author kennydude
 */
public abstract class ActivityEditCard<T extends Card> extends BaseActivity {
	public boolean newCardMode = false;
	public T theCard;
	public CardUtils.StoredCard storedCard;

	public T getCard(){
		return theCard;
	}

	public void setCard(T newcard){
		theCard = newcard;
	}

	@Override
	public void onCreate(Bundle bis){
		super.onCreate(bis);

		LinearLayout lv = (LinearLayout) getLayoutInflater().inflate(R.layout.activity_edit, null);
		lv.addView(
			getLayoutInflater().inflate(getEditorLayout(), null),
			new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.MATCH_PARENT,
					LinearLayout.LayoutParams.MATCH_PARENT
			)
		);
		setContentView(lv);

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		toolbar.setNavigationIcon(R.drawable.ic_check_white_24dp);
		toolbar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				validateCardAndSave();
			}
		});

		if(getIntent().getAction().equals(CardUtils.ACTION_EDIT_CARD)){
			storedCard = Entity.query(CardUtils.StoredCard.class).where(eql("id", getIntent().getIntExtra("id", -1))).execute();
			setCard((T) storedCard.getCard());
			toolbar.setTitle(getString(R.string.edit_x_card).replace("{card}", getString(theCard.getName())));
		} else{
			newCardMode = true;
			createNewCard();
			toolbar.setTitle(getString(R.string.new_x_card).replace("{card}", getString(theCard.getName())));
		}
	}

	@Override
	public boolean onCreateOptionsMenu (Menu menu){
		getMenuInflater().inflate(R.menu.card_edit, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected (MenuItem item){
		switch(item.getItemId()){
			case android.R.id.home:
				// Up we go!
				Intent up = new Intent(this, ActivityCardList.class);
				up.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(up); finish();
				break;
			case R.id.save:
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

						if(newCardMode){
							storedCard = new CardUtils.StoredCard();
						} else {
							// Don't think anything is required
						}

						storedCard.setCard(theCard);
						storedCard.save();

						if(newCardMode){
							setResult(RESULT_OK, new Intent().putExtra("msg", R.string.card_created));
						} else{
							setResult(RESULT_OK, new Intent().putExtra("msg", R.string.card_edited));
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
				} catch (Exception e){
					e.printStackTrace();
				}

				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						pd.dismiss();
						SnackBar sb = new SnackBar(ActivityEditCard.this);
						sb.show(getString(R.string.card_details_incorrect));
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
