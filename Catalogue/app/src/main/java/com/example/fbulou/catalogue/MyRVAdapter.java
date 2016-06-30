package com.example.fbulou.catalogue;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.Collections;
import java.util.List;

public class MyRVAdapter extends RecyclerView.Adapter<MyRVAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    List<Information> data = Collections.emptyList();

    public MyRVAdapter(Context context, List<Information> data) {
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = inflater.inflate(R.layout.item_layout, parent, false);
        return new MyViewHolder(itemLayoutView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        Information curObj = data.get(position);
        holder.title.setText(curObj.title);

        Glide.with(MainActivity.getInstance())
                 .load(curObj.imageID)
                //.load(curObj.imagePath)
                .placeholder(R.drawable.default_img)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .centerCrop()
                .into(holder.icon);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView title;
        ImageView icon;

        public MyViewHolder(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.id_RVtitle);
            icon = (ImageView) itemView.findViewById(R.id.id_RVicon);

            ViewGroup.LayoutParams layoutParams = icon.getLayoutParams();
            layoutParams.width = MainActivity.Instance.getEffectiveHeight() / 2;
            icon.setLayoutParams(layoutParams);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            MainActivity.Instance.onClicked(getAdapterPosition());

        }
    }
}