package com.example.android.quakereport.EarthquakeStuff;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.graphics.drawable.GradientDrawable;

import androidx.core.content.ContextCompat;

import com.example.android.quakereport.R;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;

public class EarthquakeAdapter extends ArrayAdapter<Earthquake> {

    public EarthquakeAdapter(Activity context, ArrayList<Earthquake> earthquakes) {
        super(context, 0, earthquakes);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        // MAGNITUDE


        TextView textMagnitude = listItemView.findViewById(R.id.textMagnitude);

        GradientDrawable magnitudeCircle = (GradientDrawable) textMagnitude.getBackground();
        int magnitudeColor = getMagnitudeColor(getItem(position).getMagnitude());
        magnitudeCircle.setColor(magnitudeColor);

        textMagnitude.setText(formatMagnitude(getItem(position).getMagnitude()));

        // LOCATION

        String location = getItem(position).getLocation();

        String[] formattedLocation = formatLocation(location);

        TextView directionView = listItemView.findViewById(R.id.textDirection);
        directionView.setText(formattedLocation[0]);

        TextView placeView = listItemView.findViewById(R.id.textPlace);
        placeView.setText(formattedLocation[1]);

        // TIME

        Long time = getItem(position).getTime();

        TextView textDate = listItemView.findViewById(R.id.textDate);
        textDate.setText(formatDate(time));

        TextView textTime = listItemView.findViewById(R.id.textTime);
        textTime.setText(formatTime(time));

        // ONCLICK

        listItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getItem(position).getUrl()));
                getContext().startActivity(browserIntent);
            }
        });

        return listItemView;
    }

    private String formatMagnitude(double magnitude) {
        DecimalFormat formatter = new DecimalFormat("0.0");
        return formatter.format(magnitude);
    }

    private String[] formatLocation(String location) {
        String textDirection = "";
        String textPlace = "";

        if (location.contains("of")) {
            int index = location.indexOf("of");

            textDirection = location.substring(0, index + 2);
            textPlace = location.substring(index + 3);
        } else {
            textDirection = "near the";
            textPlace = location;
        }

        return new String[]{textDirection, textPlace};
    }

    private String formatDate(long time) {
        DateFormat dateFormat = DateFormat.getDateInstance();
        return dateFormat.format(new Date(time));
    }

    private String formatTime(long time) {
        DateFormat dateFormat = DateFormat.getTimeInstance(DateFormat.SHORT);
        return dateFormat.format(new Date(time));
    }

    private int getMagnitudeColor(double magnitude) {
        int magnitudeColorResourceId;
        int magnitudeFloor = (int) Math.floor(magnitude);
        switch (magnitudeFloor) {
            case 0:
            case 1:
                magnitudeColorResourceId = R.color.magnitude1;
                break;
            case 2:
                magnitudeColorResourceId = R.color.magnitude2;
                break;
            case 3:
                magnitudeColorResourceId = R.color.magnitude3;
                break;
            case 4:
                magnitudeColorResourceId = R.color.magnitude4;
                break;
            case 5:
                magnitudeColorResourceId = R.color.magnitude5;
                break;
            case 6:
                magnitudeColorResourceId = R.color.magnitude6;
                break;
            case 7:
                magnitudeColorResourceId = R.color.magnitude7;
                break;
            case 8:
                magnitudeColorResourceId = R.color.magnitude8;
                break;
            case 9:
                magnitudeColorResourceId = R.color.magnitude9;
                break;
            default:
                magnitudeColorResourceId = R.color.magnitude10plus;
                break;
        }
        return ContextCompat.getColor(getContext(), magnitudeColorResourceId);
    }
    
}
