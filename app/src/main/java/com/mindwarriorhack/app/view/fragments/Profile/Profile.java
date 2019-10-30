package com.mindwarriorhack.app.view.fragments.Profile;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.bumptech.glide.request.RequestOptions;
import com.ligl.android.widget.iosdialog.IOSDialog;
import com.mindwarriorhack.app.manager.PreferenceManager;

import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mindwarriorhack.app.R;
import com.mindwarriorhack.app.helper.Constant;
import com.mindwarriorhack.app.helper.Utils;
import com.mindwarriorhack.app.presenter.ProfilePresenter;
import com.mindwarriorhack.app.view.Home.HomeActivity;
import com.mindwarriorhack.app.view.Popups.editProfilePopup;
import com.mindwarriorhack.app.view.Popups.uploadPicturePopup;
import com.mindwarriorhack.app.view.newPostScreen.newPostScreen;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_NO_LOCALIZED_COLLATORS;
import static android.content.Context.MODE_PRIVATE;


public class Profile extends Fragment implements View.OnClickListener, uploadPicturePopup.uploadPictureInterface, ProfileView, editProfilePopup.updateData {

    final int VIDEO_FROM_CAMERA_INTENT_REQUEST_CODE = 2;
    final int VIDEO_FROM_GALLERY_INTENT_REQUEST_CODE = 3;
    final int REQUEST_WRITE_STORAGE_PERMISSION_FOR_VIDEO = 6;
    private final int PICTURE_FROM_CAMERA_INTENT_REQUEST_CODE = 1;
    private final int PICTURE_FROM_GALLERY_INTENT_REQUEST_CODE = 4;
    private final int REQUEST_WRITE_STORAGE_PERMISSION_FOR_PICTURES = 5;
    private RelativeLayout profileLayout;
    private AppCompatActivity context;
    private ProfilePresenter profilePresenter;
    private EditText textProfName, textProfEmail, textCountry;
    private Spinner spinnerGender;
    private Button btnSave;
    private ProgressBar progressBar;
    private CircleImageView profile_image;
    private Uri CamerafileUri;
    private ProgressBar profileProgressBar;
    private Uri actualProfilePicUri;
    private String compressedImagePath;
    private String selectedImagePath = "";
    private String[] gender={"Male" , "Female"};
    private Calendar calendar;
    private TextView dobInput, textPhoneNo, occupationInput;
    private ArrayAdapter<String> adapter;
    SharedPreferences sharedPreferences;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = (AppCompatActivity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        initUI(view);

        return view;

    }





    @SuppressLint("ClickableViewAccessibility")
    private void initUI(View view) {
        initProgressBar();
        profileProgressBar = view.findViewById(R.id.progressBar);
        textProfName = view.findViewById(R.id.nameInput);
        textProfEmail = view.findViewById(R.id.emailInput);
        textCountry = view.findViewById(R.id.countryInput);
        textPhoneNo = view.findViewById(R.id.phoneNoInput);
        dobInput = view.findViewById(R.id.dobInput);
        dobInput.setTextColor(Color.BLACK);
        spinnerGender=view.findViewById(R.id.genderInput);
        occupationInput=view.findViewById(R.id.occupationInput);
        adapter=new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,gender);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGender.setAdapter(adapter);
        switch (PreferenceManager.getString(Constant.USER_GENDER)){

            case "male":
                spinnerGender.setSelection(0);
                break;

            case "female":
                spinnerGender.setSelection(1);
                break;

        }

        spinnerGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
              parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        calendar=Calendar.getInstance();

