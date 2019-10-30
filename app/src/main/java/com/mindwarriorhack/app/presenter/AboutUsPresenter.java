package com.mindwarriorhack.app.presenter;

import android.content.Context;

import com.google.gson.JsonElement;
import com.mindwarriorhack.app.helper.Constant;
import com.mindwarriorhack.app.services.ApiClient;
import com.mindwarriorhack.app.view.fragments.AboutUsFragment.AboutUsView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AboutUsPresenter {
    Context context;
    private AboutUsView aboutUsView;
    private ApiClient apiClient;

    public AboutUsPresenter(AboutUsView aboutUsView, Context context) {
        this.aboutUsView = aboutUsView;
        this.context = context;
        if (this.apiClient == null) {
            this.apiClient = new ApiClient();
        }

    }

    public void aboutUs() {

            apiClient
                    .getApi()
                    .aboutUs()
                    .enqueue(new Callback<JsonElement>() {
                        @Override
                        public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                            if (response.body() != null) {
                                int code = response.body().getAsJsonObject().get("responseCode").getAsInt();
                                if (code == 0) {
                                    String successMsg = response.body().getAsJsonObject().get("responseMsg").getAsString();
                                    aboutUsView.onSuccess(response);
                                } else if (code == 1) {
                                    String errorMsg = response.body().getAsJsonObject().get("responseMsg").getAsString();
                                    aboutUsView.onFailure(errorMsg);
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<JsonElement> call, Throwable t) {
                            aboutUsView.onFailure(t.toString());
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