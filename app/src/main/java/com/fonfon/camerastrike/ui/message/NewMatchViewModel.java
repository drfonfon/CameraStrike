package com.fonfon.camerastrike.ui.message;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import com.fonfon.camerastrike.App;
import com.fonfon.camerastrike.lib.FirebaseConstants;
import com.fonfon.camerastrike.R;
import com.fonfon.camerastrike.ui.game.GameActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

final class NewMatchViewModel extends MessageViewModel implements ValueEventListener {

    NewMatchViewModel(AppCompatActivity activity) {
        super(activity);
        messageText.set(activity.getString(R.string.wait_opponents));
        reference.child(App.getInstance().code).setValue(FirebaseConstants.MAX_LIVES);
        reference.child(FirebaseConstants.STATUS).addValueEventListener(this);
    }

    @Override
    void onDestroy() {
        reference.child(FirebaseConstants.STATUS).removeEventListener(this);
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        if (dataSnapshot.getValue() != null && (Long) dataSnapshot.getValue() > 0) {
            activity.startActivity(
                    new Intent(activity, GameActivity.class)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK)
            );
            activity.finish();
        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
