package com.mindwarriorhack.app.model.Packages;

import com.google.gson.annotations.SerializedName;
import com.mindwarriorhack.app.model.CommonResponse;

import java.io.Serializable;

public class PackageListItem implements Serializable {

	@SerializedName("levelPrice")
	private float levelPrice;

	@SerializedName("id")
	private String id;

	@SerializedName("levelLogo")
	private String levelLogo;

	@SerializedName("title")
	private String title;

	@SerializedName("isPaidLevel")
	private float isPaidLevel;

	public void setLevelPrice(float levelPrice){
		this.levelPrice = levelPrice;
	}

	public float getLevelPrice(){
		return levelPrice;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
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

	public float getIsPaidLevel(){
		return isPaidLevel;
	}

	@Override
 	public String toString(){
		return 
			"PackageListItem{" +
			"levelPrice = '" + levelPrice + '\'' + 
			",id = '" + id + '\'' + 
			",levelLogo = '" + levelLogo + '\'' + 
			",title = '" + title + '\'' + 
			",isPaidLevel = '" + isPaidLevel + '\'' + 
			"}";
		}
}