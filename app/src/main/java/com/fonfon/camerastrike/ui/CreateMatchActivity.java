package com.fonfon.camerastrike.ui;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.AsyncTaskLoader;
import android.view.View;
import android.widget.Toast;

import com.fonfon.camerastrike.App;
import com.fonfon.camerastrike.R;
import com.fonfon.camerastrike.databinding.ActivityCreateMatchBinding;
import com.fonfon.camerastrike.lib.CodeGenerator;
import com.fonfon.camerastrike.lib.FirebaseConstants;
import com.fonfon.camerastrike.ui.game.GameActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

public class CreateMatchActivity extends FullScreenActivity implements View.OnClickListener {

    private ActivityCreateMatchBinding binding;
    private DatabaseReference reference;
    private ValueEventListener connectionsListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            long count = dataSnapshot.getChildrenCount() - 1;
            binding.start.setEnabled(count > 1);
            binding.connect.setText(String.format(Locale.getDefault(), getString(R.string.connected), count));
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_match);
        content = binding.content;
        reference = FirebaseDatabase.getInstance().getReference().child(FirebaseConstants.DUELS);

        reference.push().child(FirebaseConstants.STATUS).setValue(-1, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError != null) {
                    finish();
                    Toast.makeText(CreateMatchActivity.this, databaseError.toString(), Toast.LENGTH_SHORT).show();
                }
                App.getInstance().nodename = databaseReference.getParent().getKey();
                showBarcode(App.getInstance().nodename);
                reference.child(App.getInstance().nodename)
                        .child(App.getInstance().code)
                        .setValue(FirebaseConstants.MAX_LIVES);
                listenConnections();
            }
        });
        binding.start.setOnClickListener(this);
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (App.getInstance().nodename != null) {
                    reference.child(App.getInstance().nodename).removeValue();
                }
                finish();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding.unbind();
        reference.removeEventListener(connectionsListener);
    }

    private void listenConnections() {
        reference.child(App.getInstance().nodename).addValueEventListener(connectionsListener);
    }

    private void showBarcode(final String key) {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap =new AsyncTaskLoader<Bitmap>(CreateMatchActivity.this){
                    @Override
                    public Bitmap loadInBackground() {
                        return CodeGenerator.generateCode(key);
                    }
                }.loadInBackground();
                binding.code.setImageBitmap(bitmap);
                binding.progress.setVisibility(View.GONE);
                binding.start.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onClick(View v) {
        reference.child(App.getInstance().nodename).child(FirebaseConstants.STATUS).setValue(1,
                new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        if (databaseError != null) {
                            finish();
                            Toast.makeText(CreateMatchActivity.this, databaseError.toString(), Toast.LENGTH_SHORT).show();
                        }
                        startActivity(
                                new Intent(CreateMatchActivity.this, GameActivity.class)
                                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK)
                        );
                        finish();
                    }
                });
    }
}
