package com.sp.mad_project;

import android.content.Context;
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AstraHelper {
    static String region = "us-east1";
    static String keyspace = "app_space";
    static String recipeTable = "recipe_table";
    static String url = region + "/v2/keyspaces/" + keyspace + "/" + recipeTable + "/{primary_key}";
    static String Cassandra_Token = "AstraCS:ZMetAiPMmTGKEhjTXESYvyTO:964b9d72dd52cfcb550fa1a2793190b66bac9a21939666be4efda2f8eee492a7";
    static int lastID = 0;
    private String username;
    private String foodname;
    private int calories;
    private int imageResource;
    private long ingredients;
    private int volleyResponseStatus;

    static HashMap<String, String> getHeader() {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("X-Cassandra-Token", Cassandra_Token);
        headers.put("Accept", "application/json");
        return headers;
    }

    private void insertVolley(Context context, String usernameStr, String foodnameStr, String caloriesStr, String imageResourceStr, String typeStr, String preperationtimeStr, String descriptionStr, String ratingStr) {
        Map<String, String> params = new HashMap<>();
        params.put("username", usernameStr);
        params.put("foodname", foodnameStr);
        params.put("calories", caloriesStr);
        params.put("image", imageResourceStr);
        params.put("type", typeStr);
        params.put("preparationtime", preperationtimeStr);
        params.put("description", descriptionStr);
        params.put("rating", ratingStr);

        JSONObject postdata = new JSONObject(params);
        RequestQueue queue = Volley.newRequestQueue(context);
        String insertUrl = region + "/v2/keyspaces/" + keyspace + "/" + recipeTable + "/" + typeStr;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, insertUrl, postdata,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Handle response if needed
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("OnErrorResponses", error.toString());
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                return AstraHelper.getHeader();
            }
        };
        queue.add(jsonObjectRequest);
    }

    private void getIDByVolley(Context context, String type, String calories) {
        String getUrl = region + "/v2/keyspaces/" + keyspace + "/" + recipeTable + "/" + type + "/" + calories;
        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, getUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (volleyResponseStatus == 200) {
                            try {
                                int count = response.getInt("count");
                                if (count > 0) {
                                    JSONArray data = response.getJSONArray("data");
                                    // Update values from JSON
                                    String Sqlusername = data.getJSONObject(0).getString("UsernameStr");
                                    String Sqlfoodname = data.getJSONObject(0).getString("foodnameStr");
                                    String Sqlcalories = data.getJSONObject(0).getString("caloriesStr");
                                    String Sqlimage = data.getJSONObject(0).getString("imageResourceStr");
                                    String Sqltype = data.getJSONObject(0).getString("typeStr");
                                    String Sqlpreparationtime = data.getJSONObject(0).getString("preparationtimeStr");
                                    String Sqldescription = data.getJSONObject(0).getString("descriptionStr");
                                    String Sqlrating = data.getJSONObject(0).getString("ratingStr");
                                    // Assuming LocalDBHelper is another class with an insertRecipe method
                                    LocalDBHelper.insertRecipe(Sqlusername, Sqlfoodname, Sqlcalories, Sqlimage, Sqltype, Sqlpreparationtime, Sqldescription, Sqlrating);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("OnErrorResponse", error.toString());
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                return AstraHelper.getHeader();
            }

            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                volleyResponseStatus = response.statusCode;
                return super.parseNetworkResponse(response);
            }
        };
        // Add JsonObjectRequest to the request queue
        queue.add(jsonObjectRequest);
    }
}
