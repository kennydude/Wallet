package me.kennydude.wallet.nectar;

import android.os.Bundle;
import android.widget.TextView;

import me.kennydude.wallet.ActivityViewCard;
import me.kennydude.wallet.R;

/**
 * @author kennydude
 */
public class ViewNectarCard extends ActivityViewCard<NectarCard> {

	@Override
	public void showCard(){

		setContentView(R.layout.card_nectar_view);

		TextView v = (TextView) findViewById(R.id.text);

		String x = getString(R.string.nectar_details);
		x = x.replace("{points}", getCard().totalPoints+"");
		x = x.replace("{worth}", getCard().monetaryValue);
		v.setText(x);
	}

}
