package com.mindwarriorhack.app.view.FeedLikeScreen;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.JsonElement;
import com.mindwarriorhack.app.R;
import com.mindwarriorhack.app.helper.Constant;
import com.mindwarriorhack.app.helper.Utils;
import com.mindwarriorhack.app.manager.PreferenceManager;
import com.mindwarriorhack.app.model.SocialFeeds.Likes.FeedLikeResponse;
import com.mindwarriorhack.app.model.SocialFeeds.Likes.LikesItem;
import com.mindwarriorhack.app.model.SocialFeeds.SocialFeedResponse.FeedsItem;
import com.mindwarriorhack.app.presenter.feedLikePresenter;
import com.mindwarriorhack.app.presenter.newPostPresenter;
import com.mindwarriorhack.app.view.Popups.uploadPicturePopup;
import com.mindwarriorhack.app.view.adapters.FeedLikeAdapter;
import com.mindwarriorhack.app.view.adapters.SocialFeedsAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;


public class feedLikeScreen extends AppCompatActivity implements View.OnClickListener, feedLikeView, FeedLikeAdapter.feedLikeInterface {

    private RecyclerView feedLikeRecyclerView;
    private feedLikePresenter presenter;
    int postId = -1;
    private FeedLikeAdapter adapter;
    private List<LikesItem> feedlikeList;
    private ImageView backButton;
    private TextView totalLikeCount;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_like_screen);
        initUI();
    }

    private void initUI(){

        if(getIntent().hasExtra("postId")){
            postId = getIntent().getIntExtra("postId",-1);
        }

        presenter = new feedLikePresenter(this,this);
        feedlikeList = new ArrayList<>();
        totalLikeCount = findViewById(R.id.likeCount);
        feedLikeRecyclerView = (RecyclerView) findViewById(R.id.feedLikeRecyclerView);
        feedLikeRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        PreferenceManager.setString(Constant.FEED_lIKE_PAGINATION_URL,"https://mindwarriorhacks.app/uat/api/Api/getAllLikes?page=1");
        presenter.getAllLikes(postId);
        backButton = (ImageView) findViewById(R.id.back);
        backButton.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.back:
                finish();
                break;
        }
    }


    @Override
    public void getAllFeedLikeSuccess(Response<FeedLikeResponse> response) {
            if (response.body().getResponseData().getNext() != null) {
                PreferenceManager.setString(Constant.FEED_lIKE_PAGINATION_URL, response.body().getResponseData().getNext());
            } else {
                PreferenceManager.setString(Constant.FEED_lIKE_PAGINATION_URL, "");
            }

            if (adapter != null) {

                if (PreferenceManager.getString(Constant.IS_FEED_LIKE_PAGINATION_PROCESSING).equals("true")) {
                    PreferenceManager.setString(Constant.IS_FEED_LIKE_PAGINATION_PROCESSING, "false");
                    if (response.body().getResponseCode() == 0) {
                        feedlikeList.addAll(response.body().getResponseData().getLikes());
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    feedlikeList.clear();
                    if (response.body().getResponseCode() == 0) {
                        feedlikeList.addAll(response.body().getResponseData().getLikes());
                    }
                    adapter.notifyDataSetChanged();
                }
            } else {

                feedlikeList.clear();
                feedlikeList.addAll(response.body().getResponseData().getLikes());
                adapter = new FeedLikeAdapter(this, feedlikeList, this);
                feedLikeRecyclerView.setAdapter(adapter);
                totalLikeCount.setText("Likes " + response.body().getResponseData().getCount() + "");
            }
        }

    @Override
    public void getAllFeedLikeFailure(String msg) {

    }

    @Override
    public void callFeedPaginationApi() {
        if(!PreferenceManager.getString(Constant.FEED_lIKE_PAGINATION_URL).isEmpty()) {

            PreferenceManager.setString(Constant.IS_FEED_LIKE_PAGINATION_PROCESSING,"true");
            presenter.getAllLikes(postId);
        }
    }
}
