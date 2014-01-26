package me.kennydude.wallet.thegym;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import me.kennydude.wallet.ActivityEditCard;
import me.kennydude.wallet.ActivityViewCard;
import me.kennydude.wallet.Card;
import me.kennydude.wallet.R;

/**
 * Fake Membership card for The Gym Group
 * @author kennydude
 */
public class TheGymCard extends Card {
	private static final String START_URL = "http://www.thegymgroup.com/login/";
	private static final String USAGE_URL = "http://www.thegymgroup.com/my-account/my-usage/";

	public String pin, email;
	public List<GymSession> usage;

	public static class GymSession{
		public String length;

		public int year, month, day;
		public float startHour, endHour;
	}

	@Override
	public View getCardView(LayoutInflater inflater) {
		View card = inflater.inflate(R.layout.card_thegym_card, null);

		// Oh well, it'll fit here! :D
		TextView x = (TextView) card.findViewById(R.id.points);
		x.setText(pin);

		return card;
	}

	@Override
	public boolean refreshData() throws Exception {
		if(email.trim().isEmpty()) return true; // Don't care

		Connection.Response res = Jsoup.connect(START_URL)
				.data("tx_gym_auth_pi[email]", email)
				.data("tx_gym_auth_pi[password]", pin) // odd people
				.data("tx_gym_auth_pi[active]", "1")
				.execute();

		Map<String, String> cookies = res.cookies();

		Document doc = Jsoup.connect(USAGE_URL).cookies(cookies).get();

		Element usage = doc.getElementById("myusagedata");
		if(usage == null) return false; // Error

		Elements myUsage = usage.select("tbody tr");
		this.usage = new ArrayList<GymSession>();
		for(Element visit : myUsage){
			GymSession session = new GymSession();

			SimpleDateFormat sdf = new SimpleDateFormat("d/MM/y HH:mm:ss");

			String time = visit.child(0).text();
			Date x = sdf.parse(time);

			session.year = x.getYear();
			session.month = x.getMonth();
			session.day = x.getDate();

			session.startHour = x.getHours() + ( (float)x.getMinutes() / 100 );

			time = visit.child(1).text();
			x = sdf.parse(time);
			session.endHour = x.getHours() + ( (float)x.getMinutes() / 100 );

			session.length = visit.child(2).text().split(" ")[0];
			this.usage.add(session);
		}

		return true;
	}

	@Override
	public Class<? extends ActivityEditCard> getEditActivity() {
		return EditTheGymCard.class;
	}

	@Override
	public Class<? extends ActivityViewCard> getViewActivity() {
		return ViewTheGymCard.class;
	}

	@Override
	public int getName() {
		return R.string.the_gym;
	}
}
