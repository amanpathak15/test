package com.mindwarriorhack.app.view.FeedLikeScreen;

import com.google.gson.JsonElement;
import com.mindwarriorhack.app.model.SocialFeeds.Likes.FeedLikeResponse;

import retrofit2.Response;

public interface feedLikeView {

    void getAllFeedLikeSuccess(Response<FeedLikeResponse> response);
    void getAllFeedLikeFailure(String msg);

}
