package com.mindwarriorhack.app.helper;

import android.graphics.Color;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

import androidx.annotation.NonNull;

public class MySpannable extends ClickableSpan {

    private boolean isUnderline = false;

    public MySpannable(boolean isUnderline) {
        this.isUnderline = isUnderline;
    }

    @Override
    public void updateDrawState(@NonNull TextPaint ds) {
        ds.setColor(Color.parseColor("#F3970A"));
    }

    @Override
    public void onClick(@NonNull View widget) {

    }
}
