package com.mindwarriorhack.app;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.mindwarriorhack.app.helper.Constant;
import com.mindwarriorhack.app.manager.PreferenceManager;
import com.stripe.android.Stripe;

public class MindWarriorApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        PreferenceManager.createSharedPreferences(this);

        if(PreferenceManager.getDeviceToken().isEmpty()) {
            FirebaseInstanceId.getInstance().getInstanceId()
                    .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                        @Override
                        public void onComplete(@NonNull Task<InstanceIdResult> task) {
                            if (!task.isSuccessful()) {

                                return;
                            }
                            String token = task.getResult().getToken();
                            PreferenceManager.setDeviceToken(token);
                        }

                    });

        }
    }
}
