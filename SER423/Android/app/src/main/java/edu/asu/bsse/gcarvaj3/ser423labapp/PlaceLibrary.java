package edu.asu.bsse.gcarvaj3.ser423labapp;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Iterator;

public class PlaceLibrary {
    private ArrayList<PlaceDescription> placeLibrary;
    private static PlaceLibrary library = null;

    private PlaceLibrary(String fileName) {
        placeLibrary = readFromJson(fileName);
    }

    public static PlaceLibrary getInstance(String fileName) {
        if (library == null) {
            library = new PlaceLibrary(fileName);
        }
        return library;
    }


    private ArrayList<PlaceDescription> readFromJson(String fileLocation) {
        try {
            ArrayList<PlaceDescription> places = new ArrayList<>();
            JSONObject placeLibraryJSON = new JSONObject(fileLocation);
            Iterator<String> keys = placeLibraryJSON.keys();
            while (keys.hasNext()){
                String key = keys.next();
                if (placeLibraryJSON.get(key) instanceof JSONObject) {
                    places.add(new PlaceDescription(placeLibraryJSON.get(key).toString()));
                }
            }
            return places;
        } catch (JSONException e) {
            android.util.Log.e(this.getClass().getSimpleName(),
                    "error converting to/from json from file");
            android.util.Log.e(this.getClass().getSimpleName(), e.toString());
            return null;
        }

    }

    public ArrayList<PlaceDescription> getPlaceLibrary() {
        return placeLibrary;
    }
}
