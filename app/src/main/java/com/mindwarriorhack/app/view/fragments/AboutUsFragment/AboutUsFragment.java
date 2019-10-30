package com.mindwarriorhack.app.view.fragments.AboutUsFragment;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mindwarriorhack.app.R;
import com.mindwarriorhack.app.presenter.AboutUsPresenter;
import com.mindwarriorhack.app.view.Settings.SettingsActivity;

import retrofit2.Response;

public class AboutUsFragment extends Fragment implements AboutUsView{

    TextView emailView, phoneView, aboutUsView;
    AboutUsPresenter presenter;
    ProgressBar progressBar;
    RelativeLayout mainLayout;
    AppCompatActivity context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_about_us, container, false);;
        initUI(view);
        return view;

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = (AppCompatActivity) context;
    }

    public void initUI(View view) {
        emailView = view.findViewById(R.id.email);
        phoneView = view.findViewById(R.id.phone);
        aboutUsView = view.findViewById(R.id.intro);
        progressBar = view.findViewById(R.id.progressBar);
        mainLayout = view.findViewById(R.id.mainLayout);
        presenter = new AboutUsPresenter(this, getActivity());
        presenter.aboutUs();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = context.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(context,R.color.colorPrimaryDark));
        }

    }

    @Override
    public void onSuccess(Response<JsonElement> response) {

        JsonObject responseObjectData = response.body().getAsJsonObject().get("responseData").getAsJsonObject();
        String description = responseObjectData.get("description").getAsString();
        String email = responseObjectData.get("email").getAsString();
        String contact = responseObjectData.get("contact").getAsString();
        emailView.setText(email);
        phoneView.setText(contact);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            aboutUsView.setText(Html.fromHtml(description, Html.FROM_HTML_MODE_COMPACT));
        } else {
            aboutUsView.setText(Html.fromHtml(description));
        }

        progressBar.setVisibility(View.GONE);
        mainLayout.setVisibility(View.VISIBLE);

    }

    @Override
    public void onFailure(String error) {

    }

}
