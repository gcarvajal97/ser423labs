package edu.asu.bsse.gcarvaj3.ser423labapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;

import androidx.annotation.RawRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.ser423Lab.R;

import java.io.InputStream;
import java.util.Scanner;

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

public class MainActivity extends AppCompatActivity {
    RecyclerViewAdapter dataAdapter;
    PlaceLibrary library;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        this.library = PlaceLibrary.getInstance(readRawResource(R.raw.places));

        // Get Spinner
        RecyclerView _rlv = findViewById(R.id.recycle_list);
        _rlv.setLayoutManager(new LinearLayoutManager(this));
        _rlv.addItemDecoration(
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        // Get the widgets reference from XML layout
        RelativeLayout rl = findViewById(R.id.rl);

        // Set places in spinner:
        this.dataAdapter = new RecyclerViewAdapter(this,
                library.getPlaceLibrary(), library);

        _rlv.setAdapter(this.dataAdapter);
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

        if (id == R.id.add_location) {
            Intent addLocationIntent = new Intent(this.getBaseContext(), AddLocationActivity.class);
            addLocationIntent.putExtra("LIBRARY_STR", this.library.toJSONString());
            addLocationIntent.putExtra("request", "add");
            startActivityForResult(addLocationIntent, Constants.REQUEST_FOR_ADDITION);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            switch (data.getStringExtra("request")) {
                case "update":
                    requestCode = Constants.REQUEST_FOR_UPDATE;
                    this.dataAdapter.onActivityResult(requestCode, resultCode, data);
                    break;
                case "delete":
                    requestCode = Constants.REQUEST_FOR_DELETE;
                    this.dataAdapter.onActivityResult(requestCode, resultCode, data);
                    break;
                case "add":
                    this.library = PlaceLibrary.getInstance(
                            data.getStringExtra("NEW_LIBRARY_STR"));
                    this.recreate();
                    break;
                default:
                    break;
            }
        }
    }

}
