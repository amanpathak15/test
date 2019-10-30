package com.mindwarriorhack.app.view.Purchase;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.ligl.android.widget.iosdialog.IOSDialog;
import com.mindwarriorhack.app.R;
import com.mindwarriorhack.app.helper.Constant;
import com.mindwarriorhack.app.helper.SwipeHelper;
import com.mindwarriorhack.app.manager.PreferenceManager;
import com.mindwarriorhack.app.model.Packages.PackageListItem;
import com.mindwarriorhack.app.presenter.PurchasePresenter;
import com.mindwarriorhack.app.view.Payment.PaymentActivity;
import com.mindwarriorhack.app.view.Popups.LevelsPopup;
import com.mindwarriorhack.app.view.SignIn.SignIn;
import com.mindwarriorhack.app.view.adapters.PackagesAdapter;
import com.mindwarriorhack.app.view.adapters.PurchasePackagesAdapter;

import java.util.ArrayList;
import java.util.List;

public class PurchaseActivity extends AppCompatActivity implements View.OnClickListener, PurchaseView, PackagesAdapter.PackageInterface, LevelsPopup.updatePurchaseInterface {


    private TextView add_more_packages;
    private Button button_Proceed;
    private ImageView backPurchase;
    private RecyclerView recyclerViewLevelsAdded;
    private PurchasePresenter purchasePresenter;
    private List<PackageListItem> packageList;
    private List<PackageListItem> addMorePackageList;
    private PurchasePackagesAdapter adapter;
    boolean isMorePackagesAdded = false;
    int tempListIndex = 1;
    private SwipeHelper swipeHelper;
    private ProgressBar progressBar;
    private LinearLayout linearLayout;


