package com.mindwarriorhack.app.model.SocialFeeds.SocialFeedResponse;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FeedsItem{

	@SerializedName("image")
	private String image;

	@SerializedName("userImage")
	private String userImage;

	@SerializedName("postDate")
	private String postDate;

	@SerializedName("postId")
	private int postId;

	@SerializedName("message")
	private String message;

	@SerializedName("userName")
	private String userName;

	@SerializedName("userId")
	private String userId;

	@SerializedName("likeCount")
	private String likeCount;

	@SerializedName("commentCount")
	private String commentCount;

	@SerializedName("latestComment")
	private List<CommentItem> latestComment;

	@SerializedName("isLike")
	private boolean isLike;

	public boolean isLike() {
		return isLike;
	}

	public void setLike(boolean like) {
		isLike = like;
	}

	public List<CommentItem> getLatestComment() {
		return latestComment;
	}

	public void setLatestComment(List<CommentItem> latestComment) {
		this.latestComment = latestComment;
	}

	public String getLikeCount() {
		return likeCount;
	}

	public void setLikeCount(String likeCount) {
		this.likeCount = likeCount;
	}

	public String getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(String commentCount) {
		this.commentCount = commentCount;
	}

	public void setImage(String image){
		this.image = image;
	}

	public String getImage(){
		return image;
	}

	public void setUserImage(String userImage){
		this.userImage = userImage;
	}

	public String getUserImage(){
		return userImage;
	}

	public void setPostDate(String postDate){
		this.postDate = postDate;
	}

	public String getPostDate(){
		return postDate;
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

	public void setUserId(String userId){
		this.userId = userId;
	}

	public String getUserId(){
		return userId;
	}

	@Override
 	public String toString(){
		return 
			"FeedsItem{" + 
			"image = '" + image + '\'' + 
			",userImage = '" + userImage + '\'' + 
			",postDate = '" + postDate + '\'' + 
			",postId = '" + postId + '\'' + 
			",message = '" + message + '\'' + 
			",userName = '" + userName + '\'' + 
			",userId = '" + userId + '\'' + 
			"}";
		}
}