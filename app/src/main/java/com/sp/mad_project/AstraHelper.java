package com.sp.mad_project;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
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

public class AstraHelper {
    static String region = "us-east1";
    static String keyspace = "api/rest/v2/keyspaces/app_space";
    static String recipeTable = "bakingbread";
    static String CredentialsTable = "logininfo";
    static String astraid ="dfbca21d-c613-4eb2-af50-f8c61015c33c";

    // static String url =  region + "/v2/keyspaces/" + keyspace + "/" + recipeTable + "/{primary_key}";
    // static String url = "https://" + region + "/v2/keyspaces/" + keyspace + "/" + recipeTable + "/{primary_key}";
    // static String url = "https://" + region + ".apps.astra.datastax.com/v2/keyspaces/" + keyspace + "/" + recipeTable + "/{primary_key}";
    static String url = "https://" + astraid + "-" + region + ".apps.astra.datastax.com/"+ keyspace + "/" + recipeTable + "/";
    static String Loginurl =  "https://" + astraid + "-" + region + ".apps.astra.datastax.com/"+ keyspace + "/" + CredentialsTable+ "/";
    static String Cassandra_Token = "AstraCS:vPMfhrwCAAwdeoBakkHbfoLM:9fa3eca58b18c041fc936b2444ed41d68215efb342040dabb028eb19cb9cd0f7";
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

    void insertVolley(Context context, String usernameStr, String foodnameStr, String caloriesStr, byte[] imageBytes, String typeStr, String preparationTimeStr, String descriptionStr, String rating) {
        Map<String, String> params = new HashMap<>();
        // Remove generating UUID
        params.put("username", usernameStr);
        params.put("foodname", foodnameStr);
        params.put("calories", caloriesStr);
        params.put("imageresource", Base64.encodeToString(imageBytes, Base64.DEFAULT));
        params.put("type", typeStr);
        params.put("preparationtime", preparationTimeStr);
        params.put("description", descriptionStr);
        params.put("rating", rating);

        String insertUrl = url;

        JSONObject postdata = new JSONObject(params);
        RequestQueue queue = Volley.newRequestQueue(context);

        Log.d("AstraHelper", "Constructed URL: " + insertUrl);
        Log.d("AstraHelper", "Request Payload: " + postdata.toString());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, insertUrl, postdata,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Handle success response
                        Log.d("AstraHelper", "Response: " + response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error response
                        Log.e("AstraHelper", "Error: " + error.toString());
                        if (error.networkResponse != null) {
                            Log.e("Insert", "Error Response Code: " + error.networkResponse.statusCode);
                            Log.e("Insert", "Error Response Data: " + new String(error.networkResponse.data));
                            // Additional error details from error.networkResponse
                        }
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
        RequestQueue queue = Volley.newRequestQueue(context);

        String rowsurl = AstraHelper.url + "/rows";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, rowsurl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            int count = response.getInt("count");
                            if (count > 0) {
                                JSONArray data = response.getJSONArray("data");
                                LocalDBHelper localdb = new LocalDBHelper(context);
                                SQLiteDatabase db = localdb.getWritableDatabase();
                                db.delete(LocalDBHelper.TABLE_NAME, null, null);

                                try {
                                    for (int i = 0; i < count; i++) {
                                        String Sqlusername = data.getJSONObject(i).getString("username");
                                        String Sqlfoodname = data.getJSONObject(i).getString("foodname");
                                        String Sqlcalories = data.getJSONObject(i).getString("calories");
                                        byte[] Sqlimage = Base64.decode(data.getJSONObject(i).getString("imageresource"), Base64.DEFAULT);
                                        String Sqltype = data.getJSONObject(i).getString("type");
                                        String Sqlpreparationtime = data.getJSONObject(i).getString("preparationtime");
                                        String Sqldescription = data.getJSONObject(i).getString("description");
                                        Integer Sqlrating = data.getJSONObject(i).getInt("rating");

                                        localdb.insertRecipe(Sqlusername, Sqlfoodname, Sqlcalories, Sqlimage, Sqltype, Sqlpreparationtime, Sqldescription, String.valueOf(Sqlrating));
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    // Handle JSONException (parsing individual recipe) if needed
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
                        if (error.networkResponse != null) {
                            Log.e("GetAll", "Error Response Code: " + error.networkResponse.statusCode);
                            Log.e("GetAll", "Error Response Data: " + new String(error.networkResponse.data));
                        }
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

    void insertVolleyLogin(Context context,String NameofUser, Integer Password) {
        Map<String, String> params = new HashMap<>();
        params.put("NameofUser", NameofUser);
        params.put("Password", String.valueOf(Password));

        // Construct the URL by directly appending the UUID to the base URL
        String insertUrl = Loginurl + "/" + NameofUser;

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
                        Log.e("OnErrorResponse", error.toString());
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                return AstraHelper.getHeader();
            }
        };
        queue.add(jsonObjectRequest);
    }

    private void getByIDVolleyLogin(Context context, String name) {
        String url = AstraHelper.Loginurl + name;
        RequestQueue queue = Volley.newRequestQueue(context);
        // Use GET REST api call
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (volleyResponseStatus == 200) { // Read successfully from database
                            try {
                                int count = response.getInt("count"); //Number of records from database
                                if (count > 0) {
                                    JSONArray data = response.getJSONArray("data");//Get the record as JSON array
                                    //parse value somehow lol
                                    // data.getJSONObject(0).getString("password");

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
                        // TODO: Handle error
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
        // add JsonObjectRequest to the RequestQueue
        queue.add(jsonObjectRequest);
    }
}
