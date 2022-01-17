package com.flytrom.learning.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.databinding.library.baseAdapters.BR;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.flytrom.learning.R;
import com.flytrom.learning.activities.q_bank_menu.TestsActivity;
import com.flytrom.learning.base.BaseCallback;
import com.flytrom.learning.base.BaseFragment;
import com.flytrom.learning.base.SimpleRecyclerViewAdapter;
import com.flytrom.learning.beans.normal_beans.ApiErrorBean;
import com.flytrom.learning.beans.response_beans.subject.GetSubjectsBean;
import com.flytrom.learning.beans.response_beans.subject.SubjectBean;
import com.flytrom.learning.databinding.FragmentTestsBinding;
import com.flytrom.learning.databinding.ItemTestMenuSubjectBinding;
import com.flytrom.learning.utils.AppController;
import com.flytrom.learning.utils.Constants;
import com.flytrom.learning.utils.PrefUtils;
import com.flytrom.learning.utils.ResponseHandler;
import com.flytrom.learning.utils.VerticalPagination;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;

public class TestsFragment extends BaseFragment<FragmentTestsBinding>
        implements VerticalPagination.VerticalScrollListener {

    private SimpleRecyclerViewAdapter<SubjectBean, ItemTestMenuSubjectBinding> mSubjectsAdapter;
    private Call<GetSubjectsBean> mCallGetSubjects;
    private int mCurrentPage = 1;
    private VerticalPagination mVerticalPagination;
    String catogeryId;

    public TestsFragment() {
        // Required empty public constructor
    }

    @Override
    protected void onFragmentCreateView(Bundle savedInstanceState) {
        super.onFragmentCreateView(savedInstanceState);
        catogeryId = getArguments().getString("CategoryId");
        initView();
    }

    @Override
    public void onLoadMore() {
        getSubjects(mCurrentPage, false);
    }


    @Override
    public int getFragmentLayout() {
        return R.layout.fragment_tests;
    }

    @Override
    public void onDetach() {
        mCurrentPage = 1;
        if (mCallGetSubjects != null) mCallGetSubjects.cancel();
        super.onDetach();
    }

    private void initView() {
        setBaseCallback(baseCallback);
        mSubjectsAdapter = new SimpleRecyclerViewAdapter<>(R.layout.q_bank_item_list, BR.bean, (v, subjectBean) -> {

            Bundle bundle = new Bundle();
            if (subjectBean.getType() != null) {
                bundle.putString("type", Constants.FREE);
            } else {
                bundle.putSerializable("chapterBean", subjectBean);
                bundle.putString("type", Constants.PAID);
            }

            if (subjectBean.isLocked())
                PrefUtils.getInstance().setSelectedSubject(subjectBean.getTitle());
            else
                PrefUtils.getInstance().setSelectedSubject(null);

            bundle.putString("from", "testMenu");
            Objects.requireNonNull(getActivity()).startActivity(new Intent(getActivity(), TestsActivity.class).putExtras(bundle));
            goNextAnimation();
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mVerticalPagination = new VerticalPagination(linearLayoutManager, 3);
        mVerticalPagination.setListener(this);
        binding.recyclerSubjects.setLayoutManager(linearLayoutManager);
        binding.recyclerSubjects.addOnScrollListener(mVerticalPagination);
        binding.recyclerSubjects.setAdapter(mSubjectsAdapter);

        getSubjectsFromRoom();
        if (AppController.getInstance().isInternetOn()) getSubjects(mCurrentPage, false);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private BaseCallback baseCallback = view -> {
        if (view.getId() == R.id.card_free_test) {
            Bundle bundle = new Bundle();
            bundle.putString("from", "testMenu");
            bundle.putString("type", "Free");
            startActivity(new Intent(getActivity(), TestsActivity.class).putExtras(bundle));
            goNextAnimation();
        }
    };


    private void getSubjects(int currentPage, boolean progress) {
        //if (progress) showBaseProgress();
        mCallGetSubjects = AppController.getInstance().getApis().getSubjects2(getHeader(),
                String.valueOf(currentPage), "test", "tests_visibility",catogeryId);
        mCallGetSubjects.enqueue(new ResponseHandler<GetSubjectsBean>() {
            @Override
            public void onSuccess(GetSubjectsBean getSubjectsBean) {

                Log.e("ASDASAASAS",getSubjectsBean.getMessage());

                if (getSubjectsBean != null) {
                    if (getSubjectsBean.getStatus() == Constants.SUCCESS_CODE) {

                        if (getSubjectsBean.getData() != null && getSubjectsBean.getData().size() > 0) {

                            if (mCurrentPage == 1) {
                                prepareDefaultListAndSet(getSubjectsBean.getData());
                            } else {
                                mSubjectsAdapter.addToList(getSubjectsBean.getData());
                            }
                            if (mCurrentPage < getSubjectsBean.getMetadata().getRemainingPages()) {
                                mCurrentPage++;
                                mVerticalPagination.setLoading(false);
                            }

                            insertSubjectsInDatabase(getSubjectsBean);
                        }
                    }
                }
            }

            @Override
            public void apiError(ApiErrorBean t) {
                hideBaseProgress();
                // if (t != null) showErrorToast(t.getMessage());
            }

            @Override
            public void onComplete(Call<GetSubjectsBean> call, Throwable t) {
                hideBaseProgress();
                // onCallComplete(call, t);
                //if (t != null) getSubjectsFromRoom();
            }

            @Override
            public void statusCode(int t) {
                super.statusCode(t);
                handleCodes(t);
                if (t == Constants.NO_DATA) {
                    if (mCurrentPage == 1) {
                        hideEmptyView();
                    }
                }
            }
        });
    }

    private void prepareDefaultListAndSet(List<SubjectBean> list) {
//        SubjectBean bean1 = new SubjectBean();
//        bean1.setTitle("Free Tests");
//        bean1.setIndex(0);
//        bean1.setOrderType("test");
//        bean1.setType("Free");
//        list.add(0, bean1);

        mSubjectsAdapter.setList(list);
    }

    private void insertSubjectsInDatabase(GetSubjectsBean model) {
        if (model.getData() != null) {
            for (int i = 0; i < model.getData().size(); i++) {
                model.getData().get(i).setOrderType("test");
            }
            mDBRepositoryKotlin.insertSubjects(model.getData());
            hideEmptyView();
        }
    }

    private void getSubjectsFromRoom() {
        mDBRepository.getSubjects("test").observe(this, list -> {
            if (list.size() > 0) {
                if (mSubjectsAdapter.getList().size() == 0) {
                    mSubjectsAdapter.setList(list);
                    hideEmptyView();
                }
            } else {
                showEmptyView();
            }
        });
    }

    private void showEmptyView() {
        binding.recyclerSubjects.setVisibility(View.GONE);
        binding.linearEmptyView.setVisibility(View.VISIBLE);
    }

    private void hideEmptyView() {
        binding.recyclerSubjects.setVisibility(View.VISIBLE);
        binding.linearEmptyView.setVisibility(View.GONE);
    }
}