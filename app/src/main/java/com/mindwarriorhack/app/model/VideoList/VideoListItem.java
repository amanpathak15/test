package com.mindwarriorhack.app.model.VideoList;


import com.google.gson.annotations.SerializedName;

public class VideoListItem {

	@SerializedName("duration")
	private String duration;

	@SerializedName("thumbNail")
	private String thumbNail;

	@SerializedName("videoUrl")
	private String videoUrl;

	@SerializedName("isLiked")
	private String isLiked;

	@SerializedName("isRated")
	private boolean isRated;

	@SerializedName("rating")
	private int rating;

	@SerializedName("videoId")
	private String videoId;

	@SerializedName("isWatched")
	private String isWatched;

	@SerializedName("title")
	private String title;

	@SerializedName("isAvailableToView")
	private boolean isAvailableToView;

	public boolean getIsAvailableToView() {
		return isAvailableToView;
	}

	public void setIsAvailableToView(boolean isAvailableToView) {
		this.isAvailableToView = isAvailableToView;
	}

	public boolean isRated() {
		return isRated;
	}

	public void setRated(boolean rated) {
		isRated = rated;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	public void setDuration(String duration){
		this.duration = duration;
	}

	public String getDuration(){
		return duration;
	}

	public void setThumbNail(String thumbNail){
		this.thumbNail = thumbNail;
	}

	public String getThumbNail(){
		return thumbNail;
	}

	public void setVideoUrl(String videoUrl){
		this.videoUrl = videoUrl;
	}

	public String getVideoUrl(){
		return videoUrl;
	}

	public boolean setIsLiked(String isLiked){
		this.isLiked = isLiked;
		return false;
	}

	public String getIsLiked(){
	    return isLiked;
	}

	public void setVideoId(String videoId){
		this.videoId = videoId;
	}

	public String getVideoId(){
		return videoId;
	}

	public boolean setIsWatched(String isWatched){
		this.isWatched = isWatched;
		return false;
	}

	public String getIsWatched(){
		return isWatched;
	}

	public void setTitle(String title){
		this.title = title;
	}

	public String getTitle(){
		return title;
	}

	@Override
 	public String toString(){
		return 
			"VideoListItem{" +
			"duration = '" + duration + '\'' + 
			",thumbNail = '" + thumbNail + '\'' + 
			",videoUrl = '" + videoUrl + '\'' + 
			",isLiked = '" + isLiked + '\'' + 
			",videoId = '" + videoId + '\'' + 
			",isWatched = '" + isWatched + '\'' + 
			",title = '" + title + '\'' + 
			"}";
		}
}