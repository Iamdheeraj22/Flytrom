package com.flytrom.learning.utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.flytrom.learning.R;
import com.flytrom.learning.databinding.DialogProgressAvlBinding;


/**
 * Created by Arvind on 26-07-2017.
 */

public class ProgressDialogAvl extends Dialog {

    DialogProgressAvlBinding binding;
    private Context context;

    public ProgressDialogAvl(@NonNull Context context) {
        // super(context);

        super(context, R.style.Loader);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_progress_avl, null, false);
        setContentView(binding.getRoot());

    }


    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        binding.avl.show();
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        binding.avl.hide();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
