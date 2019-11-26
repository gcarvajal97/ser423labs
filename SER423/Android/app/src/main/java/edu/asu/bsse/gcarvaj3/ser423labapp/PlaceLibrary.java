package edu.asu.bsse.gcarvaj3.ser423labapp;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Iterator;
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

public class PlaceLibrary {
    private ArrayList<PlaceDescription> placeLibrary;
    private static PlaceLibrary library = null;

    private PlaceLibrary(String fileName) {
        placeLibrary = readFromJson(fileName);
    }
    private PlaceLibrary() {
        placeLibrary = new ArrayList<>();
    }

    public static PlaceLibrary getInstance(String fileName) {
        if (library == null) {
            if (fileName.contains("{")) library = new PlaceLibrary(fileName);
            else {
                library = new PlaceLibrary();
            }
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

    public String toJSONString() {
        if (placeLibrary == null) {
            return "No library found";
        }
        String jsonStrToReturn = "{";
        for (int i = 0; i < placeLibrary.size(); i++) {
            jsonStrToReturn += placeLibrary.get(i).getName() + ": " +
                    placeLibrary.get(i).toJSONString();
        }
        jsonStrToReturn += "}";
        return jsonStrToReturn;
    }

    public ArrayList<PlaceDescription> getPlaceLibrary() {
        return placeLibrary;
    }

    public boolean findName(PlaceLibrary library, String name) {
        AtomicReference<Boolean> found = new AtomicReference<>(false);
        if (library.getPlaceLibrary().isEmpty()) return false;
        library.getPlaceLibrary().forEach(location -> {
            if (location.getName().equals(name)) {
                found.set(true);
            }
        });
        return found.get();
    }
}
