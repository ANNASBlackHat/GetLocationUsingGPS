package com.developer.annasblackhat.getlocation;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by ANNAS BlackHat on 21/01/2016.
 */
public class AddressJSON extends AsyncTask<Double, Void, String> {

    private TextView address;

    public AddressJSON(TextView address) {
        this.address = address;
    }


    @Override
    protected String doInBackground(Double... params) {
        double latitude = params[0];
        double longitude = params[1];
        String urlJson = "http://maps.googleapis.com/maps/api/geocode/json?latlng="+latitude+","+longitude+"&sensor=true";

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String response = null;
        String result = "";

        try {
            URL url = new URL(urlJson);

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }
            if (buffer.length() == 0) {
                return null;
            }
            response = buffer.toString();
            Log.v("AMCC",response);
        }catch (IOException e){
            Log.e("AMCC", "Error ", e);
        }finally{
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e("AMCC", "Error closing stream", e);
                }
            }
        }

        if(response != null){
            try{
                JSONObject jsonObject = new JSONObject(response);
                JSONArray jsonArray = jsonObject.getJSONArray("results");
                jsonObject = jsonArray.getJSONObject(0);
                result = jsonObject.getString("formatted_address");
            }catch (Exception e){
                Log.e("AMCC",e.getMessage());
            }
        }

        return result;
    }

    @Override
    protected void onPostExecute(String s) {
        address.setText(s);
    }
}
