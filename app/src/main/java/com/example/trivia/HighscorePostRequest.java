package com.example.trivia;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import java.util.HashMap;
import java.util.Map;

/** Posts the user's score and name. */
public class HighscorePostRequest extends StringRequest{
    private static String name, score; // The name and the score of the user

    //** creating the constructor of the class */
    public HighscorePostRequest(int method, String url, Response.Listener<String> listener,
                       Response.ErrorListener errorListener, String name, String score) {
        super(method, url, listener, errorListener);
        this.score = score;
        this.name = name;
    }

    //** putting the parameters to the request */
    @Override
    protected Map<String, String> getParams() {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("score", score);
        return params;
    }
}
