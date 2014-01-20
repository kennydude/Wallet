package me.kennydude.wallet;

import android.os.Bundle;
import android.webkit.WebView;

/**
 * @author kennydude
 */
public class ActivityOpenSource extends BaseActivity {

	@Override
	public void onCreate(Bundle bis){
		super.onCreate(bis);

		WebView wv = new WebView(this);
		setContentView(wv);
		wv.loadUrl("file:///android_res/raw/opensource.html");
	}

}
