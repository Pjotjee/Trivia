package com.example.trivia;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;

/** Get the user's name and score and put them into a list. */
public class HighscoreAdapter extends ArrayAdapter<Highscore> {
    private ArrayList<Highscore> highscores; // a list for the highscores

    //** constructor for the class */
    HighscoreAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Highscore> scores) {
        super(context, resource, scores);
        highscores = scores;
    }

    //** create the view's and put in the highscores */
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.highscore, parent,
                    false);
        }
        // the name and highscore views
        TextView name = view.findViewById(R.id.name);
        TextView highscore = view.findViewById(R.id.score);
        // and set the text ofthe views
        name.setText(highscores.get(position).getName() + " ");
        highscore.setText(highscores.get(position).getScore() + " ");
        return view;
    }

}