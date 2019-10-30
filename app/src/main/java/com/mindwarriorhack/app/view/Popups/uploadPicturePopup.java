package com.mindwarriorhack.app.view.Popups;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.mindwarriorhack.app.R;


public class uploadPicturePopup extends Dialog {


    Context context;
    String message = "Upload";
    uploadPictureInterface uploadPictureInterface;

    public uploadPicturePopup(Context context, uploadPictureInterface uploadPictureInterface) {
        super(context);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.context = context;
        this.uploadPictureInterface = uploadPictureInterface;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.uploadpicture_popup);
        setCancelable(true);
        setCanceledOnTouchOutside(true);
        TextView messageView = findViewById(R.id.choosePhotoText);
        messageView.setText(message);

        FrameLayout galleryButton = findViewById(R.id.imageLayout);
        ImageView closeButton = findViewById(R.id.close);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        galleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadPictureInterface.picturesFromGallery();
                dismiss();
            }
        });
        FrameLayout cameraButton = findViewById(R.id.cameraLayout);
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadPictureInterface.picturesFromCamera();
                dismiss();
            }
        });


    }

    public interface uploadPictureInterface {

        void picturesFromGallery();

        void picturesFromCamera();

    }


}
