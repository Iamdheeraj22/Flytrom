package com.flytrom.learning.activities.others;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.arvind.android.permissions.PermissionHandler;
import com.arvind.android.permissions.Permissions;
import com.flytrom.learning.activities.auth.SignUpActivity;
import com.flytrom.learning.activities.changeNumEmail.ChangePhoneOrrEmailActivity;
import com.flytrom.learning.R;
import com.flytrom.learning.activities.auth.ChangePasswordActivity;
import com.flytrom.learning.activities.auth.ValidatePhoneActivity;
import com.flytrom.learning.activities.info.ProfileActivity;
import com.flytrom.learning.activities.otp.OtpActivity;
import com.flytrom.learning.base.BaseActivity;
import com.flytrom.learning.base.BaseCallback;
import com.flytrom.learning.base.BaseCustomDialog;
import com.flytrom.learning.beans.normal_beans.ApiErrorBean;
import com.flytrom.learning.beans.response_beans.auth.UserDataBean;
import com.flytrom.learning.databinding.ActivityEditProfileBinding;
import com.flytrom.learning.databinding.DialogLogoutBinding;
import com.flytrom.learning.interfaces.Apis;
import com.flytrom.learning.model.LoginModel.LoginModel;
import com.flytrom.learning.utils.AppController;
import com.flytrom.learning.utils.CircleImageTransform;
import com.flytrom.learning.utils.Constants;
import com.flytrom.learning.utils.ConstantsNew;
import com.flytrom.learning.utils.FieldsValidator;
import com.flytrom.learning.utils.MySharedPreferences;
import com.flytrom.learning.utils.PrefUtils;
import com.flytrom.learning.utils.ProgressRequestBody;
import com.flytrom.learning.utils.ResponseHandler;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import pl.aprilapps.easyphotopicker.ChooserType;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;
import pl.aprilapps.easyphotopicker.MediaFile;
import pl.aprilapps.easyphotopicker.MediaSource;
import retrofit2.Call;
import timber.log.Timber;

public class EditProfileActivity extends BaseActivity<ActivityEditProfileBinding> implements ProgressRequestBody.UploadCallbacks {


    private static final int CODE_PERMISSION = 101;
    private static final int CODE_REQUEST_CODE = 121;
    private Call<LoginModel> mCallUpdateProfilePic;
    private Call<LoginModel> mCallUpdateProfile;
    private FieldsValidator validator;
    ArrayList<String> returnValue = new ArrayList<>();
    private BaseCustomDialog<DialogLogoutBinding> mDialogLogout;
    private String mPhoneNumber;
    PopupMenu popupMenu,popupMenu2;
    Apis apisInterface;
    TextView region,spinner_designation;
    String destId;
    Calendar calendar;
    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
    String dob = "";
    int day, month, year;
    //easy image picker
    private EasyImage easyImage;
    File file;
    private ArrayList<MediaFile> photos = new ArrayList<>();


    @Override
    protected void onViewReady(Bundle savedInstanceState, Intent intent) {
        super.onViewReady(savedInstanceState, intent);
        calendar = Calendar.getInstance();
        spinner_designation = findViewById(R.id.spinner_designation2);
        popup();
        initView();
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_edit_profile;
    }

    @Override
    public void onPermissionsGranted(int requestCode) {
    }


