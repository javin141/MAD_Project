package com.sp.mad_project;

import java.util.HashMap;



public class AstraHelper {
    static String region = "us-east1";
    static String url = region + "/v2/keyspaces/app_space/recipe_table/{primary_key}";
    static String Cassandra_Token = "AstraCS:ZMetAiPMmTGKEhjTXESYvyTO:964b9d72dd52cfcb550fa1a2793190b66bac9a21939666be4efda2f8eee492a7";
    static int lastID = 0;
    static HashMap getHeader() {
        HashMap<String, String> headers = new HashMap<~>();
        headers.put("Content-Type", "application/json");
        headers.put("X-Cassandra-Token", Cassandra_Token);
        headers.put("Accept", "application/json");
        return headers;
    }


}
