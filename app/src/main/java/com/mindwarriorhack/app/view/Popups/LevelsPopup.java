package com.mindwarriorhack.app.view.Popups;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.TextView;

import com.mindwarriorhack.app.R;
import com.mindwarriorhack.app.model.Packages.PackageListItem;
import com.mindwarriorhack.app.view.Purchase.PurchaseActivity;
import com.mindwarriorhack.app.view.adapters.PackagesAdapter;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class LevelsPopup extends Dialog implements View.OnClickListener {

    private RecyclerView recyclerViewLevels;
    private Context context;
    private List<PackageListItem> addMorePackageList;
    private updatePurchaseInterface updateInterface;


    public LevelsPopup(@NonNull Context context, List<PackageListItem> addMorePackageList, updatePurchaseInterface updateInterface) {
        super(context);
        this.context = context;
        this.addMorePackageList = addMorePackageList;
        this.updateInterface = updateInterface;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.levels_popup);
        setCanceledOnTouchOutside(false);
        setCancelable(false);

        TextView cancelButton = (TextView) findViewById(R.id.cancel);
        cancelButton.setOnClickListener(this);
        TextView done = (TextView) findViewById(R.id.done);
        done.setOnClickListener(this);

        TextView select_levels = findViewById(R.id.select_levels);
        recyclerViewLevels = findViewById(R.id.recyclerViewLevels);
        recyclerViewLevels.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(context);
        recyclerViewLevels.setLayoutManager(manager);
        PackagesAdapter adapter = new PackagesAdapter(addMorePackageList, context, (PurchaseActivity) context);
        recyclerViewLevels.setAdapter(adapter);
        setAnimation();

    }


    private void setAnimation(){
        recyclerViewLevels.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {

                    @Override
                    public boolean onPreDraw() {
                        recyclerViewLevels.getViewTreeObserver().removeOnPreDrawListener(this);

                        for (int i = 0; i < recyclerViewLevels.getChildCount(); i++) {
                            View v = recyclerViewLevels.getChildAt(i);
                            ScaleAnimation anim = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_PARENT, 0.5f, Animation.RELATIVE_TO_PARENT, 0.5f);
                            anim.setDuration(600);
                            v.startAnimation(anim);
                        }

                        return true;
                    }
                });

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.done:
                updateInterface.updatePurchaseList();
                dismiss();
                break;

            case R.id.cancel:
                updateInterface.removeAddedMorePurchaseItems();
                dismiss();
                break;

        }

    }


    public interface updatePurchaseInterface {

        void updatePurchaseList();

        void removeAddedMorePurchaseItems();

    }


}
