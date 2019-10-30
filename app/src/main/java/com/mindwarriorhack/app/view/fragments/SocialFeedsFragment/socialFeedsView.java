package com.mindwarriorhack.app.view.fragments.SocialFeedsFragment;

import com.google.gson.JsonElement;
import com.mindwarriorhack.app.model.SocialFeeds.SocialFeedResponse.SocialFeedResponse;

import retrofit2.Response;

public interface socialFeedsView {

    void onGetFeedsSuccess(Response<SocialFeedResponse> response);
    void onGetFeedsFailure(String error);
    void onFeedLikeSuccess(Response<JsonElement> response);
    void onFeedLikeFailure(String error);
    void onReportFeedSuccess(Response<JsonElement> response);
    void onReportFeedFailure(String error);

}
