package com.example.getright_malaysia.testlist;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    DBAdapter adapter_ob;
    DBHelper helper_ob;
    SQLiteDatabase db_ob;
    ListView componentList;
    Cursor cursor;
    TextView totalwatt;
    String ERROR_LOG = "MainActivity.java";
    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Total Watt Bottom Sheet Expander
        totalwatt = (TextView) findViewById(R.id.txt_total_watt);
        totalwatt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBottomSheet(v);
            }
        });

        //Floating Action Button Code
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        //get the drawable
        Drawable myFabSrc = ResourcesCompat.getDrawable(getResources(), android.R.drawable.ic_input_add ,getTheme());
        //copy it in a new one
        Drawable willBeWhite = myFabSrc.getConstantState().newDrawable();
        //set the color filter, you can use also Mode.SRC_ATOP
        willBeWhite.mutate().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        //set it to your fab button initialized before
        fab.setImageDrawable(willBeWhite);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addIntent = new Intent(MainActivity.this, AddComponent.class);
                startActivity(addIntent);
            }
        });

        componentList = (ListView) findViewById(R.id.lst_cname);
        adapter_ob = new DBAdapter(this);
        try{
            totalwatt.setText(String.format("Total Bill: %.0f RM", adapter_ob.calculateTotalBill()));
        } catch (NumberFormatException e){
            totalwatt.setText(String.format("Total Bill: 0 RM"));
            Log.e(ERROR_LOG, "Caught Exception lol");
        }

        String[] from = {DBHelper.COMPONENT_NAME, DBHelper.USAGE, DBHelper.WATTAGE};
        int[] to = {R.id.txt_component_name};
        cursor = adapter_ob.queryName();
        SimpleCursorAdapter cursorAdapter = new SimpleCursorAdapter(this, R.layout.custom_row,
                cursor, from, to);

        componentList.setAdapter(cursorAdapter);
        componentList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView arg0, View arg1, int arg2, long arg3) {
                Bundle passdata = new Bundle();
                Cursor listCursor = (Cursor) arg0.getItemAtPosition(arg2);
                int nameId = listCursor.getInt(listCursor.getColumnIndex(helper_ob.KEY_ID));
                passdata.putInt("keyid", nameId);
                Intent passIntent = new Intent(MainActivity.this, EditActivity.class);
                passIntent.putExtras(passdata);
                startActivity(passIntent);
            }
        });
    }

    public void openBottomSheet(View v){
        View view = getLayoutInflater().inflate(R.layout.bottom_sheet, null);
        TextView txtTotal = (TextView)view.findViewById( R.id.txt_total);
        TextView txtTotalKW = (TextView)view.findViewById( R.id.txt_totalKW);
        TextView txtComponentHighestCost = (TextView)view.findViewById( R.id.txt_component_highest_cost);

        final Dialog mBottomSheetDialog = new Dialog(MainActivity.this,
                R.style.MaterialDialogSheet);
        mBottomSheetDialog.setContentView(view);
        mBottomSheetDialog.setCancelable(true);
        mBottomSheetDialog.getWindow ().setLayout(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mBottomSheetDialog.getWindow ().setGravity(Gravity.BOTTOM);
        mBottomSheetDialog.show();

        try{
            if(adapter_ob.calculateTotalBill() != 0){
                txtTotal.setText(String.format("Total Bill: %.0f RM", adapter_ob.calculateTotalBill()));
            }
        } catch (NumberFormatException e){
            totalwatt.setText(String.format("Total Bill: 0 RM"));
            Log.e(ERROR_LOG, "Caught Exception lol");
        }

        try{
            txtTotalKW.setText(String.format("Total Kilowatts Used: %.0f kW", adapter_ob.calculateTotalKW()));
        } catch (NumberFormatException e){
            totalwatt.setText(String.format("Total Kilowatts Used: 0 kW"));
            Log.e(ERROR_LOG, "Caught Exception lol");
        }

        try{
            txtComponentHighestCost.setText(String.format("Component with Highest Load: "));
        } catch (NumberFormatException e){
            totalwatt.setText(String.format("Component with Highest Load: "));
            Log.e(ERROR_LOG, "Caught Exception lol");
        }
    }



    @Override
    public void onResume() {
        super.onResume();
        cursor.requery();

        try{
            totalwatt.setText(String.format("Total Bill: %.0f RM", adapter_ob.calculateTotalBill()));
        } catch (NumberFormatException e){
            totalwatt.setText(String.format("Total Bill: 0 RM"));
            Log.e(ERROR_LOG, "Caught Exception lol");
        }


    }

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
            final Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.settings);
            dialog.setTitle("Change Rate by Country");
            Spinner spincon = (Spinner) findViewById(R.id.spinner);
            List<String> list = new ArrayList<String>();
            list.add("list 1");
            list.add("list 2");
            list.add("list 3");
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_single_choice, list);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            try{
                spincon.setAdapter(dataAdapter);
            } catch (NullPointerException e){

            }
            dialog.show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void addItemsOnSpinner() {


    }
}
