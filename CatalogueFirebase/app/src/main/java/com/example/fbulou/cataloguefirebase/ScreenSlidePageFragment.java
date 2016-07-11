package com.example.fbulou.cataloguefirebase;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import uk.co.senab.photoview.PhotoViewAttacher;

public class ScreenSlidePageFragment extends Fragment {

    private PhotoViewAttacher mAttacher;
    ImageView image;

    public ScreenSlidePageFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_screen_slide_page, container, false);

        TextView title = (TextView) rootView.findViewById(R.id.mTitle);
        image = (ImageView) rootView.findViewById(R.id.mImage);

        long pos = getArguments().getInt("position");
        String imgPath = MainActivity.Instance.posInfoMap.get(pos).imagePath;
        String description = MainActivity.Instance.posInfoMap.get(pos).title;


        Glide.with(this)
                .load(imgPath)
                .placeholder(R.drawable.default_img)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        mAttacher = new PhotoViewAttacher(image);
                        mAttacher.update();
                        // Toast.makeText(getActivity(), "Double Tap or Pinch to Zoom", Toast.LENGTH_SHORT).show();


                        return false;
                    }
                })
                .into(image);

        title.setText(description);

        // Inflate the layout for this fragment
        return rootView;
    }

}
