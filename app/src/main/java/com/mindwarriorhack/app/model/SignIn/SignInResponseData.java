package com.mindwarriorhack.app.model.SignIn;


import com.google.gson.annotations.SerializedName;


public class SignInResponseData {

	@SerializedName("country")
	private String country;

	@SerializedName("mobNo")
	private String mobNo;

	@SerializedName("profilePic")
	private String profilePic;

	@SerializedName("name")
	private String name;

	@SerializedName("userType")
	private String userType;

	@SerializedName("userId")
	private String userId;

	@SerializedName("email")
	private String email;

	@SerializedName("notifyMe")
	private int notifyMe;

	@SerializedName("dateofBirth")
	private String dateofBirth;

	@SerializedName("occupation")
	private String occupation;

	@SerializedName("gender")
	private String gender;

	@SerializedName("timeZone")
	private String timeZone;

	public String getDateofBirth() {
		return dateofBirth;
	}

	public void setDateofBirth(String dateofBirth) {
		this.dateofBirth = dateofBirth;
	}

	public String getOccupation() {
		return occupation;
	}

	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}

	public void setCountry(String country){
		this.country = country;
	}

	public String getCountry(){
		return country;
	}

	public void setMobNo(String mobNo){
		this.mobNo = mobNo;
	}

	public String getMobNo(){
		return mobNo;
	}

	public void setProfilePic(String profilePic){
		this.profilePic = profilePic;
	}

	public String getProfilePic(){
		return profilePic;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setUserType(String userType){
		this.userType = userType;
	}

	public String getUserType(){
		return userType;
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

	public int getNotifyMe() {
		return notifyMe;
	}

	public void setNotifyMe(int notifyMe) {
		this.notifyMe = notifyMe;
	}

	@Override
 	public String toString(){
		return 
			"SignInResponseData{" +
			"confirm_password = '" + country + '\'' +
			",mobNo = '" + mobNo + '\'' + 
			",profilePic = '" + profilePic + '\'' + 
			",name = '" + name + '\'' + 
			",userType = '" + userType + '\'' + 
			",userId = '" + userId + '\'' + 
			",email = '" + email + '\'' +
			",notifyMe = '" + notifyMe + '\'' +
			"}";
		}
}