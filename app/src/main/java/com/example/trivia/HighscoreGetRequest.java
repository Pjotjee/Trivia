package com.example.trivia;

import android.content.Context;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

/** Request current highscores from server. */
public class HighscoreGetRequest implements Response.Listener<JSONArray>, Response.ErrorListener {
    private Context context;                                        // context of the class
    private ArrayList<Highscore> highscores = new ArrayList<>();    // list with highscores
    private HighscoreGetRequest.Callback activity;                  // callback from another activity

    //** the callback from the requests */
    public interface Callback {
        void gotHighscores(ArrayList<Highscore> highscores);
        void gotHighscoresError(String message);
    }

    //** constructor for the class */
    public HighscoreGetRequest(Context context) {
        this.context = context;
    }

    //** retrieve the highscores from the server */
    void getHighscores(HighscoreGetRequest.Callback activity) {
        // create a queue
        RequestQueue queue = Volley.newRequestQueue(context);
        // the server on CS50 IDE, won't work if not running
        String url = "https://ide50-pjotjee.legacy.cs50.io:8080/highscores";
        // request a json array and add that to the queue
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(url, this, this);
        queue.add(jsonObjectRequest);
        // save the callback activity
        this.activity = activity;
    }

    //** an successful response from the api */
    @Override
    public void onResponse(JSONArray response) {
        try {
            // go through the list and add the highscore and name to the list
            for (int i = 0; i < response.length(); i++) {
                JSONObject highscoreObject = response.getJSONObject(i);
                String name = highscoreObject.getString("name");
                int score = highscoreObject.getInt("score");
                Highscore highscore = new Highscore(name, score );
                highscores.add(highscore);
            }
        }
        // give an error if it failed
        catch(JSONException error) {
            activity.gotHighscoresError(error.getMessage());
        }
        // add the highscores to the callback
        activity.gotHighscores(highscores);
    }

    //** an error response from the api */
    @Override
    public void onErrorResponse(VolleyError error) {
        activity.gotHighscoresError(error.getMessage());
    }
}