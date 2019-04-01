package com.example.trivia;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Collections;

public class HighscoreListActivity extends AppCompatActivity implements HighscoreGetRequest.Callback {
    ListView highscoreList; // list view of the highscore list

    //** create the activity */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscore_list);
        // request the high scores
        HighscoreGetRequest highscoreGetRequest = new HighscoreGetRequest(getApplicationContext());
        highscoreGetRequest.getHighscores(this);
        // initialize the highscore list and the start screen button
        highscoreList = findViewById(R.id.highScoreList);
        Button start = findViewById(R.id.restart);
        // when pressing start button go back to the start activity
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // go to start activity
                Intent intent = new Intent(HighscoreListActivity.this, StartActivity.class);
                startActivity(intent);
            }
        });
    }

    //** response when the callback was good */
    @Override
    public void gotHighscores(ArrayList<Highscore> scores) {
        // get the scores in descending order
        Collections.sort(scores, new Sort());
        // make the highscore adapter
        HighscoreAdapter highscoreAdapter = new HighscoreAdapter(this, R.layout.highscore, scores);
        highscoreList.setAdapter(highscoreAdapter);
    }

    //** response when the callback was bad */
    @Override
    public void gotHighscoresError(String message) {
        Toast.makeText(getApplicationContext(), "Failed to retrieve Highscores.",
                Toast.LENGTH_SHORT).show();
    }

    // when back button pressed, go back to home screen
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(HighscoreListActivity.this, StartActivity.class);
        startActivity(intent);
    }

}