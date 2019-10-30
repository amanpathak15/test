package com.mindwarriorhack.app.view.FeedCommentScreen;

import com.mindwarriorhack.app.model.SocialFeeds.Comment.FeedCommentResponse;
import com.mindwarriorhack.app.model.SocialFeeds.Likes.FeedLikeResponse;

import retrofit2.Response;

public interface feedCommentView {

    void getAllCommentSuccess(Response<FeedCommentResponse> response);
    void getAllCommentFailure(String msg);
    void postCommentSuccess(Response<FeedCommentResponse> response);
    void postCommentFailure(String msg);


}
