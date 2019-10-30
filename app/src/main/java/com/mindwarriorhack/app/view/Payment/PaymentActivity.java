package com.mindwarriorhack.app.view.Payment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.mindwarriorhack.app.R;
import com.mindwarriorhack.app.StripePayment;
import com.mindwarriorhack.app.helper.Constant;
import com.mindwarriorhack.app.manager.PreferenceManager;
import com.mindwarriorhack.app.presenter.PaymentPresenter;
import com.mindwarriorhack.app.view.Settings.SettingsActivity;
import com.mindwarriorhack.app.view.SignIn.SignIn;

import java.text.DecimalFormat;

import retrofit2.Response;

public class PaymentActivity extends AppCompatActivity implements View.OnClickListener, PaymentView {

    private ImageView backPayment;
    private Button stripeButton;
    private TextView amountTotalView;
    private float totalAmountToBePaid;
    private PaymentPresenter presenter;
    private ProgressBar progressBar;
    private String[] levelIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        presenter = new PaymentPresenter(this,this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }

        if(getIntent().hasExtra("totalAmountToBePaid")){
            totalAmountToBePaid = Float.parseFloat(new DecimalFormat("##.##").format(getIntent().getFloatExtra("totalAmountToBePaid",0)));
        }

        if(getIntent().hasExtra("levelIds")){
            String levelIdsString = getIntent().getStringExtra("levelIds");
            levelIds = levelIdsString.split(",");
        }

        amountTotalView = findViewById(R.id.amountTotalView);
        amountTotalView.setText("£ "+totalAmountToBePaid);
        backPayment=findViewById(R.id.backPayment);
        backPayment.setOnClickListener(this);
        stripeButton=findViewById(R.id.stripeButton);
        stripeButton.setOnClickListener(this);
        initProgressBar();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.backPayment:
                finish();
                break;

            case R.id.stripeButton:
                progressBar.setVisibility(View.VISIBLE);
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                presenter.callCheckOut(PreferenceManager.getString(Constant.USER_ID), levelIds, totalAmountToBePaid);
                break;
        }
    }

    @Override
    public void onCheckOutSuccess(String msg, Response<JsonElement> response) {
        progressBar.setVisibility(View.INVISIBLE);
        int orderId = response.body().getAsJsonObject().get("responseData").getAsJsonObject().get("orderId").getAsInt();
        String paymentIntent = response.body().getAsJsonObject().get("responseData").getAsJsonObject().get("paymentIntent").getAsString();
        String clientSecret = response.body().getAsJsonObject().get("responseData").getAsJsonObject().get("client_secret").getAsString();
        Intent intent = new Intent(PaymentActivity.this, StripePayment.class);
        intent.putExtra("OrderId",orderId);
        intent.putExtra("clientSecret",clientSecret);
        startActivity(intent);
        finish();
    }

    @Override
    public void onCheckOutFailure(String error) {
        progressBar.setVisibility(View.INVISIBLE);
        Toast.makeText(this,error,Toast.LENGTH_SHORT).show();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }


    private void initProgressBar() {
        progressBar = new ProgressBar(PaymentActivity.this, null, android.R.attr.progressBarStyle);
        progressBar.setIndeterminate(true);

        RelativeLayout relativeLayout = new RelativeLayout(PaymentActivity.this);
        relativeLayout.setGravity(Gravity.CENTER);
        relativeLayout.addView(progressBar);

        RelativeLayout.LayoutParams params = new
                RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        progressBar.setVisibility(View.INVISIBLE);

        this.addContentView(relativeLayout, params);
    }
}
