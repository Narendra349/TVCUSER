package com.pickanddrop.adapter;

import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by dhananjay on 5/10/17.
 */

public class GooglePlacesAutocompleteAdapter extends ArrayAdapter<String> implements Filterable {
    private ArrayList<String> resultList;
    public static String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    public static String TYPE_AUTOCOMPLETE = "/autocomplete";
    public static String OUT_JSON = "/json";
    public static final String API_KEY = "AIzaSyC6lRBIOxTw25tWUA91uBtJkbd7-8uWMsM";
    public ListView listView;
    public Context context;
    public static IsDataAvialble isDataAvialble;

    public interface IsDataAvialble {
        public void isDataAvailable(ArrayList<String> list);
    }

    public GooglePlacesAutocompleteAdapter(Context context, int textViewResourceId, IsDataAvialble inteface) {
        super(context, textViewResourceId);
        this.context = context;
        this.isDataAvialble = inteface;

    }

    @Override
    public int getCount() {
        if (resultList != null)
            return resultList.size();
        return 0;
    }

    @Override
    public String getItem(int index) {
        if (resultList != null)
            return resultList.get(index);
        return null;
    }


    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint != null) {
                    // Retrieve the autocomplete results.
                    resultList = autocomplete(context, constraint.toString(), listView);
                    if (resultList != null) {
                        // Assign the data to the FilterResults
                        filterResults.values = resultList;
                        filterResults.count = resultList.size();
                    }
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint,
                                          FilterResults results) {
                if (results != null && results.count > 0) {
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }
        };
        return filter;
    }



    public static ArrayList<String> autocomplete(Context context, String input, ListView lv) {
        ArrayList<String> resultList = null;
        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder(PLACES_API_BASE
                    + TYPE_AUTOCOMPLETE + OUT_JSON);
            sb.append("?input=" + input);
            sb.append("&types=geocode");
            sb.append("&key=" + API_KEY);

            URL url = new URL(sb.toString());
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());
            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        } catch (MalformedURLException e) {
            return resultList;
        } catch (IOException e) {
            return resultList;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        try {
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");
            Log.e("",""+jsonObj);
            resultList = new ArrayList<String>(predsJsonArray.length());
            for (int i = 0; i < predsJsonArray.length(); i++) {
                System.out.println("product_description****** " + predsJsonArray.getJSONObject(i).getString("description"));
                resultList.add(predsJsonArray.getJSONObject(i).getString("description"));
            }
            isDataAvialble.isDataAvailable(resultList);
        } catch (JSONException e) {
            Log.e("", "google places error" + e.getMessage());
            isDataAvialble.isDataAvailable(resultList);
        }
        return resultList;
    }
}