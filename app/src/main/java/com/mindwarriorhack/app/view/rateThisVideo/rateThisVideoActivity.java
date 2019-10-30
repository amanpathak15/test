package com.mindwarriorhack.app.view.rateThisVideo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterInside;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonElement;
import com.hsalf.smilerating.BaseRating;
import com.hsalf.smilerating.SmileRating;
import com.mindwarriorhack.app.R;
import com.mindwarriorhack.app.helper.Constant;
import com.mindwarriorhack.app.manager.PreferenceManager;
import com.mindwarriorhack.app.model.Packages.PackagesResponse;
import com.mindwarriorhack.app.presenter.rateThisVideoPresenter;
import com.mindwarriorhack.app.view.Purchase.PurchaseActivity;
import com.mindwarriorhack.app.view.VideoList.VideoListActivity;

import retrofit2.Response;

public class rateThisVideoActivity extends AppCompatActivity implements View.OnClickListener, rateThisVideoView{

    private String videoName, videoUrl, videoId;
    private ImageView videoThumbnail;
    private Button submitButton;
    private RelativeLayout relativeLayoutRate;
    private TextView skip;
    int rate = 0;
    private rateThisVideoPresenter rateThisVideoPresenter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rate_this_video);
        initViews();
        SmileRating smileRating = (SmileRating) findViewById(R.id.smileRating);
        smileRating.setOnSmileySelectionListener(new SmileRating.OnSmileySelectionListener() {
            @Override
            public void onSmileySelected(@BaseRating.Smiley int smiley, boolean reselected) {
                // reselected is false when user selects different smiley that previously selected one
                // true when the same smiley is selected.
                // Except if it first time, then the value will be false.
                switch (smiley) {
                    case SmileRating.TERRIBLE:
                        rate = 1;
                        break;
                    case SmileRating.BAD:
                        rate = 2;
                        break;
                    case SmileRating.OKAY:
                        rate = 3;
                        break;
                    case SmileRating.GOOD:
                        rate = 4;
                        break;
                    case SmileRating.GREAT:
                        rate = 5;
                        break;
                }
            }
        });



    }


    private void initViews(){

        if(getIntent().hasExtra("imageThumbnail")){
            videoUrl = getIntent().getStringExtra("imageThumbnail");
            ImageView videoThumbnail = (ImageView) findViewById(R.id.thumbnail);
            RequestOptions requestOptions = new RequestOptions();
            requestOptions = requestOptions.transforms(new CenterInside(), new RoundedCorners(30));
            Glide.with(this).load(videoUrl).apply(requestOptions).into(videoThumbnail);
        }

        if(getIntent().hasExtra("videoName")){
            videoName = getIntent().getStringExtra("videoName");
        }

        if(getIntent().hasExtra("videoId")){
            videoId = getIntent().getStringExtra("videoId");
        }
        skip=findViewById(R.id.skip);
        skip.setOnClickListener(this);
        relativeLayoutRate=findViewById(R.id.relativeLayoutRate);

        rateThisVideoPresenter = new rateThisVideoPresenter(this,this);

        TextView videoNameView = (TextView) findViewById(R.id.videoTitle);
        videoNameView.setText(videoName);
        TextView toolbarTitle = (TextView) findViewById(R.id.toolbar_text);
        ImageView back = findViewById(R.id.back);
        back.setOnClickListener(this);
        toolbarTitle.setText("Rate this Video");
        submitButton = (Button) findViewById(R.id.submitButton);
        submitButton.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.submitButton:
                rateTheVideo(videoId, rate);
                Snackbar snackbar = Snackbar
                        .make(relativeLayoutRate, videoName + " is rated successfully",Snackbar.LENGTH_LONG);
                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(ContextCompat.getColor(rateThisVideoActivity.this,R.color.statusBarColor));
                snackbar.getView().getLayoutParams().width= ViewGroup.LayoutParams.MATCH_PARENT;
                snackbar.show();
                //finish();
                break;

            case R.id.back:
                finish();
                break;

            case R.id.skip:
                finish();
                break;
        }
    }


    private void rateTheVideo(String videoId, int rate){
        rateThisVideoPresenter.rateThisVideoApi(PreferenceManager.getString(Constant.USER_ID), videoId, rate);
    }


    @Override
    public void onRateSuccess(Response<JsonElement> response) {
        finish();
    }

    @Override
    public void onRateFailure(String errorMsg) {

    }
}
