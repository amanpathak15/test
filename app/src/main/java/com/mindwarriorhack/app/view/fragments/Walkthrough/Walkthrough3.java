package com.mindwarriorhack.app.view.fragments.Walkthrough;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mindwarriorhack.app.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Walkthrough3 extends Fragment {


    public Walkthrough3() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_walkthrough3, container, false);

        ImageView imageView=container.findViewById(R.id.image);

        return  view;
    }

}
