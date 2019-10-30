package com.mindwarriorhack.app.view.rateThisVideo;

import com.google.gson.JsonElement;

import retrofit2.Response;

public interface rateThisVideoView {
    void onRateSuccess(Response<JsonElement> response);
    void onRateFailure(String errorMsg);

}
