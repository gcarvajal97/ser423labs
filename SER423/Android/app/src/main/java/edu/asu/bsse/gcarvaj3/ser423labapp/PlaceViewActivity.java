package edu.asu.bsse.gcarvaj3.ser423labapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.ser423Lab.R;

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

public class PlaceViewActivity extends AppCompatActivity {
    PlaceDescription selectedPlace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle placeToDisplayBundle = getIntent().getExtras();
        setContentView(R.layout.activity_place_view);
        String placeJsonStr = placeToDisplayBundle != null ?
                placeToDisplayBundle.getString("JSON_STR") : null;
        this.setFinishOnTouchOutside(false);
        this.selectedPlace = new PlaceDescription(placeJsonStr);

        TextView tv = findViewById(R.id.textView2);
        tv.setText(this.selectedPlace.toString());
        EditText editText = findViewById(R.id.update_text);
        editText.setText(this.selectedPlace.toJSONString());

        Button closeButton = findViewById(R.id.close_button);
        Button updateButton = findViewById(R.id.update_button);
        Button deleteButton = findViewById(R.id.delete_button);
        Button calculateDistanceButton = findViewById(R.id.calculate_distance_button);

        closeButton.setOnClickListener(v -> {
            PlaceDescription updated = new PlaceDescription(editText.getText().toString());
            updated.setName(this.selectedPlace.getName());
            this.selectedPlace = updated;
            setResult(RESULT_OK, new Intent().putExtra("JSON_STR",
                    this.selectedPlace.toJSONString()).putExtra("request", "update"));
            finish();
        });

        updateButton.setOnClickListener(v -> {
            tv.setVisibility(View.GONE);
            editText.setVisibility(View.VISIBLE);
        });

        deleteButton.setOnClickListener(v -> {
            setResult(RESULT_OK,
                    new Intent().putExtra("JSON_STR",
                            this.selectedPlace.toJSONString()
                    ).putExtra("request", "delete"));
            finish();
        });

        calculateDistanceButton.setOnClickListener(v -> {
            Intent compareActivity = new Intent(this.getBaseContext(),
                    DistanceCalculationActivity.class);
            compareActivity.putExtras(placeToDisplayBundle);
            startActivity(compareActivity);
        });
    }

}