        final DatePickerDialog.OnDateSetListener dateSetListener=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                dobInput.setText(formatDate(year,month,dayOfMonth));
            }
        };

        dobInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog=new DatePickerDialog(Objects.requireNonNull(getContext()),dateSetListener, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));

                //following line to restrict future date selection
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = context.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(context,R.color.colorPrimaryDark));
        }


        btnSave = view.findViewById(R.id.btnSend);
        btnSave.setOnClickListener(this);

        profilePresenter = new ProfilePresenter(context, this);
        profileLayout = view.findViewById(R.id.profileLayout);
        profile_image = view.findViewById(R.id.profile_image);
        if (!PreferenceManager.getString(Constant.PROFILE_PIC).isEmpty()) {
            Glide.with(context).load(PreferenceManager.getString(Constant.PROFILE_PIC)).into(profile_image);
        } else {
            Glide.with(context).load(R.drawable.default_profilepic).into(profile_image);
        }

        textProfName.setText(PreferenceManager.getString(Constant.USER_NAME));
        textProfEmail.setText(PreferenceManager.getString(Constant.USER_EMAIL));
        textCountry.setText(PreferenceManager.getString(Constant.USER_COUNTRY));
        textPhoneNo.setText(PreferenceManager.getString(Constant.USER_MOBILE));
        textPhoneNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editProfilePopup popup = new editProfilePopup(getActivity(),textPhoneNo.getText().toString(),"Phone",textPhoneNo.getHint().toString(), Profile.this);
                popup.show();
            }
        });
        dobInput.setText(PreferenceManager.getString(Constant.USER_DOB));
        if(PreferenceManager.getString(Constant.USER_GENDER_POSITION).isEmpty()) {
        }else {
            spinnerGender.setSelection(Integer.parseInt(PreferenceManager.getString(Constant.USER_GENDER_POSITION)));
        }
        occupationInput.setText(PreferenceManager.getString(Constant.USER_OCCUPATION));
        occupationInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editProfilePopup popup = new editProfilePopup(getActivity(),occupationInput.getText().toString(),"Occupation",occupationInput.getHint().toString(), Profile.this);
                popup.show();
            }
        });
        profileLayout.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.profileLayout:
                openUploadPicturePopup();
                break;

            case R.id.btnSend:
                String userId = PreferenceManager.getString(Constant.USER_ID);
                String name = textProfName.getText().toString().trim();
                String email = PreferenceManager.getString(Constant.USER_EMAIL);
                String country = PreferenceManager.getString(Constant.USER_COUNTRY);
                String mobNo = textPhoneNo.getText().toString().trim();
                int selectedGenderPosition= spinnerGender.getSelectedItemPosition();
                PreferenceManager.setString(Constant.USER_GENDER_POSITION, String.valueOf(selectedGenderPosition));
                String gender = spinnerGender.getSelectedItem().toString();
                String dob= dobInput.getText().toString().trim();
                String occupation= occupationInput.getText().toString().trim();

                progressBar.setVisibility(View.VISIBLE);
                profilePresenter.updateProfile(userId, name, email, country, mobNo,gender,dob,occupation);
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_WRITE_STORAGE_PERMISSION_FOR_PICTURES) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                uploadPicturePopup popup = new uploadPicturePopup(context, this);
                popup.show();
            }
        }
    }

    public void openUploadPicturePopup() {

        int permissionStorage = ContextCompat.checkSelfPermission(context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionStorage == PackageManager.PERMISSION_GRANTED) {
            uploadPicturePopup popup = new uploadPicturePopup(context, this);
            popup.show();
        } else {

            requestPermissionStorageForPictures();
        }

    }


    private void requestPermissionStorageForPictures() {

        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_STORAGE_PERMISSION_FOR_PICTURES
            );
        }
    }

    private void pickImages() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICTURE_FROM_GALLERY_INTENT_REQUEST_CODE);
    }

    @Override
    public void picturesFromGallery() {
        int permissionStorage = ContextCompat.checkSelfPermission(context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionStorage != PackageManager.PERMISSION_GRANTED) {
            requestPermissionStorageForPictures();
        }
        if (permissionStorage == PackageManager.PERMISSION_GRANTED) {
            pickImages();
        }
    }

    private static String formatDate(int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(0);
        cal.set(year, month, day);
        Date date = cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");

        return sdf.format(date);
    }

    @Override
    public void picturesFromCamera() {

        int MEDIA_TYPE_IMAGE = 1;

        int permissionStorage = ContextCompat.checkSelfPermission(context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionStorage == PackageManager.PERMISSION_GRANTED) {
            // Assume thisActivity is the current activity
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (Utils.getOutputMediaFile(MEDIA_TYPE_IMAGE) != null) {
                actualProfilePicUri = Uri.fromFile(Utils.getOutputMediaFile(MEDIA_TYPE_IMAGE));
                CamerafileUri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".helper.AppFileProvider", Utils.getOutputMediaFile(MEDIA_TYPE_IMAGE));
                intent.putExtra(MediaStore.EXTRA_OUTPUT, CamerafileUri);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivityForResult(intent, PICTURE_FROM_CAMERA_INTENT_REQUEST_CODE);
            }
        } else {
            requestPermissionStorageForPictures();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bitmap;
        if (requestCode == PICTURE_FROM_GALLERY_INTENT_REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            actualProfilePicUri = data.getData();
            selectedImagePath = getRealPathFromURI(actualProfilePicUri);
            Glide.with(context).load(selectedImagePath).into(profile_image);
            profileProgressBar.setVisibility(View.VISIBLE);

            bitmap = Utils.compressOriginalImageFileSize(selectedImagePath);
            compressedImagePath = Utils.CompressBitmap(context, bitmap, "galleryFile");

            profilePresenter.updateProfilePic(compressedImagePath);

        }
        if (requestCode == PICTURE_FROM_CAMERA_INTENT_REQUEST_CODE && resultCode == RESULT_OK) {
            Uri uri = Uri.parse(actualProfilePicUri.getEncodedPath());
            selectedImagePath = uri.toString();

            Glide.with(context).load(selectedImagePath).into(profile_image);
            profileProgressBar.setVisibility(View.VISIBLE);

            bitmap = Utils.compressOriginalImageFileSize(selectedImagePath);
            compressedImagePath = Utils.CompressBitmap(context, bitmap, "camFile");


            profilePresenter.updateProfilePic(compressedImagePath);
        }
    }


    private String getRealPathFromURI(Uri contentUri) {
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            Cursor cursor = context.managedQuery(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } catch (Exception e) {
            return contentUri.getPath();
        }
    }


    @Override
    public void profileValidations(String error, int errorView) {
        progressBar.setVisibility(View.GONE);
        customDialogFailure(error);
    }

    @Override
    public void onSuccess(String msg, String profilePicUrl, String name, String email, String country, String mobNo,String gender,String dob,String occupation) {
        progressBar.setVisibility(View.GONE);
        PreferenceManager.setString(Constant.PROFILE_PIC, profilePicUrl);
        PreferenceManager.setString(Constant.USER_NAME, name);
        PreferenceManager.setString(Constant.USER_EMAIL, email);
        PreferenceManager.setString(Constant.USER_COUNTRY, country);
        PreferenceManager.setString(Constant.USER_MOBILE, mobNo);
        PreferenceManager.setString(Constant.USER_GENDER,gender);
        PreferenceManager.setString(Constant.USER_DOB,dob);
        PreferenceManager.setString(Constant.USER_OCCUPATION,occupation);
        ((HomeActivity) context).getDrawerUserName().setText(PreferenceManager.getString(Constant.USER_NAME));
        customDialog(msg);
    }

    @Override
    public void onFailure(String error) {
        progressBar.setVisibility(View.GONE);
        if(Utils.internetConnectionAvailable(2000)){
            customDialogFailure(error);
        }else
        {
            Toast.makeText(context,"Internet Connection unavailable.Please try after sometime",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onProfilePicUploadSuccess(String msg, String profilePic) {
        profileProgressBar.setVisibility(View.INVISIBLE);
        PreferenceManager.setString(Constant.PROFILE_PIC, profilePic);
        Glide.with(context).load(PreferenceManager.getString(Constant.PROFILE_PIC)).apply(RequestOptions.circleCropTransform()).into(((HomeActivity) context).getDrawerUserProfilePic());
    }

    @Override
    public void onProfilePicUploadFailure(String msg) {
        profileProgressBar.setVisibility(View.INVISIBLE);
        customDialogFailure(msg);
    }

    private void customDialog(String msg) {
        new IOSDialog.Builder(getContext())
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    private void customDialogFailure(String msg) {
        new IOSDialog.Builder(getContext())
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }


    private void initProgressBar() {
        progressBar = new ProgressBar(getActivity(), null, android.R.attr.progressBarStyle);
        progressBar.setIndeterminate(true);

        //Changes the color of progressbar itself and not background
        // progressBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(getApplicationContext(), android.R.color.white), PorterDuff.Mode.SRC_IN);

        RelativeLayout relativeLayout = new RelativeLayout(getActivity());
        relativeLayout.setGravity(Gravity.CENTER);
        relativeLayout.addView(progressBar);

        RelativeLayout.LayoutParams params = new
                RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        progressBar.setVisibility(View.INVISIBLE);

        getActivity().addContentView(relativeLayout, params);
    }


    @Override
    public void update(String data, String type) {

        switch (type){

            case "Phone":
                textPhoneNo.setText(data);
                break;

            case "Occupation":
                occupationInput.setText(data);
                break;
        }

    }
}
