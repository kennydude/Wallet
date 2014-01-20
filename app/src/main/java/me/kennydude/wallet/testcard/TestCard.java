package me.kennydude.wallet.testcard;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import me.kennydude.wallet.ActivityEditCard;
import me.kennydude.wallet.ActivityViewCard;
import me.kennydude.wallet.Card;
import me.kennydude.wallet.R;

/**
 * @author kennydude
 */
public class TestCard extends Card {
	@Override
	public View getCardView(LayoutInflater inflater) {
		TextView tv = new TextView(inflater.getContext());
		tv.setText("TEST");
		tv.setBackgroundResource(R.drawable.card_background);
		return tv;
	}

	@Override
	public boolean refreshData() throws Exception {
		return true;
	}

	@Override
	public Class<? extends ActivityEditCard> getEditActivity() {
		return EditTestCard.class;
	}

	@Override
	public Class<? extends ActivityViewCard> getViewActivity() {
		return ViewTestCard.class;
	}
}
