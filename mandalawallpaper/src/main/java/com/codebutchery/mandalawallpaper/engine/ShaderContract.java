package com.codebutchery.mandalawallpaper.engine;

public interface ShaderContract {

    interface Vertex {
        String V_POSITION = "vPosition";
    }

    interface Fragment {
        String U_TEXTURE = "uTexture";
        String U_TIME = "uTime";
        String U_RESOLUTION = "uResolution";
    }
}
