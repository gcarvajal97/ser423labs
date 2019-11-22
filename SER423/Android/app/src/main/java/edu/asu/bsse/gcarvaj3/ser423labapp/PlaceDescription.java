package edu.asu.bsse.gcarvaj3.ser423labapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

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

public class PlaceDescription {
    private String name = "";
    private String description = "";
    private String category = "";
    private String addressTitle = "";
    private String addressStreet = "";
    private Double elevation = 0.00;
    private Double latitude = 0.00;
    private Double longitude = 0.00;

    /**
     * Default Constructor with args
     * @param name String
     * @param description String
     * @param category String
     * @param addressStreet String
     * @param elevation Double
     * @param latitude Double
     * @param longitude Double
     */
    public PlaceDescription(String name, String description,
                            String category, String addressTitle,
                            String addressStreet, Double elevation,
                            Double latitude, Double longitude){
        this.name = name;
        this.description = description;
        this.category = category;
        this.addressStreet = addressStreet;
        this.elevation = elevation;
        this.latitude = latitude;
        this.longitude = longitude;
        this.addressTitle = addressTitle;
    }

    /**
     * Default Constructor
     */
    public PlaceDescription() {

    }

    /**
     * Constructor using JSON parsing
     * @param jsonStr String
     */
    public PlaceDescription(String jsonStr) {
        try {
            JSONObject jo = new JSONObject(jsonStr);
            this.name = jo.getString("name");
            this.category = jo.getString("category");
            this.description = jo.getString("description");
            this.addressStreet = jo.getString("address-street");
            this.addressTitle = jo.getString("address-title");
            this.elevation = Double.parseDouble(jo.getString("elevation"));
            this.latitude = Double.parseDouble(jo.getString("latitude"));
            this.longitude = Double.parseDouble(jo.getString("longitude"));
        } catch (JSONException e) {
            android.util.Log.w(this.getClass().getSimpleName(),
                    "error converting to/from json");
        }
    }

    /**
     * Default constructor using a JSONObject
     * @param jo JSONObject
     */
    public PlaceDescription(JSONObject jo) {
        try{
            this.name = jo.getString("name");
            this.category = jo.getString("category");
            this.description = jo.getString("description");
            this.addressStreet = jo.getString("address-street");
            this.addressTitle = jo.getString("address-title");
            this.elevation = Double.parseDouble(jo.getString("elevation"));
            this.latitude = Double.parseDouble(jo.getString("latitude"));
            this.longitude = Double.parseDouble(jo.getString("longitude"));
        } catch (JSONException e){
            android.util.Log.w(this.getClass().getSimpleName(),
                    "error converting to/from json");
        }
    }

    // Default getters
    public Double getElevation() {
        return this.elevation;
    }

    public Double getLatitude() {
        return this.latitude;
    }

    public Double getLongitude() {
        return this.longitude;
    }

    public String getAddressStreet() {
        return this.addressStreet;
    }

    public String getAddressTitle() {
        return this.addressTitle;
    }

    public String getCategory() {
        return this.category;
    }

    public String getDescription() {
        return this.description;
    }

    public String getName() {
        return this.name;
    }

    // Default Setters
    public void setAddressStreet(String addressStreet) {
        this.addressStreet = addressStreet;
    }

    public void setAddressTitle(String addressTitle) {
        this.addressTitle = addressTitle;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setElevation(Double elevation) {
        this.elevation = elevation;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @NonNull
    @Override
    public String toString() {
        return "Name: " + this.getName()
                + "\nDescription: " + this.getDescription()
                + "\nCategory: " + this.getCategory()
                + "\nAddress Title: " + this.getAddressTitle()
                + "\n\nAddress Street: " + this.getAddressStreet()
                + "\n\nElevation: " + this.getElevation()
                + "\nLatitude: " + this.getLatitude()
                + "\nLongitude: " + this.getLongitude();
    }

    @NonNull
    public String toJSONString() {
        return "{ \"name\": \"" + this.getName()
                + "\", \"description\": \"" + this.getDescription()
                + "\", \"category\": \"" + this.getCategory()
                + "\", \"address-title\": \"" + this.getAddressTitle()
                + "\", \"address-street\": \"" + this.getAddressStreet()
                + "\", \"elevation\": " + this.getElevation()
                + ", \"latitude\": " + this.getLatitude()
                + ", \"longitude\": " + this.getLongitude()
                + "\n }";
    }

    public Boolean compare(PlaceDescription placeOne, PlaceDescription placeTwo, String field) {
     switch (field) {
         case "name":
             return placeOne.getName().equals(placeTwo.getName());
         case "description":
             return placeOne.getDescription().equals(placeTwo.getDescription());
         case "addressTitle":
             return placeOne.getAddressTitle().equals(placeTwo.getAddressTitle());
         case "addressStreet":
             return placeOne.getAddressStreet().equals(placeTwo.getAddressStreet());
         case "elevation":
             return placeOne.getElevation().equals(placeTwo.getElevation());
         case "longitude":
             return placeOne.getLongitude().equals(placeTwo.getLongitude());
         case "latitude":
             return placeOne.getLatitude().equals(placeTwo.getLatitude());
         default:
             return false;
     }
    }
}
