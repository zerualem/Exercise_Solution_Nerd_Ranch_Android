package com.bignerdranch.android.geoquiz;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class QuizActivity extends AppCompatActivity {

    private Button mTrueButton;
    private Button mFalseButton;
    private Button mNextButton;
    private TextView mQuestionTextView;

    private Question[] mQuestionBank = new Question[]{
            new Question(R.string.question_australia, true),
            new Question(R.string.question_oceans, true),
            new Question(R.string.question_mideast, false),
            new Question(R.string.question_africa, false),
            new Question(R.string.question_americas, true),
            new Question(R.string.question_asia, true),
    };

    private int mCurrentIndex = 0;
    private int mCorrectAnswers = 0;


    private static final String TAG = "QuizActivity";
    private static final String KEY_INDEX = "index";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate(Bundle) called");
        if (savedInstanceState != null){
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX);
        }
        setContentView(R.layout.activity_quiz);

        mTrueButton = (Button) findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswer(true);
                updateButtons(false);
            }
        });
        mFalseButton = (Button) findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswer(false);
                updateButtons(false);
            }
        });

        mNextButton = (Button) findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentIndex++;
                mCurrentIndex %= mQuestionBank.length;
                updateQuestion();
            }
        });

        mQuestionTextView = (TextView) findViewById(R.id.question_text_view);
        updateQuestion();
    }

    private void updateQuestion(){
        Question question = mQuestionBank[mCurrentIndex];
        mQuestionTextView.setText(question.getTextResId());
        updateButtons(true);

    }

    private void updateButtons(boolean isUnanswered){
        mFalseButton.setEnabled(isUnanswered);
        mTrueButton.setEnabled(isUnanswered);
    }

    private void checkAnswer(boolean userPressedTrue){

        int messageResId = 0;

        if (mQuestionBank[mCurrentIndex].isAnswerTrue() == userPressedTrue){
            messageResId = R.string.correct_toast;
            mCorrectAnswers++;
        }
        else{
            messageResId = R.string.incorrect_toast;
        }
        Toast.makeText(QuizActivity.this, messageResId, Toast.LENGTH_SHORT).show();

        if (mCurrentIndex == mQuestionBank.length - 1){
            double perc = (double) mCorrectAnswers / mCurrentIndex * 100;
            String result = String.format("%.2f", perc);
            Log.d(TAG, perc + "");
            Toast.makeText(QuizActivity.this,"You answered "+ result + "% correctly", Toast.LENGTH_LONG).show();
            mCorrectAnswers = 0;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() called");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "onSaveInstanceState(Bundle) called");
        outState.putInt(KEY_INDEX, mCurrentIndex);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }
}
