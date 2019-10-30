package com.mindwarriorhack.app.view.fragments.AboutUsFragment;

import com.google.gson.JsonElement;

import retrofit2.Response;

public interface AboutUsView {
    void onSuccess(Response<JsonElement> response);
    void onFailure(String error);
}
