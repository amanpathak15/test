package com.mindwarriorhack.app.model.SocialFeeds.Likes;
import com.google.gson.annotations.SerializedName;

public class LikesItem{

	@SerializedName("userImage")
	private String userImage;

	@SerializedName("likeId")
	private int likeId;

	@SerializedName("postId")
	private int postId;

	@SerializedName("message")
	private String message;

	@SerializedName("userName")
	private String userName;

	@SerializedName("userId")
	private int userId;

	public void setUserImage(String userImage){
		this.userImage = userImage;
	}

	public String getUserImage(){
		return userImage;
	}

	public void setLikeId(int likeId){
		this.likeId = likeId;
	}

	public int getLikeId(){
		return likeId;
	}

	public void setPostId(int postId){
		this.postId = postId;
	}

	public int getPostId(){
		return postId;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}

	public void setUserName(String userName){
		this.userName = userName;
	}

	public String getUserName(){
		return userName;
	}

	public void setUserId(int userId){
		this.userId = userId;
	}

	public int getUserId(){
		return userId;
	}

	@Override
 	public String toString(){
		return 
			"LikesItem{" + 
			"userImage = '" + userImage + '\'' + 
			",likeId = '" + likeId + '\'' + 
			",postId = '" + postId + '\'' + 
			",message = '" + message + '\'' + 
			",userName = '" + userName + '\'' + 
			",userId = '" + userId + '\'' + 
			"}";
		}
}