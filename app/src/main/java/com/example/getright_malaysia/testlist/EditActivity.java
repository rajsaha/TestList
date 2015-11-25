package com.example.getright_malaysia.testlist;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class EditActivity extends AppCompatActivity {
    DBAdapter dbadapter;
    int rowId;
    Cursor c;
    String error_log = "EditActivity.java";
    String title;

    EditText compName, usage, wattage, units;
    Button save, delete;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_component);

        compName = (EditText) findViewById(R.id.txt_name);
        usage = (EditText) findViewById(R.id.txt_usage);
        wattage = (EditText) findViewById(R.id.txt_wattage);
        units = (EditText) findViewById(R.id.txt_units);
        save = (Button) findViewById(R.id.btn_OK);
        delete = (Button) findViewById(R.id.btn_delete);

        Bundle showData = getIntent().getExtras();
        rowId = showData.getInt("keyid");
        dbadapter = new DBAdapter(this);

        c = dbadapter.queryAll(rowId);

        if (c.moveToFirst()) {
            do {
                compName.setText(c.getString(1));
                title = c.getString(1);
                usage.setText(Double.toString(c.getDouble(2)));
                wattage.setText(Integer.toString(c.getInt(3)));
                units.setText(Integer.toString(c.getInt(4)));
            } while (c.moveToNext());
        }

        compName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() == 0){
                    compName.setError("Component name required!");
                }
            }
        });

        setTitle("Edit " + title);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbadapter.updateldetail(rowId, compName.getText().toString(),
                        Double.parseDouble(usage.getText().toString()),
                        Integer.parseInt(wattage.getText().toString()),
                        Integer.parseInt(units.getText().toString()));
                finish();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    alertMessage();
                } catch (Exception e) {
                    Log.e(error_log, "Something went wrong");
                }
            }
        });
    }

    public void alertMessage() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        dbadapter.deleteOneRecord(rowId);
                        Toast.makeText(EditActivity.this, "Component Deleted", Toast.LENGTH_SHORT).show();
                        finish();
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        //Do nothing
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure?")
                .setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }
}

//Snackbar snackbar = Snackbar
//                                .make(coordinatorLayout, "Component deleted", Snackbar.LENGTH_SHORT)
//                                .setAction("UNDO", new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View view) {
//                                        Snackbar snackbar1 = Snackbar.make(coordinatorLayout, "Component Restored", Snackbar.LENGTH_LONG);
//                                        snackbar1.show();
//                                    }
//                                });

//                        snackbar.show();
//                        final Handler handler = new Handler();
//                        handler.postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                finish();
//                            }
//                        }, 1000);