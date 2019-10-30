package com.mindwarriorhack.app.model.Packages;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class ResponseData{

	@SerializedName("levelsList")
	private List<PackageListItem> levelsList;

	public void setLevelsList(List<PackageListItem> levelsList){
		this.levelsList = levelsList;
	}

	public List<PackageListItem> getLevelsList(){
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