package com.example.fbulou.wizardpager.Wizard;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fbulou.wizardpager.R;


public class WizardFragment extends Fragment {

    public WizardFragment() {
        // Required empty public constructor
    }

    //TODO
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_wizard, container, false);

        ImageView mImageView = (ImageView) rootView.findViewById(R.id.imageView);
        TextView mTextView = (TextView) rootView.findViewById(R.id.mTextView);

        int position = getArguments().getInt("position");   //This position is not current position.
        /*
            For current position :
            OnCreate of WizardActivity ->  ViewPager.addOnPageChangeListener -> onPageSelected(int position)
        */


        switch (position) {

            case 0:
                //TODO set mImageView and mTextView
                mTextView.setText("Text 1");
                break;

            case 1:
                mTextView.setText("Text 2");
                break;

            case 2:
                mTextView.setText("Text 3");
                break;

            case 3:
                mTextView.setText("Text 4");
                break;

            case 4:
                mTextView.setText("Text 5");
                break;
        }

        // Inflate the layout for this fragment
        return rootView;
    }

}
