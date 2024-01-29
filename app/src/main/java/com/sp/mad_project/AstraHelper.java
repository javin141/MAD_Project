package com.sp.mad_project;

import static android.provider.Settings.System.getString;

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
    static String url = region + "/v2/keyspaces/app_space/recipedb/";
    static String Cassandra_Token = "AstraCS:ZMetAiPMmTGKEhjTXESYvyTO:964b9d72dd52cfcb550fa1a2793190b66bac9a21939666be4efda2f8eee492a7";
    static int lastID = 0;
    private String username;
    private String foodname;
    private int calories;
    private int imageResource;
    private long ingredients;
    private int volleyResponseStatus;

    static HashMap getHeader() {
        HashMap<String, String> headers = new HashMap<~>();
        headers.put("Content-Type", "application/json");
        headers.put("X-Cassandra-Token", Cassandra_Token);
        headers.put("Accept", "application/json");
        return headers;
    }

    private void insertVolley(String usernameStr, String foodnameStr, String caloriesStr, String imageResourceStr, String typeStr, String preperationtimeStr, String descriptionStr, String ratingStr) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("username", usernameStr);
        params.put("foodname", foodnameStr);
        params.put("calories", caloriesStr);
        params.put("image", imageResourceStr);
        params.put("type", typeStr);
        params.put("preparationtime", preperationtimeStr);
        params.put("description", descriptionStr);
        params.put("rating",ratingStr);
        JSONObject postdata = new JSONObject(params);
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = this.url + typeStr ;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, postdata,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
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
                return this.getHeaders();
            }
            };
            queue.add(jsonObjectRequest);
        };



    private void getIDByVolley(String type, String calories) {
        String url = AstraHelper.url + type + "/" + calories;
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (volleyResponseStatus == 200) {
                            try {
                                int count = response.getInt("count");
                                if (count > 0) {
                                    JSONArray data = response.getJSONArray("data");
                                    //update values from json
                                    String Sqlusername = data.getJSONObject(0).getString("UsernameStr");
                                    String Sqlfoodname = data.getJSONObject(0).getString("foodnameStr");
                                    String Sqlcalories = data.getJSONObject(0).getString("caloriesStr");
                                    String Sqlimage = data.getJSONObject(0).getString("imageResourceStr");
                                    String Sqltype = data.getJSONObject(0).getString("typeStr");
                                    String Sqlpreperationtime = data.getJSONObject(0).getString("preperationtimeStr");
                                    String Sqldescription = data.getJSONObject(0).getString("descriptionStr");
                                    String Sqlrating = data.getJSONObject(0).getString("ratingStr");
                                    insertRecipe(Sqlusername, Sqlfoodname, Sqlcalories, Sqlimage, Sqltype, Sqlpreperationtime, Sqldescription, Sqlrating);
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
            public Map<String, String> getHeaders() {return AstraHelper.getHeader();}

            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                volleyResponseStatus = response.statusCode;
                return super.parseNetworkResponse(response);
            }
        };
        //add jsonobjectrequest to the requestqueue
        queue.add(jsonObjectRequest);
    }






};
