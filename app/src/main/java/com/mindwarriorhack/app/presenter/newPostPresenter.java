package com.mindwarriorhack.app.presenter;

import android.content.Context;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mindwarriorhack.app.helper.Constant;
import com.mindwarriorhack.app.manager.PreferenceManager;
import com.mindwarriorhack.app.services.ApiClient;
import com.mindwarriorhack.app.view.newPostScreen.newPostView;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class newPostPresenter {

    private Context context;
    private ApiClient apiClient;
    private com.mindwarriorhack.app.view.newPostScreen.newPostView newPostView;
    public newPostPresenter(Context context, newPostView newPostView) {
        this.context = context;
        this.newPostView = newPostView;
        if (this.apiClient == null){
            this.apiClient = new ApiClient();
        }
    }


    public void postTextFeed(String text) {

        apiClient
                .getApi()
                .postTextFeed(Integer.valueOf(PreferenceManager.getString(Constant.USER_ID)), text)
                .enqueue(new Callback<JsonElement>() {
                    @Override
                    public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                        assert response.body() != null;
                        if(response.body().getAsJsonObject().get("responseCode").getAsInt() == 0){
                            newPostView.onPostFeedSuccess(response);
                        }
                        else
                        {
                            String message = response.body().getAsJsonObject().get("responseMsg").getAsString();
                            newPostView.onPostFeedFailure(message,true);
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonElement> call, Throwable t) {
                        newPostView.onPostFeedFailure(t.toString(),false);
                    }
                });

    }




    public void postImageFeed(String text, String imageUrl) {



        RequestBody userIdPart = RequestBody.create(MultipartBody.FORM, PreferenceManager.getString(Constant.USER_ID));

        RequestBody messagePart = RequestBody.create(MultipartBody.FORM, text);

        File imageFile = new File(imageUrl);
        RequestBody requestFile = RequestBody.create(MediaType.parse("images/*"), imageFile);
        MultipartBody.Part body = MultipartBody.Part.createFormData("image", imageFile.getName(), requestFile);


        apiClient
                .getApi()
                .postImageFeed(userIdPart, messagePart, body)
                .enqueue(new Callback<JsonElement>() {
                    @Override
                    public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                        if(response.body().getAsJsonObject().get("responseCode").getAsInt() == 0) {
                            newPostView.onPostFeedSuccess(response);
                        }
                        else
                        {
                            String message = response.body().getAsJsonObject().get("responseMsg").getAsString();
                            newPostView.onPostFeedFailure(message,true);
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonElement> call, Throwable t) {
                        newPostView.onPostFeedFailure(t.toString(),false);
                    }
                });

    }













}
