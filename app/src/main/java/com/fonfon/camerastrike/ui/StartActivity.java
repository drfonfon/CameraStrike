package com.fonfon.camerastrike.ui;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.fonfon.camerastrike.App;
import com.fonfon.camerastrike.R;
import com.fonfon.camerastrike.databinding.ActivityStartBinding;
import com.fonfon.camerastrike.lib.GameType;
import com.fonfon.camerastrike.ui.game.GameActivity;
import com.fonfon.camerastrike.ui.scan.ScanActivity;

public final class StartActivity extends FullScreenActivity implements View.OnClickListener {

    private ActivityStartBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_start);
        content = binding.content;

        binding.loginText.setOnClickListener(this);
        binding.trainingBtn.setOnClickListener(this);
        binding.createMatch.setOnClickListener(this);
        binding.connect.setOnClickListener(this);
        binding.generate.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        App.getInstance().gameType = GameType.None;
        App.getInstance().nodename = null;
        if (App.getInstance().code == null) {
            binding.loginText.setText(R.string.scan_you_code);
        } else {
            binding.loginText.setText(App.getInstance().code);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding.unbind();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.generate:
                startActivity(new Intent(StartActivity.this, GenerateCodeActivity.class));
                break;
            case R.id.loginText:
                startActivity(new Intent(StartActivity.this, ScanActivity.class));
                break;
            case R.id.training_btn:
                App.getInstance().gameType = GameType.None;
                startActivity(new Intent(StartActivity.this, GameActivity.class));
                break;
            case R.id.create_match:
                if(App.getInstance().code != null) {
                    App.getInstance().gameType = GameType.Match;
                    startActivity(new Intent(StartActivity.this, CreateMatchActivity.class));
                } else {
                    Toast.makeText(StartActivity.this, R.string.first_authorize, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.connect:
                if(App.getInstance().code != null) {
                    App.getInstance().gameType = GameType.Match;
                    startActivity(new Intent(StartActivity.this, ScanActivity.class));
                } else {
                    Toast.makeText(StartActivity.this, R.string.first_authorize, Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
