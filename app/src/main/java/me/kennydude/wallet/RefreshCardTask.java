package me.kennydude.wallet;

import android.content.Intent;

import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;
import com.roscopeco.ormdroid.Entity;

import static com.roscopeco.ormdroid.Query.eql;

/**
 * @author kennydude
 */
public class RefreshCardTask extends Job {
	public int cardID = 0;

	public RefreshCardTask(int cardID){
		super(new Params(1).requireNetwork());
		this.cardID = cardID;
	}

	@Override
	public void onAdded() {

	}

	@Override
	public void onRun() throws Throwable {
		try{
			CardUtils.StoredCard sc = Entity.query(CardUtils.StoredCard.class).where(eql("id", cardID)).execute();
			if(sc == null) throw new Exception("NULL CARD!!! HELP!!!");

			Card realCard = sc.getCard();
			if(!realCard.refreshData()){
				throw new Exception("Refresh Failed");
			}
			sc.setCard(realCard);
			sc.save();

			WalletApplication.instance.sendBroadcast(new Intent(CardUtils.ACTION_CARD_REFRESHED));
		} catch(Exception e){
			e.printStackTrace();
			throw new Exception("Refresh Failed");
		}
	}

	@Override
	protected void onCancel() {

	}

	@Override
	protected boolean shouldReRunOnThrowable(Throwable throwable) {
		return true;
	}
}
