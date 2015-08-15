package com.codebutchery.mandalawallpaper;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Francesco Rigoni on 22/05/2015.
 * https://codebutchery.wordpress.com/
 */
public class ConfigurationActivity extends Activity implements MandalaEngine.FPSListener {

    public static class PFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // Load the preferences from XML resource
            addPreferencesFromResource(R.xml.mandala_prefs);
        }
    }

    private WallpaperPreviewView mWpvPreview;
    private TextView mTvFPS;

    private int mLastReceivedFPS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_configuration);

        mWpvPreview = (WallpaperPreviewView) findViewById(R.id.wpvPreview);
        mTvFPS = (TextView) findViewById(R.id.tvFPS);

        mWpvPreview.setFPSListener(this);

        findViewById(R.id.btDone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mLastReceivedFPS < MandalaEngine.DESIRED_FRAMES_PER_SECOND) {
                    new AlertDialog.Builder(ConfigurationActivity.this)
                            .setTitle(R.string.warning)
                            .setMessage(getString(R.string.warning_settings_too_heavy, (int)MandalaEngine.DESIRED_FRAMES_PER_SECOND))
                            .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            })
                            .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();

                } else {
                    finish();
                }
            }
        });

        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.mainLayout, new PFragment());
        ft.commit();
    }

    @Override
    public void onFPSUpdate(int fps) {

        mLastReceivedFPS = fps;

        String htmlColorStart = "<font color=\"";
        String htmlColorEnd = "</font>";

        if (fps < MandalaEngine.DESIRED_FRAMES_PER_SECOND) {
            htmlColorStart += "#E3A58A";
        }
        else {
            htmlColorStart += "#FFFFFF";
        }

        htmlColorStart += "\">";

        mTvFPS.setText(Html.fromHtml("FPS <b>" + htmlColorStart + fps + htmlColorEnd + "</b>"));
    }
}
