package com.example.demirhanaydin.tempsystem;

import android.app.ActionBar;
import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;


public class MapActivity extends FragmentActivity {
    ListView lv;
    GoogleMap mMap;
    Cursor cursor;
    DatabaseHandler db;
    EntryCursorAdapter entryCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // hide action bar for map activity
//        ActionBar actionBar = getActionBar();
//        actionBar.hide();
        setContentView(R.layout.activity_map);
        db = new DatabaseHandler(getApplicationContext());

        // setup list
        setUpList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_map, menu);
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

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }
    private void setUpMap() {

    }
    private void setUpList(){
        lv = (ListView) findViewById(R.id.listView);
        //-- Read cursor from the database
        cursor = db.getEntries();
        entryCursorAdapter = new EntryCursorAdapter(
                MapActivity.this,
                cursor,
                R.layout.list_item
        );
        //-- Set adapter to listview
        lv.setAdapter(entryCursorAdapter);
        registerForContextMenu(lv);
    }
}
