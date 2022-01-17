package com.flytrom.learning.fragments.video;


import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;

import androidx.databinding.library.baseAdapters.BR;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.bumptech.glide.Glide;
import com.flytrom.learning.R;
import com.flytrom.learning.activities.video_menu.FreeVideosActivity;
import com.flytrom.learning.activities.video_menu.SubjectVideosActivity;
import com.flytrom.learning.activities.video_menu.SavedVideosActivity;
import com.flytrom.learning.base.BaseFragment;
import com.flytrom.learning.base.SimpleRecyclerViewAdapter;
import com.flytrom.learning.beans.normal_beans.ApiErrorBean;
import com.flytrom.learning.beans.response_beans.subject.GetSubjectsBean;
import com.flytrom.learning.beans.response_beans.subject.SubjectBean;
import com.flytrom.learning.databinding.FragmentLecturesBinding;
import com.flytrom.learning.databinding.ItemVideosMenuBinding;
import com.flytrom.learning.utils.AppController;
import com.flytrom.learning.utils.Constants;
import com.flytrom.learning.utils.PrefUtils;
import com.flytrom.learning.utils.ResponseHandler;
import com.flytrom.learning.utils.VerticalPagination;

import java.util.List;
import java.util.Objects;

import kotlinx.coroutines.GlobalScope;
import retrofit2.Call;

/**
 * A simple {@link Fragment} subclass.
 */
public class LecturesFragment extends BaseFragment<FragmentLecturesBinding>
        implements VerticalPagination.VerticalScrollListener {

    private SimpleRecyclerViewAdapter<SubjectBean, ItemVideosMenuBinding> mSubjectsAdapter;
    private Call<GetSubjectsBean> mCallGetSubjects;
    private int mCurrentPage = 1;
    private VerticalPagination mVerticalPagination;
    String catogeryId,subject,image,name;

    public LecturesFragment() {
        // Required empty public constructor
    }

    @Override
    protected void onFragmentCreateView(Bundle savedInstanceState) {
        super.onFragmentCreateView(savedInstanceState);
        catogeryId = getArguments().getString("CategoryId");
        subject = getArguments().getString("Subject");
        image = getArguments().getString("image");
        name = getArguments().getString("name");
        binding.subjectLecture.setText(subject+" Subject");
        binding.lectureName.setText(name);
        Glide.with(this).load(Constants.MEDIA_URL + image).into(binding.lectureImg);
        Log.d("QWERTYUIO","ID "+catogeryId +","+"subject "+subject+","+"image "+Constants.MEDIA_URL + image);
        initView();
    }

    @Override
    public int getFragmentLayout() {
        return R.layout.fragment_lectures;
    }

    @Override
    public void onDetach() {
        mCurrentPage = 1;
        if (mCallGetSubjects != null) mCallGetSubjects.cancel();
        super.onDetach();
    }


    private void initView() {
        mSubjectsAdapter = new SimpleRecyclerViewAdapter<>(R.layout.video_item_list, BR.bean, (v, subjectBean) -> {
            if (subjectBean.isLocked())
                PrefUtils.getInstance().setSelectedSubject(subjectBean.getTitle());
            else
                PrefUtils.getInstance().setSelectedSubject(null);

            switch (subjectBean.getId()) {
                case Constants.SAVED_VIDEOS_MENU_ID:
                    startActivity(new Intent(getActivity(), SavedVideosActivity.class));
                    break;
                case Constants.FREE_VIDEOS_MENU_ID:
                    startActivity(new Intent(getActivity(), FreeVideosActivity.class));
                    break;
                default:
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("subjectBean", subjectBean);
                    startActivity(new Intent(getActivity(), SubjectVideosActivity.class).putExtras(bundle));
                    break;
            }
            goNextAnimation();
        });

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1);
        mVerticalPagination = new VerticalPagination(gridLayoutManager, 3);
        mVerticalPagination.setListener(this);
        binding.recyclerSubjects.setLayoutManager(gridLayoutManager);
        binding.recyclerSubjects.addOnScrollListener(mVerticalPagination);
        binding.recyclerSubjects.setAdapter(mSubjectsAdapter);

        getSubjectsFromRoom();
        if (AppController.getInstance().isInternetOn()) getSubjects(mCurrentPage, false);
    }

    @Override
    public void onLoadMore() {
        getSubjects(mCurrentPage, false);
    }

    private void getSubjects(int currentPage, boolean progress) {
        //showBaseProgress();
        mCallGetSubjects = AppController.getInstance().getApis().getSubjects2(getHeader(),
                String.valueOf(currentPage), "video", "videos_visibility",catogeryId);
        mCallGetSubjects.enqueue(new ResponseHandler<GetSubjectsBean>() {
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

                            insertSubjectsInDatabase(getSubjectsBean.getData());
                        }
                    }
                }
            }

            @Override
            public void apiError(ApiErrorBean t) {
                hideBaseProgress();
                //if (t != null) showErrorToast(t.getMessage());
            }

            @Override
            public void onComplete(Call<GetSubjectsBean> call, Throwable t) {
                //hideBaseProgress();
                //onCallComplete(call, t);
                //  if (t != null) getSubjectsFromRoom();
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

//        SubjectBean bean2 = new SubjectBean();
//        bean2.setTitle("Free Videos");
//        bean2.setIndex(1);
//        bean2.setId(Constants.FREE_VIDEOS_MENU_ID);
//        bean2.setOrderType("video");
//        //bean2.setTotalVideoCompleted("0");
//        // bean2.setTotalVideos("0");
//        list.add(0, bean2);

        SubjectBean bean1 = new SubjectBean();
        bean1.setTitle("My Downloaded Videos");
        bean1.setIndex(0);
        bean1.setId(Constants.SAVED_VIDEOS_MENU_ID);
        bean1.setOrderType("video");
        //bean1.setTotalVideoCompleted("0");
        //bean1.setTotalVideos("0");
        list.add(0, bean1);

        mSubjectsAdapter.setList(list);
    }

    private void insertSubjectsInDatabase(List<SubjectBean> list) {
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                list.get(i).setOrderType("video");
            }
            mDBRepositoryKotlin.insertSubjects(list);
            hideEmptyView();
        }
    }

    private void getSubjectsFromRoom() {
        mDBRepository.getSubjects("video").observe(this, list -> {
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
