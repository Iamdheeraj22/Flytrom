package com.flytrom.learning.pdf_viewer;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.pdf.PdfRenderer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.ParcelFileDescriptor;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.arvind.android.permissions.PermissionHandler;
import com.arvind.android.permissions.Permissions;
import com.flytrom.learning.R;
import com.flytrom.learning.activities.test_menu.SolveTestChapterActivity;
import com.flytrom.learning.activities.video_menu.PlayVideoActivity;
import com.flytrom.learning.adapters.NotesAdapter;
import com.flytrom.learning.base.BaseActivity;
import com.flytrom.learning.base.BaseCallback;
import com.flytrom.learning.base.SimpleRecyclerViewAdapter;
import com.flytrom.learning.beans.normal_beans.NoteBean;
import com.flytrom.learning.beans.response_beans.videos.VideoBean;
import com.flytrom.learning.databinding.ActivityPdfBinding;
import com.flytrom.learning.interfaces.OnTaskCompleted;
import com.flytrom.learning.utils.AsyncLoadPdf;
import com.flytrom.learning.utils.Constants;
import com.flytrom.learning.utils.ConstantsNew;
import com.flytrom.learning.utils.MySharedPreferences;
import com.flytrom.learning.utils.PrefUtils;
import com.flytrom.learning.utils.UtilMethods;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class PDFViewActivity extends BaseActivity<ActivityPdfBinding> implements OnTaskCompleted {

    public static final int RANDOM_TEXT_DURATION = 5000;
    private ParcelFileDescriptor mFileDescriptor;
    private PdfRenderer mPdfRenderer;
    private PdfRenderer.Page mCurrentPage;
    //private PDFConfig config;
    private Random mRandom;
    private Handler mHandler;
    private List<NoteBean> mNotesList;
    private VideoBean mVideoBean;
    public static PDFViewActivity mInstance;

    @Override
    protected void onViewReady(Bundle savedInstanceState, Intent intent) {
        super.onViewReady(savedInstanceState, intent);
        initView(intent);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_pdf;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mHandler.postDelayed(runnable, RANDOM_TEXT_DURATION);
    }

    @Override
    public void onPermissionsGranted(int requestCode) {

    }

    public static PDFViewActivity getInstance() {
        return mInstance;
    }

    public List<NoteBean> getNotesList() {
        return mNotesList;
    }

    private void initView(Intent intent) {
        mInstance = this;
        setBaseCallback(baseCallback);
        binding.toolbar.textTitle.setText(getString(R.string.text_notes));
        binding.toolbar.textTitle.setTextColor(Color.parseColor("#484848"));
        mRandom = new Random();
        mHandler = new Handler();
        binding.textEmail.setText(PrefUtils.getInstance().getUser().getEmail());
        binding.textName.setText(PrefUtils.getInstance().getUser().getFirst_name()+" "+PrefUtils.getInstance().getUser().getLast_name());
        binding.textPhone.setText(PrefUtils.getInstance().getUser().getPhone_number());
        /*if (intent == null) {
            finishActivityWithBackAnim();
            return;
        }*/

        //config = intent.getParcelableExtra(PDFConfig.EXTRA_CONFIG);

        // if (config != null) {

        getPdf();


        // }
    }

    private void getPdf() {

        if (getIntent().getExtras() != null) {
            mVideoBean = (VideoBean) getIntent().getSerializableExtra("videoBean");
            if (mVideoBean != null) {
                askPermissions(mVideoBean);
            }
        }
    }

    private void askPermissions(VideoBean mVideoBean) {
        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        Permissions.check(this, permissions, R.string.enable_media_permission, null, new PermissionHandler() {
            @Override
            public void onGranted() {
                if (UtilMethods.getSavedPdfs(PDFViewActivity.this).contains(getBaseDirectoryPath() + mVideoBean.getTitle() + mVideoBean.getId())) {
                    File file = new File(getBaseDirectoryPath() + mVideoBean.getTitle() + mVideoBean.getId());
                    if (file.exists()) {
                        prepareAllThings(file.getAbsolutePath());
                    }
                } else {
                    showBaseProgress();
                    AsyncLoadPdf runner = new AsyncLoadPdf(PDFViewActivity.this, PDFViewActivity.this, mVideoBean.getTitle() + mVideoBean.getId());
                    runner.execute(Constants.MEDIA_URL + mVideoBean.getNotes(), mVideoBean.getTitle() + mVideoBean.getId());
                }
            }
        });
    }

    private void prepareAllThings(String absolutePath) {
        //render the pdf view
        try {
            openRenderer(absolutePath);
            //setUpViewPager();
        } catch (FileNotFoundException e) {
            showToast("Error! " + e.getMessage());
            finishActivityWithBackAnim();
        } catch (IOException e) {
            e.printStackTrace();
            showToast("Error! " + e.getMessage());
            finishActivityWithBackAnim();
        }

        mNotesList = new ArrayList<>();
        for (int x = 0; x < mPdfRenderer.getPageCount(); x++) {
            mNotesList.add(new NoteBean(getImage(x)));
        }

        NotesAdapter mNotesAdapter = new NotesAdapter(this, new SimpleRecyclerViewAdapter.SimpleCallback() {
            @Override
            public void onClick(View v, Object o) {

            }

            @Override
            public void onClickWithPosition(View v, Object o, int pos) {
                goToDisplayEachPageScreen(pos);
            }
        });

        binding.rvNotesImages.setLayoutManager(new LinearLayoutManager(this));
        binding.rvNotesImages.setItemAnimator(new DefaultItemAnimator());
        binding.rvNotesImages.setAdapter(mNotesAdapter);
        mNotesAdapter.setDataList(mNotesList);
    }

    private void goToDisplayEachPageScreen(int pos) {
        Bundle bundle = new Bundle();
        bundle.putInt("position", pos);
        startActivity(new Intent(this, PDFViewActivityWithViewPager.class).putExtras(bundle));
        goNextAnimation();
    }

    private BaseCallback baseCallback = view -> {
//        if (view.getId() == R.id.image_back) {
//            goBack();
//        }
        switch (view.getId()) {
            case R.id.minimize:
                MySharedPreferences.getInstance().saveString(this, ConstantsNew.BACK,"1");
                goBack();
                break;

            case R.id.rotate:
                binding.rotate2.setVisibility(View.VISIBLE);
                binding.rotate.setVisibility(View.GONE);
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                break;

            case R.id.rotate2:
                binding.rotate2.setVisibility(View.GONE);
                binding.rotate.setVisibility(View.VISIBLE);
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                break;

        }

    };

    @Override
    protected void onDestroy() {
        try {
            closeRenderer();
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    private void openRenderer(String absolutePath) throws IOException {
        File file = new File(absolutePath);
        mFileDescriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY);
        if (mFileDescriptor != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mPdfRenderer = new PdfRenderer(mFileDescriptor);
            }
        }
    }

    private void closeRenderer() throws IOException {
        if (null != mCurrentPage)
            mCurrentPage.close();

        if (null != mPdfRenderer)
            mPdfRenderer.close();

        if (null != mFileDescriptor)
            mFileDescriptor.close();
    }

    private Bitmap getImage(int position) {

        if (null != mCurrentPage) {
            mCurrentPage.close();
        }

        mCurrentPage = mPdfRenderer.openPage(position);

        Bitmap bitmap = Bitmap.createBitmap(
                getResources().getDisplayMetrics().densityDpi * mCurrentPage.getWidth() / 150,
                getResources().getDisplayMetrics().densityDpi * mCurrentPage.getHeight() / 150,
                Bitmap.Config.ARGB_8888
        );

        mCurrentPage.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
        return bitmap;
    }

    private Runnable runnable = this::setRandomTextOnScreen;

    private void setRandomTextOnScreen() {
        float dx = mRandom.nextFloat() * binding.view.getWidth();
        float dy = mRandom.nextFloat() * binding.view.getHeight();

        final Handler ha = new Handler();
        ha.postDelayed(new Runnable() {

            @Override
            public void run() {
//                if (dx > 200 && dy > 200) {
//                    if (dx < binding.view.getWidth() && dy < binding.view.getHeight()) {
//
//                        // Log.d("RandomValues", "dx :" + dx + "    dy:" + dy);
//                        binding.rlAnimation.animate()
//                                .x(dx)
//                                .y(dy)
//                                .setDuration(0);
//                    }
//                } else setRandomTextOnScreen();

                Random r = new Random();
                int top = r.nextInt(1000 - 10) + 10;
                int right = r.nextInt(1000 - 10) + 50;


                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(right,top,0,0);
                binding.rlAnimation.setLayoutParams(params);
                binding.rlAnimation.setVisibility(binding.rlAnimation.getVisibility() == View.VISIBLE?View.GONE:View.VISIBLE);
                ha.postDelayed(this, 5000);
            }
        }, 5000);

    }

    @Override
    public void onTaskCompletedPdf(String name) {
        hideBaseProgress();
        File file = new File(getBaseDirectoryPath() + name);
        if (file.exists()) {
            prepareAllThings(file.getAbsolutePath());
        }
    }
}
