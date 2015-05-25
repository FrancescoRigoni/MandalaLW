package com.codebutchery.mandalawallpaper;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.Random;

/**
 * Created by Francesco Rigoni on 22/05/2015.
 * https://codebutchery.wordpress.com/
 */
public class Mandala {

    // Size of each square
    private float mSquareSizePx = 0;

    // Horizontal and vertical space between cells
    int mSpacingBetweenCellsPx = 4;

    // Number of rows
    private int mRowsCount = 0;

    // Number of columns
    private int mColsCount = 0;

    // Two matrices, used alternatively in order to avoid
    // a copy operation of the whole matrix every time
    private short[][] mMatrixOne = null;
    private short[][] mMatrixTwo = null;

    // Possible effects filters
    int[] mEffect01 = new int[] {-1, +1, -1, -1, -1, -1, +1, +1, -1, +1};
    int[] mEffect02 = new int[] {-1, +1, -1, -1, +1, +1, -1, -1, -1, +1};
    int[] mEffect03 = new int[] {+1, -1, +1, -1,  0, +1, +1,  0, -1, +1};
    int[] mEffect04 = new int[] {-1, +1, +1, -1, -1, -1, -1, +1, -1, -1};

    // Currently displayed filter
    int[] mCurrentEffect = null;

    // Timestamp of the last effect change
    private long mLastEffectChangedTimestamp = 0;

    // Time duration for each effect, in ms, changes every time
    private long mMsForNextEffect = 1000;

    // Main color Hue, used fpor the base color which changes with time
    int mColorHueMain = 0;

    private float mInitialCanvasX = 0;
    private float mInitialCanvasY = 0;

    private Paint mPaint = new Paint();

    public void init(int width, int height) {

        mPaint.setAntiAlias(true);

        int mCellSize = width / 8;
        mSquareSizePx = (int) (mCellSize * 0.95);
        mSpacingBetweenCellsPx = (int) (mCellSize * 0.05);

        mRowsCount = (int) ((height / (mSquareSizePx + mSpacingBetweenCellsPx)) + 1);
        mColsCount = (int) ((width / (mSquareSizePx + mSpacingBetweenCellsPx)) + 1);

        float realWidth = mColsCount * (mSquareSizePx + mSpacingBetweenCellsPx);
        float realHeight = mRowsCount * (mSquareSizePx + mSpacingBetweenCellsPx);

        mInitialCanvasX = -(realWidth - width) / 2;
        mInitialCanvasY = -(realHeight - height) / 2;

        // Two extra rows
        mRowsCount += 2;
        // Two extra cols
        mColsCount += 2;

        mMatrixOne = new short[mRowsCount][mColsCount];
        mMatrixTwo = new short[mRowsCount][mColsCount];

        mCurrentEffect = mEffect01;

    }

