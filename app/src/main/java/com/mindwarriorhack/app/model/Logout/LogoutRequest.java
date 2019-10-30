package com.mindwarriorhack.app.model.Logout;


import com.google.gson.annotations.SerializedName;


public class LogoutRequest {

	@SerializedName("userId")
	private String userId;

	@SerializedName("deviceToken")
	private String deviceToken;

	public void setUserId(String userId){
		this.userId = userId;
	}

	public String getUserId(){
		return userId;
	}

	public void setDeviceToken(String deviceToken){
		this.deviceToken = deviceToken;
	}

	public String getDeviceToken(){
		return deviceToken;
	}

	@Override
 	public String toString(){
		return 
			"LogoutRequest{" +
			"userId = '" + userId + '\'' + 
			",deviceToken = '" + deviceToken + '\'' + 
			"}";
		}
}