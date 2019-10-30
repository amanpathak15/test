package com.mindwarriorhack.app.view.Popups;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.mindwarriorhack.app.R;
import com.mindwarriorhack.app.model.Levels.LevelsListItem;
import com.mindwarriorhack.app.model.Packages.PackageListItem;
import com.mindwarriorhack.app.view.Purchase.PurchaseActivity;

import java.io.Serializable;
import java.util.Objects;


public class editProfilePopup extends Dialog {


    private Context context;
    private ImageView cancel;
    private EditText editText;
    private String hint, data;
    private Button save;
    private String type;
    private updateData updateData;



    public editProfilePopup(Context context, String data, String type, String hint, updateData updateData) {
        super(context);
        Objects.requireNonNull(getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.context = context;
        this.data = data;
        this.type = type;
        this.hint = hint;
        this.updateData = updateData;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile_popup);
        setCancelable(true);
        setCanceledOnTouchOutside(true);
        initUI();

    }

    public void initUI() {
        cancel = findViewById(R.id.cancel);
        editText = findViewById(R.id.ediText);
        editText.setHint(hint);
        editText.setText(data);
        save = (Button) findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               updateData.update(editText.getText().toString().trim(),type);
               dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public interface updateData{

        void update(String data, String type);

    }




}
