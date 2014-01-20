package me.kennydude.wallet.subwayuk;

import android.widget.EditText;

import me.kennydude.wallet.ActivityEditCard;
import me.kennydude.wallet.R;

/**
 * @author kennydude
 */
public class EditSubwayCard extends ActivityEditCard<SubwayCard> {
	@Override
	public int getEditorLayout() {
		return R.layout.card_subcard_setup;
	}

	@Override
	public void createNewCard() {
		setCard(new SubwayCard());
	}

	@Override
	public void setCardData() {
		SubwayCard myCard = getCard();

		myCard.username = ((EditText)findViewById(R.id.email)).getText().toString();
		myCard.password = ((EditText)findViewById(R.id.password)).getText().toString();
	}
}
