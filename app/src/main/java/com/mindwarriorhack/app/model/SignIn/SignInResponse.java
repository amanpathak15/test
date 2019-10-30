package com.mindwarriorhack.app.model.SignIn;


import com.google.gson.annotations.SerializedName;
import com.mindwarriorhack.app.model.CommonResponse;


public class SignInResponse extends CommonResponse {

	@SerializedName("responseData")
	private SignInResponseData signInResponseData;

	public void setSignInResponseData(SignInResponseData signInResponseData){
		this.signInResponseData = signInResponseData;
	}

	public SignInResponseData getSignInResponseData(){
		return signInResponseData;
	}

	@Override
 	public String toString(){
		return 
			"SignInResponse{" + 
			"responseData = '" + signInResponseData + '\'' +
			"}";
		}
}