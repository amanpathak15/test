package com.mindwarriorhack.app.view.FeedCommentScreen;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ligl.android.widget.iosdialog.IOSDialog;
import com.mindwarriorhack.app.R;
import com.mindwarriorhack.app.helper.Constant;
import com.mindwarriorhack.app.helper.Utils;
import com.mindwarriorhack.app.manager.PreferenceManager;
import com.mindwarriorhack.app.model.SocialFeeds.Comment.CommentsItem;
import com.mindwarriorhack.app.model.SocialFeeds.Comment.FeedCommentResponse;
import com.mindwarriorhack.app.model.SocialFeeds.Likes.FeedLikeResponse;
import com.mindwarriorhack.app.model.SocialFeeds.Likes.LikesItem;
import com.mindwarriorhack.app.presenter.feedCommentPresenter;
import com.mindwarriorhack.app.presenter.feedLikePresenter;
import com.mindwarriorhack.app.view.adapters.FeedCommentAdapter;
import com.mindwarriorhack.app.view.adapters.FeedLikeAdapter;
import com.mindwarriorhack.app.view.newPostScreen.newPostScreen;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;


public class feedCommentScreen extends AppCompatActivity implements View.OnClickListener, feedCommentView, FeedCommentAdapter.feedCommentInterface {

    private RecyclerView feedLikeRecyclerView;
    private feedCommentPresenter presenter;
    int postId, feedPosition = -1;
    String userName;
    private FeedCommentAdapter adapter;
    private List<CommentsItem> feedCommentList;
    private ImageView backButton, postCommentButton, profilePic;
    private TextView totalCommentCount,toolbarText;
    private EditText inputComment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_comment_screen);
        initUI();
    }


    private void initUI(){
        if(getIntent().hasExtra("postId")){
            postId = getIntent().getIntExtra("postId",-1);
        }

        if(getIntent().hasExtra("feedPosition")){
            feedPosition = getIntent().getIntExtra("feedPosition", -1);
        }

        if(getIntent().hasExtra("userName")){
            userName= getIntent().getStringExtra("userName");
        }

        presenter = new feedCommentPresenter(this,this);
        feedCommentList = new ArrayList<>();
        profilePic = findViewById(R.id.profilePic);
        if(!PreferenceManager.getString(Constant.PROFILE_PIC).isEmpty()){
            Glide.with(this).load(PreferenceManager.getString(Constant.PROFILE_PIC)).apply(RequestOptions.circleCropTransform()).into(profilePic);
        }
        else
        {
            Glide.with(this).load(R.drawable.default_profilepic).apply(RequestOptions.circleCropTransform()).into(profilePic);
        }
        toolbarText=findViewById(R.id.toolbarCommentScreenText);
        toolbarText.setText(userName + " Post");
        inputComment = findViewById(R.id.enterComment);
        totalCommentCount = findViewById(R.id.commentCount);
        feedLikeRecyclerView = (RecyclerView) findViewById(R.id.feedLikeRecyclerView);
        feedLikeRecyclerView.setNestedScrollingEnabled(false);
        feedLikeRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        PreferenceManager.setString(Constant.FEED_COMMENT_PAGINATION_URL,"https://mindwarriorhacks.app/uat/api/Api/getAllComments?page=1");
        presenter.getAllComments(postId);
        backButton = (ImageView) findViewById(R.id.back);
        backButton.setOnClickListener(this);
        postCommentButton = findViewById(R.id.postCommentButton);
        postCommentButton.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.back:
                finish();
                break;

            case R.id.postCommentButton:
                presenter.postComment(postId,inputComment.getText().toString());
                inputComment.setText("");
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(postCommentButton.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
                break;
        }

    }


    @Override
    public void getAllCommentSuccess(Response<FeedCommentResponse> response) {
        assert response.body() != null;
        if (response.body().getResponseData().getNext() != null) {
                PreferenceManager.setString(Constant.FEED_COMMENT_PAGINATION_URL, response.body().getResponseData().getNext());
            } else {
                PreferenceManager.setString(Constant.FEED_COMMENT_PAGINATION_URL, "");
            }

            if (adapter != null) {
                if (PreferenceManager.getString(Constant.IS_FEED_COMMENT_PAGINATION_PROCESSING).equals("true")) {
                    PreferenceManager.setString(Constant.IS_FEED_COMMENT_PAGINATION_PROCESSING, "false");
                    if (response.body().getResponseCode() == 0) {
                        feedCommentList.addAll(response.body().getResponseData().getComments());
                        adapter.notifyDataSetChanged();
                        if(feedCommentList.size() > 0){
                            PreferenceManager.storePojo(feedCommentList.get(0),"latestComment");
                        }

                    }

                } else {
                    feedCommentList.clear();
                    if (response.body().getResponseCode() == 0) {
                        feedCommentList.addAll(response.body().getResponseData().getComments());
                        if(feedCommentList.size() > 0){
                            PreferenceManager.storePojo(feedCommentList.get(0),"latestComment");
                        }

                    }
                    adapter.notifyDataSetChanged();
                }
            } else {
                feedCommentList.clear();
                feedCommentList.addAll(response.body().getResponseData().getComments());
                adapter = new FeedCommentAdapter(this, feedCommentList, this);
                feedLikeRecyclerView.setAdapter(adapter);
                totalCommentCount.setText("Comments " + response.body().getResponseData().getCount() + "");
                if(feedCommentList.size() > 0){
                    PreferenceManager.storePojo(feedCommentList.get(0),"latestComment");
                }

            }
        }

    @Override
    public void getAllCommentFailure(String msg) {

    }

    @Override
    public void postCommentSuccess(Response<FeedCommentResponse> response) {
        PreferenceManager.setString(Constant.COMMENT_COUNT, String.valueOf(feedCommentList.size() + 1));
        PreferenceManager.setString(Constant.FEED_POSITION,feedPosition + "");

        totalCommentCount.setText("Comments " +Integer.parseInt(String.valueOf(feedCommentList.size() + 1)));
        PreferenceManager.setString(Constant.FEED_COMMENT_PAGINATION_URL,"https://mindwarriorhacks.app/uat/api/Api/getAllComments?page=1");
        presenter.getAllComments(postId);
    }

    @Override
    public void postCommentFailure(String msg) {

        if(Utils.internetConnectionAvailable(2000)){
            failureCustomDialog(msg);
        }
        else
        {
            Toast.makeText(feedCommentScreen.this,"Internet Connection unavailable.Please try after sometime",Toast.LENGTH_SHORT).show();
        }

    }


    public void failureCustomDialog(String alert) {


        new IOSDialog.Builder(feedCommentScreen.this)
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
    public void callFeedPaginationApi() {
        if(!PreferenceManager.getString(Constant.FEED_COMMENT_PAGINATION_URL).isEmpty()) {
            PreferenceManager.setString(Constant.IS_FEED_COMMENT_PAGINATION_PROCESSING,"true");
            presenter.getAllComments(postId);
        }
    }
}
