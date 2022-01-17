package com.flytrom.learning.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.flytrom.learning.R;
import com.flytrom.learning.adapters.FaqAdapter;
import com.flytrom.learning.base.BaseActivity;
import com.flytrom.learning.base.BaseCallback;
import com.flytrom.learning.databinding.ActivityFaqBinding;
import com.flytrom.learning.databinding.ActivityTermsBinding;
import com.flytrom.learning.interfaces.Apis;
import com.flytrom.learning.model.DatumFaq;
import com.flytrom.learning.model.FaqModle;
import com.flytrom.learning.utils.AppController;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TermsActivity extends BaseActivity<ActivityTermsBinding> {
    Apis apiInterface;
    FaqAdapter faqAdapter;

    @Override
    protected int getContentView() {
        return R.layout.activity_terms;
    }

    protected void onViewReady(Bundle savedInstanceState, Intent intent) {
        super.onViewReady(savedInstanceState, intent);
        apiInterface = AppController.getInstance().getApis();
        binding.toolbarTerms.textTitle.setText(getString(R.string.text_terms_conditions));
        setBaseCallback(baseCallback);
        fetFaqApi();

    }

    private void fetFaqApi() {
        apiInterface.getFaq(getHeader(),"1","1").enqueue(new Callback<FaqModle>() {
            @Override
            public void onResponse(Call<FaqModle> call, Response<FaqModle> response) {
                FaqModle faqModle = response.body();
                if (response.isSuccessful()){
                    setAdapter(faqModle.getData());
                }else {
                    Toast.makeText(TermsActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<FaqModle> call, Throwable t) {
                Log.d("QWERTYU",t.getLocalizedMessage());

            }
        });

    }
    private BaseCallback baseCallback = view -> {
        switch (view.getId()) {
            case R.id.image_back:
                goBack();
                break;

        }
    };
    private void setAdapter(List<DatumFaq> data) {
        faqAdapter = new FaqAdapter(data,TermsActivity.this);
        binding.rlTerms.setHasFixedSize(true);
        binding.rlTerms.setLayoutManager(new LinearLayoutManager(TermsActivity.this));
        binding.rlTerms.setAdapter(faqAdapter);
    }

    @Override
    public void onPermissionsGranted(int requestCode) {

    }
}