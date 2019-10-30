package com.mindwarriorhack.app.view.newPostScreen;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.JsonElement;
import com.ligl.android.widget.iosdialog.IOSDialog;
import com.mindwarriorhack.app.R;
import com.mindwarriorhack.app.helper.Constant;
import com.mindwarriorhack.app.helper.Utils;
import com.mindwarriorhack.app.manager.PreferenceManager;
import com.mindwarriorhack.app.presenter.newPostPresenter;
import com.mindwarriorhack.app.view.Popups.uploadPicturePopup;
import com.mindwarriorhack.app.view.SignUp.SignUp;

import retrofit2.Response;

import static com.mindwarriorhack.app.helper.Utils.internetConnectionAvailable;


public class newPostScreen extends AppCompatActivity implements View.OnClickListener, newPostView, uploadPicturePopup.uploadPictureInterface {

    final int PICTURE_FROM_GALLERY_INTENT_REQUEST_CODE = 4;
    private final int PICTURE_FROM_CAMERA_INTENT_REQUEST_CODE = 1;
    private final int VIDEO_FROM_CAMERA_INTENT_REQUEST_CODE = 2;
    private final int VIDEO_FROM_GALLERY_INTENT_REQUEST_CODE = 3;
    private final int REQUEST_WRITE_STORAGE_PERMISSION_FOR_PICTURES = 5;
    private final int REQUEST_WRITE_STORAGE_PERMISSION_FOR_VIDEO = 6;
    String imageUrl;
    private com.mindwarriorhack.app.presenter.newPostPresenter newPostPresenter;
    private EditText postEditText;
    private TextView postButton, addPhotos;
    private PopupWindow feedPrivacySettingsOptionMenu;
    private boolean isFacialRecognitionComplete = false;
    private ImageView backButton, addedImage, cancelImage;
    private FrameLayout addedImageLayout;
    private ProgressBar progressBarNewPost;
    private Uri CamerafileUri;
    private Uri ActualFileUri;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newpost);
        initUI();
    }


    private void initUI() {

       /* TextView toolbarTitle = (TextView) findViewById(R.id.title);
        toolbarTitle.setText("New Post");

        ImageView backButton = (ImageView) findViewById(R.id.backButton);
        backButton.setOnClickListener(this);*/

        newPostPresenter = new newPostPresenter(this, this);
        postEditText = (EditText) findViewById(R.id.postEditText);
        addPhotos = findViewById(R.id.addPhotos);
        addPhotos.setOnClickListener(this);
        postButton = findViewById(R.id.post);
        postButton.setOnClickListener(this);
        backButton = (ImageView) findViewById(R.id.backButton);
        backButton.setOnClickListener(this);
        addedImageLayout = (FrameLayout) findViewById(R.id.addedImageLayout);
        addedImage = findViewById(R.id.addedImage);
        cancelImage = findViewById(R.id.cancelImage);
        cancelImage.setOnClickListener(this);
        progressBarNewPost = findViewById(R.id.progressBarNewPost);
        progressBarNewPost.setVisibility(View.INVISIBLE);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.post:

                if(progressBarNewPost.getVisibility() == View.VISIBLE){
                    return;
                }

                String enteredText = postEditText.getText().toString().trim();
                postEditText.setText("");
                if (imageUrl != null) {
                    progressBarNewPost.setVisibility(View.VISIBLE);
                    newPostPresenter.postImageFeed(enteredText, imageUrl);
                } else if (enteredText.length() > 0) {
                    progressBarNewPost.setVisibility(View.VISIBLE);
                    newPostPresenter.postTextFeed(enteredText);
                }
                break;

            case R.id.addPhotos:
                openUploadPicturePopup();
                break;


            case R.id.backButton:
                finish();
                break;

            case R.id.cancelImage:
                addedImage.setImageURI(null);
                addedImageLayout.setVisibility(View.GONE);
                addPhotos.setVisibility(View.VISIBLE);
                break;
        }


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_WRITE_STORAGE_PERMISSION_FOR_PICTURES) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                uploadPicturePopup popup = new uploadPicturePopup(this, this);
                popup.show();
            }
        }
    }


    @Override
    public void onPostFeedSuccess(Response<JsonElement> response) {
        progressBarNewPost.setVisibility(View.INVISIBLE);
        PreferenceManager.setString(Constant.IS_POST_CALLED, "true");
        PreferenceManager.setString(Constant.FEED_PAGINATION_URL, "https://mindwarriorhacks.app/uat/api/Api/getPost?page=1");
        postButton.setVisibility(View.VISIBLE);
        finish();
    }

    @Override
    public void onPostFeedFailure(String msg, boolean isUserBlocked) {
        progressBarNewPost.setVisibility(View.INVISIBLE);
        postButton.setVisibility(View.VISIBLE);
        if(internetConnectionAvailable(2000)){
            failureCustomDialog(msg);

        }
        else
        {
            Toast.makeText(newPostScreen.this,"Internet Connection unavailable.Please try after sometime",Toast.LENGTH_SHORT).show();
        }
    }


    public void openUploadPicturePopup() {
        int permissionStorage = ContextCompat.checkSelfPermission(newPostScreen.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionStorage == PackageManager.PERMISSION_GRANTED) {
            uploadPicturePopup popup = new uploadPicturePopup(newPostScreen.this, this);
            popup.show();
        } else {
            requestPermissionStorageForPictures();
        }

    }


    @Override
    public void picturesFromGallery() {

        int permissionStorage = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionStorage != PackageManager.PERMISSION_GRANTED) {
            requestPermissionStorageForPictures();
        }
        if (permissionStorage == PackageManager.PERMISSION_GRANTED) {
            pickImages();
        }
    }

    public void successCustomDialog(String alert) {


        new IOSDialog.Builder(newPostScreen.this)
                .setTitle("Success")
                .setMessage(alert)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                }).show();
    }

    public void failureCustomDialog(String alert) {


        new IOSDialog.Builder(newPostScreen.this)
                .setMessage(alert)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }



    @Override
    public void picturesFromCamera() {

        int MEDIA_TYPE_IMAGE = 1;


        int permissionStorage = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionStorage == PackageManager.PERMISSION_GRANTED) {
            // Assume thisActivity is the current activity
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (Utils.getOutputMediaFile(MEDIA_TYPE_IMAGE) != null) {
                ActualFileUri = Uri.fromFile(Utils.getOutputMediaFile(MEDIA_TYPE_IMAGE));
                CamerafileUri = FileProvider.getUriForFile(this, this.getApplicationContext().getPackageName() + ".helper.AppFileProvider", Utils.getOutputMediaFile(MEDIA_TYPE_IMAGE));
                intent.putExtra(MediaStore.EXTRA_OUTPUT, CamerafileUri);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivityForResult(intent, PICTURE_FROM_CAMERA_INTENT_REQUEST_CODE);
            }
        } else {
            requestPermissionStorageForPictures();
        }
    }

    private void pickImages() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICTURE_FROM_GALLERY_INTENT_REQUEST_CODE);
    }


    private void requestPermissionStorageForPictures() {

        if (ContextCompat.checkSelfPermission(newPostScreen.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_STORAGE_PERMISSION_FOR_PICTURES
            );
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICTURE_FROM_CAMERA_INTENT_REQUEST_CODE && resultCode == RESULT_OK) {
            Uri uri = Uri.parse(ActualFileUri.getEncodedPath());
            addPhotos.setVisibility(View.GONE);
            addedImage.setImageURI(uri);
            imageUrl = ActualFileUri.getEncodedPath();
            addedImageLayout.setVisibility(View.VISIBLE);

        } else if (requestCode == PICTURE_FROM_GALLERY_INTENT_REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            String url = getRealPathFromURI(uri);
            addPhotos.setVisibility(View.GONE);
            addedImage.setImageURI(uri);
            imageUrl = url;
            addedImageLayout.setVisibility(View.VISIBLE);
        }

    }


    public String getRealPathFromURI(Uri contentUri) {
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            Cursor cursor = managedQuery(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } catch (Exception e) {
            return contentUri.getPath();
        }
    }


}
