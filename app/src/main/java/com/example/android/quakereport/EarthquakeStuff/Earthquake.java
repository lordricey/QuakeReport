package com.example.android.quakereport.EarthquakeStuff;

public class Earthquake {
    private double magnitude;
    private String location;
    private Long time;
    private String url;

    public Earthquake(double magnitude, String location, Long time, String url) {
        this.magnitude = magnitude;
        this.location = location;
        this.time = time;
        this.url = url;
    }

    public double getMagnitude() {
        return magnitude;
    }

    public String getLocation() {
        return location;
    }

    public Long getTime() {
        return time;
    }

    public String getUrl() {
        return url;
    }
}
