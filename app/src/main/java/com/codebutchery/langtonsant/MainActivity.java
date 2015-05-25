package com.codebutchery.langtonsant;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends Activity implements MandalaView.MandalaViewFPSListener {

    private MandalaView mMandalaView;
    private TextView mTvFPS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMandalaView = (MandalaView) findViewById(R.id.mandalaView);
        mTvFPS = (TextView) findViewById(R.id.tvFPS);

        mMandalaView.setMandalaViewFPSListener(this);

    }


    @Override
    public void onFPSValue(int fps) {
        mTvFPS.setText("FPS: " + fps);
    }
}
