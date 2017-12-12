package com.codebutchery.mandalawallpaper;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.Random;

/**
 * Created by Francesco Rigoni on 9/12/2017.
 * https://codebutchery.wordpress.com/
 */
public class MandalaAlgorithm<T> {
    // Two matrices, used alternatively in order to avoid
    // a copy operation of the whole matrix every time
    private float[][] mMatrixOne = null;
    private float[][] mMatrixTwo = null;

    private final int mWidth;
    private final int mHeight;

    // Currently displayed filter
    float[] mCurrentEffect = new float[]{-1f, +6f, -1f, +6f, +1f, -6f, +1f, +1f, -6f, +1f};

    public MandalaAlgorithm(int width, int height) {
        mMatrixOne = new float[width][height];
        mMatrixTwo = new float[width][height];
        mWidth = width;
        mHeight = height;

//        final Random random = new Random();
//        for (int row = 1; row < mHeight - 1; row++) {
//            for (int col = 1; col < mWidth - 1; col++) {
//                mMatrixOne[row][col] = (byte) random.nextInt();
//            }
//        }
    }

    private int i = 3;
    private int j = 0;

    public float[][] drawNextStep() {
        Random rand = new Random();

        j++;
        if (j > 100) {
            i++;
            j = 0;
        }

        for (int row = 1; row < mHeight - 1; row++) {
            for (int col = 1; col < mWidth - 1; col++) {

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
                avg /= 9.0;

                float before = mMatrixTwo[row][col];


                if (avg < 5)
                    mMatrixTwo[row][col] = (float) (mMatrixOne[row][col] + mCurrentEffect[(i+0)%10]);
                else if (avg < 10)
                    mMatrixTwo[row][col] = (float) (mMatrixOne[row][col] + mCurrentEffect[(i+1)%10]);
                else if (avg < 20)
                    mMatrixTwo[row][col] = (float) (mMatrixOne[row][col] + mCurrentEffect[(i+2)%10]);
                else if (avg < 30)
                    mMatrixTwo[row][col] = (float) (mMatrixOne[row][col] + mCurrentEffect[(i+3)%10]);
                else if (avg < 35)
                    mMatrixTwo[row][col] = (float) (mMatrixOne[row][col] + mCurrentEffect[(i+4)%10]);
                else if (avg < 50)
                    mMatrixTwo[row][col] = (float) (mMatrixOne[row][col] + mCurrentEffect[(i+5)%10]);
                else if (avg < 100)
                    mMatrixTwo[row][col] = (float) (mMatrixOne[row][col] + mCurrentEffect[(i+6)%10]);
                else if (avg < 1500)
                    mMatrixTwo[row][col] = (float) (mMatrixOne[row][col] + mCurrentEffect[(i+7)%10]);
                else if (avg < 1750)
                    mMatrixTwo[row][col] = (float) (mMatrixOne[row][col] + mCurrentEffect[(i+8)%10]);
                else
                    mMatrixTwo[row][col] = (float) (mMatrixOne[row][col] + mCurrentEffect[(i+9)%10]);

//                // From mandala to simple plasma
//                if (Math.abs(before - mMatrixTwo[row][col]) > 10) {
//                    mMatrixTwo[row][col] = before + mCurrentEffect[rand.nextInt(9)];
//                }
            }
        }

//        for (int row = 1; row < mHeight - 1; row++) {
//            for (int col = 1; col < mWidth - 1; col++) {
//
//                int avg = 0;
//                avg += mMatrixTwo[row - 1][col - 1];
//                avg += mMatrixTwo[row - 1][col - 0];
//                avg += mMatrixTwo[row - 1][col + 1];
//                avg += mMatrixTwo[row - 0][col - 1];
//                avg += mMatrixTwo[row - 0][col - 0];
//                avg += mMatrixTwo[row - 0][col + 1];
//                avg += mMatrixTwo[row + 1][col - 1];
//                avg += mMatrixTwo[row + 1][col - 0];
//                avg += mMatrixTwo[row + 1][col + 1];
//                avg /= 9.0;
//
//                mMatrixOne[row][col] = avg;
//            }
//        }

//        for (int row = 1; row < mHeight - 1; row++) {
//            for (int col = 1; col < mWidth - 1; col++) {
//
//                int avg = 0;
//                avg += mMatrixOne[row - 1][col - 1];
//                avg += mMatrixOne[row - 1][col - 0];
//                avg += mMatrixOne[row - 1][col + 1];
//                avg += mMatrixOne[row - 0][col - 1];
//                avg += mMatrixOne[row - 0][col - 0];
//                avg += mMatrixOne[row - 0][col + 1];
//                avg += mMatrixOne[row + 1][col - 1];
//                avg += mMatrixOne[row + 1][col - 0];
//                avg += mMatrixOne[row + 1][col + 1];
//                avg /= 9.0;
//
//                mMatrixTwo[row][col] = avg;
//            }
//        }

        // Swap matrices
        float[][] temp = mMatrixTwo;
        mMatrixTwo = mMatrixOne;
        mMatrixOne = temp;

        return mMatrixOne;
    }
}
