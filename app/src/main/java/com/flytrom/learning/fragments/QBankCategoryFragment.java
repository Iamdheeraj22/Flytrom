package com.flytrom.learning.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.databinding.library.baseAdapters.BR;
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
import com.flytrom.learning.activities.FreeQBankActivity;
import com.flytrom.learning.activities.LecturesActivity;
import com.flytrom.learning.activities.QBankActivity;
import com.flytrom.learning.activities.q_bank_menu.ChaptersActivity;
import com.flytrom.learning.activities.q_bank_menu.CreateCustomModuleActivity;
import com.flytrom.learning.activities.q_bank_menu.GoingToStartModuleActivity;
import com.flytrom.learning.adapters.QBankCategoryAdapter;
import com.flytrom.learning.base.BaseCallback;
import com.flytrom.learning.base.BaseFragment;
import com.flytrom.learning.base.SimpleRecyclerViewAdapter;
import com.flytrom.learning.databinding.FragmentQBankCategoryBinding;
import com.flytrom.learning.interfaces.Apis;
import com.flytrom.learning.model.MainCategorey.DataCategory;
import com.flytrom.learning.model.MainCategorey.MainCategory;
import com.flytrom.learning.utils.AppController;
import com.flytrom.learning.utils.Constants;
import com.flytrom.learning.utils.ConstantsNew;
import com.flytrom.learning.utils.MySharedPreferences;
import com.flytrom.learning.utils.PrefUtils;
import com.flytrom.learning.utils.VerticalPagination;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QBankCategoryFragment extends BaseFragment<FragmentQBankCategoryBinding> implements QBankCategoryAdapter.OnItemClickListnerBank {
    Apis apiInterface;
    QBankCategoryAdapter bankCategoryAdapter;
    RecyclerView recyclerQBank2;
    private boolean mIsCustomModuleExist;
    @Override
    protected void onFragmentCreateView(Bundle savedInstanceState) {
        super.onFragmentCreateView(savedInstanceState);
        apiInterface = AppController.getInstance().getApis();
        recyclerQBank2 = binding.getRoot().findViewById(R.id.recycler_q_bank2);
        getCategoryApi();
        setBaseCallback(callback);
    }

    @Override
    public int getFragmentLayout() {
        return R.layout.fragment_q_bank_category;
    }


    @Override
    public void onResume() {
        super.onResume();
        apiInterface = AppController.getInstance().getApis();
        recyclerQBank2 = binding.getRoot().findViewById(R.id.recycler_q_bank2);
        getCategoryApi();
    }

    private void getCategoryApi() {
        showBaseProgress();
        apiInterface.main_categories(getHeader(),"1", "qb", "question_bank_visibility").enqueue(new Callback<MainCategory>() {
            @Override
            public void onResponse(Call<MainCategory> call, Response<MainCategory> response) {
                MainCategory mainCategory = response.body();
                hideBaseProgress();
                if (response.isSuccessful()){
                    if (mainCategory.getStatus() == 202){
                        binding.rlQBank.setVisibility(View.VISIBLE);
                    }
                    if (mainCategory.getStatus() == 200){
                        setMainAdapter(mainCategory.data);
                        binding.rlQBank.setVisibility(View.GONE);
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
        bankCategoryAdapter = new QBankCategoryAdapter(data,getContext(),this::onitemClickBankNew);
        recyclerQBank2.setHasFixedSize(true);
        recyclerQBank2.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerQBank2.setAdapter(bankCategoryAdapter);
    }




    private BaseCallback callback = view -> {
        switch (view.getId()) {
            case R.id.card_custom_module2:
                MySharedPreferences.getInstance().saveString(getActivity(), ConstantsNew.CLICK_NUM,"0");
                if (mIsCustomModuleExist) {
                    startActivity(new Intent(getActivity(), GoingToStartModuleActivity.class));
                } else {
                    startActivity(new Intent(getActivity(), CreateCustomModuleActivity.class));
                }
                goNextAnimation();
                break;
        case R.id.card_Qbank:
            Bundle bundle = new Bundle();
            bundle.putString("type", Constants.FREE);
            startActivity(new Intent(getActivity(), FreeQBankActivity.class).putExtras(bundle));
                goNextAnimation();
                break;
        }
    };

    @Override
    public void onitemClickBankNew(String id, String subject, String image,String name) {
        if (subject.equalsIgnoreCase("0")){
            Toast.makeText(basecontext, "No Subjects", Toast.LENGTH_SHORT).show();
        }else {
//            Fragment fr= new QBankFragment();
//            FragmentManager fm=getFragmentManager();
//            FragmentTransaction ft=fm.beginTransaction();
//            Bundle args = new Bundle();
//            args.putString("CategoryId", id);
//            fr.setArguments(args);
//            ft.replace(R.id.frame_layout, fr);
//            ft.commit();
            Intent intent = new Intent(getActivity(), QBankActivity.class);
            intent.putExtra("CategoryId", id);
            intent.putExtra("Subject", subject);
            intent.putExtra("image", image);
            intent.putExtra("name", name);

            Log.i("QCategoryInfo","id:-"+id);
            Log.i("QCategoryInfo","subject:-"+subject);
            Log.i("QCategoryInfo","image:-"+image);
            Log.i("QCategoryInfo","name:-"+name);


            /*id:-13
            * subject:-3
            * image:-main_category_images/2021-09/20210908_EP-2030-1631088797.jpg
            * name:-Commercial Pilot License
            * */


            startActivity(intent);
        }

    }
}