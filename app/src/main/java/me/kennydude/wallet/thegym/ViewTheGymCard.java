package me.kennydude.wallet.thegym;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import me.kennydude.wallet.ActivityViewCard;
import me.kennydude.wallet.CardUtils;
import me.kennydude.wallet.R;

/**
 * @author kennydude
 */
public class ViewTheGymCard extends ActivityViewCard<TheGymCard> {
	GymSessionAdapter gsa;

	public static class GymSessionAdapter extends ArrayAdapter<TheGymCard.GymSession> {

		public GymSessionAdapter(Context cntx){
			super(cntx,0);
		}

		String formatTFloat(float i){
			return String.format("%.2f",i).replace(".", ":");
		}

		String pd(int i){
			String r = i + "";
			if(r.length() == 1) r = "0"+r;
			return r;
		}

		@Override
		public View getView (int position, View convertView, ViewGroup parent){
			if(convertView == null){
				convertView = View.inflate(getContext(), R.layout.card_thegym_session, null);
			}

			TheGymCard.GymSession sess = getItem(position);

			TextView tv = (TextView) convertView.findViewById(R.id.entry);
			tv.setText( formatTFloat(sess.startHour) );

			tv = (TextView) convertView.findViewById(R.id.entry_lbl);
			tv.setText( getContext().getString(R.string.entry).replace("{date}", pd(sess.day) + "/" + pd(sess.month) + "/" + sess.year) );

			tv = (TextView) convertView.findViewById(R.id.exit);
			tv.setText( formatTFloat(sess.endHour) );

			tv = (TextView) convertView.findViewById(R.id.length);
			tv.setText( sess.length );

			return convertView;
		}

	}

	@Override
	public void showCard(){
		ListView lv = new ListView(this);
		lv.setDivider(null);
		setContentView(lv);

		lv.addHeaderView(getCard().getCardView(getLayoutInflater()));

		gsa = new GymSessionAdapter(this);
		if(getCard().usage != null) {
			gsa.addAll(getCard().usage);
		}

		lv.setAdapter(gsa);
		gsa.notifyDataSetInvalidated();
	}

}
