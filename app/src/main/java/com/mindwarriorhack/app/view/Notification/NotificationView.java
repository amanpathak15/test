package com.mindwarriorhack.app.view.Notification;

import com.mindwarriorhack.app.model.Notification.BroadcastListItem;
import com.mindwarriorhack.app.model.Notification.BroadcastResponse;
import com.mindwarriorhack.app.model.Notification.ResponseData;

import java.util.List;

import retrofit2.Response;

public interface NotificationView {
    void onNotificationSuccess(BroadcastResponse response);
    void onNotificationFailure(String error);
    void onNotificationDeleted(String msg);
    void onNotificationDeletedError(String error);
}