package com.fonfon.camerastrike.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.PointF;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;

import com.fonfon.camerastrike.R;
import com.fonfon.camerastrike.camera.CameraSourcePreview;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.vision.CameraSource;

import java.io.IOException;

public class CameraActivity extends FullScreenActivity {

    protected static final int RC_HANDLE_GMS = 9001;
    protected static final int RC_HANDLE_CAMERA_PERM = 2;
    protected PointF center = new PointF(0,0);
    protected CameraSource mCameraSource = null;

    public void checkPermission() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            createCameraSource();
        } else {
            requestCameraPermission();
        }
    }

    protected void showNoCameraPermissionDialog() {
        new AlertDialog.Builder(this).setTitle(getString(R.string.app_name))
                .setCancelable(false)
                .setMessage(R.string.no_camera_permission)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                })
                .show();
    }

    protected void startCameraSource(CameraSourcePreview preview) {
        int code = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(
                getApplicationContext());
        if (code != ConnectionResult.SUCCESS) {
            GoogleApiAvailability.getInstance().getErrorDialog(this, code, RC_HANDLE_GMS).show();
        }

        if (mCameraSource != null) {
            try {
                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                preview.start(mCameraSource, center);
            } catch (IOException e) {
                mCameraSource.release();
                mCameraSource = null;
            }
        }
    }

    protected void requestCameraPermission() {
        final String[] permissions = new String[]{android.Manifest.permission.CAMERA};
        if (!ActivityCompat.shouldShowRequestPermissionRationale(this,
                android.Manifest.permission.CAMERA)) {
            ActivityCompat.requestPermissions(this, permissions, RC_HANDLE_CAMERA_PERM);
            return;
        }

        new AlertDialog.Builder(this).setTitle(getString(R.string.app_name))
                .setCancelable(false)
                .setMessage(R.string.permission_camera_rationale)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ActivityCompat.requestPermissions(CameraActivity.this, permissions,
                                RC_HANDLE_CAMERA_PERM);
                    }
                })
                .show();
    }

    public void createCameraSource() {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode != RC_HANDLE_CAMERA_PERM) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            return;
        }
        if (grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            createCameraSource();
            return;
        }
        showNoCameraPermissionDialog();
    }

}
