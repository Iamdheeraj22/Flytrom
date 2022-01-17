package com.flytrom.learning.base;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.databinding.library.baseAdapters.BR;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.Objects;

public class BaseBottomSheetDialog<V extends ViewDataBinding> extends BottomSheetDialog {

    private Context context;
    private V binding;
    private @LayoutRes
    int layoutId;
    private final BaseBottomSheetDialogListener listener;

    public BaseBottomSheetDialog(@NonNull Context context, @LayoutRes int layoutId, BaseBottomSheetDialogListener listener) {
        super(context);
        this.context = context;
        this.layoutId = layoutId;
        this.listener = listener;
    }

    public V getBinding() {
        init();
        return binding;
    }

    private void init() {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (binding == null)
            binding = DataBindingUtil.inflate(Objects.requireNonNull(inflater), layoutId, null, false);
        if (listener != null) binding.setVariable(BR.callback, listener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        setContentView(binding.getRoot());
    }

    public interface BaseBottomSheetDialogListener {
        void onViewClick(View view);
    }
}
