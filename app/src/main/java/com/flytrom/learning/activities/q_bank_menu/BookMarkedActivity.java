package com.flytrom.learning.activities.q_bank_menu;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.databinding.library.baseAdapters.BR;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.flytrom.learning.R;
import com.flytrom.learning.base.BaseActivity;
import com.flytrom.learning.base.BaseCallback;
import com.flytrom.learning.base.SimpleRecyclerViewAdapter;
import com.flytrom.learning.beans.normal_beans.ApiErrorBean;
import com.flytrom.learning.beans.response_beans.bookmark.BookMarkParticularQuestionBean;
import com.flytrom.learning.beans.response_beans.bookmark.GetBookmarkedQuesBean;
import com.flytrom.learning.beans.response_beans.test_bean.QuestionBean;
import com.flytrom.learning.databinding.ActivityBookMarkedBinding;
import com.flytrom.learning.databinding.ItemBookMarkedQuestionBinding;
import com.flytrom.learning.utils.AppController;
import com.flytrom.learning.utils.Constants;
import com.flytrom.learning.utils.ResponseHandler;
import com.flytrom.learning.utils.VerticalPagination;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import timber.log.Timber;

public class BookMarkedActivity extends BaseActivity<ActivityBookMarkedBinding>
        implements VerticalPagination.VerticalScrollListener {

    private SimpleRecyclerViewAdapter<QuestionBean, ItemBookMarkedQuestionBinding> mBookmarkedAdapter;
    private Call<GetBookmarkedQuesBean> mCallGetBookmarked;
    private Call<BookMarkParticularQuestionBean> mCallBookmarkQuestion;
    private VerticalPagination mVerticalPagination;
    private String mFrom;
    private int mCurrentPage = 1;

    @Override
    protected void onViewReady(Bundle savedInstanceState, Intent intent) {
        super.onViewReady(savedInstanceState, intent);
        initView();
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_book_marked;
    }

    @Override
    public void onPermissionsGranted(int requestCode) {

    }

    @Override
    public void onDestroy() {
        if (mCallGetBookmarked != null) mCallGetBookmarked.cancel();
        if (mCallBookmarkQuestion != null) mCallBookmarkQuestion.cancel();
        super.onDestroy();
    }

    @Override
    public void onLoadMore() {
        if (mFrom != null) {
            if (mFrom.equals("bookmark")) {
                getBookmarkedQuestions(mCurrentPage, false);
            }
        }
    }


    private void initView() {
        getData();
        setBaseCallback(baseCallback);

        mBookmarkedAdapter = new SimpleRecyclerViewAdapter<>(R.layout.item_book_marked_question, BR.bean, (v, questionBean) -> {
            switch (v.getId()) {
                case R.id.image_bookmark:
                    if (checkIsInternetOn()) {
                        if (mFrom != null) {
                            if (mFrom.equals("bookmark")) {
                                bookmarkQuestion(questionBean);
                            }
                        }
                    }
                    break;
                case R.id.relative_entire_view:
                    goToParticularQuestionDetails(questionBean);
                    break;
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mVerticalPagination = new VerticalPagination(linearLayoutManager, 3);
        mVerticalPagination.setListener(this);
        binding.recyclerQuestions.setLayoutManager(linearLayoutManager);
        binding.recyclerQuestions.addOnScrollListener(mVerticalPagination);
        binding.recyclerQuestions.setAdapter(mBookmarkedAdapter);

        if (mFrom != null) {
            if (mFrom.equals("bookmark")) {
                getBookmarkedQuestionsFromRoom();
                if (AppController.getInstance().isInternetOn())
                    getBookmarkedQuestions(mCurrentPage, true);
            }
        }
    }

    private void getData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mFrom = bundle.getString("from");
            if (mFrom != null) {
                if (mFrom.equals("bookmark")) {
                    binding.toolbar.textTitle.setText(getString(R.string.text_bookmarks));
                    binding.toolbar.textTitle.setTextColor(Color.parseColor("#484848"));
                }
            }
        }
    }

    private void goToParticularQuestionDetails(QuestionBean questionBean) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("questionBean", questionBean);
        bundle.putString("action", "solve");
        startActivity(new Intent(this, ParticularQuestionActivity.class).putExtras(bundle));
        goNextAnimation();
    }

    private void bookmarkQuestion(QuestionBean questionBean) {
        if (questionBean.isSelected()) {
            questionBean.setSelected(false);
        } else {
            questionBean.setSelected(true);
        }
        mBookmarkedAdapter.notifyDataSetChanged();
        bookMarkQuestion(questionBean);
    }

    private void bookMarkQuestion(QuestionBean questionBean) {
        //showBaseProgress();
        HashMap<String, String> map = new HashMap<>();
        map.put("question_id", String.valueOf(questionBean.getId()));
        map.put("b_type", questionBean.getB_type());
        mCallBookmarkQuestion = AppController.getInstance().getApis().bookmarkQuestion(getHeader(), map);
        mCallBookmarkQuestion.enqueue(new ResponseHandler<BookMarkParticularQuestionBean>() {
            @Override
            public void onSuccess(BookMarkParticularQuestionBean successBean) {

                Timber.d("df");
            }

            @Override
            public void apiError(ApiErrorBean t) {

            }

            @Override
            public void onComplete(Call<BookMarkParticularQuestionBean> call, Throwable t) {

            }

            @Override
            public void statusCode(int t) {
                super.statusCode(t);
                handleCodes(t);
            }
        });
    }

    private BaseCallback baseCallback = view -> {

        if (view.getId() == R.id.image_back) {
            goBack();
        }
    };

    private void getBookmarkedQuestions(int currentPage, boolean progress) {
        if (progress) showBaseProgress();
        mCallGetBookmarked = AppController.getInstance().getApis().getBookmarkedQuestions(getHeader(),
                String.valueOf(currentPage));
        mCallGetBookmarked.enqueue(new ResponseHandler<GetBookmarkedQuesBean>() {
            @Override
            public void onSuccess(GetBookmarkedQuesBean getBookmarks) {

                if (getBookmarks != null) {

                    if (getBookmarks.getStatus() == Constants.SUCCESS_CODE) {

                        if (getBookmarks.getData() != null && getBookmarks.getData().size() > 0) {

                            for (int i = 0; i < getBookmarks.getData().size(); i++) {
                                getBookmarks.getData().get(i).setSelected(true);
                            }

                            if (mCurrentPage == 1)
                                mBookmarkedAdapter.setList(getBookmarks.getData());
                            else
                                mBookmarkedAdapter.addToList(getBookmarks.getData());

                            if (getBookmarks.get_metadata().getRemainingPages() > mCurrentPage) {
                                mCurrentPage++;
                                mVerticalPagination.setLoading(false);
                            }

                            hideEmptyView();
                            insertBookmarkedInDatabase(getBookmarks.getData());
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
            public void onComplete(Call<GetBookmarkedQuesBean> call, Throwable t) {
                hideBaseProgress();
                //onCallComplete(call, t);
            }

            @Override
            public void statusCode(int t) {
                super.statusCode(t);
                handleCodes(t);
                if (t == Constants.NO_DATA) {
                    if (currentPage == 1) {
                        showEmptyView();
                    }
                }
            }
        });
    }

    private void insertBookmarkedInDatabase(List<QuestionBean> list) {
        mDBRepositoryKotlin.insertQuestions(list);
    }

    private void getBookmarkedQuestionsFromRoom() {
        mDBRepository.getBookmarked().observe(this, list -> {
            if (list.size() > 0) {
                if (mBookmarkedAdapter.getList().size() == 0) {
                    for (int i = 0; i < list.size(); i++) {
                        list.get(i).setSelected(true);
                    }
                    mBookmarkedAdapter.setList(list);
                    hideBaseProgress();
                    hideEmptyView();
                }
            } else {
                showEmptyView();
            }
        });
    }

    private void showEmptyView() {
        binding.recyclerQuestions.setVisibility(View.GONE);
        binding.linearEmptyView.setVisibility(View.VISIBLE);
    }

    private void hideEmptyView() {
        binding.recyclerQuestions.setVisibility(View.VISIBLE);
        binding.linearEmptyView.setVisibility(View.GONE);
    }
}
