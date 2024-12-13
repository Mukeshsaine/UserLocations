package com.example.userlocationlogin.Models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "user_tableLocation")
public class userLocation {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String Image;
    private double latitude;
    private double longitude;
    private float accuracy;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public float getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(float accuracy) {
        this.accuracy = accuracy;
    }
}
