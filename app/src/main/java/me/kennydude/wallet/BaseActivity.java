package me.kennydude.wallet;

import android.app.Activity;
import android.os.Bundle;

/**
 * @author kennydude
 */
public class BaseActivity extends Activity {

	@Override
	public void onCreate(Bundle bis){
		super.onCreate(bis);

		getActionBar().setIcon(R.drawable.ic_app);
	}

}
