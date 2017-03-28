package com.fonfon.camerastrike.ui.scan;

import android.content.Intent;
import android.databinding.ObservableField;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;

import com.fonfon.camerastrike.App;
import com.fonfon.camerastrike.lib.GameType;
import com.fonfon.camerastrike.R;
import com.fonfon.camerastrike.ui.message.MessageActivity;
import com.google.android.gms.vision.barcode.Barcode;

public class ScanViewModel {

    private static final int SCAN_YOU_CODE_RES = R.string.scan_you_code;
    private static final int SCAN_MATCH_RES = R.string.match_scan_text;
    public ObservableField<String> message = new ObservableField<>("");
    private AppCompatActivity activity;

    ScanViewModel(AppCompatActivity activity) {
        this.activity = activity;
        if (App.getInstance().gameType == GameType.Match) {
            message.set(activity.getString(SCAN_MATCH_RES));
        } else {
            message.set(activity.getString(SCAN_YOU_CODE_RES));
        }
    }

    void onReceive(SparseArray items) {
        if (App.getInstance().gameType == GameType.Match) {
            onMatch(((Barcode) items.valueAt(0)).rawValue);
        } else {
            onAuthorization(((Barcode) items.valueAt(0)).rawValue);
        }
    }

    private void onAuthorization(String text) {
        App.getInstance().code = text;
        activity.finish();
    }

    private void onMatch(String text) {
        App.getInstance().nodename = text;
        activity.startActivity(new Intent(activity, MessageActivity.class));
        activity.finish();
    }
}
