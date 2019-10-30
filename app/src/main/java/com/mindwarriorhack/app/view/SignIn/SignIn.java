package com.mindwarriorhack.app.view.SignIn;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Build;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ligl.android.widget.iosdialog.IOSDialog;
import com.mindwarriorhack.app.BuildConfig;
import com.mindwarriorhack.app.R;
import com.mindwarriorhack.app.helper.Constant;
import com.mindwarriorhack.app.manager.PreferenceManager;
import com.mindwarriorhack.app.model.SignIn.SignInResponseData;
import com.mindwarriorhack.app.presenter.SignInPresenter;
import com.mindwarriorhack.app.troubleshoot.TroubleShootActivity;
import com.mindwarriorhack.app.view.ForgotPassword.ForgotPassword;
import com.mindwarriorhack.app.view.Home.HomeActivity;
import com.mindwarriorhack.app.view.SignUp.SignUp;

import java.util.Calendar;
import java.util.TimeZone;

public class SignIn extends AppCompatActivity implements View.OnClickListener, SignInView {

    private ProgressBar progressBar;
    private EditText signInEmail, signInPassword;
    private SignInPresenter signInPresenter;

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


    @SuppressLint("ObsoleteSdkInt")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        if (PreferenceManager.isUserLogin()) {
            startActivity(new Intent(this, HomeActivity.class));
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            finish();
        }

        setContentView(R.layout.activity_sign_in);
        signInPresenter = new SignInPresenter(SignIn.this, this);
        initProgressBar();
        initUI();

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
    }

    private void initUI() {
        signInPresenter.deviceInfoApi();

        RelativeLayout toolbarSignIn = findViewById(R.id.toolbarSignIn);

        TextView forgotPassword = findViewById(R.id.forgotPassword);
        TextView createAccount = findViewById(R.id.createAccount);

        //TextView troubleShootProblems=findViewById(R.id.troubleShootProblems);

//        troubleShootProblems.setOnClickListener(this);

        Button signInBtn = findViewById(R.id.signInBtn);

        signInEmail = findViewById(R.id.signInEmail);
        //signInEmail.setText("vrushabhchitalia@gmail.com");
        signInPassword = findViewById(R.id.signInPassword);
        //signInPassword.setText("123456");
        createAccount.setOnClickListener(this);
        forgotPassword.setOnClickListener(this);
        signInBtn.setOnClickListener(this);

        signInEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    if(signInPresenter.validateInputFieldsIndividual(signInEmail.getText().toString().trim(),Constant.EMAIL_VALIDATION) == Constant.EMAIL_EMPTY){
                        signInEmail.setError("Please enter email");
                    }
                    else if(signInPresenter.validateInputFieldsIndividual(signInEmail.getText().toString().trim(),Constant.EMAIL_VALIDATION) == Constant.EMAIL_INVALID){
                        signInEmail.setError("Enter valid email");
                    }
                }
            }
        });

        signInPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    if(signInPresenter.validateInputFieldsIndividual(signInPassword.getText().toString(),Constant.PASSWORD_VALIDATION) == Constant.PASSWORD_EMPTY){
                        signInPassword.setError("Please enter password");
                    }
                    else if(signInPresenter.validateInputFieldsIndividual(signInEmail.getText().toString().trim(),Constant.PASSWORD_VALIDATION) == Constant.EXISTING_PASSWORD_INVALID){
                        signInPassword.setError("Enter valid password");
                    }
                }
            }
        });

    }

    private void getUserInput() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        progressBar.setVisibility(View.VISIBLE);
        String email = signInEmail.getText().toString().trim();
        String password = signInPassword.getText().toString().trim();
        String deviceToken= PreferenceManager.getDeviceToken();
        Log.d("token",deviceToken);
        signInPresenter.SignIn(email, password,deviceToken);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.forgotPassword:
                startActivity(new Intent(SignIn.this, ForgotPassword.class));
                break;
            case R.id.createAccount:
                startActivity(new Intent(SignIn.this, SignUp.class));
                break;
            case R.id.signInBtn:
                getUserInput();
                break;
