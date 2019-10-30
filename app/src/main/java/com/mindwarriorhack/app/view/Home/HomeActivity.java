package com.mindwarriorhack.app.view.Home;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.internal.NavigationMenuView;
import com.google.android.material.navigation.NavigationView;

import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.recyclerview.widget.DividerItemDecoration;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.ligl.android.widget.iosdialog.IOSDialog;
import com.mindwarriorhack.app.R;
import com.mindwarriorhack.app.helper.Constant;
import com.mindwarriorhack.app.helper.Utils;
import com.mindwarriorhack.app.manager.PreferenceManager;
import com.mindwarriorhack.app.presenter.HomePresenter;
import com.mindwarriorhack.app.view.Notification.Notification;
import com.mindwarriorhack.app.view.Redeem.Redeem;
import com.mindwarriorhack.app.view.Settings.SettingsActivity;
import com.mindwarriorhack.app.view.SignIn.SignIn;
import com.mindwarriorhack.app.view.fragments.AboutUsFragment.AboutUsFragment;
import com.mindwarriorhack.app.view.fragments.ContactUs.ContactUsFragment;
import com.mindwarriorhack.app.view.fragments.HomeFragment.HomeFragment;
import com.mindwarriorhack.app.view.fragments.Profile.Profile;
import com.mindwarriorhack.app.view.fragments.SocialFeedsFragment.SocialFeedsFragment;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener, HomeView {

    private DrawerLayout drawerLayout;
    private Fragment presentFragment;
    private ImageView mindWarriorIcon;
    private TextView toolbarTitle;
    private RelativeLayout contentHomeRelativeLayout;
    private ImageView drawerUserProfilePic;
    private HomePresenter homePresenter;
    private TextView drawerUserName;
    private TextView drawerUsersType;
    private boolean isDrawerLocked;


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
        setContentView(R.layout.activity_home);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        initUI();
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            //drawerLayout.setScrimColor(Color.TRANSPARENT); // default shadow
            isDrawerLocked = false;
        }
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        } //make fully Android Transparent Status bar
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

    }

    private void initUI() {
        drawerLayout = findViewById(R.id.drawer_layout);

        final NavigationView navigationView = findViewById(R.id.nav_view);
        //navigationView.setScrollContainer(true);
        View navigationHeaderView = navigationView.inflateHeaderView(R.layout.nav_header);
        navigationView.getMenu().getItem(0).setChecked(false);

        ImageView icon_menu = findViewById(R.id.icon_menu);
        mindWarriorIcon = findViewById(R.id.mindWarriorLogo);
        toolbarTitle = findViewById(R.id.toolbarTitle);
        drawerUserName = navigationHeaderView.findViewById(R.id.userName);
        drawerUserProfilePic = navigationHeaderView.findViewById(R.id.userProfilePic);
        drawerUsersType = navigationHeaderView.findViewById(R.id.usersType);
        if (PreferenceManager.getString(Constant.USER_TYPE).equals("Premium")) {
            drawerUsersType.setBackground(getResources().getDrawable(R.drawable.premium));
        } else {
            drawerUsersType.setBackground(getResources().getDrawable(R.drawable.free));
        }
        drawerUsersType.setText(PreferenceManager.getString(Constant.USER_TYPE));
        drawerUserName.setText(PreferenceManager.getString(Constant.USER_NAME));
        if (!PreferenceManager.getString(Constant.PROFILE_PIC).isEmpty()) {
            Glide.with(this).load(PreferenceManager.getString(Constant.PROFILE_PIC)).apply(RequestOptions.circleCropTransform()).into(drawerUserProfilePic);
        } else {
            Glide.with(this).load(R.drawable.default_profilepic).apply(RequestOptions.circleCropTransform()).into(drawerUserProfilePic);
        }


        homePresenter = new HomePresenter(this, this);

        contentHomeRelativeLayout = findViewById(R.id.contentHomeRelativeLayout);
        icon_menu.setOnClickListener(this);

        navigationView.setCheckedItem(R.id.nav_home);
        NavigationMenuView navMenuView = (NavigationMenuView) navigationView.getChildAt(0);
        navMenuView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.nav_home:
                                presentFragment = getSupportFragmentManager().findFragmentByTag(Constant.HOME_TAG);
                                if (presentFragment == null || !presentFragment.isVisible()) {
                                    mindWarriorIcon.setVisibility(View.VISIBLE);
                                    toolbarTitle.setText("");
                                    contentHomeRelativeLayout.setBackground(getDrawable(R.drawable.loginbg));
                                    loadFragment(new HomeFragment(), Constant.HOME_TAG);
                                }
                                break;

                            case R.id.nav_socialfeeds:
                                presentFragment = getSupportFragmentManager().findFragmentByTag(Constant.SOCIAL_FEED_TAG);
                                if (presentFragment == null || !presentFragment.isVisible()) {
                                    mindWarriorIcon.setVisibility(View.GONE);
                                    toolbarTitle.setText("Social Feeds");
                                    contentHomeRelativeLayout.setBackgroundColor(getResources().getColor(R.color.darkBlueColor));
                                    loadFragment(new SocialFeedsFragment(), Constant.SOCIAL_FEED_TAG);
                                }
                                break;

                            case R.id.nav_profile:
                                presentFragment = getSupportFragmentManager().findFragmentByTag(Constant.PROFILE_TAG);
                                if (presentFragment == null || !presentFragment.isVisible()) {
                                    mindWarriorIcon.setVisibility(View.GONE);
                                    toolbarTitle.setText("Profile");
                                    contentHomeRelativeLayout.setBackground(getDrawable(R.drawable.loginbg));
                                    loadFragment(new Profile(), Constant.PROFILE_TAG);
                                }
                                break;

                            case R.id.nav_noti:
                                startActivity(new Intent(HomeActivity.this, Notification.class));
                                break;

                            case R.id.nav_settings:
                                startActivity(new Intent(HomeActivity.this, SettingsActivity.class));
                                break;

                            /*case R.id.nav_redeem:
                                startActivity(new Intent(HomeActivity.this, Redeem.class));
                                break;*/

                            case R.id.nav_about_us:
                                presentFragment = getSupportFragmentManager().findFragmentByTag(Constant.ABOUT_US_TAG);
                                if (presentFragment == null || !presentFragment.isVisible()) {
                                    mindWarriorIcon.setVisibility(View.VISIBLE);
                                    toolbarTitle.setText("About Us");
                                    contentHomeRelativeLayout.setBackground(getDrawable(R.drawable.loginbg));
                                    loadFragment(new AboutUsFragment(), Constant.ABOUT_US_TAG);
                                }
                                break;

                            case R.id.nav_contact_us:
                                presentFragment = getSupportFragmentManager().findFragmentByTag(Constant.CONTACT_US_TAG);
                                if (presentFragment == null || !presentFragment.isVisible()) {
                                    mindWarriorIcon.setVisibility(View.GONE);
                                    contentHomeRelativeLayout.setBackgroundColor(getResources().getColor(R.color.darkBlueColor));
                                    toolbarTitle.setText("Contact Us");
                                    loadFragment(new ContactUsFragment(), Constant.CONTACT_US_TAG);
                                }
                                break;

                            case R.id.nav_logout:
                                String deviceToken = PreferenceManager.getDeviceToken();
                                homePresenter.logoutApi(PreferenceManager.getString(Constant.USER_ID), deviceToken);
                                break;
                        }


                        drawerLayout.closeDrawers();

                        return true;
                    }
                }
        );

        loadFragment(new HomeFragment(), Constant.HOME_TAG);


    }

    public void loadFragment(Fragment fragment, String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.candidate_fragment, fragment, tag);
        fragmentTransaction.commit();
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.icon_menu) {
            hideKeyboard(HomeActivity.this);
            drawerLayout.openDrawer(GravityCompat.START);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onLogoutSuccess(String successMsg) {

        String deviceToken = PreferenceManager.getDeviceToken();
        PreferenceManager.clearPreference();
        PreferenceManager.setDeviceToken(deviceToken);
        startActivity(new Intent(HomeActivity.this, SignIn.class));
        finish();
    }

    @Override
    public void onFailure(String error) {
        customDialog("Internet Connection unavailable.Please try after sometime");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void customDialog(String alert) {
        new IOSDialog.Builder(HomeActivity.this)
                .setTitle("Can't connect to the internet")
                .setMessage(alert)
                .setCancelable(false)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Retry", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                }).show();
    }


    private void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public ImageView getDrawerUserProfilePic() {
        return drawerUserProfilePic;
    }

    public TextView getDrawerUserName() {
        return drawerUserName;
    }

    public TextView getDrawerUsersType() {
        return drawerUsersType;
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (drawerUsersType != null) {
            drawerUsersType.setText(PreferenceManager.getString(Constant.USER_TYPE));
            if (PreferenceManager.getString(Constant.USER_TYPE).equals("Premium")) {
                drawerUsersType.setBackground(getResources().getDrawable(R.drawable.premium));
            } else {
                drawerUsersType.setBackground(getResources().getDrawable(R.drawable.free));
            }
        }
    }
}
