package com.mindwarriorhack.app.presenter;

import android.content.Context;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mindwarriorhack.app.helper.Constant;
import com.mindwarriorhack.app.manager.PreferenceManager;
import com.mindwarriorhack.app.services.ApiClient;
import com.mindwarriorhack.app.view.PaymentStatusActivity.paymentStatusView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentStatusPresenter {
    Context context;
    private paymentStatusView paymentStatusView;
    private ApiClient apiClient;

    public PaymentStatusPresenter(paymentStatusView paymentStatusView, Context context) {
        this.paymentStatusView = paymentStatusView;
        this.context = context;
        if (this.apiClient == null) {
            this.apiClient = new ApiClient();
        }
    }

    public void updatePaymentStatus(final int orderId, final String paymentStatus) {

            JsonObject paymentStatusObject = new JsonObject();
            paymentStatusObject.addProperty("userId", PreferenceManager.getString(Constant.USER_ID));
            paymentStatusObject.addProperty("device", "Android");
            paymentStatusObject.addProperty("orderId", orderId);
            paymentStatusObject.addProperty("paymentGateway", "Stripe");
            paymentStatusObject.addProperty("paymentGatewayResponse", "");
            paymentStatusObject.addProperty("paymentMode", "");
            paymentStatusObject.addProperty("paymentstatus", paymentStatus);

            apiClient
                    .getApi()
                    .updatePaymentStatus(paymentStatusObject)
                    .enqueue(new Callback<JsonElement>() {
                        @Override
                        public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                            if(!response.isSuccessful()){
                                paymentStatusView.onPaymentUpdateStatusFailure("");
                                PreferenceManager.setString(Constant.PENDING_PAYMENT_STATUS_ORDER_ID,orderId + "");
                                PreferenceManager.setString(Constant.PENDING_PAYMENT_STATUS_ORDER_STATUS,paymentStatus);
                            }

                            if (response.body() != null) {
                                int code = response.body().getAsJsonObject().get("responseCode").getAsInt();
                                if (code == 0) {
                                    String successMsg = response.body().getAsJsonObject().get("responseMsg").getAsString();
                                    if(paymentStatus.equals("paid")){
                                        PreferenceManager.setString(Constant.USER_TYPE,"Premium");
                                    }
                                    paymentStatusView.onPaymentUpdateStatusSuccess(response);
                                } else if (code == 1) {
                                    String errorMsg = response.body().getAsJsonObject().get("responseMsg").getAsString();
                                    paymentStatusView.onPaymentUpdateStatusFailure(errorMsg);
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<JsonElement> call, Throwable t) {
                            paymentStatusView.onPaymentUpdateStatusFailure(t.toString());
                            PreferenceManager.setString(Constant.PENDING_PAYMENT_STATUS_ORDER_ID,orderId + "");
                            PreferenceManager.setString(Constant.PENDING_PAYMENT_STATUS_ORDER_STATUS,paymentStatus);
                        }
                    });
        }

}