    @Override
    protected void onDestroy() {
        if (mCallUpdateProfilePic != null) mCallUpdateProfilePic.cancel();
        if (mCallUpdateProfile != null) mCallUpdateProfile.cancel();
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri imageURi = result.getUri();
                binding.imageProfile.setImageURI(imageURi);
                String profilePictureStr = getRealPathFromURI(this,imageURi);
                file = new File(profilePictureStr);
                updatePicture();
            }
        }

        if (resultCode == CODE_REQUEST_CODE) {
            if (data != null) {
                mPhoneNumber = data.getStringExtra("number");
            }
            binding.textPhoneNumber.setText(String.format("(+91)%s", mPhoneNumber));
            if (binding.editTextFirstName.getText().length() > 0 && binding.editTextLastName.getText().length() > 0)
                updateProfile();
            else
                showErrorToast("Please fill First name and last name to change phone mPhoneNumber");
        }


    }

    private void initView() {

        validator = new FieldsValidator();
        binding.toolbar.textTitle.setText(getString(R.string.edit_profile));
        binding.toolbar.imageBack.setColorFilter(this.getResources().getColor(R.color.white));
        binding.toolbar.toolRL.setBackgroundColor(Color.parseColor("#4E7ADA"));
        binding.toolbar.textTitle.setTextColor(Color.parseColor("#ffffff"));
//        binding.toolbar.editText.setTextColor(Color.parseColor("#ffffff"));
        binding.toolbar.editText.setVisibility(View.GONE);
        binding.toolbar.llTool.setVisibility(View.GONE);

        setBaseCallback(baseCallback);
        /*options = Options.init()
                .setRequestCode(CODE_PERMISSION)
                .setCount(1)
                .setFrontfacing(false)
                .setImageQuality(ImageQuality.LOW)
                .setPreSelectedUrls(returnValue)
                .setScreenOrientation(Options.SCREEN_ORIENTATION_PORTRAIT);*/

        easyImage = new EasyImage.Builder(this)
                .setChooserTitle("Pick media")
                .setCopyImagesToPublicGalleryFolder(false)
                .setChooserType(ChooserType.CAMERA_AND_GALLERY)
                .setFolderName("EasyImage sample")
                .allowMultiple(false)
                .build();

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.array_designation, R.layout.item_simple_spinner);
        adapter.setDropDownViewResource(R.layout.item_simple_spinner_dropdown);
        binding.spinnerDesignation.setAdapter(adapter);
        setUserData();
    }

    private void setUserData() {
        UserDataBean userBean = PrefUtils.getInstance().getUser();
        if (userBean != null) {
            mPhoneNumber = userBean.getPhone_number();
            if (userBean.getProfile_picture() != null)
                Picasso.with(EditProfileActivity.this).load(Constants.MEDIA_URL + userBean.getProfile_picture())
                        .transform(new CircleImageTransform())
                        .placeholder(R.drawable.ic_user_placeholder)
                        .into(binding.imageProfile);
            binding.editTextFirstName.setText(userBean.getFirst_name());
            binding.editTextLastName.setText(userBean.getLast_name());

            String inputPattern = "yyyy-MM-dd";
            String outputPattern = "dd-MM-yyyy";
            SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
            SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

            Date date = null;
            String str = null;
/*
            try {
                date = inputFormat.parse(userBean.getDob());
                str = outputFormat.format(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Log.d("QWERTYUIO",str);
            binding.textDate.setText(str);*/
//            binding.editTextCpl.setText("1234");
            binding.editTextAtpl.setText(userBean.getAtpl_number());
            binding.textPhoneNumber.setText(String.format("(+91) %s", userBean.getPhone_number()));
//            binding.editTextFirstName.requestFocus(binding.editTextFirstName.getText().length());
            binding.spinnerDesignation2.setText(Constants.DesignationNames[Integer.parseInt(userBean.getDesignation())].toUpperCase());
        }
    }

    private BaseCallback baseCallback = view -> {
        switch (view.getId()) {
            case R.id.image_back:
                goBack();
                break;
            case R.id.change_pic:
                String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                Permissions.check(this, permissions, R.string.enable_media_permission, null, new PermissionHandler() {
                    @Override
                    public void onGranted() {
                        //Pix.start(EditProfileActivity.this, options);
//                        easyImage.openChooser(EditProfileActivity.this);
                        selectPicture();

                    }
                });
                break;
            case R.id.relative_change_pass:
                startActivity(new Intent(this, ChangePasswordActivity.class));
                goNextAnimation();
                break;
            case R.id.button_update:
                if (validateDetails()) updateProfile();
                break;
            case R.id.relative_change_number:
                startActivityForResult(new Intent(EditProfileActivity.this, ValidatePhoneActivity.class), CODE_REQUEST_CODE);
                goNextAnimation();
                break;
           case R.id.spinner_designation2:
               popupMenu2.show();
                break;
        case R.id.text_date:
            datePickClicked();
                break;
        case R.id.change:
            startActivity(new Intent(EditProfileActivity.this, ChangePhoneOrrEmailActivity.class));
            goNextAnimation();
            finish();
                break;
        }
    };

    private boolean validateDetails() {
        return validator.hasText(binding.editTextFirstName) &&
                validator.hasText(binding.editTextLastName);
    }

