package com.fonfon.camerastrike.lib;

import android.graphics.Bitmap;
import android.graphics.Color;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Writer;
import com.google.zxing.WriterException;
import com.google.zxing.aztec.AztecWriter;
import com.google.zxing.common.BitMatrix;

public class CodeGenerator {

    public static Bitmap generateCode(String data) {
        Writer ean13Writer = new AztecWriter();
        Bitmap mBitmap = null;
        try {
            BitMatrix bm = ean13Writer.encode(data, BarcodeFormat.AZTEC, 512, 512);
            mBitmap = Bitmap.createBitmap(512, 512, Bitmap.Config.ARGB_4444);
            for (int i = 0; i < 512; i++)
                for (int j = 0; j < 512; j++)
                    mBitmap.setPixel(i, j, bm.get(i, j) ? Color.BLACK : Color.WHITE);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return mBitmap;
    }
}
