package com.mindwarriorhack.app.view.VimeoPlayer;

import com.mindwarriorhack.app.model.VideoLike.VideoLikeResponse;
import com.mindwarriorhack.app.model.VideoList.VideoListResponse;
import com.mindwarriorhack.app.model.VideoWatched.VideoWatchedResponse;

import retrofit2.Response;

public interface VimeoPlayerView
{


    void onVideoWatchedSuccess(String msg);
    void onVideoWatchedFailure(String errorMsg);
}
