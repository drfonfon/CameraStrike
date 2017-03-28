package com.fonfon.camerastrike.ui.message;

import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.fonfon.camerastrike.App;
import com.fonfon.camerastrike.lib.FirebaseConstants;
import com.fonfon.camerastrike.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

final class EndMatchViewModel extends MessageViewModel {

    private static final int deathRef = R.string.you_died;
    private static final int aliveRef = R.string.you_alive;
    private static final int deathColorRef = R.color.ured;
    private static final int aliveColorRef = R.color.ugreen;

    EndMatchViewModel(final AppCompatActivity activity) {
        super(activity);

        reference.child(App.getInstance().code).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                messageColor.set(Color.WHITE);
                if ((Long)dataSnapshot.getValue() > 0) {
                    messageText.set(activity.getString(aliveRef));
                    backgroundColor.set(ContextCompat.getColor(activity, aliveColorRef));
                    reference.removeValue();
                } else {
                    messageText.set(activity.getString(deathRef));
                    backgroundColor.set(ContextCompat.getColor(activity, deathColorRef));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        reference.child(FirebaseConstants.STATUS).runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Long val = (Long) mutableData.getValue();
                if (val == null) {
                    return Transaction.success(mutableData);
                }
                mutableData.setValue(val - 1);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {

            }
        });
    }

    @Override
    void onDestroy() {

    }
}
