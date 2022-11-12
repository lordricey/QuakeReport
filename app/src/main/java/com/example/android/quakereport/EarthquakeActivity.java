package com.example.android.quakereport;

import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.preference.PreferenceManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.example.android.quakereport.EarthquakeStuff.Earthquake;
import com.example.android.quakereport.EarthquakeStuff.EarthquakeAdapter;
import com.example.android.quakereport.Utils.EarthquakeListFetcher;

import java.util.ArrayList;
import java.util.List;

public class EarthquakeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Earthquake>> {

    private final String USGS_REQUEST_URL = "https://earthquake.usgs.gov/fdsnws/event/1/query";
    private EarthquakeAdapter adapter;
    private ListView earthquakeListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);

        earthquakeListView = findViewById(R.id.list);
        adapter = new EarthquakeAdapter(this, new ArrayList<Earthquake>());
        earthquakeListView.setAdapter(adapter);


        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = (connectivityManager != null) ? connectivityManager.getActiveNetworkInfo() : null;

        if (networkInfo != null && networkInfo.isConnected()) {
            LoaderManager.getInstance(this).initLoader(1, null, this).forceLoad();
        } else {
            findViewById(R.id.noConnection).setVisibility(View.VISIBLE);
            findViewById(R.id.progessBar).setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<List<Earthquake>> onCreateLoader(int id, Bundle args) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        String minMagnitude = sharedPrefs.getString(
                getString(R.string.settings_min_magnitude_key),
                getString(R.string.settings_min_magnitude_default));

        String orderBy = sharedPrefs.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default)
        );

        Uri baseUri = Uri.parse(USGS_REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("format", "geojson");
        uriBuilder.appendQueryParameter("limit", "10");
        uriBuilder.appendQueryParameter("minmag", minMagnitude);
        uriBuilder.appendQueryParameter("orderby", orderBy);

        Log.v("h", uriBuilder.toString());

        return new EarthquakeListFetcher(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<Earthquake>> loader, List<Earthquake> data) {
        findViewById(R.id.progessBar).setVisibility(View.GONE);
        earthquakeListView.setEmptyView(findViewById(R.id.emptyText));
        adapter.clear();

        if (data != null && !data.isEmpty()) {
            adapter.addAll(data);
        }

    }

    @Override
    public void onLoaderReset(Loader<List<Earthquake>> loader) {
        Log.v("hi", "onLoaderReset");
        adapter = new EarthquakeAdapter(this, new ArrayList<Earthquake>());
    }
}