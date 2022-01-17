package com.flytrom.learning.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.flytrom.learning.R;
import com.flytrom.learning.activities.ProgressActivity;
import com.flytrom.learning.activities.info.ProfileActivity;
import com.flytrom.learning.activities.others.EditProfileActivity;
import com.flytrom.learning.activities.others.HomeActivity;
import com.flytrom.learning.activities.payment.SubscribeActivity;
import com.flytrom.learning.activities.q_bank_menu.BookMarkedActivity;
import com.flytrom.learning.activities.q_bank_menu.ParticularQuestionActivity;
import com.flytrom.learning.adapters.RandomQuestionAdapter;
import com.flytrom.learning.base.BaseCallback;
import com.flytrom.learning.base.BaseFragment;
import com.flytrom.learning.beans.normal_beans.ApiErrorBean;
import com.flytrom.learning.beans.response_beans.auth.LoginBean;
import com.flytrom.learning.beans.response_beans.others.GetUrlDataBean;
import com.flytrom.learning.beans.response_beans.others.GetUrlsBean;
import com.flytrom.learning.beans.response_beans.random_question.GetRandomQuestionBean;
import com.flytrom.learning.beans.response_beans.others.SuccessBean;
import com.flytrom.learning.beans.response_beans.random_question.RandomQuestionBean;
import com.flytrom.learning.beans.response_beans.test_bean.QuestionBean;
import com.flytrom.learning.databinding.FragmentHomeBinding;
import com.flytrom.learning.model.LoginModel.LoginModel;
import com.flytrom.learning.utils.AppController;
import com.flytrom.learning.utils.Constants;
import com.flytrom.learning.utils.ConstantsNew;
import com.flytrom.learning.utils.MySharedPreferences;
import com.flytrom.learning.utils.PrefUtils;
import com.flytrom.learning.utils.ResponseHandler;

import org.androidannotations.annotations.App;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.util.HashMap;
import java.util.Objects;

