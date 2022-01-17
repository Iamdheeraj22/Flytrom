package com.flytrom.learning.activities.q_bank_menu;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;

import com.flytrom.learning.R;
import com.flytrom.learning.activities.test_menu.ReviewTestActivity;
import com.flytrom.learning.activities.test_menu.SolveTestChapterActivity;
import com.flytrom.learning.base.BaseActivity;
import com.flytrom.learning.base.BaseCallback;
import com.flytrom.learning.base.BaseCustomDialog;
import com.flytrom.learning.beans.normal_beans.ApiErrorBean;
import com.flytrom.learning.beans.response_beans.custom_module.CustomModuleBean;
import com.flytrom.learning.beans.response_beans.custom_module.GetCustomModuleQuestionsBean;
import com.flytrom.learning.beans.response_beans.others.SuccessBean;
import com.flytrom.learning.beans.response_beans.test_bean.QuestionBean;
import com.flytrom.learning.databinding.ActivityGoingToStartModuleBinding;
import com.flytrom.learning.databinding.DialogDeleteModuleBinding;
import com.flytrom.learning.utils.AppController;
import com.flytrom.learning.utils.Constants;
import com.flytrom.learning.utils.ResponseHandler;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;
import timber.log.Timber;

public class GoingToStartModuleActivity extends BaseActivity<ActivityGoingToStartModuleBinding> {

    private CustomModuleBean mGetCustomModuleBean;
    private List<QuestionBean> mQuestionsList;
    private Call<SuccessBean> mCallDeleteModule;
    private Call<GetCustomModuleQuestionsBean> mCallGetCustomModuleQuestions;
    int completedQuestions=0;
    private BaseCustomDialog<DialogDeleteModuleBinding> mDeleteModuleDialog;

    @Override
    protected void onViewReady(Bundle savedInstanceState, Intent intent) {
        super.onViewReady(savedInstanceState, intent);

        initView();
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_going_to_start_module;
    }

    @Override
    public void onPermissionsGranted(int requestCode) {

    }

    @Override
    protected void onDestroy() {
        if (mCallDeleteModule != null) mCallDeleteModule.cancel();
        if (mCallGetCustomModuleQuestions != null) mCallGetCustomModuleQuestions.cancel();
        super.onDestroy();
    }

    private void initView() {
        mQuestionsList = new ArrayList<>();
        binding.toolbar.textTitle.setText(getString(R.string.start_your_custom_test));
        binding.toolbar.textTitle.setTextColor(Color.parseColor("#484848"));
        setBaseCallback(baseCallback);
        getCustomModuleFromRoom();
    }

    private BaseCallback baseCallback = view -> {
        switch (view.getId()) {
            case R.id.image_back:
                goBack();
                break;
            case R.id.button_action:
                goNextScreen();
                break;
            case R.id.text_discard_module:
                showDeleteModuleDialog();
                break;
        }
    };


    private void goNextScreen() {
        Bundle bundle = new Bundle();
        Intent intent = null;

        if (mGetCustomModuleBean != null) {
            //Test is completed now review the answers of the questions
//            if (Integer.parseInt(mGetCustomModuleBean.getHas_answered()) > 0) {
//                bundle.putString("module_id", String.valueOf(mGetCustomModuleBean.getId()));
//                bundle.putString("from", "CustomModule");
//                if (AppController.getInstance().isInternetOn())
//                    intent = new Intent(GoingToStartModuleActivity.this, ReviewTestActivity.class).putExtras(bundle);
//                else showToast(getString(R.string.no_internet));
//            }

            if(mQuestionsList.size()==completedQuestions){
                bundle.putString("module_id", String.valueOf(mGetCustomModuleBean.getId()));
                bundle.putString("from", "CustomModule");
                if (AppController.getInstance().isInternetOn())
                    intent = new Intent(GoingToStartModuleActivity.this, ReviewTestActivity.class).putExtras(bundle);
                else showToast(getString(R.string.no_internet));
            }
            //if already test is pending then direct jump on the Solvechapter activity
            else {
                if (mQuestionsList.size() > 0) {
                    bundle.putString("module_id", String.valueOf(mGetCustomModuleBean.getId()));
                    bundle.putInt("completedQuestions", completedQuestions);
                    bundle.putString("from", "CustomModule");
                    bundle.putString("mode", mGetCustomModuleBean.getMode());
                    bundle.putSerializable("customModuleQuestionsList", (Serializable) mQuestionsList);
                    intent = new Intent(GoingToStartModuleActivity.this, SolveTestChapterActivity.class).putExtras(bundle);
                } else showToast("Questions not found");
            }
        }

        if (intent != null) {
            intent.putExtras(bundle);
            startActivity(intent);
            goNextAnimation();
        }

    }

