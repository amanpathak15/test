package com.mindwarriorhack.app.presenter;

import android.content.Context;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mindwarriorhack.app.helper.Constant;
import com.mindwarriorhack.app.helper.Utils;
import com.mindwarriorhack.app.manager.PreferenceManager;
import com.mindwarriorhack.app.model.Levels.LevelResponse;
import com.mindwarriorhack.app.model.Levels.LevelsListItem;
import com.mindwarriorhack.app.services.ApiClient;
import com.mindwarriorhack.app.view.fragments.HomeFragment.HomeFragmentView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragmentPresenter {
    private Context context;
    private HomeFragmentView homeFragmentView;
    private ApiClient apiClient;

    public HomeFragmentPresenter(Context context, HomeFragmentView homeFragmentView) {
        this.context = context;
        this.homeFragmentView = homeFragmentView;
        if (this.apiClient == null) {
            this.apiClient = new ApiClient();
        }
    }

    public void getLevels() {
        JsonObject levelObject = new JsonObject();
        levelObject.addProperty("userId", PreferenceManager.getString(Constant.USER_ID));

        apiClient
                .getApi()
                .getLevels(levelObject)
                .enqueue(new Callback<LevelResponse>() {
                    @Override
                    public void onResponse(Call<LevelResponse> call, Response<LevelResponse> response) {
                        if (response.body() != null) {

                             int responseCode = response.body().getResponseCode();

                            if (responseCode == 0) {
                                String successMsg = response.body().getResponseMsg();
                                homeFragmentView.onSuccess(response);
                            }
                            else if (responseCode == 1) {
                                String errorMsg = response.body().getResponseMsg();
                                homeFragmentView.onFailure(errorMsg);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<LevelResponse> call, Throwable t) {

                        if(Utils.internetConnectionAvailable(2000)){
                            homeFragmentView.onFailure(t.toString());
                        }
                        else
                        {
                            homeFragmentView.onInternetFailure("Please check your internet connection");
                        }
                    }
                });
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
                            homeFragmentView.onPaymentUpdateStatusFailure();
                            PreferenceManager.setString(Constant.PENDING_PAYMENT_STATUS_ORDER_ID,orderId + "");
                            PreferenceManager.setString(Constant.PENDING_PAYMENT_STATUS_ORDER_STATUS,paymentStatus);
                        }

                        if (response.body() != null) {
                            int code = response.body().getAsJsonObject().get("responseCode").getAsInt();
                            if (code == 0) {
                                String successMsg = response.body().getAsJsonObject().get("responseMsg").getAsString();
                                PreferenceManager.setString(Constant.PENDING_PAYMENT_STATUS_ORDER_STATUS,"");
                                PreferenceManager.setString(Constant.PENDING_PAYMENT_STATUS_ORDER_ID,"");
                                if(paymentStatus.equals("paid")){
                                    PreferenceManager.setString(Constant.USER_TYPE,"Premium");
                                }
                                homeFragmentView.onPaymentUpdateStatusSuccess();
                            } else if (code == 1) {
                                String errorMsg = response.body().getAsJsonObject().get("responseMsg").getAsString();
                                homeFragmentView.onPaymentUpdateStatusFailure();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonElement> call, Throwable t) {
                        homeFragmentView.onPaymentUpdateStatusFailure();
                        PreferenceManager.setString(Constant.PENDING_PAYMENT_STATUS_ORDER_ID,orderId + "");
                        PreferenceManager.setString(Constant.PENDING_PAYMENT_STATUS_ORDER_STATUS,paymentStatus);
                    }
                });
    }




}
