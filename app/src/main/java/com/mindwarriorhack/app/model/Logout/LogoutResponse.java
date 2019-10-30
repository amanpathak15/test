package com.mindwarriorhack.app.model.Logout;


import com.google.gson.annotations.SerializedName;
import com.mindwarriorhack.app.helper.Constant;
import com.mindwarriorhack.app.model.CommonResponse;

public class LogoutResponse extends CommonResponse {

	@SerializedName("responseData")
	private ResponseData responseData;


	public void setResponseData(ResponseData responseData){
		this.responseData = responseData;
	}

	public ResponseData getResponseData(){
		return responseData;
	}


	@Override
 	public String toString(){
		return 
			"LogoutResponse{" + 
			"responseData = '" + responseData + '\'' +
			"}";
		}
}