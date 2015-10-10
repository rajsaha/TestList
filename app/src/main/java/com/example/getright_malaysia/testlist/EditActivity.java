package com.example.getright_malaysia.testlist;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

public class EditActivity extends Activity{
    DBAdapter dbadapter;
    DBHelper openHelper;
    int rowId;
    Cursor c;
    String input_name;
    int input_usage, input_wattage;

    EditText compName, usage, wattage;
    ImageButton save, reset, delete;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_component);

        compName = (EditText) findViewById(R.id.txt_name);
        usage = (EditText) findViewById(R.id.txt_usage);
        wattage = (EditText) findViewById(R.id.txt_wattage);
        save = (ImageButton) findViewById(R.id.btn_OK);
        reset = (ImageButton) findViewById(R.id.btn_undo);
        delete = (ImageButton) findViewById(R.id.btn_delete);

        Bundle showData = getIntent().getExtras();
        rowId = showData.getInt("keyid");
        dbadapter = new DBAdapter(this);

        c = dbadapter.queryAll(rowId);

        if(c.moveToFirst()){
            do{
                compName.setText(c.getString(1));
                usage.setText(Integer.toString(c.getInt(2)));
                wattage.setText(Integer.toString(c.getInt(3)));
            } while (c.moveToNext());
        }

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbadapter.updateldetail(rowId, compName.getText().toString(),
                        Integer.parseInt(usage.getText().toString()),
                        Integer.parseInt(wattage.getText().toString()));
                finish();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbadapter.deleteOneRecord(rowId);
                finish();
            }
        });
    }
}
