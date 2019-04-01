package com.example.trivia;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

public class GameActivity extends AppCompatActivity implements Serializable {
    int version = Build.VERSION.SDK_INT;    // variable for build version
    private int questionCount;              // the current question
    private int wins;                       // how many questions answered correctly
    private int fails;                      // how many questions answered wrong
    private int score =0;                   // the score
    private TextView questionView;          // textView of the question
    private Button answer1;                 // the button for answer 1
    private Button answer2;
    private Button answer3;
    private Button answer4;
    private Button submit;                  // the submit button
    private TextView whichQuestion;         // displaying current question number
    private TextView whichScore;            // displaying the score
    private boolean answerChosen;           // boolean if there has been an answer chosen
    private boolean answerBoolean;          // boolean for correct or wrong answer
    private ArrayList<Question> questions;  // variable for array list questions

    /** create the activity */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        // get the variables from the View
        questionView = findViewById(R.id.question);
        answer1 = findViewById(R.id.answer1);
        answer2 = findViewById(R.id.answer2);
        answer3 = findViewById(R.id.answer3);
        answer4 = findViewById(R.id.answer4);
        submit = findViewById(R.id.submit);
        whichQuestion = findViewById(R.id.questionCount);
        whichScore = findViewById(R.id.scoreCount);
        // get the list of questions from the intent
        Intent intent = getIntent();
        questions = (ArrayList<Question>) intent.getSerializableExtra("questions");
        // set the number of the question and the score
        setInfo(questionCount, score);
        // get the question and bind it to the layout's views
        Question currentQuestion = questions.get(questionCount);
        bindViews(currentQuestion);
    }

    //** handle the view when an answer button is clicked */
    public void onAnswer(View view) {
        Button button = (Button) view;
        answer1 = findViewById(R.id.answer1);
        answer2 = findViewById(R.id.answer2);
        answer3 = findViewById(R.id.answer3);
        answer4 = findViewById(R.id.answer4);
        answerBoolean = getAnswer(questions.get(questionCount), button.getText().toString());
        // set the background of the right answer to green
        if (getAnswer(questions.get(questionCount), answer1.getText().toString())){
            answer1.setBackgroundColor(Color.GREEN);
        } else if (getAnswer(questions.get(questionCount), answer2.getText().toString())){
            answer2.setBackgroundColor(Color.GREEN);
        } else if (getAnswer(questions.get(questionCount), answer3.getText().toString())){
            answer3.setBackgroundColor(Color.GREEN);
        } else if (getAnswer(questions.get(questionCount), answer4.getText().toString())){
            answer4.setBackgroundColor(Color.GREEN);
        }
        // check if the answer was correct and add the score to the counters
        // if the score was wrong set the background to red
        if (answerBoolean) {
            score++;
            wins++;
            Toast.makeText(getApplicationContext(), "answer is correct!", Toast.LENGTH_SHORT).show();
        } else {
            button.setBackgroundColor(Color.RED);
            Toast.makeText(getApplicationContext(), "answer is incorrect! correct answer was " + questions.get(questionCount).getCorrectAnswer(), Toast.LENGTH_SHORT).show();
            fails++;
        }
        // if an answer button was clicked an answer has been chosen
        if (button.getText() != "submit" ) {
            answerChosen = true ;
        }
        answer1.setClickable(false);
        answer2.setClickable(false);
        answer3.setClickable(false);
        answer4.setClickable(false);
    }

    //** handle the view when the submit button is clicked */
    public void onSubmit(View view ) {
        // first check if an answer has been submitted
        if (answerChosen == true) {
            // get the question and bind it to the layout's views
            Question nextQuestion = questions.get(questionCount + 1);
            bindViews(nextQuestion);
            // keep track of question number
            questionCount++;
            // keep track of the score and question number and show to user
            setInfo(questionCount, score);
            answer1 = findViewById(R.id.answer1);
            answer2 = findViewById(R.id.answer2);
            answer3 = findViewById(R.id.answer3);
            answer4 = findViewById(R.id.answer4);
            // reset the stats for the next question
            answer1.setBackground(null);
            answer2.setBackground(null);
            answer3.setBackground(null);
            answer4.setBackground(null);
            answer1.setClickable(true);
            answer2.setClickable(true);
            answer3.setClickable(true);
            answer4.setClickable(true);
            // check if it is the last question if so, go to the highscore activity
            if (questionCount == questions.size() - 1) {
                // go to the highscore activity, put the game variables as intent
                Intent intent = new Intent(GameActivity.this, HighscoreActivity.class);
                intent.putExtra("score", score);
                intent.putExtra("wins", wins);
                intent.putExtra("fails", fails);
                startActivity(intent);
            }
        } else {
            Toast.makeText(getApplicationContext(), "please choose an answer",
                    Toast.LENGTH_SHORT).show();
        }
    }

    //** bind the text views to the question and answers */
    public void bindViews(Question question) {
        // make an array of shuffled answers
        ArrayList<String> answers = getShuffledAnswers(question);
        // show the question
        String questionText = getQuestion(question);
        questionView.setText(questionText);
        // set the first two answers
        answer1.setText(answers.get(0));
        answer2.setText(answers.get(1));
        // if the questions are true/false
        if (answers.size() == 2) {
            answer3.setVisibility(View.INVISIBLE);
            answer4.setVisibility(View.INVISIBLE);
        }
        // if the questions are multiple choice
        else {
            answer3.setVisibility(View.VISIBLE);
            answer4.setVisibility(View.VISIBLE);
            answer3.setText(answers.get(2));
            answer4.setText(answers.get(3));
        }
    }

    //** get the question */
    public String getQuestion(Question question) {
        return  Html.fromHtml(question.getQuestion(), version).toString();
    }

    //** make an array of the answers */
    public ArrayList<String> getShuffledAnswers(Question question) {
        // an array for the answers
        ArrayList<String> answers = new ArrayList<>();
        // fill the array with the correct and incorrect answers
        answers.add(Html.fromHtml(question.getCorrectAnswer(), version).toString());
        for (int i = 0; i < question.getIncorrectAnswers().size(); i++) {
            answers.add(Html.fromHtml(question.getIncorrectAnswers().get(i), version).toString());
        }
        // shuffle the answers and return the array
        Collections.shuffle(answers);
        return answers;
    }

    //** check if an answer is correct and return that value */
    public Boolean getAnswer(Question question, String answer) {
        if (question.getCorrectAnswer().equals(answer)) {
            return true;
        }else {
            return false;
        }
    }

    //** keep track of the score and the current question number and show that */
    public void setInfo(int questionCount, int score) {
        String questionText = "Question: " + (questionCount + 1) + "/" + questions.size();
        String scoreText = "Score: " + score;
        whichQuestion.setText(questionText);
        whichScore.setText(scoreText);
    }

    //** when back pressed, go to the start activity */
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(GameActivity.this, StartActivity.class);
        startActivity(intent);
    }
}