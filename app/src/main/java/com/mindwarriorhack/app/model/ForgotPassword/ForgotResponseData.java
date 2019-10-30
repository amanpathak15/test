package com.mindwarriorhack.app.model.ForgotPassword;

import com.google.gson.annotations.SerializedName;

public class ForgotResponseData{

	@SerializedName("ForgotResponse")
	private ForgotResponse ForgotResponse;

	@SerializedName("responseCode")
	private int responseCode;

	@SerializedName("responseMsg")
	private String responseMsg;

	public void setForgotResponse(ForgotResponse forgotResponse){
		this.ForgotResponse = forgotResponse;
	}

	public ForgotResponse getForgotResponse(){
		return ForgotResponse;
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
			"ForgotResponseData{" + 
			"ForgotResponse = '" + ForgotResponse + '\'' +
			",responseCode = '" + responseCode + '\'' + 
			",responseMsg = '" + responseMsg + '\'' + 
			"}";
		}
}