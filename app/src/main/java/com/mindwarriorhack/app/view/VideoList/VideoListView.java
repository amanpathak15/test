package com.mindwarriorhack.app.view.VideoList;

import com.mindwarriorhack.app.model.VideoLike.VideoLikeResponse;
import com.mindwarriorhack.app.model.VideoList.VideoListResponse;
import com.mindwarriorhack.app.model.VideoWatched.VideoWatchedResponse;

import retrofit2.Response;

public interface VideoListView
{
    void onSuccess(Response<VideoListResponse> response);
    void onFailure(String error);
    void onSuccessLiked(Response<VideoLikeResponse> success, int like, int position);
    void onVideoWatched(Response<VideoWatchedResponse> successMsg,int position, int watched);
    void onVideoWatchedSuccess(String msg);
    void onVideoWatchedFailure(String errorMsg);
}
