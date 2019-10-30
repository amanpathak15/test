package com.mindwarriorhack.app.presenter;

import android.content.Context;

import com.google.gson.JsonElement;
import com.mindwarriorhack.app.R;
import com.mindwarriorhack.app.helper.Constant;
import com.mindwarriorhack.app.manager.PreferenceManager;
import com.mindwarriorhack.app.model.UpdateProfile.UpdateProfileResponse;
import com.mindwarriorhack.app.model.UpdateProfile.UpdateResponseData;
import com.mindwarriorhack.app.services.ApiClient;
import com.mindwarriorhack.app.view.fragments.Profile.ProfileView;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfilePresenter {
    private ApiClient apiClient;
    private Context context;
    private ProfileView profileView;


    public ProfilePresenter(Context context, ProfileView profileView) {
        this.context = context;
        this.profileView = profileView;
        if (this.apiClient == null) {
            apiClient = new ApiClient();
        }
    }

    public void updateProfilePic(String imageUrl) {


        RequestBody userIdPart = RequestBody.create(MultipartBody.FORM, PreferenceManager.getString(Constant.USER_ID));

        File imageFile = new File(imageUrl);
        RequestBody requestFile = RequestBody.create(MediaType.parse("images/*"), imageFile);
        MultipartBody.Part body = MultipartBody.Part.createFormData("image", imageFile.getName(), requestFile);


        apiClient.
                getApi()
                .editProfilePic(userIdPart, body)
                .enqueue(new Callback<JsonElement>() {
                    @Override
                    public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                        if (response.isSuccessful()) {
                            int responseCode = response.body().getAsJsonObject().get("responseCode").getAsInt();
                            if (responseCode == 0) {
                                String msg = response.body().getAsJsonObject().get("responseMsg").getAsString();
                                String profilePic = response.body().getAsJsonObject().get("responseData").getAsJsonObject().get("profilepic").getAsString();
                                profileView.onProfilePicUploadSuccess(msg, profilePic);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonElement> call, Throwable t) {
                        profileView.onProfilePicUploadFailure(t.toString());
                    }
                });
    }

    public void updateProfile(String userId, String name, String email, String country, String mobNo,String gender,String dob,String occupation) {
        if (validateLoginInputFields(userId, name, country, email, mobNo,gender,dob,occupation) == Constant.NAME_INVALID) {
            profileView.profileValidations("Please enter name", Constant.NAME_INVALID);
        } else if (validateLoginInputFields(userId, name, country, email, mobNo,gender,dob,occupation) == Constant.EMAIL_INVALID) {
            profileView.profileValidations("Please enter valid email", Constant.EMAIL_INVALID);
        } else if (validateLoginInputFields(userId, name, country, email, mobNo,gender,dob,occupation) == Constant.COUNTRY_INVALID) {
            profileView.profileValidations("Please enter valid country", Constant.COUNTRY_INVALID);
        } else if (validateLoginInputFields(userId, name, email, country, mobNo,gender,dob,occupation) == Constant.PHONE_INVALID) {
            profileView.profileValidations("Please enter valid mobile number", Constant.PHONE_INVALID);
        }else if(mobNo.length()<8 && mobNo.length() == 0){
            profileView.profileValidations("Enter between 8-12 numbers",0);
        }else if(validateLoginInputFields(userId, name, email, country, mobNo, gender, dob, occupation) == Constant.GENDER_INVALID){
            profileView.profileValidations("Please enter gender",Constant.GENDER_INVALID);
        }else if(validateLoginInputFields(userId, name, email, country, mobNo,gender, dob, occupation) == Constant.DOB_INVALID){
            profileView.profileValidations("Please enter date of birth",Constant.DOB_INVALID);
        }else if(validateLoginInputFields(userId, name, email, country, mobNo,gender, dob, occupation) == Constant.OCCUPATION_INVALID ){
            profileView.profileValidations("Please enter occupation",Constant.OCCUPATION_INVALID);
        }
        else {
            UpdateResponseData updateResponseData = new UpdateResponseData();
            updateResponseData.setUserId(userId);
            updateResponseData.setName(name);
            updateResponseData.setEmail(email);
            updateResponseData.setCountry(country);
            updateResponseData.setMobNo(mobNo);
            updateResponseData.setGender(gender);
            updateResponseData.setDob(dob);
            updateResponseData.setOccupation(occupation);

            apiClient
                    .getApi()
                    .updateProfile(updateResponseData)
                    .enqueue(new Callback<UpdateProfileResponse>() {
                        @Override
                        public void onResponse(Call<UpdateProfileResponse> call, Response<UpdateProfileResponse> response) {
                            if (response.body() != null) {
                                int code = response.body().getResponseCode();
                                if (code == 0) {
                                    String successMsg = response.body().getResponseMsg();
                                    String email = response.body().getUpdateResponse().getEmail();
                                    String name = response.body().getUpdateResponse().getName();
                                    String country = response.body().getUpdateResponse().getCountry();
                                    String mobNo = response.body().getUpdateResponse().getMobNo();
                                    String gender = response.body().getUpdateResponse().getGender();
                                    String dob= response.body().getUpdateResponse().getDob();
                                    String occupation = response.body().getUpdateResponse().getOccupation();

                                    profileView.onSuccess(successMsg, PreferenceManager.getString(Constant.PROFILE_PIC), name, email, country, mobNo,gender,dob,occupation);
                                } else if (code == 1) {
                                    String errorMsg = response.body().getResponseMsg();
                                    profileView.onFailure(errorMsg);
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<UpdateProfileResponse> call, Throwable t) {
                            profileView.onFailure(t.toString());
                        }
                    });
        }
    }

    private int validateLoginInputFields(String userId, String name, String country,
           String email, String mobNo,String gender,String dob,String occupation) {
        if (email == null || email.length() == 0 || !email.matches(context.getResources().getString(R.string.regex_emailAddress))) {
            return Constant.EMAIL_INVALID;
        } else if (name == null || name.length() == 0) {
            return Constant.NAME_INVALID;
        } else if (country == null || country.length() == 0) {
            return Constant.COUNTRY_INVALID;
        } else if (mobNo == null || mobNo.length() == 0) {
            return Constant.PHONE_INVALID;
        }else if(gender == null || gender.length() == 0){
            return Constant.GENDER_INVALID;
        } else if(dob == null || dob.length() == 0 || !dob.matches(context.getResources().getString(R.string.regex_date))){
            return Constant.DOB_INVALID;
        }else if(occupation == null || occupation.length() == 0){
            return Constant.OCCUPATION_INVALID;
        }
        return 0;
    }


}
