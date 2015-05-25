package com.codebutchery.mandalawallpaper;

import android.graphics.Canvas;
import android.os.Handler;
import android.service.wallpaper.WallpaperService;
import android.view.SurfaceHolder;

/**
 * Created by Francesco Rigoni on 22/05/2015.
 * https://codebutchery.wordpress.com/
 */
public class MandalaWallpaperService extends WallpaperService {

    private class MandalaEngine extends Engine {

        private Mandala mMandala;

        private Handler mAnimationHandler;
        private Runnable mAnimationRunnable = new Runnable() {
            @Override
            public void run() {

                SurfaceHolder holder = getSurfaceHolder();
                Canvas canvas = holder.lockCanvas();
                if (canvas == null) return;

                mMandala.drawNextStep(canvas);

                holder.unlockCanvasAndPost(canvas);

                mAnimationHandler.postDelayed(this, 50);
            }
        };

        public MandalaEngine() {

            mMandala = new Mandala();
            mAnimationHandler = new Handler();

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
        public void onSurfaceChanged(SurfaceHolder holder, int format,
                                     int width, int height) {
            mMandala.init(width, height);
            super.onSurfaceChanged(holder, format, width, height);
        }

    }


    @Override
    public Engine onCreateEngine() {
        return new MandalaEngine();
    }


}
