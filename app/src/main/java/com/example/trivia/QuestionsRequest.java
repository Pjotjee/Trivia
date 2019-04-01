package com.example.trivia;

import android.content.Context;
import android.util.Log;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

/** request a question from the API */
public class QuestionsRequest implements Response.Listener<JSONObject>, Response.ErrorListener {
    private Context context;                                    // context of the activity
    private Callback activity;                                  // Callback of the activity
    private ArrayList<Question> questions = new ArrayList<>();  // array list for the question objects

    //** callback of the question */
    public interface Callback {
        void gotQuestions(ArrayList<Question> questions);
        void gotQuestionsError(String message);
    }

    //** constructor for the class */
    public QuestionsRequest(Context context) {
        this.context = context;
    }

    //** get the questions */
    void getQuestions(Callback activity, String url) {
        // queue for the questions
        RequestQueue queue = Volley.newRequestQueue(context);
        try {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null, this, this);
            queue.add(jsonObjectRequest);
        }
        catch(Exception error) {
            Log.e("error", error.getMessage());
        }
        this.activity = activity;
    }

    //** response from the json object */
    @Override
    public void onResponse(JSONObject response) {
        try {
            // get json objects and make it question objects
            JSONArray questionsArray = response.getJSONArray("results");
            for (int i = 0; i < questionsArray.length(); i++) {
                JSONObject questionObject = questionsArray.getJSONObject(i);
                String category = questionObject.getString("category");
                String type = questionObject.getString("type");
                String difficulty = questionObject.getString("difficulty");
                String question = questionObject.getString("question");
                String correctAnswer = questionObject.getString("correct_answer");
                JSONArray incorrectAnswers = questionObject.getJSONArray("incorrect_answers");
                ArrayList<String> incorrectAnswersArray = new ArrayList<>();
                for (int j = 0; j < incorrectAnswers.length(); j++) {
                    incorrectAnswersArray.add(incorrectAnswers.get(j).toString());
                }
                // add the questions to the list
                Question objectQuestion =  new Question(category, type, difficulty, question,
                        correctAnswer, incorrectAnswersArray);
                questions.add(objectQuestion);
            }
            activity.gotQuestions(questions);
        }
        catch (JSONException e) {
            activity.gotQuestionsError(e.getMessage());
        }
    }

    //** error response message */
    @Override
    public void onErrorResponse(VolleyError error) {
        activity.gotQuestionsError(error.getMessage());
    }
}