package me.kennydude.wallet.nectar;

import android.widget.EditText;

import me.kennydude.wallet.ActivityEditCard;
import me.kennydude.wallet.R;

/**
 * @author kennydude
 */
public class EditNectarCard extends ActivityEditCard<NectarCard> {
	@Override
	public int getEditorLayout() {
		return R.layout.card_nectar_setup;
	}

	@Override
	public void createNewCard() {
		setCard(new NectarCard());
	}

	@Override
	public void setCardData() {
		NectarCard card = getCard();

		EditText ed = (EditText) findViewById(R.id.number);
		card.cardNumber = ed.getText().toString();

		ed = (EditText) findViewById(R.id.surname);
		card.surname = ed.getText().toString();

		ed = (EditText) findViewById(R.id.postcode);
		card.postcode = ed.getText().toString();
	}
}
