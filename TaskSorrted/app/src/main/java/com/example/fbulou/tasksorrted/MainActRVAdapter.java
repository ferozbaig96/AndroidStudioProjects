package com.example.fbulou.tasksorrted;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

public class MainActRVAdapter extends RecyclerView.Adapter<MainActRVAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    List<InformationMain> data = Collections.emptyList();

    public MainActRVAdapter(Context context, List<InformationMain> data) {
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = inflater.inflate(R.layout.item_layout_main, parent, false);
        return new MyViewHolder(itemLayoutView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        InformationMain curObj = data.get(position);
        holder.title.setText(curObj.title);
        holder.types.setText(curObj.types);
        holder.distance.setText(curObj.distance);
        holder.address.setText(curObj.address);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView title, types, distance, address;

        public MyViewHolder(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.id_RVtitle);
            types = (TextView) itemView.findViewById(R.id.types);
            distance = (TextView) itemView.findViewById(R.id.distance);
            address = (TextView) itemView.findViewById(R.id.address);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            MainActivity.Instance.onClicked(position);
        }
    }
}
