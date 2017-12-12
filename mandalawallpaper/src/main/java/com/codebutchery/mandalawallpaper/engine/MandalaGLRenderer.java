package com.codebutchery.mandalawallpaper.engine;

import android.graphics.Rect;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.util.Log;

public class MandalaGLRenderer implements GLSurfaceView.Renderer {
    /**
     * This is a rectangular screen made up of two triangles, drawn using TRIANGLE_STRIP.
     * It covers the whole render surface and provides a surface where the shader can draw
     * their magic.
     */
    private static class Screen {
        interface ScreenCallback {
            int onSetupShaderProgram();
            void onUpdateShaderProgram(int program);
        }

        private FloatBuffer mVertexBuffer;

        final float mScreenCoords[] = {
                    -1f, +1f, 0f,   // Top Left
                    -1f, -1f, 0f,   // Bottom Left
                    +1f, +1f, 0f,   // Top Right
                    +1f, -1f, 0f,   // Bottom Right
                 };

        private int mPositionHandle;
        private ScreenCallback mCallback;
        private int mProgram = GLES20.GL_INVALID_VALUE;

        public Screen(final ScreenCallback callback) {
            mCallback = callback;
            // Initialize vertex byte buffer for shape coordinates
            final ByteBuffer vertexByteBuffer = ByteBuffer.allocateDirect(mScreenCoords.length * 4);
            vertexByteBuffer.order(ByteOrder.nativeOrder());
            mVertexBuffer = vertexByteBuffer.asFloatBuffer();
            mVertexBuffer.put(mScreenCoords);
            mVertexBuffer.position(0);

            mProgram = mCallback.onSetupShaderProgram();
        }

        public void draw() {
            // Add program to OpenGL ES environment
            GLES20.glUseProgram(mProgram);
            // Get handle to vertex shader's position member
            mPositionHandle = GLES20.glGetAttribLocation(mProgram, ShaderContract.Vertex.V_POSITION);
            // Enable a handle to the triangle vertices
            GLES20.glEnableVertexAttribArray(mPositionHandle);
            // Prepare the triangle coordinate data
            GLES20.glVertexAttribPointer(mPositionHandle, 3, GLES20.GL_FLOAT, false,
                    12, mVertexBuffer);
            // Just before drawing the screen we want to update the fragment shader
            mCallback.onUpdateShaderProgram(mProgram);
            // Draw the screen
            GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
            // Disable vertex array
            GLES20.glDisableVertexAttribArray(mPositionHandle);
        }
    }

    final Screen.ScreenCallback mScreenCallback = new Screen.ScreenCallback() {
        @Override
        public int onSetupShaderProgram() {
            final int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, mVertexShaderCode);
            final int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, mFragmentShaderCode);
            // Create empty OpenGL ES Program
            int program = GLES20.glCreateProgram();
            // add the vertex shader to program
            GLES20.glAttachShader(program, vertexShader);
            // add the fragment shader to program
            GLES20.glAttachShader(program, fragmentShader);
            // creates OpenGL ES program executables
            GLES20.glLinkProgram(program);
            return program;
        }

        @Override
        public void onUpdateShaderProgram(int program) {
            mTime = System.currentTimeMillis();
            int timeHandle = GLES20.glGetAttribLocation(program, ShaderContract.Fragment.U_TIME);
            int resolutionHandle = GLES20.glGetAttribLocation(program, ShaderContract.Fragment.U_RESOLUTION);
            GLES20.glUniform1f(timeHandle, mTime);

            float[] resolution = new float[] {mResolution.width(), mResolution.height()};
            GLES20.glUniform2fv(resolutionHandle, 1, resolution, 0);
        }
    };

    /**
     * The GLSL code for the vertex shader, this will always be just a pass through shader,
     * nothing interesting happens here.
     */
    private final String mVertexShaderCode;

    /**
     * The GLSL code for the fragment shader, this is where the magic happens.
     */
    private final String mFragmentShaderCode;

    private Screen mScreen;

    public MandalaGLRenderer(final String vertexCode, final String fragmentCode) {
        mVertexShaderCode = vertexCode;
        mFragmentShaderCode = fragmentCode;
    }

    @Override
    public void onSurfaceCreated(final GL10 gl10, final EGLConfig eglConfig) {
        mScreen = new Screen(mScreenCallback);
        GLES20.glClearColor(1.0f, 1.0f, 0.0f, 1.0f);
    }

    private Rect mResolution = new Rect();
    private long mTime;

    @Override
    public void onSurfaceChanged(final GL10 gl10, final int width, final int height) {
        GLES20.glViewport(0, 0, width, height);
        mResolution.set(0, 0, width, height);
    }

    @Override
    public void onDrawFrame(final GL10 gl10) {
        // Redraw background color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        mScreen.draw();
    }

    private int loadShader(final int type, final String shaderCode){
        int shader = GLES20.glCreateShader(type);
        // Add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);
        return shader;
    }
}
