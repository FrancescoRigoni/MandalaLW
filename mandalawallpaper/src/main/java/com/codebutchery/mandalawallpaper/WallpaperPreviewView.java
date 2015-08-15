package com.codebutchery.mandalawallpaper;

import android.content.Context;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

/**
 * Created by Francesco Rigoni on 22/05/2015.
 * https://codebutchery.wordpress.com/
 */
public class WallpaperPreviewView extends SurfaceView implements SurfaceHolder.Callback, MandalaEngine.FPSListener {

    private MandalaEngine mMandalaEngine;
    private MandalaEngine.FPSListener mFPSListener;

    public WallpaperPreviewView(Context context) {
        super(context);
        init();
    }

    public WallpaperPreviewView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WallpaperPreviewView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setFPSListener(MandalaEngine.FPSListener listener) {
        mFPSListener = listener;
    }

    private void init() {
        getHolder().addCallback(this);
    }

    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        onWindowVisibilityChanged(visibility);
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);

        if (mMandalaEngine != null) {
            if (visibility == View.VISIBLE) mMandalaEngine.resumeAnimating();
            else mMandalaEngine.stopAnimating();
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        mMandalaEngine = new MandalaEngine(getContext(), width, height);
        mMandalaEngine.create(holder);
        mMandalaEngine.setFPSListener(this);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mMandalaEngine.stopAnimating();
    }

    @Override
    public void onFPSUpdate(int fps) {
        if (mFPSListener != null) mFPSListener.onFPSUpdate(fps);
    }
}
