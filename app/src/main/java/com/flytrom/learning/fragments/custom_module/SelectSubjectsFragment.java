package com.flytrom.learning.fragments.custom_module;


import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.flytrom.learning.R;
import com.flytrom.learning.adapters.SelectSubjectsAdapter;
import com.flytrom.learning.base.BaseCallback;
import com.flytrom.learning.base.BaseFragment;
import com.flytrom.learning.base.SimpleRecyclerViewAdapter;
import com.flytrom.learning.beans.normal_beans.ApiErrorBean;
import com.flytrom.learning.beans.response_beans.q_bank.ChapterBean;
import com.flytrom.learning.beans.response_beans.q_bank.GetQuestionBankChapterBean;
import com.flytrom.learning.beans.response_beans.subject.GetSubjectsBean;
import com.flytrom.learning.beans.response_beans.subject.SubjectBean;
import com.flytrom.learning.databinding.FragmentSelectSubjectsBinding;
import com.flytrom.learning.utils.AppController;
import com.flytrom.learning.utils.Constants;
import com.flytrom.learning.utils.ConstantsNew;
import com.flytrom.learning.utils.MySharedPreferences;
import com.flytrom.learning.utils.ResponseHandler;
import com.flytrom.learning.utils.VerticalPagination;

import java.util.HashMap;
import java.util.Objects;

import retrofit2.Call;

/**
 * A simple {@link Fragment} subclass.
 */
