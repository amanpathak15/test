package com.mindwarriorhack.app.model.Notification;

import java.util.Calendar;
import java.util.List;
import com.google.gson.annotations.SerializedName;
import com.mindwarriorhack.app.model.CommonResponse;


public class ResponseData extends CommonResponse {

	@SerializedName("broadcastList")
	private List<BroadcastListItem> broadcastList;

	public void setBroadcastList(List<BroadcastListItem> broadcastList){
		this.broadcastList = broadcastList;
	}

	public List<BroadcastListItem> getBroadcastList(){
		return broadcastList;
	}

	@Override
 	public String toString(){
		return 
			"ResponseData{" + 
			"broadcastList = '" + broadcastList + '\'' + 
			"}";
		}
}