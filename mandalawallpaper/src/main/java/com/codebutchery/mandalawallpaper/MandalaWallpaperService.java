package com.codebutchery.mandalawallpaper;

import android.service.wallpaper.WallpaperService;
import android.view.SurfaceHolder;

/**
 * Created by Francesco Rigoni on 22/05/2015.
 * https://codebutchery.wordpress.com/
 */
public class MandalaWallpaperService extends WallpaperService {

    public class MandalaEngineWrapper extends Engine {

        private MandalaEngine mMandalaEngine;

        @Override
        public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            if (mMandalaEngine != null) mMandalaEngine.stopAnimating();

            mMandalaEngine = new MandalaEngine(MandalaWallpaperService.this, width, height);
            mMandalaEngine.create(holder);

            super.onSurfaceChanged(holder, format, width, height);
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            if (visible) {
                mMandalaEngine.resumeAnimating();
            } else {
                mMandalaEngine.stopAnimating();
            }
        }

        @Override
        public void onSurfaceDestroyed(SurfaceHolder holder) {
            mMandalaEngine.stopAnimating();

            super.onSurfaceDestroyed(holder);
        }

        @Override
        public void onDestroy() {
            mMandalaEngine.destroy();

            super.onDestroy();
        }
    }

    @Override
    public Engine onCreateEngine() {
        return new MandalaEngineWrapper();
    }

}
