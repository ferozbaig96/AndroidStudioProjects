package com.example.fbulou.taskaftertax;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;

// Description in assets/Task - Android.pdf

public class MainActivity extends AppCompatActivity {

    TextView categoryName, amounts, result;
    Button n0, n1, n2, n3, n4, n5, n6, n7, n8, n9, nDel, nClearAll, food, clothes, wine, beer;

    double res = 0, tax = .20;
    long[] a;             // Stores amount for each category in order

    int category = 0;       // By Default, Food Category

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initialise();
        setupCategoriesListeners();
        setupNumberListeners();
    }

    private void setupNumberListeners() {
        n0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calAmount(0);
                setMyTextAmount();
            }
        });

        n1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calAmount(1);
                setMyTextAmount();
            }
        });

        n2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calAmount(2);
                setMyTextAmount();
            }
        });

        n3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calAmount(3);
                setMyTextAmount();
            }
        });

        n4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calAmount(4);
                setMyTextAmount();
            }
        });

        n5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calAmount(5);
                setMyTextAmount();
            }
        });

        n6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calAmount(6);
                setMyTextAmount();
            }
        });

        n7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calAmount(7);
                setMyTextAmount();
            }
        });

        n8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calAmount(8);
                setMyTextAmount();
            }
        });

        n9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calAmount(9);
                setMyTextAmount();
            }
        });

        nDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                a[category] = a[category] / 10;
                double total = a[0] + a[1] + a[2] + a[3];
                res = total * (1 + tax);

                result.setText(String.format(Locale.ENGLISH, "%.2f", res));
                setMyTextAmount();
            }
        });

        nClearAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                a[category] = 0;
                double total = a[0] + a[1] + a[2] + a[3];
                res = total * (1 + tax);

                result.setText(String.format(Locale.ENGLISH, "%.2f", res));
                setMyTextAmount();
            }
        });

    }

    private void calAmount(int x) {
        a[category] = (a[category] * 10) + x;

        double total = a[0] + a[1] + a[2] + a[3];
        res = total * (1 + tax);

        result.setText(String.format(Locale.ENGLISH, "%.2f", res));
    }

    private void setMyTextAmount() {
        String s = "Rs. ";
        String plus = " + Rs. ";

        for (long tmp : a) {
            if (tmp != 0)
                s = s + tmp + plus;
        }

        if (s.length() > 5)
            s = s.substring(0, s.length() - plus.length());
        else
            s = "";

        amounts.setText(s);
    }

    private void initialise() {

        a = new long[4];

        categoryName = (TextView) findViewById(R.id.category_name);
        amounts = (TextView) findViewById(R.id.amounts);
        result = (TextView) findViewById(R.id.result);

        n0 = (Button) findViewById(R.id.n0);
        n1 = (Button) findViewById(R.id.n1);
        n2 = (Button) findViewById(R.id.n2);
        n3 = (Button) findViewById(R.id.n3);
        n4 = (Button) findViewById(R.id.n4);
        n5 = (Button) findViewById(R.id.n5);
        n6 = (Button) findViewById(R.id.n6);
        n7 = (Button) findViewById(R.id.n7);
        n8 = (Button) findViewById(R.id.n8);
        n9 = (Button) findViewById(R.id.n9);
        nDel = (Button) findViewById(R.id.ndel);
        nClearAll = (Button) findViewById(R.id.nClearAll);

        food = (Button) findViewById(R.id.category_food);
        clothes = (Button) findViewById(R.id.category_clothes);
        wine = (Button) findViewById(R.id.category_wine);
        beer = (Button) findViewById(R.id.category_beer);


    }

    private void setupCategoriesListeners() {

        food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                categoryName.setText("Food");
                category = 0;
            }
        });

        clothes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                categoryName.setText("Clothes");
                category = 1;
            }
        });

        wine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                categoryName.setText("Wine");
                category = 2;
            }
        });

        beer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                categoryName.setText("Beer");
                category = 3;
            }
        });


    }
}