package com.example.trivia;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

/** The user fills in his name and put it in the highscore list with their score. Then show the highscorelist */
public class HighscoreActivity extends AppCompatActivity implements Response.Listener<String>, Response.ErrorListener {
    private int score;                                                                  // the user's score
    private static String URL = "https://ide50-pjotjee.legacy.cs50.io:8080/highscores"; //url of the database

    /** create the activity */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscore);
        // get the variables from the intent
        Intent intent = getIntent();
        score = intent.getIntExtra("score", 0);
        int correct = intent.getIntExtra("wins", 0);
        int incorrect = intent.getIntExtra("fails", 0);
        // initiate the button and views
        TextView textOne = findViewById(R.id.textOne);
        TextView textTwo = findViewById(R.id.textTwo);
        // show the results to the user
        String resultTextOne = "Thank you! But the Princess is in another castle! You have " + correct +
                " questions good and " + incorrect + " questions wrong!";
        String resultTextTwo = "Your score is " + score + "! Please fill in your name to submit" +
                "your score!";
        textOne.setText(resultTextOne);
        textTwo.setText(resultTextTwo);
    }

    //** when the name is submitted update the highscorelist and go to the highscore list activity */
    public void onClick(View view) {
        EditText nameText = findViewById(R.id.name);
        RequestQueue queue = Volley.newRequestQueue(this);
        String scoreString = String.valueOf(score);
        // check if the has been given a name
        if (nameText.getText().toString().trim().equals("")) {
            Toast.makeText(getApplicationContext(), "please insert name",
                    Toast.LENGTH_SHORT).show();
        } else {
            // get the name and add the score and post it
            String name = nameText.getText().toString().trim();
            HighscorePostRequest highsorePostRequest = new HighscorePostRequest(Request.Method.POST, URL, this, this, name, scoreString);
            queue.add(highsorePostRequest);
            // go to the highscore list activity using the intent
            Intent intent = new Intent(HighscoreActivity.this, HighscoreListActivity.class);
            startActivity(intent);
        }
    }

    //** go to the start activity when user pressed on the back button */
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(HighscoreActivity.this, StartActivity.class);
        startActivity(intent);
    }

    //** a response in case of an error */
    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(this, error.toString(), Toast.LENGTH_SHORT).show();
    }

    //** a response for the request */
    @Override
    public void onResponse(String response) {
        Toast.makeText(this, response, Toast.LENGTH_LONG).show();
    }

}