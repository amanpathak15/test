package com.mindwarriorhack.app.presenter;

import android.content.Context;

import androidx.annotation.NonNull;
import android.util.Log;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mindwarriorhack.app.R;
import com.mindwarriorhack.app.helper.Constant;
import com.mindwarriorhack.app.helper.Utils;
import com.mindwarriorhack.app.services.ApiClient;
import com.mindwarriorhack.app.view.SignUp.SignUpView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpPresenter {

    private Context context;
    private SignUpView signUpView;
    private ApiClient apiClient;

    public SignUpPresenter(Context context, SignUpView signUpView) {
        this.context = context;
        this.signUpView = signUpView;
        if (this.apiClient == null) {
            this.apiClient = new ApiClient();
        }
    }


    public void SignUp(String name, String email, String country, String password, String confirmPassword) {
        if (validateLoginInputFields(name, email, country, password, confirmPassword) == Constant.NAME_INVALID) {
            signUpView.validateInputFields("Please Enter Name", Constant.NAME_INVALID);
        } else if (email.length()==0) {
            signUpView.validateInputFields("Please Enter Email Address", Constant.EMAIL_INVALID);
        } else if (validateLoginInputFields(name, email, country, password, confirmPassword) == Constant.EMAIL_EMPTY) {
            signUpView.validateInputFields("Please Enter Email Address", Constant.EMAIL_EMPTY);
        } else if (validateLoginInputFields(name, email, country, password, confirmPassword) == Constant.COUNTRY_INVALID) {
            signUpView.validateInputFields("Please Choose Country", Constant.COUNTRY_INVALID);
        } else if (validateLoginInputFields(name, email, country, password, confirmPassword) == Constant.EXISTING_PASSWORD_INVALID) {
            signUpView.validateInputFields("Password should be of 6–12 alpha numeric characters", Constant.EXISTING_PASSWORD_INVALID);
        }else if(!password.equals(confirmPassword)) {
            signUpView.validateInputFields("Password and Confirm Password didn't match",Constant.CONFIRM_PASSWORD_INVALID);
        }else {
            String encryptedPassword = Utils.encryptingTOMD5(password);
            JsonObject signUpObject = new JsonObject();

            signUpObject.addProperty("name",name);
            signUpObject.addProperty("email",email);
            signUpObject.addProperty("country",country);
            signUpObject.addProperty("password",encryptedPassword);

            Log.d("signup",signUpObject + "");

            apiClient
                    .getApi()
                    .logUp(signUpObject)
                    .enqueue(new Callback<JsonElement>() {
                        @Override
                        public void onResponse(@NonNull Call<JsonElement> call, @NonNull Response<JsonElement> response) {
                            if (response.body() != null) {
                                int code = response.body().getAsJsonObject().get("responseCode").getAsInt();
                                if (code == 0){
                                    String responseMsg = response.body().getAsJsonObject().get("responseMsg").getAsString();
                                    signUpView.signUpSuccess(responseMsg,"");
                                }else if (code == 1){
                                    String errorMsg = response.body().getAsJsonObject().get("responseMsg").getAsString();
                                    signUpView.signUpSuccess("",errorMsg);
                                }
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<JsonElement> call, @NonNull Throwable t) {
                            //Toast.makeText(context, t.toString(), Toast.LENGTH_SHORT).show();
                            signUpView.onFailure(t.toString());
                        }
                    });
        }
    }

    private int validateLoginInputFields(String name, String email, String country, String password, String confirmPassword) {

        if (name == null || name.length() == 0) {
            return Constant.NAME_INVALID;
        } else if (email.length() == 0) {
            return Constant.EMAIL_EMPTY;
        } else if(!email.matches(context.getResources().getString(R.string.regex_emailAddress))){
            return Constant.EMAIL_INVALID;
        } else if (country == null || country.equals("Country")) {
            return Constant.COUNTRY_INVALID;
        } else if (password == null || password.length() < 6) {
            return Constant.EXISTING_PASSWORD_INVALID;
        } else if (confirmPassword == null || confirmPassword.length() < 6) {
            return Constant.CONFIRM_PASSWORD_INVALID;
        } else if (!(password).equals(confirmPassword)) {
            return Constant.PASSWORD_NOT_MATCHED;
        }
        return 0;
    }
}
