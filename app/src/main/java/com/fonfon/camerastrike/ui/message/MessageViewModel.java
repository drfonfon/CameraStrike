package com.fonfon.camerastrike.ui.message;

import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;

import com.fonfon.camerastrike.App;
import com.fonfon.camerastrike.lib.FirebaseConstants;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public abstract class MessageViewModel {

    public ObservableField<String> messageText = new ObservableField<>("");
    public ObservableInt backgroundColor = new ObservableInt(Color.WHITE);
    public ObservableInt messageColor = new ObservableInt(Color.BLACK);
    DatabaseReference reference;
    AppCompatActivity activity;

    MessageViewModel(AppCompatActivity activity) {
        this.activity = activity;

        reference = FirebaseDatabase.getInstance().getReference()
                .child(FirebaseConstants.DUELS)
                .child(App.getInstance().nodename);
    }

    abstract void onDestroy();
}
