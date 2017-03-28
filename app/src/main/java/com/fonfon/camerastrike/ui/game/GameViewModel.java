package com.fonfon.camerastrike.ui.game;

import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.graphics.PointF;
import android.support.v7.app.AppCompatActivity;

import com.fonfon.camerastrike.R;
import com.fonfon.camerastrike.camera.BarcodeTrackerFactory;
import com.fonfon.camerastrike.camera.FaceTrackerFactory;
import com.fonfon.camerastrike.lib.ShakeDetector;
import com.fonfon.camerastrike.lib.SoundPoolPlayer;
import com.google.android.gms.vision.MultiDetector;
import com.google.android.gms.vision.MultiProcessor;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.android.gms.vision.face.FaceDetector;

public class GameViewModel {

    public ObservableInt hearts = new ObservableInt(0);
    public ObservableInt bullets = new ObservableInt(6);
    public ObservableField<String> activeAim = new ObservableField<>(null);

    private SoundPoolPlayer player;
    AppCompatActivity activity;
    public GameView view;

    private ShakeDetector.ShakeListener detector = null;
    private ShakeDetector shakeDetector;

    GameViewModel(AppCompatActivity appCompatActivity) {
        this.activity = appCompatActivity;
        player = new SoundPoolPlayer();
        player.setSoundList(activity, new int[]{R.raw.revolver_38_shoot, R.raw.revolver_38_lock, R.raw.revolver_38_reload});

        detector = new ShakeDetector.ShakeListener() {
            @Override
            public void onShakeDetected() {
                if (bullets.get() == 0) {
                    bullets.set(6);
                    player.play(R.raw.revolver_38_reload);
                }
            }
        };
        shakeDetector = new ShakeDetector(activity, 10, detector);
        shakeDetector.start();
    }

    public void setView(GameView view) {
        this.view = view;
    }

    void onTap() {
        if (bullets.get() > 0) {
            String raw = activeAim.get();
            if (raw != null) {
                view.bang();
                bang(raw);
            }
            player.play(R.raw.revolver_38_shoot);
            bullets.set(bullets.get() - 1);
        } else {
            player.play(R.raw.revolver_38_lock);
        }
    }

    void onDestroy() {
        if (detector != null) {
            shakeDetector.stop();
            detector = null;
        }
    }

    protected void bang(String code) {
    }

    protected void setDetector(MultiDetector.Builder detectorBuilder) {
    }

    FaceDetector getFaceDetector(PointF center) {
        FaceDetector faceDetector = new FaceDetector.Builder(activity).setMode(FaceDetector.FAST_MODE).build();
        FaceTrackerFactory faceFactory = new FaceTrackerFactory(center, activeAim);
        faceDetector.setProcessor(new MultiProcessor.Builder<>(faceFactory).build());
        return faceDetector;
    }

    BarcodeDetector getBarcodeDetector(PointF center) {
        BarcodeDetector barcodeDetector = new BarcodeDetector.Builder(activity).setBarcodeFormats(Barcode.AZTEC).build();
        BarcodeTrackerFactory barcodeFactory = new BarcodeTrackerFactory(center, activeAim);
        barcodeDetector.setProcessor(new MultiProcessor.Builder<>(barcodeFactory).build());
        return barcodeDetector;
    }

}