public class SelectSubjectsFragment extends BaseFragment<FragmentSelectSubjectsBinding>
        implements VerticalPagination.VerticalScrollListener, BaseCallback,
        SimpleRecyclerViewAdapter.SimpleCallback<ChapterBean> {

    private SelectSubjectsAdapter mSubjectsAdapter;
    private Call<GetSubjectsBean> mCallGetSubjects;
    private Call<GetQuestionBankChapterBean> mCallGetQuestionBankChapter;
    private int mCurrentPage = 1;
    private int mCurrentPageChapter = 1;
    private VerticalPagination mVerticalPagination;
    private String mSelectedSubject;
    //private ArrayList<String> mSelectedIdsList;
    private boolean isAllChecked = false;
    private StringBuilder stringBuilderSubjects;
    private StringBuilder stringBuilderChapters;
    public static SelectSubjectsFragment self;
    public int mTotalSubjectsSelected = 0;
    private HashMap<Integer, String> mMapOfChapterNames;

    public SelectSubjectsFragment() {
        // Required empty public constructor
    }

    @Override
    protected void onFragmentCreateView(Bundle savedInstanceState) {
        super.onFragmentCreateView(savedInstanceState);

        initView();
    }

    @Override
    public void onLoadMore() {
        //getSubjects(mCurrentPage, false);
    }

    @Override
    public void onDetach() {
        mCurrentPage = 1;
        if (mCallGetSubjects != null) mCallGetSubjects.cancel();
        if (mCallGetQuestionBankChapter != null) mCallGetQuestionBankChapter.cancel();
        super.onDetach();
    }

    @Override
    public int getFragmentLayout() {
        return R.layout.fragment_select_subjects;
    }

    private void initView() {
        this.self = this;
        mMapOfChapterNames = new HashMap<>();
        mSubjectsAdapter = new SelectSubjectsAdapter(getActivity(), this, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mVerticalPagination = new VerticalPagination(linearLayoutManager, 3);
        mVerticalPagination.setListener(this);
        binding.recyclerSubjects.setLayoutManager(linearLayoutManager);
        binding.recyclerSubjects.addOnScrollListener(mVerticalPagination);
        binding.recyclerSubjects.setAdapter(mSubjectsAdapter);

        ((SimpleItemAnimator) Objects.requireNonNull(binding.recyclerSubjects.getItemAnimator())).setSupportsChangeAnimations(false);

        getSubjects(mCurrentPage, true);

        binding.radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.rb_choose:
                    selectAllSubjects(false);
                    break;
                case R.id.rb_all:
                    selectAllSubjects(true);
                    break;
            }
        });

    }

    private void selectAllSubjects(boolean value) {
        for (int i = 0; i < mSubjectsAdapter.getData().size(); i++) {
            if (!mSubjectsAdapter.getData().get(i).isLocked())
                mSubjectsAdapter.getData().get(i).setSelected(value);
        }

        mSubjectsAdapter.notifyDataSetChanged();

        isAllChecked = value;
    }

    public String getSelectedSubjects() {
        //mSelectedIdsList.clear();
        if (isAllChecked) {
            mTotalSubjectsSelected = mSubjectsAdapter.getData().size();
            return "0";
        } else {
            stringBuilderSubjects = new StringBuilder();
            for (int i = 0; i < mSubjectsAdapter.getData().size(); i++) {
                //mSelectedIdsList.add(mSubjectsAdapter.getData()().get(i).getId());
                if (mSubjectsAdapter.getData().get(i).isSelected()) {
                    mTotalSubjectsSelected++;
                    stringBuilderSubjects.append(mSubjectsAdapter.getData().get(i).getTitle());
                    stringBuilderSubjects.append(Constants.SEPARATOR);
                }
            }
            String csv = stringBuilderSubjects.toString();
            if (csv.length() > 0)
                return stringBuilderSubjects.substring(0, csv.length() - Constants.SEPARATOR.length());
        }
        return null;
    }

    public String getSelectedChapters() {
        stringBuilderChapters = new StringBuilder();
        String[] values = mMapOfChapterNames.values().toArray(new String[0]);
        for (int i = 0; i <= values.length-1; i++) {
            stringBuilderChapters.append(values[i]);
            stringBuilderChapters.append(Constants.SEPARATOR);
        }
        String csv = stringBuilderChapters.toString();
        if (csv.length() > 0)
            return stringBuilderChapters.substring(0, csv.length() - Constants.SEPARATOR.length());
        return null;
    }

    private void getSubjects(int currentPage, boolean progress) {
        if (progress) showBaseProgress();
        mCallGetSubjects = AppController.getInstance().getApis().getSubjects2(getHeader(),
                String.valueOf(currentPage), "qb", "question_bank_visibility", MySharedPreferences.getInstance().getString(getActivity(), ConstantsNew.CAT_ID));
        mCallGetSubjects.enqueue(new ResponseHandler<GetSubjectsBean>() {
            @Override
            public void onSuccess(GetSubjectsBean getSubjectsBean) {

                if (getSubjectsBean != null) {
                    if (getSubjectsBean.getStatus() == Constants.SUCCESS_CODE) {

                        if (getSubjectsBean.getData() != null && getSubjectsBean.getData().size() > 0) {


                            int lastIndex = mSubjectsAdapter.getData().size();
                            for (int i = 0; i < getSubjectsBean.getData().size(); i++) {
                                getSubjectsBean.getData().get(i).setIndex(lastIndex + i);
                            }

                            if (mCurrentPage == 1)
                                mSubjectsAdapter.setDataList(getSubjectsBean.getData());
                            else
                                mSubjectsAdapter.addToDataList(getSubjectsBean.getData());

                            if (mCurrentPage < getSubjectsBean.getMetadata().getRemainingPages()) {
                                mCurrentPage++;
                                mVerticalPagination.setLoading(false);
                            }
                        }
                    }
                }
            }

            @Override
            public void apiError(ApiErrorBean t) {
                hideBaseProgress();
                if (t != null) showErrorToast(t.getMessage());
            }

            @Override
            public void onComplete(Call<GetSubjectsBean> call, Throwable t) {
                hideBaseProgress();
                onCallComplete(call, t);
            }

            @Override
            public void statusCode(int t) {
                super.statusCode(t);
                handleCodes(t);
                if (t == Constants.NO_DATA) {
                    binding.recyclerSubjects.setVisibility(View.GONE);
                    binding.linearEmptyView.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void getQuestionBankChapters(int currentPage, boolean progress, SubjectBean subjectBean, int mainItemPosition) {
        if (progress) showBaseProgress();
        mCallGetQuestionBankChapter = AppController.getInstance().getApis().getChapters(getHeader(),
                String.valueOf(currentPage), subjectBean.getTitle(), null);
        mCallGetQuestionBankChapter.enqueue(new ResponseHandler<GetQuestionBankChapterBean>() {
            @Override
            public void onSuccess(GetQuestionBankChapterBean bankChapterBean) {

                if (bankChapterBean != null) {
                    if (bankChapterBean.getStatus() == Constants.SUCCESS_CODE) {

                        if (bankChapterBean.getData() != null && bankChapterBean.getData().size() > 0) {

                            //insertChaptersInDatabase(bankChapterBean);
                            //prepareListAndSet(bankChapterBean.getData());

                            // if (mCurrentPageChapter == 1)
                            for (int i = 0; i < bankChapterBean.getData().size(); i++) {
                                bankChapterBean.getData().get(i).setMain_item_position(mainItemPosition);
                            }
                            subjectBean.setExpanded(true);
                            mSubjectsAdapter.getData().get(subjectBean.getIndex()).setChapters(bankChapterBean.getData());
                            mSubjectsAdapter.notifyItemChanged(subjectBean.getIndex());
                            // else
                            //   mSubjectsAdapter.getData().get(subjectBean.getIndex()).setChapters(bankChapterBean.getData());

                            /*if (mCurrentPageChapter < bankChapterBean.get_metadata().getRemaining_pages()) {
                                mCurrentPageChapter++;
                                mVerticalPagination.setLoading(false);
                            }*/
                        }
                    }
                }
            }

            @Override
            public void apiError(ApiErrorBean t) {
                hideBaseProgress();
                if (t != null) showErrorToast(t.getMessage());
            }

            @Override
            public void onComplete(Call<GetQuestionBankChapterBean> call, Throwable t) {
                onCallComplete(call, t);
            }

            @Override
            public void statusCode(int t) {
                super.statusCode(t);
                handleCodes(t);
                if (t == 202) {
                    showToast("No chapter in " + subjectBean.getTitle());
                }
            }
        });
    }

    /*Adapter Clicks*/

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onViewClick(View view, int position) {
        SubjectBean subjectBean = mSubjectsAdapter.getData().get(position);
        if (subjectBean.isLocked()) {
            showPurchasePlanDialog(getString(R.string.subject));
        } else {
            switch (view.getId()) {
                case R.id.relative_subject:
                    if (!isAllChecked) {
                        if (subjectBean.isSelected()) {
                            subjectBean.setSelected(false);
                        } else {
                            subjectBean.setSelected(true);
                        }
                        mSubjectsAdapter.notifyItemChanged(position);
                    } else {
                        isAllChecked = false;
                        subjectBean.setSelected(false);
                        mSubjectsAdapter.notifyItemChanged(position);
                    }
                    break;
                case R.id.image_select_chapter:
                    if (subjectBean.isExpanded()) {
                        subjectBean.setExpanded(false);
                        mSubjectsAdapter.notifyItemChanged(position);
                    } else {
                        getQuestionBankChapters(mCurrentPageChapter, true, subjectBean, position);
                    }
                    break;
            }
        }
    }

    @Override
    public void onClick(View v, ChapterBean chapterBean) {

        if (chapterBean.isSelected()) {
            chapterBean.setSelected(false);
            mMapOfChapterNames.remove(chapterBean.getId());
        } else {
            mMapOfChapterNames.put(chapterBean.getId(), chapterBean.getTitle());
            chapterBean.setSelected(true);
        }
        mSubjectsAdapter.notifyItemChanged(chapterBean.getMain_item_position());
    }
}
