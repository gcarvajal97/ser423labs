package com.example.ser423lab;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RawRes;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

import edu.asu.bsse.gcarvaj3.ser423labapp.PlaceDescription;

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

public class MainActivity extends AppCompatActivity {
    static final String JSONSTR = "{\"address-title\" : \"ASU West Campus\"," +
                "\"address-street\" : \"13591 N 47th Ave, Phoenix AZ 85051\"," +
                "\"elevation\" : 1100.0," +
                "\"latitude\" : 33.608979," +
                "\"longitude\" : -112.159469," +
                "\"name\" : \"ASU-West\"," +
                "\"image\" : \"asuwest\"," +
                "\"description\" : \"Home of ASU's Applied Computing Program\"," +
                "\"category\" : \"School\"}";
    Button closeButton;
    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        PlaceDescription place = new PlaceDescription(JSONSTR);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        // Get the widgets reference from XML layout
        RelativeLayout rl = findViewById(R.id.rl);

        // Create a TextView programmatically.
        TextView tv = new TextView(getApplicationContext());

        // Create a LayoutParams for TextView
        LayoutParams lp = new RelativeLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, // Width of TextView
                LayoutParams.WRAP_CONTENT); // Height of TextView

        // Apply the layout parameters to TextView widget
        tv.setLayoutParams(lp);

        // Set text to display in TextView
        tv.setText(place.toString());

        // Set a text color for TextView text
        tv.setTextColor(Color.parseColor("#ff0000"));

        // Add newly created TextView to parent view group (RelativeLayout)
        rl.addView(tv);

        closeButton = findViewById(R.id.button);
        builder = new AlertDialog.Builder(this);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Setting message manually and performing action on button click
                builder.setMessage(R.string.dialog_message)
                        .setCancelable(false)
                        .setTitle(R.string.dialog_title)
                        .setPositiveButton(R.string.dialog_close,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        Toast.makeText(getApplicationContext(),
                                                "you choose OK action for alertbox",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                });
                //Creating dialog box
                AlertDialog alert = builder.create();
                //Setting the title manually
                alert.setTitle("AlertDialogExample");
                alert.show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public String readRawResource(@RawRes int res) {
        return readStream(this.getResources().openRawResource(res));
    }

    private String readStream(InputStream is) {
        Scanner s = new Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    // To get into the onRestart method, navigate away from the activity, and then navigate to
    // it again. In my testing it was go to Android Home, and then open App again.
    @Override
    protected void onRestart() {
        super.onRestart();
        android.util.Log.d(this.getClass().getSimpleName(), "onRestart Method");
    }

    // To get into the onStart method, open the application or go through the onRestart
    // activity flow.
    @Override
    protected void onStart() {
        super.onStart();
        android.util.Log.d(this.getClass().getSimpleName(), "onStart Method");
    }

    // To get into the onPause method, navigate away from the activity.
    @Override
    protected void onPause() {
        super.onPause();
        android.util.Log.d(this.getClass().getSimpleName(), "onPause Method");
    }

    // To get into the onStart method, close the activity.
    @Override
    protected void onDestroy() {
        super.onDestroy();
        android.util.Log.d(this.getClass().getSimpleName(), "onDestroy Method");
    }


    // To get into the onResume method, navigate away from the activity, and then navigate to
    // it again. In my testing it was go to Android Home, and then open App again.
    @Override
    protected void onResume() {
        super.onResume();
        android.util.Log.d(this.getClass().getSimpleName(), "onResume Method");
    }

    // To get into the onStop method, navigate away from the activity.
    @Override
    protected void onStop() {
        super.onStop();
        android.util.Log.d(this.getClass().getSimpleName(), "onStop Method");
    }
}
