package com.mindwarriorhack.app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.ligl.android.widget.iosdialog.IOSDialog;
import com.mindwarriorhack.app.view.Payment.PaymentActivity;
import com.mindwarriorhack.app.view.PaymentStatusActivity.paymentStatusActivity;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.exception.APIConnectionException;
import com.stripe.android.exception.APIException;
import com.stripe.android.exception.AuthenticationException;
import com.stripe.android.exception.InvalidRequestException;
import com.stripe.android.model.Card;
import com.stripe.android.model.PaymentIntent;
import com.stripe.android.model.PaymentIntentParams;
import com.stripe.android.model.PaymentMethod;
import com.stripe.android.model.PaymentMethodCreateParams;
import com.stripe.android.model.Token;
import com.stripe.android.view.CardMultilineWidget;
import com.stripe.model.Source;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class StripePayment extends AppCompatActivity implements View.OnClickListener {
    ImageView backStripePayment;
    Button stripeButton;
    CardMultilineWidget cardDetails;
    int orderId;
    String clientKey;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stripe_payment);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.statusBarColor));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.statusBarColor));
        }

        if (getIntent().hasExtra("OrderId")) {
            orderId = getIntent().getIntExtra("OrderId", -1);
        }

        if (getIntent().hasExtra("clientSecret")) {
            clientKey = getIntent().getStringExtra("clientSecret");
        }

        backStripePayment = findViewById(R.id.backStripePayment);
        backStripePayment.setOnClickListener(this);
        stripeButton = findViewById(R.id.stripeButton);
        stripeButton.setOnClickListener(this);
        cardDetails = findViewById(R.id.cardDetails);
        initProgressBar();

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backStripePayment:
                finish();
                break;

            case R.id.stripeButton:
                getCardDetails();
                break;
        }
    }


    private void getCardDetails() {
        final Card card = cardDetails.getCard();


        if (card == null) {
            customDialog("Invalid Card Details");
        } else {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            progressBar.setVisibility(View.VISIBLE);
            //key is live look in values.xml for changing to test
            final Stripe stripe = new Stripe(this, getString(R.string.test_publishable_key));

            PaymentMethodCreateParams paymentMethodCreateParams =
                    PaymentMethodCreateParams.create(card.toPaymentMethodParamsCard(), null);
            final PaymentIntentParams paymentIntentParams =
                    PaymentIntentParams.createConfirmPaymentIntentWithPaymentMethodCreateParams(
                            paymentMethodCreateParams, clientKey,
                            getString(R.string.redirect_url));

            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    //key is live look in values.xml for changing to test
                    try {
                        PaymentIntent paymentIntent = stripe.confirmPaymentIntentSynchronous(
                                paymentIntentParams,
                                getString(R.string.test_publishable_key)
                        );

                        PaymentIntent.Status status = PaymentIntent.Status
                                .fromCode(paymentIntent.getStatus());
                        if (PaymentIntent.Status.RequiresAction == status) {
                            Uri redirectUrl = paymentIntent.getRedirectUrl();
                            if (redirectUrl != null) {
                                progressBar.setVisibility(View.INVISIBLE);
                                startActivity(new Intent(Intent.ACTION_VIEW, redirectUrl));
                            }
                        } else {

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    progressBar.setVisibility(View.INVISIBLE);
                                    Intent intent = new Intent(StripePayment.this, paymentStatusActivity.class);
                                    intent.putExtra("status", "success");
                                    intent.putExtra("orderId", orderId);
                                    startActivity(intent);
                                    finish();
                                }
                            });

                        }

                    } catch (AuthenticationException e) {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setVisibility(View.INVISIBLE);
                                Intent intent = new Intent(StripePayment.this, paymentStatusActivity.class);
                                intent.putExtra("status", "fail");
                                intent.putExtra("orderId", orderId);
                                startActivity(intent);
                                finish();
                            }
                        });

                    } catch (InvalidRequestException e) {

                        String a = e.toString();

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setVisibility(View.INVISIBLE);
                                Intent intent = new Intent(StripePayment.this, paymentStatusActivity.class);
                                intent.putExtra("status", "fail");
                                intent.putExtra("orderId", orderId);
                                startActivity(intent);
                                finish();
                            }
                        });

                        e.printStackTrace();
                    } catch (APIConnectionException e) {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setVisibility(View.INVISIBLE);
                                Intent intent = new Intent(StripePayment.this, paymentStatusActivity.class);
                                intent.putExtra("status", "fail");
                                intent.putExtra("orderId", orderId);
                                startActivity(intent);
                                finish();
                            }
                        });

                        e.printStackTrace();
                    } catch (APIException e) {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setVisibility(View.INVISIBLE);
                                Intent intent = new Intent(StripePayment.this, paymentStatusActivity.class);
                                intent.putExtra("status", "fail");
                                intent.putExtra("orderId", orderId);
                                startActivity(intent);
                                finish();
                            }
                        });


                        e.printStackTrace();
                    }

                }


            });


        }


    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        progressBar.setVisibility(View.INVISIBLE);
        Intent startFreshIntent = new Intent(StripePayment.this, paymentStatusActivity.class);
        startFreshIntent.putExtra("orderId", orderId);

        if (intent.getData() != null) {
            startFreshIntent.setData(intent.getData());
        }
        startActivity(startFreshIntent);
        finish();
    }


    public void customDialog(String alert) {
        new IOSDialog.Builder(this)
                .setTitle("Error")
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
        progressBar = new ProgressBar(StripePayment.this, null, android.R.attr.progressBarStyle);
        progressBar.setIndeterminate(true);

        RelativeLayout relativeLayout = new RelativeLayout(StripePayment.this);
        relativeLayout.setGravity(Gravity.CENTER);
        relativeLayout.addView(progressBar);

        RelativeLayout.LayoutParams params = new
                RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        progressBar.setVisibility(View.INVISIBLE);

        this.addContentView(relativeLayout, params);
    }
}
