package com.example.android.quakereport.Utils;

import android.content.Context;
import android.util.Log;

import androidx.loader.content.AsyncTaskLoader;

import com.example.android.quakereport.EarthquakeStuff.Earthquake;

import java.util.List;

public class EarthquakeListFetcher extends AsyncTaskLoader<List<Earthquake>> {
    private final String url;

    public EarthquakeListFetcher(Context context, String url) {
        super(context);
        this.url = url;
    }

    @Override
    public List<Earthquake> loadInBackground() {
        if (url == null) {
            return null;
        }

        return QueryUtils.fetchEarthquakeData(url);
    }
}
