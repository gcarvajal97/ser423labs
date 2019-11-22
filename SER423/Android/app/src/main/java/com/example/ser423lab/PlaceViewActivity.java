package com.example.ser423lab;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import edu.asu.bsse.gcarvaj3.ser423labapp.PlaceDescription;

public class PlaceViewActivity extends AppCompatActivity {
    PlaceDescription selectedPlace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle placeToDisplayBundle = getIntent().getExtras();
        setContentView(R.layout.activity_place_view);
        String placeJsonStr = placeToDisplayBundle.getString("JSON_STR");
        this.setFinishOnTouchOutside(false);
        this.selectedPlace = new PlaceDescription(placeJsonStr);

        TextView tv = findViewById(R.id.textView2);
        tv.setText(this.selectedPlace.toString());
        EditText editText = findViewById(R.id.update_text);
        editText.setText(this.selectedPlace.toJSONString());

        Button close_button = findViewById(R.id.close_button);
        Button update_button = findViewById(R.id.update_button);
        Button delete_button = findViewById(R.id.delete_button);
        close_button.setOnClickListener(v -> {
            PlaceDescription updated = new PlaceDescription(editText.getText().toString());
            updated.setName(this.selectedPlace.getName());
            this.selectedPlace = updated;
            setResult(RESULT_OK, new Intent().putExtra("JSON_STR",
                    this.selectedPlace.toJSONString()).putExtra("request", "update"));
            finish();
        });

        update_button.setOnClickListener(v -> {
            tv.setVisibility(View.GONE);
            editText.setVisibility(View.VISIBLE);
        });

        delete_button.setOnClickListener(v -> {
            setResult(RESULT_OK,
                    new Intent().putExtra("JSON_STR",
                            this.selectedPlace.toJSONString()
                    ).putExtra("request", "delete"));
            finish();
        });
    }

}
