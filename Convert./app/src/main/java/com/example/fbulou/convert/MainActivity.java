package com.example.fbulou.convert;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.lang.String;

public class MainActivity extends AppCompatActivity {

    private Spinner unitTypeSpinner;
    private EditText amountTextView;
    TextView teaspoon, tablespoon, cup, ounce, pint, quart, gallon, pound, millilitre, litre, milligram, kilogram;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialiseTextView();
        addItemstoUnitTypeSpinner();
        addListenertoUnitTypeSpinner();

        amountTextView = (EditText) findViewById(R.id.amount_text_view);
        amountTextView.addTextChangedListener(myTextWatcher);


    }

    public void initialiseTextView() {
        teaspoon = (TextView) findViewById(R.id.tsp_text_view);
        tablespoon = (TextView) findViewById(R.id.tbs_text_view);
        cup = (TextView) findViewById(R.id.cup_text_view);
        ounce = (TextView) findViewById(R.id.oz_text_view);
        pint = (TextView) findViewById(R.id.pint_text_view);
        quart = (TextView) findViewById(R.id.quart_text_view);
        gallon = (TextView) findViewById(R.id.gallon_text_view);
        pound = (TextView) findViewById(R.id.pound_text_view);
        milligram = (TextView) findViewById(R.id.mg_text_view);
        millilitre = (TextView) findViewById(R.id.ml_text_view);
        litre = (TextView) findViewById(R.id.l_text_view);
        kilogram = (TextView) findViewById(R.id.kg_text_view);

    }

    public void addItemstoUnitTypeSpinner() {
        unitTypeSpinner = (Spinner) findViewById(R.id.unit_type_spinner);
        ArrayAdapter<CharSequence> unitTypeSpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.conversion_type, android.R.layout.simple_spinner_item);
        unitTypeSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        unitTypeSpinner.setAdapter(unitTypeSpinnerAdapter);

    }

    String itemSelected;
    double units;

    public void addListenertoUnitTypeSpinner() {
        unitTypeSpinner = (Spinner) findViewById(R.id.unit_type_spinner);

        unitTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                itemSelected = parent.getItemAtPosition(position).toString();

                double baseUnit;
                baseUnit = func(itemSelected);

                try {
                    units = Double.valueOf(amountTextView.getText().toString());
                    convertforall(baseUnit, units);
                }

                catch (NumberFormatException e) {
                    convertforall(baseUnit,1.0);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //TODO maybe add something here later

            }
        });
    }

    private TextWatcher myTextWatcher = new TextWatcher() {


        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            //TODO maybe something else

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //TODO maybe something else
        }

        @Override
        public void afterTextChanged(Editable s) {
            try {
                convertforall(func(itemSelected), Double.valueOf(s.toString()));
            } catch (NumberFormatException e) {
                amountTextView.setHint("Enter the value .. ");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    public void convertforall(double baseUnit, double units) {


        tablespoon.setText(String.format("%.4f tbs", (func("tablespoon") / baseUnit * units)));
        teaspoon.setText(String.format("%.4f tsp", (func("teaspoon") / baseUnit * units)));
        cup.setText(String.format("%.4f cup", (func("cup") / baseUnit * units)));
        ounce.setText(String.format("%.4f oz", (func("ounce") / baseUnit * units)));
        pint.setText(String.format("%.4f pint", (func("pint") / baseUnit * units)));
        quart.setText(String.format("%.4f quart", (func("quart") / baseUnit * units)));
        gallon.setText(String.format("%.4f gallon", (func("gallon") / baseUnit * units)));
        millilitre.setText(String.format("%.4f mL", (func("millilitre") / baseUnit * units)));
        pound.setText(String.format("%.4f lbs", (func("pound") / baseUnit * units)));
        kilogram.setText(String.format("%.4f kg", (func("kilogram") / baseUnit * units)));
        milligram.setText(String.format("%.4f mg", (func("milligram") / baseUnit * units)));
        litre.setText(String.format("%.4f L", (func("litre") / baseUnit * units)));

    }

    private double func(String itemSelected) {

        switch (itemSelected) {
            case "cup":
                return 0.208;
            case "tablespoon":
                return 0.333;
            case "teaspoon":
                return 1.0;
            case "ounce":
                return 0.1666;
            case "pint":
                return 0.104;
            case "quart":
                return 0.0052;
            case "gallon":
                return 0.0013;
            case "millilitre":
                return 4.9289;
            case "pound":
                return 0.0125;
            case "litre":
                return 0.0049;
            case "kilogram":
                return 0.057;
            case "milligram":
                return 5687.5;
            default:
                return 0;
        }
    }
}

