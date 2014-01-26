package me.kennydude.wallet.thegym;

import android.widget.EditText;

import me.kennydude.wallet.ActivityEditCard;
import me.kennydude.wallet.R;

/**
 * @author kennydude
 */
public class EditTheGymCard extends ActivityEditCard<TheGymCard> {
	@Override
	public int getEditorLayout() {
		return R.layout.card_thegym_setup;
	}

	@Override
	public void createNewCard() {
		setCard(new TheGymCard());
	}

	@Override
	public void setCardData() {
		TheGymCard card = getCard();

		EditText ed = (EditText) findViewById(R.id.pin);
		card.pin = ed.getText().toString();

		ed = (EditText) findViewById(R.id.email);
		card.email = ed.getText().toString();
	}
}
