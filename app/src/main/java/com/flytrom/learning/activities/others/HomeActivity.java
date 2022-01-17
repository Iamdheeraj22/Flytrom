package com.flytrom.learning.activities.others;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.flytrom.learning.BR;
import com.flytrom.learning.R;
import com.flytrom.learning.activities.FaqActivity;
import com.flytrom.learning.activities.TermsActivity;
import com.flytrom.learning.activities.info.AboutUsActivity;
import com.flytrom.learning.activities.info.AppSettingsActivity;
import com.flytrom.learning.activities.info.ContactUsActivity;
import com.flytrom.learning.activities.info.ProfileActivity;
import com.flytrom.learning.activities.info.ReportPiracyActivity;
import com.flytrom.learning.activities.info.TermsConditionsActivity;
import com.flytrom.learning.activities.info.TrainingAndAppsActivity;
import com.flytrom.learning.activities.payment.SubscribeActivity;
import com.flytrom.learning.base.BaseActivity;
import com.flytrom.learning.base.BaseCallback;
import com.flytrom.learning.base.SimpleRecyclerViewAdapter;
import com.flytrom.learning.beans.normal_beans.SideMenuBean;
import com.flytrom.learning.beans.response_beans.auth.UserDataBean;
import com.flytrom.learning.databinding.ActivityHomeBinding;
import com.flytrom.learning.databinding.ItemSideMenuBinding;
import com.flytrom.learning.fragments.HomeFragment;
import com.flytrom.learning.fragments.QBankCategoryFragment;
import com.flytrom.learning.fragments.QBankFragment;
import com.flytrom.learning.fragments.TestCatogeryFragment;
import com.flytrom.learning.fragments.TestsFragment;
import com.flytrom.learning.fragments.Video_Category_Fragment;
import com.flytrom.learning.fragments.video.LecturesFragment;
import com.flytrom.learning.interfaces.Apis;
import com.flytrom.learning.model.UrlModle;
import com.flytrom.learning.model.regionLodel;
import com.flytrom.learning.utils.AppController;
import com.flytrom.learning.utils.BackStackManager;
import com.flytrom.learning.utils.CircleImageTransform;
import com.flytrom.learning.utils.Constants;
import com.flytrom.learning.utils.PrefUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Url;

public class HomeActivity extends BaseActivity<ActivityHomeBinding> {

    public static final String TAB_HOME = "tab_home";
    public static final String TAB_VIDEOS = "tab_videos";
    public static final String TAB_Q_BANK = "tab_q_bank";
    public static final String TAB_TESTS = "tab_tests";
    private int mSelectedMenuTag = 0;
    boolean check = false;
    boolean check2 = false;
    String whatsappUrl,instagramUrl,facebookUrl,youtubeUrl;
    Apis apisInterface;

    @Override
    protected void onViewReady(Bundle savedInstanceState, Intent intent) {
        super.onViewReady(savedInstanceState, intent);
        apisInterface = AppController.getInstance().getApis();
        callUrlApi();
        initView();
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_home;
    }

    @Override
    public void onPermissionsGranted(int requestCode) {

    }

    @Override
    protected void onResume() {
        super.onResume();

        mSelectedMenuTag = -1;
    }

