package com.example.fbulou.measureipd;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

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
        MyViewHolder holder = new MyViewHolder(itemLayoutView);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        final Information curObj = data.get(position);
        holder.title.setText(curObj.title);
        holder.index.setText(curObj.index);

        holder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DioptreActivity.getInstance().mIndex = Double.parseDouble(curObj.index);

                int lenCylin = DioptreActivity.getInstance().mEdittextCylindrical.getText().length();
                int lenAxis = DioptreActivity.getInstance().mEdittextAxis.getText().length();

                if (lenAxis + lenCylin > 0 && lenAxis * lenCylin == 0) {
                    Snackbar.make(DioptreActivity.getInstance().fab, "Provide both Cylindrical Power and Axis or none", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }

            }
        });
        //holder.icon.setImageResource(curObj.imageID);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        Button title;
        TextView index;
        //ImageView icon;

        public MyViewHolder(View itemView) {
            super(itemView);

            this.title = (Button) itemView.findViewById(R.id.id_RVtitle);
            this.index = (TextView) itemView.findViewById(R.id.id_RVindex);
            //icon = (ImageView) itemView.findViewById(R.id.id_RVicon);
        }
    }
}
