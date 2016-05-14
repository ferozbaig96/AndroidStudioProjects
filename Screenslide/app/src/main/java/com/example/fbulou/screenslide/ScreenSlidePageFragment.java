package com.example.fbulou.screenslide;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ScreenSlidePageFragment extends Fragment {


    public ScreenSlidePageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_screen_slide_page, container, false);

        TextView mTextView = (TextView) rootView.findViewById(R.id.mTextView);

        int pos = getArguments().getInt("position");

        switch (pos) {
            case 0:
                mTextView.setText(R.string.lorem_ipsum1);
                break;
            case 1:
                mTextView.setText(R.string.lorem_ipsum2);
                break;
            case 2:
                mTextView.setText(R.string.lorem_ipsum5);
                break;
            case 3:
                mTextView.setText(R.string.lorem_ipsum3);
                break;
            case 4:
                mTextView.setText(R.string.lorem_ipsum4);
                break;

            default:
                mTextView.setText(R.string.default_name);

        }

        // Inflate the layout for this fragment
        return rootView;
    }

}
