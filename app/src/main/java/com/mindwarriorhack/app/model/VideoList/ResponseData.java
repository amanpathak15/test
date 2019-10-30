package com.mindwarriorhack.app.model.VideoList;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ResponseData{

	@SerializedName("levelsList")
	private List<VideoListItem> levelsList;

	public void setLevelsList(List<VideoListItem> levelsList){
		this.levelsList = levelsList;
	}

	public List<VideoListItem> getLevelsList(){
		return levelsList;
	}

	@Override
 	public String toString(){
		return 
			"ResponseData{" + 
			"levelsList = '" + levelsList + '\'' + 
			"}";
		}
}