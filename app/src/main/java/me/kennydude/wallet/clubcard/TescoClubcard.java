package me.kennydude.wallet.clubcard;

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
 * @author kennydude
 */
public class TescoClubcard extends Card {
	private static final String CLUBCARD_URL = "http://mobile.tesco.com/oneapp/restservice.aspx";

	public String clubcardNumber;
	public String postcode;

	public int points;

	@Override
	public View getCardView(LayoutInflater inflater) {
		View card = inflater.inflate(R.layout.card_clubcard_view, null);
		if(postcode.isEmpty()){
			// TODO: Better view!
			((TextView)card.findViewById(R.id.points)).setText( "todo" );
		} else{
			((TextView)card.findViewById(R.id.points)).setText( points + "" );
		}
		return card;
	}

	@Override
	public boolean refreshData() throws Exception {
		if(postcode.isEmpty()){
			points = -1;
			return true;
		}

		HttpRequest clubcardReq = HttpRequest.get(CLUBCARD_URL,
				true,
				"COMMAND", "CLUBCARDPOINTS",
				"clubcardnumber", clubcardNumber,
				"postcode", postcode);

		JSONObject json = new JSONObject(clubcardReq.body());
		if(!json.optString("StatusCode").equals( "0" )){
			Utils.debug(json.toString());
			return false; // Request failed!
		}

		points = json.optInt("ClubcardPoints");

		return true;
	}

	@Override
	public Class<? extends ActivityEditCard> getEditActivity() {
		return EditClubcard.class;
	}

	@Override
	public Class<? extends ActivityViewCard> getViewActivity() {
		return ViewClubcard.class;
	}
}
