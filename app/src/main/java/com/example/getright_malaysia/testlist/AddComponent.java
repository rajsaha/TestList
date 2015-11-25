package com.example.getright_malaysia.testlist;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddComponent extends AppCompatActivity {

    DBAdapter adapter;
    EditText compName, usage, wattage, units;
    Button save, reset;
    String LOG = "AddComponent.java";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_component_coordinatelayout);

        compName = (EditText) findViewById(R.id.txt_name);
        usage = (EditText) findViewById(R.id.txt_usage);
        wattage = (EditText) findViewById(R.id.txt_wattage);
        units = (EditText) findViewById(R.id.txt_units);

        save = (Button) findViewById(R.id.btn_OK);
        reset = (Button) findViewById(R.id.btn_undo);

        adapter = new DBAdapter(this);

        compName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String userInput = charSequence.toString();
                if (userInput.toLowerCase().contains("fan")) {
                    wattage.setText("140");
                } else if (userInput.toLowerCase().contains("fridge")) {
                    wattage.setText("130");
                } else if (userInput.toLowerCase().contains("rice")) {
                    wattage.setText("800");
                } else if (userInput.toLowerCase().contains("water")) {
                    wattage.setText("800");
                } else if (userInput.toLowerCase().contains("microwave")) {
                    wattage.setText("1200");
                } else if (userInput.toLowerCase().contains("juicer")) {
                    wattage.setText("1200");
                } else if (userInput.toLowerCase().contains("dish")) {
                    wattage.setText("200");
                } else if (userInput.toLowerCase().contains("cooker")) {
                    wattage.setText("1200");
                } else if (userInput.toLowerCase().contains("multifunctional pot")) {
                    wattage.setText("1350");
                } else if (userInput.toLowerCase().contains("toaster")) {
                    wattage.setText("900");
                } else if (userInput.toLowerCase().contains("coffee")) {
                    wattage.setText("590");
                } else if (userInput.toLowerCase().contains("oven")) {
                    wattage.setText("800");
                } else if (userInput.toLowerCase().contains("aircon") || userInput.toLowerCase().contains("air con")) {
                    wattage.setText("900");
                } else if (userInput.toLowerCase().contains("hair dryer")) {
                    wattage.setText("800");
                } else if (userInput.toLowerCase().contains("heater")) {
                    wattage.setText("700");
                } else if (userInput.toLowerCase().contains("dehumidifier")) {
                    wattage.setText("285");
                } else if (userInput.toLowerCase().contains("electric fan")) {
                    wattage.setText("66");
                } else if (userInput.toLowerCase().contains("vacuum")) {
                    wattage.setText("400");
                } else if (userInput.toLowerCase().contains("exhaust fan")) {
                    wattage.setText("30");
                } else if (userInput.toLowerCase().contains("bulb")) {
                    wattage.setText("60");
                } else if (userInput.toLowerCase().contains("fluorescent lamp")) {
                    wattage.setText("25");
                } else if (userInput.toLowerCase().contains("energy") && userInput.toLowerCase().contains("bulb")) {
                    wattage.setText("17");
                } else if (userInput.toLowerCase().contains("altar light")) {
                    wattage.setText("50");
                } else if (userInput.toLowerCase().contains("hdtv")) {
                    wattage.setText("140");
                } else if (userInput.toLowerCase().contains("lcd")) {
                    wattage.setText("140");
                } else if (userInput.toLowerCase().contains("hi-fi") || userInput.toLowerCase().contains("hifi")) {
                    wattage.setText("50");
                } else if (userInput.toLowerCase().contains("radio")) {
                    wattage.setText("10");
                } else if (userInput.toLowerCase().contains("computer") || userInput.toLowerCase().contains("pc")) {
                    wattage.setText("154");
                } else if (userInput.toLowerCase().contains("inkjet") && userInput.toLowerCase().contains("printer")) {
                    wattage.setText("12");
                } else if (userInput.toLowerCase().contains("printer")) {
                    wattage.setText("12");
                } else if (userInput.toLowerCase().contains("mobile charger") || userInput.toLowerCase().contains("phone")) {
                    wattage.setText("10");
                } else if (userInput.toLowerCase().contains("storage water heater")) {
                    wattage.setText("6000");
                } else if (userInput.toLowerCase().contains("instant") && userInput.toLowerCase().contains("water")) {
                    wattage.setText("10100");
                } else if (userInput.toLowerCase().contains("tube")) {
                    wattage.setText("25");
                } else if (userInput.toLowerCase().contains("laptop")) {
                    wattage.setText("45");
                } else if (userInput.toLowerCase().contains("light")) {
                    wattage.setText("25");
                } else if (userInput.toLowerCase().contains("washing")) {
                    wattage.setText("700");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() == 0) {
                    compName.setError("Component name required!");
                }
            }
        });


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (compName.getText().toString().isEmpty() || usage.getText().toString().isEmpty() ||
                            wattage.getText().toString().isEmpty() ||
                            units.getText().toString().isEmpty()) {
                        Snackbar snackbar = Snackbar.make(findViewById(R.id.add_component_coordinator),
                                "Please fill all the fields!", Snackbar.LENGTH_SHORT);
                        snackbar.show();

                        Log.e(LOG, "Acknowledgement that if was entered");

                    } else {
                        String input_name = compName.getText().toString();
                        double input_usage = Double.parseDouble(usage.getText().toString());
                        int input_wattage = Integer.parseInt(wattage.getText().toString());
                        int input_units = Integer.parseInt(units.getText().toString());

                        long val = adapter.insertDetails(input_name, input_usage, input_wattage, input_units);
                        Toast.makeText(AddComponent.this, input_name + " added", Toast.LENGTH_SHORT).show();
                        Log.e(LOG, "Did not go into the if part obviously");
                        finish();
                    }
                } catch (NumberFormatException e) {
                    Log.e(LOG, "Caught exception --> on Save");
                }
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                compName.setText("");
                usage.setText("");
                wattage.setText("");
                units.setText("");
            }
        });
    }
}
