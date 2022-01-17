package com.flytrom.learning.activities.q_bank_menu;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.flytrom.learning.R;
import com.flytrom.learning.base.BaseActivity;
import com.flytrom.learning.base.BaseCallback;
import com.flytrom.learning.beans.normal_beans.ApiErrorBean;
import com.flytrom.learning.beans.normal_beans.CustomModuleDataBean;
import com.flytrom.learning.beans.response_beans.custom_module.GetCustomModuleBean;
import com.flytrom.learning.databinding.ActivityCustomModuleBinding;
import com.flytrom.learning.fragments.CategoryCustomFragment;
import com.flytrom.learning.fragments.custom_module.QuestionsFromFragment;
import com.flytrom.learning.fragments.custom_module.SelectModeFragment;
import com.flytrom.learning.fragments.custom_module.SelectSubjectsFragment;
import com.flytrom.learning.utils.AppController;
import com.flytrom.learning.utils.BackStackManager;
import com.flytrom.learning.utils.Constants;
import com.flytrom.learning.utils.ConstantsNew;
import com.flytrom.learning.utils.MySharedPreferences;
import com.flytrom.learning.utils.ResponseHandler;

import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Stack;

import retrofit2.Call;

public class CreateCustomModuleActivity extends BaseActivity<ActivityCustomModuleBinding> {

    public static final String TAG_CUSTOM_MODULE = "CreateCustomModuleActivity";
    private FragmentManager manager;
    private FragmentTransaction fragmentTransaction;
    Stack<Fragment> s = new Stack<>();
    private int mMenuCounter = 0;
    private Call<GetCustomModuleBean> mCallCreateCustomModule;
    private CustomModuleDataBean mCustomModuleDataBean;
    private Call<GetCustomModuleBean> mCallGetCustomModule;
    public static boolean radioSelect = false;

    @Override
    protected void onViewReady(Bundle savedInstanceState, Intent intent) {
        super.onViewReady(savedInstanceState, intent);
        MySharedPreferences.getInstance().saveString(CreateCustomModuleActivity.this, ConstantsNew.CAT_ID,"");
        initView();
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_custom_module;
    }

    @Override
    public void onPermissionsGranted(int requestCode) {

    }

    @Override
    protected void onDestroy() {
        BackStackManager.getInstance(this).removeKey(TAG_CUSTOM_MODULE);
        if (mCallCreateCustomModule != null) mCallCreateCustomModule.cancel();
        super.onDestroy();
    }

