package com.flytrom.learning.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.flytrom.learning.R;
import com.flytrom.learning.activities.LecturesActivity;
import com.flytrom.learning.activities.LiveFreeActivity;
import com.flytrom.learning.activities.LiveVideo.LiveNewActivity;
import com.flytrom.learning.activities.LiveVideoActivity;
import com.flytrom.learning.activities.q_bank_menu.CreateCustomModuleActivity;
import com.flytrom.learning.activities.q_bank_menu.GoingToStartModuleActivity;
import com.flytrom.learning.activities.video_menu.FreeVideosActivity;
import com.flytrom.learning.adapters.VideoCategoryAdapter;
import com.flytrom.learning.base.BaseCallback;
import com.flytrom.learning.base.BaseFragment;
import com.flytrom.learning.beans.response_beans.videos.VideoBean;
import com.flytrom.learning.databinding.FragmentVideoCategoryBinding;
import com.flytrom.learning.fragments.video.LecturesFragment;
import com.flytrom.learning.interfaces.Apis;
import com.flytrom.learning.model.MainCategorey.DataCategory;
import com.flytrom.learning.model.MainCategorey.MainCategory;
import com.flytrom.learning.utils.AppController;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Video_Category_Fragment extends BaseFragment<FragmentVideoCategoryBinding>
        implements VideoCategoryAdapter.OnItemClickListnerVideo{

    Apis apiInterface;
    VideoCategoryAdapter bankCategoryAdapter;
    RecyclerView recyclerQBank2;
    VideoBean videoBean;
    private boolean mIsCustomModuleExist;
    @Override
    protected void onFragmentCreateView(Bundle savedInstanceState) {
        super.onFragmentCreateView(savedInstanceState);
        apiInterface = AppController.getInstance().getApis();
        recyclerQBank2 = binding.getRoot().findViewById(R.id.recycler_video);
        getCategoryApi();
        setBaseCallback(callback);
    }

    @Override
    public int getFragmentLayout() {
        return R.layout.fragment_video__category_;
    }

    private void getCategoryApi() {
        showBaseProgress();
        apiInterface.main_categories(getHeader(),"1", "video_order", "videos_visibility").enqueue(new Callback<MainCategory>() {
            @Override
            public void onResponse(Call<MainCategory> call, Response<MainCategory> response) {
                MainCategory mainCategory = response.body();
                hideBaseProgress();
                if (response.isSuccessful()){
                    if (mainCategory.getStatus() == 202){
                        binding.rlVideo.setVisibility(View.VISIBLE);
                    }
                    if (mainCategory.getStatus() == 200){
                        setMainAdapter(mainCategory.data);
                        binding.rlVideo.setVisibility(View.GONE);
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
        bankCategoryAdapter = new VideoCategoryAdapter(data,getContext(),this::onitemClickVideo);
        recyclerQBank2.setHasFixedSize(true);
        recyclerQBank2.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerQBank2.setAdapter(bankCategoryAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        apiInterface = AppController.getInstance().getApis();
        recyclerQBank2 = binding.getRoot().findViewById(R.id.recycler_video);
        getCategoryApi();
    }


    private BaseCallback callback = view -> {
        switch (view.getId()) {
            case R.id.card_custom_module2:
                startActivity(new Intent(getActivity(), LiveNewActivity.class));
//                startActivity(new Intent(getActivity(), LiveFreeActivity.class));
//                startActivity(new Intent(getActivity(), LiveVideoActivity.class));
                goNextAnimation();
                break;
          case R.id.freeLecture:
              startActivity(new Intent(getActivity(), FreeVideosActivity.class));
                goNextAnimation();
                break;
        }
    };

    @Override
    public void onitemClickVideo(String id, String subject, String image,String name) {
        if (subject.equalsIgnoreCase("0")){
            Toast.makeText(basecontext, "No Subject", Toast.LENGTH_SHORT).show();
        }else {

            Intent intent = new Intent(getActivity(), LecturesActivity.class);
            intent.putExtra("CategoryId", id);
            intent.putExtra("Subject", subject);
            intent.putExtra("image", image);
            intent.putExtra("name", name);
            startActivity(intent);
        }
    }
}