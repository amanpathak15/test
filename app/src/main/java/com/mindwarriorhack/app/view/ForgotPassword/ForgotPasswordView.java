package com.mindwarriorhack.app.view.ForgotPassword;

public interface ForgotPasswordView {

    void validateEmail(String error, int errorView);
    void onSuccess(String msg);
    void onError(String error);
}
