package com.mindwarriorhack.app.presenter;

import android.content.Context;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mindwarriorhack.app.model.Packages.PackageListItem;
import com.mindwarriorhack.app.model.Packages.PackagesResponse;
import com.mindwarriorhack.app.services.ApiClient;
import com.mindwarriorhack.app.view.Purchase.PurchaseView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PurchasePresenter {

    ApiClient apiClient;
    Context context;
    PurchaseView purchaseView;

    public PurchasePresenter(Context context, PurchaseView purchaseView) {
        this.context = context;
        this.purchaseView = purchaseView;
        if (this.apiClient == null) {
            apiClient = new ApiClient();
        }
    }



    public void getLevelPackages(String userId, List<PackageListItem> packageList) {

        JsonArray array = new JsonArray();
        for (int i = 0; i<packageList.size(); i++){
            array.add(Integer.parseInt(packageList.get(i).getId()));
        }

        JsonObject packagesObject = new JsonObject();
        packagesObject.addProperty("userId", userId);
        packagesObject.add("levelSelectedId", array);

        apiClient
                .getApi()
                .getPackages(packagesObject)
                .enqueue(new Callback<PackagesResponse>() {
                    @Override
                    public void onResponse(Call<PackagesResponse> call, Response<PackagesResponse> response) {
                        if (response.body() != null) {
                            int responseCode = response.body().getResponseCode();

                            if (responseCode == 0) {
                                String successMsg = response.body().getResponseMsg();
                                purchaseView.onSuccess(response.body().getResponseData().getLevelsList());
                            } else if (responseCode == 1) {
                                String errorMsg = response.body().getResponseMsg();
                                purchaseView.onFailure(errorMsg);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<PackagesResponse> call, Throwable t) {
                        purchaseView.onFailure(t.toString());
                    }
                });
    }
}
