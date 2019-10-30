package com.mindwarriorhack.app.model;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;


public class CommonResponse {

	@SerializedName("responseMsg")
	private String responseMsg;

	@SerializedName("responseCode")
	private int responseCode;

	public void setResponseMsg(String responseMsg){
		this.responseMsg = responseMsg;
	}

	public String getResponseMsg(){
		return responseMsg;
	}

	public void setResponseCode(int responseCode){
		this.responseCode = responseCode;
	}

	public int getResponseCode(){
		return responseCode;
	}

	@NonNull
	@Override
 	public String toString(){
		return 
			"CommonResponse{" +
			"responseMsg = '" + responseMsg + '\'' +
			",responseCode = '" + responseCode + '\'' + 
			"}";
		}
}