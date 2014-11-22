package me.kennydude.wallet.testcard;

import me.kennydude.wallet.ActivityViewCard;
import me.kennydude.wallet.R;

/**
 * @author kennydude
 */
public class ViewTestCard extends ActivityViewCard {
	@Override
	public void showCard() {
		setContentView(R.layout.card_test_setup);
	}

	// Does nothing
	// This card is purely for testing!

}
