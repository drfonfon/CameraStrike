package com.fonfon.camerastrike.ui.scan;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;

import com.fonfon.camerastrike.ui.CameraActivity;
import com.fonfon.camerastrike.R;
import com.fonfon.camerastrike.databinding.ActivityScanBinding;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

public final class ScanActivity extends CameraActivity implements Detector.Processor<Barcode> {

    private ScanViewModel viewModel;
    private ActivityScanBinding binding;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_scan);
        content = binding.topLayout;
        viewModel = new ScanViewModel(this);
        binding.setPresenter(viewModel);

        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        checkPermission();
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
        if (mCameraSource != null) {
            mCameraSource.release();
        }
        binding.unbind();
    }

    public void createCameraSource() {
        super.createCameraSource();
        BarcodeDetector barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.AZTEC).build();
        barcodeDetector.setProcessor(this);

        mCameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setFacing(CameraSource.CAMERA_FACING_BACK)
                .setRequestedFps(30.0f)
                .build();
    }

    @Override
    public void release() {

    }

    @Override
    public void receiveDetections(Detector.Detections<Barcode> detections) {
        SparseArray items = detections.getDetectedItems();
        if (items.size() != 0) {
            viewModel.onReceive(items);
        }
    }
}
