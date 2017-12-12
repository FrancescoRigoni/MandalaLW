package com.codebutchery.mandalawallpaper.development;

import android.graphics.Bitmap;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.codebutchery.mandalawallpaper.MandalaAlgorithm;
import com.codebutchery.mandalawallpaper.engine.MandalaGLRenderer;
import com.codebutchery.mandalawallpaper.MandalaTextureGenerator;
import com.codebutchery.mandalawallpaper.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class DevelopmentActivity extends Activity {

    private FrameLayout mGLViewContainer;
    private GLSurfaceView mGLView;
    private ImageView mImageView;

    private MandalaGLRenderer mRenderer;

    private final MandalaAlgorithm mAlgorithm = new MandalaAlgorithm(100, 100);
    private final MandalaTextureGenerator mGenerator = new MandalaTextureGenerator();
    private final Handler mRender = new Handler(Looper.getMainLooper());

    private Runnable mRenderRunnable = new Runnable() {
        @Override
        public void run() {
            final Bitmap bitmap = mGenerator.generateFrom(mAlgorithm.drawNextStep(), 100, 100);
            mImageView.setImageBitmap(bitmap);
            mRender.postDelayed(mRenderRunnable, 10);
        }
    };

    private String getVertexProgramCode() {
        try {
            InputStream is = getResources().openRawResource(R.raw.vertex);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuffer data = new StringBuffer();
            String line = reader.readLine();
            while (line != null) {
                data.append(line).append("\n");
                line = reader.readLine();
            }
            return data.toString();
        } catch (final IOException e) {
            throw new RuntimeException("Cannot load vertex shader");
        } finally {
            // TODO close the stream
        }
    }

    private String getFragmentProgramCode() {
        try {
            InputStream is = getResources().openRawResource(R.raw.fragment);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuffer data = new StringBuffer();
            String line = reader.readLine();
            while (line != null) {
                data.append(line).append("\n");
                line = reader.readLine();
            }
            return data.toString();
        } catch (final IOException e) {
            throw new RuntimeException("Cannot load vertex shader");
        } finally {
            // TODO close the stream
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_development);

        mRenderer = new MandalaGLRenderer(getVertexProgramCode(), getFragmentProgramCode());
        mGLView = new GLSurfaceView(this);
        mGLView.setEGLContextClientVersion(2);
        mGLView.setRenderer(mRenderer);

        mGLViewContainer = (FrameLayout) findViewById(R.id.surfaceViewContainer);
        mGLViewContainer.addView(mGLView);

        mImageView = (ImageView) findViewById(R.id.imageView);
        mRender.postDelayed(mRenderRunnable, 10);
    }

}
