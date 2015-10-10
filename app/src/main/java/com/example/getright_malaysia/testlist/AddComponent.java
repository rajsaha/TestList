package com.example.getright_malaysia.testlist;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

public class AddComponent extends Activity {

    DBAdapter adapter;
    DBHelper helper;
    EditText compName, usage, wattage;
    ImageButton save, reset;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_component);
        compName = (EditText) findViewById(R.id.txt_name);
        usage = (EditText) findViewById(R.id.txt_usage);
        wattage = (EditText) findViewById(R.id.txt_wattage);
        save = (ImageButton) findViewById(R.id.btn_OK);
        reset = (ImageButton) findViewById(R.id.btn_undo);

        adapter = new DBAdapter(this);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String input_name = compName.getText().toString();
                int input_usage = Integer.parseInt(usage.getText().toString());
                int input_wattage = Integer.parseInt(wattage.getText().toString());

                long val = adapter.insertDetails(input_name, input_usage, input_wattage);
                finish();
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                compName.setText("");
                usage.setText("");
                wattage.setText("");
            }
        });
    }
}
