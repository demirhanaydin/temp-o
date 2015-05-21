package com.example.demirhanaydin.tempsystem;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * Created by demirhanaydin on 16/05/15.
 */
public class EntryCursorAdapter extends CursorAdapter {
    private Context context;
    private int layout;
    private GoogleMap mMap;

    public EntryCursorAdapter(Context context, Cursor cursor, int layout, GoogleMap mMap) {
        super(context, cursor, false);
        this.context = context;
        this.layout = layout;
        this.mMap = mMap;
    }
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layout, viewGroup, false);
        bindView(view, context, cursor);
        return view;
    }

    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {
        // assign entry object
        final Entry entry = new Entry(cursor);
        // set Date
        TextView created_at = (TextView) view.findViewById(R.id.date);
        created_at.setText(entry.stringfyCreatedAt());
        // set temp
        TextView temp = (TextView) view.findViewById(R.id.temp);
        temp.setText(entry.stringfyTemp());
        // set humidity
        TextView humidity = (TextView) view.findViewById(R.id.humidity);
        humidity.setText(entry.stringfyHumidity());
        // set delete button & its action
        ImageButton btnDelete = (ImageButton) view.findViewById(R.id.btn_delete);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            DatabaseHandler db = new DatabaseHandler(context);
            db.deleteEntry(entry);
            cursor.requery();
            notifyDataSetChanged();
            }
        });
        // set location button & its action
        ImageButton btnLocation = (ImageButton) view.findViewById(R.id.btn_location);
        btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // map things
                if(mMap != null){
                    LatLng location = new LatLng(entry.getLat(), entry.getLng());

                    MarkerOptions markerOptions  = new MarkerOptions();
                    markerOptions.position(location);
                    markerOptions.title(entry.getTitleInfo());
                    markerOptions.snippet(entry.getBriefInfo());
                    mMap.addMarker(markerOptions).showInfoWindow();
                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(location).zoom(15).build();
                    mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                } else{
                    System.out.println("mmap is null, sorry!");
                }
            }
        });
    }
}
