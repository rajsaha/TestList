package com.example.getright_malaysia.testlist;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    DBAdapter adapter_ob;
    DBHelper helper_ob;
    SQLiteDatabase db_ob;
    ListView componentList;
    Cursor cursor;
    TextView totalwatt;

    String ERROR_LOG = "MainActivity.java";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        totalwatt = (TextView) findViewById(R.id.txt_total_watt);

        totalwatt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBottomSheet(v);
            }
        });

        //Floating Action Button Code
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
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
        TextView txtBackup = (TextView)view.findViewById( R.id.txt_backup);
        TextView txtDetail = (TextView)view.findViewById( R.id.txt_detail);
        TextView txtOpen = (TextView)view.findViewById( R.id.txt_open);
        final TextView txtUninstall = (TextView)view.findViewById( R.id.txt_uninstall);

        final Dialog mBottomSheetDialog = new Dialog(MainActivity.this,
                R.style.MaterialDialogSheet);
        mBottomSheetDialog.setContentView (view);
        mBottomSheetDialog.setCancelable (true);
        mBottomSheetDialog.getWindow ().setLayout (LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mBottomSheetDialog.getWindow ().setGravity (Gravity.BOTTOM);
        mBottomSheetDialog.show();

        txtBackup.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Clicked Backup", Toast.LENGTH_SHORT).show();
                mBottomSheetDialog.dismiss();
            }
        });

        txtDetail.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Clicked Detail", Toast.LENGTH_SHORT).show();
                mBottomSheetDialog.dismiss();
            }
        });

        txtOpen.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Clicked Open", Toast.LENGTH_SHORT).show();
                mBottomSheetDialog.dismiss();
            }
        });

        txtUninstall.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Clicked Uninstall", Toast.LENGTH_SHORT).show();
                mBottomSheetDialog.dismiss();
            }
        });
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
