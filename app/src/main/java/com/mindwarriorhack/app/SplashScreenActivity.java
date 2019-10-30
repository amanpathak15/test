package com.mindwarriorhack.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.mindwarriorhack.app.helper.Constant;
import com.mindwarriorhack.app.manager.PreferenceManager;
import com.mindwarriorhack.app.view.Home.HomeActivity;
import com.mindwarriorhack.app.view.SignIn.SignIn;
import com.mindwarriorhack.app.view.SignUp.SignUp;

public class SplashScreenActivity extends AppCompatActivity {

    private static final String LOG_TAG = "AppUpgrade";

    public static void setWindowFlag(Activity activity, final int bits, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        isNetworkAvailable(this);
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        //make fully Android Transparent Status bar
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(PreferenceManager.isUserLogin()){
                    startActivity(new Intent(SplashScreenActivity.this,HomeActivity.class));
                } else{

                    if(PreferenceManager.getString(Constant.IS_APP_FIRSTTIME_TAG).equals("")){
                        startActivity(new Intent(SplashScreenActivity.this, WalkthroughScreens.class));
                    }
                    else
                    {
                        startActivity(new Intent(SplashScreenActivity.this, SignIn.class));
                    }


                }
                // startActivity(new Intent(SplashScreenActivity.this,WalkthroughScreens.class));

                finish();
            }
        },3000);
    }

    private void isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    Log.v(LOG_TAG, String.valueOf(i));
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        Log.v(LOG_TAG, "connected!");
                        return;
                    }
                }
            }
        }
    }
}
