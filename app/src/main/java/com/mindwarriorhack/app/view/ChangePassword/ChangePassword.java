package com.mindwarriorhack.app.view.ChangePassword;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.ligl.android.widget.iosdialog.IOSDialog;
import com.mindwarriorhack.app.R;
import com.mindwarriorhack.app.helper.Constant;
import com.mindwarriorhack.app.presenter.ChangePasswordPresenter;

public class ChangePassword extends AppCompatActivity implements View.OnClickListener, ChangePasswordView {

    EditText existing_password, new_password, confirm_password;
    ImageView back;
    Button btnSave;
    private ProgressBar progressBar;
    ChangePasswordPresenter changePasswordPresenter;
    RelativeLayout toolbarChangePassword;


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
        setContentView(R.layout.activity_change_password);
        initUI();
        initProgressBar();



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

    @SuppressLint("NewApi")
    private void initUI() {
        changePasswordPresenter = new ChangePasswordPresenter(this, this);
        existing_password = findViewById(R.id.existing_password);
        toolbarChangePassword = findViewById(R.id.toolbarChangePassword);
        back = findViewById(R.id.back_btn);
        new_password = findViewById(R.id.new_password);
        confirm_password = findViewById(R.id.confirm_password);
        btnSave = findViewById(R.id.btnSend);
        btnSave.setOnClickListener(this);
        back.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSend:
                if (existing_password.getText().toString().isEmpty() || new_password.getText().toString().isEmpty() || confirm_password.getText().toString().isEmpty()) {
                    customDialog("Required password field(s) are empty");
                    existing_password.setText("");
                    new_password.setText("");
                    confirm_password.setText("");
                } else if (!existing_password.getText().toString().equals(new_password.getText().toString().trim()) && new_password.getText().toString().trim().equals(confirm_password.getText().toString().trim())) {
                    progressBar.setVisibility(View.VISIBLE);
                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    changePasswordPresenter.changePassword(existing_password.getText().toString(), new_password.getText().toString());
                } else if (!new_password.getText().toString().trim().equals(confirm_password.getText().toString().trim())) {
                    customDialog("New and Confirm Password didn't match");
                }
                else if (existing_password.getText().toString().equals(new_password.getText().toString())) {
                    customDialog("Existing and New Password should be different");
                }
                break;

            case R.id.back_btn:
                finish();
                break;
        }
    }

    public void customDialog(String msg) {

        new IOSDialog.Builder(ChangePassword.this)
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    @Override
    public void validateInput(String error, int errorView) {
        if (errorView == Constant.EXISTING_PASSWORD_INVALID) {
            existing_password.setError(error);
            progressBar.setVisibility(View.INVISIBLE);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        } else if (errorView == Constant.NEW_PASSWORD_INVALID) {
            new_password.setError(error);
            progressBar.setVisibility(View.INVISIBLE);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
         else if (errorView == Constant.PASSWORD_NOT_MATCHED) {
            confirm_password.setError(error);
            progressBar.setVisibility(View.INVISIBLE);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }else{
            progressBar.setVisibility(View.INVISIBLE);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            existing_password.setText("");
            confirm_password.setText("");
            new_password.setText("");
        }
    }

    @Override
    public void onSuccess(String msg, String errorMsg) {
        //customDialog(msg + "successfully");
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        progressBar.setVisibility(View.INVISIBLE);
        if (!msg.isEmpty()) {
            customDialog(msg);
        } else {
            existing_password.setText("");
            confirm_password.setText("");
            new_password.setText("");
            customDialog(errorMsg);
        }
    }

    @Override
    public void onError(String error) {
        progressBar.setVisibility(View.INVISIBLE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        existing_password.setText("");
        confirm_password.setText("");
        new_password.setText("");
        customDialog(error);
    }

    private void initProgressBar() {
        progressBar = new ProgressBar(ChangePassword.this, null, android.R.attr.progressBarStyle);
        progressBar.setIndeterminate(true);

        RelativeLayout relativeLayout = new RelativeLayout(ChangePassword.this);
        relativeLayout.setGravity(Gravity.CENTER);
        relativeLayout.addView(progressBar);

        RelativeLayout.LayoutParams params = new
                RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        progressBar.setVisibility(View.INVISIBLE);

        this.addContentView(relativeLayout, params);
    }
}
