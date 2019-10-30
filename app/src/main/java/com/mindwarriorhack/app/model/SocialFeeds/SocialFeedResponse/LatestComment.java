package com.mindwarriorhack.app.model.SocialFeeds.SocialFeedResponse;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class LatestComment{

	@SerializedName("latestComment")
	private List<CommentItem> latestComment;

	public void setLatestComment(List<CommentItem> latestComment){
		this.latestComment = latestComment;
	}

	public List<CommentItem> getLatestComment(){
		return latestComment;
	}

	@Override
 	public String toString(){
		return 
			"LatestComment{" + 
			"latestComment = '" + latestComment + '\'' + 
			"}";
		}
}