    private void showDeleteModuleDialog() {
        mDeleteModuleDialog = new BaseCustomDialog<>(this, R.layout.dialog_delete_module, view -> {
            switch (view.getId()) {
                case R.id.text_cancel:
                    mDeleteModuleDialog.dismiss();
                    break;
                case R.id.text_delete:
                    deleteCustomModuleApi();
                    break;
            }
        });
        Objects.requireNonNull(mDeleteModuleDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDeleteModuleDialog.setCancelable(true);
        mDeleteModuleDialog.show();
    }

    private void deleteCustomModuleApi() {
        mCallDeleteModule = AppController.getInstance().getApis().deleteCustomModule(getHeader());
        mCallDeleteModule.enqueue(new ResponseHandler<SuccessBean>() {
            @Override
            public void onSuccess(SuccessBean successBean) {

                if (successBean != null) {
                    if (successBean.getStatus() == Constants.SUCCESS_CODE) {
                        deleteCustomModuleFromDatabase();
                        showToast("Module discarded successfully");
                        goBack();
                    }
                }
            }

            @Override
            public void apiError(ApiErrorBean t) {

                if (t != null) showToast(t.getMessage());
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

    private void deleteCustomModuleFromDatabase() {
        mDBRepositoryKotlin.deleteLocalCustomModule();
    }

    @Override
    public void onStart() {
        super.onStart();
        getCustomModuleFromRoom();
    }

    @SuppressLint("SetTextI18n")
    private void setCustomModuleData(List<CustomModuleBean> bean) {
        this.mGetCustomModuleBean = bean.get(0);
        CustomModuleBean dataBean = bean.get(0);
        SimpleDateFormat dateFormatOne = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        SimpleDateFormat dateFormatTwo = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        getCustomModuleQuestionsFromRoom(mGetCustomModuleBean.getId());
        Date date = null;
        try {
            date = dateFormatOne.parse(dataBean.getCreated_at());
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        if (date != null) {
            binding.textDate.setText(dateFormatTwo.format(date));
        }
        binding.textModuleFrom.setText(Constants.QUESTIONS_FROM[Integer.parseInt(dataBean.getQuestions_from())]);
        if (dataBean.getSubjects().equals("0")) {
            binding.textTotalSubject.setText("Subject : All subjects selected");
        } else {
            String[] subjects = dataBean.getSubjects().split(",");
            binding.textTotalSubject.setText("Subject : " + subjects.length + " subject selected");
        }

        if (dataBean.getDifficulty().equals("-1")) {
            dataBean.setDifficulty("0");
        }
        binding.textDifficulty.setText("Difficulty : " + Constants.DIFFICULTY_LEVEL[Integer.parseInt(dataBean.getDifficulty())]);
        //binding.textMode.setText(Constants.MODES_TO_SHOW[Integer.parseInt(bean.getMode_index())] + " Mode");

        //if (PrefUtils.getInstance().getCustomModuleData())
        binding.textMode.setText(Constants.MODES_TO_SHOW[Integer.parseInt(dataBean.getMode())] + " : Mode");
        binding.textTotalQuestions.setText(dataBean.getNumber_of_questions() + " Questions");
        if (Integer.parseInt(dataBean.getHas_answered()) > 0) {
            binding.buttonAction.setText(getString(R.string.review_caps));
            binding.startCompleted.setText("Completed");
        }
        if (AppController.getInstance().isInternetOn())
            getCustomModuleQuestions(mGetCustomModuleBean.getId());
    }

    private void getCustomModuleQuestions(int moduleId) {
        mCallGetCustomModuleQuestions = AppController.getInstance().getApis().getCustomModuleQuestions(getHeader(), String.valueOf(moduleId));
        mCallGetCustomModuleQuestions.enqueue(new ResponseHandler<GetCustomModuleQuestionsBean>() {
            @Override
            public void onSuccess(GetCustomModuleQuestionsBean customModuleQuestionsBean) {

                if (customModuleQuestionsBean != null) {
                    if (customModuleQuestionsBean.getStatus() == Constants.SUCCESS_CODE) {
                        insertCustomModuleQuestionsInDatabase(customModuleQuestionsBean);
                    }
                }
            }
            @Override
            public void apiError(ApiErrorBean t) {

                if (t != null) showErrorToast(t.getMessage());
            }
            @Override
            public void onComplete(Call<GetCustomModuleQuestionsBean> call, Throwable t) {
                onCallComplete(call, t);
            }
            @Override
            public void statusCode(int t) {
                super.statusCode(t);
                handleCodes(t);
            }
        });
    }


    private void getCustomModuleFromRoom() {
        mDBRepository.getCustomModuleFromRoom().observe(this, list -> {
            if (list.size() > 0) {
                setCustomModuleData(list);
            }
        });
    }

    private void insertCustomModuleQuestionsInDatabase(GetCustomModuleQuestionsBean model) {
        for (int i = 0; i < model.getData().size(); i++) {
            model.getData().get(i).setCustomModuleId(mGetCustomModuleBean.getId());
        }
        if (model.getData() != null) mDBRepositoryKotlin.insertQuestions(model.getData());
    }

    @SuppressLint("SetTextI18n")
    private void getCustomModuleQuestionsFromRoom(int id) {
        mDBRepository.getCustomModuleQuestions(String.valueOf(id)).observe(this, list -> {
            int completed=0;
            if (list.size() > 0)
                mQuestionsList = list;
            for (int i=0;i<list.size();i++){
               for (int j=0;j<list.get(i).getOptions().size();j++){
                   if(Integer.parseInt(list.get(i).getOptions().get(j).getHasAnswered())==1){
                       completed+=1;
                   }
               }
            }
            completedQuestions=completed;
            if(mQuestionsList.size()!=completedQuestions){
                binding.startCompleted.setText(completedQuestions +" "+"Completed");
                binding.buttonAction.setText(getString(R.string.solve_caps));
            }else{
                binding.startCompleted.setText("Completed");
                binding.buttonAction.setText(getString(R.string.review_caps));
            }
            Log.i("completedQuestions",String.valueOf(completedQuestions));
        });
    }
}
