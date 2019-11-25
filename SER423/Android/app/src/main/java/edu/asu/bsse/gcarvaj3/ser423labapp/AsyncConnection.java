package edu.asu.bsse.gcarvaj3.ser423labapp;

import android.os.AsyncTask;
import android.os.Looper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;

/*
 * Copyright (c) 2018 Tim Lindquist,
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
 * Purpose: Example Android application that uses an AsyncTask to accomplish the same effect
 * as using a Thread and android.os.Handler
 *
 * Ser423 Mobile Applications
 * see http://pooh.poly.asu.edu/Mobile
 * @author Tim Lindquist Tim.Lindquist@asu.edu
 *         Software Engineering, CIDSE, IAFSE, ASU Poly
 * @version December 3, 2018
 */

// AsyncTask. Generic type parameters:
//    first is base type of array input to doInBackground method
//    second is type for input to onProgressUpdate method. also base type of an array
//    third is the return type for the doInBackground method, whose value is the argument
//          to the onPostExecute method.
public class AsyncConnection extends AsyncTask<MethodInformation, Integer, MethodInformation> {

    @Override
    protected void onPreExecute(){
        android.util.Log.d(this.getClass().getSimpleName(),"in onPreExecute on "+
                (Looper.myLooper() == Looper.getMainLooper()?"Main thread":"Async Thread"));
    }

    @Override
    protected MethodInformation doInBackground(MethodInformation... aRequest){
        // array of methods to be called. Assume exactly one input, a single MethodInformation object
        android.util.Log.d(this.getClass().getSimpleName(),"in doInBackground on "+
                (Looper.myLooper() == Looper.getMainLooper()?"Main thread":"Async Thread"));
        try {
            JSONArray ja = new JSONArray(aRequest[0].params);
            android.util.Log.d(this.getClass().getSimpleName(),"params: "+ja.toString());
            String requestData = "{ \"jsonrpc\":\"2.0\", \"method\":\""+aRequest[0].method+"\", \"params\":"+ja.toString()+
                    ",\"id\":3}";
            android.util.Log.d(this.getClass().getSimpleName(),"requestData: "+requestData+" url: "+aRequest[0].urlString);
            JSONRPCServerConnection conn = new JSONRPCServerConnection((new URL(aRequest[0].urlString)), aRequest[0].parent);
            String resultStr = conn.call(requestData);
            aRequest[0].resultAsJson = resultStr;
        }catch (Exception ex){
            android.util.Log.d(this.getClass().getSimpleName(),"exception in remote call "+
                    ex.getMessage());
        }
        return aRequest[0];
    }

    @Override
    protected void onPostExecute(MethodInformation res){
        /*
         * Using AsyncTask is constraining in the following sense: Either you create a separate Class
         * extending AsyncTask for each call (or method being called), or you must determine which
         * (and possibly where) the method was called in onPostExecute. Thus, the if-then-else below.
         * In iOS we use a different approach by passing the block of code to be executed upon completion
         * of the asynchronous call. While Java has lambda's, they are constrained such that doing so is
         * not a viable option at this time.
         * Another approach would be to define an interface and have the calling class implement the
         * methods of the interface. This would abbreviate the code below while requiring the caller to
         * define what are commonly called delegate methods, or call-backs which could be called by
         * onPostExecute.
         * If you are thinking: Why didn't the designers of Android define the View Objects to be shared
         * (thread-safe) objects, thus allowing multiple threads to access them, each getting the "key"
         * before reading or making changes. The problem with this approach is that the view objects would
         * have to relinquish control to threads other than the UI Thread. This could cause the user to
         * experience non-responsiveness of the UI/App if misused by the App Developer.
         */
        android.util.Log.d(this.getClass().getSimpleName(), "in onPostExecute on " +
                (Looper.myLooper() == Looper.getMainLooper() ? "Main thread" : "Async Thread"));
        android.util.Log.d(this.getClass().getSimpleName(), " resulting is: " + res.resultAsJson);
        try {
            switch (res.method) {
                case "getNames": {
                    JSONObject jo = new JSONObject(res.resultAsJson);
                    JSONArray ja = jo.getJSONArray("result");
                    ArrayList<String> al = new ArrayList<>();
                    for (int i = 0; i < ja.length(); i++) {
                        al.add(ja.getString(i));
                    }
                    String[] names = al.toArray(new String[0]);
                    res.parent.dataAdapter.setNames(al);
                    res.parent.dataAdapter.notifyDataSetChanged();
                    break;
                }
                case "get": {
                    JSONObject jo = new JSONObject(res.resultAsJson);
                    res.parent.dataAdapter.addData(
                            new PlaceDescription(jo.getJSONObject("result")));
                    android.util.Log.d(this.getClass().getSimpleName(),
                            jo.getJSONObject("result").getString("name"));
                    res.parent.dataAdapter.notifyDataSetChanged();
                    break;
                }
                case "add":
                    try {
                        // finished adding a student.
                        // refresh the list of students by going back to the server for names
                        MethodInformation mi = new MethodInformation(res.parent,
                                res.urlString, "getNames", new Object[]{});
                        AsyncConnection ac = (AsyncConnection) new AsyncConnection().execute(mi);
                    } catch (Exception ex) {
                        android.util.Log.w(this.getClass().getSimpleName(),
                                "Exception processing getNames: " +
                                ex.getMessage());
                    }
                    break;
            }
        } catch (Exception ex){
            android.util.Log.d(this.getClass().getSimpleName(),"Exception: "+ex.getMessage());
        }
    }

}
