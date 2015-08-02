package com.codebutchery.mandalawallpaper;

import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.service.wallpaper.WallpaperService;
import android.view.SurfaceHolder;

/**
 * Created by Francesco Rigoni on 22/05/2015.
 * https://codebutchery.wordpress.com/
 */
public class MandalaWallpaperService extends WallpaperService {

    private class MandalaEngine extends Engine implements SharedPreferences.OnSharedPreferenceChangeListener {

        private SharedPreferences mSharedPreferences;

        private Mandala mMandala;

        private Handler mAnimationHandler;

        private int mWidth;
        private int mHeight;

        private static final long FRAMES_PER_SECOND = 60;
        private static final long FRAME_FREQUENCY_MS = 1000 / FRAMES_PER_SECOND;

        private Runnable mAnimationRunnable = new Runnable() {
            @Override
            public void run() {

                long timestampAtStartMs = System.currentTimeMillis();

                SurfaceHolder holder = getSurfaceHolder();
                Canvas canvas = holder.lockCanvas();
                if (canvas == null) return;

                mMandala.drawNextStep(canvas);

                holder.unlockCanvasAndPost(canvas);

                long timestampAtEndMs = System.currentTimeMillis();
                long elapsedTimeMs = timestampAtEndMs - timestampAtStartMs;
                long nextFrameDelayMs = 0;

                nextFrameDelayMs = elapsedTimeMs % FRAME_FREQUENCY_MS;

               // Log.e("---", "regular: " + FRAME_FREQUENCY_MS + " elapsedTimeMs: " + elapsedTimeMs + " nextFrameDelayMs: " + nextFrameDelayMs);

                mAnimationHandler.postDelayed(this, nextFrameDelayMs);
            }
        };

        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);

            mMandala = new Mandala();

            mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(MandalaWallpaperService.this);
            mSharedPreferences.registerOnSharedPreferenceChangeListener(this);

            mAnimationHandler = new Handler();
        }

        @Override
        public void onDestroy() {

            mSharedPreferences.unregisterOnSharedPreferenceChangeListener(this);

            super.onDestroy();
        }

        private void configureMandala() {

            String cellsAmountPref = mSharedPreferences.getString(getString(R.string.pref_cells_amount_key), getString(R.string.pref_cells_amount_normal));
            Mandala.CellsAmount cellsAmount = Mandala.CellsAmount.NORMAL;

            // Configure cells amount
            if (cellsAmountPref.equals(getString(R.string.pref_cells_amount_few))) cellsAmount = Mandala.CellsAmount.FEW;
            else if (cellsAmountPref.equals(getString(R.string.pref_cells_amount_value_normal))) cellsAmount = Mandala.CellsAmount.NORMAL;
            else if (cellsAmountPref.equals(getString(R.string.pref_cells_amount_value_more))) cellsAmount = Mandala.CellsAmount.MORE;
            else if (cellsAmountPref.equals(getString(R.string.pref_cells_amount_value_even_more))) cellsAmount = Mandala.CellsAmount.EVEN_MORE;
            else if (cellsAmountPref.equals(getString(R.string.pref_cells_amount_value_extreme))) cellsAmount = Mandala.CellsAmount.EXTREME;

            String cellsSpacingPref = mSharedPreferences.getString(getString(R.string.pref_spacing_key), getString(R.string.pref_spacing_normal));
            Mandala.CellsSpacing cellsSpacing = Mandala.CellsSpacing.NORMAL;

            // Configure cells spacing
            if (cellsSpacingPref.equals(getString(R.string.pref_spacing_value_nospacing))) cellsSpacing = Mandala.CellsSpacing.NO_SPACE;
            if (cellsSpacingPref.equals(getString(R.string.pref_spacing_value_small))) cellsSpacing = Mandala.CellsSpacing.SMALL;
            if (cellsSpacingPref.equals(getString(R.string.pref_spacing_value_normal))) cellsSpacing = Mandala.CellsSpacing.NORMAL;
            if (cellsSpacingPref.equals(getString(R.string.pref_spacing_value_large))) cellsSpacing = Mandala.CellsSpacing.LARGE;
            if (cellsSpacingPref.equals(getString(R.string.pref_spacing_value_extreme))) cellsSpacing = Mandala.CellsSpacing.EXTREME;

            mMandala.init(mWidth, mHeight, cellsAmount, cellsSpacing);

            // Configure theme
            String themePref = mSharedPreferences.getString(getString(R.string.pref_theme_key), getString(R.string.pref_theme_automatic));
            if (themePref.equals(getString(R.string.pref_theme_value_ruby_red))) mMandala.setTheme(Mandala.ColorTheme.RUBY_RED);
            if (themePref.equals(getString(R.string.pref_theme_value_barley_corn))) mMandala.setTheme(Mandala.ColorTheme.BARLEY_CORN);
            if (themePref.equals(getString(R.string.pref_theme_value_silver_tree))) mMandala.setTheme(Mandala.ColorTheme.SILVER_TREE);
            if (themePref.equals(getString(R.string.pref_theme_value_grassy_green))) mMandala.setTheme(Mandala.ColorTheme.GRASSY_GREEN);
            if (themePref.equals(getString(R.string.pref_theme_value_ship_cove))) mMandala.setTheme(Mandala.ColorTheme.SHIP_COVE);
            if (themePref.equals(getString(R.string.pref_theme_value_indigo))) mMandala.setTheme(Mandala.ColorTheme.INDIGO);
            if (themePref.equals(getString(R.string.pref_theme_value_vodoo_violet))) mMandala.setTheme(Mandala.ColorTheme.VODOO_VIOLET);
            if (themePref.equals(getString(R.string.pref_theme_value_vivid_violet))) mMandala.setTheme(Mandala.ColorTheme.VIVID_VIOLET);
            if (themePref.equals(getString(R.string.pref_theme_automatic))) mMandala.setAutomaticTheme(true);

            // Configure shape
            String shapePref = mSharedPreferences.getString(getString(R.string.pref_shape_key), getString(R.string.pref_shape_value_squares));
            if (shapePref.equals(getString(R.string.pref_shape_value_circles))) {
                mMandala.setShape(Mandala.Shape.SHAPE_CIRCLE);
            } else if (shapePref.equals(getString(R.string.pref_shape_value_squares))) {
                mMandala.setShape(Mandala.Shape.SHAPE_SQUARE);
            }

            // Configure fill shape
            Boolean fillShapesPref = mSharedPreferences.getBoolean(getString(R.string.pref_fill_shapes_key), true);
            mMandala.setFillShapes(fillShapesPref);

            // Configure cells size
            Boolean randomCellsSize = mSharedPreferences.getBoolean(getString(R.string.pref_random_cell_size_key), false);
            mMandala.setRandomCellsSize(randomCellsSize);

        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            if (visible) {
                mAnimationHandler.post(mAnimationRunnable);
            } else {
                mAnimationHandler.removeCallbacks(mAnimationRunnable);
            }
        }

        @Override
        public void onSurfaceDestroyed(SurfaceHolder holder) {
            super.onSurfaceDestroyed(holder);
            mAnimationHandler.removeCallbacks(mAnimationRunnable);
        }

        @Override
        public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            mWidth = width;
            mHeight = height;

            configureMandala();

            super.onSurfaceChanged(holder, format, width, height);
        }


        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
            configureMandala();
        }

    }


    @Override
    public Engine onCreateEngine() {
        return new MandalaEngine();
    }


}
