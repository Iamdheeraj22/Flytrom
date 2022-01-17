package com.flytrom.learning.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.pdf.PdfRenderer;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.arvind.android.permissions.PermissionHandler;
import com.arvind.android.permissions.Permissions;
import com.flytrom.learning.BR;
import com.flytrom.learning.R;
import com.flytrom.learning.activities.video_menu.PlayVideoActivity;
import com.flytrom.learning.activities.video_menu.SlidesActivity;
import com.flytrom.learning.adapters.NotesAdapter;
import com.flytrom.learning.base.BaseActivity;
import com.flytrom.learning.base.BaseCallback;
import com.flytrom.learning.base.BaseCustomDialog;
import com.flytrom.learning.base.SimpleRecyclerViewAdapter;
import com.flytrom.learning.beans.normal_beans.ApiErrorBean;
import com.flytrom.learning.beans.normal_beans.NoteBean;
import com.flytrom.learning.beans.response_beans.others.SuccessBean;
import com.flytrom.learning.beans.response_beans.others.TopicsBean;
import com.flytrom.learning.beans.response_beans.videos.PostImageBean;
import com.flytrom.learning.beans.response_beans.videos.VideoBean;
import com.flytrom.learning.custom_views.player.OptionSelector;
import com.flytrom.learning.custom_views.player.VdoPlayerControlView;
import com.flytrom.learning.databinding.ActivityLiveVideoBinding;
import com.flytrom.learning.databinding.ActivityPlayVideoBinding;
import com.flytrom.learning.databinding.DialogConfirmBinding;
import com.flytrom.learning.databinding.DialogRatingBinding;
import com.flytrom.learning.databinding.DialogReportErrorBinding;
import com.flytrom.learning.databinding.ItemVideoTopicBinding;
import com.flytrom.learning.interfaces.Apis;
import com.flytrom.learning.pdf_viewer.PDFView;
import com.flytrom.learning.pdf_viewer.PDFViewActivity;
import com.flytrom.learning.pdf_viewer.PDFViewActivityWithViewPager;
import com.flytrom.learning.utils.AppController;
import com.flytrom.learning.utils.AsyncLoadPdf;
import com.flytrom.learning.utils.Constants;
import com.flytrom.learning.utils.ConstantsNew;
import com.flytrom.learning.utils.MySharedPreferences;
import com.flytrom.learning.utils.PrefUtils;
import com.flytrom.learning.utils.ResponseHandler;
import com.flytrom.learning.utils.UtilMethods;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;
import com.vdocipher.aegis.media.ErrorDescription;
import com.vdocipher.aegis.media.Track;
import com.vdocipher.aegis.offline.DownloadOptions;
import com.vdocipher.aegis.offline.DownloadRequest;
import com.vdocipher.aegis.offline.DownloadSelections;
import com.vdocipher.aegis.offline.DownloadStatus;
import com.vdocipher.aegis.offline.OptionsDownloader;
import com.vdocipher.aegis.offline.VdoDownloadManager;
import com.vdocipher.aegis.player.VdoPlayer;
import com.vdocipher.aegis.player.VdoPlayerSupportFragment;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

import static com.flytrom.learning.custom_views.player.Utilities.getSizeString;

