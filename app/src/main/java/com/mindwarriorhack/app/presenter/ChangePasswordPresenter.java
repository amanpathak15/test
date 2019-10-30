package com.mindwarriorhack.app.presenter;

import android.content.Context;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mindwarriorhack.app.helper.Constant;
import com.mindwarriorhack.app.helper.Utils;
import com.mindwarriorhack.app.manager.PreferenceManager;
import com.mindwarriorhack.app.services.ApiClient;
import com.mindwarriorhack.app.view.ChangePassword.ChangePasswordView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordPresenter {
    Context context;
    private ChangePasswordView changePasswordView;
    private ApiClient apiClient;

    public ChangePasswordPresenter(ChangePasswordView changePasswordView, Context context) {
        this.changePasswordView = changePasswordView;
        this.context = context;
        if (this.apiClient == null) {
            this.apiClient = new ApiClient();
        }
    }

    public void changePassword(String oldPassword, String newPassword) {
        int validate = validateInputFields(oldPassword, newPassword);

        if (validate == Constant.EXISTING_PASSWORD_INVALID || validate == Constant.NEW_PASSWORD_INVALID ) {
            changePasswordView.validateInput("Password should be of 6–12 alpha numeric characters", validate);
        }else {
            String encryptedExistingPassword = Utils.encryptingTOMD5(oldPassword);
            String encryptedNewPassword = Utils.encryptingTOMD5(newPassword);

            JsonObject changePasswordObject = new JsonObject();
            changePasswordObject.addProperty("userId", PreferenceManager.getString(Constant.USER_ID));
            changePasswordObject.addProperty("oldPassword", encryptedExistingPassword);
            changePasswordObject.addProperty("newPassword", encryptedNewPassword);

            apiClient
                    .getApi()
                    .changePassword(changePasswordObject)
                    .enqueue(new Callback<JsonElement>() {
                        @Override
                        public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                            if (response.body() != null) {
                                int code = response.body().getAsJsonObject().get("responseCode").getAsInt();
                                if (code == 0) {
                                    String successMsg = response.body().getAsJsonObject().get("responseMsg").getAsString();
                                    changePasswordView.onSuccess(successMsg, "");
                                } else if (code == 1) {
                                    String errorMsg = response.body().getAsJsonObject().get("responseMsg").getAsString();
                                    changePasswordView.onError(errorMsg);
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<JsonElement> call, Throwable t) {
                            changePasswordView.onError(t.toString());
                        }
                    });
        }
    }

    private int validateInputFields(String oldPassword, String newPassword) {
        if (oldPassword == null || oldPassword.length() < 6) {
            return Constant.EXISTING_PASSWORD_INVALID;
        } else if (newPassword == null || newPassword.length() < 6) {
            return Constant.NEW_PASSWORD_INVALID;
        }
        return 0;
    }

}