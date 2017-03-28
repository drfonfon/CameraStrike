package com.fonfon.camerastrike.camera;

import android.databinding.ObservableField;
import android.graphics.PointF;
import android.graphics.RectF;

import com.google.android.gms.vision.MultiProcessor;
import com.google.android.gms.vision.Tracker;
import com.google.android.gms.vision.barcode.Barcode;

public final class BarcodeTrackerFactory implements MultiProcessor.Factory<Barcode> {

    private PointF center;
    private ObservableField<String> activeAim;

    public BarcodeTrackerFactory(PointF center, ObservableField<String> activeAim) {
        this.center = center;
        this.activeAim = activeAim;
    }

    @Override
    public Tracker<Barcode> create(Barcode barcode) {
        return new AimTracker<Barcode>(center, activeAim) {
            @Override
            public void updateItem(Barcode item) {
                if (item != null) {
                    rawValue = item.rawValue;
                    setRect(new RectF(item.getBoundingBox()));
                } else {
                    rawValue = "";
                    setRect(null);
                }
            }
        };
    }
}