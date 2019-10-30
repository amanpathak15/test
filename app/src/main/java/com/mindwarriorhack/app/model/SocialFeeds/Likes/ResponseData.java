package com.mindwarriorhack.app.model.SocialFeeds.Likes;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ResponseData{

	@SerializedName("next")
	private String next;

	@SerializedName("count")
	private int count;

	@SerializedName("likes")
	private List<LikesItem> likes;

	public void setNext(String next){
		this.next = next;
	}

	public String getNext(){
		return next;
	}

	public void setCount(int count){
		this.count = count;
	}

	public int getCount(){
		return count;
	}

	public void setLikes(List<LikesItem> likes){
		this.likes = likes;
	}

	public List<LikesItem> getLikes(){
		return likes;
	}

	@Override
 	public String toString(){
		return 
			"ResponseData{" + 
			"next = '" + next + '\'' + 
			",count = '" + count + '\'' + 
			",likes = '" + likes + '\'' + 
			"}";
		}
}