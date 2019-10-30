package com.mindwarriorhack.app.presenter;

import android.content.Context;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mindwarriorhack.app.services.ApiClient;
import com.mindwarriorhack.app.view.Payment.PaymentView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentPresenter {

    private ApiClient apiClient;
    private Context context;
    private PaymentView paymentView;

    public PaymentPresenter(Context context, PaymentView paymentView) {
        this.context = context;
        this.paymentView = paymentView;
        if (this.apiClient == null) {
            apiClient = new ApiClient();
        }
    }



    public void callCheckOut(String userId, String[] levelIds, float totalPrice) {

        JsonArray array = new JsonArray();
        for (int i=0; i< levelIds.length; i++){
            array.add(Integer.parseInt(levelIds[i]));
        }

        JsonObject checkoutObject = new JsonObject();
        checkoutObject.addProperty("userId", userId);
        checkoutObject.add("levelIds", array);
        checkoutObject.addProperty("totalPrice",totalPrice);

        apiClient
                .getApi()
                .checkoutCart(checkoutObject)
                .enqueue(new Callback<JsonElement>() {
                    @Override
                    public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                        if (response.body() != null) {
                            int responseCode = response.body().getAsJsonObject().get("responseCode").getAsInt();

                            if (responseCode == 0) {
                                String successMsg = response.body().getAsJsonObject().get("responseMsg").getAsString();
                                paymentView.onCheckOutSuccess(response.body().toString(), response);
                            } else if (responseCode == 1) {
                                String errorMsg = response.body().getAsJsonObject().get("responseMsg").getAsString();
                                paymentView.onCheckOutFailure(errorMsg);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonElement> call, Throwable t) {
                        paymentView.onCheckOutFailure(t.toString());
                    }
                });
    }
}
