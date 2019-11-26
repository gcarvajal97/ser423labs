package edu.asu.bsse.gcarvaj3.ser423labapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ser423Lab.R;

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
 * @version November 25, 2019
 */

public class AddLocationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_location);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Bundle addLocationBundle = getIntent().getExtras();
        AtomicReference<Boolean> found = new AtomicReference<>(false);
        PlaceLibrary currentLibrary = PlaceLibrary.getInstance(
                addLocationBundle != null ? addLocationBundle.getString("LIBRARY_STR") : null
        );

        EditText addLocationNameField = findViewById(R.id.add_location_name);
        EditText addLocationTitleField = findViewById(R.id.add_location_address_title);
        EditText addLocationStreetField = findViewById(R.id.add_location_address_street);
        EditText addLocationElevationField = findViewById(R.id.add_location_elevation);
        EditText addLocationLongitudeField = findViewById(R.id.add_location_longitude);
        EditText addLocationLatitudeField = findViewById(R.id.add_location_latitude);
        EditText addLocationDescriptionField = findViewById(R.id.add_location_description);
        EditText addLocationCategoryField = findViewById(R.id.add_location_category);

        Button addLocationButton = findViewById(R.id.add_location_button);

        addLocationButton.setOnClickListener(v -> {
            String newLocationName = addLocationNameField.getText().toString();
            currentLibrary.getPlaceLibrary().forEach(place -> {
                if (place.getName().equals(newLocationName)) {
                    Toast.makeText(this,"Location already created in library",
                            Toast.LENGTH_LONG).show();
                    found.set(true);
                }
            });
            if (!found.get()) {
                String newLocationTitle = addLocationTitleField.getText().toString();
                String newLocationStreet = addLocationStreetField.getText().toString();
                String newLocationElevation = addLocationElevationField.getText().toString();
                String newLocationLongitude = addLocationLongitudeField.getText().toString();
                String newLocationLatitude = addLocationLatitudeField.getText().toString();
                String newLocationDescription = addLocationDescriptionField.getText().toString();
                String newLocationCategory = addLocationCategoryField.getText().toString();
                try {
                    PlaceDescription newLocation = new PlaceDescription(newLocationName,
                            newLocationDescription, newLocationCategory, newLocationTitle,
                            newLocationStreet,
                            Double.parseDouble(newLocationElevation),
                            Double.parseDouble(newLocationLatitude),
                            Double.parseDouble(newLocationLongitude));
                    currentLibrary.getPlaceLibrary().add(newLocation);
                    setResult(RESULT_OK,
                            new Intent().putExtra("NEW_LOCATION",
                                    newLocation.toJSONString()
                            ).putExtra("request", "add"));
                    finish();
                } catch (NumberFormatException e) {
                    Toast.makeText(this, Constants.INVALID_NUMBER_FORMAT_MESSAGE,
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

}
