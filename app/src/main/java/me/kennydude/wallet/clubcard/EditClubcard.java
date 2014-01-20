package me.kennydude.wallet.clubcard;

import android.widget.EditText;

import me.kennydude.wallet.ActivityEditCard;
import me.kennydude.wallet.R;

/**
 * @author kennydude
 */
public class EditClubcard extends ActivityEditCard<TescoClubcard> {
	@Override
	public int getEditorLayout() {
		return R.layout.card_clubcard_setup;
	}

	@Override
	public void createNewCard() {
		setCard(new TescoClubcard());
	}

	@Override
	public void setCardData() {
		TescoClubcard myCard = getCard();

		myCard.clubcardNumber = ((EditText)findViewById(R.id.number)).getText().toString();
		myCard.postcode = ((EditText)findViewById(R.id.postcode)).getText().toString();
	}
}
