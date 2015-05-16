package com.example.demirhanaydin.tempsystem;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity {
    DatabaseHandler db;
    TextView tempTextView;
    TextView humidityTextView;
    LocationManager locationManager;
    Location lastKnownLocation;
    GPSService mGPSService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // set db
        db = new DatabaseHandler(getApplicationContext());
        tempTextView = (TextView) findViewById(R.id.textViewTemp);
        humidityTextView = (TextView) findViewById(R.id.textViewHumidity);
        // set location service
        mGPSService = new GPSService(MainActivity.this);
        // set last known location
        lastKnownLocation = mGPSService.getLocation();
        // show error if location is not known
        if (mGPSService.isLocationAvailable == false) {
            Toast.makeText(this, "Your location is not available, please try again.", Toast.LENGTH_SHORT).show();
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
        if (id == R.id.action_history) {
            // intent to history page
            Intent intent = new Intent(this, MapActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void saveCurrentEntry(View view){
        long result = 0;
        double temp = Double.parseDouble(tempTextView.getText().toString());
        double humidity = Double.parseDouble(humidityTextView.getText().toString());
        // create entry
        lastKnownLocation = mGPSService.getLocation();
        if(lastKnownLocation != null){
            Entry entry = new Entry(0,
                    temp,
                    humidity,
                    "",
                    lastKnownLocation.getLatitude(),
                    lastKnownLocation.getLongitude(),
                    System.currentTimeMillis());
            result = db.addEntry(entry);
            if(result > 0){
                Toast.makeText(this, "saved" + lastKnownLocation.getLatitude(), Toast.LENGTH_LONG).show();
            }else {
                Toast.makeText(this, "problem!", Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(this, "last known location is null!", Toast.LENGTH_LONG).show();
        }

    }
}
