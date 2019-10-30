package com.mindwarriorhack.app.presenter;

import android.content.Context;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mindwarriorhack.app.helper.Constant;
import com.mindwarriorhack.app.services.ApiClient;
import com.mindwarriorhack.app.view.fragments.AboutUsFragment.AboutUsView;
import com.mindwarriorhack.app.view.rateThisVideo.rateThisVideoView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class rateThisVideoPresenter {
    Context context;
    private rateThisVideoView rateThisVideoView;
    private ApiClient apiClient;

    public rateThisVideoPresenter(rateThisVideoView rateThisVideoView, Context context) {
        this.rateThisVideoView = rateThisVideoView;
        this.context = context;
        if (this.apiClient == null) {
            this.apiClient = new ApiClient();
        }

    }

    public void rateThisVideoApi(String userId, String videoId, int rating) {

        JsonObject object = new JsonObject();
        object.addProperty("userId", userId);
        object.addProperty("videoId", videoId);
        object.addProperty("rate", rating);

            apiClient
                    .getApi()
                    .rateVideo(object)
                    .enqueue(new Callback<JsonElement>() {
                        @Override
                        public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                            if (response.body() != null) {
                                int code = response.body().getAsJsonObject().get("responseCode").getAsInt();
                                if (code == 0) {
                                    String successMsg = response.body().getAsJsonObject().get("responseMsg").getAsString();
                                    rateThisVideoView.onRateSuccess(response);
                                } else if (code == 1) {
                                    String errorMsg = response.body().getAsJsonObject().get("responseMsg").getAsString();
                                    rateThisVideoView.onRateFailure(errorMsg);
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<JsonElement> call, Throwable t) {
                            rateThisVideoView.onRateFailure(t.toString());
                        }
                    });
    }


    private int validateInputFields(String oldPassword, String newPassword) {
        if (oldPassword == null || oldPassword.length() < 6) {
            return Constant.EXISTING_PASSWORD_INVALID;
        } else if (newPassword == null || newPassword.length() < 6) {
            return Constant.EXISTING_PASSWORD_INVALID;
        }
        return 0;
    }

}