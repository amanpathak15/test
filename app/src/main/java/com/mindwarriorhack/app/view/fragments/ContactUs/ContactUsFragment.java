package com.mindwarriorhack.app.view.fragments.ContactUs;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.google.android.material.snackbar.Snackbar;
import com.ligl.android.widget.iosdialog.IOSDialog;
import com.mindwarriorhack.app.R;
import com.mindwarriorhack.app.helper.Constant;
import com.mindwarriorhack.app.presenter.ContactUsPresenter;
import com.mindwarriorhack.app.view.SignIn.SignIn;


public class ContactUsFragment extends Fragment implements View.OnClickListener, ContactUsView {

    private AppCompatActivity context;
    private ContactUsPresenter contactUsPresenter;
    private ProgressBar progressBar;
    private EditText subject, description;
    private Button btnSend;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = (AppCompatActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        contactUsPresenter = new ContactUsPresenter(context, this);
        View view = inflater.inflate(R.layout.fragment_contact_us, container, false);
        initProgressBar();
        initUI(view);
        return view;
    }

    private void initUI(View view) {

        subject = view.findViewById(R.id.subject);

        description = view.findViewById(R.id.description);

        btnSend = view.findViewById(R.id.btnSend);

        btnSend.setOnClickListener(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = context.getWindow();
            //window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(context,R.color.colorPrimaryDark));
        }

        
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnSend) {
            progressBar.setVisibility(View.VISIBLE);
            contactUsPresenter.contactUs(subject.getText().toString(), description.getText().toString());
        }
    }

    @Override
    public void onSuccess(String msg) {
        customDialog(msg);
        subject.setText("");
        description.setText("");
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onFailure(String error) {
        customDialog(error);
        subject.setText("");
        description.setText("");
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void inputValidations(String error, int errorview) {
        if (errorview == Constant.SUBJECT_INVALID) {
            /*Snackbar.make(context.findViewById(android.R.id.content),"Please enter required field(s).",Snackbar.LENGTH_SHORT).show();*/
            progressBar.setVisibility(View.INVISIBLE);
            customDialog("Please enter required field(s).");
        }

        if (errorview == Constant.DESCRIPTION_INVALID) {
            /*Snackbar.make(context.findViewById(android.R.id.content),"Please enter required field(s).",Snackbar.LENGTH_SHORT).show();*/
            progressBar.setVisibility(View.INVISIBLE);
            customDialog("Please enter required field(s).");
        }
    }

    private void customDialog(String alert) {
        new IOSDialog.Builder(getContext())
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

}
