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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VideoListPresenter {

    Context context;
    private VideoListView videoListView;
    private ApiClient apiClient;

    public VideoListPresenter(VideoListView videoListView,Context context) {
        this.videoListView=videoListView;
        this.context=context;
        if(this.apiClient==null){
            apiClient=new ApiClient();
        }
    }

    public void getVideoList(String levelId){
        JsonObject videoObject=new JsonObject();
        videoObject.addProperty("userId",PreferenceManager.getString(Constant.USER_ID));
        videoObject.addProperty("levelId",levelId);

        apiClient
                .getApi()
                .getVideoList(videoObject)
                .enqueue(new Callback<VideoListResponse>() {
                    @Override
                    public void onResponse(Call<VideoListResponse> call, Response<VideoListResponse> response) {
                        if (response.body() != null) {

                            int responseCode = response.body().getResponseCode();

                            if (responseCode == 0) {
                                String successMsg = response.body().getResponseMsg();
                                videoListView.onSuccess(response);
                            } else if(responseCode == 1) {
                                String errorMsg = response.body().getResponseMsg();
                                videoListView.onFailure(errorMsg);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<VideoListResponse> call, Throwable t) {
                        videoListView.onFailure(t.toString());
                    }
                });
    }

    public  void getVideoLike(String videoId, final int like, final int position){
        JsonObject videoLikeObject=new JsonObject();
        videoLikeObject.addProperty("userId",PreferenceManager.getString(Constant.USER_ID));
        videoLikeObject.addProperty("videoId",videoId);
        videoLikeObject.addProperty("like",like);

        apiClient
                .getApi()
                .getVideoLike(videoLikeObject)
                .enqueue(new Callback<VideoLikeResponse>() {
                    @Override
                    public void onResponse(Call<VideoLikeResponse> call, Response<VideoLikeResponse> response) {
                        if(response.body()!=null){
                            int responseCode=response.body().getResponseCode();

                            if(responseCode==0){
                                String successMsg=response.body().getResponseMsg();
                                videoListView.onSuccessLiked(response, like, position );
                            }
                            else if(responseCode==1){
                                String errorMsg=response.body().getResponseMsg();
                                videoListView.onFailure(errorMsg);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<VideoLikeResponse> call, Throwable t) {
                        videoListView.onFailure(t.toString());
                    }
                });
    }


    public void videoWatched(String videoId, int position,int isWatched){
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
                                videoListView.onVideoWatchedSuccess(successMsg);
                            } else if(responseCode == 1) {
                                String errorMsg = response.body().getResponseMsg();
                                videoListView.onVideoWatchedFailure(errorMsg);
                            }
                        }

                    }

                    @Override
                    public void onFailure(Call<CommonResponse> call, Throwable t) {
                        videoListView.onVideoWatchedFailure(t.toString());
                    }
                });

    }
}
