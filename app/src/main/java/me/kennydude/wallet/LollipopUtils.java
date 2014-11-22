package me.kennydude.wallet;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by kennydude on 19/11/14.
 */
@TargetApi(21)
public class LollipopUtils {
	public static void setTaskDescription(Activity a) {
		Bitmap icon = BitmapFactory.decodeResource(a.getResources(),
				R.drawable.ic_app);

		a.setTaskDescription(
				new ActivityManager.TaskDescription(
					a.getString(R.string.app_name),
					icon,
					a.getResources().getColor(R.color.HoloOrange)
				)
		);
	}
}
