package com.mindwarriorhack.app.presenter;

import android.content.Context;

import com.google.gson.JsonObject;
import com.mindwarriorhack.app.model.Logout.LogoutResponse;
import com.mindwarriorhack.app.services.ApiClient;
import com.mindwarriorhack.app.view.Home.HomeView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomePresenter {
    private Context context;
    private HomeView homeView;
    private ApiClient apiClient;

    public HomePresenter(Context context, HomeView homeView) {
        this.context = context;
        this.homeView = homeView;
        if (this.apiClient == null) {
            this.apiClient = new ApiClient();
        }
    }
    public  void logoutApi(String userId,String deviceToken){
        final JsonObject logoutObject=new JsonObject();
        logoutObject.addProperty("userId",userId);
        logoutObject.addProperty("deviceToken",deviceToken);

        apiClient
                .getApi()
                .logout(logoutObject)
                .enqueue(new Callback<LogoutResponse>() {
                    @Override
                    public void onResponse(Call<LogoutResponse> call, Response<LogoutResponse> response) {
                        if (response.body() != null) {

                            int responseCode = response.body().getResponseCode();

                            if (responseCode == 0) {
                                String successMsg=response.body().getResponseMsg();
                                homeView.onLogoutSuccess(successMsg);
                            } else if(responseCode == 1) {
                                String errorMsg = response.body().getResponseMsg();
                                homeView.onFailure(errorMsg);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<LogoutResponse> call, Throwable t) {
                        homeView.onFailure(t.toString());
                    }
                });
    }

}
