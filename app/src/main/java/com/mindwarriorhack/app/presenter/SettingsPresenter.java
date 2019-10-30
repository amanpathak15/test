package com.mindwarriorhack.app.presenter;

import android.content.Context;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mindwarriorhack.app.services.ApiClient;
import com.mindwarriorhack.app.view.Settings.SettingsView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingsPresenter {
    private SettingsView settingsView;
    private ApiClient apiClient;
    Context context;

    public SettingsPresenter(SettingsView settingsView, Context context) {
        this.settingsView = settingsView;
        this.context = context;
        if (this.apiClient == null) {
            this.apiClient = new ApiClient();
        }
    }

    public void changeNotificationStatus(String userId, int notifyMe) {

            JsonObject notificationStatusObject = new JsonObject();
            notificationStatusObject.addProperty("userId",userId);
            notificationStatusObject.addProperty("notifyMe", notifyMe);

            apiClient
                    .getApi()
                    .changeNotificationStatus(notificationStatusObject)
                    .enqueue(new Callback<JsonElement>() {
                        @Override
                        public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                            if (response.body() != null) {
                                int code = response.body().getAsJsonObject().get("responseCode").getAsInt();
                                if (code == 0) {
                                    String successMsg = response.body().getAsJsonObject().get("responseMsg").getAsString();
                                    int notifyMe = response.body().getAsJsonObject().get("responseData").getAsJsonObject().get("notifyMe").getAsInt();
                                    settingsView.onChangeNotificationStatusSuccess(successMsg,notifyMe);
                                } else if(code == 1) {
                                    String errorMsg = response.body().getAsJsonObject().get("responseMsg").getAsString();
                                    settingsView.onChangeNotificationStatusFailure(errorMsg);
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<JsonElement> call, Throwable t) {
                            settingsView.onChangeNotificationStatusFailure(t.toString());
                        }
                    });
        }
}