package com.mindwarriorhack.app.model.VideoWatched;


import com.google.gson.annotations.SerializedName;

public class VideoWatchedRequest{

	@SerializedName("videoId")
	private String videoId;

	@SerializedName("isWatched")
	private int isWatched;

	@SerializedName("userId")
	private String userId;

	public void setVideoId(String videoId){
		this.videoId = videoId;
	}

	public String getVideoId(){
		return videoId;
	}

	public void setIsWatched(int isWatched){
		this.isWatched = isWatched;
	}

	public int getIsWatched(){
		return isWatched;
	}

	public void setUserId(String userId){
		this.userId = userId;
	}

	public String getUserId(){
		return userId;
	}

	@Override
 	public String toString(){
		return 
			"VideoWatchedRequest{" + 
			"videoId = '" + videoId + '\'' + 
			",isWatched = '" + isWatched + '\'' + 
			",userId = '" + userId + '\'' + 
			"}";
		}
}