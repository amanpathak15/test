package com.mindwarriorhack.app.model.Levels;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class LevelsListItem implements Serializable {

	@SerializedName("subscribed")
	private int subscribed;

	@SerializedName("levelPrice")
	private float levelPrice;

	@SerializedName("levelId")
	private String levelId;

	@SerializedName("levelLogo")
	private String levelLogo;

	@SerializedName("description")
	private String description;

	@SerializedName("title")
	private String title;

	@SerializedName("isPaidLevel")
	private int isPaidLevel;


	public LevelsListItem(int subscribed, int levelPrice, String levelId, String levelLogo, String title, int isPaidLevel,String description) {
		this.subscribed = subscribed;
		this.levelPrice = levelPrice;
		this.levelId = levelId;
		this.levelLogo = levelLogo;
		this.description=description;
		this.title = title;
		this.isPaidLevel = isPaidLevel;
	}
	public void setDescription(String description){this.description=description;}

	public String getDescription(){return description;}

	public void setSubscribed(int subscribed){
		this.subscribed = subscribed;
	}

	public int getSubscribed(){
		return subscribed;
	}

	public void setLevelPrice(int levelPrice){
		this.levelPrice = levelPrice;
	}

	public float getLevelPrice(){
		return levelPrice;
	}

	public void setLevelId(String levelId){
		this.levelId = levelId;
	}

	public String getLevelId(){
		return levelId;
	}

	public void setLevelLogo(String levelLogo){
		this.levelLogo = levelLogo;
	}

	public String getLevelLogo(){
		return levelLogo;
	}

	public void setTitle(String title){
		this.title = title;
	}

	public String getTitle(){
		return title;
	}

	public void setIsPaidLevel(int isPaidLevel){
		this.isPaidLevel = isPaidLevel;
	}

	public int getIsPaidLevel(){
		return isPaidLevel;
	}

	@Override
 	public String toString(){
		return 
			"VideoListItem{" +
			"subscribed = '" + subscribed + '\'' + 
			",levelPrice = '" + levelPrice + '\'' + 
			",levelId = '" + levelId + '\'' + 
			",levelLogo = '" + levelLogo + '\'' +
			",description = '" + description + '\'' +
			",title = '" + title + '\'' +
			",isPaidLevel = '" + isPaidLevel + '\'' + 
			"}";
		}
}