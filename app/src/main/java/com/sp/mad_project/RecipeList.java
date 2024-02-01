package com.sp.mad_project;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RecipeList extends AppCompatActivity {
    private List<Recipes> model = new ArrayList<>();
    private RecipeAdapter adapter = null;
    private RequestQueue queue;
    private int volleyResponseStatus;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_list);
        adapter = new RecipeAdapter();


        // Initialize RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerViewRecipes);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2)); // Assuming 2 columns

        // Generate sample recipe data
        List<Recipe> recipeList = generateRecipeList();

        // Create and set a simple adapter directly without a custom adapter class
        recyclerView.setAdapter(new SimpleRecipeAdapter(recipeList));
    }

    // Method to test gen recipes
    private List<Recipe> generateRecipeList() {
        List<Recipe> recipes = new ArrayList<>();

        return recipes;
    }
    private void getAllVolley() {
        queue = Volley.newRequestQueue(this);
        String url = AstraHelper.url + "rows"; //Query all records
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (volleyResponseStatus == 200) { // Read successfully from database
                            try {
                                int count = response.getInt("count"); //Number of records from database
                                //adapter.clear(); //reset adapter
                                if (count > 0) { //Has more than 1 record
                                   // empty.setVisibility(View.INVISIBLE);
                                    JSONArray data = response.getJSONArray("data");//Get all the records as JSON array
                                    for (int i = 0; i <= count; i++) { // Loop through all records
                                        Recipe r = new Recipe();
                                        // Store the lastest id in lastID
                                        if (AstraHelper.lastID < data.getJSONObject(i).getInt("id")) {
                                            AstraHelper.lastID = data.getJSONObject(i).getInt("id");
                                        }
                                        // For each json record
                                        /*
                                        r.setId(data.getJSONObject(i).getString("id")); //read the id
                                        r.setName(data.getJSONObject(i).getString("restaurantname")); //extract the restaurantname
                                        r.setAddress(data.getJSONObject(i).getString("restaurantaddress")); //extract the restaurantaddress
                                        r.setTelephone(data.getJSONObject(i).getString("restauranttel")); //extract the restauranttel
                                        r.setRestaurantType(data.getJSONObject(i).getString("restauranttype")); //extract the restauranttype
                                        r.setLat(data.getJSONObject(i).getString("lat")); //extract the lat
                                        r.setLon(data.getJSONObject(i).getString("lon")); //extract the lon
                                        adapter.add(r); // add the record to the adapter
                                        */
                                        Integer Sqlid = data.getJSONObject(0).getInt("ID");
                                        String Sqlusername = data.getJSONObject(0).getString("Username");
                                        String Sqlfoodname = data.getJSONObject(0).getString("foodname");
                                        String Sqlcalories = data.getJSONObject(0).getString("calories");
                                        String Sqlimage = data.getJSONObject(0).getString("imageResource");
                                        String Sqltype = data.getJSONObject(0).getString("type");
                                        String Sqlpreparationtime = data.getJSONObject(0).getString("preparationtime");
                                        String Sqldescription = data.getJSONObject(0).getString("description");
                                        Integer Sqlrating = data.getJSONObject(0).getInt("rating");
                                        // Assuming LocalDBHelper is another class with an insertRecipe method
                                        LocalDBHelper.insertRecipe(Sqlid,Sqlusername, Sqlfoodname, Sqlcalories, Sqlimage, Sqltype, Sqlpreparationtime, Sqldescription, Sqlrating);
                                    }
                                } else {
                                   // empty.setVisibility(View.VISIBLE);
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

