package com.mindwarriorhack.app.presenter;

import android.content.Context;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mindwarriorhack.app.helper.Constant;
import com.mindwarriorhack.app.manager.PreferenceManager;
import com.mindwarriorhack.app.model.SocialFeeds.Likes.FeedLikeResponse;
import com.mindwarriorhack.app.model.SocialFeeds.SocialFeedResponse.SocialFeedResponse;
import com.mindwarriorhack.app.services.ApiClient;
import com.mindwarriorhack.app.view.FeedLikeScreen.feedLikeView;
import com.mindwarriorhack.app.view.fragments.SocialFeedsFragment.socialFeedsView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class feedLikePresenter {

    private Context context;
    private ApiClient apiClient;
    private feedLikeView feedLikeView;
    private feedLikePresenter feedLikePresenter;


    public feedLikePresenter(Context context, feedLikeView feedLikeView) {
        this.context = context;
        this.feedLikeView = feedLikeView;
        if (this.apiClient == null){
            this.apiClient = new ApiClient();
        }
    }


    public void getAllLikes(int postId) {

        JsonObject object = new JsonObject();
        object.addProperty("postId", postId);


        apiClient
                .getApi()
                .getAllLikes(PreferenceManager.getString(Constant.FEED_lIKE_PAGINATION_URL), object)
                .enqueue(new Callback<FeedLikeResponse>() {
                    @Override
                    public void onResponse(Call<FeedLikeResponse> call, Response<FeedLikeResponse> response) {
                        if(response.body().getResponseCode() == 0) {
                            feedLikeView.getAllFeedLikeSuccess(response);
                        }
                    }

                    @Override
                    public void onFailure(Call<FeedLikeResponse> call, Throwable t) {
                        feedLikeView.getAllFeedLikeFailure(t.toString());
                    }
                });
    }

}