import retrofit2.Call;
import timber.log.Timber;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends BaseFragment<FragmentHomeBinding> implements BaseCallback {

    private Call<GetRandomQuestionBean> mCallGetRandomQuestion;
    private Call<SuccessBean> mCallAnswerRandomQuestion;
    private Call<LoginModel> mCallGetProfile;
    private RandomQuestionAdapter randomQuestionAdapter;
    private int mQuestionId;
    private boolean mIsSeeExplanationEnable;
    private Call<GetUrlsBean> mCallGetUrls;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    protected void onFragmentCreateView(Bundle savedInstanceState) {
        super.onFragmentCreateView(savedInstanceState);

        initView();
    }

    @Override
    public void onDetach() {
        if (mCallGetRandomQuestion != null) mCallGetRandomQuestion.cancel();
        if (mCallGetUrls != null) mCallGetUrls.cancel();
        super.onDetach();
    }

    @Override
    public int getFragmentLayout() {
        return R.layout.fragment_home;
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onViewClick(View view, int position) {

        mQuestionId = randomQuestionAdapter.getData().get(position).getQuestionId();
        setSelectedIndex(position);
    }

    private void initView() {
        randomQuestionAdapter = new RandomQuestionAdapter(getActivity(), this);
        binding.recyclerViewOptions.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.recyclerViewOptions.setAdapter(randomQuestionAdapter);
        setBaseCallback(baseCallback);

        getRandomQuestionFromDatabase();
        if (AppController.getInstance().isInternetOn()) getRandomQuestionFromServer();
        if (AppController.getInstance().isInternetOn()) getProfile();

        binding.textNotificationDesc.setText(PrefUtils.getInstance().getNotificationText());
        if (AppController.getInstance().isInternetOn()) getNotificationText();
    }

    private BaseCallback baseCallback = view -> {

        switch (view.getId()) {
            case R.id.text_submit:
                if (getSelectedOptionIndex() != -1) {
                    answerRandomQuestion();
                } else {
                    showToast("Please select at least one option");
                }
                break;
            case R.id.text_see_explanation:
                mIsSeeExplanationEnable = true;
                mDBRepository.getRandomQuestion().observe(this, list -> {
                    if (list.size() > 0) {
                        if (mIsSeeExplanationEnable)
                            goToParticularQuestionDetails(list.get(0));
                    }
                });
                break;

            case R.id.myProfileCard:
                Intent intent = new Intent(getActivity(), ProfileActivity.class);
                startActivity(intent);
                break;
           case R.id.myProgressCard:
                Intent intent2 = new Intent(getActivity(), ProgressActivity.class);
                startActivity(intent2);
                break;
           case R.id.byPlaneCard:
               startActivity(new Intent(getActivity(), SubscribeActivity.class));
               goNextAnimation();
                break;
           case R.id.myBookCard:
               startActivity(new Intent(getActivity(), BookMarkedActivity.class).putExtra("from", "bookmark"));
               goNextAnimation();
                break;
        }
    };

    private void goToParticularQuestionDetails(RandomQuestionBean bean) {
        QuestionBean questionBean = new QuestionBean();

        /*Normal Data*/
        questionBean.setId(bean.getId());
        /*Question data*/
        questionBean.setQuestion(bean.getQuestion());
        questionBean.setQuestionFile(bean.getQuestion_file());
        questionBean.setOptions(bean.getOptions());

        /*Answer data*/
        questionBean.setTagId(bean.getTagId());
        questionBean.setAnswerExplanation(bean.getAnswer_explanation());
        questionBean.setAnswerFile(bean.getAnswer_file());

        Bundle bundle = new Bundle();
        bundle.putSerializable("questionBean", questionBean);
        bundle.putString("action", "home");
        startActivity(new Intent(getActivity(), ParticularQuestionActivity.class).putExtras(bundle));
        goNextAnimation();
    }

    private void answerRandomQuestion() {
        if (mCallAnswerRandomQuestion != null) mCallAnswerRandomQuestion.cancel();
        HashMap<String, String> map = new HashMap<>();
        map.put("question_id", String.valueOf(mQuestionId));
        map.put("question_option_id", String.valueOf(randomQuestionAdapter.getData().get(getSelectedOptionIndex()).getId()));
        mCallAnswerRandomQuestion = AppController.getInstance().getApis().answerRandomQuestion(getHeader(), map);
        mCallAnswerRandomQuestion.enqueue(new ResponseHandler<SuccessBean>() {
            @Override
            public void onSuccess(SuccessBean successBean) {

                if (successBean != null) {
                    if (successBean.getStatus() == Constants.SUCCESS_CODE) {
                        showToast("Answer submitted successfully");
                        getRandomQuestionFromServer();
                    }
                }
            }

            @Override
            public void apiError(ApiErrorBean t) {

                if (t != null) Log.d("t_message", t.getMessage());
            }

            @Override
            public void onComplete(Call<SuccessBean> call, Throwable t) {

                onCallComplete(call, t);
            }

            @Override
            public void statusCode(int t) {
                super.statusCode(t);
                handleCodes(t);
            }
        });
    }

    private void getRandomQuestionFromServer() {
        //showBaseProgress();
        mCallGetRandomQuestion = AppController.getInstance().getApis().getRandomMCQ(getHeader());
        mCallGetRandomQuestion.enqueue(new ResponseHandler<GetRandomQuestionBean>() {
            @Override
            public void onSuccess(GetRandomQuestionBean model) {

                if (model != null) {
                    if (model.getStatus() == Constants.SUCCESS_CODE) {
                        if (model.getFactsBean() != null) {
                            model.getData().setHeadline(model.getFactsBean().getHeadline());
                            model.getData().setDescription(model.getFactsBean().getDescription());
                        }
                        insertRandomQuestionInDb(model.getData());
                    }
                }
            }

            @Override
            public void apiError(ApiErrorBean t) {
                // if (t != null)
                //   showErrorToast(t.getMessage());
            }

            @Override
            public void onComplete(Call<GetRandomQuestionBean> call_update_profile, Throwable t) {
                //onCallComplete(call_update_profile, t);
            }

            @Override
            public void statusCode(int t) {
                super.statusCode(t);
                handleCodes(t);
            }
        });
    }

    private void insertRandomQuestionInDb(RandomQuestionBean data) {
        mDBRepositoryKotlin.insertRandomQuestion(data);
    }

    private void getRandomQuestionFromDatabase() {
        mIsSeeExplanationEnable = false;
        mDBRepository.getRandomQuestion().observe(this, list -> {
            if (list.size() > 0) {
                for (int i = 0; i < list.get(0).getOptions().size(); i++) {
                    list.get(0).getOptions().get(i).setIndex(i);
                    list.get(0).getOptions().get(i).setSelectedIndex(-1);
                }

                setRandomQuestionData(list.get(0));
            }
        });
    }

    private void setRandomQuestionData(RandomQuestionBean data) {

        binding.textQuestion.setText(data.getQuestion());
        if (data.getHas_answered() != null) {
            if (data.getHas_answered().equals("0")) {
                randomQuestionAdapter.setMode("not_answered_yet");
                binding.cardSubmit.setVisibility(View.VISIBLE);
            } else {
                randomQuestionAdapter.setMode("answered");
                binding.cardSubmit.setVisibility(View.GONE);
                binding.textSeeExplanation.setVisibility(View.VISIBLE);
            }
        } else {
            randomQuestionAdapter.setMode("not_answered_yet");
            binding.cardSubmit.setVisibility(View.VISIBLE);
        }

        randomQuestionAdapter.setDataList(data.getOptions());
        binding.textDynHeadline.setText(data.getHeadline());
        binding.textDynDescription.setText(data.getDescription());
    }


    private void setSelectedIndex(int index) {
        for (int i = 0; i < randomQuestionAdapter.getData().size(); i++) {
            randomQuestionAdapter.getData().get(i).setSelectedIndex(randomQuestionAdapter.getData().get(index).getIndex());
        }

        randomQuestionAdapter.notifyDataSetChanged();
    }

    private int getSelectedOptionIndex() {
        for (int i = 0; i < randomQuestionAdapter.getData().size(); i++) {

            if (randomQuestionAdapter.getData().get(i).getSelectedIndex() != -1)
                return randomQuestionAdapter.getData().get(i).getSelectedIndex();
        }

        return -1;
    }

    private void getProfile() {
        mCallGetProfile = AppController.getInstance().getApis().getProfile(getHeader());
        mCallGetProfile.enqueue(new ResponseHandler<LoginModel>() {
            @Override
            public void onSuccess(LoginModel model) {

                if (model != null) {
                    if (model.getStatus() == Constants.SUCCESS_CODE) {
                        binding.textQuestions.setText(model.getData().getQuestion_attempted());
                        binding.textVideos.setText(model.getData().getVideo_completed());
                        MySharedPreferences.getInstance().saveString(getActivity(), ConstantsNew.USER_ID,model.getData().getId());
                        PrefUtils.getInstance().setUser(model.getData());
                    }
                }
            }

            @Override
            public void apiError(ApiErrorBean t) {
                if (t != null) Timber.d(t.getMessage());
            }

            @Override
            public void onComplete(Call<LoginModel> call_update_profile, Throwable t) {
                Timber.d(t);
                //onCallComplete(call_update_profile, t);
            }

            @Override
            public void statusCode(int t) {
                super.statusCode(t);
                handleCodes(t);
            }
        });
    }

    private void getNotificationText() {
        mCallGetUrls = AppController.getInstance().getApis().getUrls(getHeader());
        mCallGetUrls.enqueue(new ResponseHandler<GetUrlsBean>() {
            @Override
            public void onSuccess(GetUrlsBean getUrlsBean) {
                if (getUrlsBean != null) {
                    if (getUrlsBean.getStatus() == Constants.SUCCESS_CODE) {
                        GetUrlDataBean dataBean = getUrlsBean.getData().get(2);
                        if (dataBean != null && dataBean.getType().equals(Constants.NOTIFICATION)) {
                            binding.textNotificationDesc.setText(dataBean.getUrl());
                            PrefUtils.getInstance().setNotificationText(dataBean.getUrl());
                        } else binding.linearNotification.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void apiError(ApiErrorBean t) {
                if (t != null) showErrorToast(t.getMessage());
            }

            @Override
            public void onComplete(Call<GetUrlsBean> call, Throwable t) {

                //onCallComplete(call, t);
            }

            @Override
            public void statusCode(int t) {
                super.statusCode(t);
                handleCodes(t);
            }
        });
    }
}
