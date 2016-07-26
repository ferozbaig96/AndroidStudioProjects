package com.example.fbulou.tasksorrted;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.Collections;
import java.util.List;

public class MapPhotosActRVAdapter extends RecyclerView.Adapter<MapPhotosActRVAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    List<InformationMapPhotos> data = Collections.emptyList();

    public MapPhotosActRVAdapter(Context context, List<InformationMapPhotos> data) {
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = inflater.inflate(R.layout.item_layout_map_photos, parent, false);
        return new MyViewHolder(itemLayoutView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        InformationMapPhotos curObj = data.get(position);

        Glide.with(MapPhotosActivity.Instance)
                .load(curObj.url)
                .centerCrop()
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;

        public MyViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.imageview);
        }
    }
}