    private void initView() {
        mCustomModuleDataBean = new CustomModuleDataBean();
        manager = getSupportFragmentManager();
        fragmentTransaction = manager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.alpha_undim,
                R.anim.alpha_dim,
                R.anim.alpha_undim,
                R.anim.alpha_dim);
        binding.toolbar.textTitle.setText(getString(R.string.create_custom_test));
        binding.toolbar.textTitle.setTextColor(Color.parseColor("#484848"));
        replaceFragment(TAG_CUSTOM_MODULE, new QuestionsFromFragment(), true);
        setBaseCallback(baseCallback);
        getCustomModuleFromRoom();
    }

    private BaseCallback baseCallback = view -> {
        switch (view.getId()) {
            case R.id.image_back:
                finishActivityWithBackAnim();
                break;
            case R.id.image_next:
                Log.d("HFYFYUGIGUI", String.valueOf(mMenuCounter));
                switch (mMenuCounter) {
                    case 0:
                        mCustomModuleDataBean.setNumber_of_questions(String.valueOf(QuestionsFromFragment.self.mNoOfQuestions));
                        mCustomModuleDataBean.setDifficulty(String.valueOf(QuestionsFromFragment.self.mDifficultyLevel));
                        mCustomModuleDataBean.setQuestions_from(String.valueOf(QuestionsFromFragment.self.mQuestionsFrom));
                        //binding.imagePrevious.setVisibility(View.VISIBLE);
                        replaceSubFragment(TAG_CUSTOM_MODULE, new CategoryCustomFragment(), true);
                        mMenuCounter++;
                        setViewsColor();
                        break;
                    case 1:
                        if (radioSelect== true){
                            replaceSubFragment(TAG_CUSTOM_MODULE, new SelectSubjectsFragment(), true);
                            mMenuCounter++;
                            setViewsColor();
                        }else {
                            Toast.makeText(this, "Please Select Category", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 2:
                        if (SelectSubjectsFragment.self.getSelectedSubjects() != null) {
                            mCustomModuleDataBean.setSubjects(SelectSubjectsFragment.self.getSelectedSubjects());
                            mCustomModuleDataBean.setTotalSelectedSubjects(String.valueOf(SelectSubjectsFragment.self.mTotalSubjectsSelected));
                            replaceSubFragment(TAG_CUSTOM_MODULE, new SelectModeFragment(), true);
                            mMenuCounter++;
                            setViewsColor();
                            String selectedChapters = SelectSubjectsFragment.self.getSelectedChapters();
                            if (selectedChapters != null)
                                mCustomModuleDataBean.setTotalSelectedChapters(selectedChapters);
                        } else {
                            showToast("Please select subjects");
                        }
                        break;

                    case 3:
                        mCustomModuleDataBean.setMode_index(String.valueOf(SelectModeFragment.self.mSelectedIndex));
                        if (mCustomModuleDataBean != null) {
                            if (AppController.getInstance().isInternetOn()) createCustomModule();
                            else showToast("No internet to save your module");
                        }
                        break;
                }
                break;
            case R.id.image_previous:
                Log.d("HFYFYUGIGUI", String.valueOf(mMenuCounter));
                try {
                    BackStackManager.getInstance(this).removeSubFragment(TAG_CUSTOM_MODULE, R.id.frame_layout, true);
                } catch (NoSuchElementException ex) {
                    BackStackManager.getInstance(this).getBackStack().remove(TAG_CUSTOM_MODULE);
                }
              //  Objects.requireNonNull(BackStackManager.getInstance(this).getBackStack().get(TAG_CUSTOM_MODULE)).clear();
                setViewsColor2();
                mMenuCounter--;
                Log.d("HHHJH", String.valueOf(mMenuCounter));
                break;
        }
    };


    @Override
    public void onBackPressed() {

        if (mMenuCounter > 0) {
            try {
                BackStackManager.getInstance(this).removeSubFragment(TAG_CUSTOM_MODULE, R.id.frame_layout, true);
            } catch (NoSuchElementException ex) {
                BackStackManager.getInstance(this).getBackStack().remove(TAG_CUSTOM_MODULE);
            }
            //  Objects.requireNonNull(BackStackManager.getInstance(this).getBackStack().get(TAG_CUSTOM_MODULE)).clear();
            setViewsColor2();
            mMenuCounter--;
        } else {
            super.onBackPressed();
        }
    }

    private void createCustomModule() {
        showBaseProgress();
        HashMap<String, String> map = new HashMap<>();
        map.put("number_of_questions", mCustomModuleDataBean.getNumber_of_questions());
        map.put("difficulty", mCustomModuleDataBean.getDifficulty());
        map.put("questions_from", mCustomModuleDataBean.getQuestions_from());
        map.put("subjects", mCustomModuleDataBean.getSubjects());
        if (mCustomModuleDataBean.getTotalSelectedChapters() != null)
            map.put("chapters", mCustomModuleDataBean.getTotalSelectedChapters());
        map.put("mode", mCustomModuleDataBean.getMode_index());

        mCallCreateCustomModule = AppController.getInstance().getApis().
                createCustomModule(getHeader(), map);
        mCallCreateCustomModule.enqueue(new ResponseHandler<GetCustomModuleBean>() {
            @Override
            public void onSuccess(GetCustomModuleBean moduleBean) {
                if (moduleBean != null) {
                    if (moduleBean.getStatus() == Constants.SUCCESS_CODE) {
                        getCustomModule();
                        showToast("Module created successfully");
                    }
                }
            }

            @Override
            public void apiError(ApiErrorBean t) {

                if (t != null) showErrorToast(t.getMessage());
            }

            @Override
            public void onComplete(Call<GetCustomModuleBean> call, Throwable t) {

                onCallComplete(call, t);
            }

            @Override
            public void statusCode(int t) {
                super.statusCode(t);
                handleCodes(t);
            }
        });
    }

    private void getCustomModule() {
        showBaseProgress();
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
                onCallComplete(call, t);
            }

            @Override
            public void statusCode(int t) {
                super.statusCode(t);
                handleCodes(t);
            }
        });
    }
    private void setViewsColor() {
        switch (mMenuCounter) {
           case 0:
                binding.viewOne.setBackgroundColor(getResources().getColor(R.color.blue_text_color));
                binding.viewTwo.setBackgroundColor(getResources().getColor(R.color.gray_light_three));
                binding.viewThree.setBackgroundColor(getResources().getColor(R.color.gray_light_three));
                binding.viewFour.setBackgroundColor(getResources().getColor(R.color.gray_light_three));
                break;
             case 1:
                binding.viewOne.setBackgroundColor(getResources().getColor(R.color.blue_text_color));
                binding.viewTwo.setBackgroundColor(getResources().getColor(R.color.blue_text_color));
                binding.viewThree.setBackgroundColor(getResources().getColor(R.color.gray_light_three));
                binding.viewFour.setBackgroundColor(getResources().getColor(R.color.gray_light_three));
                break;
            case 2:
                binding.viewOne.setBackgroundColor(getResources().getColor(R.color.blue_text_color));
                binding.viewTwo.setBackgroundColor(getResources().getColor(R.color.blue_text_color));
                binding.viewThree.setBackgroundColor(getResources().getColor(R.color.blue_text_color));
                binding.viewFour.setBackgroundColor(getResources().getColor(R.color.gray_light_three));
                break;
            case 3:
                binding.viewOne.setBackgroundColor(getResources().getColor(R.color.blue_text_color));
                binding.viewTwo.setBackgroundColor(getResources().getColor(R.color.blue_text_color));
                binding.viewThree.setBackgroundColor(getResources().getColor(R.color.blue_text_color));
                binding.viewFour.setBackgroundColor(getResources().getColor(R.color.blue_text_color));
                break;
        }
    }
    private void setViewsColor2() {
        switch (mMenuCounter) {
            case 1:
                binding.viewOne.setBackgroundColor(getResources().getColor(R.color.blue_text_color));
                binding.viewTwo.setBackgroundColor(getResources().getColor(R.color.gray_light_three));
                binding.viewThree.setBackgroundColor(getResources().getColor(R.color.gray_light_three));
                binding.viewFour.setBackgroundColor(getResources().getColor(R.color.gray_light_three));
                break;
            case 2:
                binding.viewOne.setBackgroundColor(getResources().getColor(R.color.blue_text_color));
                binding.viewTwo.setBackgroundColor(getResources().getColor(R.color.blue_text_color));
                binding.viewThree.setBackgroundColor(getResources().getColor(R.color.gray_light_three));
                binding.viewFour.setBackgroundColor(getResources().getColor(R.color.gray_light_three));
                break;
            case 3:
                binding.viewOne.setBackgroundColor(getResources().getColor(R.color.blue_text_color));
                binding.viewTwo.setBackgroundColor(getResources().getColor(R.color.blue_text_color));
                binding.viewThree.setBackgroundColor(getResources().getColor(R.color.blue_text_color));
                binding.viewFour.setBackgroundColor(getResources().getColor(R.color.gray_light_three));
                break;
            }
    }
    public void addFragment(Fragment fragment) {
        s.push(fragment);
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    //public void removeFragment(Fragment fragment);

    public void replaceFragment(String tag, Fragment fragment, boolean animation) {
        BackStackManager.getInstance(this).pushFragments(R.id.frame_layout, tag, fragment, animation);
    }

    public void replaceSubFragment(String tag, Fragment fragment, boolean animation) {
        BackStackManager.getInstance(this).pushSubFragments(R.id.frame_layout, tag, fragment, animation);
    }

    private void insertCustomModuleInDatabase(GetCustomModuleBean model) {
        if (model.getData() != null)
            mDBRepositoryKotlin.insertCustomModule(model.getData());
    }

    private void getCustomModuleFromRoom() {
        mDBRepository.getCustomModuleFromRoom().observe(this, list -> {
            if (list.size() > 0) {
                goNextForStartModule();
            }
        });
    }

    private void goNextForStartModule() {
        startActivity(new Intent(CreateCustomModuleActivity.this, GoingToStartModuleActivity.class));
        goNextAnimation();
        finish();
    }
}
