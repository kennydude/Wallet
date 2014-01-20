package me.kennydude.wallet.testcard;

import me.kennydude.wallet.ActivityEditCard;
import me.kennydude.wallet.R;

/**
 * @author kennydude
 */
public class EditTestCard extends ActivityEditCard {
	@Override
	public int getEditorLayout() {
		return R.layout.card_test_setup;
	}

	@Override
	public void createNewCard() {
		setCard(new TestCard());
	}

	@Override
	public void setCardData() {
		// No data!
	}
}
