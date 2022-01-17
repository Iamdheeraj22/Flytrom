package com.flytrom.learning.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.flytrom.learning.R;
import com.flytrom.learning.activities.q_bank_menu.CreateCustomModuleActivity;
import com.flytrom.learning.adapters.QBankCategoryAdapter;
import com.flytrom.learning.adapters.QBankCustomCreateAdapter;
import com.flytrom.learning.base.BaseFragment;
import com.flytrom.learning.databinding.FragmentCategoryCustomBinding;
import com.flytrom.learning.databinding.FragmentQBankCategoryBinding;
import com.flytrom.learning.fragments.custom_module.QuestionsFromFragment;
import com.flytrom.learning.interfaces.Apis;
import com.flytrom.learning.model.MainCategorey.DataCategory;
import com.flytrom.learning.model.MainCategorey.MainCategory;
import com.flytrom.learning.utils.AppController;
import com.flytrom.learning.utils.ConstantsNew;
import com.flytrom.learning.utils.MySharedPreferences;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryCustomFragment extends BaseFragment<FragmentCategoryCustomBinding> implements QBankCustomCreateAdapter.OnItemClickListnerBank {
    Apis apiInterface;
    public int mNoOfQuestions = 10;
    QBankCustomCreateAdapter qBankCustomCreateAdapter;
    public static CategoryCustomFragment self;
    @Override
    protected void onFragmentCreateView(Bundle savedInstanceState) {
        super.onFragmentCreateView(savedInstanceState);
        apiInterface = AppController.getInstance().getApis();
        ((CreateCustomModuleActivity)this.getActivity()).radioSelect = false;
        if (MySharedPreferences.getInstance().getString(getActivity(),ConstantsNew.CLICK_NUM).equalsIgnoreCase("0")){
            getCategoryApi();
        }else {
            getCategoryApi2();
        }

    }


    @Override
    public int getFragmentLayout() {
        return R.layout.fragment_category_custom;
    }

    private void getCategoryApi() {
        apiInterface.main_categories(getHeader(),"1", "qb", "question_bank_visibility").enqueue(new Callback<MainCategory>() {
            @Override
            public void onResponse(Call<MainCategory> call, Response<MainCategory> response) {
                MainCategory mainCategory = response.body();
                if (response.isSuccessful()){
                    if (mainCategory.getStatus() == 202){
                        binding.rlQBankCustom.setVisibility(View.VISIBLE);
                    }
                    if (mainCategory.getStatus() == 200){
                        setMainAdapter(mainCategory.data);
                        binding.rlQBankCustom.setVisibility(View.GONE);
                    }

                }else {
                    Toast.makeText(basecontext, "Error", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<MainCategory> call, Throwable t) {
                Log.d("QWERTYU",t.getLocalizedMessage());

            }
        });
    }
    private void getCategoryApi2() {
        apiInterface.main_categories(getHeader(),"1", "test_order", "tests_visibility").enqueue(new Callback<MainCategory>() {
            @Override
            public void onResponse(Call<MainCategory> call, Response<MainCategory> response) {
                MainCategory mainCategory = response.body();
                if (response.isSuccessful()){
                    if (mainCategory.getStatus() == 202){
                        binding.rlQBankCustom.setVisibility(View.VISIBLE);
                    }
                    if (mainCategory.getStatus() == 200){
                        setMainAdapter(mainCategory.data);
                        binding.rlQBankCustom.setVisibility(View.GONE);
                    }

                }else {
                    Toast.makeText(basecontext, "Error", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<MainCategory> call, Throwable t) {
                Log.d("QWERTYU",t.getLocalizedMessage());

            }
        });
    }
    private void setMainAdapter(List<DataCategory> data) {
        qBankCustomCreateAdapter = new QBankCustomCreateAdapter(data,getContext(),this::onitemClickBankNew);
        binding.recyclerQBankCustom.setHasFixedSize(true);
        binding.recyclerQBankCustom.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.recyclerQBankCustom.setAdapter(qBankCustomCreateAdapter);
    }


    @Override
    public void onitemClickBankNew(String id) {
        ((CreateCustomModuleActivity)this.getActivity()).radioSelect = true;
        MySharedPreferences.getInstance().saveString(getContext(),ConstantsNew.CAT_ID,id);
    }
}