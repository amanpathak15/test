package com.mindwarriorhack.app.model.ContactUs;

import com.google.gson.annotations.SerializedName;

public class ContactUsResponseData{

	@SerializedName("contactUsResponse")
	private ContactUsResponse contactUsResponse;

	@SerializedName("responseCode")
	private int responseCode;

	@SerializedName("responseMsg")
	private String responseMsg;

	public void setContactUsResponse(ContactUsResponse contactUsResponse){
		this.contactUsResponse = contactUsResponse;
	}

	public ContactUsResponse getContactUsResponse(){
		return contactUsResponse;
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
			"ContactUsResponseData{" + 
			"contactUsResponse = '" + contactUsResponse + '\'' +
			",responseCode = '" + responseCode + '\'' + 
			",responseMsg = '" + responseMsg + '\'' + 
			"}";
		}
}