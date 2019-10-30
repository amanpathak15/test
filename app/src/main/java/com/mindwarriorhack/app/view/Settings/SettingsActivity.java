package com.mindwarriorhack.app.view.Settings;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ligl.android.widget.iosdialog.IOSDialog;
import com.mindwarriorhack.app.R;
import com.mindwarriorhack.app.helper.Constant;
import com.mindwarriorhack.app.manager.PreferenceManager;
import com.mindwarriorhack.app.model.Packages.PackageListItem;
import com.mindwarriorhack.app.presenter.SettingsPresenter;
import com.mindwarriorhack.app.view.ChangePassword.ChangePassword;
import com.mindwarriorhack.app.view.Home.HomeActivity;
import com.mindwarriorhack.app.view.Purchase.PurchaseActivity;
import com.suke.widget.SwitchButton;

import java.util.List;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener, SettingsView, SwitchButton.OnCheckedChangeListener {

    private ImageView backSettings;
    private TextView buyPackage, change_password, rate_google_play, share_app;
    private SwitchButton switchButton;
    private SettingsPresenter settingsPresenter;
    private RelativeLayout relativeLayoutHead;
    private ProgressBar progressBar;
    private List<PackageListItem> packageList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        initUI();
        initProgressBar();


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
    }

    private void initUI() {
        settingsPresenter = new SettingsPresenter(this, this);
        switchButton = findViewById(R.id.switch_push);
        switchButton.setOnCheckedChangeListener(this);
        if (PreferenceManager.IsNotificationsEnabled()){
            switchButton.setChecked(true);
        }else {
            switchButton.setChecked(false);
        }
        buyPackage = findViewById(R.id.buyPackage);
        buyPackage.setOnClickListener(this);
        change_password = findViewById(R.id.change_password);
        change_password.setOnClickListener(this);
        rate_google_play = findViewById(R.id.rate_google_play);
        rate_google_play.setOnClickListener(this);
        share_app = findViewById(R.id.share_app);
        backSettings = findViewById(R.id.backRedeem);
        backSettings.setOnClickListener(this);
        share_app.setOnClickListener(this);
        relativeLayoutHead = findViewById(R.id.relativeLayoutHead);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backRedeem:
                startActivity(new Intent(SettingsActivity.this,HomeActivity.class));
                break;

            case R.id.change_password:
                startActivity(new Intent(SettingsActivity.this, ChangePassword.class));
                break;

            case R.id.buyPackage:
                startActivity(new Intent(SettingsActivity.this, PurchaseActivity.class));
                break;

            case R.id.rate_google_play:
                final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.mindwarriorhack.app" + appPackageName)));
                }
                break;

            case R.id.share_app:
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My application name");
                String shareMessage = "\nLet me recommend you this application\n\n";
                shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=com.mindwarriorhack.app";
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                startActivity(Intent.createChooser(shareIntent, "choose one"));
                break;
        }
    }

    @Override
    public void onChangeNotificationStatusSuccess(String msg, int notifyMe) {
        if (notifyMe == 1){
            PreferenceManager.setIsNotificationsEnabled(true);
        }else {
            PreferenceManager.setIsNotificationsEnabled(false);
        }
        successCustomDialog(msg);
    }

    @Override
    public void onChangeNotificationStatusFailure(String errorMsg) {
        failureCustomDialog(errorMsg);
    }

    public void successCustomDialog(String alert) {


        new IOSDialog.Builder(SettingsActivity.this)
                .setMessage(alert)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    public void failureCustomDialog(String alert) {


        new IOSDialog.Builder(SettingsActivity.this)
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
        progressBar = new ProgressBar(SettingsActivity.this, null, android.R.attr.progressBarStyle);
        progressBar.setIndeterminate(true);

        //Changes the color of progressbar itself and not background
        // progressBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(getApplicationContext(), android.R.color.white), PorterDuff.Mode.SRC_IN);

        RelativeLayout relativeLayout = new RelativeLayout(SettingsActivity.this);
        relativeLayout.setGravity(Gravity.CENTER);
        relativeLayout.addView(progressBar);

        RelativeLayout.LayoutParams params = new
                RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        progressBar.setVisibility(View.INVISIBLE);

        this.addContentView(relativeLayout, params);
    }

    @Override
    public void onCheckedChanged(SwitchButton view, boolean isChecked) {
        if (view.getId() == R.id.switch_push) {
            if (isChecked) {
                settingsPresenter.changeNotificationStatus(PreferenceManager.getString(Constant.USER_ID), 1);
                //Toast.makeText(getApplicationContext(), "Push Notification enabled", Toast.LENGTH_SHORT).show();
            } else {
                settingsPresenter.changeNotificationStatus(PreferenceManager.getString(Constant.USER_ID), 0);
                //Toast.makeText(getApplicationContext(), "Push Notification disabled", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(SettingsActivity.this,HomeActivity.class));
    }
}
