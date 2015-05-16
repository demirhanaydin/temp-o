package com.example.demirhanaydin.tempsystem;

import android.database.Cursor;

/**
 * Created by demirhanaydin on 12/05/15.
 */
public class Entry {
    private int id;
    private double temp;
    private double humidity;
    private String description;
    private double lat;
    private double lng;
    private long created_at;

    public Entry(int id, double temp, double humidity, String description, double lat, double lng, long created_at) {
        this.id = id;
        this.temp = temp;
        this.humidity = humidity;
        this.description = description;
        this.lat = lat;
        this.lng = lng;
        this.created_at = created_at;
    }

    public Entry(Cursor cursor){
        this(Integer.parseInt(cursor.getString(0)),
                cursor.getDouble(1),
                cursor.getDouble(2),
                cursor.getString(3),
                cursor.getDouble(4),
                cursor.getDouble(5),
                cursor.getLong(6)
        );
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getTemp() {
        return temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public long getCreated_at() {
        return created_at;
    }

    public void setCreated_at(long created_at) {
        this.created_at = created_at;
    }
}
