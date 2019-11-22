package com.example.ser423lab;

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

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

import edu.asu.bsse.gcarvaj3.ser423labapp.PlaceDescription;

import static android.app.Activity.RESULT_OK;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private ArrayList<PlaceDescription> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private static final int REQUEST_FOR_UPDATE = 0;
    private static final int REQUEST_FOR_DELETE = 1;

    // data is passed into the constructor
    RecyclerViewAdapter(Context context, ArrayList<PlaceDescription> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
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
        String place = mData.get(position).getName();
        holder.textView.setText(place);
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
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
                ((Activity) view.getContext()).startActivityForResult(intent, REQUEST_FOR_UPDATE);
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
        return extras;
    }

    void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_FOR_UPDATE:
                    int foundIndex = 0;
                    PlaceDescription updatedLocation =
                            new PlaceDescription(data.getStringExtra("JSON_STR"));
                    for (int i = 0; i < mData.size(); i++) {
                        if (updatedLocation.compare(updatedLocation, mData.get(i), "name")) {
                            foundIndex = i;
                            break;
                        }

                    }
                    this.mData.set(foundIndex, updatedLocation);
                    this.notifyDataSetChanged();
                    android.util.Log.i(this.getClass().getSimpleName(), "Location updated");
                    break;
                case REQUEST_FOR_DELETE:
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
                    this.mData.remove(foundIndex);
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
}
