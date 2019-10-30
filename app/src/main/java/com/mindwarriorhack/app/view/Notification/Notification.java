package com.mindwarriorhack.app.view.Notification;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.ligl.android.widget.iosdialog.IOSDialog;
import com.mindwarriorhack.app.R;
import com.mindwarriorhack.app.helper.SwipeHelper;
import com.mindwarriorhack.app.model.Notification.BroadcastListItem;
import com.mindwarriorhack.app.model.Notification.BroadcastResponse;
import com.mindwarriorhack.app.presenter.NotificationPresenter;
import com.mindwarriorhack.app.view.Home.HomeActivity;
import com.mindwarriorhack.app.view.Purchase.PurchaseActivity;
import com.mindwarriorhack.app.view.adapters.NotificationAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

public class Notification extends AppCompatActivity implements NotificationView {
    private ImageView backNotification;
    private TextView no_notification_text;
    private ProgressBar progressBar;
    private NotificationPresenter notificationPresenter;
    private RecyclerView notificationRecyclerView;
    private NotificationAdapter notificationAdapter;
    private List<BroadcastListItem> items;
    private SwipeHelper swipeHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        initProgressBar();
        initUI();
    }

    private void initUI() {
        items = new ArrayList<>();
        notificationPresenter = new NotificationPresenter(this, this);
        backNotification = findViewById(R.id.backNotification);
        no_notification_text=findViewById(R.id.no_notification_text);

        backNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Notification.this,HomeActivity.class));
            }
        });
        progressBar.setVisibility(View.VISIBLE);
        notificationPresenter.broadcastList();
        notificationRecyclerView = findViewById(R.id.notificationRecyclerView);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setStackFromEnd(true);
        manager.setReverseLayout(true);
        notificationRecyclerView.setLayoutManager(manager);

        swipeToDelete();
    }

    private void initProgressBar() {
        progressBar = new ProgressBar(Notification.this, null, android.R.attr.progressBarStyle);
        progressBar.setIndeterminate(true);

        //Changes the color of progressbar itself and not background
        //progressBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(getApplicationContext(), android.R.color.white), PorterDuff.Mode.SRC_IN);

        RelativeLayout relativeLayout = new RelativeLayout(Notification.this);
        relativeLayout.setGravity(Gravity.CENTER);
        relativeLayout.addView(progressBar);

        RelativeLayout.LayoutParams params = new
                RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        progressBar.setVisibility(View.INVISIBLE);

        this.addContentView(relativeLayout, params);
    }

    @Override
    public void onNotificationSuccess(BroadcastResponse response) {
        if (response.getResponseData().getBroadcastList() != null) {
            items.addAll(response.getResponseData().getBroadcastList());
            notificationAdapter = new NotificationAdapter(items, this);
            notificationRecyclerView.setAdapter(notificationAdapter);
        }
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onNotificationFailure(String error) {
        progressBar.setVisibility(View.INVISIBLE);
        customDialog(error);
    }

    @Override
    public void onNotificationDeleted(String msg) {
        //customDialog(msg);
    }

    @Override
    public void onNotificationDeletedError(String error) {
        customDialog(error);
    }

    public void customDialog(String alert) {
        new IOSDialog.Builder(this)
                .setTitle("Alert")
                .setMessage(alert)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
    }

    private void swipeToDelete() {

        swipeHelper = new SwipeHelper(this, notificationRecyclerView) {
            @Override
            public void instantiateUnderlayButton(final RecyclerView.ViewHolder viewHolder, List<UnderlayButton> underlayButtons) {
                try {
                    underlayButtons.add(new SwipeHelper.UnderlayButton(
                            "Delete",
                            R.drawable.ic_delete,
                            Color.parseColor("#FF3C30"),
                            new UnderlayButtonClickListener() {
                                @SuppressLint("ObsoleteSdkInt")
                                @Override
                                public void onClick(int pos) {
                                    /*items.remove(pos);
                                    notificationAdapter.notifyDataSetChanged();*/
                                    String broadcastId=items.get(pos).getBroadcastId();
                                    String title = items.get(pos).getTitle();
                                    notificationPresenter.deleteBroadcast(broadcastId);
                                    notificationAdapter.removeItem(pos);
                                    Snackbar snackbar = Snackbar
                                            .make(notificationRecyclerView, title + " removed from the list", Snackbar.LENGTH_LONG);
                                    View snackBarView = snackbar.getView();
                                    snackBarView.setBackgroundColor(ContextCompat.getColor(Notification.this, R.color.statusBarColor));
                                    snackbar.getView().getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
                                    snackbar.show();
                                }
                            }
                    ));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }

}
