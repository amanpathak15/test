package com.mindwarriorhack.app.view.SignUp;

public interface SignUpView
{
    void signUpSuccess(String msg, String errorMsg);
    void validateInputFields(String error, int errorView);
    void onFailure(String error);
}
