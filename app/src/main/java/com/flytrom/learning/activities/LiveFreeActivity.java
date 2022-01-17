package com.flytrom.learning.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.Toast;
import android.widget.VideoView;

import com.flytrom.learning.R;
import com.flytrom.learning.model.vimeo.VimeoResponse;
import com.flytrom.learning.vimeo_video.VimeoClientAPI;
import com.flytrom.learning.vimeo_video.VimeoInterface;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.ui.StyledPlayerView;
import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.util.Util;
import com.vimeo.networking.Configuration;
import com.vimeo.networking.Vimeo;
import com.vimeo.networking.VimeoClient;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Response;

public class LiveFreeActivity extends AppCompatActivity implements View.OnClickListener,
        MediaPlayer.OnPreparedListener , SimpleExoPlayer.EventListener{
    private static final String TAG = LiveFreeActivity.class.getName();

    /**
     * You have to sign up as developer with vimeo developer dashboard <a href="https://developer.vimeo.com/">the developer console</a>
     * <p>
     * VIMEO_ACCESS_TOKEN you can it from <a href="https://developer.vimeo.com/apps/192457#generate_access_token">the developer console</a>
     */
    private static final String VIMEO_ACCESS_TOKEN = "2ac01d5598827dcd854d8acb40627b17";

    private static final String VIMDEO_ID = "562715241";

    private PlayerView playerView;
    private SimpleExoPlayer player;
    VideoView videoView;
    //Release references
    private boolean playWhenReady = false; //If true the player auto play the media
    private int currentWindow = 0;
    private long playbackPosition = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_free);
        playerView = findViewById(R.id.video_view);
//        videov = findViewById(R.id.videov);
        Button playBtn = findViewById(R.id.button2);

        playBtn.setOnClickListener(this);

        //Build vimeo configuration
        configVimeoClient();

//        player.addListener(new Player.Listener() {
//            @Override
//            public void onPlayerError(PlaybackException error) {
//                switch (error.errorCode) {
//                    case ExoPlaybackException.TYPE_SOURCE:
//                        Log.e(TAG, "TYPE_SOURCE: " + error.getMessage());
//                        //Restart the playback
//                        break;
//
//                    case ExoPlaybackException.TYPE_RENDERER:
//                        Log.e(TAG, "TYPE_RENDERER: " + error.getMessage());
//                        break;
//
//                    case ExoPlaybackException.TYPE_UNEXPECTED:
//                        Log.e(TAG, "TYPE_UNEXPECTED: " + error.getMessage());
//                        break;
//                }
//            }
//        });


    }

    private void configVimeoClient() {
        Configuration.Builder configBuilder =
                new Configuration.Builder(LiveFreeActivity.VIMEO_ACCESS_TOKEN) //Pass app access token
                        .setCacheDirectory(this.getCacheDir());
        VimeoClient.initialize(configBuilder.build());
    }

    private void createMediaItem(String url) {
        MediaItem mediaItem = MediaItem.fromUri(url);
        player.setMediaItem(mediaItem);
    }
    private void initializePlayer() {
        player = new SimpleExoPlayer.Builder(this).build();
        playerView.setPlayer(player);
        callVimeoAPIRequest();
        player.setPlayWhenReady(playWhenReady);
        player.seekTo(currentWindow, playbackPosition);
        player.prepare();
        if (player.isPlaying() == true){

        }

    }

    private void callVimeoAPIRequest() {
        VimeoInterface vimeoInterface = VimeoClientAPI.getClient().create(VimeoInterface.class);
        vimeoInterface.getVimeoUrlResponse(VIMDEO_ID).enqueue(new retrofit2.Callback<VimeoResponse>() {
                    @Override
                    public void onResponse(@NotNull Call<VimeoResponse> call, @NotNull Response<VimeoResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {

                            //Create media item
                            if (response.body().getRequest().getFiles().getProgressive().size() > 0)
                                createMediaItem(response.body().getRequest().getFiles().getProgressive().get(0).getUrl());

                            Log.d(TAG, response.body().getRequest().getFiles().getProgressive().get(0).getUrl());

                            MediaPlayer mp = new MediaPlayer();
                            try {
                                mp.reset();
                                mp.setDataSource(response.body().getRequest().getFiles().getProgressive().get(0).getUrl());
                                mp.prepare();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            String time = millisecondToTime(mp.getDuration());
                            Log.d("GHCK",time);
                        }else {
                            Log.d("DSJDSBJ",response.message());
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<VimeoResponse> call, @NotNull Throwable t) {
                        Log.e(TAG, Objects.requireNonNull(t.getMessage()));

                    }
                });
    }

    public String millisecondToTime(long duration) {
        String finalTimeString = "";
        String secondTime ;

        int hour = (int) (duration / (1000*60*60));
        int minuets = (int) (duration % (1000*60*60) / (1000*60) );
        int seconds = (int) (duration % (1000*60*60) % (1000*60) / 1000);

        if (hour > 0){
            finalTimeString = hour + ":";
        }

        if (seconds < 10){
            secondTime = "0" +seconds;
        }else {
            secondTime = "" +seconds;
        }
        finalTimeString = finalTimeString + minuets +":"+secondTime;

        return finalTimeString;
    }


    @SuppressLint("InlinedApi")
    private void hideSystemUi() {
        playerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }


    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT >= 24) {
            //Init exoplayer builder
            initializePlayer();
        }
    }

    /**
     * Android API level 24 and higher supports multiple windows.
     * As your app can be visible, but not active in split window mode, you need to initialize the player in onStart.
     * Android API level 24 and lower requires you to wait as long as possible until you grab resources,
     * so you wait until onResume before initializing the player.
     */
    @Override
    public void onResume() {
        super.onResume();
        //Helper method which allows you to have a full-screen experience.
        hideSystemUi();
        if ((Util.SDK_INT < 24 || player == null)) {
            //Init exoplayer builder
            initializePlayer();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT < 24) {
            //Frees the player's resources and destroys it.
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT >= 24) {
            //Frees the player's resources and destroys it.
            releasePlayer();
        }
    }

    private void releasePlayer() {
        if (player != null) {
            playWhenReady = player.getPlayWhenReady();
            playbackPosition = player.getCurrentPosition();
            currentWindow = player.getCurrentWindowIndex();
            player.release();
            player = null;
        }
    }


    @Override
    public void onClick(View v) {
        player.pause();

    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        videoView.start();
    }
}