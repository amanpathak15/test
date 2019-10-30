package com.mindwarriorhack.app.view.Popups;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.mindwarriorhack.app.R;
import com.mindwarriorhack.app.model.Levels.LevelsListItem;
import com.mindwarriorhack.app.model.Packages.PackageListItem;
import com.mindwarriorhack.app.view.Purchase.PurchaseActivity;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;


public class SubscribePopup extends Dialog {


    private LevelsListItem levelItem;
    private Context context;
    private ImageView cancel;
    private TextView level_purchase_title, levels;
    private Button subscribe_now;
    private String levelName;


    public SubscribePopup(Context context, String levelName, LevelsListItem item) {
        super(context);
        Objects.requireNonNull(getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.context = context;
        this.levelName = levelName;
        this.levelItem = item;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.level_subscribe_popup);
        setCancelable(true);
        setCanceledOnTouchOutside(true);
        initUI();

    }

    public void initUI() {
        cancel = findViewById(R.id.cancel);
        level_purchase_title = findViewById(R.id.level_purchase_title);
        levels = findViewById(R.id.levels);
        level_purchase_title.setText(Html.fromHtml("To purchase " + "<b>" + levelName + "</b> " + "please click on BUY NOW"));
        levels.setText(levelName);
        subscribe_now = findViewById(R.id.subscribe_now);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        subscribe_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PackageListItem item = new PackageListItem();
                item.setId(levelItem.getLevelId());
                item.setIsPaidLevel(levelItem.getIsPaidLevel());
                item.setLevelLogo(levelItem.getLevelLogo());
                item.setLevelPrice(levelItem.getLevelPrice());
                item.setTitle(levelItem.getTitle());


                Intent intent = new Intent(context, PurchaseActivity.class);
                intent.putExtra("level", (Serializable) item);
                context.startActivity(intent);
                dismiss();
            }
        });

    }


}
