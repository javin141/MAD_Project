package com.sp.mad_project;

import android.content.Context;
import android.util.Base64;
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
import java.util.UUID;

import android.net.Uri;


public class AstraHelper {
    static String region = "us-east1";
    static String keyspace = "app_space";
    static String recipeTable = "bakingbread";

    // static String url =  region + "/v2/keyspaces/" + keyspace + "/" + recipeTable + "/{primary_key}";
    // static String url = "https://" + region + "/v2/keyspaces/" + keyspace + "/" + recipeTable + "/{primary_key}";
    // static String url = "https://" + region + ".apps.astra.datastax.com/v2/keyspaces/" + keyspace + "/" + recipeTable + "/{primary_key}";
    static String url = "https://" + region + ".apps.astra.datastax.com/v2/keyspaces/" + keyspace + "/" + recipeTable;
    static String Cassandra_Token = "AstraCS:ZMetAiPMmTGKEhjTXESYvyTO:964b9d72dd52cfcb550fa1a2793190b66bac9a21939666be4efda2f8eee492a7";
    static int lastID = 0;
    private String username;
    private String foodname;
    private int calories;
    private int imageResource;
    private long ingredients;
    private static int volleyResponseStatus;

    static HashMap<String, String> getHeader() {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("X-Cassandra-Token", Cassandra_Token);
        headers.put("Accept", "application/json");
        return headers;
    }

    void insertVolley(Context context, String usernameStr, String foodnameStr, String caloriesStr, byte[] imageBytes, String typeStr, String preperationtimeStr, String descriptionStr, String rating) {
        Map<String, String> params = new HashMap<>();
        String primaryKey = generateUniqueId();
        params.put("id", primaryKey);
        params.put("username", usernameStr);
        params.put("foodname", foodnameStr);
        params.put("calories", caloriesStr);
        params.put("image", Base64.encodeToString(imageBytes, Base64.DEFAULT));
        params.put("type", typeStr);
        params.put("preparationtime", preperationtimeStr);
        params.put("description", descriptionStr);
        params.put("rating", rating);

        // Construct the URL by directly appending the UUID to the base URL
        String insertUrl = url + "/" + primaryKey;

        JSONObject postdata = new JSONObject(params);
        RequestQueue queue = Volley.newRequestQueue(context);

        Log.d("AstraHelper", "Constructed URL: " + insertUrl);

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

    public static void getAllRecipesByVolley(Context context) {
        // String getUrl = https:// region + "/v2/keyspaces/" + keyspace + "/" + recipeTable;
        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            int count = response.getInt("count");
                            if (count > 0) {
                                JSONArray data = response.getJSONArray("data");
                                // Assuming LocalDBHelper is another class with an insertRecipe method
                                LocalDBHelper localdb = new LocalDBHelper();
                                for (int i = 0; i < count; i++) {
                                    try {
                                        Integer Sqlid = data.getJSONObject(i).getInt("ID");
                                        String Sqlusername = data.getJSONObject(i).getString("Username");
                                        String Sqlfoodname = data.getJSONObject(i).getString("foodname");
                                        String Sqlcalories = data.getJSONObject(i).getString("calories");
                                        byte[] Sqlimage = Base64.decode(data.getJSONObject(i).getString("imageResource"), Base64.DEFAULT);
                                        String Sqltype = data.getJSONObject(i).getString("type");
                                        String Sqlpreparationtime = data.getJSONObject(i).getString("preparationtime");
                                        String Sqldescription = data.getJSONObject(i).getString("description");
                                        Integer Sqlrating = data.getJSONObject(i).getInt("rating");

                                        localdb.insertRecipe(Sqlid, Sqlusername, Sqlfoodname, Sqlcalories, Sqlimage, Sqltype, Sqlpreparationtime, Sqldescription, String.valueOf(Sqlrating));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        // Handle JSONException (parsing individual recipe) if needed
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            // Handle JSONException (parsing the main response) if needed
                        } catch (Exception e) {
                            e.printStackTrace();
                            // Handle other exceptions if needed
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

    private static String generateUniqueId() {
        // Generate a unique ID using UUID (Universally Unique Identifier)
        return UUID.randomUUID().toString();
    }

}
