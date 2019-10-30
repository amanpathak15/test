package com.mindwarriorhack.app.presenter;

import android.content.Context;
import android.content.Intent;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mindwarriorhack.app.R;
import com.mindwarriorhack.app.helper.Constant;
import com.mindwarriorhack.app.manager.PreferenceManager;
import com.mindwarriorhack.app.services.ApiClient;
import com.mindwarriorhack.app.view.ForgotPassword.ForgotPasswordView;
import com.mindwarriorhack.app.view.SignIn.SignIn;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordPresenter {
    private ForgotPasswordView forgotPasswordView;
    private ApiClient apiClient;
    private Context context;

    public ForgotPasswordPresenter(ForgotPasswordView forgotPasswordView, Context context) {
        this.forgotPasswordView = forgotPasswordView;
        this.context = context;
        if (this.apiClient == null) {
            this.apiClient = new ApiClient();
        }
    }


    public void forgotPassword(String email) {
        if (validateLoginInputFields(email) == Constant.EMAIL_INVALID) {
            forgotPasswordView.validateEmail("Please enter email", Constant.EMAIL_INVALID);
        } else {
            JsonObject forgotPasswordObject = new JsonObject();
            forgotPasswordObject.addProperty("email", email);

            apiClient
                    .getApi()
                    .forgotPassword(forgotPasswordObject)
                    .enqueue(new Callback<JsonElement>() {
                        @Override
                        public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                            if (response.body() != null) {
                                int code = response.body().getAsJsonObject().get("responseCode").getAsInt();
                                if (code == 0) {
                                    String successMsg = response.body().getAsJsonObject().get("responseMsg").getAsString();
                                    forgotPasswordView.onSuccess(successMsg);
                                } else if (code == 1) {
                                    String errorMsg = response.body().getAsJsonObject().get("responseMsg").getAsString();
                                    forgotPasswordView.onError(errorMsg);
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<JsonElement> call, Throwable t) {
                            forgotPasswordView.onError(t.toString());
                        }
                    });
        }
    }

    private int validateLoginInputFields(String email) {
        if (email == null || email.length() == 0 || !email.matches(context.getResources().getString(R.string.regex_emailAddress))) {
            return Constant.EMAIL_INVALID;
        }
        return 0;
    }

}