    @Override
    public void onBackPressed() {

        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        } else {




            if (getCurrentFragment().equals("HomeFragment")) {

                new AlertDialog.Builder(this)
                        .setTitle("Exit!")
                        .setMessage("Are you sure you want to exit from app?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                finishActivityWithBackAnim();
                            }
                        })
                        .setNegativeButton(android.R.string.cancel, null)
                        .setIcon(R.drawable.logo_hd)
                        .show();


            }else{

                binding.appBarHome.toolbar.imageBookmark.setVisibility(View.GONE);
                binding.appBarHome.toolbar.textFlytrom.setVisibility(View.VISIBLE);
                binding.appBarHome.toolbar.textTitleCenter.setVisibility(View.GONE);
                replaceFragment(TAB_HOME, new HomeFragment(), true);
                binding.appBarHome.bottomMenus.setSelectedItemId(R.id.menu_home);

            }




        }

    }

    private void initView() {
        binding.appBarHome.bottomMenus.setOnNavigationItemSelectedListener(itemSelectedListener);
        binding.appBarHome.bottomMenus.setSelectedItemId(R.id.menu_home);
        setSideMenus();
        setBaseCallback(baseCallback);
    }

    public void replaceFragment(String tag, Fragment fragment, boolean animation) {
        BackStackManager.getInstance(this).pushFragments(R.id.frame_layout, tag, fragment, animation);
    }

    private BaseCallback baseCallback = view -> {
        switch (view.getId()) {
            case R.id.image_side_menu:
                binding.drawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.image_search:
                break;
            case R.id.text_my_profile:
                startActivity(new Intent(HomeActivity.this, ProfileActivity.class));
                goNextAnimation();
                break;
            case R.id.buy_plan:
                startActivity(new Intent(HomeActivity.this, SubscribeActivity.class));
                goNextAnimation();
                break;
          case R.id.my_profile:
                startActivity(new Intent(HomeActivity.this, ProfileActivity.class));
                goNextAnimation();
                break;
            case R.id.my_sub:
             goToSubscribeScreen();

                break;
         case R.id.rp:
             startActivity(new Intent(HomeActivity.this, ReportPiracyActivity.class));
             goNextAnimation();
                break;
         case R.id.otherApp:
             if (check){
                 binding.sideMenu.showService.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_baseline_add));
             check=false;
             }else {
                 binding.sideMenu.showService.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_minus));
                 check=true;
             }
             binding.sideMenu.otherService.setVisibility(binding.sideMenu.otherService.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
              break;
         case R.id.centerOf:
             startActivity(new Intent(HomeActivity.this, TrainingAndAppsActivity.class)
                     .putExtra("from", Constants.SIDE_MENU_TITLES[3]));
             goNextAnimation();
                break;
         case R.id.crewApp:
             startActivity(new Intent(HomeActivity.this, TrainingAndAppsActivity.class)
                     .putExtra("from", Constants.SIDE_MENU_TITLES[3]));
             goNextAnimation();
                break;
         case R.id.setting_Card:
             if (check2){
                 binding.sideMenu.showSetting.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_baseline_add));
                 check2=false;
             }else {
                 binding.sideMenu.showSetting.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_minus));
                 check2=true;

             }
               binding.sideMenu.llsetting.setVisibility(binding.sideMenu.llsetting.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                break;
        case R.id.appSettings:
            startActivity(new Intent(HomeActivity.this, AppSettingsActivity.class));
            goNextAnimation();
                break;
        case R.id.aboutUs:
            startActivity(new Intent(HomeActivity.this, TrainingAndAppsActivity.class)
                    .putExtra("from", getString(R.string.text_about_us)));
            goNextAnimation();
                break;
        case R.id.cus:
            startActivity(new Intent(HomeActivity.this, TrainingAndAppsActivity.class)
                    .putExtra("from", getString(R.string.text_contact_us)));
            goNextAnimation();
                break;
        case R.id.faq:
            startActivity(new Intent(HomeActivity.this, FaqActivity.class)
                    .putExtra("from", getString(R.string.title_faqs)));
//            startActivity(new Intent(HomeActivity.this, TrainingAndAppsActivity.class)
//                    .putExtra("from", getString(R.string.title_faqs)));
            goNextAnimation();
                break;
            case R.id.tsc:
                startActivity(new Intent(HomeActivity.this, TermsActivity.class));
//                startActivity(new Intent(HomeActivity.this, TrainingAndAppsActivity.class)
//                        .putExtra("from", getString(R.string.text_terms_conditions)));
                goNextAnimation();
                break;
            case R.id.facebook:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(facebookUrl)));

//                PackageManager packageManagerFB = this.getPackageManager();
//                Intent iFB = new Intent(Intent.ACTION_VIEW);
//
//                try {
//                    iFB.setPackage("com.facebook");
//                    iFB.setData(Uri.parse(facebookUrl));
//                    if (iFB.resolveActivity(packageManagerFB) != null) {
//                        this.startActivity(iFB);
//                    } else showToast("Facebook not installed");
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
                break;
            case R.id.insta:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(instagramUrl)));

//                PackageManager packageManagerINS = this.getPackageManager();
//                Intent iINS = new Intent(Intent.ACTION_VIEW);
//
//                try {
//                    iINS.setPackage("com.instagram");
//                    iINS.setData(Uri.parse(instagramUrl));
//                    if (iINS.resolveActivity(packageManagerINS) != null) {
//                        this.startActivity(iINS);
//                    } else showToast("Instagram not installed");
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
                break;
            case R.id.whatsapp:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(whatsappUrl)));
//                PackageManager packageManagerWTS = this.getPackageManager();
//                Intent iWTS = new Intent(Intent.ACTION_VIEW);
//
//                try {
//                    iWTS.setPackage("com.whatsapp");
//                    iWTS.setData(Uri.parse(whatsappUrl));
//                    if (iWTS.resolveActivity(packageManagerWTS) != null) {
//                        this.startActivity(iWTS);
//                    } else showToast("WhatsApp not installed");
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
                break;
            case R.id.youtube:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(youtubeUrl)));
