package com.sp.mad_project;

import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AstraHelper {
    static String region = "us-east1";
    static String url = region + "/v2/keyspaces/app_space/recipedb/";
    static String Cassandra_Token = "AstraCS:ZMetAiPMmTGKEhjTXESYvyTO:964b9d72dd52cfcb550fa1a2793190b66bac9a21939666be4efda2f8eee492a7";
    int volleyResponseStatus;

    static HashMap<String, String> getHeader() {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("X-Cassandra-Token", Cassandra_Token);
        headers.put("Accept", "application/json");
        return headers;
    }

    public void insertRecipe(String usernameStr, String foodnameStr, String caloriesStr, String imageResourceStr,
                             String typeStr, String preperationtimeStr, String descriptionStr, String ratingStr) {
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
        RequestQueue queue = Volley.newRequestQueue(/* Pass your context here */);
        String insertUrl = url + typeStr;
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

    public void getIDByVolley(String type, String calories) {
        String url = AstraHelper.url + type + "/" + calories;
        RequestQueue queue = Volley.newRequestQueue(/* Pass your context here */);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (volleyResponseStatus == 200) {
                            try {
                                int count = response.getInt("count");
                                if (count > 0) {
                                    JSONObject data = response.getJSONArray("data").getJSONObject(0);
                                    String Sqlusername = "Anonymous"; // Assuming username should be "Anonymous"
                                    String Sqlfoodname = data.getString("foodnameStr");
                                    String Sqlcalories = data.getString("caloriesStr");
                                    String Sqlimage = data.getString("imageResourceStr");
                                    String Sqltype = data.getString("typeStr");
                                    String Sqlpreperationtime = data.getString("preperationtimeStr");
                                    String Sqldescription = data.getString("descriptionStr");
                                    String Sqlrating = "0"; // Assuming rating should be "0"
                                    insertRecipe(Sqlusername, Sqlfoodname, Sqlcalories, Sqlimage,
                                            Sqltype, Sqlpreperationtime, Sqldescription, Sqlrating);
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
        queue.add(jsonObjectRequest);
    }
}
