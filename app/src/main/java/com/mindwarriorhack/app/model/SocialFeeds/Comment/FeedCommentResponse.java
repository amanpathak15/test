package com.mindwarriorhack.app.model.SocialFeeds.Comment;


import com.google.gson.annotations.SerializedName;
import com.mindwarriorhack.app.model.CommonResponse;


public class FeedCommentResponse extends CommonResponse {

	@SerializedName("responseData")
	private ResponseData responseData;

	public void setResponseData(ResponseData responseData){
		this.responseData = responseData;
	}

	public ResponseData getResponseData(){
		return responseData;
	}

	@Override
 	public String toString(){
		return 
			"FeedCommentResponse{" + 
			"responseData = '" + responseData + '\'' + 
			"}";
		}
}