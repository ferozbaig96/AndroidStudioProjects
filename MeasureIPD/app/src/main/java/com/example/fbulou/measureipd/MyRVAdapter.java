package com.example.fbulou.measureipd;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
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
        holder.btnTitle.setText(curObj.title);
        holder.index.setText(curObj.index);

        holder.btnTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DioptreActivity.getInstance().mIndex = Double.parseDouble(curObj.index);

                int lenCylin = DioptreActivity.getInstance().mEdittextCylindrical.getText().length();
                int lenAxis = DioptreActivity.getInstance().mEdittextAxis.getText().length();
                String strSph = DioptreActivity.getInstance().mEdittextSpherical.getText().toString();
                float Sph = 0;

                if (strSph.length() != 0)
                    Sph = Float.parseFloat(strSph);
                else
                    Snackbar.make(DioptreActivity.getInstance().fab, "Provide both Spherical power", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

                DioptreActivity.getInstance().canvas.drawColor(0, PorterDuff.Mode.CLEAR);
                DrawLens.showLens(DioptreActivity.getInstance().canvas, (int) (Sph * 100), 50);
                DioptreActivity.getInstance().mLensLinearLayout.setBackground(new BitmapDrawable(DioptreActivity.getInstance().bg));

                //Validation of Cylindrical Power and Cylindrical Axis
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

        Button btnTitle;
        TextView index;
        //ImageView icon;

        public MyViewHolder(View itemView) {
            super(itemView);

            this.btnTitle = (Button) itemView.findViewById(R.id.id_RVtitle);
            this.index = (TextView) itemView.findViewById(R.id.id_RVindex);
            //icon = (ImageView) itemView.findViewById(R.id.id_RVicon);
        }
    }
}
