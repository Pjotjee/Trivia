package com.example.trivia;


import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import java.util.ArrayList;

/** Start activity with title screen and play button. */
public class StartActivity extends AppCompatActivity implements QuestionsRequest.Callback {
    private Spinner difficultyOptions;                              // spinner for the different difficulties
    private Spinner questionTypeOptions;                            // spinner for the different question types
    private int numberOfQuestions;                                  // the total number of questions
    private int category;                                           // the question category
    private String difficulty;                                      // the question difficulty
    private String questionType;                                    // the question type
    private String ApiUrl = "https://opentdb.com/api.php?amount=";  // url for the questions
    private Context context;                                        // the context of the activity

    //** initialize the arrays for the spinners */
    private static final String[] categories = {"Any Category", "General Knowledge", "Entertainment:"
            + " Books", "Entertainment: Film", "Entertainment: Music", "Entertainment: Musicals &" +
            "Theatres", "Entertainment: Television", "Entertainment: Video Games", "Entertainment:" +
            "Board Games", "Science & Nature", "Science: Computers", "Science: Mathematics",
            "Mythology", "Sports", "Geography", "History", "Politics", "Art", "Celebrities",
            "Animals", "Vehicles", "Entertainment: Comics", "Science: Gadgets", "Entertainment:" +
            "Japanese Anime & Manga", "Entertainment: Cartoon & Animations"};
    private static final String[] difficulties = {"Any Difficulty", "Easy", "Medium", "Hard"};
    private static final String[] types = {"Any Type", "Multiple Choice", "True / False"};

    //** create the activity */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        // create the options
        Spinner categoriesOptions = findViewById(R.id.category);
        difficultyOptions = findViewById(R.id.difficulty);
        questionTypeOptions = findViewById(R.id.type);
        // get the context
        context = getApplicationContext();
        // initialize the buttons
        Button startButton = findViewById(R.id.start);
        Button highscoresButton = findViewById(R.id.highscores);
        // the variable for the number of questions
        final EditText numQuestions = findViewById(R.id.numQuestions);
        // create adapter for the different options
        ArrayAdapter<String> categoriesAdapter = new ArrayAdapter<>(StartActivity.this, android.R.layout.simple_spinner_item, categories);
        ArrayAdapter<String> difficultyAdapter = new ArrayAdapter<>(StartActivity.this, android.R.layout.simple_spinner_item, difficulties);
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(StartActivity.this, android.R.layout.simple_spinner_item, types);
        categoriesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        difficultyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // setters for the options
        categoriesOptions.setAdapter(categoriesAdapter);
        difficultyOptions.setAdapter(difficultyAdapter);
        questionTypeOptions.setAdapter(typeAdapter);

        //** listener for the selected category */
        categoriesOptions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            // get the category
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    category = position + 8;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // the default category will be given
            }
        });

        //** listener for the start button */
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // get the input
                difficulty = difficultyOptions.getSelectedItem().toString();
                questionType = questionTypeOptions.getSelectedItem().toString();
                // default for number of questions
                if (TextUtils.isEmpty(numQuestions.getText())) {
                    ApiUrl += 10;
                }
                // set the number of questions
                else {
                    numberOfQuestions = Integer.parseInt(numQuestions.getText().toString());
                    // 100 questions max
                    if (numberOfQuestions >= 100) {
                        Toast.makeText(context, "maximum number of questions is 100",
                                Toast.LENGTH_SHORT).show();
                        ApiUrl += 100;
                    }
                }
                // add everything to the api url
                ApiUrl += numberOfQuestions;
                if (category != 0) {
                    ApiUrl += "&category=" + Integer.toString(category);
                }
                if (!difficulty.equals("Any Difficulty")) {
                    ApiUrl += "&difficulty=" + difficulty.toLowerCase();
                }
                if (!questionType.equals("Any Type")) {
                    ApiUrl += "&type=";
                    if (questionType.equals("Multiple Choice")) {
                        ApiUrl += "multiple";
                    } else if (questionType.equals("True / False")) {
                        ApiUrl += "boolean";
                    }
                }
                // the questions request
                QuestionsRequest questionRequest = new QuestionsRequest(context);
                questionRequest.getQuestions(StartActivity.this, ApiUrl);
            }
        });

        //** listener for the highscore button to go to highscorelist activity */
        highscoresButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StartActivity.this, HighscoreListActivity.class);
                startActivity(intent);
            }
        });
    }

    //** put the questions in the intent */
    @Override
    public void gotQuestions(ArrayList<Question> questions) {
        Intent intent = new Intent(StartActivity.this, GameActivity.class);
        intent.putExtra("questions", questions);
        startActivity(intent);
    }

    //** error response message */
    @Override
    public void gotQuestionsError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}