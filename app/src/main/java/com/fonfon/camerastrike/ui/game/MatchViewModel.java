package com.fonfon.camerastrike.ui.game;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import com.fonfon.camerastrike.App;
import com.fonfon.camerastrike.lib.FirebaseConstants;
import com.fonfon.camerastrike.lib.GameType;
import com.fonfon.camerastrike.ui.message.MessageActivity;
import com.google.android.gms.vision.MultiDetector;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

final class MatchViewModel extends GameViewModel {

    private DatabaseReference reference;
    private boolean isAlive = true;

    private ValueEventListener lifeListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            hearts.set(((Long) dataSnapshot.getValue()).intValue());
            if (isAlive && hearts.get() <= 0) {
                isAlive = false;
                finishGame();
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    private ValueEventListener statusListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if ((Long) dataSnapshot.getValue() == 0)
                finishGame();
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    MatchViewModel(AppCompatActivity appCompatActivity) {
        super(appCompatActivity);
        hearts.set(8);
        reference = FirebaseDatabase.getInstance().getReference()
                .child(FirebaseConstants.DUELS).child(App.getInstance().nodename);
        reference.child(App.getInstance().code).addValueEventListener(lifeListener);
        reference.child(FirebaseConstants.STATUS).addValueEventListener(statusListener);
    }

    @Override
    void onDestroy() {
        super.onDestroy();
        reference.child(App.getInstance().code).removeEventListener(lifeListener);
        reference.child(FirebaseConstants.STATUS).removeEventListener(statusListener);
    }

    @Override
    protected void bang(final String code) {
        super.bang(code);

        reference.child(code).runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Long lives = (Long) mutableData.getValue();
                if (lives == null) {
                    return Transaction.success(mutableData);
                }
                mutableData.setValue(lives - 1);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {

            }
        });
    }

    @Override
    protected void setDetector(MultiDetector.Builder detectorBuilder) {
        super.setDetector(detectorBuilder);
        view.setBarcodeDetector(detectorBuilder);
    }

    private void finishGame() {
        App.getInstance().gameType = GameType.MatchEnd;
        activity.startActivity(
                new Intent(activity, MessageActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK)
        );
        activity.finish();
    }
}
