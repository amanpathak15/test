package com.mindwarriorhack.app.presenter;

import android.content.Context;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mindwarriorhack.app.helper.Constant;
import com.mindwarriorhack.app.manager.PreferenceManager;
import com.mindwarriorhack.app.model.SocialFeeds.SocialFeedResponse.SocialFeedResponse;
import com.mindwarriorhack.app.services.ApiClient;
import com.mindwarriorhack.app.view.fragments.SocialFeedsFragment.socialFeedsView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class socialFeedsPresenter {

    private Context context;
    private ApiClient apiClient;
    private socialFeedsView socialFeedsView;
    private socialFeedsPresenter socialFeedsPresenter;


    public socialFeedsPresenter(Context context, socialFeedsView socialFeedsView) {
        this.context = context;
        this.socialFeedsView = socialFeedsView;
        if (this.apiClient == null){
            this.apiClient = new ApiClient();
        }
    }


    public void getSocialFeeds() {

        JsonObject object = new JsonObject();
        object.addProperty("userId", Integer.parseInt(PreferenceManager.getString(Constant.USER_ID)));


        apiClient
                .getApi()
                .getSocialFeeds(PreferenceManager.getString(Constant.FEED_PAGINATION_URL), object)
                .enqueue(new Callback<SocialFeedResponse>() {
                    @Override
                    public void onResponse(Call<SocialFeedResponse> call, Response<SocialFeedResponse> response) {
                        if(response.body().getResponseCode() == 0) {
                            socialFeedsView.onGetFeedsSuccess(response);
                        }
                        else
                        {
                            PreferenceManager.setString(Constant.IS_FEED_PAGINATION_PROCESSING,"false");
                        }
                    }

                    @Override
                    public void onFailure(Call<SocialFeedResponse> call, Throwable t) {
                        socialFeedsView.onGetFeedsFailure(t.toString());
                    }
                });
    }







    public void likesCallApi(int flag, int postId, final int feedPosition){

        JsonObject object = new JsonObject();
        object.addProperty("userId", PreferenceManager.getString(Constant.USER_ID));
        object.addProperty("postId", postId);
        object.addProperty("like", flag);


        apiClient
                .getApi()
                .likeFeed(object)
                .enqueue(new Callback<JsonElement>() {
                    @Override
                    public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                        if(response.isSuccessful()) {
                            socialFeedsView.onFeedLikeSuccess(response);
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonElement> call, Throwable t) {
                        socialFeedsView.onFeedLikeFailure(t.toString());
                    }
                });
    }




    public void reportFeedCallApi(int postId){

        JsonObject object = new JsonObject();
        object.addProperty("userId",PreferenceManager.getString(Constant.USER_ID));
        object.addProperty("postId",postId);


        apiClient
                .getApi()
                .reportFeed(object)
                .enqueue(new Callback<JsonElement>() {
                    @Override
                    public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                        if(response.isSuccessful()) {
                            socialFeedsView.onReportFeedSuccess(response);
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonElement> call, Throwable t) {
                        socialFeedsView.onReportFeedFailure(t.toString());
                    }
                });
    }








    /*public void postTextFeeds(String text) {

        JsonObject object = new JsonObject();
        object.addProperty("created_by", (String) null);
        object.addProperty("url","");
        object.addProperty("text",text);
        object.addProperty("group", (String) null);

        apiClient
                .getApi()
                .postFeed(PreferenceManager.getString(Constant.TOKEN),PreferenceManager.getString(Constant.DOMAIN),object)
                .enqueue(new Callback<postFeeds>() {
                    @Override
                    public void onResponse(Call<postFeeds> call, Response<postFeeds> response) {
                        if(response.isSuccessful()) {
                            homeView.onPostFeedSuccess(response);
                        }
                    }

                    @Override
                    public void onFailure(Call<postFeeds> call, Throwable t) {
                        homeView.onFailure(t.toString());
                    }
                });
    }
*/


}