/*            case R.id.troubleShootProblems:
                startActivity(new Intent(SignIn.this, TroubleShootActivity.class));
                break;*/
        }

    }


    @Override
    public void signInValidations(String error, int errorView) {
        if (errorView == Constant.EMAIL_INVALID || errorView == Constant.EMAIL_EMPTY) {
            progressBar.setVisibility(View.GONE);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            signInEmail.setError(error);
        } else if (errorView == Constant.EXISTING_PASSWORD_INVALID || errorView == Constant.PASSWORD_EMPTY) {
            progressBar.setVisibility(View.GONE);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            signInPassword.setError(error);
        }
    }

    @Override
    public void signInSuccess(String msg, SignInResponseData signInResponseData) {
        Log.d("response",signInResponseData.toString());
        PreferenceManager.setString(Constant.USER_NAME, signInResponseData.getName());
        PreferenceManager.setString(Constant.USER_ID, signInResponseData.getUserId());
        PreferenceManager.setString(Constant.USER_COUNTRY, signInResponseData.getCountry());
        PreferenceManager.setString(Constant.USER_EMAIL, signInResponseData.getEmail());
        PreferenceManager.setString(Constant.PROFILE_PIC, signInResponseData.getProfilePic());
        PreferenceManager.setString(Constant.USER_TYPE, signInResponseData.getUserType());
        PreferenceManager.setString(Constant.USER_MOBILE, signInResponseData.getMobNo());
        PreferenceManager.setString(Constant.USER_DOB, signInResponseData.getDateofBirth());
        PreferenceManager.setString(Constant.USER_OCCUPATION, signInResponseData.getOccupation());
        PreferenceManager.setString(Constant.USER_TIMEZONE, signInResponseData.getTimeZone());
        PreferenceManager.setString(Constant.USER_GENDER, signInResponseData.getGender());

        PreferenceManager.setIsUserLogin(true);
        if(signInResponseData.getEmail().isEmpty()){
            customDialog("Please enter email");
        }
        if (signInResponseData.getNotifyMe() == 1){
            PreferenceManager.setIsNotificationsEnabled(true);
        }else {
            PreferenceManager.setIsNotificationsEnabled(false);
        }
        progressBar.setVisibility(View.INVISIBLE);
        startActivity(new Intent(SignIn.this, HomeActivity.class));
        finish();
    }

    @Override
    public void signInError(String error) {
        progressBar.setVisibility(View.INVISIBLE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        if(signInEmail.length()==0){
            customDialog("Please enter Email!");
        }else if(signInPassword.length()==0){
            customDialog("Please enter Password!");
        }else if(!error.matches(this.getResources().getString(R.string.regex_emailAddress))){
            customDialog("Invalid Email or Password!");
        }
        signInEmail.setText("");
        signInPassword.setText("");
    }

    @Override
    public void deviceInfoSuccess() {
        //Toast.makeText(this,"Success",Toast.LENGTH_SHORT).show();
        initData();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void deviceInfoError() {
        //Toast.makeText(this,"failed",Toast.LENGTH_SHORT).show();
        if (PreferenceManager.isUserLogin()) {
            startActivity(new Intent(SignIn.this, HomeActivity.class));
            finish();
        }
    }

    public void customDialog(String alert) {
        new IOSDialog.Builder(SignIn.this)
                .setMessage(alert)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    private void initProgressBar() {
        progressBar = new ProgressBar(SignIn.this, null, android.R.attr.progressBarStyle);
        progressBar.setIndeterminate(true);

        //Changes the color of progressbar itself and not background
        progressBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(getApplicationContext(), android.R.color.white), PorterDuff.Mode.SRC_IN);

        RelativeLayout relativeLayout = new RelativeLayout(SignIn.this);
        relativeLayout.setGravity(Gravity.CENTER);
        relativeLayout.addView(progressBar);

        RelativeLayout.LayoutParams params = new
                RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        progressBar.setVisibility(View.INVISIBLE);

        this.addContentView(relativeLayout, params);
    }

    private void initData(){
        final float appVersion = Float.valueOf(BuildConfig.VERSION_NAME);
        final float CompulsoryVersion = Float.valueOf(PreferenceManager.getString(Constant.LAST_COMPULSORY_VERSION));

        if (!(appVersion >= CompulsoryVersion)) {
            new IOSDialog.Builder(SignIn.this)
                    .setTitle("Requires Update")
                    .setMessage("A new update is available. Click on UPDATE to enhance your experience with Mind Warrior")
                    .setCancelable(false)
                    .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String link = PreferenceManager.getString(Constant.DOWNLOAD_PATH);
                            Intent downloadLatestVersionIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                            PackageManager manager = SignIn.this.getPackageManager();
                            if (downloadLatestVersionIntent.resolveActivity(manager) != null) {
                                startActivity(downloadLatestVersionIntent);
                            } else{
                                Log.d("Download Error", "No Intent available to handle action");
                            }
                        }
                    }).show();
        }
    }
}
