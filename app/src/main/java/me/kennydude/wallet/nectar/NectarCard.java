package me.kennydude.wallet.nectar;

import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.kevinsawicki.http.HttpRequest;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import me.kennydude.wallet.ActivityEditCard;
import me.kennydude.wallet.ActivityViewCard;
import me.kennydude.wallet.Card;
import me.kennydude.wallet.R;
import me.kennydude.wallet.Utils;

/**
 * @author kennydude
 */
public class NectarCard extends Card {
	public static final String NECTAR_USERAGENT = "SainsburysMobileApp/2.10.0 (Android/ABC100-1; Mobile/3.2.1.0; en_GB; 0012345678912)";
	public static final String NECTAR_ENDPOINT = "https://mobile.nectar.com/nectar-loyalty-api/collectors/";

	public String cardNumber, surname, postcode, monetaryValue;
	public int totalPoints;

	@Override
	public View getCardView(LayoutInflater inflater) {
		View card = inflater.inflate(R.layout.card_generic_points, null);

		((ImageView)card.findViewById(R.id.logo)).setImageResource(R.drawable.nectar_logo);

		TextView x = (TextView) card.findViewById(R.id.points);
		x.setText(totalPoints+"");

		return card;
	}

	@Override
	public boolean refreshData() throws Exception {
		// Shameless yancked from https://code.google.com/p/sainsburys-nectar-api/source/browse/src/com/mobile/nectar/NectarUtils.java
		String authorization = String.format("%s:%s|%s", cardNumber, postcode, surname);
		authorization = Base64.encodeToString(authorization.getBytes(), Base64.DEFAULT);

		SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z");
		String date = sdf.format(new Date());

		// This bit isn't... only the URL and params
		HttpRequest nectar = HttpRequest.get(NECTAR_ENDPOINT + cardNumber)
										.header("User-Agent", NECTAR_USERAGENT)
										.header("Date", date)
										.header("Authorization", "Basic " + authorization);

		// Sample Response: {"name":"Mr Joe Simpson","currency":"GBP","points_balance":460,"monetary_value":"2.30","account_type":"C"}
		//Utils.debug(nectar.body());
		//Utils.debug(nectar.code() +"");
		if(nectar.code() != 200){ return false; }
		Utils.debug("200");

		JSONObject myNectar = new JSONObject(nectar.body());
		this.totalPoints = myNectar.optInt("points_balance");
		this.monetaryValue = myNectar.optString("monetary_value");

		return true;
	}

	@Override
	public Class<? extends ActivityEditCard> getEditActivity() {
		return EditNectarCard.class;
	}

	@Override
	public Class<? extends ActivityViewCard> getViewActivity() {
		return ViewNectarCard.class;
	}

	@Override
	public int getName() {
		return R.string.nectar_card;
	}
}
