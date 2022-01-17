package com.flytrom.learning.activities.info;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.flytrom.learning.R;
import com.flytrom.learning.base.BaseActivity;
import com.flytrom.learning.base.BaseCallback;
import com.flytrom.learning.beans.normal_beans.ApiErrorBean;
import com.flytrom.learning.beans.response_beans.others.SuccessBean;
import com.flytrom.learning.databinding.ActivityReportPiracyBinding;
import com.flytrom.learning.utils.AppController;
import com.flytrom.learning.utils.Constants;
import com.flytrom.learning.utils.FieldsValidator;
import com.flytrom.learning.utils.ProgressAnimation;
import com.flytrom.learning.utils.ProgressRequestBody;
import com.flytrom.learning.utils.ResponseHandler;

import java.io.File;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import timber.log.Timber;

public class ReportPiracyActivity extends BaseActivity<ActivityReportPiracyBinding> implements ProgressRequestBody.UploadCallbacks {

    private Call<SuccessBean> mCallReportPiracy;
    private FieldsValidator mFieldValidator;
    private String selectedMediaPath;

    @Override
    protected void onViewReady(Bundle savedInstanceState, Intent intent) {
        super.onViewReady(savedInstanceState, intent);
        initView();
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_report_piracy;
    }

    @Override
    public void onPermissionsGranted(int requestCode) {

    }

    @Override
    protected void onDestroy() {
        if (mCallReportPiracy != null) mCallReportPiracy.cancel();
        super.onDestroy();
    }

    private void initView() {
        mFieldValidator = new FieldsValidator();
        binding.toolbar.textTitle.setText(getString(R.string.text_report_piracy));
        binding.toolbar.textTitle.setTextColor(Color.parseColor("#484848"));
        setBaseCallback(baseCallback);
    }

    private BaseCallback baseCallback = view -> {
        switch (view.getId()) {
            case R.id.image_back:
                goBack();
                break;
            case R.id.button_submit:
                if (mFieldValidator.hasText(binding.editTextDescription))
                    reportPiracy();
                break;
            case R.id.button_reset:
                resetData();
                break;
            case R.id.image_file:
                getMediaFromPhone();
                break;
            case R.id.image_cancel:
                binding.imageFile.setImageResource(R.drawable.ic_file_upload);
                binding.imageCancel.setVisibility(View.GONE);
                selectedMediaPath = null;
                break;
        }
    };

    private void getMediaFromPhone() {
        Intent intentPhotoVideoPicker = new Intent(Intent.ACTION_PICK);
        intentPhotoVideoPicker.setType("*/*");
        intentPhotoVideoPicker.putExtra(Intent.EXTRA_MIME_TYPES, new String[]{"image/*", "video/*"});
        startActivityForResult(intentPhotoVideoPicker, 111);
    }

    private void resetData() {
        binding.editTextName.setText("");
        binding.editTextEmail.setText("");
        binding.editTextMobileNumber.setText("");
        binding.editTextDescription.setText("");
        binding.imageFile.setImageResource(R.drawable.ic_file_upload);
        binding.imageCancel.setVisibility(View.GONE);
        selectedMediaPath = null;
    }

    private void reportPiracy() {
        showAvlIndicator();
        if (mCallReportPiracy != null) mCallReportPiracy.cancel();
        HashMap<String, RequestBody> map = new HashMap<>();

        RequestBody name = RequestBody.create(MediaType.parse("multipart/form-data"),
                binding.editTextName.getText()
                        .toString());
        RequestBody email_address = RequestBody.create(MediaType.parse("multipart/form-data"),
                binding.editTextEmail.getText()
                        .toString());
        RequestBody mobile_number = RequestBody.create(MediaType.parse("multipart/form-data"),
                binding.editTextMobileNumber.getText()
                        .toString());
        RequestBody description = RequestBody.create(MediaType.parse("multipart/form-data"),
                binding.editTextDescription.getText()
                        .toString());

        map.put("name", name);
        map.put("email_address", email_address);
        map.put("mobile_number", mobile_number);
        map.put("description", description);

        MultipartBody.Part part = null;
        if (selectedMediaPath != null) {
            //binding.relativeProgress.setVisibility(View.VISIBLE);
            File file = new File(selectedMediaPath);
            ProgressRequestBody progressRequestBody = new ProgressRequestBody(file, this);
            part = MultipartBody.Part.createFormData("file", (file.getName() + ""), progressRequestBody);
            Timber.e(("im1 " + file.getName()));
        }

        mCallReportPiracy = AppController.getInstance().getApis().reportPiracy(getHeader(), map, part);
        mCallReportPiracy.enqueue(new ResponseHandler<SuccessBean>() {
            @Override
            public void onSuccess(SuccessBean successBean) {

                if (successBean != null) {
                    if (successBean.getStatus() == Constants.SUCCESS_CODE) {
                        showToast(successBean.getMessage());
                        onBackPressed();
                    }
                }
            }

            @Override
            public void apiError(ApiErrorBean t) {
                hideAvlIndicator();
                if (t != null) showToast(t.getMessage());
            }

            @Override
            public void onComplete(Call<SuccessBean> call, Throwable t) {
                hideAvlIndicator();
                onCallComplete(call, t);
            }

            @Override
            public void statusCode(int t) {
                super.statusCode(t);
                handleCodes(t);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == 111) {
                if (data != null) {

                    Uri selectedImageUri = data.getData();

                    // MEDIA GALLERY
                    String selectedImagePath = getPath(selectedImageUri);
                    if (selectedImagePath != null) {
                        Glide.with(this)
                                .load(selectedImagePath)
                                .centerCrop()
                                .apply(RequestOptions.circleCropTransform())
                                .placeholder(R.drawable.ic_user_placeholder)
                                .into(binding.imageFile);

                        this.selectedMediaPath = selectedImagePath;
                        binding.imageCancel.setVisibility(View.VISIBLE);
                    }
                }
            }
        }
    }

    // UPDATED!
    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Video.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            // HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
            // THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else
            return null;
    }


    @Override
    public void onProgressUpdate(long s, long l) {

        setStatus(s, l);
    }

    @Override
    public void onError() {

    }

    @Override
    public void onFinish() {

    }

    private void setStatus(long s, long l) {

        if (binding.progressBar.getMax() != l) {
            binding.progressBar.setMax((int) l);
        }
        ProgressAnimation animation = new ProgressAnimation(binding.progressBar, binding.progressBar.getProgress(), s);
        animation.setDuration(500);
        binding.progressBar.startAnimation(animation);
        binding.textPercent.setText(String.valueOf(100 * s / l));
    }

    private void showAvlIndicator() {
        binding.progress.setVisibility(View.VISIBLE);
        binding.avl.show();
    }

    private void hideAvlIndicator() {
        binding.progress.setVisibility(View.GONE);
        binding.avl.hide();
    }
}
