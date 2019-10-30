package com.mindwarriorhack.app.model.SocialFeeds.Comment;

import com.google.gson.annotations.SerializedName;


public class CommentsItem{

	@SerializedName("userImage")
	private String userImage;

	@SerializedName("commentId")
	private int commentId;

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

	public void setCommentId(int commentId){
		this.commentId = commentId;
	}

	public int getCommentId(){
		return commentId;
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
			"CommentsItem{" + 
			"userImage = '" + userImage + '\'' + 
			",commentId = '" + commentId + '\'' + 
			",postId = '" + postId + '\'' + 
			",message = '" + message + '\'' + 
			",userName = '" + userName + '\'' + 
			",userId = '" + userId + '\'' + 
			"}";
		}
}