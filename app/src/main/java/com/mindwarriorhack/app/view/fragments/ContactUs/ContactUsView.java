package com.mindwarriorhack.app.view.fragments.ContactUs;

import com.mindwarriorhack.app.model.ContactUs.ContactUsResponseData;

public interface ContactUsView {
    void onSuccess(String msg);
    void onFailure(String error);
    void inputValidations(String error, int errorview);
}
