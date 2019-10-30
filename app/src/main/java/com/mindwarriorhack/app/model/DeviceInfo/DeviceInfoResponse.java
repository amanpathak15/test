package com.mindwarriorhack.app.model.DeviceInfo;


import com.google.gson.annotations.SerializedName;

public class DeviceInfoResponse{

	@SerializedName("responseData")
	private ResponseData responseData;

	@SerializedName("responseCode")
	private int responseCode;

	@SerializedName("responseMsg")
	private String responseMsg;

	public void setResponseData(ResponseData responseData){
		this.responseData = responseData;
	}

	public ResponseData getResponseData(){
		return responseData;
	}

	public void setResponseCode(int responseCode){
		this.responseCode = responseCode;
	}

	public int getResponseCode(){
		return responseCode;
	}

	public void setResponseMsg(String responseMsg){
		this.responseMsg = responseMsg;
	}

	public String getResponseMsg(){
		return responseMsg;
	}

	@Override
 	public String toString(){
		return 
			"DeviceInfoResponse{" + 
			"responseData = '" + responseData + '\'' + 
			",responseCode = '" + responseCode + '\'' + 
			",responseMsg = '" + responseMsg + '\'' + 
			"}";
		}
}