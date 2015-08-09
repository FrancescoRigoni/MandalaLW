package com.codebutchery.mandalawallpaper;

import android.app.Activity;
import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by francesco on 02/08/15.
 */
public class MandalaWallpaperIntroActivity extends Activity {

    private Button mBtMoreInfo;
    private Button mBtSetWallpaper;
    private TextView mTvThanks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_intro);

        mBtMoreInfo = (Button) findViewById(R.id.btMoreInfos);
        mBtSetWallpaper = (Button) findViewById(R.id.btSetWallpaper);
        mTvThanks = (TextView) findViewById(R.id.tvThanks);

        mTvThanks.setText(Html.fromHtml(getString(R.string.thanks_for_installing)));

        mBtSetWallpaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER);
                intent.putExtra(WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT,
                        new ComponentName(MandalaWallpaperIntroActivity.this, MandalaWallpaperService.class));
                startActivity(intent);

                finish();

            }
        });

        mBtMoreInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.codebutchery_page_url)));
                startActivity(browserIntent);
            }
        });
    }
}
