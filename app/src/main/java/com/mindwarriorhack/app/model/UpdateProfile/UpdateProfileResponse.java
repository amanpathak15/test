package com.mindwarriorhack.app.model.UpdateProfile;

import com.google.gson.annotations.SerializedName;
import com.mindwarriorhack.app.model.CommonResponse;

public class UpdateProfileResponse extends CommonResponse {

	@SerializedName("responseData")
	private UpdateResponseData updateResponse;

    public UpdateResponseData getUpdateResponse() {
        return updateResponse;
    }

    public void setUpdateResponse(UpdateResponseData updateResponse) {
        this.updateResponse = updateResponse;
    }
}