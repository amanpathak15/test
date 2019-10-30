package com.mindwarriorhack.app.services;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mindwarriorhack.app.model.CommonResponse;
import com.mindwarriorhack.app.model.DeviceInfo.DeviceInfoResponse;
import com.mindwarriorhack.app.model.Levels.LevelResponse;
import com.mindwarriorhack.app.model.Logout.LogoutResponse;
import com.mindwarriorhack.app.model.Notification.BroadcastResponse;
import com.mindwarriorhack.app.model.Notification.ResponseData;
import com.mindwarriorhack.app.model.Packages.PackagesResponse;
import com.mindwarriorhack.app.model.SignIn.SignInResponse;
import com.mindwarriorhack.app.model.SocialFeeds.Comment.FeedCommentResponse;
import com.mindwarriorhack.app.model.SocialFeeds.Likes.FeedLikeResponse;
import com.mindwarriorhack.app.model.SocialFeeds.SocialFeedResponse.SocialFeedResponse;
import com.mindwarriorhack.app.model.UpdateProfile.UpdateProfileResponse;
import com.mindwarriorhack.app.model.UpdateProfile.UpdateResponseData;
import com.mindwarriorhack.app.model.VideoLike.VideoLikeResponse;
import com.mindwarriorhack.app.model.VideoList.VideoListResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Url;

public interface Api {

    @POST("signIn")
    @Headers("Content-type: application/json")
    Call<SignInResponse> loginIn(@Body JsonObject signInObject);

    @POST("signUp")
    @Headers("Content-type: application/json")
    Call<JsonElement> logUp(@Body JsonObject signUpObject);

    @POST("editProfilePic")
    @Multipart
    Call<JsonElement> editProfilePic(@Part("userId") RequestBody userIdPart, @Part MultipartBody.Part file);

    @POST("editProfile")
    @Headers("Content-type: application/json")
    Call<UpdateProfileResponse> updateProfile(@Body UpdateResponseData object);

    @POST("changePassword")
    @Headers("Content-type: application/json")
    Call<JsonElement> changePassword(@Body JsonObject changePasswordObject);

    @POST("aboutContent")
    @Headers("Content-type: application/json")
    Call<JsonElement> aboutUs();

    @POST("forgotPassword")
    @Headers("Content-type: application/json")
    Call<JsonElement> forgotPassword(@Body JsonObject forgotPasswordObject);

    @POST("changeNotificationStatus")
    @Headers("Content-type: application/json")
    Call<JsonElement> changeNotificationStatus(@Body JsonObject changeNotificationStatusObject);

    @POST("contactUs")
    Call<JsonElement> contactUs(@Body JsonObject jsonObject);

    @POST("getLevels")
    Call<LevelResponse> getLevels(@Body JsonObject jsonObject);

    @POST("checkoutCart")
    Call<JsonElement> checkoutCart(@Body JsonObject jsonObject);

    @POST("updatePaymentStatus")
    Call<JsonElement> updatePaymentStatus(@Body JsonObject jsonObject);

    @POST("getVideoList/")
    Call<VideoListResponse> getVideoList(@Body JsonObject jsonObject);

    @POST("videoLike")
    Call<VideoLikeResponse> getVideoLike(@Body JsonObject jsonObject);

    @POST("videoView")
    Call<CommonResponse> videoWatched(@Body JsonObject jsonObject);

    @POST("getPackages")
    Call<PackagesResponse> getPackages(@Body JsonObject jsonObject);

    @POST("deviceInfo")
    Call<DeviceInfoResponse> deviceInfo(@Body JsonObject jsonObject);

    @POST("logout")
    Call<LogoutResponse> logout(@Body JsonObject jsonObject);

    @POST("getBroadCastList")
    Call<BroadcastResponse> getBroadCast(@Body JsonObject jsonObject);

    @POST
    @Headers("Content-type: application/json")
    Call<SocialFeedResponse> getSocialFeeds(@Url String url, @Body JsonObject jsonObject);

    @POST("addPost")
    @FormUrlEncoded
    Call<JsonElement> postTextFeed(@Field("userId") Integer userId, @Field("message") String message);

    @POST("addPost")
    @Multipart
    Call<JsonElement> postImageFeed(@Part("userId") RequestBody userIdPart, @Part("message") RequestBody messagePart, @Part MultipartBody.Part file);
    @POST("postLike")
    @Headers("Content-type: application/json")
    Call<JsonElement> likeFeed(@Body JsonObject jsonObject);

    @POST("postAbusive")
    @Headers("Content-type: application/json")
    Call<JsonElement> reportFeed(@Body JsonObject jsonObject);

    @POST
    @Headers("Content-type: application/json")
    Call<FeedLikeResponse> getAllLikes(@Url String url, @Body JsonObject jsonObject);

    @POST
    @Headers("Content-type: application/json")
    Call<FeedCommentResponse> getAllComment(@Url String url, @Body JsonObject jsonObject);

    @POST("postComment")
    @Headers("Content-type: application/json")
    Call<FeedCommentResponse> postComment(@Body JsonObject jsonObject);

    @POST("videoRate")
    @Headers("Content-type: application/json")
    Call<JsonElement> rateVideo(@Body JsonObject object);

    @POST("deleteBroadCast")
    Call<BroadcastResponse> deleteBroadCast(@Body JsonObject object);
}
