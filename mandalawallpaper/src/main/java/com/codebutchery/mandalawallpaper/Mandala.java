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

    public enum Shape {
        SHAPE_SQUARE,
        SHAPE_CIRCLE
    }

    public enum CellsAmount {
        FEW,
        NORMAL,
        MORE,
        EVEN_MORE,
        EXTREME
    }

    public enum CellsSpacing {
        NO_SPACE,
        SMALL,
        NORMAL,
        LARGE,
        EXTREME
    }

    public enum ColorTheme {
        RUBY_RED,
        BARLEY_CORN,
        GRASSY_GREEN,
        SILVER_TREE,
        SHIP_COVE,
        INDIGO,
        VODOO_VIOLET,
        VIVID_VIOLET,
        AUTOMATIC
    }

    private Shape mShape = Shape.SHAPE_SQUARE;
    private CellsAmount mCellsAmount = CellsAmount.NORMAL;
    private CellsSpacing mCellsSpacing = CellsSpacing.NORMAL;

    private boolean mFillShapes;

    private boolean mRandomCellsSize;

    private float mCellsSpacingPx;

    private float mCellSizePx;

    // Number of rows
    private int mRowsCount = 0;

    // Number of columns
    private int mColsCount = 0;

    // Two matrices, used alternatively in order to avoid
    // a copy operation of the whole matrix every time
    private short[][] mMatrixOne = null;
    private short[][] mMatrixTwo = null;

    // Possible effects filters
    int[] mEffect01 = new int[]{-1, +1, -1, -1, -1, -1, +1, +1, -1, +1};
    int[] mEffect02 = new int[]{-1, +1, -1, -1, +1, +1, -1, -1, -1, +1};
    int[] mEffect03 = new int[]{+1, -1, +1, -1, 0, +1, +1, 0, -1, +1};
    int[] mEffect04 = new int[]{-1, +1, +1, -1, -1, -1, -1, +1, -1, -1};

    // Currently displayed filter
    int[] mCurrentEffect = null;

    private int mColorHueMain;
    private boolean mAutomaticTheme;

    private float mInitialCanvasX = 0;
    private float mInitialCanvasY = 0;

    private Paint mPaint = new Paint();

    public void setShape(Shape shape) {
        mShape = shape;
    }

    public void setFillShapes(boolean fillShapes) {
        mFillShapes = fillShapes;
    }

    public void setRandomCellsSize(boolean randomCellsSize) {
        mRandomCellsSize = randomCellsSize;
    }

    public void setTheme(ColorTheme theme) {

        mAutomaticTheme = false;

        switch (theme) {
            case AUTOMATIC:
                mAutomaticTheme = true;
                break;
            case RUBY_RED:
                mColorHueMain = 0;
                break;
            case BARLEY_CORN:
                mColorHueMain = 45;
                break;
            case GRASSY_GREEN:
                mColorHueMain = 90;
                break;
            case SILVER_TREE:
                mColorHueMain = 145;
                break;
            case SHIP_COVE:
                mColorHueMain = 200;
                break;
            case INDIGO:
                mColorHueMain = 245;
                break;
            case VODOO_VIOLET:
                mColorHueMain = 290;
                break;
            case VIVID_VIOLET:
                mColorHueMain = 335;
                break;
        }
    }

    public void init(int width, int height, CellsAmount amount, CellsSpacing spacing) {

        mCellsAmount = amount;
        mCellsSpacing = spacing;

        if (mCellsAmount == CellsAmount.FEW) mCellSizePx = width / 4;
        else if (mCellsAmount == CellsAmount.NORMAL) mCellSizePx = width / 6;
        else if (mCellsAmount == CellsAmount.MORE) mCellSizePx = width / 12;
        else if (mCellsAmount == CellsAmount.EVEN_MORE) mCellSizePx = width / 18;
        else if (mCellsAmount == CellsAmount.EXTREME) mCellSizePx = width / 24;

        if (mCellsSpacing == CellsSpacing.NO_SPACE) mCellsSpacingPx = 0;
        if (mCellsSpacing == CellsSpacing.SMALL) mCellsSpacingPx = (int) (mCellSizePx * 0.05);
        if (mCellsSpacing == CellsSpacing.NORMAL) mCellsSpacingPx = (int) (mCellSizePx * 0.15);
        if (mCellsSpacing == CellsSpacing.LARGE) mCellsSpacingPx = (int) (mCellSizePx * 0.25);
        if (mCellsSpacing == CellsSpacing.EXTREME) mCellsSpacingPx = (int) (mCellSizePx * 0.40);

        mCellSizePx -= mCellsSpacingPx;

        mPaint.setAntiAlias(true);

        mRowsCount = (int) ((height / (mCellSizePx + mCellsSpacingPx)) + 1);
        mColsCount = (int) ((width / (mCellSizePx + mCellsSpacingPx)) + 1);

        float realWidth = mColsCount * (mCellSizePx + mCellsSpacingPx);
        float realHeight = mRowsCount * (mCellSizePx + mCellsSpacingPx);

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

                if (mFillShapes) mPaint.setStyle(Paint.Style.FILL);
                else {
                    mPaint.setStyle(Paint.Style.STROKE);
                    // Different stroke sizes
                    mPaint.setStrokeWidth((float) (mCellsSpacingPx * sin));
                }

                float[] hsv = new float[]{(float) mColorHueMain, 0.7f, 0.5f};
                int color = Color.HSVToColor(hsv);

                // Convert to RGB
                int colorRGB = Color.rgb((int) (Color.red(color) * sin),
                        (int) (Color.green(color) * sin),
                        (int) (Color.blue(color) * sin));

                mPaint.setColor(colorRGB);

                float radius = (mCellSizePx / 2.0f);

                // Different sizes
                if (mRandomCellsSize) radius *= (1 - sin);

                float halfCellsSpacing = (mCellsSpacingPx / 2);
                float cellSizePlusSpacing = (mCellSizePx + mCellsSpacingPx);

                float top = halfCellsSpacing + (row - 1) * cellSizePlusSpacing;
                float bottom = top + mCellSizePx;
                float left = halfCellsSpacing + (col - 1) * cellSizePlusSpacing;
                float right = left + mCellSizePx;

                if (mShape == Shape.SHAPE_SQUARE) {
                    canvas.drawRect(left, top, right, bottom, mPaint);
                } else {
                    canvas.drawCircle(left + mCellSizePx / 2.0f, top + mCellSizePx / 2.0f, radius, mPaint);
                }
            }
        }

        // Swap matrices
        short[][] temp = mMatrixOne;
        mMatrixOne = mMatrixTwo;
        mMatrixTwo = temp;

        if (mAutomaticTheme) {
            mColorHueMain++;
            mColorHueMain %= 360;
        }
    }
}
