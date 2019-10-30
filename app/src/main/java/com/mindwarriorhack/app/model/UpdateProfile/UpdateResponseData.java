package com.mindwarriorhack.app.model.UpdateProfile;

import com.google.gson.annotations.SerializedName;

public class UpdateResponseData {

	@SerializedName("country")
	private String country;

	@SerializedName("name")
	private String name;

	@SerializedName("userId")
	private String userId;

	@SerializedName("email")
	private String email;

    @SerializedName("mobNo")
	private String mobNo;

    @SerializedName("dob")
	private String dob;

    @SerializedName("occupation")
	private String occupation;

    @SerializedName("gender")
	private String gender;

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getOccupation() {
		return occupation;
	}

	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public String getMobNo() {
        return mobNo;
    }

    public void setMobNo(String mobNo) {
        this.mobNo = mobNo;
    }

    public void setCountry(String country){
		this.country = country;
	}

	public String getCountry(){
		return country;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setUserId(String userId){
		this.userId = userId;
	}

	public String getUserId(){
		return userId;
	}

	public void setEmail(String email){
		this.email = email;
	}

	public String getEmail(){
		return email;
	}

	@Override
 	public String toString(){
		return 
			"UpdateResponse{" +
			"country = '" + country + '\'' + 
			",name = '" + name + '\'' + 
			",userId = '" + userId + '\'' + 
			",email = '" + email + '\'' +
            "mobNo='" + mobNo + '\'' +
			"dob='" + dob + '\'' +
			"occupation='" + occupation + '\'' +
			"}";
		}
}