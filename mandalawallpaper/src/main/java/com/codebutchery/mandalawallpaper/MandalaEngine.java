package com.codebutchery.mandalawallpaper;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.SurfaceHolder;

/**
 * Created by francesco on 09/08/15.
 */
public class MandalaEngine implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String TAG = "MandalaEngine";

    public static Mandala buildMandala(Context c, int width, int height) {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(c);

        String cellsAmountPref = prefs.getString(c.getString(R.string.pref_cells_amount_key), c.getString(R.string.pref_cells_amount_normal));
        Mandala.CellsAmount cellsAmount = Mandala.CellsAmount.MORE;

        // Configure cells amount
        if (cellsAmountPref.equals(c.getString(R.string.pref_cells_amount_few))) cellsAmount = Mandala.CellsAmount.FEW;
        else if (cellsAmountPref.equals(c.getString(R.string.pref_cells_amount_value_normal))) cellsAmount = Mandala.CellsAmount.NORMAL;
        else if (cellsAmountPref.equals(c.getString(R.string.pref_cells_amount_value_more))) cellsAmount = Mandala.CellsAmount.MORE;
        else if (cellsAmountPref.equals(c.getString(R.string.pref_cells_amount_value_even_more))) cellsAmount = Mandala.CellsAmount.EVEN_MORE;
        else if (cellsAmountPref.equals(c.getString(R.string.pref_cells_amount_value_extreme))) cellsAmount = Mandala.CellsAmount.EXTREME;

        String cellsSpacingPref = prefs.getString(c.getString(R.string.pref_spacing_key), c.getString(R.string.pref_spacing_normal));
        Mandala.CellsSpacing cellsSpacing = Mandala.CellsSpacing.SMALL;

        // Configure cells spacing
        if (cellsSpacingPref.equals(c.getString(R.string.pref_spacing_value_nospacing))) cellsSpacing = Mandala.CellsSpacing.NO_SPACE;
        else if (cellsSpacingPref.equals(c.getString(R.string.pref_spacing_value_small))) cellsSpacing = Mandala.CellsSpacing.SMALL;
        else if (cellsSpacingPref.equals(c.getString(R.string.pref_spacing_value_normal))) cellsSpacing = Mandala.CellsSpacing.NORMAL;
        else if (cellsSpacingPref.equals(c.getString(R.string.pref_spacing_value_large))) cellsSpacing = Mandala.CellsSpacing.LARGE;
        else if (cellsSpacingPref.equals(c.getString(R.string.pref_spacing_value_extreme))) cellsSpacing = Mandala.CellsSpacing.EXTREME;

        Mandala mandala = new Mandala();
        mandala.init(width, height, cellsAmount, cellsSpacing);

        // Configure theme
        String themePref = prefs.getString(c.getString(R.string.pref_theme_key), c.getString(R.string.pref_theme_value_automatic));
        if (themePref.equals(c.getString(R.string.pref_theme_value_automatic))) mandala.setTheme(Mandala.ColorTheme.AUTOMATIC);
        else if (themePref.equals(c.getString(R.string.pref_theme_value_ruby_red))) mandala.setTheme(Mandala.ColorTheme.RUBY_RED);
        else if (themePref.equals(c.getString(R.string.pref_theme_value_barley_corn))) mandala.setTheme(Mandala.ColorTheme.BARLEY_CORN);
        else if (themePref.equals(c.getString(R.string.pref_theme_value_silver_tree))) mandala.setTheme(Mandala.ColorTheme.SILVER_TREE);
        else if (themePref.equals(c.getString(R.string.pref_theme_value_grassy_green))) mandala.setTheme(Mandala.ColorTheme.GRASSY_GREEN);
        else if (themePref.equals(c.getString(R.string.pref_theme_value_ship_cove))) mandala.setTheme(Mandala.ColorTheme.SHIP_COVE);
        else if (themePref.equals(c.getString(R.string.pref_theme_value_indigo))) mandala.setTheme(Mandala.ColorTheme.INDIGO);
        else if (themePref.equals(c.getString(R.string.pref_theme_value_vodoo_violet))) mandala.setTheme(Mandala.ColorTheme.VODOO_VIOLET);
        else if (themePref.equals(c.getString(R.string.pref_theme_value_vivid_violet))) mandala.setTheme(Mandala.ColorTheme.VIVID_VIOLET);

        // Configure shape
        String shapePref = prefs.getString(c.getString(R.string.pref_shape_key), c.getString(R.string.pref_shape_value_squares));
        if (shapePref.equals(c.getString(R.string.pref_shape_value_circles))) {
            mandala.setShape(Mandala.Shape.SHAPE_CIRCLE);
        } else if (shapePref.equals(c.getString(R.string.pref_shape_value_squares))) {
            mandala.setShape(Mandala.Shape.SHAPE_SQUARE);
        }

        // Configure fill shape
        Boolean fillShapesPref = prefs.getBoolean(c.getString(R.string.pref_fill_shapes_key), true);
        mandala.setFillShapes(fillShapesPref);

        // Configure cells size
        Boolean randomCellsSize = prefs.getBoolean(c.getString(R.string.pref_random_cell_size_key), false);
        mandala.setRandomCellsSize(randomCellsSize);

        return mandala;
    }

    public interface FPSListener {
        void onFPSUpdate(int fps);
    }

    private FPSListener mFPSListener;

    private Context mContext;
    private SurfaceHolder mSurfaceHolder;

    private SharedPreferences mSharedPreferences;
    private Mandala mMandala;
    private Handler mAnimationHandler;

    private int mWidth;
    private int mHeight;

    private long mFPSCounterTimestamp;
    private int mLastFPS;

    public static final float DESIRED_FRAMES_PER_SECOND = 30f;
    private static final float FRAME_FREQUENCY_MS = 1000f / DESIRED_FRAMES_PER_SECOND;

    private Runnable mAnimationRunnable = new Runnable() {
        @Override
        public void run() {
            long timestampAtStartMs = System.currentTimeMillis();

            // Needs FPS update?
            long currentTimestamp = System.currentTimeMillis();
            if (currentTimestamp - mFPSCounterTimestamp >= 1000) {
                if (mFPSListener != null) mFPSListener.onFPSUpdate(mLastFPS);
                mLastFPS = 0;
                mFPSCounterTimestamp = currentTimestamp;
            }

            mLastFPS++;

            Canvas canvas = mSurfaceHolder.lockCanvas();
            if (canvas == null) {
                Log.e(TAG, "Canvas is null");
                return;
            }
            mMandala.drawNextStep(canvas);

            mSurfaceHolder.unlockCanvasAndPost(canvas);

            long timestampAtEndMs = System.currentTimeMillis();
            long elapsedTimeMs = timestampAtEndMs - timestampAtStartMs;
            long nextFrameDelayMs = (long) (FRAME_FREQUENCY_MS - elapsedTimeMs);

            // Log.e("---", "regular: " + FRAME_FREQUENCY_MS + " elapsedTimeMs: " + elapsedTimeMs + " nextFrameDelayMs: " + nextFrameDelayMs);

            mAnimationHandler.postDelayed(this, nextFrameDelayMs);
        }
    };

    public MandalaEngine(Context context, int width, int height) {
        mWidth = width;
        mHeight = height;
        mContext = context;
        mMandala = buildMandala(mContext, mWidth, mHeight);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        mSharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    public void setFPSListener(FPSListener listener) {
        mFPSListener = listener;
    }

    public void create(SurfaceHolder surfaceHolder) {
        mSurfaceHolder = surfaceHolder;
        mAnimationHandler = new Handler();
        resumeAnimating();
    }

    public void destroy() {
        stopAnimating();
        mSharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
    }

    public void stopAnimating() {
        mAnimationHandler.removeCallbacks(mAnimationRunnable);
        mFPSCounterTimestamp = 0;
        mLastFPS = 0;
    }

    public void resumeAnimating() {
        mAnimationHandler.post(mAnimationRunnable);
        mFPSCounterTimestamp = System.currentTimeMillis();
        mLastFPS = 0;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        mMandala = buildMandala(mContext, mWidth, mHeight);
    }

}
