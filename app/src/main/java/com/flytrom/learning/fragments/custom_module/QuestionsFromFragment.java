package com.flytrom.learning.fragments.custom_module;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.SeekBar;

import androidx.databinding.library.baseAdapters.BR;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.flytrom.learning.R;
import com.flytrom.learning.base.BaseCallback;
import com.flytrom.learning.base.BaseFragment;
import com.flytrom.learning.base.SimpleRecyclerViewAdapter;
import com.flytrom.learning.beans.normal_beans.SubscribePlansBean;
import com.flytrom.learning.databinding.FragmentQuestionsFromBinding;
import com.flytrom.learning.databinding.ItemQuestionFromBinding;
import com.flytrom.learning.utils.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class QuestionsFromFragment extends BaseFragment<FragmentQuestionsFromBinding> {

    private SimpleRecyclerViewAdapter<SubscribePlansBean, ItemQuestionFromBinding> mQuestionsFromAdapter;
    @SuppressLint("StaticFieldLeak")
    public static QuestionsFromFragment self;
    public int mQuestionsFrom = 0;
    public int mDifficultyLevel = 0;
    public int mNoOfQuestions = 10;

    public QuestionsFromFragment() {
        // Required empty public constructor
    }

    @Override
    protected void onFragmentCreateView(Bundle savedInstanceState) {
        super.onFragmentCreateView(savedInstanceState);

        initView();
    }

    @Override
    public int getFragmentLayout() {
        return R.layout.fragment_questions_from;
    }

    private void initView() {
        this.self = this;
        setBaseCallback(baseCallback);
        List<SubscribePlansBean> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            SubscribePlansBean bean = new SubscribePlansBean(Constants.QUESTIONS_FROM[i], "", i, false);
            list.add(bean);
            if (i == 0) list.get(i).setSelected(true);
        }
        mQuestionsFromAdapter = new SimpleRecyclerViewAdapter<>(R.layout.item_question_from, BR.bean, (v, questionsFromBean) -> {

            mQuestionsFrom = questionsFromBean.getIndex();
            for (int i = 0; i < mQuestionsFromAdapter.getList().size(); i++) {
                mQuestionsFromAdapter.getList().get(i).setSelected(false);
            }
            mQuestionsFromAdapter.getList().get(questionsFromBean.getIndex()).setSelected(true);
            mQuestionsFromAdapter.notifyDataSetChanged();

        });
        binding.recyclerQuestionsFrom.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.recyclerQuestionsFrom.setAdapter(mQuestionsFromAdapter);
        mQuestionsFromAdapter.setList(list);

        binding.radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.rb_all:
                    mDifficultyLevel = 0;
                    binding.relativeLevel.setVisibility(View.GONE);
                    break;
                case R.id.rb_choose_level:
                    mDifficultyLevel = 1;
                    binding.relativeLevel.setVisibility(View.VISIBLE);
                    break;
            }
        });

        binding.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mDifficultyLevel = progress + 1;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        binding.rbAll.setChecked(true);
    }

    private BaseCallback baseCallback = view -> {
        switch (view.getId()) {
            case R.id.relative_ques_number:
                showPopUpMenu(view);
                break;
        }
    };

    private void showPopUpMenu(View view) {
        //Creating the instance of PopupMenu
        PopupMenu popup = new PopupMenu(getActivity(), view);
        //Inflating the Popup using xml file
        popup.getMenuInflater().inflate(R.menu.no_of_question_menu, popup.getMenu());

        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(item -> {
            mNoOfQuestions = Integer.parseInt(item.getTitle().toString());
            binding.textQuestions.setText(item.getTitle());
            //Toast.makeText(this,"You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
            return true;
        });

        popup.show();//showing popup menu
    }
}
