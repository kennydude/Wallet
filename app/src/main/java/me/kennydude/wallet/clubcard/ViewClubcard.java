package me.kennydude.wallet.clubcard;

import android.graphics.Bitmap;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.aztec.AztecWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.Code128Writer;

import me.kennydude.wallet.ActivityViewBarcode;
import me.kennydude.wallet.ActivityViewCard;
import me.kennydude.wallet.Utils;

/**
 * @author kennydude
 */
public class ViewClubcard extends ActivityViewBarcode<TescoClubcard> {
	@Override
	public Bitmap getBarcode(int width, int height) {
		// Make a writer
		Code128Writer writer = new Code128Writer();

		// Square to the smallest element
		Utils.debug(width + "x" + height);
		int x = Math.min(width, height);

		// Make the barcode
		try{
			BitMatrix matrix = writer.encode( getCard().clubcardNumber, BarcodeFormat.CODE_128, x, x );

			// Convert it to bitmap
			return Utils.bitMatrixToBitmap(matrix);
		} catch (Exception e){
			e.printStackTrace();
		}

		return null;
	}
}
