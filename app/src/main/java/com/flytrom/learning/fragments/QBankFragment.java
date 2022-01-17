package com.flytrom.learning.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.databinding.library.baseAdapters.BR;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.flytrom.learning.R;
import com.flytrom.learning.activities.info.AppSettingsActivity;
import com.flytrom.learning.activities.q_bank_menu.BookMarkedActivity;
import com.flytrom.learning.activities.q_bank_menu.CreateCustomModuleActivity;
import com.flytrom.learning.activities.q_bank_menu.ChaptersActivity;
import com.flytrom.learning.activities.q_bank_menu.GoingToStartModuleActivity;
import com.flytrom.learning.adapters.QBankCategoryAdapter;
import com.flytrom.learning.adapters.RegionAdapter;
import com.flytrom.learning.base.BaseCallback;
import com.flytrom.learning.base.BaseFragment;
import com.flytrom.learning.base.SimpleRecyclerViewAdapter;
import com.flytrom.learning.beans.normal_beans.ApiErrorBean;
import com.flytrom.learning.beans.response_beans.custom_module.GetCustomModuleBean;
import com.flytrom.learning.beans.response_beans.subject.GetSubjectsBean;
import com.flytrom.learning.beans.response_beans.subject.SubjectBean;
import com.flytrom.learning.databinding.FragmentQBankBinding;
import com.flytrom.learning.databinding.ItemQBankSubjectBinding;
import com.flytrom.learning.interfaces.Apis;
import com.flytrom.learning.model.MainCategorey.DataCategory;
import com.flytrom.learning.model.MainCategorey.MainCategory;
import com.flytrom.learning.utils.AppController;
import com.flytrom.learning.utils.Constants;
import com.flytrom.learning.utils.PrefUtils;
import com.flytrom.learning.utils.ResponseHandler;
import com.flytrom.learning.utils.VerticalPagination;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class QBankFragment extends BaseFragment<FragmentQBankBinding>
        implements VerticalPagination.VerticalScrollListener {

    private SimpleRecyclerViewAdapter<SubjectBean, ItemQBankSubjectBinding> mSubjectsAdapter;
    private Call<GetSubjectsBean> mCallGetChapters;
    private int mCurrentPage = 1;
    private VerticalPagination mVerticalPagination;
    private Call<GetCustomModuleBean> mCallGetCustomModule;
    private boolean mIsCustomModuleExist;
    String catogeryId;

    public QBankFragment() {
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
        return R.layout.fragment_q_bank;
    }

    @Override
    public void onDetach() {
        mCurrentPage = 1;
        if (mCallGetChapters != null) mCallGetChapters.cancel();
        if (mCallGetCustomModule != null) mCallGetCustomModule.cancel();
        super.onDetach();
    }

    private void initView() {
        setBaseCallback(callback);
        mSubjectsAdapter = new SimpleRecyclerViewAdapter<>(R.layout.q_bank_item_list, BR.bean, (v, subjectBean) -> {
            Bundle bundle = new Bundle();
            if (subjectBean.isLocked())
                PrefUtils.getInstance().setSelectedSubject(subjectBean.getTitle());
            else
                PrefUtils.getInstance().setSelectedSubject(null);

            if (subjectBean.getId() == Constants.FREE_QUESTIONS_MENU_ID) {
                bundle.putString("type", Constants.FREE);
            } else {
                bundle.putString("type", Constants.PAID);
                bundle.putSerializable("subjectBean", subjectBean);
            }

            startActivity(new Intent(getActivity(), ChaptersActivity.class).putExtras(bundle));
            goNextAnimation();
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mVerticalPagination = new VerticalPagination(linearLayoutManager, 3);
        mVerticalPagination.setListener(this);
        binding.recyclerQBank.setLayoutManager(linearLayoutManager);
        binding.recyclerQBank.addOnScrollListener(mVerticalPagination);
        binding.recyclerQBank.setAdapter(mSubjectsAdapter);

        getSubjectsFromRoom();
        if (AppController.getInstance().isInternetOn()) getSubjects(mCurrentPage, false);

        getCustomModuleFromRoom();
        if (AppController.getInstance().isInternetOn()) getCustomModule();
    }

    private BaseCallback callback = view -> {
        switch (view.getId()) {
            case R.id.card_bookmark:
                goToBookMarkScreen();
                break;
            case R.id.card_custom_module:
                if (mIsCustomModuleExist) {
                    startActivity(new Intent(getActivity(), GoingToStartModuleActivity.class));
                } else {
                    startActivity(new Intent(getActivity(), CreateCustomModuleActivity.class));
                }
                goNextAnimation();
                break;
        }
    };

    private void goToBookMarkScreen() {
        startActivity(new Intent(getActivity(), BookMarkedActivity.class).putExtra("from", "bookmark"));
        goNextAnimation();
    }

    private void getCustomModule() {
        mCallGetCustomModule = AppController.getInstance().getApis().getCustomModule(getHeader());
        mCallGetCustomModule.enqueue(new ResponseHandler<GetCustomModuleBean>() {
            @Override
            public void onSuccess(GetCustomModuleBean getCustomModuleBean) {

                if (getCustomModuleBean != null) {
                    if (getCustomModuleBean.getStatus() == Constants.SUCCESS_CODE) {
                        insertCustomModuleInDatabase(getCustomModuleBean);
                    }
                }
            }

            @Override
            public void apiError(ApiErrorBean t) {

                //if (t != null) showErrorToast(t.getMessage());
            }

            @Override
            public void onComplete(Call<GetCustomModuleBean> call, Throwable t) {
                //onCallComplete(call, t);
            }

            @Override
            public void statusCode(int t) {
                super.statusCode(t);
                handleCodes(t);
            }
        });
    }

    private void getSubjects(int currentPage, boolean progress) {
        //if (progress) showBaseProgress();
        mCallGetChapters = AppController.getInstance().getApis().getSubjects2(getHeader(),
                String.valueOf(currentPage), "qb", "question_bank_visibility",catogeryId);
        mCallGetChapters.enqueue(new ResponseHandler<GetSubjectsBean>() {
            @Override
            public void onSuccess(GetSubjectsBean getSubjectsBean) {

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

                            setBookmarkLocally(getSubjectsBean.getBookmarkCounter());
                            insertSubjectsInDatabase(getSubjectsBean);
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
                //onCallComplete(call, t);
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
//        bean1.setTitle("Free Questions");
//        bean1.setId(Constants.FREE_QUESTIONS_MENU_ID);
//        bean1.setIndex(0);
//        bean1.setOrderType("qb");
//        list.add(0, bean1);

        mSubjectsAdapter.setList(list);
    }

    private void setBookmarkLocally(int bookmarkCounter) {
        PrefUtils.getInstance().setBookmarkCounter(bookmarkCounter);
        binding.textBookmarkCount.setText(String.valueOf(bookmarkCounter));
    }

    private void insertSubjectsInDatabase(GetSubjectsBean model) {
        if (model.getData() != null) {
            for (int i = 0; i < model.getData().size(); i++) {
                model.getData().get(i).setOrderType("qb");
            }
            mDBRepositoryKotlin.insertSubjects(model.getData());
        }

        hideEmptyView();
    }

    private void getSubjectsFromRoom() {
        mDBRepository.getSubjects("qb").observe(this, list -> {
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

    private void insertCustomModuleInDatabase(GetCustomModuleBean model) {
        if (model.getData() != null) mDBRepositoryKotlin.insertCustomModule(model.getData());
    }

    private void getCustomModuleFromRoom() {
        mDBRepository.getCustomModuleFromRoom().observe(this, list -> {
            mIsCustomModuleExist = list.size() > 0;
        });
    }

    private void showEmptyView() {
        binding.recyclerQBank.setVisibility(View.GONE);
        binding.linearEmptyView.setVisibility(View.VISIBLE);
    }

    private void hideEmptyView() {
        binding.recyclerQBank.setVisibility(View.VISIBLE);
        binding.linearEmptyView.setVisibility(View.GONE);
    }
}