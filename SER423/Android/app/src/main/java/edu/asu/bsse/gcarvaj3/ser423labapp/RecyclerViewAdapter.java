package edu.asu.bsse.gcarvaj3.ser423labapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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


    // data is passed into the constructor
    RecyclerViewAdapter(Context context, MainActivity main) {
        this.mInflater = LayoutInflater.from(context);
        this.mData.add(new PlaceDescription());
        this.names.add("unknown");
        this.main = main;
    }

    // inflates the row layout from xml when needed
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.places_list_layout, parent, false);
        if (this.names.get(0).equals("unknown")) {
            try {
                MethodInformation mi = new MethodInformation(this.main,
                        view.getResources().getString(R.string.defaultUrl), "getNames",
                        new Object[]{});
                AsyncConnection ac = (AsyncConnection) new AsyncConnection().execute(mi);
            } catch (Exception ex) {
                android.util.Log.w(this.getClass().getSimpleName(), "Exception creating adapter: " +
                        ex.getMessage());
            }
        }
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
                    for (int i = 0; i < mData.size(); i++) {
                        if (updatedLocation.compare(updatedLocation, mData.get(i), "name")) {
                            foundIndex = i;
                            break;
                        }

                    }
                    try {
                        MethodInformation mi = new MethodInformation(this.main,
                                this.main.getResources().getString(R.string.defaultUrl), "remove",
                                new String[]{this.mData.get(foundIndex).getName()});
                        AsyncConnection ac = (AsyncConnection) new AsyncConnection().execute(mi);
                    } catch (Exception ex) {
                        android.util.Log.w(this.getClass().getSimpleName(), "Exception creating adapter: " +
                                ex.getMessage());
                    }
                    try {
                        MethodInformation mi = new MethodInformation(this.main,
                                this.main.getResources().getString(R.string.defaultUrl),
                                "add",
                                new Object[]{updatedLocation.toJson()});
                        AsyncConnection ac = (AsyncConnection) new AsyncConnection().execute(mi);
                    } catch (Exception ex) {
                        android.util.Log.w(this.getClass().getSimpleName(),
                                "Exception creating adapter: " + ex.getMessage());
                    }
                    this.mData.set(foundIndex, updatedLocation);
                    this.notifyDataSetChanged();
                    android.util.Log.i(this.getClass().getSimpleName(), "Location updated");
                    break;
                case Constants.REQUEST_FOR_DELETE:
                    android.util.Log.i(this.getClass().getSimpleName(), "Location deleted");
                    foundIndex = 0;
                    PlaceDescription deletedLocation =
                            new PlaceDescription(data.getStringExtra("JSON_STR"));
                    for (int i = 0; i < mData.size(); i++) {
                        if (deletedLocation.compare(deletedLocation, mData.get(i), "name")) {
                            foundIndex = i;
                            break;
                        }

                    }
                    try {
                        MethodInformation mi = new MethodInformation(this.main,
                                this.main.getResources().getString(R.string.defaultUrl), "remove",
                                new String[]{this.mData.get(foundIndex).getName()});
                        AsyncConnection ac = (AsyncConnection) new AsyncConnection().execute(mi);
                    } catch (Exception ex) {
                        android.util.Log.w(this.getClass().getSimpleName(), "Exception creating adapter: " +
                                ex.getMessage());
                    }
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


    void setNames(ArrayList<String> names) {
        this.names = names;
        for (int i = 0; i < names.size(); i ++)
            try {
                MethodInformation mi = new MethodInformation(main,
                        main.getResources().getString(R.string.defaultUrl), "get",
                        new String[]{names.get(i)});
                AsyncConnection ac = (AsyncConnection) new AsyncConnection().execute(mi);
            } catch (Exception ex) {
                android.util.Log.w(this.getClass().getSimpleName(), "Exception creating adapter: " +
                        ex.getMessage());
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
