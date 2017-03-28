package com.fonfon.camerastrike.ui.game;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.fonfon.camerastrike.R;
import com.google.android.gms.vision.MultiDetector;

final class TrainingViewModel extends GameViewModel {

    TrainingViewModel(AppCompatActivity activity) {
        super(activity);
    }

    @Override
    public void setView(GameView view) {
        super.setView(view);
        hearts.set(0);

        new AlertDialog.Builder(activity)
                .setTitle(activity.getTitle())
                .setMessage(R.string.training_message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    @Override
    protected void setDetector(MultiDetector.Builder detectorBuilder) {
        super.setDetector(detectorBuilder);
        view.setFaceDetector(detectorBuilder);
        view.setBarcodeDetector(detectorBuilder);
    }
}
