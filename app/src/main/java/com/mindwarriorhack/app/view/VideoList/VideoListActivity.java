package com.mindwarriorhack.app.view.VideoList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import retrofit2.Response;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ligl.android.widget.iosdialog.IOSDialog;
import com.mindwarriorhack.app.R;
import com.mindwarriorhack.app.view.VimeoPlayer.VimeoPlayerActivity;
import com.mindwarriorhack.app.model.VideoLike.VideoLikeResponse;
import com.mindwarriorhack.app.model.VideoList.VideoListItem;
import com.mindwarriorhack.app.model.VideoList.VideoListResponse;
import com.mindwarriorhack.app.model.VideoWatched.VideoWatchedResponse;
import com.mindwarriorhack.app.presenter.VideoListPresenter;
import com.mindwarriorhack.app.view.adapters.VideoListAdapter;

import java.util.List;

public class VideoListActivity extends AppCompatActivity implements View.OnClickListener, VideoListView, VideoListAdapter.VideoAdapterCallback {

    private ImageView backVideoList;
    private VideoListPresenter presenter;
    private List<VideoListItem> videoList;
    private RecyclerView recyclerViewVideoList;
    private ImageView videoLiked;
    private VideoListAdapter adapter;
    private TextView toolbarTitle;
    private String title;
    private String videotitle, videoUrl;
    private boolean isSuccess = false;
    private ProgressBar progressBarVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_list);
        initUI();


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }

    }

    private void initUI() {
        presenter = new VideoListPresenter(this, this);
        String levelId = "";

        if (getIntent().hasExtra("LevelId")) {
            levelId = getIntent().getStringExtra("LevelId");
        }

        if (getIntent().hasExtra("Title")) {
            title = getIntent().getStringExtra("Title");
        }

        presenter.getVideoList(levelId);


        toolbarTitle = (TextView) findViewById(R.id.toolbar_text);
        toolbarTitle.setText(title);
        backVideoList = findViewById(R.id.backVideo);
        backVideoList.setClipToOutline(true);
        recyclerViewVideoList = findViewById(R.id.recyclerViewVideoList);
        recyclerViewVideoList.setLayoutManager(new LinearLayoutManager(this));
        backVideoList.setOnClickListener(this);
        videoLiked = findViewById(R.id.videoLiked);
        progressBarVideo=findViewById(R.id.progressBarVideo);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.backVideo) {
            finish();
        }
    }


    @Override
    public void onSuccess(Response<VideoListResponse> response) {
        progressBarVideo.setVisibility(View.GONE);
        if (response.body() != null && response.body().getResponseData().getLevelsList() != null) {
            videoList = response.body().getResponseData().getLevelsList();
            Log.d("videoslist", videoList + "");
            adapter = new VideoListAdapter(videoList, this, this);
            recyclerViewVideoList.setAdapter(adapter);
            setAnimation();
        }
    }

    @Override
    public void onFailure(String error) {
        progressBarVideo.setVisibility(View.GONE);
        customDialog("Videos Not Available");
    }

    @Override
    public void onSuccessLiked(Response<VideoLikeResponse> success, int like, int position) {
        if (like == 0) {
            VideoListItem videoItem = videoList.get(position);
            videoItem.setIsLiked("0");
            videoList.set(position, videoItem);
        } else {
            VideoListItem videoItem = videoList.get(position);
            videoItem.setIsLiked("1");
            videoList.set(position, videoItem);
        }

        adapter.notifyDataSetChanged();

    }

    @Override
    public void onVideoWatched(Response<VideoWatchedResponse> successMsg, int position, int watched) {
        /*if (watched == 0) {
            VideoListItem videoItem = videoList.get(position);
            videoItem.setIsWatched("0");
            videoList.set(position, videoItem);
        } else {
            VideoListItem videoItem = videoList.get(position);
            videoItem.setIsWatched("1");
            videoList.set(position, videoItem);
        }
        adapter.notifyDataSetChanged();*/


    }

    @Override
    public void onVideoWatchedSuccess(String msg) {
        Intent intent = new Intent(this, VimeoPlayerActivity.class);
        intent.putExtra("link", videoUrl);
        intent.putExtra("title", videotitle);
        startActivity(intent);
        //Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }



    @Override
    public void onVideoWatchedFailure(String errorMsg) {
        customDialog(errorMsg);
    }


    public void customDialog(String alert) {
        new IOSDialog.Builder(this)
                .setTitle("Error")
                .setMessage(alert)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }


    @Override
    public void onVideoLiked(String videoId, int like, int position) {
        presenter.getVideoLike(videoId, like, position);
    }

    @Override
    public void onVideoWatched(String videoId, int position, int isWatched) {
        presenter.videoWatched(videoId, position, isWatched);
    }

    @Override
    public void getVideoDetails(String title, String url) {
        videotitle = title;
        videoUrl = url;
    }

    private void setAnimation() {

        recyclerViewVideoList.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {

                    @Override
                    public boolean onPreDraw() {
                        recyclerViewVideoList.getViewTreeObserver().removeOnPreDrawListener(this);

                        for (int i = 0; i < recyclerViewVideoList.getChildCount(); i++) {
                            View v = recyclerViewVideoList.getChildAt(i);
                            v.setAlpha(0f);
                            v.animate().alpha(1f)
                                    .setDuration(500)
                            ;
                        }

                        return true;
                    }
                });

    }

}
