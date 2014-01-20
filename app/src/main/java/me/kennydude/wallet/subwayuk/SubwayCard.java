package me.kennydude.wallet.subwayuk;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.github.kevinsawicki.http.HttpRequest;

import org.json.JSONObject;

import me.kennydude.wallet.ActivityEditCard;
import me.kennydude.wallet.ActivityViewCard;
import me.kennydude.wallet.Card;
import me.kennydude.wallet.R;
import me.kennydude.wallet.Utils;

/**
 * UK Subcard
 *
 * @author kennydude
 */
public class SubwayCard extends Card {
	public static final String SUBWAY_URL = "https://subcard.subway.co.uk/j2ee/servlet/JSONTraderEnquiry;interface=wifi";

	// Details required for subcard
	public String username, password;

	// Cached details required for rendering
	public String totalPoints;

	public String getSubcardNumber() {
		return subcardNumber;
	}

	public String subcardNumber;

	@Override
	public View getCardView(LayoutInflater inflater) {
		View card = inflater.inflate(R.layout.card_subcard_uk, null);
		((TextView)card.findViewById(R.id.points)).setText( totalPoints );
		return card;
	}

	@Override
	public boolean refreshData() throws Exception{
		JSONObject json = new JSONObject();
		json.put("login", username);
		json.put("password", password);
		json.put("programID", 6);

		HttpRequest subcardRequest = HttpRequest.post(SUBWAY_URL)
				.send(json.toString());

		json = new JSONObject(subcardRequest.body());
		if(!json.optString("responseCode").equals( "0" )){
			Utils.debug(json.toString());
			return false; // Request failed!
		}

		totalPoints = json.optJSONObject("traderBalances").optString("loyaltyBalance");
		subcardNumber = json.optString("virtualCard");

		// TODO: Add previous history

		return true;
	}

	@Override
	public Class<? extends ActivityEditCard> getEditActivity() {
		return EditSubwayCard.class;
	}

	@Override
	public Class<? extends ActivityViewCard> getViewActivity() {
		return ViewSubwayCard.class;
	}

}
