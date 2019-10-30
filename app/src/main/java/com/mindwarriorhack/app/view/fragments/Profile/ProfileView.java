package com.mindwarriorhack.app.view.fragments.Profile;

public interface ProfileView {
    void profileValidations(String error, int errorView);
    void onSuccess(String msg, String profilePicUrl,String name,String email,String country,String mobNo,String gender,String dob,String occupation );
    void onFailure(String error);
    void onProfilePicUploadSuccess(String msg, String profilePic);
    void onProfilePicUploadFailure(String msg);

}
