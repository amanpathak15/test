package com.mindwarriorhack.app.presenter;

import android.content.Context;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mindwarriorhack.app.helper.Constant;
import com.mindwarriorhack.app.manager.PreferenceManager;
import com.mindwarriorhack.app.services.ApiClient;
import com.mindwarriorhack.app.view.fragments.ContactUs.ContactUsView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContactUsPresenter {
    private Context context;
    private ContactUsView contactUsView;
    private ApiClient apiClient;

    public ContactUsPresenter(Context context, ContactUsView contactUsView) {
        this.context = context;
        this.contactUsView = contactUsView;
        if (this.apiClient == null) {
            this.apiClient = new ApiClient();
        }
    }


    public void contactUs(String subject, String description) {
        if (validateInputFields(subject, description) == Constant.SUBJECT_INVALID) {
            contactUsView.inputValidations("Please enter subject", Constant.SUBJECT_INVALID);
        } else if (validateInputFields(subject, description) == Constant.DESCRIPTION_INVALID) {
            contactUsView.inputValidations("Please enter description", Constant.DESCRIPTION_INVALID);
        } else {
            JsonObject contactUsObject = new JsonObject();
            contactUsObject.addProperty("userId", PreferenceManager.getString(Constant.USER_ID));
            contactUsObject.addProperty("subject", subject);
            contactUsObject.addProperty("description", description);

            apiClient
                    .getApi()
                    .contactUs(contactUsObject)
                    .enqueue(new Callback<JsonElement>() {
                        @Override
                        public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                            if (response.body() != null) {
                                int code = response.body().getAsJsonObject().get("responseCode").getAsInt();
                                if (code == 0) {
                                    String successMsg = response.body().getAsJsonObject().get("responseMsg").getAsString();
                                    contactUsView.onSuccess(successMsg);
                                } else if (code == 1) {
                                    String errorMsg = response.body().getAsJsonObject().get("responseMsg").getAsString();
                                    contactUsView.onFailure(errorMsg);
                                }
                            } else {
                                contactUsView.onFailure("Please try reaching us after sometime.");
                            }
                        }

                        @Override
                        public void onFailure(Call<JsonElement> call, Throwable t) {
                            contactUsView.onFailure(t.toString());
                        }
                    });
        }
    }

    private int validateInputFields(String subject, String description) {
        if (subject == null || subject.length() == 0) {
            return Constant.SUBJECT_INVALID;
        } else if (description == null || description.length() == 0) {
            return Constant.DESCRIPTION_INVALID;
        }
        return 0;
    }

}
