package com.mindwarriorhack.app.view.fragments.HomeFragment;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.ligl.android.widget.iosdialog.IOSDialog;
import com.mindwarriorhack.app.R;
import com.mindwarriorhack.app.helper.Constant;
import com.mindwarriorhack.app.helper.Utils;
import com.mindwarriorhack.app.manager.PreferenceManager;
import com.mindwarriorhack.app.view.Home.HomeActivity;
import com.mindwarriorhack.app.view.Popups.LevelInfoPopup;
import com.mindwarriorhack.app.view.VideoList.VideoListActivity;
import com.mindwarriorhack.app.model.Levels.LevelResponse;
import com.mindwarriorhack.app.model.Levels.LevelsListItem;
import com.mindwarriorhack.app.presenter.HomeFragmentPresenter;
import com.mindwarriorhack.app.view.Popups.SubscribePopup;
import com.mindwarriorhack.app.view.adapters.LevelAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import retrofit2.Response;

public class HomeFragment extends Fragment implements HomeFragmentView, LevelAdapter.AdapterCallBack, View.OnClickListener {

    private RecyclerView levelsRecyclerView;
    private HomeFragmentPresenter homeFragmentPresenter;
    private AppCompatActivity context;
    private List<LevelsListItem> levels;
    private LevelAdapter levelAdapter;
    private LevelAdapter.AdapterCallBack adapterCallBack;
    private View view;
    private ProgressBar progressBar;
    private SwipeRefreshLayout swipeContainer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        initProgressBar();
        initUI(view);
        return view;
    }

    private void initUI(View view) {
        levels = new ArrayList<>();
        homeFragmentPresenter = new HomeFragmentPresenter(context, this);
        progressBar.setVisibility(View.VISIBLE);
        homeFragmentPresenter.getLevels();
        if(!PreferenceManager.getString(Constant.PENDING_PAYMENT_STATUS_ORDER_ID).isEmpty())
        {
            homeFragmentPresenter.updatePaymentStatus(Integer.parseInt(PreferenceManager.getString(Constant.PENDING_PAYMENT_STATUS_ORDER_ID)),PreferenceManager.getString(Constant.PENDING_PAYMENT_STATUS_ORDER_STATUS));
        }
            //for status bar when navigation is open
          if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = context.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(context,R.color.colorPrimaryDark));
        }

        // int spanCount = 2; // 2 columns
        // int spacing = 25; // 50px
        // boolean includeEdge = true;
        levelsRecyclerView = view.findViewById(R.id.levelsRecyclerView);
        //levelsRecyclerView.addItemDecoration(new SpaceItemDecoration(spanCount, spacing, includeEdge));
        /*int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.spacing);
        levelsRecyclerView.addItemDecoration(new SpaceItemDecoration(spacingInPixels));*/
        swipeContainer = view.findViewById(R.id.swipeContainer);
        swipeContainer.setColorSchemeColors(getResources().getColor(R.color.colorPrimaryDark));
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                homeFragmentPresenter.getLevels();
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = (AppCompatActivity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onSuccess(Response<LevelResponse> response) {
        progressBar.setVisibility(View.INVISIBLE);
        if (response.body() != null && response.body().getLevelResponseData() != null) {
            if (levelAdapter != null) {
                swipeContainer.setRefreshing(false);
                levels.clear();
                levels.addAll(response.body().getLevelResponseData().getLevelsList());
                levelAdapter.notifyDataSetChanged();
            } else {

                if(swipeContainer != null){
                    swipeContainer.setRefreshing(false);
                }
                levels = response.body().getLevelResponseData().getLevelsList();
                levelAdapter = new LevelAdapter(levels, context, this);
                GridLayoutManager gridLayoutManager=new GridLayoutManager(context,2,RecyclerView.VERTICAL,false);
                gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int position) {
                        //here 2 represents the column count change as per required
                        return (position%2==0?1:1);
                    }
                });
                levelsRecyclerView.setLayoutManager(gridLayoutManager);
                levelsRecyclerView.setAdapter(levelAdapter);
                setAnimation();
            }
        }
    }


    @Override
    public void onFailure(String error) {
        progressBar.setVisibility(View.INVISIBLE);
        ErrorDialog(error);
    }

    @Override
    public void onInternetFailure(String error) {
        progressBar.setVisibility(View.INVISIBLE);
        internetErrorDialog();
    }

    @Override
    public void onPaymentUpdateStatusFailure() {
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onPaymentUpdateStatusSuccess() {
        PreferenceManager.setString(Constant.PENDING_PAYMENT_STATUS_ORDER_ID,"");
        PreferenceManager.setString(Constant.PENDING_PAYMENT_STATUS_ORDER_STATUS,"");
        ((HomeActivity) context).getDrawerUsersType().setText(PreferenceManager.getString(Constant.USER_TYPE));
        if (PreferenceManager.getString(Constant.USER_TYPE).equals("Premium")) {
            ((HomeActivity) context).getDrawerUsersType().setBackground(getResources().getDrawable(R.drawable.premium));
        } else {
            ((HomeActivity) context).getDrawerUsersType().setBackground(getResources().getDrawable(R.drawable.free));
        }
        homeFragmentPresenter.getLevels();
    }


    private void internetErrorDialog() {
        progressBar.setVisibility(View.INVISIBLE);
        new IOSDialog.Builder(getContext())
                .setTitle("Can't connect to the internet")
                .setMessage("Internet Connection unavailable.Please try after sometime")
                .setCancelable(false)
                .setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Retry", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        homeFragmentPresenter.getLevels();
                        dialog.dismiss();
                    }
                }).show();
    }

    private void ErrorDialog(String msg) {
        new IOSDialog.Builder(getContext())
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
    public void OpenSubscribePopup(String levelName, LevelsListItem item) {
        SubscribePopup popup = new SubscribePopup(context, levelName, item);
        Objects.requireNonNull(popup.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popup.show();
    }

    @Override
    public void openVideoListActivity(String levelId, String title) {

        Intent intent = new Intent(getContext(), VideoListActivity.class);
        intent.putExtra("LevelId", levelId);
        intent.putExtra("Title", title);
        startActivity(intent);
    }

    @Override
    public void OpenLevelPopup(String description,String title) {
        LevelInfoPopup popup=new LevelInfoPopup(context,description,title);
        Objects.requireNonNull(popup.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        popup.show();
    }

    @Override
    public void onClick(View v) {
        //adapterCallBack.OpenSubscribePopup();
    }

    @Override
    public void onResume() {
        super.onResume();

        if(!PreferenceManager.getString(Constant.IS_PURCHASE_DONE).isEmpty()){
            homeFragmentPresenter.getLevels();
            PreferenceManager.setString(Constant.IS_PURCHASE_DONE,"");
        }

    }

    private void initProgressBar() {
        progressBar = new ProgressBar(context, null, android.R.attr.progressBarStyle);
        progressBar.setIndeterminate(true);

        RelativeLayout relativeLayout = new RelativeLayout(context);
        relativeLayout.setGravity(Gravity.CENTER);
        relativeLayout.addView(progressBar);

        RelativeLayout.LayoutParams params = new
                RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        progressBar.setVisibility(View.INVISIBLE);

        context.addContentView(relativeLayout, params);
    }



    private void setAnimation(){

        levelsRecyclerView.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {

                    @Override
                    public boolean onPreDraw() {
                        levelsRecyclerView.getViewTreeObserver().removeOnPreDrawListener(this);

                        for (int i = 0; i < levelsRecyclerView.getChildCount(); i++) {
                            View v = levelsRecyclerView.getChildAt(i);
                            ScaleAnimation anim = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_PARENT, 0.5f, Animation.RELATIVE_TO_PARENT, 0.5f);
                            anim.setDuration(1000);
                            v.startAnimation(anim);

                        }

                        return true;
                    }
                });

    }
}
