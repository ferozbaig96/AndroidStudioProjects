package com.example.fbulou.myrecyclerviewmultiselectdelete;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyRVAdapter extends RecyclerView.Adapter<MyRVAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    List<Information> data = Collections.emptyList();

   // static MyRVAdapter myRVAdapter;
    //static Map<Integer, View> posViewMap = new HashMap<>();           //can use this to get the whole itemView - view for an item
    static Map<Integer, ImageView> posImgMap = new HashMap<>();
    boolean longClickActivated = false;

    public MyRVAdapter(Context context, List<Information> data) {
        inflater = LayoutInflater.from(context);
        this.data = data;

      //  myRVAdapter = this;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = inflater.inflate(R.layout.item_layout, parent, false);
        MyViewHolder holder = new MyViewHolder(itemLayoutView);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        Information curObj = data.get(position);
        holder.title.setText(curObj.title);
        holder.icon.setImageResource(curObj.imageID);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        TextView title;
        ImageView icon;
        View mItemView;  //can be used in static Map<Integer, View> posViewMap
        ImageView tick;

        public MyViewHolder(View itemView) {
            super(itemView);
            mItemView = itemView;

            title = (TextView) itemView.findViewById(R.id.id_RVtitle);
            icon = (ImageView) itemView.findViewById(R.id.id_RVicon);
            tick = (ImageView) itemView.findViewById(R.id.tick);

            itemView.setOnClickListener(this);  //setting clickListener on the whole itemView
            itemView.setOnLongClickListener(this);    //setting longClickListener on the whole itemView
        }

        @Override
        public void onClick(View v) {

            if (longClickActivated) {
                int position = getAdapterPosition();
                performSelections(position);
            }
        }

        @Override
        public boolean onLongClick(View v) {

            longClickActivated = true;
            int position = getAdapterPosition();
            performSelections(position);
            return true;         //explicitly set to true to make it work
        }

        private void performSelections(int position) {
            if (position != -1 && position < data.size()) {
                if (data.get(position).isSelected) {
                    // posViewMap.remove(position);
                    posImgMap.remove(position);
                    data.get(position).isSelected = false;

                    //--icon.setAlpha(0.5f);

                    tick.setVisibility(View.GONE);
                    Log.e("TAG", "Unselected at pos " + position);

                } else {
                    // posViewMap.put(position, mItemView);
                    /* -- posImgMap.put(position, icon);
                    icon.setAlpha(0.5f);*/

                    posImgMap.put(position, tick);
                    data.get(position).isSelected = true;

                    tick.setVisibility(View.VISIBLE);
                    Log.e("TAG", "Selected at pos " + position);
                }
            }

            if (posImgMap.size() == 0) {
                longClickActivated = false;
                MainActivity.getInstance().mMenu.findItem(R.id.action_delete_long_press).setVisible(false);
                MainActivity.getInstance().mMenu.findItem(R.id.action_settings).setVisible(true);
                MainActivity.getInstance().getSupportActionBar().setTitle("My App");
                MainActivity.getInstance().getSupportActionBar().setDisplayHomeAsUpEnabled(false);

            } else {
                MainActivity.getInstance().mMenu.findItem(R.id.action_delete_long_press).setVisible(true);
                MainActivity.getInstance().mMenu.findItem(R.id.action_settings).setVisible(false);
                MainActivity.getInstance().getSupportActionBar().setTitle("" + posImgMap.size());
                MainActivity.getInstance().getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }

        }
    }


}
