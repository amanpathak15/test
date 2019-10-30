package com.mindwarriorhack.app.view.PaymentStatusActivity;

import com.google.gson.JsonElement;
import com.mindwarriorhack.app.model.Levels.LevelResponse;

import retrofit2.Response;

public interface paymentStatusView {

    void onPaymentUpdateStatusSuccess(Response<JsonElement> response);
    void onPaymentUpdateStatusFailure(String error);
}
