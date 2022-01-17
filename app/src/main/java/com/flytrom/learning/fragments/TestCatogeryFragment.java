package com.flytrom.learning.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.flytrom.learning.R;
import com.flytrom.learning.activities.FreeTestActivity;
import com.flytrom.learning.activities.QBankActivity;
import com.flytrom.learning.activities.TestActivity;
import com.flytrom.learning.activities.q_bank_menu.CreateCustomModuleActivity;
import com.flytrom.learning.activities.q_bank_menu.GoingToStartModuleActivity;
import com.flytrom.learning.activities.q_bank_menu.TestsActivity;
import com.flytrom.learning.adapters.QBankCategoryAdapter;
import com.flytrom.learning.adapters.TestCategoryAdapter;
import com.flytrom.learning.base.BaseCallback;
import com.flytrom.learning.base.BaseFragment;
import com.flytrom.learning.databinding.FragmentTestCatogeryBinding;
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


public class TestCatogeryFragment extends BaseFragment<FragmentTestCatogeryBinding> implements TestCategoryAdapter.OnItemClickListnerTest {

    Apis apiInterface;
    TestCategoryAdapter bankCategoryAdapter;
    RecyclerView recyclerQBank2;
    private boolean mIsCustomModuleExist;
    @Override
    protected void onFragmentCreateView(Bundle savedInstanceState) {
        super.onFragmentCreateView(savedInstanceState);
        apiInterface = AppController.getInstance().getApis();
        recyclerQBank2 = binding.getRoot().findViewById(R.id.recycler_test);
        getCategoryApi();
        setBaseCallback(callback);
    }

    @Override
    public int getFragmentLayout() {
        return R.layout.fragment_test_catogery;
    }

    private void getCategoryApi() {
        showBaseProgress();
        apiInterface.main_categories(getHeader(),"1", "test_order", "tests_visibility").enqueue(new Callback<MainCategory>() {
            @Override
            public void onResponse(Call<MainCategory> call, Response<MainCategory> response) {
                MainCategory mainCategory = response.body();
               hideBaseProgress();
                if (response.isSuccessful()){

                    if (mainCategory.getStatus() == 202){
                        binding.rlTest.setVisibility(View.VISIBLE);
                    }
                    if (mainCategory.getStatus() == 200){
                        setMainAdapter(mainCategory.data);
                        binding.rlTest.setVisibility(View.GONE);
                    }
                }else {
                    Toast.makeText(basecontext, "Error", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<MainCategory> call, Throwable t) {
               hideBaseProgress();
                Log.d("QWERTYU",t.getLocalizedMessage());

            }
        });
    }

    private void setMainAdapter(List<DataCategory> data) {
        bankCategoryAdapter = new TestCategoryAdapter(data,getContext(),this::onitemClickBank);
        recyclerQBank2.setHasFixedSize(true);
        recyclerQBank2.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerQBank2.setAdapter(bankCategoryAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        apiInterface = AppController.getInstance().getApis();
        recyclerQBank2 = binding.getRoot().findViewById(R.id.recycler_test);
        getCategoryApi();
    }


    private BaseCallback callback = view -> {
        switch (view.getId()) {
            case R.id.card_custom_module2:
                MySharedPreferences.getInstance().saveString(getActivity(), ConstantsNew.CLICK_NUM,"1");
                if (mIsCustomModuleExist) {
                    startActivity(new Intent(getActivity(), GoingToStartModuleActivity.class));
                } else {
                    startActivity(new Intent(getActivity(), CreateCustomModuleActivity.class));
                }
                goNextAnimation();
                break;
        case R.id.cardFreeTest:
            Bundle bundle = new Bundle();
            bundle.putString("from", "testMenu");
            bundle.putString("type", "Free");
            startActivity(new Intent(getActivity(), FreeTestActivity.class).putExtras(bundle));
            goNextAnimation();
            break;
        }
    };



    @Override
    public void onitemClickBank(String id, String subject, String image,String name) {
        if (subject.equalsIgnoreCase("0")){
            Toast.makeText(basecontext, "No Subject", Toast.LENGTH_SHORT).show();
        }else {

            Intent intent = new Intent(getActivity(),TestActivity.class);
            intent.putExtra("CategoryId", id);
            intent.putExtra("Subject", subject);
            intent.putExtra("image", image);
            intent.putExtra("name", name);
            startActivity(intent);
        }

    }
}