public class LiveVideoActivity extends BaseActivity<ActivityLiveVideoBinding>
        implements VdoPlayer.InitializationListener {




    private static final String TAG = "PlayVideoActivity";
    public static final String EXTRA_VDO_PARAMS = "vdo_params";

    private VdoPlayerSupportFragment mPlayerFragment;
    private VdoPlayer.VdoInitParams mVdoParams;
    private VideoBean mVideoBean;
    private VdoPlayer mVdoPlayer;
    private int currentOrientation, mCurrentVideoDuration = 0;

    public static final int RANDOM_TEXT_DURATION = 5000;
    private ParcelFileDescriptor mFileDescriptor;
    private PdfRenderer mPdfRenderer;
    private PdfRenderer.Page mCurrentPage;
    //private PDFConfig config;
    private Random mRandom;
    private Handler mHandler;

    @Override
    protected void onViewReady(Bundle savedInstanceState, Intent intent) {

        super.onViewReady(savedInstanceState, intent);
        initView(savedInstanceState);

    }

    protected void setBaseCallback(BaseCallback baseCallback) {
        binding.setVariable(com.flytrom.learning.BR.callback, baseCallback);
    }




    @Override
    public void onPermissionsGranted(int requestCode) {

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_live_video;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            showFullScreen(false);
            binding.playerControlViewLive.setFullscreenState(false);
        } else {
            if (AppController.getInstance().isInternetOn())
//                updateVideoStatusToServer(Constants.CONTENT_STATUS[1]);
//            Bundle bundle = new Bundle();
//            bundle.putSerializable("videoBean", mVideoBean);
//            Intent intent = new Intent();
//            intent.putExtras(bundle);
//            setResult(RESULT_OK, intent);
            finishActivityWithBackAnim();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onStart() {
        Timber.tag(TAG).v("onStart called");
        super.onStart();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            maybeCreateManager();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private void maybeCreateManager() {
    }

    @Override
    protected void onSaveInstanceState(@NotNull Bundle outState) {
        Timber.tag(TAG).v("onSaveInstanceState called");
        super.onSaveInstanceState(outState);
        if (mVdoParams != null) {
            outState.putParcelable("initParams", mVdoParams);
        }
    }

    @Override
    public void onInitializationSuccess(VdoPlayer.PlayerHost playerHost, VdoPlayer player, boolean wasRestored) {
        this.mVdoPlayer = player;
        Timber.i("onInitializationSuccess");
        player.addPlaybackEventListener(playbackListener);
        binding.playerControlViewLive.setPlayer(player);
        showControls(true);

        binding.playerControlViewLive.setFullscreenActionListener(fullscreenToggleListener);
        binding.playerControlViewLive.setControllerVisibilityListener(visibilityListener);

        // load a media to the player
        player.load(mVdoParams);

    }

    @Override
    public void onInitializationFailure(VdoPlayer.PlayerHost playerHost, ErrorDescription errorDescription) {
        String msg = "onInitializationFailure: errorCode = " + errorDescription.errorCode + ": " + errorDescription.errorMsg;
        Timber.e(msg);
        Toast.makeText(LiveVideoActivity.this, "initialization failure: " + errorDescription.errorMsg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        final int newOrientation = newConfig.orientation;
        final int oldOrientation = currentOrientation;
        currentOrientation = newOrientation;
        Timber.i("new orientation %s", (newOrientation == Configuration.ORIENTATION_PORTRAIT ? "PORTRAIT" :
                newOrientation == Configuration.ORIENTATION_LANDSCAPE ? "LANDSCAPE" : "UNKNOWN"));
        super.onConfigurationChanged(newConfig);
        if (newOrientation == oldOrientation) {
            Timber.i("orientation unchanged");
        } else if (newOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            // hide other views
            binding.relativeToolbar.setVisibility(View.GONE);
            findViewById(mPlayerFragment.getId()).setLayoutParams(new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
            binding.playerControlViewLive.setFitsSystemWindows(true);
            // hide system windows
            showSystemUi(false);
            showControls(false);
        } else {
            // show other views
            binding.relativeToolbar.setVisibility(View.VISIBLE);
            findViewById(mPlayerFragment.getId()).setLayoutParams(new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
            binding.playerControlViewLive.setFitsSystemWindows(false);
            binding.playerControlViewLive.setPadding(0, 0, 0, 0);
            showSystemUi(true);
        }
    }

    private void initView(Bundle savedInstanceState) {
        setBaseCallback(baseCallback);
        getData();

        getWindow().getDecorView().setOnSystemUiVisibilityChangeListener(uiVisibilityListener);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        if (savedInstanceState != null) {
            mVdoParams = savedInstanceState.getParcelable("initParams");
        }

        if (mVdoParams == null) {
            mVdoParams = getIntent().getParcelableExtra(EXTRA_VDO_PARAMS);
        }

        mPlayerFragment = (VdoPlayerSupportFragment) getSupportFragmentManager().findFragmentById(R.id.vdo_player_fragment);
        showControls(false);

        currentOrientation = getResources().getConfiguration().orientation;
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);

        setData();
        initializePlayer();
        //getPosterImage();

    }

    private void setData() {
        if (mVideoBean != null) {
            if (mVideoBean.getDescription() != null && !mVideoBean.getDescription().equals("")){

            }else {

            }
            if (mVideoBean.getTopics() != null) {
                if (mVideoBean.getTopics().size() > 0) {
                } else {
                }
            }

            mDBRepository.getParticularSavedVideoById(mVideoBean.getId()).observe(this, list -> {
                if (list.size() > 0) {
                    DownloadStatus downloadStatus = new Gson().fromJson(mVideoBean.getDownloadStatus(), DownloadStatus.class);
                    if (downloadStatus != null) startPlayback(downloadStatus);
                }
            });

            if (mVideoBean.getStatus().equals(Constants.CONTENT_STATUS[2])) {
            }

            if (mVideoBean.getVideoBanner() != null && !mVideoBean.getVideoBanner().equals("")) {
                Picasso.with(LiveVideoActivity.this)
                        .load(Constants.MEDIA_URL + mVideoBean.getVideoBanner())
                        .into(binding.imagePosterLive);
            } else {
                binding.rlPosterLive.setVisibility(View.GONE);
            }
        }
    }

    private void getData() {
        if (getIntent().getExtras() != null) {
            mVideoBean = (VideoBean) getIntent().getSerializableExtra("videoBean");
            if (mVideoBean != null) {
                binding.toolbar.textTitle.setText(mVideoBean.getCategory());
                binding.toolbar.textTitle.setTextColor(Color.parseColor("#484848"));
            }
        }
    }

    private void removeDownloadedVideoFromDB() {
        mDBRepositoryKotlin.deleteDownloadVideo(mVideoBean.getId());
    }

    private void goToSlidesScreen() {
        Bundle bundle = new Bundle();
        bundle.putSerializable("videoBean", mVideoBean);
        startActivity(new Intent(this, SlidesActivity.class).putExtras(bundle));
        goNextAnimation();
    }




    private void getOptions() {
        HandlerThread handlerThread = new HandlerThread(TAG);
        handlerThread.start();
        new Handler(handlerThread.getLooper()).post(
                () -> new OptionsDownloader().downloadOptionsWithOtp(
                        mVideoBean.getOtp(), mVideoBean.getPlaybackInfo(), new OptionsDownloader.Callback() {
                            @Override
                            public void onOptionsReceived(DownloadOptions options) {
                                Timber.i("onOptionsReceived");
                                showSelectionDialog(options, options.mediaInfo.duration);
                            }

                            @Override
                            public void onOptionsNotReceived(ErrorDescription errDesc) {
                                String errMsg = "onOptionsNotReceived : " + errDesc.toString();
                                Timber.e(errMsg);
                                Toast.makeText(LiveVideoActivity.this, errMsg, Toast.LENGTH_LONG).show();
                            }
                        }));
    }

    public void showSelectionDialog(DownloadOptions downloadOptions, long durationMs) {
        new OptionSelector(downloadOptions, durationMs, optionsSelectedCallback,
                OptionSelector.OptionStyle.SHOW_HIGHEST_AND_LOWEST_QUALITY)
                .showSelectionDialog(this, "Download options");
    }

    private void downloadSelectedOptions(DownloadOptions downloadOptions, int[] selectionIndices) {
        DownloadSelections selections = new DownloadSelections(downloadOptions, selectionIndices);

        // ensure external storage is in read-write mode
        if (!isExternalStorageWritable()) {
            showToastAndLog("External storage is not available", Toast.LENGTH_LONG);
            return;
        }

        String downloadLocation;
        try {
            downloadLocation = Objects.requireNonNull(getExternalFilesDir(null)).getPath() + File.separator + "offlineVdos";
        } catch (NullPointerException npe) {
            Timber.e("external storage not available: %s", Log.getStackTraceString(npe));
            Toast.makeText(this, "external storage not available", Toast.LENGTH_LONG).show();
            return;
        }

        // ensure download directory is created
        File dlLocation = new File(downloadLocation);
        if (!(dlLocation.exists() && dlLocation.isDirectory())) {
            // directory not created yet; let's create it
            if (!dlLocation.mkdir()) {
                Timber.e("failed to create storage directory");
                Toast.makeText(this, "failed to create storage directory", Toast.LENGTH_LONG).show();
                return;
            }
        }

        Timber.i("will save media to %s", downloadLocation);


        // build a DownloadRequest
        DownloadRequest request = new DownloadRequest.Builder(selections, downloadLocation).build();

        // enqueue request to VdoDownloadManager for download
        try {
        } catch (IllegalArgumentException | IllegalStateException e) {
            Timber.e("error enqueuing download request");
            Toast.makeText(this, "error enqueuing download request", Toast.LENGTH_LONG).show();
        }
    }

    private boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    private void startPlayback(DownloadStatus downloadStatus) {
        if (downloadStatus.status != VdoDownloadManager.STATUS_COMPLETED) {
            showToastAndLog("Download not complete", Toast.LENGTH_SHORT);
            return;
        }

        this.mVdoParams = VdoPlayer.VdoInitParams.createParamsForOffline(downloadStatus.mediaInfo.mediaId);
        Timber.d("dfd");
        //initializePlayer();
    }

    private void showToastAndLog(final String message, final int toastLength) {
        runOnUiThread(() -> Toast.makeText(getApplicationContext(), message, toastLength).show());
        Timber.i(message);
    }

    private static String getDownloadItemName(Track track, long durationMs) {
        String type = track.type == Track.TYPE_VIDEO ? "V" : track.type == Track.TYPE_AUDIO ? "A" : "?";
        return type + " " + (track.bitrate / 1024) + " kbps, " +
                getSizeString(track.bitrate, durationMs);
    }

    private void openPdfActivity() {
        if (mVideoBean != null) {
            if (mVideoBean.getNotes() != null && !mVideoBean.getNotes().equals("")) {
                PDFView.with(this)
                        .videoBean(mVideoBean)
                        .swipeHorizontal(false)
                        .start();
            } else {
                showToast("Not available");
            }
        }
    }

    /*private void OpenPdfActivity(String absolutePath) {
        PDFView.with(this)
                .fromFilePath(absolutePath)
                .videoBean(mVideoBean)
                .swipeHorizontal(false)
                .start();
    }*/

    private void initializePlayer() {
        if (mVdoParams != null) {
            // initialize the playerFragment; a VdoPlayer instance will be received
            // in onInitializationSuccess() callback
            mPlayerFragment.initialize(LiveVideoActivity.this);
        } else {
            // lets get otp and playbackInfo before creating the player
            obtainOtpAndPlaybackInfo();
        }
    }

    /**
     * Fetch (otp + playbackInfo) and initialize VdoPlayer
     * here we're fetching a sample (otp + playbackInfo)
     * TODO you need to generate/fetch (otp + playbackInfo) OR (signature + playbackInfo) for the
     * video you wish to play
     */
    private void obtainOtpAndPlaybackInfo() {
        new Thread(() -> {
            try {
                mVdoParams = new VdoPlayer.VdoInitParams.Builder()
                        .setOtp(mVideoBean.getOtp())
                        .setPlaybackInfo(mVideoBean.getPlaybackInfo())
                        .setPreferredCaptionsLanguage("en")
                        .build();

                runOnUiThread(this::initializePlayer);
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> {
                    //showToast("Error fetching otp and playbackInfo: " + e.getClass().getSimpleName());
                });
            }
        }).start();
    }

    public void showToast(final String message) {
        runOnUiThread(() -> Toast.makeText(LiveVideoActivity.this, message, Toast.LENGTH_SHORT).show());
    }

    private void showControls(boolean show) {
        Timber.tag(TAG).v("%s controls", (show ? "show" : "hide"));
        if (show) {
            binding.playerControlViewLive.show();
        } else {
            binding.playerControlViewLive.hide();
        }
    }

    public boolean isPlaying() {
        return mVdoPlayer.getPlaybackState() == VdoPlayer.STATE_READY && mVdoPlayer.getPlayWhenReady();
    }

    private void showFullScreen(boolean show) {
        Timber.tag(TAG).v("%s fullscreen", (show ? "enter" : "exit"));
        if (show) {
            // go to landscape orientation for fullscreen mode
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        } else {
            // go to portrait orientation
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        }
    }

    private void showSystemUi(boolean show) {
        Timber.tag(TAG).v("%s system ui", (show ? "show" : "hide"));
        if (!show) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN);
        } else {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }
    }

    private void showRateDialog() {

    }

    private void updateVideoStatusInBean(String type) {
        mVideoBean.setSeconds(mCurrentVideoDuration);

        mDBRepositoryKotlin.updateSecondAndStatusOfVideo(mVideoBean);
    }

//    private void updateVideoStatusToServer(String status) {
//        if (mVideoBean != null && !mVideoBean.getStatus().equals(Constants.CONTENT_STATUS[2])) {
//            HashMap<String, String> map = new HashMap<>();
//            map.put("video_id", String.valueOf(mVideoBean.getId()));
//            map.put("status", status);
//            map.put("seconds", String.valueOf(mCurrentVideoDuration));
//            mCallUpdateVideoStatus = AppController.getInstance().getApis().updateVideoStatus(getHeader(), map);
//            mCallUpdateVideoStatus.enqueue(new ResponseHandler<SuccessBean>() {
//                @Override
//                public void onSuccess(SuccessBean successBean) {
//                    Timber.i("Success");
//                }
//
//                @Override
//                public void apiError(ApiErrorBean t) {
//                    hideBaseProgress();
//                    //if (t != null) showErrorToast(t.getMessage());
//                }
//
//                @Override
//                public void onComplete(Call<SuccessBean> call, Throwable t) {
//                    //onCallComplete(call, t);
//                }
//
//                @Override
//                public void statusCode(int t) {
//                    super.statusCode(t);
//                    handleCodes(t);
//                }
//            });
//        }
//    }

    private BaseCallback baseCallback = view -> {
        switch (view.getId()) {
            case R.id.image_back:
                goBack();
                break;
            case R.id.image_play:
                binding.rlPosterLive.setVisibility(View.GONE);
                mVdoPlayer.setPlayWhenReady(true);
                break;
        }
    };

    private VdoPlayer.PlaybackEventListener playbackListener = new VdoPlayer.PlaybackEventListener() {
        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            if (playbackState == VdoPlayer.STATE_READY) {

                if (!isPlaying()) {
                    //updateVideoStatusInBean(Constants.CONTENT_STATUS[1]);
//                    updateVideoStatusToServer(Constants.CONTENT_STATUS[1]);
                }
            }
        }

        @Override
        public void onTracksChanged(Track[] tracks, Track[] tracks1) {
            Timber.i("onTracksChanged");
        }

        @Override
        public void onBufferUpdate(long bufferTime) {
            Timber.d("Dfds");
        }

        @Override
        public void onSeekTo(long millis) {
            Timber.i("onSeekTo: %s", millis);
        }

        @Override
        public void onProgress(long millis) {
            mCurrentVideoDuration = (int) millis / 1000;
            Timber.d("Progress %s", mCurrentVideoDuration);
        }

        @Override
        public void onPlaybackSpeedChanged(float speed) {
            Timber.i("onPlaybackSpeedChanged %s", speed);
        }

        @Override
        public void onLoading(VdoPlayer.VdoInitParams vdoInitParams) {
            Timber.i("onLoading");
        }

        @Override
        public void onLoadError(VdoPlayer.VdoInitParams vdoInitParams, ErrorDescription errorDescription) {
            String err = "onLoadError code: " + errorDescription.errorCode;
            Timber.e(err);
        }

        @Override
        public void onLoaded(VdoPlayer.VdoInitParams vdoInitParams) {
            Timber.i("onLoaded");
            if (mVideoBean != null) {
                mVdoPlayer.seekTo(mVideoBean.getSeconds() * 1000);
                mVdoPlayer.setPlayWhenReady(false);
            }
        }

        @Override
        public void onError(VdoPlayer.VdoInitParams vdoParams, ErrorDescription errorDescription) {
            String err = "onError code " + errorDescription.errorCode + ": " + errorDescription.errorMsg;
            Timber.e(err);
        }

        @Override
        public void onMediaEnded(VdoPlayer.VdoInitParams vdoInitParams) {
            Timber.i("onMediaEnded");
            updateVideoStatusInBean(Constants.CONTENT_STATUS[2]);
//            updateVideoStatusToServer(Constants.CONTENT_STATUS[2]);
            showRateDialog();
        }
    };

    private VdoPlayerControlView.FullscreenActionListener fullscreenToggleListener = enterFullscreen -> {
        showFullScreen(enterFullscreen);
        return true;
    };

    private VdoPlayerControlView.ControllerVisibilityListener visibilityListener = new VdoPlayerControlView.ControllerVisibilityListener() {
        @Override
        public void onControllerVisibilityChange(int visibility) {
            Timber.i("controller visibility %s", visibility);
            if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
                if (visibility != View.VISIBLE) {
                    showSystemUi(false);
                }
            }
        }
    };

    private View.OnSystemUiVisibilityChangeListener uiVisibilityListener = visibility -> {
        Timber.tag(TAG).v("onSystemUiVisibilityChange");
        // show player controls when system ui is showing
        if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
            Timber.tag(TAG).v("system ui visible, making controls visible");
            showControls(true);
        }
    };

    private OptionSelector.OptionsSelectedCallback optionsSelectedCallback = (downloadOptions, selectedTracks) -> {
        Timber.i(selectedTracks.length + " options selected: " + Arrays.toString(selectedTracks));
        long durationMs = downloadOptions.mediaInfo.duration;
        Timber.i("---- selected tracks ----");
        for (int trackIndex : selectedTracks) {
            Timber.i(getDownloadItemName(downloadOptions.availableTracks[trackIndex], durationMs));
        }
        Timber.i("---- selected tracks ----");

        // currently only (1 video + 1 audio) track supported
        if (selectedTracks.length != 2) {
            showToastAndLog("Invalid selection", Toast.LENGTH_LONG);
            return;
        }

        downloadSelectedOptions(downloadOptions, selectedTracks);

    };


    private Apis getApiInstance() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.readTimeout(30, TimeUnit.SECONDS);
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl("https://dev.vdocipher.com/api/")
                .client(httpClient.build())
                .build();

        return retrofit.create(Apis.class);
    }





}