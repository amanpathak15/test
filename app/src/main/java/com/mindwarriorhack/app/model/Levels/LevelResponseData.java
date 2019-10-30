package com.mindwarriorhack.app.model.Levels;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class LevelResponseData {

	@SerializedName("levelsList")
	private List<LevelsListItem> levelsList;

	public void setLevelsList(List<LevelsListItem> levelsList){
		this.levelsList = levelsList;
	}

	public List<LevelsListItem> getLevelsList(){
		return levelsList;
	}

	@Override
 	public String toString(){
		return 
			"LevelResponseData{" +
			"levelsList = '" + levelsList + '\'' + 
			"}";
		}
}