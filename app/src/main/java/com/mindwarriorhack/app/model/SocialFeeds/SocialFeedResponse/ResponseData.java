package com.mindwarriorhack.app.model.SocialFeeds.SocialFeedResponse;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ResponseData{

	@SerializedName("next")
	private String next;

	@SerializedName("count")
	private int count;

	@SerializedName("feeds")
	private List<FeedsItem> feeds;

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

	public void setFeeds(List<FeedsItem> feeds){
		this.feeds = feeds;
	}

	public List<FeedsItem> getFeeds(){
		return feeds;
	}

	@Override
 	public String toString(){
		return 
			"ResponseData{" + 
			"next = '" + next + '\'' + 
			",count = '" + count + '\'' + 
			",feeds = '" + feeds + '\'' + 
			"}";
		}
}