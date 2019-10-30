package com.mindwarriorhack.app.presenter;

import android.content.Context;

import com.google.gson.JsonObject;
import com.mindwarriorhack.app.helper.Constant;
import com.mindwarriorhack.app.manager.PreferenceManager;
import com.mindwarriorhack.app.model.SocialFeeds.Comment.FeedCommentResponse;
import com.mindwarriorhack.app.model.SocialFeeds.Likes.FeedLikeResponse;
import com.mindwarriorhack.app.services.ApiClient;
import com.mindwarriorhack.app.view.FeedCommentScreen.feedCommentView;
import com.mindwarriorhack.app.view.FeedLikeScreen.feedLikeView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class feedCommentPresenter {

    private Context context;
    private ApiClient apiClient;
    private feedCommentView feedCommentView;


    public feedCommentPresenter(Context context, feedCommentView feedCommentView) {
        this.context = context;
        this.feedCommentView = feedCommentView;
        if (this.apiClient == null){
            this.apiClient = new ApiClient();
        }
    }


    public void getAllComments(int postId) {

        JsonObject object = new JsonObject();
        object.addProperty("postId", postId);

        apiClient
                .getApi()
                .getAllComment(PreferenceManager.getString(Constant.FEED_COMMENT_PAGINATION_URL), object)
                .enqueue(new Callback<FeedCommentResponse>() {
                    @Override
                    public void onResponse(Call<FeedCommentResponse> call, Response<FeedCommentResponse> response) {
                        if(response.body().getResponseCode() == 0) {
                            feedCommentView.getAllCommentSuccess(response);
                        }
                        else
                        {
                            PreferenceManager.setString(Constant.IS_FEED_COMMENT_PAGINATION_PROCESSING,"false");
                        }
                    }

                    @Override
                    public void onFailure(Call<FeedCommentResponse> call, Throwable t) {
                        feedCommentView.getAllCommentFailure(t.toString());
                    }
                });
    }


    public void postComment(int postId, String comment) {

        JsonObject object = new JsonObject();
        object.addProperty("userId", PreferenceManager.getString(Constant.USER_ID));
        object.addProperty("postId", postId);
        object.addProperty("comment", comment);

        apiClient
                .getApi()
                .postComment(object)
                .enqueue(new Callback<FeedCommentResponse>() {
                    @Override
                    public void onResponse(Call<FeedCommentResponse> call, Response<FeedCommentResponse> response) {
                        if(response.body().getResponseCode() == 0) {
                            feedCommentView.postCommentSuccess(response);
                        }
                        else
                        {
                            feedCommentView.postCommentFailure(response.body().getResponseMsg());
                        }
                    }

                    @Override
                    public void onFailure(Call<FeedCommentResponse> call, Throwable t) {
                        feedCommentView.postCommentFailure(t.toString());
                    }
                });
    }





}
