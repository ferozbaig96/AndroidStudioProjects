package com.example.fbulou.tinderswipe;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

/*
	Mixture of : 

	https://github.com/Diolor/Swipecards

	and

	http://blog.nkdroidsolutions.com/android-swipe-cards-like-tinder-app/
*/

    Button left, right;
    public static MyAppAdapter myAppAdapter;
    public static MyViewHolder viewHolder;
    private SwipeFlingAdapterView flingContainer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        right = (Button) findViewById(R.id.right);
        left = (Button) findViewById(R.id.left);

        showSwipeCards();
    }

    private void showSwipeCards() {

        //add the view via xml or programmatically
        flingContainer = (SwipeFlingAdapterView) findViewById(R.id.frame);

        final ArrayList<Data> al = new ArrayList<>();
        al.add(new Data("http://i.ytimg.com/vi/PnxsTxV8y3g/maxresdefault.jpg", "Image 1"));
        al.add(new Data("http://i.imgur.com/Lq7dyu0.jpg", "Image 2"));
        al.add(new Data("http://i.ytimg.com/vi/PnxsTxV8y3g/maxresdefault.jpg", "Image 3"));
        al.add(new Data("http://i.imgur.com/Lq7dyu0.jpg", "Image 4"));
        al.add(new Data("http://i.ytimg.com/vi/PnxsTxV8y3g/maxresdefault.jpg", "Image 5"));

        //set the listener and the adapter
        myAppAdapter = new MyAppAdapter(al, MainActivity.this);
        flingContainer.setAdapter(myAppAdapter);

        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                // this is the simplest way to delete an object from the Adapter (/AdapterView)
                Log.e("LIST", "removed object!");
                al.remove(0);
                myAppAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                //Do something on the left!
                //You also have access to the original object.
                //If you want to use it just cast it (String) dataObject
                Toast.makeText(MainActivity.this, "Left!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                Toast.makeText(MainActivity.this, "Right!", Toast.LENGTH_SHORT).show();
            }


            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
                // Ask for more data here

                if (itemsInAdapter == 0) {
                    right.setEnabled(false);
                    left.setEnabled(false);
                } else {
                    right.setEnabled(true);
                    left.setEnabled(true);
                }

            }

            @Override
            public void onScroll(float scrollProgressPercent) {
                View view = flingContainer.getSelectedView();
                view.findViewById(R.id.item_swipe_right_indicator).setAlpha(scrollProgressPercent < 0 ? -scrollProgressPercent : 0);
                view.findViewById(R.id.item_swipe_left_indicator).setAlpha(scrollProgressPercent > 0 ? scrollProgressPercent : 0);

            }
        });

        // Optionally add an OnItemClickListener
        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {
                Toast.makeText(MainActivity.this, "Clicked", Toast.LENGTH_SHORT).show();
            }
        });


        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flingContainer.getTopCardListener().selectRight();
            }
        });

        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flingContainer.getTopCardListener().selectLeft();
            }
        });
    }


    public static class MyViewHolder {
        public static FrameLayout background;
        public TextView DataText;
        public ImageView cardImage;
    }

    public class MyAppAdapter extends BaseAdapter {

        public List<Data> parkingList;
        public Context context;

        private MyAppAdapter(List<Data> apps, Context context) {
            this.parkingList = apps;
            this.context = context;
        }

        @Override
        public int getCount() {
            return parkingList.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            View rowView = convertView;


            if (rowView == null) {

                LayoutInflater inflater = getLayoutInflater();
                rowView = inflater.inflate(R.layout.item, parent, false);
                // configure view holder
                viewHolder = new MyViewHolder();
                viewHolder.DataText = (TextView) rowView.findViewById(R.id.bookText);
                viewHolder.background = (FrameLayout) rowView.findViewById(R.id.background);
                viewHolder.cardImage = (ImageView) rowView.findViewById(R.id.cardImage);
                rowView.setTag(viewHolder);

            } else {
                viewHolder = (MyViewHolder) convertView.getTag();
            }
            viewHolder.DataText.setText(parkingList.get(position).getDescription());

            Glide.with(MainActivity.this).load(parkingList.get(position).getImagePath()).into(viewHolder.cardImage);

            return rowView;
        }
    }

    //MENUS
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
