package com.mindwarriorhack.app.view.SignUp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ligl.android.widget.iosdialog.IOSDialog;
import com.mindwarriorhack.app.R;
import com.mindwarriorhack.app.helper.Constant;
import com.mindwarriorhack.app.presenter.SignUpPresenter;
import com.mindwarriorhack.app.view.Popups.CountrySelectionDialog;
import com.mindwarriorhack.app.view.Popups.TermsConditionPopup;
import com.mindwarriorhack.app.view.termsAndConditions.TermsAndConditions;


public class SignUp extends AppCompatActivity implements View.OnClickListener, SignUpView, CountrySelectionDialog.getSelectedCountryInterface {

    private static final String DEEP_LINK_URL = "https://mindwarriorhacks.app/test/verify-email/NDE=";
    private EditText userEditName, userEmailAddress, userPassword, userConfirmPassword;
    private SignUpPresenter signUpPresenter;
    private TextView countrySpinner, checkbox_txt, terms_and_conditions;
    private ProgressBar progressBarSignUp;
    private CheckBox checkbox_tc;


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
        setContentView(R.layout.activity_sign_up);

        initUI();


        signUpPresenter = new SignUpPresenter(this, SignUp.this);
    }

    @SuppressLint("RestrictedApi")
    private void initUI() {


        progressBarSignUp = findViewById(R.id.progressBar);

        RelativeLayout toolbarSignUp = findViewById(R.id.toolbarChangePassword);

        ImageView back = findViewById(R.id.backSignUp);

        TextView alreadyCreated = findViewById(R.id.alreadyCreated);

        ImageView profImg = findViewById(R.id.profImg);

        userEditName = findViewById(R.id.userEditName);
        userEmailAddress = findViewById(R.id.userEmailAddress);
        userPassword = findViewById(R.id.userPassword);
        userConfirmPassword = findViewById(R.id.userConfirmPassword);
        checkbox_tc = findViewById(R.id.checkbox_tc);
        checkbox_txt = findViewById(R.id.checkbox_txt);
        terms_and_conditions = findViewById(R.id.terms_and_conditions);
        terms_and_conditions.setOnClickListener(this);


        countrySpinner = findViewById(R.id.countrySpinner);

        countrySpinner.setOnClickListener(this);

        Button btnSignUp = findViewById(R.id.btnSignUp);

        btnSignUp.setOnClickListener(this);

        alreadyCreated.setOnClickListener(this);

        back.setOnClickListener(this);
    }

    private void getUserInput() {
        String name = userEditName.getText().toString().trim();
        String mail = userEmailAddress.getText().toString().trim();
        String password = userPassword.getText().toString().trim();
        String confirmPassword = userConfirmPassword.getText().toString().trim();
        String country = countrySpinner.getText().toString().trim();
        if (checkbox_tc.isChecked()) {
            signUpPresenter.SignUp(name, mail, country, password, confirmPassword);
        }else {
            progressBarSignUp.setVisibility(View.INVISIBLE);
            failureCustomDialog("Please read terms and conditions");
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btnSignUp:
                progressBarSignUp.setVisibility(View.VISIBLE);
                getUserInput();
                break;
            case R.id.alreadyCreated:
                finish();
                break;
            case R.id.backSignUp:
                finish();
                break;

            case R.id.countrySpinner:
                FragmentManager fm = getSupportFragmentManager();
                CountrySelectionDialog dialog = new CountrySelectionDialog();
                dialog.setRetainInstance(true);
                dialog.show(fm, "countryFragment");
                break;

            case R.id.terms_and_conditions:
                startActivity(new Intent(SignUp.this, TermsAndConditions.class));
                break;
        }
    }

    @Override
    public void signUpSuccess(String msg, String errorMsg) {
        if (!msg.equals("")) {
            //Toast.makeText(SignUp.this, msg, Toast.LENGTH_SHORT).show();
            progressBarSignUp.setVisibility(View.GONE);
            successCustomDialog(msg);
        } else if (!errorMsg.equals("")) {
            //Toast.makeText(SignUp.this, errorMsg, Toast.LENGTH_SHORT).show();
            progressBarSignUp.setVisibility(View.GONE);
            failureCustomDialog(errorMsg);
        }
    }

    @Override
    public void validateInputFields(String error, int errorView) {
        progressBarSignUp.setVisibility(View.GONE);
        if (errorView == Constant.NAME_INVALID) {
            userEditName.setError(error);
        } else if (errorView == Constant.EMAIL_INVALID) {
            userEmailAddress.setError(error);
        } else if (errorView == Constant.EXISTING_PASSWORD_INVALID) {
            userPassword.setError(error);
        } else if (errorView == Constant.CONFIRM_PASSWORD_INVALID) {
            userConfirmPassword.setError(error);
        } else if (errorView == Constant.COUNTRY_INVALID) {
            countrySpinner.setError(error);
        } else if (errorView == Constant.PASSWORD_NOT_MATCHED) {
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onFailure(String error) {
        progressBarSignUp.setVisibility(View.GONE);
        failureCustomDialog(error);
        userEditName.setText("");
        userPassword.setText("");
        userConfirmPassword.setText("");
        userEmailAddress.setText("");
        countrySpinner.setText("");
    }

    public void successCustomDialog(String alert) {


        new IOSDialog.Builder(SignUp.this)
                .setTitle("Success")
                .setMessage(alert)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                }).show();
    }

    public void failureCustomDialog(String alert) {


        new IOSDialog.Builder(SignUp.this)
                .setMessage(alert)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }


    @Override
    public void getSelectedCountry(String country) {
        countrySpinner.setText(country);
    }
}
