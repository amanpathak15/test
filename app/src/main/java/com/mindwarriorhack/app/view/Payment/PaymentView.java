package com.mindwarriorhack.app.view.Payment;

import com.google.gson.JsonElement;

import retrofit2.Response;

public interface PaymentView {
    void onCheckOutSuccess(String msg, Response<JsonElement> response);
    void onCheckOutFailure(String error);
}
