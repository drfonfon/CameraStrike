package com.fonfon.camerastrike.ui;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class FullScreenActivity extends AppCompatActivity {

    protected View content;

    @Override
    protected void onResume() {
        super.onResume();
        hideBars();
    }

    @SuppressLint("InlinedApi")
    protected void hideBars() {
        if (content != null) {
            content.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    }
}
