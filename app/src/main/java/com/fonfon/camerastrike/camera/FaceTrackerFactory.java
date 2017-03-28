package com.fonfon.camerastrike.camera;

import android.databinding.ObservableField;
import android.graphics.PointF;
import android.graphics.RectF;

import com.google.android.gms.vision.MultiProcessor;
import com.google.android.gms.vision.Tracker;
import com.google.android.gms.vision.face.Face;

public final class FaceTrackerFactory implements MultiProcessor.Factory<Face> {

    private PointF center;
    private ObservableField<String> activeAim;

    public FaceTrackerFactory(PointF center, ObservableField<String> activeAim) {
        this.center = center;
        this.activeAim = activeAim;
    }

    @Override
    public Tracker<Face> create(Face face) {
        return new AimTracker<Face>(center, activeAim) {
            @Override
            public void updateItem(Face item) {
                if (item != null) {
                    setRect(new RectF(
                            item.getPosition().x,
                            item.getPosition().y,
                            item.getPosition().x + item.getWidth(),
                            item.getPosition().y + item.getHeight())
                    );
                } else {
                    setRect(null);
                }
            }
        };
    }
}