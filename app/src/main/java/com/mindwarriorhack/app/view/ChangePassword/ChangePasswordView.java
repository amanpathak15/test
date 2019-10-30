package com.mindwarriorhack.app.view.ChangePassword;

public interface ChangePasswordView {
    void validateInput(String error, int errorView);
    void onSuccess(String msg, String errorMsg);
    void onError(String error);
}
