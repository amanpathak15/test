package com.mindwarriorhack.app;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintSet;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.OnPhotoTapListener;
import com.github.chrisbanes.photoview.PhotoView;

import java.util.Timer;

public class PostDetailedScreen extends AppCompatActivity
{
    private String image;
    private PhotoView imageViewDetailed;
    private ImageView backPostDetailed;
    private TextView textMessage;
    private boolean isToolbarVisible = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        View view = LayoutInflater.from(this).inflate(R.layout.activity_post_detailed_screen,null,false);
        setContentView(view);
        initUI(view);
    }

    private void initUI(View view){
        if(getIntent().hasExtra("image")){
            image = getIntent().getStringExtra("image");
        }

        /*if (getIntent().hasExtra("message")){
            postTextMessage=getIntent().getStringExtra("message");
        }*/

        imageViewDetailed=findViewById(R.id.imageViewDetailed);
        Glide.with(this).load(image).into(imageViewDetailed);
        backPostDetailed=findViewById(R.id.backPostDetailed);
        backPostDetailed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                isToolbarVisible = false;
                backPostDetailed.setVisibility(View.INVISIBLE);
            }
        },2000);


        imageViewDetailed.setOnPhotoTapListener(new OnPhotoTapListener() {
            @Override
            public void onPhotoTap(ImageView view, float x, float y) {

                isToolbarVisible = true;
                backPostDetailed.setVisibility(View.VISIBLE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isToolbarVisible = false;
                        backPostDetailed.setVisibility(View.INVISIBLE);

                    }
                },2000);


            }
        });


/*        textMessage=findViewById(R.id.textMessage);

        textMessage.setMovementMethod(new ScrollingMovementMethod());
        textMessage.setText(postTextMessage);*/
    }
}
