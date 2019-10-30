package com.mindwarriorhack.app.view.VimeoPlayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.mindwarriorhack.app.R;
import com.mindwarriorhack.app.model.VideoLike.VideoLikeResponse;
import com.mindwarriorhack.app.model.VideoList.VideoListResponse;
import com.mindwarriorhack.app.model.VideoWatched.VideoWatchedResponse;
import com.mindwarriorhack.app.presenter.VimeoPlayerPresenter;
import com.mindwarriorhack.app.view.rateThisVideo.rateThisVideoActivity;

import retrofit2.Response;


public class VimeoPlayerActivity extends AppCompatActivity implements VimeoPlayerView{

    private VideoView videoView;
    private ImageView backButton;
    private TextView titleView;
    private String title;
    private String videoUrl, videoId, videoThumbnail;
    private int videoCallbackTime = 0;
    private boolean isPlaying=false;
    private RelativeLayout toolbarVideoList;
    private boolean isVideoCompleted = false;
    private VimeoPlayerPresenter presenter;
    private boolean isUserRated = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vimeo_player);
        presenter = new VimeoPlayerPresenter(this,this);

        if (getIntent().hasExtra("link")) {
            videoUrl = getIntent().getStringExtra("link");
        }

        if (getIntent().hasExtra("title")) {
            title = getIntent().getStringExtra("title");
        }

        if(getIntent().hasExtra("imageThumbnail")){
            videoThumbnail = getIntent().getStringExtra("imageThumbnail");
        }


        if(getIntent().hasExtra("videoId")){
            videoId = getIntent().getStringExtra("videoId");
        }

        if(getIntent().hasExtra("isUserRated")){
            isUserRated = getIntent().getBooleanExtra("isUserRated",false);
        }

        presenter.videoWatched(videoId, 1);
        backButton = (ImageView) findViewById(R.id.backVideo);
        toolbarVideoList=findViewById(R.id.toolbarVideoList);


        final int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            backButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
                    finish();
                }
            });


        } else {

            titleView = (TextView) findViewById(R.id.toolbar_text);
            titleView.setText(title);
            backButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }


        videoView = (VideoView) findViewById(R.id.videoView);

        Uri uri = Uri.parse(videoUrl);
        videoView.setVideoURI(uri);
        final MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);
        mediaController.setMediaPlayer(videoView);
        mediaController.requestFocus();
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                if (mediaController.isShowing()) {
                    mediaController.hide();
                }
                videoView.start();
            }

        });

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {

                if(!isUserRated){

                    Intent intent = new Intent(VimeoPlayerActivity.this, rateThisVideoActivity.class);
                    intent.putExtra("imageThumbnail",videoThumbnail);
                    intent.putExtra("videoName", title);
                    intent.putExtra("videoId", videoId);
                    startActivity(intent);
                    finish();
                }
            }
        });

        videoView.start();

    }


    @Override
    protected void onPause() {
        super.onPause();
        videoCallbackTime = videoView.getCurrentPosition();
        videoView.stopPlayback();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        if (videoView != null)
            outState.putInt("videoPlaybackTime", videoCallbackTime);
        super.onSaveInstanceState(outState);

    }


    @Override
    protected void onRestoreInstanceState(final Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState.containsKey("videoPlaybackTime")) {
            MediaPlayer.OnPreparedListener onPreparedListener = new MediaPlayer.OnPreparedListener() {
                public void onPrepared(MediaPlayer mp) {
                    mp.seekTo(savedInstanceState.getInt("videoPlaybackTime"));
                    videoView.start();
                }
            };
            videoView.setOnPreparedListener(onPreparedListener);
        }
    }


    @Override
    public void onVideoWatchedSuccess(String msg) {

    }

    @Override
    public void onVideoWatchedFailure(String errorMsg) {

    }
}
