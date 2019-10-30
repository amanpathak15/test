package com.mindwarriorhack.app.model.DeviceInfo;


import com.google.gson.annotations.SerializedName;


public class ResponseData{

	@SerializedName("downloadPath")
	private String downloadPath;

	@SerializedName("deviceId")
	private int deviceId;

	@SerializedName("lastCompulsoryVersion")
	private String lastCompulsoryVersion;

	@SerializedName("currentVersion")
	private String currentVersion;

	public void setDownloadPath(String downloadPath){
		this.downloadPath = downloadPath;
	}

	public String getDownloadPath(){
		return downloadPath;
	}

	public void setDeviceId(int deviceId){
		this.deviceId = deviceId;
	}

	public int getDeviceId(){
		return deviceId;
	}

	public void setLastCompulsoryVersion(String lastCompulsoryVersion){
		this.lastCompulsoryVersion = lastCompulsoryVersion;
	}

	public String getLastCompulsoryVersion(){
		return lastCompulsoryVersion;
	}

	public void setCurrentVersion(String currentVersion){
		this.currentVersion = currentVersion;
	}

	public String getCurrentVersion(){
		return currentVersion;
	}

	@Override
 	public String toString(){
		return 
			"ResponseData{" + 
			"downloadPath = '" + downloadPath + '\'' + 
			",deviceId = '" + deviceId + '\'' + 
			",lastCompulsoryVersion = '" + lastCompulsoryVersion + '\'' + 
			",currentVersion = '" + currentVersion + '\'' + 
			"}";
		}
}