//    public void updatePicture(String selectedImage) {
//        File file = new File(selectedImage);
//        ProgressRequestBody fileBody = new ProgressRequestBody(file, this);
//        MultipartBody.Part part = MultipartBody.Part.createFormData("profile_picture", file.getName(), fileBody);
//        updateProfilePicture(part);
//    }
    public void updatePicture() {
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part part = MultipartBody.Part.createFormData("profile_picture", file.getName(), requestFile);
        updateProfilePicture(part);
    }

    private void updateProfilePicture(MultipartBody.Part part) {
        showBaseProgress();
        mCallUpdateProfilePic = AppController.getInstance().getApis().updateProfilePicture(getHeader(), part);
        mCallUpdateProfilePic.enqueue(new ResponseHandler<LoginModel>() {
            @Override
            public void onSuccess(LoginModel model) {
                if (model != null) {
                    if (model.getStatus() == Constants.SUCCESS_CODE) {
                        if (model.getData().getProfile_picture() != null) {
                            Picasso.with(EditProfileActivity.this).load(Constants.MEDIA_URL + model.getData().getProfile_picture())
                                    .resize(100, 100)
                                    .centerCrop()
                                    .transform(new CircleImageTransform())
                                    .placeholder(R.drawable.ic_user_placeholder)
                                    .into(binding.imageProfile);
                            getProfile();
                        }
                    }
                }
            }

            @Override
            public void apiError(ApiErrorBean t) {
                if (t != null) showErrorToast(t.getMessage());
            }

            @Override
            public void onComplete(Call<LoginModel> call_update_picture, Throwable t) {
                onCallComplete(call_update_picture, t);
            }

            @Override
            public void statusCode(int t) {
                super.statusCode(t);
                handleCodes(t);
            }
        });
    }

    @Override
    public void onProgressUpdate(long s, long l) {

    }

    @Override
    public void onError() {

    }

    @Override
    public void onFinish() {

    }

    private void updateProfile() {


          /*  String inputPattern = "dd-MM-yyyy";
            String outputPattern = "yyyy-MM-dd";
            SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
            SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

            Date date = null;
            String str = null;

            try {
                date = inputFormat.parse(binding.textDate.getText().toString());
                str = outputFormat.format(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Log.d("QWERTYUIO",str);*/

        showBaseProgress();
        Map<String, String> map = new HashMap<>();
        map.put("first_name", binding.editTextFirstName.getText().toString());
        map.put("last_name", binding.editTextLastName.getText().toString());
//        map.put("phone_number", mPhoneNumber);
//        map.put("cpl_number", binding.editTextCpl.getText().toString());
        map.put("atpl_number", binding.editTextAtpl.getText().toString());
      //  map.put("dob", str);
        map.put("designation", String.valueOf(destId));

        mCallUpdateProfile = AppController.getInstance().getApis().updateProfile(getHeader(), map);
        mCallUpdateProfile.enqueue(new ResponseHandler<LoginModel>() {
            @Override
            public void onSuccess(LoginModel model) {

                if (model != null) {
                    if (model.getStatus() == Constants.SUCCESS_CODE) {
                        if (PrefUtils.getInstance().setUser(model.getData())) {
                            showInfoToast(model.getMessage());
                                MySharedPreferences.getInstance().saveString(EditProfileActivity.this, ConstantsNew.EMAIL,model.getData().getEmail());
                                MySharedPreferences.getInstance().saveString(EditProfileActivity.this, ConstantsNew.NUMBER,model.getData().getPhone_number());
                            MySharedPreferences.getInstance().saveString(EditProfileActivity.this, ConstantsNew.USER_ID,model.getData().getId());
                            MySharedPreferences.getInstance().saveString(EditProfileActivity.this, ConstantsNew.FIRST_NAME, model.getData().getFirst_name()+" "+model.getData().getLast_name());
                            getProfile();
                        }
                    }
                }
            }

            @Override
            public void apiError(ApiErrorBean t) {
                if (t != null)
                    showErrorToast(t.getMessage());
            }

            @Override
            public void onComplete(Call<LoginModel> call_update_profile, Throwable t) {
                onCallComplete(call_update_profile, t);
            }

            @Override
            public void statusCode(int t) {
                super.statusCode(t);
                handleCodes(t);
            }
        });
    }

    private void getProfile() {
        showBaseProgress();
        mCallUpdateProfile = AppController.getInstance().getApis().getProfile(getHeader());
        mCallUpdateProfile.enqueue(new ResponseHandler<LoginModel>() {
            @Override
            public void onSuccess(LoginModel model) {

                if (model != null) {
                    if (model.getStatus() == Constants.SUCCESS_CODE) {
                        PrefUtils.getInstance().setUser(model.getData());
                        Intent intent = new Intent(EditProfileActivity.this, ProfileActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }

            @Override
            public void apiError(ApiErrorBean t) {
                if (t != null)
                    showErrorToast(t.getMessage());
            }

            @Override
            public void onComplete(Call<LoginModel> call_update_profile, Throwable t) {
                onCallComplete(call_update_profile, t);
            }

            @Override
            public void statusCode(int t) {
                super.statusCode(t);
                handleCodes(t);
            }
        });
    }
    private void popup() {
        popupMenu2 = new PopupMenu(EditProfileActivity.this, spinner_designation);
        popupMenu2.getMenuInflater().inflate(R.menu.designation_menu, popupMenu2.getMenu());
        popupMenu2.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                spinner_designation.setText(item.getTitle());
                if (item.getTitle().equals("Caption")){
                    destId = "0";
                }else if (item.getTitle().equals("First Office")){
                    destId = "1";
                }else if (item.getTitle().equals("Student pilot")){
                    destId = "2";
                }else if (item.getTitle().equals("Engineer")){
                    destId = "3";
                }else if (item.getTitle().equals("Cabin Crew")){
                    destId = "4";
                }else if (item.getTitle().equals("Other")){
                    destId = "5";
                }
                return true;
            }
        });
    }
    public final DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int year, int month, int day) {
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month);
                    calendar.set(Calendar.DAY_OF_MONTH, day);
                    showDate();
                }
            };

    public void showDate() {
        binding.textDate.setText(sdf.format(calendar.getTime()));
    }

    public void datePickClicked() {
        DatePickerDialog dp;
        if (!dob.equalsIgnoreCase("")) {
            dp = new DatePickerDialog(this, R.style.DialogTheme, myDateListener, year, month - 1, day);

        } else {
            dp = new DatePickerDialog(this, R.style.DialogTheme, myDateListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        }
        dp.getDatePicker().setMaxDate(System.currentTimeMillis());
//        dp.updateDate(2016, 5, 22);
        dp.show();
    }


    //Crope Image

    private void selectPicture(){
        CropImage.activity()
                .setAspectRatio(1,1)
                 .setGuidelines(CropImageView.Guidelines.ON)
                .start(EditProfileActivity.this);
    }

    public static String getRealPathFromURI(Context context, Uri contentURI) {
        String result;
        Cursor cursor = context.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }



}
