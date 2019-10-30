package com.mindwarriorhack.app.view.Popups;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.mindwarriorhack.app.R;

public class TermsConditionPopup extends Dialog {
    private ImageView terms_conditions_cancel;
    private TextView terms_conditions_popup_text;

    public TermsConditionPopup(@NonNull Context context) {
        super(context);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.terms_condition_popup);
        initUI();
    }

    private void initUI() {
        terms_conditions_cancel = findViewById(R.id.terms_conditions_cancel);
        terms_conditions_popup_text = findViewById(R.id.terms_conditions_popup_text);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            terms_conditions_popup_text.setText(R.string.terms_condition_formatted_text);
        }else {
            terms_conditions_popup_text.setText(R.string.terms_condition_formatted_text);
        }
        terms_conditions_popup_text.setMovementMethod(ScrollingMovementMethod.getInstance());
        terms_conditions_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
