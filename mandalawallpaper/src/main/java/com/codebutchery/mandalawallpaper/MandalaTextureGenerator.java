package com.codebutchery.mandalawallpaper;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import java.util.Random;

/**
 * Created by Francesco Rigoni on 9/12/2017.
 * https://codebutchery.wordpress.com/
 */
public class MandalaTextureGenerator {

    final Paint mPaint = new Paint();

    public MandalaTextureGenerator() {
    }

    public Bitmap generateFrom(final float[][] matrix, final int width, final int height) {
        final Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(bitmap);

        float max = 0;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < width; y++) {
                if (matrix[x][y] > max) {
                    max = matrix[x][y];
                }

                float current = matrix[x][y];//(float) (matrix[x][y] / max);
                if (current > .5) {
                    mPaint.setColor(Color.argb((int) (current * 255), 255, 0, 180));
                } else {
                    mPaint.setColor(Color.argb((int) (current * 255), 180, 0, 255));
                }

                canvas.drawPoint(x, y, mPaint);
            }
        }
        return bitmap;
    }
}
