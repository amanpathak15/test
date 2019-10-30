package com.mindwarriorhack.app.model.Levels;
import com.google.gson.annotations.SerializedName;
import com.mindwarriorhack.app.model.CommonResponse;

public class LevelResponse extends CommonResponse {

	@SerializedName("responseData")
	private LevelResponseData levelResponseData;

	public void setLevelResponseData(LevelResponseData levelResponseData){
		this.levelResponseData = levelResponseData;
	}

	public LevelResponseData getLevelResponseData(){
		return levelResponseData;
	}



	@Override
 	public String toString(){
		return 
			"LevelResponse{" + 
			"levelResponseData = '" + levelResponseData + '\'' +
			"}";
		}
}