package edu.asu.bsse.gcarvaj3.ser423labapp;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.ser423Lab.R;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Copyright 2019 Gianni Carvajal
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * @author Gianni Carvajal mailto:gcarvaj3@asu.edu
 * @version November 1, 2019
 */


public class DistanceCalculationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_distance_calculation);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle distanceCalculationBundle = getIntent().getExtras();
        ArrayList<PlaceDescription> currentPlaceLibrary = PlaceLibrary.getInstance(
                distanceCalculationBundle.getString("LIBRARY_STR")
        ).getPlaceLibrary();
        ArrayList<String> locationNames = new ArrayList<>();
        currentPlaceLibrary.forEach(location -> locationNames.add(location.getName()));
        AtomicReference<PlaceDescription> current = new AtomicReference<>(new PlaceDescription());
        AtomicReference<PlaceDescription> compare = new AtomicReference<>(new PlaceDescription());

        ArrayAdapter<String> currentLocationAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, locationNames);
        ArrayAdapter<String> comparisonLocationAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, locationNames);

        Spinner currentLocation = findViewById(R.id.current_location_spinner);
        Spinner compareLocation = findViewById(R.id.comparison_spinner);
        TextView resultsOutputBox = findViewById(R.id.comparison_results);
        Button compareButton = findViewById(R.id.compare_button);
        Button closeButton = findViewById(R.id.done_button);

        currentLocation.setAdapter(currentLocationAdapter);
        compareLocation.setAdapter(comparisonLocationAdapter);

        String currentLocationName =
                distanceCalculationBundle.getString("NAME");
        currentLocation.setSelection(currentLocationAdapter.getPosition(currentLocationName));

        compareButton.setOnClickListener(v -> {
            currentPlaceLibrary.forEach(place -> {
                if (place.getName().equals(currentLocation.getSelectedItem().toString())) {
                    current.set(place);
                } else if (place.getName().equals(compareLocation.getSelectedItem().toString())) {
                    compare.set(place);
                }
            });
            String calcResult = greatCircleAndBearingCalculator(current.get(), compare.get());

            resultsOutputBox.setText(calcResult);
            resultsOutputBox.setVisibility(View.VISIBLE);
        });

        closeButton.setOnClickListener(v -> finish());

    }

    private String greatCircleAndBearingCalculator(PlaceDescription currentLocation,
                                                   PlaceDescription compareLocation) {
        // Calculate Great Circle Distance
        double longitudeOneInRad = Math.toRadians(currentLocation.getLongitude());
        double latitudeOneInRad = Math.toRadians(currentLocation.getLatitude());
        double longitudeTwoInRad = Math.toRadians(compareLocation.getLongitude());
        double latitudeTwoInRad = Math.toRadians(compareLocation.getLatitude());

        double longitudeDiff = longitudeTwoInRad - longitudeOneInRad;

        // Law of Spherical Cosines formula for Great Circle Distance
        double angleBetween = Math.acos(Math.sin(latitudeOneInRad) * Math.sin(latitudeTwoInRad)
                                        + Math.cos(latitudeOneInRad) * Math.cos(latitudeTwoInRad)
                                        * Math.cos(longitudeDiff));

        double nauticalDistance = Constants.KNOTS_PER_DEGREE * Math.toDegrees(angleBetween);
        double statuteMiles = Constants.KNOTS_TO_MILES_CONVERSION * nauticalDistance;

        double phi = Math.sin(longitudeDiff)*Math.cos(latitudeTwoInRad);
        double lamb = Math.cos(latitudeOneInRad) * Math.sin(latitudeTwoInRad)
                    - Math.sin(latitudeOneInRad) * Math.cos(latitudeTwoInRad)
                    * Math.cos(longitudeDiff);

        double bearing = (Math.atan2(phi, lamb) + 360) % 360;

        String[] coordinateNames = {"N", "NNE", "NE", "ENE", "E", "ESE", "SE", "SSE",
                "S", "SSW", "SW", "WSW", "W", "WNW", "NW", "NNW", "N"};
        double directionId = Math.round(bearing / 22.5);
        // no of array contain 360/16=22.5
        if (directionId < 0) {
            directionId = directionId + 16;
        }
        String compassDirection = coordinateNames[(int) directionId];

        return ("Distance between " + currentLocation.getName()
                + " and " + compareLocation.getName() + ": " + statuteMiles + " mi.\n" +
                "Bearing: " + bearing + " °" + compassDirection);
    }

}
