package com.flytrom.learning.fragments.custom_module;

import android.os.Bundle;

import androidx.databinding.library.baseAdapters.BR;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.flytrom.learning.R;
import com.flytrom.learning.base.BaseFragment;
import com.flytrom.learning.base.SimpleRecyclerViewAdapter;
import com.flytrom.learning.beans.normal_beans.SelectModeBean;
import com.flytrom.learning.databinding.FragmentSelectModeBinding;
import com.flytrom.learning.databinding.ItemModeBinding;
import com.flytrom.learning.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class SelectModeFragment extends BaseFragment<FragmentSelectModeBinding> {

    private SimpleRecyclerViewAdapter<SelectModeBean, ItemModeBinding> mModeAdapter;
    public static SelectModeFragment self;
    public int mSelectedIndex = 0;

    @Override
    protected void onFragmentCreateView(Bundle savedInstanceState) {
        super.onFragmentCreateView(savedInstanceState);

        initView();
    }

    @Override
    public int getFragmentLayout() {
        return R.layout.fragment_select_mode;
    }

    private void initView() {
        this.self = this;
        List<SelectModeBean> list = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            SelectModeBean bean = new SelectModeBean(Constants.MODES[i], Constants.allModeDescription[i], i, 0);
            list.add(bean);
        }
        mModeAdapter = new SimpleRecyclerViewAdapter<>(R.layout.item_mode, BR.bean, (v, modeBean) -> {

            mSelectedIndex = modeBean.getIndex();
            for (int i = 0; i < mModeAdapter.getList().size(); i++) {
                mModeAdapter.getList().get(i).setSelected(modeBean.getIndex());
            }
            mModeAdapter.notifyDataSetChanged();
        });
        binding.recyclerModes.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.recyclerModes.setAdapter(mModeAdapter);
        mModeAdapter.setList(list);
    }
}

