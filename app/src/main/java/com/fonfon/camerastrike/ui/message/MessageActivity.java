package com.fonfon.camerastrike.ui.message;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.fonfon.camerastrike.App;
import com.fonfon.camerastrike.ui.FullScreenActivity;
import com.fonfon.camerastrike.lib.GameType;
import com.fonfon.camerastrike.R;
import com.fonfon.camerastrike.databinding.ActivityMessageBinding;

public final class MessageActivity extends FullScreenActivity {

    private MessageViewModel viewModel;
    private ActivityMessageBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_message);
        content = binding.layoutBackground;

        if(App.getInstance().gameType == GameType.Match) {
            viewModel = new NewMatchViewModel(this);
        } else {
            viewModel = new EndMatchViewModel(this);
        }
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        binding.setPresenter(viewModel);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding.unbind();
        viewModel.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        App.getInstance().gameType = GameType.None;
    }
}
