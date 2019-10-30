package com.mindwarriorhack.app.presenter;

import android.content.Context;

import com.google.gson.JsonObject;
import com.mindwarriorhack.app.helper.Constant;
import com.mindwarriorhack.app.manager.PreferenceManager;
import com.mindwarriorhack.app.model.Notification.BroadcastResponse;
import com.mindwarriorhack.app.model.Notification.ResponseData;
import com.mindwarriorhack.app.services.ApiClient;
import com.mindwarriorhack.app.view.Notification.NotificationView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationPresenter {
    private NotificationView notificationView;
    private ApiClient apiClient;
    private Context context;

    public NotificationPresenter(NotificationView notificationView, Context context) {
        this.notificationView = notificationView;
        this.context = context;
        if (this.apiClient == null) {
            this.apiClient = new ApiClient();
        }
    }

    public void broadcastList() {
        JsonObject broadcastObject = new JsonObject();
        broadcastObject.addProperty("userId", PreferenceManager.getString(Constant.USER_ID));

        apiClient
                .getApi()
                .getBroadCast(broadcastObject)
                .enqueue(new Callback<BroadcastResponse>() {
                    @Override
                    public void onResponse(Call<BroadcastResponse> call, Response<BroadcastResponse> response) {
                        if (response.body() != null) {
                            int responseCode = response.body().getResponseCode();
                            if (responseCode == 0) {
                                notificationView.onNotificationSuccess(response.body());
                            } else if (responseCode == 1) {
                                String errorMsg = response.body().getResponseMsg();
                                notificationView.onNotificationFailure(errorMsg);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<BroadcastResponse> call, Throwable t) {
                        notificationView.onNotificationFailure(t.toString());
                    }
                });
    }

    public void deleteBroadcast(String broadcastId) {
        JsonObject deleteBroadcast = new JsonObject();
        deleteBroadcast.addProperty("userId", PreferenceManager.getString(Constant.USER_ID));
        deleteBroadcast.addProperty("broadcastId", broadcastId);

        apiClient
                .getApi()
                .deleteBroadCast(deleteBroadcast)
                .enqueue(new Callback<BroadcastResponse>() {
                    @Override
                    public void onResponse(Call<BroadcastResponse> call, Response<BroadcastResponse> response) {
                        if (response.body() != null) {
                            int responseCode = response.body().getResponseCode();
                            if (responseCode == 0) {
                                String successMsg=response.body().getResponseMsg();
                                notificationView.onNotificationDeleted(successMsg);
                            } else if (responseCode == 1) {
                                String errorMsg = response.body().getResponseMsg();
                                notificationView.onNotificationDeletedError(errorMsg);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<BroadcastResponse> call, Throwable t) {
                        notificationView.onNotificationDeletedError(t.toString());
                    }
                });
    }
}
