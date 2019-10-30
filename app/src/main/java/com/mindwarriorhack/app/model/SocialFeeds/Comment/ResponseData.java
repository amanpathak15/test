package com.mindwarriorhack.app.model.SocialFeeds.Comment;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ResponseData{

	@SerializedName("next")
	private String next;

	@SerializedName("comments")
	private List<CommentsItem> comments;

	@SerializedName("count")
	private int count;

	public void setNext(String next){
		this.next = next;
	}

	public String getNext(){
		return next;
	}

	public void setComments(List<CommentsItem> comments){
		this.comments = comments;
	}

	public List<CommentsItem> getComments(){
		return comments;
	}

	public void setCount(int count){
		this.count = count;
	}

	public int getCount(){
		return count;
	}

	@Override
 	public String toString(){
		return 
			"ResponseData{" + 
			"next = '" + next + '\'' + 
			",comments = '" + comments + '\'' + 
			",count = '" + count + '\'' + 
			"}";
		}
}