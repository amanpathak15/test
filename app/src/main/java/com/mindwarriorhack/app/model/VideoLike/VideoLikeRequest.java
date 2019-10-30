package com.mindwarriorhack.app.model.VideoLike;


import com.google.gson.annotations.SerializedName;


public class VideoLikeRequest{

	@SerializedName("like")
	private int like;

	@SerializedName("videoId")
	private String videoId;

	@SerializedName("userId")
	private String userId;

	public void setLike(int like){
		this.like = like;
	}

	public int getLike(){
		return like;
	}

	public void setVideoId(String videoId){
		this.videoId = videoId;
	}

	public String getVideoId(){
		return videoId;
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
			"VideoLikeRequest{" + 
			"like = '" + like + '\'' + 
			",videoId = '" + videoId + '\'' + 
			",userId = '" + userId + '\'' + 
			"}";
		}
}