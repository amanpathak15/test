package com.mindwarriorhack.app.view.fragments.HomeFragment;

import android.content.Context;

import com.mindwarriorhack.app.model.Levels.LevelResponse;
import com.mindwarriorhack.app.view.adapters.LevelAdapter;

import retrofit2.Response;

public interface HomeFragmentView {

    void onSuccess(Response<LevelResponse> response);
    void onFailure(String error);
    void onInternetFailure(String error);
    void onPaymentUpdateStatusFailure();
    void onPaymentUpdateStatusSuccess();
}
