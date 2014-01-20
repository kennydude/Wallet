package me.kennydude.wallet;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ImageView;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

/**
 * @author kennydude
 */
public abstract class ActivityViewBarcode<T extends Card> extends ActivityViewCard<T> {

	ImageView barcode;

	@Override
	public void onCreate(Bundle bis){
		super.onCreate(bis);

		// Make the screen full bright for this activity.
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.screenBrightness = 1.0f;

		getWindow().setAttributes(lp);

		setContentView(R.layout.activity_barcode);
		barcode = (ImageView) findViewById(R.id.image);
	}

	ViewTreeObserver.OnPreDrawListener opdl = new ViewTreeObserver.OnPreDrawListener() {
		public boolean onPreDraw() {
			new Thread(new Runnable() {

				@Override
				public void run() {

					// This may take a second or two
					final Bitmap bmp = getBarcode( barcode.getMeasuredWidth(), barcode.getMeasuredHeight() );

					if(bmp == null){
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								Crouton.makeText(ActivityViewBarcode.this, getString(R.string.problem_with_barcode), Style.ALERT).show();
							}
						});
					}

					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							barcode.setImageBitmap(bmp);
						}
					});

				}

			}).start();

			// Never run again!
			ViewTreeObserver vto = barcode.getViewTreeObserver();
			vto.removeOnPreDrawListener(opdl);

			return true;
		}
	};

	@Override
	public void onResume(){
		super.onResume();
		ViewTreeObserver vto = barcode.getViewTreeObserver();
		vto.addOnPreDrawListener(opdl);
	}

	public abstract Bitmap getBarcode(int width, int height);

}
