package com.fonfon.camerastrike.lib;

import android.databinding.BindingAdapter;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v7.widget.AppCompatImageView;

import com.fonfon.camerastrike.R;

public class BindingAdapters {

    @BindingAdapter({"isActive"})
    public static void setSrcCompat(AppCompatImageView view, boolean isActive) {
        VectorDrawableCompat drawable = VectorDrawableCompat.create(view.getContext().getResources(),
                isActive ? R.drawable.ic_aim_green : R.drawable.ic_aim, view.getContext().getTheme());
        if (drawable != null) {
            view.setImageDrawable(drawable.mutate());
        }
    }
}
