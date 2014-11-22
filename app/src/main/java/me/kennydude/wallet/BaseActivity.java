package me.kennydude.wallet;

import android.app.Activity;
import android.app.ActivityManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

/**
 * @author kennydude
 */
public class BaseActivity extends ActionBarActivity {

	@Override
	public void onCreate(Bundle bis){
		super.onCreate(bis);
		//getActionBar().setIcon(R.drawable.ic_app);
		if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ){
			LollipopUtils.setTaskDescription(this);
		}
	}

}