    public void drawNextStep(Canvas canvas) {

        canvas.drawColor(Color.BLACK);

        // Have to change the effect?
        if (System.currentTimeMillis() - mLastEffectChangedTimestamp > mMsForNextEffect) {

            mLastEffectChangedTimestamp = System.currentTimeMillis();
            mMsForNextEffect = 1000 * (new Random().nextInt(6) + 2);

            if (mCurrentEffect == mEffect01) mCurrentEffect = mEffect02;
            else if (mCurrentEffect == mEffect02) mCurrentEffect = mEffect03;
            else if (mCurrentEffect == mEffect03) mCurrentEffect = mEffect04;
            else if (mCurrentEffect == mEffect04) mCurrentEffect = mEffect01;

        }

       // mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStyle(Paint.Style.FILL);

        canvas.translate(mInitialCanvasX, mInitialCanvasY);

        for (int row = 1; row < mRowsCount - 1; row++) {

            for (int col = 1; col < mColsCount - 1; col++) {

                int avg = 0;
                avg += mMatrixOne[row - 1][col - 1];
                avg += mMatrixOne[row - 1][col - 0];
                avg += mMatrixOne[row - 1][col + 1];
                avg += mMatrixOne[row - 0][col - 1];
                avg += mMatrixOne[row - 0][col - 0];
                avg += mMatrixOne[row - 0][col + 1];
                avg += mMatrixOne[row + 1][col - 1];
                avg += mMatrixOne[row + 1][col - 0];
                avg += mMatrixOne[row + 1][col + 1];

                avg /= 9;

                if (avg == 0)
                    mMatrixTwo[row][col] = (short) (mMatrixOne[row][col] + mCurrentEffect[0]);
                else if (avg < 40)
                    mMatrixTwo[row][col] = (short) (mMatrixOne[row][col] + mCurrentEffect[1]);
                else if (avg < 80)
                    mMatrixTwo[row][col] = (short) (mMatrixOne[row][col] + mCurrentEffect[2]);
                else if (avg < 120)
                    mMatrixTwo[row][col] = (short) (mMatrixOne[row][col] + mCurrentEffect[3]);
                else if (avg < 160)
                    mMatrixTwo[row][col] = (short) (mMatrixOne[row][col] + mCurrentEffect[4]);
                else if (avg < 200)
                    mMatrixTwo[row][col] = (short) (mMatrixOne[row][col] + mCurrentEffect[5]);
                else if (avg < 240)
                    mMatrixTwo[row][col] = (short) (mMatrixOne[row][col] + mCurrentEffect[6]);
                else if (avg < 280)
                    mMatrixTwo[row][col] = (short) (mMatrixOne[row][col] + mCurrentEffect[7]);
                else if (avg < 320)
                    mMatrixTwo[row][col] = (short) (mMatrixOne[row][col] + mCurrentEffect[8]);
                else if (avg < 360)
                    mMatrixTwo[row][col] = (short) (mMatrixOne[row][col] + mCurrentEffect[9]);

                mMatrixTwo[row][col] %= 360;

                // Draw
                float angle = mMatrixTwo[row][col];
                double rad = Math.toRadians(angle);
                double sin = Math.abs(Math.sin(rad));

                // Different stroke sizes
                mPaint.setStrokeWidth((float) (4 * sin));

                float[] hsv = new float[]{(float) (mColorHueMain + (180 * sin)), 0.7f, 0.7f};
                int color = Color.HSVToColor(hsv);

                // Different colors shades
                int colorRGB = Color.rgb((int)(Color.red(color) * sin),
                        (int)(Color.green(color) * sin),
                        (int)(Color.blue(color) * sin));

                // Same color shades
//                int colorRGB = Color.rgb((int) (Color.red(color)),
//                        (int) (Color.green(color)),
//                        (int) (Color.blue(color)));

                mPaint.setColor(colorRGB);

                float radius = (float) ((mSquareSizePx / 2.0f));

                // Different sizes
                // radius *= (1 - sin);

                if (radius < 2) radius = 2;

                float top = (mSpacingBetweenCellsPx / 2) + (row - 1) * (mSquareSizePx + mSpacingBetweenCellsPx);
                float bottom = top + mSquareSizePx;
                float left = (mSpacingBetweenCellsPx / 2) + (col - 1) * (mSquareSizePx + mSpacingBetweenCellsPx);
                float right = left + mSquareSizePx;

                {
                    // Rectangles

                    float squareSizeReal = radius * 2;


                    // different sizes
//                top += (mSquareSizePx - squareSizeReal) / 2;
//                left += (mSquareSizePx - squareSizeReal) / 2;
//               bottom = top + squareSizeReal;
//                right = left + squareSizeReal;
//
//
                    canvas.drawRect(left, top, right, bottom, mPaint);

                }

                {
                    // Circles

                    // Different sizes

                    // canvas.drawCircle(left + mSquareSizePx / 2.0f, top + mSquareSizePx / 2.0f, radius, mPaint);
                }
            }

        }

        // Swap matrices
        short[][] temp = mMatrixOne;
        mMatrixOne = mMatrixTwo;
        mMatrixTwo = temp;

        mColorHueMain++;
        mColorHueMain %= 360;

    }

}
