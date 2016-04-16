package com.example.fbulou.myapp6_fragmentlayout;

import android.app.Fragment;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class FragmentDescription extends Fragment {

    View rootview;  //Just to get my id_Description textview from frag_description.xml file
    TextView tv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.fragment_description, container, false);
        return rootview;
    }

    @Override
    public void onStart() {
        super.onStart();

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            tv = (TextView) rootview.findViewById(R.id.id_Description);
            String text = DescriptionActivity.myTextviewString();
            tv.setText(text);
        }
    }
}
