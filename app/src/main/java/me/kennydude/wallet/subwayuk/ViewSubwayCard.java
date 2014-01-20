package me.kennydude.wallet.subwayuk;

import android.graphics.Bitmap;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.aztec.AztecWriter;
import com.google.zxing.common.BitMatrix;

import me.kennydude.wallet.ActivityViewBarcode;
import me.kennydude.wallet.ActivityViewCard;
import me.kennydude.wallet.Utils;

/**
 * @author kennydude
 */
public class ViewSubwayCard extends ActivityViewBarcode<SubwayCard> {
	@Override
	public Bitmap getBarcode(int width, int height) {
		// Make a writer
		AztecWriter writer = new AztecWriter();

		// Square to the smallest element
		Utils.debug(width + "x" + height);
		int x = Math.min(width, height);

		// Make the barcode
		BitMatrix matrix = writer.encode( getCard().getSubcardNumber(), BarcodeFormat.AZTEC, x, x );

		// Convert it to bitmap
		return Utils.bitMatrixToBitmap(matrix);
	}
}
