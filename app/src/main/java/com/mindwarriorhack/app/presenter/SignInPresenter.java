package com.mindwarriorhack.app.presenter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.google.gson.JsonObject;
import com.mindwarriorhack.app.BuildConfig;
import com.mindwarriorhack.app.R;
import com.mindwarriorhack.app.helper.Constant;
import com.mindwarriorhack.app.helper.Utils;
import com.mindwarriorhack.app.manager.PreferenceManager;
import com.mindwarriorhack.app.model.DeviceInfo.DeviceInfoResponse;
import com.mindwarriorhack.app.model.DeviceInfo.ResponseData;
import com.mindwarriorhack.app.model.SignIn.SignInResponse;
import com.mindwarriorhack.app.model.SignIn.SignInResponseData;
import com.mindwarriorhack.app.services.ApiClient;
import com.mindwarriorhack.app.view.SignIn.SignInView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInPresenter {
    private Context context;
    private SignInView signInView;
    private ApiClient apiClient;

    public SignInPresenter(Context context, SignInView signInView) {
        this.context = context;
        this.signInView = signInView;
        if (this.apiClient == null) {
            this.apiClient = new ApiClient();
        }
    }

    public void SignIn(String email, String password,String deviceToken) {
        if (validateLoginInputFields(email, password) == Constant.EMAIL_INVALID) {
            signInView.signInValidations("Please enter Email", Constant.EMAIL_INVALID);
        } else if (validateLoginInputFields(email, password) == Constant.EXISTING_PASSWORD_INVALID) {
            signInView.signInValidations("Please enter password", Constant.EXISTING_PASSWORD_INVALID);
        } else{
            String encryptedPassword = Utils.encryptingTOMD5(password);
            final JsonObject signInObject = new JsonObject();

            signInObject.addProperty("email", email);
            signInObject.addProperty("password", encryptedPassword);
            signInObject.addProperty("deviceToken", deviceToken);
            signInObject.addProperty("devicetype", "Android");
            signInObject.addProperty("timeZone",getTimeZone());
            Log.d("SignIn", signInObject.toString());

            apiClient
                    .getApi()
                    .loginIn(signInObject)
                    .enqueue(new retrofit2.Callback<SignInResponse>() {
                        @Override
                        public void onResponse(Call<SignInResponse> call, Response<SignInResponse> response) {
                            if (response.body() != null && response.body().getResponseCode() == 0) {
                                String successMsg = response.body().getResponseMsg();
                                SignInResponseData signInResponseData = response.body().getSignInResponseData();
                                signInView.signInSuccess(successMsg,signInResponseData);
                            } else {
                                String errorMsg = "";
                                if (response.body() != null) {
                                    errorMsg = response.body().getResponseMsg();
                                    signInView.signInError(errorMsg);
                                }
                            }
                        }


                        @Override
                        public void onFailure(Call<SignInResponse> call, Throwable t) {
                            signInView.signInError(t.toString());
                        }
                    });
        }
    }

    public void deviceInfoApi() {
        JsonObject deviceInfoObject = new JsonObject();
        deviceInfoObject.addProperty("userId",PreferenceManager.getString(Constant.USER_ID));
        deviceInfoObject.addProperty("dateTime", getCurrentDateTime());
        deviceInfoObject.addProperty("appVersion", BuildConfig.VERSION_NAME);
        deviceInfoObject.addProperty("OS", context.getString(R.string.System_OS));
        deviceInfoObject.addProperty("OSVersion", Build.VERSION.RELEASE);
        deviceInfoObject.addProperty("timeZone", getTimeZone());
        deviceInfoObject.addProperty("deviceId", PreferenceManager.getDeviceId());
        deviceInfoObject.addProperty("deviceName", Build.MODEL);
        deviceInfoObject.addProperty("appVersionCode", BuildConfig.VERSION_CODE);

        apiClient
                .getApi()
                .deviceInfo(deviceInfoObject)
                .enqueue(new Callback<DeviceInfoResponse>() {
                    @Override
                    public void onResponse(Call<DeviceInfoResponse> call, Response<DeviceInfoResponse> response) {
                        if (response.body() != null) {
                            if (response.body().getResponseCode() == 0) {
                                ResponseData responseData = Objects.requireNonNull(response.body()).getResponseData();
                                PreferenceManager.setString(Constant.DEVICE_ID, String.valueOf(responseData.getDeviceId()));
                                PreferenceManager.setString(Constant.CURRENT_VERSION, responseData.getCurrentVersion());
                                PreferenceManager.setString(Constant.LAST_COMPULSORY_VERSION, responseData.getLastCompulsoryVersion());
                                PreferenceManager.setString(Constant.DOWNLOAD_PATH, responseData.getDownloadPath());
                                signInView.deviceInfoSuccess();
                            } else if (response.body().getResponseCode() == 1) {
                                String errorMsg = response.body().getResponseMsg();
                                signInView.deviceInfoError();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<DeviceInfoResponse> call, Throwable t) {
                        signInView.deviceInfoError();
                    }
                });
    }


    private int validateLoginInputFields(String email, String password) {

        if(email.length() == 0){
            return Constant.EMAIL_EMPTY;
        }
        else if(!email.matches(context.getResources().getString(R.string.regex_emailAddress))){
            return Constant.EMAIL_INVALID;
        }
        else if(password.length() == 0){
            return Constant.PASSWORD_EMPTY;
        }
        else if(password.length() < 6){
            return Constant.EXISTING_PASSWORD_INVALID;
        }
        return 0;
    }

    public int validateInputFieldsIndividual(String inputField, String flag) {

        switch (flag) {

            case Constant.EMAIL_VALIDATION:
                if(inputField.isEmpty()){
                    return Constant.EMAIL_EMPTY;
                }
                else if(!inputField.matches(context.getResources().getString(R.string.regex_emailAddress))){
                    return Constant.EMAIL_INVALID;
                }
                break;

            case Constant.PASSWORD_VALIDATION:
                if(inputField.isEmpty()){
                    return Constant.PASSWORD_EMPTY;
                }
                else if(inputField.length() < 6){
                    return Constant.EXISTING_PASSWORD_INVALID;
                }
                break;

        }

        return 0;
    }

    private String getCurrentDateTime() {

        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }

    //returns Asia/Kolkata
    private String getTimeZone() {

        Calendar calendar = Calendar.getInstance();
        long milliDiff = calendar.get(Calendar.ZONE_OFFSET);
        String[] ids = TimeZone.getAvailableIDs();
        String timezone = null;
        for (String id : ids) {
            TimeZone tz = TimeZone.getTimeZone(id);
            if (tz.getRawOffset() == milliDiff) {
                timezone = id;
            }
        }
        return timezone;
    }
}


