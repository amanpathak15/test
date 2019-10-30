package com.mindwarriorhack.app.view.newPostScreen;

import com.google.gson.JsonElement;

import retrofit2.Response;

public interface newPostView {

    void onPostFeedSuccess(Response<JsonElement> response);
    void onPostFeedFailure(String msg, boolean isUserBlocked);

}
