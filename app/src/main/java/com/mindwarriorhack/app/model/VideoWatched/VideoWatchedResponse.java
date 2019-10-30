package com.mindwarriorhack.app.model.VideoWatched;


import com.google.android.gms.common.internal.service.Common;
import com.google.gson.annotations.SerializedName;
import com.mindwarriorhack.app.model.CommonResponse;


public class VideoWatchedResponse extends CommonResponse {

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
			"VideoWatchedResponse{" + 
			"responseData = '" + responseData + '\'' +
			"}";
		}
}