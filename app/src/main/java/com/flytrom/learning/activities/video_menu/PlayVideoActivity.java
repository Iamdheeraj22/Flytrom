package com.flytrom.learning.activities.video_menu;

import static com.flytrom.learning.custom_views.player.Utilities.getSizeString;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import com.flytrom.learning.BR;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.pdf.PdfRenderer;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.arvind.android.permissions.PermissionHandler;
import com.arvind.android.permissions.Permissions;
import com.flytrom.learning.BR;
import com.flytrom.learning.DownloadFileTask;
import com.flytrom.learning.R;
import com.flytrom.learning.adapters.GetCommentAdapter;
import com.flytrom.learning.adapters.NotesAdapter;
import com.flytrom.learning.adapters.SlideImageAdapter;
import com.flytrom.learning.base.BaseActivity;
import com.flytrom.learning.base.BaseCallback;
import com.flytrom.learning.base.BaseCustomDialog;
import com.flytrom.learning.base.SimpleRecyclerViewAdapter;
import com.flytrom.learning.beans.normal_beans.ApiErrorBean;
import com.flytrom.learning.beans.normal_beans.NoteBean;
import com.flytrom.learning.beans.response_beans.others.SlidesBean;
import com.flytrom.learning.beans.response_beans.others.SuccessBean;
import com.flytrom.learning.beans.response_beans.others.TopicsBean;
import com.flytrom.learning.beans.response_beans.videos.PostImageBean;
import com.flytrom.learning.beans.response_beans.videos.VideoBean;
import com.flytrom.learning.custom_views.player.OptionSelector;
import com.flytrom.learning.custom_views.player.VdoPlayerControlView;

import com.flytrom.learning.databinding.ActivityPlayVideoBinding;
import com.flytrom.learning.databinding.DialogConfirmBinding;
import com.flytrom.learning.databinding.DialogRatingBinding;
import com.flytrom.learning.databinding.DialogReportErrorBinding;
import com.flytrom.learning.databinding.ItemImageSlidesBinding;
import com.flytrom.learning.databinding.ItemVideoTopicBinding;
import com.flytrom.learning.interfaces.Apis;
import com.flytrom.learning.interfaces.OnTaskCompleted;
import com.flytrom.learning.model.CommentModel.GerCommentModel;
import com.flytrom.learning.model.CommentModel.GetCommentDatum;
import com.flytrom.learning.model.CommentModel.SendCommentModel;
import com.flytrom.learning.model.vimeo.VimeoResponse;
import com.flytrom.learning.pdf_viewer.PDFView;
import com.flytrom.learning.pdf_viewer.PDFViewActivity;
import com.flytrom.learning.pdf_viewer.PDFViewActivityWithViewPager;
import com.flytrom.learning.utils.AppController;
import com.flytrom.learning.utils.AsyncLoadPdf;
import com.flytrom.learning.utils.Constants;
import com.flytrom.learning.utils.ConstantsNew;
import com.flytrom.learning.utils.DownloadVideo;
import com.flytrom.learning.utils.MySharedPreferences;
import com.flytrom.learning.utils.OnDownloadListner;
import com.flytrom.learning.utils.PrefUtils;
import com.flytrom.learning.utils.ResponseHandler;
import com.flytrom.learning.utils.UtilMethods;
import com.flytrom.learning.vimeo_video.VimeoClientAPI;
import com.flytrom.learning.vimeo_video.VimeoInterface;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerControlView;
import com.google.android.exoplayer2.util.Util;
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
import com.vimeo.networking.VimeoClient;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HttpsURLConnection;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

public class PlayVideoActivity extends BaseActivity<ActivityPlayVideoBinding>
        implements VdoPlayer.InitializationListener, OnTaskCompleted, VdoDownloadManager.EventListener,
        GetCommentAdapter.OnClickComment, MediaPlayer.OnPreparedListener, View.OnClickListener, GetCommentAdapter.OnClickEdit {

    public static final int RANDOM_TEXT_DURATION = 5000;
    private ParcelFileDescriptor mFileDescriptor;
    private PdfRenderer mPdfRenderer;
    private PdfRenderer.Page mCurrentPage;
    //private PDFConfig config;
    private Random mRandom;
    Apis apiInterface;
    GetCommentAdapter getCommentAdapter;
    private Handler mHandler;
    private List<NoteBean> mNotesList;
    public static PDFViewActivity mInstance;
    private Call<SuccessBean> mCallResetTest;
    ArrayList<Integer> arrayList = new ArrayList<>();

    private static final String TAG = "PlayVideoActivity";
    public static final String EXTRA_VDO_PARAMS = "vdo_params";
    private Call<SuccessBean> mCallUpdateVideoStatus;
    private Call<SuccessBean> mCallRateVideo;
    private Call<SuccessBean> mCallMarkComplete;
    private Call<SuccessBean> mCallReportQuestion;
    private Call<PostImageBean> mCallPosterImage;

    private SimpleRecyclerViewAdapter<TopicsBean, ItemVideoTopicBinding> mVideoTopicsAdapter;
    private VdoPlayerSupportFragment mPlayerFragment;
    private VdoPlayer.VdoInitParams mVdoParams;
    private VideoBean mVideoBean;
    private VdoPlayer mVdoPlayer;
    private ArrayList<DownloadStatus> downloadStatusList;
    private int currentOrientation, mCurrentVideoDuration = 0;
    private volatile VdoDownloadManager vdoDownloadManager;

    private BaseCustomDialog<DialogReportErrorBinding> mReportErrorDialog;
    private BaseCustomDialog<DialogRatingBinding> mDialogRating;
    private BaseCustomDialog<DialogConfirmBinding> mConfirmDialog;

    String name = "";
    String pvDownloadLink = "";
    String qualityString = "";
    String email = "";
    String phone = "";
    String videoId = "";
    String videoVimeoLink = "";

    boolean full = false;
    String commentID = "0";
    String cmntId = "";
    String pdfUrl;

    boolean isDownloaded = false;
    SharedPreferences prefs;

    ProgressBar progress_bar;
    TextView text_percent;
    TextView text_download;
    LinearLayout linear_download;
    private boolean isShowingTrackSelectionDialog;
    private DefaultTrackSelector trackSelector;
    private DefaultTrackSelector.Parameters trackSelectorParameters;
    String[] speed = {"0.25x", "0.5x", "Normal", "1.5x", "2x"};
    public SimpleExoPlayer player;
    private String VIMDEO_ID = "562715241";
    private static final String VIMEO_ACCESS_TOKEN = "2ac01d5598827dcd854d8acb40627b17";
    private boolean playWhenReady = false; //If true the player auto play the media
    private int currentWindow = 0;
    private long playbackPosition = 0;
    VideoView videoView;
    ImageView setting;
    TextView quality;
    TextView currentPlay;
    Boolean play = false;
    Boolean isVideoStop = false;
    Boolean isDownloading = false;

    int total_duration_in_seconds = 0;

    RelativeLayout elEditComment;


    //Siding images

    private SimpleRecyclerViewAdapter<SlidesBean, ItemImageSlidesBinding> mSlidesAdapter;
    private SlideImageAdapter slideImageAdapter;
    int poss = 0,poss1 = 0;
    boolean open = false ,open1 =false;

    /////////////////////////////////////


    @Override
    protected void onViewReady(Bundle savedInstanceState, Intent intent) {
        trackSelector = new DefaultTrackSelector(this);
        player = new SimpleExoPlayer.Builder(this).setTrackSelector(trackSelector).build();
        ImageView farwordBtn = binding.videoView2.findViewById(R.id.fwd);
        ImageView rewBtn = binding.videoView2.findViewById(R.id.rew);
        setting = binding.videoView2.findViewById(R.id.exo_track_selection_view);
        quality = binding.videoView2.findViewById(R.id.quality);
        currentPlay = binding.videoView2.findViewById(R.id.exo_position);
        ImageView speedBtn = binding.videoView2.findViewById(R.id.exo_playback_speed);
        TextView speedTxt = binding.videoView2.findViewById(R.id.speed);
        elEditComment = findViewById(R.id.elEditComment);
        progress_bar = findViewById(R.id.progress_bar);
        text_percent = findViewById(R.id.text_percent);
        text_download = findViewById(R.id.text_download);
        linear_download = findViewById(R.id.linear_download);
        addListener();


        elEditComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                elEditComment.setVisibility(View.GONE);
            }
        });
        binding.rlCmntDlgSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.rlCmntDlgSub.setVisibility(View.GONE);
            }
        });
        binding.rlCmntDlg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.rlCmntDlg.setVisibility(View.GONE);
            }
        });

        name = MySharedPreferences.getInstance().getString(PlayVideoActivity.this, ConstantsNew.FIRST_NAME);
        email = MySharedPreferences.getInstance().getString(PlayVideoActivity.this, ConstantsNew.EMAIL);
        phone = MySharedPreferences.getInstance().getString(PlayVideoActivity.this, ConstantsNew.NUMBER);
        super.onViewReady(savedInstanceState, intent);
        apiInterface = AppController.getInstance().getApis();
        initView(savedInstanceState);
        configVimeoClient();
        MySharedPreferences.getInstance().saveString(this, ConstantsNew.BACK, "0");

        startTimerVisible();


        //Set the data in watermark on video
        binding.PVname.setText(name);
        binding.PVid.setText(phone);
        binding.PVemail.setText(email);


        speedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(PlayVideoActivity.this);
                builder.setTitle("Set Speed");
                builder.setItems(speed, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // the user clicked on colors[which]

                        if (which == 0) {

                            speedTxt.setVisibility(View.VISIBLE);
                            speedTxt.setText("0.25X");
                            PlaybackParameters param = new PlaybackParameters(0.5f);
                            player.setPlaybackParameters(param);


                        }
                        if (which == 1) {

                            speedTxt.setVisibility(View.VISIBLE);
                            speedTxt.setText("0.5X");
                            PlaybackParameters param = new PlaybackParameters(0.5f);
                            player.setPlaybackParameters(param);


                        }
                        if (which == 2) {

                            speedTxt.setVisibility(View.GONE);
                            PlaybackParameters param = new PlaybackParameters(1f);
                            player.setPlaybackParameters(param);


                        }
                        if (which == 3) {
                            speedTxt.setVisibility(View.VISIBLE);
                            speedTxt.setText("1.5X");
                            PlaybackParameters param = new PlaybackParameters(1.5f);
                            player.setPlaybackParameters(param);

                        }
                        if (which == 4) {


                            speedTxt.setVisibility(View.VISIBLE);
                            speedTxt.setText("2X");

                            PlaybackParameters param = new PlaybackParameters(2f);
                            player.setPlaybackParameters(param);


                        }


                    }
                });
                builder.show();


            }
        });
        farwordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player.seekTo(player.getCurrentPosition() + 10000);
            }
        });
        rewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long num = player.getCurrentPosition() - 10000;
                if (num < 0) {

                    player.seekTo(0);


                } else {

                    player.seekTo(player.getCurrentPosition() - 10000);

                }


            }
        });
        ImageView fullscreenButton = binding.videoView2.findViewById(R.id.fullscreen);
        fullscreenButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SourceLockedOrientationActivity")
            @Override
            public void onClick(View view) {

                int orientation = PlayVideoActivity.this.getResources().getConfiguration().orientation;
                if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                    // code for portrait mode
                    full = false;
                    binding.textAddComment.setVisibility(View.GONE);
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT
                    );
                    params2.setMargins(0, 0, 0, 0);
                    binding.llVIdeo.setLayoutParams(params2);
                    binding.textReportVideo.setVisibility(View.GONE);
