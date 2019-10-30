package com.mindwarriorhack.app.view.PaymentStatusActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.gson.JsonElement;
import com.mindwarriorhack.app.R;
import com.mindwarriorhack.app.helper.Constant;
import com.mindwarriorhack.app.manager.PreferenceManager;
import com.mindwarriorhack.app.presenter.PaymentStatusPresenter;
import com.stripe.android.Stripe;
import com.stripe.android.exception.APIConnectionException;
import com.stripe.android.exception.APIException;
import com.stripe.android.exception.AuthenticationException;
import com.stripe.android.exception.InvalidRequestException;
import com.stripe.android.model.PaymentIntent;
import com.stripe.android.model.PaymentIntentParams;

import retrofit2.Response;

public class paymentStatusActivity extends AppCompatActivity implements paymentStatusView{

    PaymentStatusPresenter presenter;
    int orderId;
    private String status = "";
    private ImageView status_icon;
    private TextView status_text,order_id;
    LinearLayout paymentIssueMessage;
    // Payment status For Success and Failure
    // paid, success
    //fail, failed

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_status_activity);
        status_icon = findViewById(R.id.status_icon);
        status_text = findViewById(R.id.status_text);
        order_id = findViewById(R.id.order_id);
        paymentIssueMessage = findViewById(R.id.issueMessage);
        presenter = new PaymentStatusPresenter(this,this);
        if(getIntent().hasExtra("orderId")){
            orderId = getIntent().getIntExtra("orderId",-1);
            order_id.setText("ORDER ID: " + orderId);
        }

        if (getIntent().hasExtra("status")){
            status = getIntent().getStringExtra("status");
            if (status.equals("success")){
                Glide.with(this).load(R.drawable.success_blue).into(status_icon);
                status_text.setText("Order Placed Successfully");
            }else {
                Glide.with(this).load(R.drawable.high_alert_icon).into(status_icon);
                status_text.setText("Transaction Failed");
            }
        }

        if (getIntent().getData() != null && getIntent().getData().getQuery() != null && !getIntent().hasExtra("status")) {

            final Stripe stripe = new Stripe(this, "pk_test_LeZP22jwCq8e1zLe7bTZuNL700OdALbrLP");
            final String host = getIntent().getData().getHost();
            final String clientSecret = getIntent().getData().getQueryParameter(
                    "payment_intent_client_secret");

            final PaymentIntentParams retrievePaymentIntentParams =
                    PaymentIntentParams.createRetrievePaymentIntentParams(clientSecret);

            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    // retrieve the PaymentIntent on a background thread
                    try {
                        final PaymentIntent paymentIntent =
                                stripe.retrievePaymentIntentSynchronous(
                                        retrievePaymentIntentParams,
                                        "pk_test_LeZP22jwCq8e1zLe7bTZuNL700OdALbrLP");


                        PaymentIntent.Status status = PaymentIntent.Status
                                .fromCode(paymentIntent.getStatus());
                        if (PaymentIntent.Status.Succeeded == status){
                            //Toast.makeText(paymentStatusActivity.this,"Your Purchase suceeded", Toast.LENGTH_SHORT).show();
                            Glide.with(paymentStatusActivity.this).load(R.drawable.success_blue).into(status_icon);
                            status_text.setText("Order Successfully Placed");
                            presenter.updatePaymentStatus(orderId,"paid");
                        }
                        else
                        {
                            //Toast.makeText(paymentStatusActivity.this,"Your Purchase failed", Toast.LENGTH_SHORT).show();
                            Glide.with(paymentStatusActivity.this).load(R.drawable.high_alert_icon).into(status_icon);
                            status_text.setText("Transaction Failed");
                            presenter.updatePaymentStatus(orderId, "fail");

                        }

                    } catch (AuthenticationException e) {
                        e.printStackTrace();
                    } catch (InvalidRequestException e) {
                        e.printStackTrace();
                    } catch (APIConnectionException e) {
                        e.printStackTrace();
                    } catch (APIException e) {
                        e.printStackTrace();
                    }
                }
            });

        }
        else
        {
            if (status.equals("success")){
                //Toast.makeText(paymentStatusActivity.this,"Your purchase suceeded",Toast.LENGTH_SHORT).show();
                presenter.updatePaymentStatus(orderId,"paid");
            }else {
                //Toast.makeText(paymentStatusActivity.this,"Your purchase suceeded",Toast.LENGTH_SHORT).show();
                presenter.updatePaymentStatus(orderId,"fail");
            }

        }

    }


    @Override
    public void onPaymentUpdateStatusSuccess(Response<JsonElement> response) {
        PreferenceManager.setString(Constant.IS_PURCHASE_DONE,"true");
        finish();
    }

    @Override
    public void onPaymentUpdateStatusFailure(String error) {
        paymentIssueMessage.setVisibility(View.VISIBLE);
    }
}
