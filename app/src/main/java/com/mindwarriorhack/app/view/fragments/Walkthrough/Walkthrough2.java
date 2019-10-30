package com.mindwarriorhack.app.view.fragments.Walkthrough;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mindwarriorhack.app.R;

public class Walkthrough2 extends Fragment {

    private AppCompatActivity context;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context= (AppCompatActivity) context;
    }
    public Walkthrough2() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_walkthrough2,container,false);

        ImageView imageView=container.findViewById(R.id.image);

        return view;
    }

}
