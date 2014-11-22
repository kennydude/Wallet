package me.kennydude.wallet.medicine;

import android.view.LayoutInflater;
import android.view.View;

import java.util.List;

import me.kennydude.wallet.ActivityEditCard;
import me.kennydude.wallet.ActivityViewCard;
import me.kennydude.wallet.Card;
import me.kennydude.wallet.R;

/**
 * @author kennydude
 */
public class MedicineUsageCard extends Card {

	public String person;
	public List<Medicine> medicinesTaken;

	public static class Medicine{
		public String name;
		public int hourPerioid, timesDaily;
		public int[] lastTakenTimes;
	}

	@Override
	public View getCardView(LayoutInflater inflater) {
		return null;
	}

	@Override
	public boolean refreshData() throws Exception {
		// There is nothing to refresh
		return true;
	}

	@Override
	public Class<? extends ActivityEditCard> getEditActivity() {
		return null;
	}

	@Override
	public Class<? extends ActivityViewCard> getViewActivity() {
		return null;
	}

	@Override
	public int getName() {
		return R.string.medicine_card;
	}
}
