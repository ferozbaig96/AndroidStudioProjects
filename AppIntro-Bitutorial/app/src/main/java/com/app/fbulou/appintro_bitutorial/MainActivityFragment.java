package com.app.fbulou.appintro_bitutorial;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class MainActivityFragment extends Fragment {

    private Button btnViews, btnFragments;

    public static MainActivityFragment instance() {
        return new MainActivityFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        btnViews = (Button) view.findViewById(R.id.btn_views);
        btnFragments = (Button) view.findViewById(R.id.btn_fragments);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnViews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction()
                        .replace(R.id.container, SimpleViewsFragment.instance())
                        .addToBackStack(null)
                        .commit();
            }
        });
        btnFragments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction()
                        .replace(R.id.container, FragmentsFragment.instance())
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(R.string.app_name);
    }
}