    public PurchaseActivity() {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase);
        initProgressBar();
        initUI();


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }


    }

    private void initUI() {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        add_more_packages = findViewById(R.id.add_more_packages);
        button_Proceed = findViewById(R.id.button_Proceed);
        backPurchase = findViewById(R.id.backPurchase);

        //recycler view
        recyclerViewLevelsAdded = findViewById(R.id.recyclerViewLevelsAdded);
        recyclerViewLevelsAdded.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewLevelsAdded.setItemAnimator(new DefaultItemAnimator());
        recyclerViewLevelsAdded.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        linearLayout=findViewById(R.id.linearLayout);

        //views
        backPurchase.setOnClickListener(this);
        add_more_packages.setOnClickListener(this);
        button_Proceed.setOnClickListener(this);
        packageList = new ArrayList<>();
        addMorePackageList = new ArrayList<>();
        purchasePresenter = new PurchasePresenter(this, this);

        if (getIntent().hasExtra("level")) {
            packageList.add((PackageListItem) getIntent().getSerializableExtra("level"));
        }


        //presenter and adapter and api called
        progressBar.setVisibility(View.VISIBLE);
        purchasePresenter.getLevelPackages(PreferenceManager.getString(Constant.USER_ID), packageList);
        adapter = new PurchasePackagesAdapter(packageList, this);
        recyclerViewLevelsAdded.setAdapter(adapter);

        //swipe method
        swipeToDelete();

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_Proceed:
                StringBuilder levelIds = new StringBuilder();

                float totalAmountToBePaid = 0;
                for(int i=0; i<packageList.size(); i++){
                    levelIds.append(packageList.get(i).getId()+ ",");
                    totalAmountToBePaid += packageList.get(i).getLevelPrice();
                }

                if (!levelIds.toString().isEmpty()){
                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    Intent intent = new Intent(PurchaseActivity.this, PaymentActivity.class);
                    intent.putExtra("totalAmountToBePaid",totalAmountToBePaid);
                    intent.putExtra("levelIds", levelIds.toString());
                    startActivity(intent);
                    finish();
                }else {
                    Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),
                            "Please add atleast one level to proceed",
                            Snackbar.LENGTH_SHORT);
                    //for full width in tablet of the snackbar
                    snackbar.getView().getLayoutParams().width= ViewGroup.LayoutParams.MATCH_PARENT;
                    snackbar.setActionTextColor(Color.parseColor("#F3970A"));
                    snackbar.show();
                }
                break;

            case R.id.add_more_packages:
                if (addMorePackageList.size() > 0) {
                    LevelsPopup popup = new LevelsPopup(this, addMorePackageList, this);
                    popup.show();
                } else {
                    Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "No more packages available", Snackbar.LENGTH_SHORT);
                    //for full width in tablet of the snackbar
                    snackbar.getView().getLayoutParams().width= ViewGroup.LayoutParams.MATCH_PARENT;
                    snackbar.setActionTextColor(Color.parseColor("#F3970A"));
                    snackbar.show();
                }
                break;

            case R.id.backPurchase:
                finish();
                break;
        }
    }


    public void customDialog(String alert) {
        new IOSDialog.Builder(this)
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
    public void onSuccess(List<PackageListItem> list) {
        addMorePackageList = list;
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onFailure(String error) {
        customDialog(error);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void addToList(PackageListItem item) {
        isMorePackagesAdded = true;
        packageList.add(item);
    }

    @Override
    public void removeFromList(String levelId) {
        for (int i = 0; i < packageList.size(); i++) {
            if (packageList.get(i).getId().equals(levelId)) {
                packageList.remove(i);
            }
        }
    }

    @Override
    public void updatePurchaseList() {
        isMorePackagesAdded = false;
        tempListIndex = packageList.size();
        adapter.notifyDataSetChanged();
        progressBar.setVisibility(View.VISIBLE);
        purchasePresenter.getLevelPackages(PreferenceManager.getString(Constant.USER_ID), packageList);
    }

    @Override
    public void removeAddedMorePurchaseItems() {
        if (isMorePackagesAdded) {
            int size = packageList.size();

            if (packageList.size() > 1) {
                for (int i = tempListIndex; i < packageList.size(); i = i) {
                    packageList.remove(i);
                }

                adapter.notifyDataSetChanged();
            }

            isMorePackagesAdded = false;
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

    }



    private void swipeToDelete() {

        swipeHelper = new SwipeHelper(this, recyclerViewLevelsAdded) {
            @Override
            public void instantiateUnderlayButton(final RecyclerView.ViewHolder viewHolder, List<UnderlayButton> underlayButtons) {
                try {
                    underlayButtons.add(new SwipeHelper.UnderlayButton(
                            "Delete",
                            0,
                            Color.parseColor("#FF3C30"),
                            new UnderlayButtonClickListener() {
                                @SuppressLint("ObsoleteSdkInt")
                                @Override
                                public void onClick(int pos) {
                                    String packageName = packageList.get(pos).getTitle();
                                    adapter.removeItem(pos);
                                    updatePurchaseList();
                                    Snackbar snackbar = Snackbar
                                            .make(recyclerViewLevelsAdded, packageName + " removed from the list", Snackbar.LENGTH_LONG);
                                    View snackBarView = snackbar.getView();
                                    snackBarView.setBackgroundColor(ContextCompat.getColor(PurchaseActivity.this,R.color.statusBarColor));
                                    snackbar.getView().getLayoutParams().width= ViewGroup.LayoutParams.MATCH_PARENT;
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


    private void initProgressBar() {
        progressBar = new ProgressBar(PurchaseActivity.this, null, android.R.attr.progressBarStyle);
        progressBar.setIndeterminate(true);

        RelativeLayout relativeLayout = new RelativeLayout(PurchaseActivity.this);
        relativeLayout.setGravity(Gravity.CENTER);
        relativeLayout.addView(progressBar);

        RelativeLayout.LayoutParams params = new
                RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        progressBar.setVisibility(View.INVISIBLE);

        this.addContentView(relativeLayout, params);
    }



}