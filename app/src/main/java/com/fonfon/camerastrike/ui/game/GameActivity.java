package com.fonfon.camerastrike.ui.game;

import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.fonfon.camerastrike.App;
import com.fonfon.camerastrike.ui.CameraActivity;
import com.fonfon.camerastrike.lib.GameType;
import com.fonfon.camerastrike.R;
import com.fonfon.camerastrike.databinding.ActivityGameBinding;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.MultiDetector;

public final class GameActivity extends CameraActivity implements GameView {

    private GameViewModel viewModel;
    private ActivityGameBinding binding;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_game);
        content = binding.topLayout;
        if(App.getInstance().gameType == GameType.Match) {
            viewModel = new MatchViewModel(this);
        } else {
            viewModel = new TrainingViewModel(this);
        }

        binding.setPresenter(viewModel);
        viewModel.setView(this);

        checkPermission();
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        if (e.getAction() == MotionEvent.ACTION_DOWN)
            viewModel.onTap();
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        startCameraSource(binding.preview);
    }

    @Override
    protected void onPause() {
        super.onPause();
        binding.preview.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewModel.onDestroy();
        if (mCameraSource != null) {
            mCameraSource.release();
        }
        binding.unbind();
    }

    public void createCameraSource() {
        super.createCameraSource();
        MultiDetector.Builder detectorBuilder = new MultiDetector.Builder();
        viewModel.setDetector(detectorBuilder);
        MultiDetector multiDetector = detectorBuilder.build();

        if (!multiDetector.isOperational()) {
            IntentFilter lowstorageFilter = new IntentFilter(Intent.ACTION_DEVICE_STORAGE_LOW);
            boolean hasLowStorage = registerReceiver(null, lowstorageFilter) != null;
            if (hasLowStorage) {
                Toast.makeText(this, R.string.low_storage_error, Toast.LENGTH_LONG).show();
                finish();
            }
        }

        mCameraSource = new CameraSource.Builder(getApplicationContext(), multiDetector)
                .setFacing(CameraSource.CAMERA_FACING_BACK)
                .setRequestedFps(60.0f)
                .build();
    }

    @Override
    public void bang() {
        binding.bang.setVisibility(View.VISIBLE);
        binding.bang.postDelayed(new Runnable() {
            @Override
            public void run() {
                binding.bang.setVisibility(View.GONE);
            }
        }, 100);
    }

    @Override
    public void setFaceDetector(MultiDetector.Builder detectorBuilder) {
        detectorBuilder.add(viewModel.getFaceDetector(center));
    }

    @Override
    public void setBarcodeDetector(MultiDetector.Builder detectorBuilder) {
        detectorBuilder.add(viewModel.getBarcodeDetector(center));
    }
}