//                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

                    hideSystemUi();
//                    binding.cardCropeV.setVisibility(View.VISIBLE);

                } else {
                    // code for landscape mode
                    showSystemUi();
//                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                    full = true;
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );
                    params.height = 600;
                    params.setMargins(0, 0, 0, 0);
                    binding.llVIdeo.setLayoutParams(params);
//                    binding.cardFullV.setVisibility(View.VISIBLE);
                    binding.cardCropeV.setVisibility(View.GONE);
//                    binding.textReportVideo.setVisibility(View.VISIBLE);

                }


            }
        });
        findViewById(R.id.exo_play).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isVideoStop = false;
                player.play();
            }
        });
        findViewById(R.id.exo_pause).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isVideoStop = true;
                String test = String.valueOf(currentPlay.getText());

                String time[] = test.split(":");

                long hour = 0;
                long min = 0;
                long sec = 0;
                if (time.length > 2) {
                    hour = Long.parseLong(time[0]);
                    min = Long.parseLong(time[1]);
                    sec = Long.parseLong(time[2]);

                } else {
                    min = Long.parseLong(time[0]);
                    sec = Long.parseLong(time[1]);
                }

                long t = sec + (60 * min) + (3600 * hour);

                mCurrentVideoDuration = Integer.parseInt(String.valueOf(t));
                if (!mVideoBean.getStatus().equals(Constants.CONTENT_STATUS[2]))
                    mVideoBean.setStatus(Constants.CONTENT_STATUS[2]);
                mVideoBean.setSeconds(mCurrentVideoDuration);
                if (t >= mCurrentVideoDuration) {
                    updateVideoStatusToServer(Constants.CONTENT_STATUS[2]);
                } else {
                    updateVideoStatusToServer(Constants.CONTENT_STATUS[1]);
                }

                player.pause();
            }
        });
        player.addListener(new Player.EventListener() {
            @Override
            public void onPlaybackStateChanged(int state) {
                if (state == ExoPlayer.STATE_ENDED) {
                }
            }
        });


        binding.videoView2.setControllerVisibilityListener(visibility -> {
        });
        setAdapter(new ArrayList<>());
    }

    private void configVimeoClient() {
        com.vimeo.networking.Configuration.Builder configBuilder =
                new com.vimeo.networking.Configuration.Builder(PlayVideoActivity.VIMEO_ACCESS_TOKEN) //Pass app access token
                        .setCacheDirectory(this.getCacheDir());
        VimeoClient.initialize(configBuilder.build());
    }

    private void createMediaItem(String url) {
        MediaItem mediaItem = MediaItem.fromUri(url);
        player.setMediaItem(mediaItem);
    }

    private void initializePlayer2() {
        player = new SimpleExoPlayer.Builder(this).build();
        binding.videoView2.setPlayer(player);
        player.setPlayWhenReady(playWhenReady);
        player.seekTo(currentWindow, playbackPosition);
        player.prepare();


        trackSelector = new DefaultTrackSelector(/* context= */ this);

    }

    private void callVimeoAPIRequest(String videoLink) {
        String url = videoLink;
        Log.d("JHGHGGHK", url);
        String strNew = url.replaceFirst("https://vimeo.com/", "");
        String stringId = strNew.replaceFirst("manage/videos/", "");
        String id = stringId;
        arrayList = new ArrayList<>();
//        id = stringId
        Log.d("JHGHGGHK2", id);
        VIMDEO_ID = id;
        VimeoInterface vimeoInterface = VimeoClientAPI.getClient().create(VimeoInterface.class);
        vimeoInterface.getVimeoUrlResponse(VIMDEO_ID).enqueue(new Callback<VimeoResponse>() {
            @Override
            public void onResponse(@NotNull Call<VimeoResponse> call, @NotNull Response<VimeoResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    arrayList = new ArrayList<>();
                    for (int k = 0; k < response.body().getRequest().getFiles().getProgressive().size(); k++) {
                        String qualityData = response.body().getRequest().getFiles().getProgressive().get(k).getQuality();
                        String mainQuality = qualityData.replaceFirst("p", "");
                        int quality = Integer.parseInt(mainQuality);
                        arrayList.add(quality);
                        Log.d("BNVHKGV", mainQuality);
                    }

                    setting.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.d("BNVHKGV3", String.valueOf(arrayList.size()));
                            Collections.sort(arrayList, Collections.reverseOrder());


                            PopupMenu popup = new PopupMenu(PlayVideoActivity.this, setting);
                            for (int j = 0; j < response.body().getRequest().getFiles().getProgressive().size(); j++) {

                                popup.getMenu().add(0, 0, 0, arrayList.get(j).toString() + "p");
                            }

                            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                public boolean onMenuItemClick(MenuItem item) {
                                    qualityString = String.valueOf(item.getTitle());
                                    getQualityUrl(response.body(), qualityString);
                                    return true;
                                }
                            });

                            popup.show();

                        }
                    });

                    if (qualityString.equalsIgnoreCase("")) {
                        if (response.body().getRequest().getFiles().getProgressive().size() > 0)
                            createMediaItem(response.body().getRequest().getFiles().getProgressive().get(0).getUrl());
                        pvDownloadLink = response.body().getRequest().getFiles().getProgressive().get(0).getUrl();
                        MediaPlayer mp = new MediaPlayer();
                        try {
                            mp.reset();
                            mp.setDataSource(response.body().getRequest().getFiles().getProgressive().get(0).getUrl());
                            mp.prepare();

                            String time = millisecondToTime(mp.getDuration());
                            String h = time;
                            String[] h1 = h.split(":");

                            int hour = 0;
                            int minute = 0;
                            int second = 0;
                            if (h1.length > 2) {
                                hour = Integer.parseInt(h1[0]);
                                minute = Integer.parseInt(h1[1]);
                                second = Integer.parseInt(h1[2]);
                            } else {
                                minute = Integer.parseInt(h1[0]);
                                second = Integer.parseInt(h1[1]);
                            }


                            total_duration_in_seconds = second + (60 * minute) + (3600 * hour);


                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {

                        getQualityUrl(response.body(), qualityString);
                        for (int i = 0; i < response.body().getRequest().getFiles().getProgressive().size(); i++) {
                            if (response.body().getRequest().getFiles().getProgressive().get(i).getQuality().equalsIgnoreCase(qualityString)) {
                                if (response.body().getRequest().getFiles().getProgressive().size() > 0)
                                    createMediaItem(response.body().getRequest().getFiles().getProgressive().get(i).getUrl());

                                MediaPlayer mp = new MediaPlayer();
                                try {
                                    mp.reset();
                                    mp.setDataSource(response.body().getRequest().getFiles().getProgressive().get(i).getUrl());
                                    mp.prepare();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                String time = millisecondToTime(mp.getDuration());

                                String h = time;
                                String[] h1 = h.split(":");

                                int hour = 0;
                                int minute = 0;
                                int second = 0;
                                if (h1.length > 2) {
                                    hour = Integer.parseInt(h1[0]);
                                    minute = Integer.parseInt(h1[1]);
                                    second = Integer.parseInt(h1[2]);
                                } else {
                                    minute = Integer.parseInt(h1[0]);
                                    second = Integer.parseInt(h1[1]);
                                }


                                total_duration_in_seconds = second + (60 * minute) + (3600 * hour);


                                pvDownloadLink = response.body().getRequest().getFiles().getProgressive().get(i).getUrl();
                            }
                        }

                    }
                    Log.d(TAG, response.body().getRequest().getFiles().getProgressive().get(0).getUrl());

                    int seconds = Integer.parseInt(String.valueOf(mVideoBean.getSeconds()));
//                    mVdoPlayer.seekTo(seconds * 1000);
                    player.seekTo(seconds * 1000);

                } else {
                    Log.d("DSJDSBJ", response.message());
                }
            }

            @Override
            public void onFailure(@NotNull Call<VimeoResponse> call, @NotNull Throwable t) {
                Log.e(TAG, Objects.requireNonNull(t.getMessage()));

            }
        });
    }

    private void getQualityUrl(VimeoResponse body, String qualityString) {
        for (int i = 0; i < body.getRequest().getFiles().getProgressive().size(); i++) {
            if (body.getRequest().getFiles().getProgressive().get(i).getQuality().equalsIgnoreCase(qualityString)) {
                if (body.getRequest().getFiles().getProgressive().size() > 0)
                    createMediaItem(body.getRequest().getFiles().getProgressive().get(i).getUrl());

                MediaPlayer mp = new MediaPlayer();
                try {
                    mp.reset();
                    mp.setDataSource(body.getRequest().getFiles().getProgressive().get(i).getUrl());
                    mp.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                pvDownloadLink = body.getRequest().getFiles().getProgressive().get(i).getUrl();
            }
        }

    }

    private void addListener() {

        binding.toolbar.imageBack.setOnClickListener(v -> onBackPressed());

        player.addListener(new Player.EventListener() {
            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                switch (playbackState) {
                    case Player.STATE_BUFFERING:
                        break;
                    case Player.STATE_ENDED:
                        isVideoStop = true;
                        markUnMarkVideoCompleted("mark");
                        break;
                    case Player.STATE_IDLE:
                        break;
                    case Player.STATE_READY:
                        if (play == true) {
                            play = false;
                        } else {
                            play = true;
                        }
                        break;
                    default:
                        break;
                }
            }
        });


    }

    public String millisecondToTime(long duration) {
        String finalTimeString = "";
        String secondTime;

        int hour = (int) (duration / (1000 * 60 * 60));
        int minuets = (int) (duration % (1000 * 60 * 60) / (1000 * 60));
        int seconds = (int) (duration % (1000 * 60 * 60) % (1000 * 60) / 1000);

        if (hour > 0) {
            finalTimeString = hour + ":";
        }

        if (seconds < 10) {
            secondTime = "0" + seconds;
        } else {
            secondTime = "" + seconds;
        }
        finalTimeString = finalTimeString + minuets + ":" + secondTime;

        return finalTimeString;
    }

    @SuppressLint("InlinedApi")
    private void hideSystemUi() {
//        binding.videoView2.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        View decorView = getWindow().getDecorView();
        int uiOptions = decorView.getSystemUiVisibility();
        int newUiOptions = uiOptions;
        newUiOptions |= View.SYSTEM_UI_FLAG_LOW_PROFILE;
        newUiOptions |= View.SYSTEM_UI_FLAG_FULLSCREEN;
        newUiOptions |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        newUiOptions |= View.SYSTEM_UI_FLAG_IMMERSIVE;
        newUiOptions |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(newUiOptions);
    }

    /**
     * Android API level 24 and higher supports multiple windows.
     * As your app can be visible, but not active in split window mode, you need to initialize the player in onStart.
     * Android API level 24 and lower requires you to wait as long as possible until you grab resources,
     * so you wait until onResume before initializing the player.
     */

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT < 24) {
            //Frees the player's resources and destroys it.
            releasePlayer();
        }
    }

    private void releasePlayer() {
        if (player != null) {
            playWhenReady = player.getPlayWhenReady();
            playbackPosition = player.getCurrentPosition();
            currentWindow = player.getCurrentWindowIndex();
            trackSelector = null;
            player.release();
            player = null;
        }
    }

    @Override
    public void onClick(View v) {
        player.setPlayWhenReady(true);
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        videoView.start();
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_play_video;
    }

    @Override
    public void onPermissionsGranted(int requestCode) {

    }

    @Override
    protected void onDestroy() {
        if (mCallMarkComplete != null) mCallMarkComplete.cancel();
        if (mCallRateVideo != null) mCallRateVideo.cancel();
        if (mCallReportQuestion != null) mCallReportQuestion.cancel();
        if (mCallPosterImage != null) mCallPosterImage.cancel();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {

        String test = String.valueOf(currentPlay.getText());

        String time[] = test.split(":");

        long hour = 0;
        long min = 0;
        long sec = 0;
        if (time.length > 2) {
            hour = Long.parseLong(time[0]);
            min = Long.parseLong(time[1]);
            sec = Long.parseLong(time[2]);

        } else {
            min = Long.parseLong(time[0]);
            sec = Long.parseLong(time[1]);
        }

        long t = sec + (60 * min) + (3600 * hour);

        mCurrentVideoDuration = Integer.parseInt(String.valueOf(t));

        mVideoBean.setSeconds(mCurrentVideoDuration);

        if (mVideoBean.getStatus().equals(Constants.CONTENT_STATUS[2])) {


        } else {

            updateVideoStatusToServer(Constants.CONTENT_STATUS[1]);

        }

        player.pause();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.height = 600;
        params.setMargins(0, 0, 0, 0);
        binding.llVIdeo.setLayoutParams(params);
        binding.cardCropeV.setVisibility(View.GONE);
        if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            showFullScreen(false);
            binding.playerControlView.setFullscreenState(false);
        } else {
            updateVideoStatusInBean(Constants.CONTENT_STATUS[1]);
            /*if (AppController.getInstance().isInternetOn()) {

                if (mVideoBean.getStatus().equals(Constants.CONTENT_STATUS[2])||mVideoBean.getStatus().equals(Constants.CONTENT_STATUS[3])){


                }else{

                    updateVideoStatusToServer(Constants.CONTENT_STATUS[1]);

                }


            }*/


            Bundle bundle = new Bundle();
            bundle.putSerializable("videoBean", mVideoBean);
            Intent intent = new Intent();
            intent.putExtras(bundle);
            if (isDownloading) {
                intent.putExtra("isDownloading", "yes");
            } else {
                intent.putExtra("isDownloading", "no");
            }
            intent.putExtra("videoId", videoId);
            setResult(RESULT_OK, intent);
            finishActivityWithBackAnim();
        }


    }

    @Override
    public void onTaskCompletedPdf(String name) {
        //hideBaseProgress();
//        File file = new File(getBaseDirectoryPath() + name);
//        //showToast(file.getAbsolutePath());
//        if (file.exists()) {
//            //showToast(file.getAbsolutePath());
//            //openPdfActivity(file.getAbsolutePath());
//        }

        hideBaseProgress();
        File file = new File(getBaseDirectoryPath() + name);
        if (file.exists()) {
            prepareAllThings(file.getAbsolutePath());
        }
    }

    @Override
    protected void onResume() {
        if (MySharedPreferences.getInstance().getString(this, ConstantsNew.BACK).equalsIgnoreCase("1")) {
            binding.rlRecy.setVisibility(View.VISIBLE);
            binding.rlPDF.setVisibility(View.VISIBLE);
        } else {
            binding.textReportVideo.setVisibility(View.VISIBLE);
        }


        getData();
        super.onResume();

//        hideSystemUi();
        addListener();
        if ((Util.SDK_INT < 24 || player == null)) {
            //Init exoplayer builder
            initializePlayer2();
        }
    }

    @Override
    public void onStart() {
        Timber.tag(TAG).v("onStart called");
        super.onStart();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            maybeCreateManager();
            vdoDownloadManager.addEventListener(this);
        }
        if (Util.SDK_INT >= 24) {
            //Init exoplayer builder
            initializePlayer2();
        }

    }

    @Override
    protected void onStop() {
        if (vdoDownloadManager != null) {
            vdoDownloadManager.removeEventListener(this);
        }
        super.onStop();

        if (Util.SDK_INT >= 24) {
            //Frees the player's resources and destroys it.
            releasePlayer();
        }

    }

    @Override
    public void onQueued(String mediaId, DownloadStatus downloadStatus) {
        //showToastAndLog("Download queued : " + mediaId, Toast.LENGTH_SHORT);
        showToast("Downloading Started");
        addListItem(downloadStatus);
    }

    @Override
    public void onChanged(String mediaId, DownloadStatus downloadStatus) {
        Timber.d("Download status changed: " + mediaId + " " + downloadStatus.downloadPercent + "%");
        updateListItem(downloadStatus);
        //Toast.makeText(this, ""+downloadStatus.bytesDownloaded, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCompleted(String mediaId, DownloadStatus downloadStatus) {
        updateListItem(downloadStatus);
        storeBeanLocally(downloadStatus);
    }

    @Override
    public void onFailed(String mediaId, DownloadStatus downloadStatus) {
        Timber.e(mediaId + " download error: " + downloadStatus.reason);
        Toast.makeText(this, " download error: " + downloadStatus.reason,
                Toast.LENGTH_LONG).show();
        //updateListItem(downloadStatus);
    }

    @Override
    public void onDeleted(String mediaId) {
        showToastAndLog("Deleted " + mediaId, Toast.LENGTH_SHORT);
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private void maybeCreateManager() {
        if (vdoDownloadManager == null) {
            vdoDownloadManager = VdoDownloadManager.getInstance(this);
        }
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
        binding.playerControlView.setPlayer(player);
        showControls(true);

        binding.playerControlView.setFullscreenActionListener(fullscreenToggleListener);
        binding.playerControlView.setControllerVisibilityListener(visibilityListener);

        // load a media to the player
        player.load(mVdoParams);

    }

    @Override
    public void onInitializationFailure(VdoPlayer.PlayerHost playerHost, ErrorDescription errorDescription) {
        String msg = "onInitializationFailure: errorCode = " + errorDescription.errorCode + ": " + errorDescription.errorMsg;
        Timber.e(msg);
        Toast.makeText(PlayVideoActivity.this, "initialization failure: " + errorDescription.errorMsg, Toast.LENGTH_LONG).show();
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
            binding.scrollView.setVisibility(View.GONE);
            findViewById(mPlayerFragment.getId()).setLayoutParams(new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
            binding.playerControlView.setFitsSystemWindows(true);
            // hide system windows
//            showSystemUi(false);
            showControls(false);
        } else {
            // show other views
            binding.relativeToolbar.setVisibility(View.VISIBLE);
            binding.scrollView.setVisibility(View.VISIBLE);
            findViewById(mPlayerFragment.getId()).setLayoutParams(new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
            binding.playerControlView.setFitsSystemWindows(false);
            binding.playerControlView.setPadding(0, 0, 0, 0);


            // show system windows
//            showSystemUi(true);
        }
    }

    private void initView(Bundle savedInstanceState) {
        downloadStatusList = new ArrayList<>();
        binding.textVideoName.setSelected(true);
        mRandom = new Random();
        mHandler = new Handler();
        mVideoTopicsAdapter = new SimpleRecyclerViewAdapter<>(R.layout.item_video_topic, BR.bean, (v, groupBean) -> {
//            if (mVdoPlayer != null) {
            try {
                int seconds = Integer.parseInt(groupBean.getMinutes()) * 60;
                seconds = seconds + Integer.parseInt(groupBean.getSeconds());
//                    mVdoPlayer.seekTo(seconds * 1000);
                player.seekTo(seconds * 1000);

            } catch (Exception ex) {
                ex.printStackTrace();
            }
//            }
        });
        binding.recyclerVideoTopics.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerVideoTopics.setAdapter(mVideoTopicsAdapter);


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

        getPdf();


        KeyboardVisibilityEvent.setEventListener(this, new KeyboardVisibilityEventListener() {

            @Override
            public void onVisibilityChanged(boolean isOpen) {

                if (isOpen) {
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.MATCH_PARENT,
                            RelativeLayout.LayoutParams.MATCH_PARENT
                    );
                    params.setMargins(0, 0, 0, 500);
                    binding.elEditComment.setLayoutParams(params);
                    binding.rlCmntDlgSub.setLayoutParams(params);
                    binding.rlCmntDlg.setLayoutParams(params);
                } else {
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.MATCH_PARENT,
                            RelativeLayout.LayoutParams.MATCH_PARENT
                    );
                    binding.elEditComment.setLayoutParams(params);
                    binding.rlCmntDlgSub.setLayoutParams(params);
                    binding.rlCmntDlg.setLayoutParams(params);

                }

            }
        });

    }

    private void setData() {
        if (mVideoBean != null) {
            getCommentApi(String.valueOf(mVideoBean.getId()));
            binding.textVideoName.setText(mVideoBean.getTitle());
            if (mVideoBean.getDescription() != null && !mVideoBean.getDescription().equals(""))
                binding.textDescription.setText(mVideoBean.getDescription());
            else
                binding.textDescription.setVisibility(View.GONE);

            if (mVideoBean.getTopics() != null) {
                if (mVideoBean.getTopics().size() > 0) {
                    binding.recyclerVideoTopics.setVisibility(View.VISIBLE);
                    binding.textNoData.setVisibility(View.GONE);
                    mVideoTopicsAdapter.setList(mVideoBean.getTopics());
                } else {
                    binding.recyclerVideoTopics.setVisibility(View.GONE);
                    binding.textNoData.setVisibility(View.VISIBLE);
                }
            }

            mDBRepository.getParticularSavedVideoById(mVideoBean.getId()).observe(this, list -> {
                if (list.size() > 0) {
                    setDownloadedUI("downloaded");
                    DownloadStatus downloadStatus = new Gson().fromJson(mVideoBean.getDownloadStatus(), DownloadStatus.class);
                    if (downloadStatus != null) startPlayback(downloadStatus);
                }
            });

            if (mVideoBean.getStatus().equals(Constants.CONTENT_STATUS[2])) {
                markCompletedUI();
            }

            if (mVideoBean.getVideoBanner() != null && !mVideoBean.getVideoBanner().equals("")) {
                Picasso.with(PlayVideoActivity.this)
                        .load(Constants.MEDIA_URL + mVideoBean.getVideoBanner())
                        .into(binding.imagePoster);
            } else {
                binding.rlPoster.setVisibility(View.GONE);
            }
        }
    }

    private void getData() {
        if (getIntent().getExtras() != null) {
            mVideoBean = (VideoBean) getIntent().getSerializableExtra("videoBean");
            videoId = getIntent().getStringExtra("videoId");
            Log.d("videoId",videoId);
            videoVimeoLink = mVideoBean.getVideoLink();

            String isDownloading = getIntent().getStringExtra("isDownloading");

            if (isDownloading != null) {
                if (isDownloading.equalsIgnoreCase("yes")) {
                    linear_download.setVisibility(View.GONE);
                    binding.relativeProgress.setVisibility(View.VISIBLE);
                    binding.linearDownload.setVisibility(View.GONE);
                }
            }

            if (mVideoBean.getIsDownloaded().equalsIgnoreCase("yes")) {
                linear_download.setVisibility(View.VISIBLE);
                binding.relativeProgress.setVisibility(View.GONE);
                binding.imageDownload.setImageResource(R.drawable.tick);
                binding.textDownload.setText("Downlaoded");
            } else {
                linear_download.setVisibility(View.VISIBLE);
                binding.relativeProgress.setVisibility(View.GONE);
                binding.imageDownload.setImageResource(R.drawable.ic_download);
                binding.textDownload.setText("Downlaod");
            }


            callVimeoAPIRequest(mVideoBean.getVideoLink());
            getCommentApi(String.valueOf(videoId));
            if (mVideoBean != null) {
                pdfUrl = mVideoBean.getNotes();

                binding.toolbar.textTitle.setText(mVideoBean.getCategory());
                binding.toolbar.textTitle.setTextColor(Color.parseColor("#484848"));
            }
        }
    }

    private void prepareDownloadVideo() {
        if (checkIsInternetOn()) {
            if (mVideoBean != null) {
                if (!binding.textDownload.getText().equals(getString(R.string.downloaded))) {
                    String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
                    Permissions.check(this, permissions, null, null, new PermissionHandler() {
                        @Override
                        public void onGranted() {
                            getOptions();
                        }
                    });
                } else {
                    showConfirmPopUp("download");
                }
            }
        }
    }

    private void prepareMarkDownload() {
        if (checkIsInternetOn()) {
            if (mVideoBean != null) {
                if (mVideoBean.getStatus().equals(Constants.CONTENT_STATUS[2])) {
                    showConfirmPopUp("un_mark");
                } else {
                    player.seekTo(total_duration_in_seconds * 1000);
                    markUnMarkVideoCompleted("mark");
                }
            }
        }
    }

    private void showConfirmPopUp(String value) {
        mConfirmDialog = new BaseCustomDialog<>(this, R.layout.dialog_confirm, view -> {
            switch (view.getId()) {
                case R.id.text_cancel:
                    mConfirmDialog.dismiss();
                    break;
                case R.id.text_yes:
                    mConfirmDialog.dismiss();
                    if (value.equals("download")) {
                        removeDownloadedVideoFromDB();
                    } else {
                        player.seekTo(mVideoBean.getSeconds());
                        markUnMarkVideoCompleted(value);
                    }
                    break;
            }
        });
        Objects.requireNonNull(mConfirmDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        if (value.equals("download")) {
            mConfirmDialog.getBinding().textMessage.setText(R.string.title_remove_downloaded_video);
        } else {
            mConfirmDialog.getBinding().textMessage.setText(R.string.title_un_mark_completed_video);
        }
        mConfirmDialog.getBinding().imageIcon.setImageResource(R.drawable.ic_alert);
        mConfirmDialog.getBinding().textYes.setText(R.string.text_yes);
        mConfirmDialog.setCancelable(true);
        mConfirmDialog.show();
    }

    private void removeDownloadedVideoFromDB() {
        mDBRepositoryKotlin.deleteDownloadVideo(mVideoBean.getId());
        setDownloadedUI("download");
    }

    private void goToSlidesScreen() {
        Bundle bundle = new Bundle();
        bundle.putSerializable("videoBean", mVideoBean);
        startActivity(new Intent(this, SlidesActivity.class).putExtras(bundle));
        goNextAnimation();
    }

    private void goToNotesScreen() {
        /*String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        Permissions.check(this, permissions, R.string.enable_media_permission, null, new PermissionHandler() {
            @Override
            public void onGranted() {
                if (UtilMethods.getSavedPdfs(PlayVideoActivity.this).contains(getBaseDirectoryPath() + mVideoBean.getTitle() + mVideoBean.getId())) {
                    File file = new File(getBaseDirectoryPath() + mVideoBean.getTitle() + mVideoBean.getId());
                    if (file.exists()) {
                        OpenPdfActivity(file.getAbsolutePath());
                    }
                } else {
                    //showBaseProgress();
                    AsyncLoadPdf runner = new AsyncLoadPdf(PlayVideoActivity.this, PlayVideoActivity.this, mVideoBean.getTitle() + mVideoBean.getId());
                    runner.execute(Constants.MEDIA_URL + mVideoBean.getNotes(), mVideoBean.getTitle() + mVideoBean.getId());
                }
            }
        });*/
    }

    private void reportErrorDialog() {
        mReportErrorDialog = new BaseCustomDialog<>(this, R.layout.dialog_report_error, view -> {
            switch (view.getId()) {
                case R.id.text_cancel:
                    mReportErrorDialog.dismiss();
                    break;
                case R.id.text_submit:
                    if (mReportErrorDialog.getBinding().editTextComment.getText().length() > 0) {
                        mReportErrorDialog.dismiss();
                        reportErrorApi(mReportErrorDialog.getBinding().editTextComment.getText().toString());

                    }
                    break;
            }
        });
        Objects.requireNonNull(mReportErrorDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mReportErrorDialog.setCancelable(true);
        mReportErrorDialog.show();
    }

    private void updateListItem(DownloadStatus status) {

        if (status != null) {
            binding.linearDownload.setVisibility(View.GONE);
            binding.relativeProgress.setVisibility(View.VISIBLE);
            binding.progressBar.setProgress(status.downloadPercent);
            binding.textPercent.setText(String.format("%s%%", String.valueOf(status.downloadPercent)));
        }
        // if media already in downloadStatusList, update it
        String mediaId = null;
        if (status != null) {
            mediaId = status.mediaInfo.mediaId;
        }
        int position = -1;
        for (int i = 0; i < downloadStatusList.size(); i++) {
            if (downloadStatusList.get(i).mediaInfo.mediaId.equals(mediaId)) {
                position = i;
                break;
            }
        }
        if (position >= 0) {
            downloadStatusList.set(position, status);
            // downloadsAdapter.notifyItemChanged(position);
        } else {
            Timber.e("item not found in adapter");
        }
        Timber.d("D");
        // updateDeleteAllButton();
    }

    private void storeBeanLocally(DownloadStatus downloadStatus) {
        setDownloadedUI("downloaded");
        String dStatus = new Gson().toJson(downloadStatus);
        mVideoBean.setDownloadStatus(dStatus);
        mVideoBean.setType(Constants.SAVED);
        mDBRepositoryKotlin.insertSingleVideo(mVideoBean);
        mDBRepositoryKotlin.updateDownloadStatusOfVideo(mVideoBean);
    }

    private void setDownloadedUI(String type) {
        binding.relativeProgress.setVisibility(View.GONE);
        binding.linearDownload.setVisibility(View.VISIBLE);
        if (type.equals("downloaded")) {
            binding.imageDownload.setImageResource(R.drawable.ic_right);
            binding.textDownload.setText(getString(R.string.downloaded));
        } else {
            binding.imageDownload.setImageResource(R.drawable.ic_download);
            binding.textDownload.setText(getString(R.string.download));
        }
    }

    private void addListItem(DownloadStatus downloadStatus) {
        downloadStatusList.add(0, downloadStatus);
    }

    private void getOptions() {
        HandlerThread handlerThread = new HandlerThread(TAG);
        handlerThread.start();
        new Handler(handlerThread.getLooper()).post(() ->
                new OptionsDownloader().downloadOptionsWithOtp(mVideoBean.getOtp(), mVideoBean.getPlaybackInfo(), new OptionsDownloader.Callback() {
                    @Override
                    public void onOptionsReceived(DownloadOptions options) {
                        Timber.i("onOptionsReceived");
                        showSelectionDialog(options, options.mediaInfo.duration);
                    }

                    @Override
                    public void onOptionsNotReceived(ErrorDescription errDesc) {
                        String errMsg = "onOptionsNotReceived : " + errDesc.toString();
                        Timber.e(errMsg);
                        Toast.makeText(PlayVideoActivity.this, errMsg, Toast.LENGTH_LONG).show();
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

        binding.linearDownload.setEnabled(false);

        // build a DownloadRequest
        DownloadRequest request = new DownloadRequest.Builder(selections, downloadLocation).build();

        // enqueue request to VdoDownloadManager for download
        try {
            vdoDownloadManager.enqueue(request);
        } catch (IllegalArgumentException | IllegalStateException e) {
            binding.linearDownload.setEnabled(false);
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
        initializePlayer();
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
                PDFView.with(this).videoBean(mVideoBean).swipeHorizontal(false).start();
                binding.rlRecy.setVisibility(View.GONE);
                binding.llDet.setVisibility(View.VISIBLE);
                binding.llTimeLine.setVisibility(View.VISIBLE);
                binding.rlComment.setVisibility(View.GONE);
                binding.textAddComment.setVisibility(View.GONE);
            } else {
                showToast("Not available");
            }
        }
    }

    private void OpenPdfActivity(String absolutePath) {
        PDFView.with(this)
                .fromFilePath(absolutePath)
                .videoBean(mVideoBean)
                .swipeHorizontal(false)
                .start();
    }

    private void initializePlayer() {
        if (mVdoParams != null) {
            // initialize the playerFragment; a VdoPlayer instance will be received
            // in onInitializationSuccess() callback
            mPlayerFragment.initialize(PlayVideoActivity.this);
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
                /*Pair<String, String> pair = Utilities.getSampleOtpAndPlaybackInfo();
                vdoParams = new VdoPlayer.VdoInitParams.Builder()
                        .setOtp(pair.first)
                        .setPlaybackInfo(pair.second)
                        .setPreferredCaptionsLanguage("en")
                        .build();
                Log.i(TAG, "obtained new otp and playbackInfo");*/

                /*
                        .setOtp("20160313versASE323nwG87ycWQgbkaxJgTbWqry9JcunuFJBcvRP2KYZsM9rXq1")
                        .setPlaybackInfo("eyJ2aWRlb0lkIjoiNTVjMDBiODI3MmQ2MjM1OWUxZWJhOTk3MWVkMWRlMzEifQ==")*/

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
        runOnUiThread(() -> Toast.makeText(PlayVideoActivity.this, message, Toast.LENGTH_SHORT).show());
    }

    private void showControls(boolean show) {
        Timber.tag(TAG).v("%s controls", (show ? "show" : "hide"));
        if (show) {
            binding.playerControlView.show();
        } else {
            binding.playerControlView.hide();
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

    private void showSystemUi() {
//        if (!show) {
//            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                    | View.SYSTEM_UI_FLAG_FULLSCREEN);
//        } else {
//            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
//        }

        View decorView = getWindow().getDecorView();
        int uiOptions = decorView.getSystemUiVisibility();
        int newUiOptions = uiOptions;
        newUiOptions &= ~View.SYSTEM_UI_FLAG_LOW_PROFILE;
        newUiOptions &= ~View.SYSTEM_UI_FLAG_FULLSCREEN;
        newUiOptions &= ~View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        newUiOptions &= ~View.SYSTEM_UI_FLAG_IMMERSIVE;
        newUiOptions &= ~View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(newUiOptions);
    }

    private void showRateDialog() {
        mDialogRating = new BaseCustomDialog<>(this, R.layout.dialog_rating, view -> {
            switch (view.getId()) {
                case R.id.text_cancel:
                    mDialogRating.dismiss();
                    break;
                case R.id.text_rate:
                    if (mDialogRating.getBinding().ratingBar.getRating() > 0)
                        rateVideo(mDialogRating);
                    break;
            }
        });
        Objects.requireNonNull(mDialogRating.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialogRating.setCancelable(true);
        mDialogRating.show();
    }

    private void updateVideoStatusInBean(String type) {
        if (!mVideoBean.getStatus().equals(Constants.CONTENT_STATUS[2]))
            mVideoBean.setStatus(type);
        mVideoBean.setSeconds(mCurrentVideoDuration);

        mDBRepositoryKotlin.updateSecondAndStatusOfVideo(mVideoBean);
    }

    private void updateVideoStatusToServer(String status) {
        HashMap<String, String> map = new HashMap<>();
        map.put("video_id", String.valueOf(mVideoBean.getId()));
        map.put("status", status);
        map.put("video_duration", total_duration_in_seconds + "");
        map.put("seconds", String.valueOf(mCurrentVideoDuration));
        mCallUpdateVideoStatus = AppController.getInstance().getApis().updateVideoStatus(getHeader(), map);
        mCallUpdateVideoStatus.enqueue(new ResponseHandler<SuccessBean>() {
            @Override
            public void onSuccess(SuccessBean successBean) {
                Timber.i("Success");
            }

            @Override
            public void apiError(ApiErrorBean t) {
                hideBaseProgress();
                //if (t != null) showErrorToast(t.getMessage());
            }

            @Override
            public void onComplete(Call<SuccessBean> call, Throwable t) {
                //onCallComplete(call, t);
            }

            @Override
            public void statusCode(int t) {
                super.statusCode(t);
                handleCodes(t);
            }
        });

    }

    private void markUnMarkVideoCompleted(String value) {
        showBaseProgress();
        HashMap<String, String> map = new HashMap<>();
        map.put("video_id", String.valueOf(mVideoBean.getId()));
        if (value.equals("un_mark")) {
            unMarkCompletedUI();
            map.put("status", Constants.CONTENT_STATUS[1]);
            map.put("seconds", mVideoBean.getSeconds() + "");
            map.put("type", "0");
            map.put("video_duration", total_duration_in_seconds + "");
        } else if(value.equals("mark")){
            markCompletedUI();
            map.put("type", "1");
            map.put("status", Constants.CONTENT_STATUS[2]);
            map.put("seconds", total_duration_in_seconds + "");
            map.put("video_duration", total_duration_in_seconds + "");
        }

        mCallMarkComplete = AppController.getInstance().getApis().markCompleted(getHeader(), map);
        mCallMarkComplete.enqueue(new ResponseHandler<SuccessBean>() {
            @Override
            public void onSuccess(SuccessBean successBean) {
                if (successBean != null) {
                    if (successBean.getStatus() == Constants.SUCCESS_CODE) {
                        if (value.equals("un_mark")) {
                            mVideoBean.setStatus(Constants.CONTENT_STATUS[1]);
                            showToast("Video Unmarked successfully");
                        } else {
                            mVideoBean.setStatus(Constants.CONTENT_STATUS[2]);
                            showToast("Video marked successfully");
                            markCompletedUI();
                        }
                        mDBRepositoryKotlin.updateSecondAndStatusOfVideo(mVideoBean);
                    }
                }
            }

            @Override
            public void apiError(ApiErrorBean t) {
                hideBaseProgress();
                //if (t != null) showErrorToast(t.getMessage());
            }

            @Override
            public void onComplete(Call<SuccessBean> call, Throwable t) {
                onCallComplete(call, t);
            }

            @Override
            public void statusCode(int t) {
                super.statusCode(t);
                handleCodes(t);
            }
        });
    }

    private void markCompletedUI() {
        mVideoBean.setStatus(Constants.CONTENT_STATUS[2]);
        binding.imageMark.setImageResource(R.drawable.ic_right);
        binding.textMark.setText(getString(R.string.title_marked_completed));
    }

    private void unMarkCompletedUI() {
        mVideoBean.setStatus(Constants.CONTENT_STATUS[1]);
        binding.imageMark.setImageResource(R.drawable.ic_square);
        binding.textMark.setText(getString(R.string.title_mark_complete));
    }

    private void rateVideo(BaseCustomDialog<DialogRatingBinding> mDialogRating) {
        if (mVideoBean != null) {

            HashMap<String, String> map = new HashMap<>();
            map.put("video_id", String.valueOf(mVideoBean.getId()));
            map.put("rate", String.valueOf(mDialogRating.getBinding().ratingBar.getRating()));
            mCallRateVideo = AppController.getInstance().getApis().rateVideo(getHeader(), map);
            mCallRateVideo.enqueue(new ResponseHandler<SuccessBean>() {
                @Override
                public void onSuccess(SuccessBean successBean) {
                    if (successBean != null) {
                        if (successBean.getStatus() == Constants.SUCCESS_CODE) {
                            mDialogRating.dismiss();
                            goBack();
                            //showSuccessToast(successBean.getMessage());
                        }
                    }
                }

                @Override
                public void apiError(ApiErrorBean t) {
                    hideBaseProgress();
                    if (t != null) showErrorToast(t.getMessage());
                }

                @Override
                public void onComplete(Call<SuccessBean> call, Throwable t) {
                    onCallComplete(call, t);
                }

                @Override
                public void statusCode(int t) {
                    super.statusCode(t);
                    handleCodes(t);
                }
            });
        }
    }

    private void reportErrorApi(String comments) {
        if (mCallReportQuestion != null) mCallReportQuestion.cancel();
        HashMap<String, String> map = new HashMap<>();
        map.put("table_id", String.valueOf(mVideoBean.getId()));
        map.put("comments", comments);
        map.put("type", "video");
        mCallReportQuestion = AppController.getInstance().getApis().reportContent(getHeader(), map);
        mCallReportQuestion.enqueue(new ResponseHandler<SuccessBean>() {
            @Override
            public void onSuccess(SuccessBean successBean) {

                if (successBean != null) {
                    if (successBean.getStatus() == Constants.SUCCESS_CODE) {
                        showToast("Reported Successfully");
                    }
                }
            }

            @Override
            public void apiError(ApiErrorBean t) {

                Timber.d(t.getMessage());
            }

            @Override
            public void onComplete(Call<SuccessBean> call, Throwable t) {

                onCallComplete(call, t);
            }

            @Override
            public void statusCode(int t) {
                super.statusCode(t);
                handleCodes(t);
            }
        });
    }

    @SuppressLint("NonConstantResourceId")
    private BaseCallback baseCallback = view -> {
        switch (view.getId()) {
            case R.id.image_back:
                binding.linearChatLine.setBackgroundColor(Color.parseColor("#00FFFDFD"));
                goBack();
                break;
            case R.id.linear_mark_complete:
                prepareMarkDownload();
                break;
            case R.id.linear_download:
//                prepareDownloadVideo();

                if (mVideoBean.getIsDownloaded().equalsIgnoreCase("yes")) {

                    new AlertDialog.Builder(this)
                            .setTitle("Confirmation!")
                            .setMessage("Are you sure you want to remove download?")

                            // Specifying a listener allows you to take an action before dismissing the dialog.
                            // The dialog is automatically dismissed when a dialog button is clicked.
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                    SharedPreferences preferences = getSharedPreferences("flytrom", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = preferences.edit();
                                    editor.clear();
                                    editor.apply();

                                    downloadVideoStatusUpdate(mVideoBean, "remove");

                                    linear_download.setVisibility(View.VISIBLE);
                                    binding.relativeProgress.setVisibility(View.GONE);
                                    binding.imageDownload.setImageResource(R.drawable.ic_download);
                                    binding.textDownload.setText("Downlaod");

                                }
                            })

                            // A null listener allows the button to dismiss the dialog and take no further action.
                            .setNegativeButton(android.R.string.no, null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();

                } else {


                    PopupMenu popup = new PopupMenu(this, binding.linearDownload);
                    //Inflating the Popup using xml file
                    popup.getMenuInflater().inflate(R.menu.download_popup, popup.getMenu());

                    //registering popup with OnMenuItemClickListener
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem item) {
                            isDownloading = true;
                            if (item.getTitle().toString().equalsIgnoreCase("High Quality")) {
                                callVimeoAPIRequest2("720p");
                            } else {
                                callVimeoAPIRequest2("360p");

                            }
                            return true;
                        }
                    });
                    popup.show();

                }


                break;
            case R.id.cardFullV:
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT
                );
                params2.setMargins(0, 0, 0, 0);
                binding.llVIdeo.setLayoutParams(params2);
                binding.cardFullV.setVisibility(View.GONE);
                binding.textReportVideo.setVisibility(View.GONE);
                binding.cardCropeV.setVisibility(View.VISIBLE);
                break;
            case R.id.cardCropeV:
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                params.height = 400;
                params.setMargins(0, 0, 0, 0);
                binding.llVIdeo.setLayoutParams(params);
                binding.cardFullV.setVisibility(View.VISIBLE);
                binding.cardCropeV.setVisibility(View.GONE);
                binding.textReportVideo.setVisibility(View.VISIBLE);
                break;
            case R.id.linear_slides:
                //goToSlidesScreen();
                initView1(mVideoBean);
                binding.scrollView.setVisibility(View.GONE);
                binding.rlRecy.setVisibility(View.GONE);
                binding.reportTextViewLayout.setVisibility(View.GONE);
                binding.slideImagesOperation.setVisibility(View.VISIBLE);
                break;


            case R.id.linear_notes:
                binding.linearChatLine.setBackgroundColor(Color.parseColor("#00FFFDFD"));
                binding.rlComment.setVisibility(View.GONE);
                binding.textAddComment.setVisibility(View.GONE);
                binding.textReportVideo.setVisibility(View.GONE);
                if (mVideoBean != null) {
                    if (mVideoBean.getNotes() != null && !mVideoBean.getNotes().equals("")) {
                        binding.rlRecy.setVisibility(View.VISIBLE);
                        binding.llDet.setVisibility(View.GONE);
                        new RetrivePDFfromUrl().execute("https://learningapp.flytrom.com/media/" + pdfUrl);
                    } else {
                        showToast("Not available");
                    }
                }
                //goToNotesScreen();
                break;
            case R.id.cardCancel:
                binding.pvNid.setVisibility(View.GONE);
                binding.rlRecy.setVisibility(View.GONE);
                binding.llDet.setVisibility(View.VISIBLE);
                binding.llTimeLine.setVisibility(View.VISIBLE);
                binding.rlComment.setVisibility(View.GONE);
                binding.textAddComment.setVisibility(View.GONE);
                binding.textReportVideo.setVisibility(View.VISIBLE);

                //goToNotesScreen();
                break;
            case R.id.cardfull:
//                 openPdfActivity();
                binding.rlPDF.setVisibility(View.VISIBLE);
                player.pause();
                binding.linearChatLine.setBackgroundColor(Color.parseColor("#00FFFDFD"));

                new RetrivePDFfromUrl2().execute("https://learningapp.flytrom.com/media/" + pdfUrl);
                break;
            case R.id.cardmin:
//                 openPdfActivity();
                binding.linearChatLine.setBackgroundColor(Color.parseColor("#00FFFDFD"));
                binding.rlPDF.setVisibility(View.GONE);
                break;
            case R.id.cardCancelFull:
//                 openPdfActivity();
                binding.linearChatLine.setBackgroundColor(Color.parseColor("#00FFFDFD"));
                binding.rlPDF.setVisibility(View.GONE);
                break;
            case R.id.linear_chat:
                try {
                    binding.linearChatLine.setBackgroundColor(Color.parseColor("#3C9FEB"));
                    binding.rlComment.setVisibility(View.VISIBLE);
                    binding.textAddComment.setVisibility(View.VISIBLE);
                    binding.llTimeLine.setVisibility(View.GONE);
                } catch (Exception e) {
                    e.printStackTrace();
                }

//                gotoWhatsUp();
                break;
            case R.id.text_report_video:
                binding.linearChatLine.setBackgroundColor(Color.parseColor("#00FFFDFD"));

                binding.rlComment.setVisibility(View.GONE);
                binding.textAddComment.setVisibility(View.GONE);
                if (mVideoBean != null) reportErrorDialog();
                break;
            case R.id.image_play:
                binding.linearChatLine.setBackgroundColor(Color.parseColor("#00FFFDFD"));
                binding.rlPoster.setVisibility(View.GONE);
                mVdoPlayer.setPlayWhenReady(true);
                break;
            case R.id.text_add_comment:
                //binding.rlCmntDlg.setVisibility(View.VISIBLE);
                showCommentAlertDialogBox();
                break;
            case R.id.rl_cmnt_dlg:
                InputMethodManager imm2 = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm2.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                break;
            case R.id.addCmnt:
                String commnet = binding.comment.getText().toString().trim();
                if (commnet.equals("")) {
                    Toast.makeText(PlayVideoActivity.this, "Please enter your comment in the box", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    HashMap<String, String> map = new HashMap<>();
                    map.put("video_id", String.valueOf(mVideoBean.getId()));
                    map.put("comment", commnet);
                    map.put("link_id", commentID);
                    callCommnetApi(map);
                    getCommentApi(String.valueOf(mVideoBean.getId()));
                    binding.rlCmntDlg.setVisibility(View.GONE);
                    binding.comment.setText("");
                    try {
                        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                break;
            case R.id.cancleDlg:
                binding.rlCmntDlg.setVisibility(View.GONE);
                try {
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            //Post the Sub comment on subject video
            case R.id.addCmntSub:
                String commnet2 = binding.commentSub.getText().toString().trim();
                if (commnet2.isEmpty()) {
                    Toast.makeText(this, "Please enter your comment in the box", Toast.LENGTH_SHORT).show();
                    return;
                }
                HashMap<String, String> mapsUB = new HashMap<>();
                mapsUB.put("video_id", String.valueOf(mVideoBean.getId()));
                mapsUB.put("comment", commnet2);
                mapsUB.put("link_id", commentID);
                callCommnetApi(mapsUB);
                binding.commentSub.setText("");
                binding.rlCmntDlgSub.setVisibility(View.GONE);
                getCommentApi(String.valueOf(mVideoBean.getId()));
                try {
                    binding.commentSub.getText().clear();
                    binding.commentSub.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.cancleDlgSub:
                binding.rlCmntDlgSub.setVisibility(View.GONE);
                try {
                    binding.commentSub.getText().clear();
                    binding.commentSub.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;

            case R.id.rotateV:
                binding.rotateV2.setVisibility(View.VISIBLE);
                binding.rotateV.setVisibility(View.GONE);
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                break;

            case R.id.rotateV2:
                binding.rotateV2.setVisibility(View.GONE);
                binding.rotateV.setVisibility(View.VISIBLE);
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                break;

            case R.id.dltComent:
                binding.rlDltCmt.setVisibility(View.GONE);
                callDeleteCommentApi(cmntId);
                break;

            case R.id.addEditCmnt:
                //callEditCommentApi(cmntId);
                break;

            case R.id.cnclDlt:
                binding.rlDltCmt.setVisibility(View.GONE);
                break;

            case R.id.cancleDlgEdit:
                binding.elEditComment.setVisibility(View.GONE);
                binding.editComment.setText("");
                break;

            ///Sliding Images
//                case R.id.image_back:
//                    goBack();
//                    break;
            case R.id.image_slide_back_cancel:
                binding.slideImagesOperation.setVisibility(View.GONE);
                binding.scrollView.setVisibility(View.VISIBLE);
                binding.rlRecy.setVisibility(View.GONE);
                binding.reportTextViewLayout.setVisibility(View.VISIBLE);
                break;


            case R.id.image_exit_full_screen:
                binding.relativeParticularSlide.setVisibility(View.GONE);
                /*                Log.d("dfd", "ddf");
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);*/
                break;
            case R.id.image_exit_full_screen2:
                binding.rlSlide.setVisibility(View.GONE);
                poss = 0;
                open = false;
                break;
            case R.id.imgNextSlide:
                int current = getItem(+1);
                if (current < mVideoBean.getSlides().size()) {
                    binding.viewPagerMain.setCurrentItem(current);
                } else {

                }
                break;
            case R.id.imgBackSlide:
                int current2 = getItem2(1);
                if (current2 < 0) {

                } else {
                    binding.viewPagerMain.setCurrentItem(current2);
                }
                break;


            //                Full Slide
            case  R.id.slideViewFullLayout:
                initView2(mVideoBean);
                binding.tool.setVisibility(View.GONE);
                binding.Wtermrk.setVisibility(View.GONE);
                binding.reportTextViewLayout.setVisibility(View.GONE);
                binding.slideViewLayout.setVisibility(View.VISIBLE);
                break;



            case R.id.cardSlideMin:
                binding.tool.setVisibility(View.VISIBLE);
                binding.Wtermrk.setVisibility(View.VISIBLE);
                binding.reportTextViewLayout.setVisibility(View.VISIBLE);
                binding.slideViewLayout.setVisibility(View.GONE);
                break;
            case R.id.imgNextSlide1:
                int current1 = getItem101(+1);
                if (current1 < mVideoBean.getSlides().size()) {
                    binding.viewPagerMain1.setCurrentItem(current1);
                } else {
                }
                break;
            case R.id.imgBackSlide1:
                int current21 = getItem102(1);
                if (current21 < 0) {
                } else {
                    binding.viewPagerMain1.setCurrentItem(current21);
                }
                break;
        }
    };

    ///////////////////////////////////////////////////////////////////////
    /* Comment Section */

    //Post Comment on subject video
    private void showCommentAlertDialogBox(){
        AlertDialog alertDialog=new AlertDialog.Builder(PlayVideoActivity.this).create();
        View view= LayoutInflater.from(getApplicationContext()).inflate(R.layout.add_comment_alertdilog_box,null,false);
        alertDialog.setView(view);
        alertDialog.setCancelable(false);
        alertDialog.show();
        Button cancelBtn,submitBtn;
        EditText etComment;

        etComment=view.findViewById(R.id.comment);
        cancelBtn=view.findViewById(R.id.cancleDlg);
        submitBtn=view.findViewById(R.id.addCmnt);

        cancelBtn.setOnClickListener(v->{
            if(etComment.isFocused()){
                hideKeyboard();
            }
            alertDialog.dismiss();
        });
        submitBtn.setOnClickListener(v->{
            String cmt=etComment.getText().toString().trim();
            if(cmt.equalsIgnoreCase("")){
                showToast("Please type the comment..");}
            else {
                HashMap<String, String> map = new HashMap<>();
                map.put("video_id", String.valueOf(mVideoBean.getId()));
                map.put("comment", cmt);
                map.put("link_id", commentID);
                callCommnetApi(map);
                alertDialog.dismiss();
            }
        });
    }
    //Post Sub comment on the subject video
    @Override
    public void onitemClickVideo(String id) {
        commentID = id;
        //binding.rlCmntDlgSub.setVisibility(View.VISIBLE);
        //binding.rlCmntDlg.setVisibility(View.GONE);
        showSubCommentPostAlertDialogBox(commentID);
    }
    private void showSubCommentPostAlertDialogBox(String commentID){
        AlertDialog alertDialog=new AlertDialog.Builder(PlayVideoActivity.this).create();
        View view= LayoutInflater.from(getApplicationContext()).inflate(R.layout.add_comment_alertdilog_box,null,false);
        alertDialog.setView(view);
        alertDialog.setCancelable(false);
        alertDialog.show();
        Button cancelBtn,submitBtn;
        EditText etComment;

        etComment=view.findViewById(R.id.comment);
        cancelBtn=view.findViewById(R.id.cancleDlg);
        submitBtn=view.findViewById(R.id.addCmnt);

        cancelBtn.setOnClickListener(v->{
            if(etComment.isFocused()){
                hideKeyboard();
            }
            alertDialog.dismiss();
        });
        submitBtn.setOnClickListener(v->{
            String cmt=etComment.getText().toString().trim();
            if(cmt.equalsIgnoreCase("")){
                showToast("Please type the comment..");}
            else {
                HashMap<String, String> mapsUB = new HashMap<>();
                mapsUB.put("video_id", String.valueOf(mVideoBean.getId()));
                mapsUB.put("comment", cmt);
                mapsUB.put("link_id", commentID);
                callCommnetApi(mapsUB);
                alertDialog.dismiss();
            }
        });
    }

    //Comment on the subject video
    private void callCommnetApi(HashMap<String, String> map) {
        showBaseProgress();
        apiInterface.sendComment(getHeader(), map).enqueue(new Callback<SendCommentModel>() {
            @Override
            public void onResponse(@NonNull Call<SendCommentModel> call, @NonNull Response<SendCommentModel> response) {
                SendCommentModel faqModle = response.body();
                if (response.isSuccessful()) {
                    binding.rlCmntDlg.setVisibility(View.GONE);
                    binding.rlCmntDlgSub.setVisibility(View.GONE);
                    binding.comment.setText("");
                    getCommentApi(String.valueOf(videoId));
                } else {
                    Log.d("BVHJH<B", response.message());
                }
            }

            @Override
            public void onFailure(Call<SendCommentModel> call, Throwable t) {
                hideBaseProgress();
                Log.d("QWERTYU", t.getLocalizedMessage());

            }
        });

    }

    public void getCommentApi(String valueOf) {
        showBaseProgress();
        apiInterface.getVideoComment(getHeader(), "1", valueOf).enqueue(new Callback<GerCommentModel>() {
            @Override
            public void onResponse(Call<GerCommentModel> call, Response<GerCommentModel> response) {
                GerCommentModel faqModle = response.body();
                hideBaseProgress();
                if (response.isSuccessful()) {
                    String userId = PrefUtils.getInstance().getUser().getId();
                    List<GetCommentDatum> list = new ArrayList<>();
                    for (int i = 0; i < faqModle.getData().size(); i++) {
//                        if (faqModle.getData().get(i).is_approve.equals("1")) {
//                            GetCommentDatum model = faqModle.data.get(i);
//                            list.add(model);
//                        }

                        list.add(faqModle.getData().get(i));
                        if(!faqModle.getData().get(i).getUser_name().equals("")){
                            if (faqModle.getData().get(i).is_approve.equals("1")) {
                                GetCommentDatum model = faqModle.data.get(i);
                                list.add(model);
                            }
                        }
                        //                        if (!faqModle.getData().get(i).user_id.equals(userId)) {
//                            if (faqModle.getData().get(i).is_approve.equals("1")) {
//                                GetCommentDatum model = faqModle.data.get(i);
//                                list.add(model);
//                            }
//                        } else {
//                            list.add(faqModle.data.get(i));
//                        }
                    }
                    Collections.reverse(list);
                    getCommentAdapter.setList(list);
                    Log.d("PlayVideoActivity", response.message());
                    hideBaseProgress();
                } else {
                    Log.d("BVHJH<B2", response.message());
                }
            }

            @Override
            public void onFailure(Call<GerCommentModel> call, Throwable t) {
                hideBaseProgress();
                Log.d("QWERTYU", t.getLocalizedMessage());

            }
        });

    }

    private void setAdapter(List<GetCommentDatum> data) {
        getCommentAdapter = new GetCommentAdapter(PlayVideoActivity.this, data, this::onitemClickVideo, this::onClickEdit);
        binding.videoComments.setHasFixedSize(true);
        binding.videoComments.setLayoutManager(new LinearLayoutManager(this));
        binding.videoComments.setAdapter(getCommentAdapter);
        binding.videoComments.invalidate();
    }


    //Edit the main comment of subject video
    @Override
    public void onClickEdit(String id, CharSequence title, String comment) {
        cmntId = id;
        if (title.toString().equalsIgnoreCase("Delete")) {
            binding.rlDltCmt.setVisibility(View.VISIBLE);
        } else {
            //binding.elEditComment.setVisibility(View.VISIBLE);
            //binding.editComment.setText(comment);
            editTheSubCommentOfVideo(cmntId,comment);
        }
    }
    //Call delete comment api
    public void callDeleteCommentApi(String id) {
        showBaseProgress();
        mCallResetTest = AppController.getInstance().getApis().deleteComment(getHeader(), id);

        mCallResetTest.enqueue(new ResponseHandler<SuccessBean>() {
            @Override
            public void onSuccess(SuccessBean successBean) {
                hideBaseProgress();
                if (successBean != null) {
                    if (successBean.getStatus() == Constants.SUCCESS_CODE) {
                        showToast(successBean.getMessage());
                        getCommentApi(String.valueOf(videoId));
                    }
                }
            }

            @Override
            public void apiError(ApiErrorBean t) {
                hideBaseProgress();
                if (t != null) showToast(t.getMessage());
            }

            @Override
            public void onComplete(Call<SuccessBean> call, Throwable t) {
                hideBaseProgress();
                onCallComplete(call, t);
            }

            @Override
            public void statusCode(int t) {
                super.statusCode(t);
                handleCodes(t);
            }
        });
    }

    //Call edit comment api
//    public void callEditCommentApi(String id) {
//        String edtComment = String.valueOf(binding.editComment.getText());
//        mCallResetTest = AppController.getInstance().getApis().editComment(getHeader(), id, edtComment);
//
//        mCallResetTest.enqueue(new ResponseHandler<SuccessBean>() {
//            @Override
//            public void onSuccess(SuccessBean successBean) {
//
//                if (successBean != null) {
//                    if (successBean.getStatus() == Constants.SUCCESS_CODE) {
//                        showToast(successBean.getMessage());
//                        getCommentApi(String.valueOf(videoId));
//                        binding.elEditComment.setVisibility(View.GONE);
//                        binding.editComment.setText("");
//                        binding.commentSub.getText().clear();
//                        binding.commentSub.clearFocus();
//                        InputMethodManager imm5 = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
//                        imm5.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
//
//                    }
//                }
//            }
//
//            @Override
//            public void apiError(ApiErrorBean t) {
//
//                if (t != null) showToast(t.getMessage());
//            }
//
//            @Override
//            public void onComplete(Call<SuccessBean> call, Throwable t) {
//
//                onCallComplete(call, t);
//            }
//
//            @Override
//            public void statusCode(int t) {
//                super.statusCode(t);
//                handleCodes(t);
//            }
//        });
//    }

    public void callEditCommentApi(String id,String comment) {
        mCallResetTest = AppController.getInstance().getApis().editComment(getHeader(),id,comment);
        mCallResetTest.enqueue(new ResponseHandler<SuccessBean>() {
            @Override
            public void onSuccess(SuccessBean successBean) {
                if (successBean != null) {
                    if (successBean.getStatus() == Constants.SUCCESS_CODE) {
                        showToast(successBean.getMessage());
                        getCommentApi(String.valueOf(videoId));
                    }
                }
            }

            @Override
            public void apiError(ApiErrorBean t) {

                if (t != null) showToast(t.getMessage());
            }

            @Override
            public void onComplete(Call<SuccessBean> call, Throwable t) {

                onCallComplete(call, t);
            }

            @Override
            public void statusCode(int t) {
                super.statusCode(t);
                handleCodes(t);
            }
        });
    }
    public void showDlt(String id) {
        cmntId = id;
        binding.rlDltCmt.setVisibility(View.VISIBLE);
    }


    //Edit the sub comment of subject video
    public void showEdt(String id, String comment) {
        cmntId = id;
        //binding.elEditComment.setVisibility(View.VISIBLE);
        //binding.editComment.setText(comment);
        editTheSubCommentOfVideo(cmntId,comment);
    }
    private void editTheSubCommentOfVideo(String cmntId, String comment) {
        AlertDialog alertDialog=new AlertDialog.Builder(PlayVideoActivity.this).create();
        View view= LayoutInflater.from(getApplicationContext()).inflate(R.layout.add_comment_alertdilog_box,null,false);
        alertDialog.setCancelable(false);
        alertDialog.setView(view);
        alertDialog.show();
        Button cancelBtn,submitBtn;
        EditText etComment;
        etComment=view.findViewById(R.id.comment);
        cancelBtn=view.findViewById(R.id.cancleDlg);
        submitBtn=view.findViewById(R.id.addCmnt);
        etComment.setText(comment);

        cancelBtn.setOnClickListener(v->{
            if(etComment.isFocused()){
                hideKeyboard();
            }
            alertDialog.dismiss();
        });
        submitBtn.setOnClickListener(v->{
            String cmt=etComment.getText().toString().trim();
            if(cmt.equalsIgnoreCase("")){
                showToast("Please type the comment..");}
            else {
                callEditCommentApi(cmntId, cmt);
                hideKeyboard();
                alertDialog.dismiss();
            }
        });

    }

    ////////////////////////////////////////////////////////////////////////////////////////

    private VdoPlayer.PlaybackEventListener playbackListener = new VdoPlayer.PlaybackEventListener() {
        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            if (playbackState == VdoPlayer.STATE_READY) {

                if (!isPlaying()) {
                    //updateVideoStatusInBean(Constants.CONTENT_STATUS[1]);
                    updateVideoStatusToServer(Constants.CONTENT_STATUS[1]);
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
                binding.textEmail.setText(PrefUtils.getInstance().getUser().getEmail());
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
            updateVideoStatusToServer(Constants.CONTENT_STATUS[2]);
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
//                    showSystemUi(false);
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

    private void getPosterImage() {
        mCallPosterImage = getApiInstance().getPosterImage(mVideoBean.getVideocypherid());
        mCallPosterImage.enqueue(new ResponseHandler<PostImageBean>() {
            @Override
            public void onSuccess(PostImageBean postImageBean) {
                if (postImageBean.getTitle() != null) {
                    if (postImageBean.getPosters().size() > 0) {
                        Picasso.with(PlayVideoActivity.this)
                                .load(postImageBean.getPosters().get(0).getUrl())
                                .into(binding.imagePoster);
                    } else {
                        binding.rlPoster.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void apiError(ApiErrorBean t) {

            }

            @Override
            public void onComplete(Call<PostImageBean> call, Throwable t) {

            }
        });
    }

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

    public static PDFViewActivity getInstance() {
        return mInstance;
    }

    public List<NoteBean> getNotesList() {
        return mNotesList;
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
                if (UtilMethods.getSavedPdfs(PlayVideoActivity.this).contains(getBaseDirectoryPath() + mVideoBean.getTitle() + mVideoBean.getId())) {
                    File file = new File(getBaseDirectoryPath() + mVideoBean.getTitle() + mVideoBean.getId());
                    if (file.exists()) {
                        prepareAllThings(file.getAbsolutePath());
                    }
                } else {
                    showBaseProgress();
                    AsyncLoadPdf runner = new AsyncLoadPdf(PlayVideoActivity.this, PlayVideoActivity.this, mVideoBean.getTitle() + mVideoBean.getId());
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
//        for (int x = 0; x < mPdfRenderer.getPageCount(); x++) {
//            mNotesList.add(new NoteBean(getImage(x)));
//        }

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

    private void openRenderer(String absolutePath) throws IOException {
        File file = new File(absolutePath);
        mFileDescriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY);
        if (mFileDescriptor != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mPdfRenderer = new PdfRenderer(mFileDescriptor);
            }
        }
    }

    private void goToDisplayEachPageScreen(int pos) {
        Bundle bundle = new Bundle();
        bundle.putInt("position", pos);
        startActivity(new Intent(this, PDFViewActivityWithViewPager.class).putExtras(bundle));
        goNextAnimation();
    }

    private void closeRenderer() throws IOException {
        if (null != mCurrentPage)
            mCurrentPage.close();

        if (null != mPdfRenderer)
            mPdfRenderer.close();

        if (null != mFileDescriptor)
            mFileDescriptor.close();
    }
//    private Bitmap getImage(int position) {
//
//        if (null != mCurrentPage) {
//            mCurrentPage.close();
//        }
//
//        mCurrentPage = mPdfRenderer.openPage(position);
//
//        Bitmap bitmap = Bitmap.createBitmap(
//                getResources().getDisplayMetrics().densityDpi * mCurrentPage.getWidth() / 150,
//                getResources().getDisplayMetrics().densityDpi * mCurrentPage.getHeight() / 150,
//                Bitmap.Config.ARGB_8888
//        );
//
//        mCurrentPage.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
//        return bitmap;
//    }





    class RetrivePDFfromUrl extends AsyncTask<String, Void, InputStream> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            binding.mProgress.setVisibility(View.VISIBLE);
        }

        @Override
        protected InputStream doInBackground(String... strings) {
            InputStream inputStream = null;
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
                if (urlConnection.getResponseCode() == 200) {
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());
                }

            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            return inputStream;
        }

        @Override
        protected void onPostExecute(InputStream inputStream) {
            binding.pdfVideo.fromStream(inputStream).load();
            binding.mProgress.setVisibility(View.GONE);
        }
    }

    class RetrivePDFfromUrl2 extends AsyncTask<String, Void, InputStream> {
        @Override
        protected InputStream doInBackground(String... strings) {
            InputStream inputStream = null;
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
                if (urlConnection.getResponseCode() == 200) {
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());
                }

            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            return inputStream;
        }

        @Override
        protected void onPostExecute(InputStream inputStream) {
            binding.pdfVideo2.fromStream(inputStream).load();
        }
    }


    //Show watermark for 4 seconds and hide for 30 seconds notes
    //Show
    private void startTimerVisible() {
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            Random r = new Random();
            int top = r.nextInt(480 - 10) + 10;
            int right = r.nextInt(200 - 10) + 10;
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(right, top, 0, 0);
            binding.pvNid.setLayoutParams(params);
            if (!isVideoStop) {
                binding.pvNid.setVisibility(View.VISIBLE);
            }
            startTimerHide();
        }, 30000);
    }
    private void startTimerHide() {

        Handler handler = new Handler();
        handler.postDelayed(() -> {
            binding.pvNid.setVisibility(View.GONE);
            startTimerVisible();
        }, 4000);
    }






    private void callVimeoAPIRequest2(String s) {
        String url = videoVimeoLink;
        Log.d("JHGHGGHK", url);
        String strNew = url.replaceFirst("https://vimeo.com/", "");
        String stringId = strNew.replaceFirst("manage/videos/", "");
        String id = stringId;
        VIMDEO_ID = id;
        VimeoInterface vimeoInterface = VimeoClientAPI.getClient().create(VimeoInterface.class);
        vimeoInterface.getVimeoUrlResponse(VIMDEO_ID).enqueue(new Callback<VimeoResponse>() {
            @Override
            public void onResponse(@NotNull Call<VimeoResponse> call, @NotNull Response<VimeoResponse> response) {
                if (response.isSuccessful() && response.body() != null) {

                    for (int i = 0; i < response.body().getRequest().getFiles().getProgressive().size(); i++) {
                        if (response.body().getRequest().getFiles().getProgressive().get(i).getQuality().equalsIgnoreCase(s)) {
                            if (response.body().getRequest().getFiles().getProgressive().size() > 0)
                                createMediaItem(response.body().getRequest().getFiles().getProgressive().get(i).getUrl());


                            MediaPlayer mp = new MediaPlayer();
                            try {
                                mp.reset();
                                mp.setDataSource(response.body().getRequest().getFiles().getProgressive().get(i).getUrl());
                                mp.prepare();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            String time = millisecondToTime(mp.getDuration());
                            pvDownloadLink = response.body().getRequest().getFiles().getProgressive().get(i).getUrl();

                            if (pvDownloadLink.equalsIgnoreCase("")) {
                                Toast.makeText(PlayVideoActivity.this, "Video link not available", Toast.LENGTH_SHORT).show();
                            } else {

                                new DownloadFileTask(PlayVideoActivity.this, progress_bar, text_percent, linear_download, text_download, binding.relativeProgress, binding.imageDownload, new OnDownloadListner() {
                                    @Override
                                    public void downloadVideo(String status) {
                                        if (status.equals("yes")) {
                                            Intent background = new Intent(PlayVideoActivity.this, DownloadVideo.class);
                                            startService(background);
                                            downloadVideoStatusUpdate(mVideoBean, "downlaod");
                                        }
                                    }
                                }
                                ).execute(pvDownloadLink);
                                binding.linearDownload.setVisibility(View.GONE);
                                binding.relativeProgress.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                } else {
                    Timber.d(response.message());
                }
            }

            @Override
            public void onFailure(@NotNull Call<VimeoResponse> call, @NotNull Throwable t) {
                Timber.e(Objects.requireNonNull(t.getMessage()));

            }
        });
    }

    private void downloadVideoStatusUpdate(VideoBean bean, String type) {
        showBaseProgress();
        HashMap<String, String> map = new HashMap<>();
        map.put("id", String.valueOf(bean.getId()));
        map.put("type", "videos");

        Call<SuccessBean> mCallMarkDownload = AppController.getInstance().getApis().markDownload(getHeader(), map);
        mCallMarkDownload.enqueue(new ResponseHandler<SuccessBean>() {
            @Override
            public void onSuccess(SuccessBean successBean) {
                if (successBean != null) {
                    if (successBean.getStatus() == Constants.SUCCESS_CODE) {
                        if (type.equals("downlaod")) {
                            mVideoBean.setIsDownloaded("yes");
                        } else {
                            mVideoBean.setIsDownloaded("no");
                        }
                    }
                }
            }

            @Override
            public void apiError(ApiErrorBean t) {
                hideBaseProgress();
                if (t != null) showErrorToast(t.getMessage());
            }

            @Override
            public void onComplete(Call<SuccessBean> call, Throwable t) {

                onCallComplete(call, t);
            }

            @Override
            public void statusCode(int t) {
                super.statusCode(t);
                handleCodes(t);
            }
        });
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //    //Sliding Images
    public void initView1(VideoBean mVideoBean) {
        setBaseCallback(baseCallback);

        mSlidesAdapter = new SimpleRecyclerViewAdapter<>(R.layout.item_image_slides, BR.bean, (v, slidesBean) -> {
//            if (binding.rlSlide.getVisibility() != View.VISIBLE) {
            //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            showSlideImage(slidesBean.getFile(), mVideoBean);
//            }
        });

        binding.recyclerSlides.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerSlides.setAdapter(mSlidesAdapter);


        getData1(mVideoBean);
    }

    private int getItem(int i) {
        return binding.viewPagerMain.getCurrentItem() + i;
    }

    private int getItem2(int i2) {
        return binding.viewPagerMain.getCurrentItem() - i2;
    }

    private void showSlideImage(String file, VideoBean mVideoBean) {

        binding.rlSlide.setVisibility(View.VISIBLE);
        open = true;
        for (int i = 0; i < mVideoBean.getSlides().size(); i++) {
            if (file.equalsIgnoreCase(mVideoBean.getSlides().get(i).getFile())) {
                poss = i;
            }
        }
        callAdapter(mVideoBean.getSlides(), file);
    }

    private void callAdapter(List<SlidesBean> slides, String file) {
        slideImageAdapter = new SlideImageAdapter(slides, PlayVideoActivity.this, file);
        binding.viewPagerMain.setAdapter(slideImageAdapter);
        binding.viewPagerMain.setCurrentItem(poss);
    }

    private void getData1(VideoBean mVideoBean) {
        if (this.mVideoBean.getSlides() != null && this.mVideoBean.getSlides().size() > 0){
            mSlidesAdapter.setList(this.mVideoBean.getSlides());
            binding.linearEmptyView.setVisibility(View.GONE);
        } else {
            binding.linearEmptyView.setVisibility(View.VISIBLE);
        }
    }

    //Full Slide View
    public void initView2(VideoBean mVideoBean) {
        setBaseCallback(baseCallback);

        mSlidesAdapter = new SimpleRecyclerViewAdapter<>(R.layout.item_image_slides, BR.bean, (v, slidesBean) -> {
//            if (binding.rlSlide.getVisibility() != View.VISIBLE) {
            //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            showSlideImage21(slidesBean.getFile(), mVideoBean);
//            }
        });

        binding.recyclerSlides1.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerSlides1.setAdapter(mSlidesAdapter);


        getData11(mVideoBean);
    }

    private int getItem101(int i) {
        return binding.viewPagerMain1.getCurrentItem() + i;
    }

    private int getItem102(int i2) {
        return binding.viewPagerMain1.getCurrentItem() - i2;
    }

    private void showSlideImage21(String file, VideoBean mVideoBean) {

        binding.rlSlide1.setVisibility(View.VISIBLE);
        open = true;
        for (int i = 0; i < mVideoBean.getSlides().size(); i++) {
            if (file.equalsIgnoreCase(mVideoBean.getSlides().get(i).getFile())) {
                poss = i;
            }
        }
        callAdapter1(mVideoBean.getSlides(), file);
    }

    private void callAdapter1(List<SlidesBean> slides, String file) {
        slideImageAdapter = new SlideImageAdapter(slides, PlayVideoActivity.this, file);
        binding.viewPagerMain1.setAdapter(slideImageAdapter);
        binding.viewPagerMain1.setCurrentItem(poss);
    }

    private void getData11(VideoBean mVideoBean) {
        if (this.mVideoBean.getSlides() != null && this.mVideoBean.getSlides().size() > 0){
            mSlidesAdapter.setList(this.mVideoBean.getSlides());
            binding.linearEmptyView21.setVisibility(View.GONE);
        } else {
            binding.linearEmptyView21.setVisibility(View.VISIBLE);
        }
    }

}