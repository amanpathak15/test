package com.mindwarriorhack.app.model.SocialFeeds.SocialFeedResponse;

import com.google.gson.annotations.SerializedName;

public class CommentItem {

	@SerializedName("dateTime")
	private String dateTime;

	@SerializedName("userPic")
	private String userPic;

	@SerializedName("commentId")
	private String commentId;

	@SerializedName("comment")
	private String comment;

	@SerializedName("commentuserName")
	private String userName;

	public void setDateTime(String dateTime){
		this.dateTime = dateTime;
	}

	public String getDateTime(){
		return dateTime;
	}

	public void setUserPic(String userPic){
		this.userPic = userPic;
	}

	public String getUserPic(){
		return userPic;
	}

	public void setCommentId(String commentId){
		this.commentId = commentId;
	}

	public String getCommentId(){
		return commentId;
	}

	public void setComment(String comment){
		this.comment = comment;
	}

	public String getComment(){
		return comment;
	}

	public void setUserName(String userName){
		this.userName = userName;
	}

	public String getUserName(){
		return userName;
	}

	@Override
 	public String toString(){
		return 
			"CommentItem{" +
			"dateTime = '" + dateTime + '\'' + 
			",userPic = '" + userPic + '\'' + 
			",commentId = '" + commentId + '\'' + 
			",comment = '" + comment + '\'' + 
			",userName = '" + userName + '\'' + 
			"}";
		}
}