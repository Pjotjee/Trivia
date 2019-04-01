package com.example.trivia;

import java.util.Comparator;

//** returns the score in descending order */
public class Sort implements Comparator<Highscore> {
    @Override
    public int compare(Highscore a, Highscore b) {
        return  b.getScore() - a.getScore();
    }
}