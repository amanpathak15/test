package com.mindwarriorhack.app.model.Notification;

import com.google.gson.annotations.SerializedName;

public class BroadcastListItem{

	@SerializedName("msg")
	private String msg;

	@SerializedName("dateTime")
	private String dateTime;

	@SerializedName("broadcastId")
	private String broadcastId;

	@SerializedName("title")
	private String title;

	public void setMsg(String msg){
		this.msg = msg;
	}

	public String getMsg(){
		return msg;
	}

	public void setDateTime(String dateTime){
		this.dateTime = dateTime;
	}

	public String getDateTime(){
		return dateTime;
	}

	public void setBroadcastId(String broadcastId){
		this.broadcastId = broadcastId;
	}

	public String getBroadcastId(){
		return broadcastId;
	}

	public void setTitle(String title){
		this.title = title;
	}

	public String getTitle(){
		return title;
	}

	@Override
 	public String toString(){
		return 
			"BroadcastListItem{" + 
			"msg = '" + msg + '\'' + 
			",dateTime = '" + dateTime + '\'' + 
			",broadcastId = '" + broadcastId + '\'' + 
			",title = '" + title + '\'' + 
			"}";
		}
}