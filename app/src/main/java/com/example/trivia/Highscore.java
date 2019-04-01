package com.example.trivia;

import java.io.Serializable;

/** highscore with name and score */
public class Highscore implements Serializable { //Comparable<Highscore> {
    private String name;    // user's name
    private int score;      // user's score

    //** constructor for the class */
    public Highscore(String name, int score) {
        this.name = name;
        this.score = score;
    }

    //** getters for the class */
    public int getScore() {
        return score;
    }

    public String getName() {
        return name;
    }



}