package com.mindwarriorhack.app.model.ContactUs;

import com.google.gson.annotations.SerializedName;

public class ContactUsRequest{

	@SerializedName("subject")
	private String subject;

	@SerializedName("description")
	private String description;

	@SerializedName("userId")
	private String userId;

	public void setSubject(String subject){
		this.subject = subject;
	}

	public String getSubject(){
		return subject;
	}

	public void setDescription(String description){
		this.description = description;
	}

	public String getDescription(){
		return description;
	}

	public void setUserId(String userId){
		this.userId = userId;
	}

	public String getUserId(){
		return userId;
	}

	@Override
 	public String toString(){
		return 
			"ContactUsRequest{" + 
			"subject = '" + subject + '\'' + 
			",description = '" + description + '\'' + 
			",userId = '" + userId + '\'' + 
			"}";
		}
}