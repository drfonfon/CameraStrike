package com.fonfon.camerastrike.ui.game;

import com.google.android.gms.vision.MultiDetector;

interface GameView {
    void bang();

    void setFaceDetector(MultiDetector.Builder detectorBuilder);
    void setBarcodeDetector(MultiDetector.Builder detectorBuilder);
}