//                PackageManager packageManagerYT = this.getPackageManager();
//                Intent iYT = new Intent(Intent.ACTION_VIEW);
//
//                try {
//                    iYT.setPackage("com.youtube");
//                    iYT.setData(Uri.parse(youtubeUrl));
//                    if (iYT.resolveActivity(packageManagerYT) != null) {
//                        this.startActivity(iYT);
//                    } else showToast("YouTube not installed");
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
                break;
        }
    };

    private BottomNavigationView.OnNavigationItemSelectedListener itemSelectedListener = menuItem -> {
        switch (menuItem.getItemId()) {

            case R.id.menu_home:
                binding.appBarHome.toolbar.imageBookmark.setVisibility(View.GONE);
                binding.appBarHome.toolbar.textFlytrom.setVisibility(View.VISIBLE);
                binding.appBarHome.toolbar.textTitleCenter.setVisibility(View.GONE);
                replaceFragment(TAB_HOME, new HomeFragment(), true);
                break;
            case R.id.menu_lectures:
                binding.appBarHome.toolbar.imageBookmark.setVisibility(View.GONE);
                binding.appBarHome.toolbar.textFlytrom.setVisibility(View.GONE);
                binding.appBarHome.toolbar.textTitleCenter.setVisibility(View.VISIBLE);
                binding.appBarHome.toolbar.textTitleCenter.setText(getString(R.string.video_lectures));
                binding.appBarHome.toolbar.textTitleCenter.setTextColor(Color.parseColor("#484848"));
                replaceFragment(TAB_VIDEOS, new Video_Category_Fragment(), true);
                break;
            case R.id.menu_q_bank:
                binding.appBarHome.toolbar.imageBookmark.setVisibility(View.GONE);
                binding.appBarHome.toolbar.textFlytrom.setVisibility(View.GONE);
                binding.appBarHome.toolbar.textTitleCenter.setVisibility(View.VISIBLE);
                binding.appBarHome.toolbar.textTitleCenter.setText(getString(R.string.q_bank));
                binding.appBarHome.toolbar.textTitleCenter.setTextColor(Color.parseColor("#484848"));
                replaceFragment(TAB_Q_BANK, new QBankCategoryFragment(), true);
                break;
            case R.id.menu_tests:
                binding.appBarHome.toolbar.imageBookmark.setVisibility(View.GONE);
                binding.appBarHome.toolbar.textFlytrom.setVisibility(View.GONE);
                binding.appBarHome.toolbar.textTitleCenter.setVisibility(View.VISIBLE);
                binding.appBarHome.toolbar.textTitleCenter.setText(getString(R.string.test_series));
                binding.appBarHome.toolbar.textTitleCenter.setTextColor(Color.parseColor("#484848"));
                replaceFragment(TAB_TESTS, new TestCatogeryFragment(), true);
                break;
        }
        return true;
    };

    private void setSideMenus() {
        SimpleRecyclerViewAdapter<SideMenuBean, ItemSideMenuBinding> mAdapterSideMenu = new SimpleRecyclerViewAdapter<>(R.layout.item_side_menu, BR.bean, (v, sideMenuBean) ->
                new Handler().postDelayed(() -> {
                    mSelectedMenuTag = sideMenuBean.getTag();
                    binding.drawerLayout.closeDrawer(GravityCompat.START);
                }, 500));

        List<SideMenuBean> list = new ArrayList<>();
        for (int i = 0; i < Constants.SIDE_MENU_TITLES.length; i++) {
            SideMenuBean sideMenuBean = new SideMenuBean(Constants.SIDE_MENU_TITLES[i], false, i, Constants.SIDE_MENU_ICONS[i]);
            list.add(sideMenuBean);
        }
        mAdapterSideMenu.setList(list);
//        binding.sideMenu.rvSideMenus.setLayoutManager(new LinearLayoutManager(this));
//        binding.sideMenu.rvSideMenus.setAdapter(mAdapterSideMenu);

        binding.drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

                float slideX = drawerView.getWidth() * slideOffset;
                binding.appBarHome.rlMain.setTranslationX(slideX);
            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
                setUserData();
            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {

                if (mSelectedMenuTag != -1) {
                    switch (mSelectedMenuTag) {
                        case 0:
                            startActivity(new Intent(HomeActivity.this, AppSettingsActivity.class));
                            goNextAnimation();
                            break;
                        case 1:
                            startActivity(new Intent(HomeActivity.this, SubscribeActivity.class));
                            goNextAnimation();
                            break;
                        case 2:
                            startActivity(new Intent(HomeActivity.this, TrainingAndAppsActivity.class)
                                    .putExtra("from", Constants.SIDE_MENU_TITLES[2]));
                            goNextAnimation();
                            break;
                        case 3:
                            startActivity(new Intent(HomeActivity.this, TrainingAndAppsActivity.class)
                                    .putExtra("from", Constants.SIDE_MENU_TITLES[3]));
                            goNextAnimation();
                            break;
                        case 4:
                            startActivity(new Intent(HomeActivity.this, TrainingAndAppsActivity.class)
                                    .putExtra("from", getString(R.string.text_about_us)));
                            goNextAnimation();
                            /*startActivity(new Intent(HomeActivity.this, AboutUsActivity.class));
                            goNextAnimation();*/
                            break;
                        case 5:
                            startActivity(new Intent(HomeActivity.this, TrainingAndAppsActivity.class)
                                    .putExtra("from", getString(R.string.text_contact_us)));
                            goNextAnimation();
                            /*startActivity(new Intent(HomeActivity.this, ContactUsActivity.class));
                            goNextAnimation();*/
                            break;
                        case 6:
                            startActivity(new Intent(HomeActivity.this, ReportPiracyActivity.class));
                            goNextAnimation();
                            break;
                        case 7:/*
                            startActivity(new Intent(HomeActivity.this, TermsConditionsActivity.class).putExtra("from", "Faqs"));
                            goNextAnimation();*/
                            startActivity(new Intent(HomeActivity.this, TrainingAndAppsActivity.class)
                                    .putExtra("from", getString(R.string.title_faqs)));
                            goNextAnimation();
                            break;
                        case 8:
                            //storeAndOpenPdf();
                            /*startActivity(new Intent(HomeActivity.this, TermsConditionsActivity.class).putExtra("from", "Terms"));
                            goNextAnimation();*/
                            startActivity(new Intent(HomeActivity.this, TrainingAndAppsActivity.class)
                                    .putExtra("from", getString(R.string.text_terms_conditions)));
                            goNextAnimation();
                            break;
                    }
                }
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
    }

    private void setUserData() {
        UserDataBean userBean = PrefUtils.getInstance().getUser();
        if (userBean != null) {
//            if (userBean.getProfile_picture() != null)
//                Picasso.with(HomeActivity.this).load(Constants.MEDIA_URL + userBean.getProfile_picture())
//                        .resize(100, 100)
//                        .centerCrop()
//                        .transform(new CircleImageTransform())
//                        .placeholder(R.drawable.ic_user_placeholder)
//                        .into(binding.sideMenu.imageProfile);
//            binding.sideMenu.textName.setText(String.format("%s %s", userBean.getFirst_name(), userBean.getLast_name()));
//            int index = Integer.parseInt(userBean.getDesignation());
//            binding.sideMenu.textDesignation.setText(Constants.DesignationNames[index]);
        }
    }

    private void callUrlApi() {
        apisInterface.getUrls2(getHeader()).enqueue(new Callback<UrlModle>() {
            @Override
            public void onResponse(Call<UrlModle> call, Response<UrlModle> response) {
                if (response.isSuccessful()){
                    UrlModle urlModle = response.body();
                    for (int i = 0; i < urlModle.getData().size(); i++) {
                        if (urlModle.getData().get(i).getType().equalsIgnoreCase("WHATSAPP")){
                            Log.d("WHATSAPP",urlModle.getData().get(i).getUrl());
                            whatsappUrl = urlModle.getData().get(i).getUrl();
                        }else if (urlModle.getData().get(i).getType().equalsIgnoreCase("INSTAGRAM")){
                            Log.d("INSTAGRAM",urlModle.getData().get(i).getUrl());
                            instagramUrl = urlModle.getData().get(i).getUrl();
                        }else if (urlModle.getData().get(i).getType().equalsIgnoreCase("YOUTUBE")){
                            Log.d("YOUTUBE",urlModle.getData().get(i).getUrl());
                            youtubeUrl  = urlModle.getData().get(i).getUrl();
                        }else if (urlModle.getData().get(i).getType().equalsIgnoreCase("FACEBOOK")){
                            Log.d("FACEBOOK",urlModle.getData().get(i).getUrl());
                            facebookUrl = urlModle.getData().get(i).getUrl();

                        }

                    }
                }
            }

            @Override
            public void onFailure(Call<UrlModle> call, Throwable t) {

            }
        });
    }

    private void goToSubscribeScreen() {
        startActivity(new Intent(HomeActivity.this, SubscribeActivity.class)
                .putExtra("from", "my_subs"));
        goNextAnimation();
    }


    public String getCurrentFragment(){
        return getSupportFragmentManager().findFragmentById(R.id.frame_layout).getClass().getSimpleName();
    }



}


