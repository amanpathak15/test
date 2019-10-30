package com.mindwarriorhack.app.view.SignIn;

import com.mindwarriorhack.app.model.SignIn.SignInResponseData;

public interface SignInView {

    void signInValidations(String error,int errorView);
    void signInSuccess(String msg,SignInResponseData signInResponseData);
    void signInError(String error);
    void deviceInfoSuccess();
    void deviceInfoError();
}
