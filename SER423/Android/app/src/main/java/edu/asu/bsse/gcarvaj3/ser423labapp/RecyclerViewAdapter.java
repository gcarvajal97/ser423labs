package edu.asu.bsse.gcarvaj3.ser423labapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ser423Lab.R;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

import static android.app.Activity.RESULT_OK;


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

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private ArrayList<PlaceDescription> mData = new ArrayList<>();
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    ArrayList<String> names = new ArrayList<>();
    MainActivity main;
    PlaceDescription selected;
    PlaceLibrary library = PlaceLibrary.getInstance("none");
    SQLiteDatabase placesDb = null;


    // data is passed into the constructor
    RecyclerViewAdapter(Context context, MainActivity main) {
        this.mInflater = LayoutInflater.from(context);
        this.mData.add(new PlaceDescription());
        this.names.add("unknown");
        this.main = main;
        try {
            PlaceDb places = new PlaceDb(context);
            this.placesDb = places.openDB();
            Cursor cur = this.placesDb.rawQuery("select name from places;", new String[]{});
            ArrayList<String> al = new ArrayList<>();
            while(cur.moveToNext()){
                try {
                    al.add(cur.getString(0));
                } catch(Exception ex){
                    android.util.Log.w(this.getClass().getSimpleName(),
                            "exception stepping through cursor" + ex.getMessage());
                }
            }
            setNames(al);
        } catch (Exception ex) {
            android.util.Log.w(this.getClass().getSimpleName(), "Exception creating adapter: " +
                    ex.getMessage());
        }
    }

    // inflates the row layout from xml when needed
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.places_list_layout, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String place = names.get(position);
        holder.textView.setText(place);
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return names.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textView;

        ViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tvPlacesNames);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            AtomicReference<PlaceDescription> selectedPlace = new AtomicReference<>();
            mData.forEach((location) -> {
                if (this.textView.getText().equals(location.getName())) {
                    selectedPlace.set(location);
                }
            });
            Bundle selectedLocationBundle = setLocationToDisplay(selectedPlace.get());
            if (view.getId() == -1) {
                Intent intent = new Intent(view.getContext(), PlaceViewActivity.class);
                intent.putExtras(selectedLocationBundle);
                ((Activity) view.getContext()).startActivityForResult(intent,
                        Constants.REQUEST_FOR_UPDATE);
            }
        }
    }

    // convenience method for getting data at click position
    String getItem(int id) {
        return mData.get(id).toString();
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    private Bundle setLocationToDisplay(PlaceDescription locationToDisplay) {
        Bundle extras = new Bundle();
        extras.putString("NAME", locationToDisplay.getName());
        extras.putString("DESCRIPTION", locationToDisplay.getDescription());
        extras.putString("ADDRESS_TITLE", locationToDisplay.getAddressTitle());
        extras.putString("ADDRESS_STREET", locationToDisplay.getAddressStreet());
        extras.putDouble("ELEVATION", locationToDisplay.getElevation());
        extras.putDouble("LONGITTUDE", locationToDisplay.getLongitude());
        extras.putDouble("LATITUDE", locationToDisplay.getLatitude());
        extras.putString("STR_REPR", locationToDisplay.toString());
        extras.putString("JSON_STR", locationToDisplay.toJSONString());
        extras.putString("LIBRARY_STR", this.library.toJSONString());
        return extras;
    }

    void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Constants.REQUEST_FOR_UPDATE:
                    int foundIndex = 0;
                    PlaceDescription updatedLocation =
                            new PlaceDescription(data.getStringExtra("JSON_STR"));
                    try {
                        ContentValues values = new ContentValues();
                        values.put("name", updatedLocation.getName());
                        values.put("title", updatedLocation.getAddressTitle());
                        values.put("street", updatedLocation.getAddressStreet());
                        values.put("category", updatedLocation.getCategory());
                        values.put("description", updatedLocation.getDescription());
                        values.put("elevation", updatedLocation.getElevation());
                        values.put("latitude", updatedLocation.getLatitude());
                        values.put("longitude", updatedLocation.getLongitude());

                        String[] params = new String[]{updatedLocation.getName()};
                        this.placesDb.update("places", values, "name=?", params);

                    } catch (Exception ex) {
                        android.util.Log.w(this.getClass().getSimpleName(),
                                "Exception creating adapter: " +
                                ex.getMessage());
                    }
                    @SuppressLint("Recycle") Cursor cur =
                            this.placesDb.rawQuery("select name from places;", new String[]{});
                    ArrayList<String> al = new ArrayList<>();
                    while(cur.moveToNext()){
                        try {
                            al.add(cur.getString(0));
                        } catch(Exception ex){
                            android.util.Log.w(this.getClass().getSimpleName(),
                                    "exception stepping through cursor" + ex.getMessage());
                        }
                    }
                    this.setNames(al);
                    this.notifyDataSetChanged();
                    android.util.Log.i(this.getClass().getSimpleName(), "Location updated");
                    break;
                case Constants.REQUEST_FOR_DELETE:
                    android.util.Log.i(this.getClass().getSimpleName(), "Location deleted");
                    PlaceDescription deletedLocation =
                            new PlaceDescription(data.getStringExtra("JSON_STR"));
                    try {
                        String[] deletedName = new String[]{deletedLocation.getName()};
                        this.placesDb.delete("places", "name=?", deletedName);
                    } catch (Exception ex) {
                        android.util.Log.w(this.getClass().getSimpleName(),
                                "Exception creating adapter: " +
                                ex.getMessage());
                    }
                    cur = this.placesDb.rawQuery("select name from places;", new String[]{});
                    al = new ArrayList<>();
                    while(cur.moveToNext()){
                        try {
                            al.add(cur.getString(0));
                        } catch(Exception ex){
                            android.util.Log.w(this.getClass().getSimpleName(),
                                    "exception stepping through cursor" + ex.getMessage());
                        }
                    }
                    this.setNames(al);
                    this.notifyDataSetChanged();
                    break;
                default:
                    android.util.Log.w(this.getClass().getSimpleName(),
                            "Request not implemented");
            }
        } else {
            android.util.Log.w(this.getClass().getSimpleName(), "Request Failed");
        }
    }


    @SuppressLint("Recycle")
    void setNames(ArrayList<String> names) {
        this.clearData();
        this.names = names;
            try {
                Cursor cur = this.placesDb.rawQuery("SELECT * FROM places", null);
                cur.moveToFirst();
                while (!cur.isAfterLast()) {
                    addData(new PlaceDescription(cur));
                    android.util.Log.w(this.getClass().getSimpleName(),
                            cur.getColumnName(1));
                    cur.moveToNext();
                }

            } catch (Exception ex) {
                android.util.Log.w(this.getClass().getSimpleName(),
                        "Exception creating adapter: " + ex.getMessage());
            }

    }

    void addData(PlaceDescription placeToAdd) {
        this.mData.add(placeToAdd);
        this.library.getPlaceLibrary().add(placeToAdd);
        this.main.library = this.library;
    }

    void clearData(){
        this.mData.clear();
        this.library.getPlaceLibrary().clear();
    }
}
