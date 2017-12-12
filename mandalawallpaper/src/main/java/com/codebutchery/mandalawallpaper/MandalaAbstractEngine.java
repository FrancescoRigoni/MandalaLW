package com.codebutchery.mandalawallpaper;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.preference.PreferenceManager;
import android.view.SurfaceHolder;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * Created by Francesco Rigoni on 9/12/2017..
 * https://codebutchery.wordpress.com/
 */
public class MandalaAbstractEngine implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String TAG = "MandalaAbstractEngine";

    public static MandalaAlgorithm buildMandala(Context c) {

//        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(c);
//
//        String cellsAmountPref = prefs.getString(c.getString(R.string.pref_cells_amount_key), c.getString(R.string.pref_cells_amount_normal));
//        Mandala.CellsAmount cellsAmount = Mandala.CellsAmount.MORE;
//
//        // Configure cells amount
//        if (cellsAmountPref.equals(c.getString(R.string.pref_cells_amount_value_few))) cellsAmount = Mandala.CellsAmount.FEW;
//        else if (cellsAmountPref.equals(c.getString(R.string.pref_cells_amount_value_normal))) cellsAmount = Mandala.CellsAmount.NORMAL;
//        else if (cellsAmountPref.equals(c.getString(R.string.pref_cells_amount_value_more))) cellsAmount = Mandala.CellsAmount.MORE;
//        else if (cellsAmountPref.equals(c.getString(R.string.pref_cells_amount_value_even_more))) cellsAmount = Mandala.CellsAmount.EVEN_MORE;
//        else if (cellsAmountPref.equals(c.getString(R.string.pref_cells_amount_value_extreme))) cellsAmount = Mandala.CellsAmount.EXTREME;
//
//        String cellsSpacingPref = prefs.getString(c.getString(R.string.pref_spacing_key), c.getString(R.string.pref_spacing_normal));
//        Mandala.CellsSpacing cellsSpacing = Mandala.CellsSpacing.SMALL;
//
//        // Configure cells spacing
//        if (cellsSpacingPref.equals(c.getString(R.string.pref_spacing_value_nospacing))) cellsSpacing = Mandala.CellsSpacing.NO_SPACE;
//        else if (cellsSpacingPref.equals(c.getString(R.string.pref_spacing_value_small))) cellsSpacing = Mandala.CellsSpacing.SMALL;
//        else if (cellsSpacingPref.equals(c.getString(R.string.pref_spacing_value_normal))) cellsSpacing = Mandala.CellsSpacing.NORMAL;
//        else if (cellsSpacingPref.equals(c.getString(R.string.pref_spacing_value_large))) cellsSpacing = Mandala.CellsSpacing.LARGE;
//        else if (cellsSpacingPref.equals(c.getString(R.string.pref_spacing_value_extreme))) cellsSpacing = Mandala.CellsSpacing.EXTREME;
//
//        MandalaAlgorithm mandala = new MandalaAlgorithm();
//        mandala.init(BITMAP_WIDTH, BITMAP_HEIGHT, cellsAmount, cellsSpacing);
//
//        // Configure theme
//        String themePref = prefs.getString(c.getString(R.string.pref_theme_key), c.getString(R.string.pref_theme_value_automatic));
//        if (themePref.equals(c.getString(R.string.pref_theme_value_automatic))) mandala.setTheme(Mandala.ColorTheme.AUTOMATIC);
//        else if (themePref.equals(c.getString(R.string.pref_theme_value_ruby_red))) mandala.setTheme(Mandala.ColorTheme.RUBY_RED);
//        else if (themePref.equals(c.getString(R.string.pref_theme_value_barley_corn))) mandala.setTheme(Mandala.ColorTheme.BARLEY_CORN);
//        else if (themePref.equals(c.getString(R.string.pref_theme_value_silver_tree))) mandala.setTheme(Mandala.ColorTheme.SILVER_TREE);
//        else if (themePref.equals(c.getString(R.string.pref_theme_value_grassy_green))) mandala.setTheme(Mandala.ColorTheme.GRASSY_GREEN);
//        else if (themePref.equals(c.getString(R.string.pref_theme_value_ship_cove))) mandala.setTheme(Mandala.ColorTheme.SHIP_COVE);
//        else if (themePref.equals(c.getString(R.string.pref_theme_value_indigo))) mandala.setTheme(Mandala.ColorTheme.INDIGO);
//        else if (themePref.equals(c.getString(R.string.pref_theme_value_vodoo_violet))) mandala.setTheme(Mandala.ColorTheme.VOODOO_VIOLET);
//        else if (themePref.equals(c.getString(R.string.pref_theme_value_vivid_violet))) mandala.setTheme(Mandala.ColorTheme.VIVID_VIOLET);
//
//        // Configure shape
//        String shapePref = prefs.getString(c.getString(R.string.pref_shape_key), c.getString(R.string.pref_shape_value_squares));
//        if (shapePref.equals(c.getString(R.string.pref_shape_value_circles))) {
//            mandala.setShape(Mandala.Shape.SHAPE_CIRCLE);
//        } else if (shapePref.equals(c.getString(R.string.pref_shape_value_squares))) {
//            mandala.setShape(Mandala.Shape.SHAPE_SQUARE);
//        }
//
//        // Configure fill shape
//        Boolean fillShapesPref = prefs.getBoolean(c.getString(R.string.pref_fill_shapes_key), true);
//        mandala.setFillShapes(fillShapesPref);
//
//        // Configure cells size
//        Boolean randomCellsSize = prefs.getBoolean(c.getString(R.string.pref_random_cell_size_key), false);
//        mandala.setRandomCellsSize(randomCellsSize);

        return null;// mandala;
    }

    private static final int BITMAP_WIDTH = 100;
    private static final int BITMAP_HEIGHT = 100;

    public interface FPSListener {
        void onFPSUpdate(int fps);
    }

    private FPSListener mFPSListener;

    private Context mContext;

    private SharedPreferences mSharedPreferences;
    private MandalaAlgorithm mMandala;

    private long mFPSCounterTimestamp;
    private int mLastFPSCount;

    private Queue<Bitmap> mBitmaps = new ArrayDeque<>(10);

    public static final float DESIRED_FRAMES_PER_SECOND = 60f;
    private static final float FRAME_FREQUENCY_MS = 1000f / DESIRED_FRAMES_PER_SECOND;

    private Runnable mAnimationRunnable = new Runnable() {
        @Override
        public void run() {
            long timestampAtStartMs = System.currentTimeMillis();

            // Needs FPS update?
            long currentTimestamp = System.currentTimeMillis();
            if (currentTimestamp - mFPSCounterTimestamp >= 1000) {
                if (mFPSListener != null) mFPSListener.onFPSUpdate(mLastFPSCount);
                mLastFPSCount = 0;
                mFPSCounterTimestamp = currentTimestamp;
            }

            mLastFPSCount++;

            final Bitmap bitmap = Bitmap.createBitmap(BITMAP_WIDTH, BITMAP_HEIGHT, Bitmap.Config.ALPHA_8);
            final Canvas canvas = new Canvas(bitmap);

            //mMandala.drawNextStep(canvas);

            long timestampAtEndMs = System.currentTimeMillis();
            long elapsedTimeMs = timestampAtEndMs - timestampAtStartMs;
            long nextFrameDelayMs = (long) (FRAME_FREQUENCY_MS - elapsedTimeMs);
        }
    };

    public MandalaAbstractEngine(Context context) {
        mContext = context;
        mMandala = buildMandala(mContext);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        mSharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    public void setFPSListener(FPSListener listener) {
        mFPSListener = listener;
    }

    public void create(SurfaceHolder surfaceHolder) {
        resumeAnimating();
    }

    public void destroy() {
        stopAnimating();
        mSharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
    }

    public void stopAnimating() {
        mFPSCounterTimestamp = 0;
        mLastFPSCount = 0;
    }

    public void resumeAnimating() {
        mFPSCounterTimestamp = System.currentTimeMillis();
        mLastFPSCount = 0;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        mMandala = buildMandala(mContext);
    }

}

