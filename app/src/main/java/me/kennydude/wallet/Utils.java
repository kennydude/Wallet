package me.kennydude.wallet;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.google.zxing.common.BitMatrix;

/**
 * @author kennydude
 */
public class Utils {
	public static final boolean DEBUG = true;

	public static void debug(String s) {
		if(DEBUG){
			Log.d("DEUBG", s);
		}
	}

	// https://github.com/zxing/zxing/blob/master/android/src/com/google/zxing/client/android/encode/QRCodeEncoder.java

	private static final int WHITE = 0xFFFFFFFF;
	private static final int BLACK = 0xFF000000;

	public static Bitmap bitMatrixToBitmap(BitMatrix matrix){
		int width = matrix.getWidth();
		int height = matrix.getHeight();
		int[] pixels = new int[width * height];
		for (int y = 0; y < height; y++) {
			int offset = y * width;
			for (int x = 0; x < width; x++) {
				pixels[offset + x] = matrix.get(x, y) ? BLACK : WHITE;
			}
		}

		Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
		return bitmap;
	}

}
