package com.fonfon.camerastrike.camera;

import android.databinding.ObservableField;
import android.graphics.PointF;
import android.graphics.RectF;

import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.Tracker;

abstract class AimTracker<T> extends Tracker<T> {

    private ObservableField<String> activeAim;
    private boolean passive = true;
    private RectF rect;
    private PointF center;
    String rawValue = "";

    private void update() {
        if (rect != null && rect.contains(center.x, center.y)) {
            if (passive) {
                activeAim.set(rawValue);
                passive = false;
            }
        } else {
            if(!passive) {
                activeAim.set(null);
                passive = true;
            }
        }
    }

    void setRect(RectF rect) {
        this.rect = rect;
        update();
    }

    public abstract void updateItem(T item);

    AimTracker(PointF center, ObservableField<String> activeAim) {
        this.activeAim = activeAim;
        this.center = center;
    }

    @Override
    public void onNewItem(int id, T item) {
        updateItem(item);
    }

    @Override
    public void onUpdate(Detector.Detections<T> detectionResults, T item) {
        updateItem(item);
    }

    @Override
    public void onMissing(Detector.Detections<T> detectionResults) {
        updateItem(null);
    }

    @Override
    public void onDone() {
        updateItem(null);
    }
}
