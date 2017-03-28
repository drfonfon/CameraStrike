package com.fonfon.camerastrike.ui;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.fonfon.camerastrike.R;
import com.fonfon.camerastrike.databinding.ActivityGenerateCodeBinding;
import com.fonfon.camerastrike.lib.CodeGenerator;
import com.fonfon.camerastrike.lib.FileUtils;

import java.io.ByteArrayOutputStream;

public class GenerateCodeActivity extends AppCompatActivity {

    protected static final int RC_HANDLE_WRITE_PERM = 2;
    private ActivityGenerateCodeBinding binding;
    private String code;
    private Bitmap codeBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_generate_code);

        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.name.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    hideKeyboard(GenerateCodeActivity.this);
                    code = binding.name.getText().toString().trim();
                    if (code.length() > 0) {
                        showImage();
                    }
                    return true;
                }
                return false;
            }
        });

        binding.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (codeBitmap != null) {
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    codeBitmap.compress(Bitmap.CompressFormat.PNG, 0 , bos);
                    byte[] bitmapdata = bos.toByteArray();
                    FileUtils.saveImageFile(bitmapdata, code);
                } else {
                    Toast.makeText(GenerateCodeActivity.this, "No image", Toast.LENGTH_SHORT).show();
                }
            }
        });

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermission();
        }
    }

    private void showImage() {
        binding.code.setImageBitmap(null);
        binding.progress.setVisibility(View.VISIBLE);
        binding.code.postDelayed(new Runnable() {
            @Override
            public void run() {
                codeBitmap = new AsyncTaskLoader<Bitmap>(GenerateCodeActivity.this){
                    @Override
                    public Bitmap loadInBackground() {
                        return CodeGenerator.generateCode(code);
                    }
                }.loadInBackground();
                binding.code.setImageBitmap(codeBitmap);
                binding.progress.setVisibility(View.INVISIBLE);
            }
        }, 300);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding.unbind();
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    protected void requestPermission() {
        final String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (!ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            ActivityCompat.requestPermissions(this, permissions, RC_HANDLE_WRITE_PERM);
            return;
        }

        new AlertDialog.Builder(this).setTitle(getString(R.string.app_name))
                .setCancelable(false)
                .setMessage(R.string.permission_write_rationale)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ActivityCompat.requestPermissions(GenerateCodeActivity.this, permissions,
                                RC_HANDLE_WRITE_PERM);
                    }
                })
                .show();
    }
}
