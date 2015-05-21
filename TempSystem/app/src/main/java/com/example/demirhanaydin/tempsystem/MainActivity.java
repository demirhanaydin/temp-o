package com.example.demirhanaydin.tempsystem;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
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
    private BluetoothAdapter mBluetoothAdapter;
    private Handler mHandler;
    private boolean mScanning;
    // Stops scanning after 10 seconds.
    private static final long SCAN_PERIOD = 10000;
    private static final String TEMP_DEVICE_ADDRESS="00:1A:7D:DA:71:0A";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // ble support
        checkBlueToothFeature();
        setBlueToothManager();
        mHandler = new Handler();
        scanLeDevice(true);
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
            intent.putExtra("lastKnownLocation", lastKnownLocation);
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
                    mGPSService.getLocationAddress(),
                    lastKnownLocation.getLatitude(),
                    lastKnownLocation.getLongitude(),
                    System.currentTimeMillis());
            result = db.addEntry(entry);
            if(result > 0){
//                Toast.makeText(this, "Saved!" + lastKnownLocation.getLatitude(), Toast.LENGTH_LONG).show();
                Toast.makeText(this, "Saved!", Toast.LENGTH_LONG).show();
            }else {
                Toast.makeText(this, "problem!", Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(this, "last known location is null!", Toast.LENGTH_LONG).show();
        }
    }
    private void checkBlueToothFeature(){
        // Use this check to determine whether BLE is supported on the device.  Then you can
        // selectively disable BLE-related features.
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
            finish();
        }
    }
    private void setBlueToothManager(){
        // Initializes a Bluetooth adapter.  For API level 18 and above, get a reference to
        // BluetoothAdapter through BluetoothManager.
        final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
        // Checks if Bluetooth is supported on the device.
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, R.string.error_bluetooth_not_supported, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
    }
    private void scanLeDevice(final boolean enable) {
        if (enable) {
            // Stops scanning after a pre-defined scan period.
//            mHandler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    mScanning = false;
//                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
//                }
//            }, SCAN_PERIOD);

            mScanning = true;
            mBluetoothAdapter.startLeScan(mLeScanCallback);
        } else {
            mScanning = false;
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }
    }
    // Device scan callback.
    private BluetoothAdapter.LeScanCallback mLeScanCallback =
            new BluetoothAdapter.LeScanCallback() {
                @Override
                public void onLeScan(final BluetoothDevice device, final int rssi, final byte[] scanRecord) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(TEMP_DEVICE_ADDRESS.equals(device.getAddress())){
                                String record = new String(scanRecord);
                                System.out.println("Name : " + device.getAddress());
                                System.out.println("RSSI : " + rssi);
                                System.out.println("Text Bytes : " + scanRecord);
                                System.out.println("Text Decryted : " + record);
                                String raw_data = record.substring(21,29);
                                System.out.println("Text Decryted Subs:" + raw_data + ":");
                                String[] splited = raw_data.split("\\s+");
                                float temp, humidity;
                                temp = Float.parseFloat(splited[0]);
                                humidity = Float.parseFloat(splited[1]);
                                System.out.println("Temp:" + temp + " Humidity:" + humidity);
                                //Toast.makeText(getApplicationContext(), "Temp:" + temp+ " Humidity:" + humidity, Toast.LENGTH_SHORT).show();
                                setValues(temp, humidity);
                                //mLeDeviceListAdapter.addDevice(device);
                                //mLeDeviceListAdapter.notifyDataSetChanged();
                            }else
                            {
                                System.out.println("Name : " + device.getAddress());
                            }

                        }
                    });
                }
            };
    private void setValues(float temp, float humidity){
        tempTextView.setText(Entry.stringfy((double) temp));
        humidityTextView.setText(Entry.stringfy((double) humidity));
    }
}
