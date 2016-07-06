package com.example.fbulou.catalogue;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.Collections;
import java.util.List;

public class MyRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private LayoutInflater inflater;
    List<Information> data = Collections.emptyList();

    final int VIEW_PROG = 0;
    final int VIEW_ITEM = 1;

    private int visibleThreshold = 1;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;

    public MyRVAdapter(Context context, List<Information> data) {
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    public void setMyOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public void setRecyclerView(RecyclerView recyclerView) {

        final LinearLayoutManager llm = (LinearLayoutManager) recyclerView.getLayoutManager();

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                totalItemCount = llm.getItemCount();
                lastVisibleItem = llm.findLastVisibleItemPosition();

                if (!loading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    // End has been reached
                    // Do something
                    if (onLoadMoreListener != null) {
                        onLoadMoreListener.onLoadMore();
                    }
                    loading = true;
                }
            }
        });
    }

    public void setLoaded() {
        loading = false;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder holder;
        if (viewType == VIEW_ITEM) {
            View itemLayoutView = inflater.inflate(R.layout.item_layout, parent, false);
            holder = new MyViewHolder(itemLayoutView);

        } else {

            View itemLayoutView = inflater.inflate(R.layout.item_progress_bar, parent, false);
            holder = new MyProgressViewHolder(itemLayoutView);
        }

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof MyViewHolder) {
            Information curObj = data.get(position);
            ((MyViewHolder) holder).title.setText(curObj.title);

            Glide.with(MainActivity.getInstance())
                    .load(curObj.imageID)
                    //.load(curObj.imagePath)
                    .placeholder(R.drawable.default_img)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .centerCrop()
                    .into(((MyViewHolder) holder).icon);
        }
    }

    //must override for multiple viewtype
    @Override
    public int getItemViewType(int position) {
        return data.get(position) != null ? VIEW_ITEM : VIEW_PROG;
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
            layoutParams.height = MainActivity.Instance.getEffectiveWidth() / 2;
            icon.setLayoutParams(layoutParams);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            MainActivity.Instance.onClicked(getAdapterPosition());

        }
    }

    class MyProgressViewHolder extends RecyclerView.ViewHolder {

        View loadingIndicatorView;

        public MyProgressViewHolder(View itemView) {
            super(itemView);

            loadingIndicatorView = itemView.findViewById(R.id.id_mLoadingIndicatorView);
        }
    }
}