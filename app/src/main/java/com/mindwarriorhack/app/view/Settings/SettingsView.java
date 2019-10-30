package com.mindwarriorhack.app.view.Settings;

public interface SettingsView {
    void onChangeNotificationStatusSuccess(String msg,int notifyMe);
    void onChangeNotificationStatusFailure(String errorMsg);

}
