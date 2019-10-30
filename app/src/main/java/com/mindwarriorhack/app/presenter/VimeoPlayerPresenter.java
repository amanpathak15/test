package com.mindwarriorhack.app.presenter;

import android.content.Context;

import com.google.gson.JsonObject;
import com.mindwarriorhack.app.helper.Constant;
import com.mindwarriorhack.app.manager.PreferenceManager;
import com.mindwarriorhack.app.model.CommonResponse;
import com.mindwarriorhack.app.model.VideoLike.VideoLikeResponse;
import com.mindwarriorhack.app.model.VideoList.VideoListResponse;
import com.mindwarriorhack.app.services.ApiClient;
import com.mindwarriorhack.app.view.VideoList.VideoListView;
import com.mindwarriorhack.app.view.VimeoPlayer.VimeoPlayerView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VimeoPlayerPresenter {

    Context context;
    private VimeoPlayerView vimeoPlayerView;
    private ApiClient apiClient;

    public VimeoPlayerPresenter(VimeoPlayerView vimeoPlayerView, Context context) {
        this.vimeoPlayerView=vimeoPlayerView;
        this.context=context;
        if(this.apiClient==null){
            apiClient=new ApiClient();
        }
    }


    public void videoWatched(String videoId,int isWatched){
        JsonObject watchedObject=new JsonObject();
        watchedObject.addProperty("userId",PreferenceManager.getString(Constant.USER_ID));
        watchedObject.addProperty("videoId",videoId);
        watchedObject.addProperty("isWatched",isWatched);

        apiClient
                .getApi()
                .videoWatched(watchedObject)
                .enqueue(new Callback<CommonResponse>() {
                    @Override
                    public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                        if (response.body()!=null){
                            int responseCode = response.body().getResponseCode();

                            if (responseCode == 0) {
                                String successMsg = response.body().getResponseMsg();
                                vimeoPlayerView.onVideoWatchedSuccess(successMsg);
                            } else if(responseCode == 1) {
                                String errorMsg = response.body().getResponseMsg();
                                vimeoPlayerView.onVideoWatchedFailure(errorMsg);
                            }
                        }

                    }

                    @Override
                    public void onFailure(Call<CommonResponse> call, Throwable t) {
                        vimeoPlayerView.onVideoWatchedFailure(t.toString());
                    }
                });

    }
}
