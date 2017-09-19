package com.bignerdranch.android.geoquiz;

import android.app.Activity;
import android.content.Intent;
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
    private Button mCheatButton;
    private TextView mQuestionTextView;
    private TextView mCheatCountTextView;

    private Question[] mQuestionBank = new Question[]{
            new Question(R.string.question_australia, true),
            new Question(R.string.question_oceans, true),
            new Question(R.string.question_mideast, false),
            new Question(R.string.question_africa, false),
            new Question(R.string.question_americas, true),
            new Question(R.string.question_asia, true),
    };

    private int mCurrentIndex = 0;
    private boolean mIsCheater;
    private int mCheatCounter = 0;

    private static final String TAG = "QuizActivity";
    private static final String KEY_INDEX = "index";
    private static final String KEY_CHEAT_COUNT = "cheat_counter";
    private static final int REQUEST_CODE_CHEAT = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate(Bundle) called");
        if (savedInstanceState != null){
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX);
            mCheatCounter = savedInstanceState.getInt(KEY_CHEAT_COUNT);
        }
        setContentView(R.layout.activity_quiz);

        mTrueButton = (Button) findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswer(true);
            }
        });
        mFalseButton = (Button) findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswer(false);
            }
        });

        mNextButton = (Button) findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentIndex++;
                mCurrentIndex %= mQuestionBank.length;
                mIsCheater = false;
                updateQuestion();
            }
        });
        mCheatButton = (Button) findViewById(R.id.cheat_button);
        mCheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Start CheatActivity
                Intent intent = CheatActivity.newIntent(QuizActivity.this, mQuestionBank[mCurrentIndex].isAnswerTrue());
                startActivityForResult(intent, REQUEST_CODE_CHEAT);
            }
        });

        mQuestionTextView = (TextView) findViewById(R.id.question_text_view);
        mCheatCountTextView = (TextView) findViewById(R.id.number_cheat_text_view);

        mCheatCountTextView.setText(getResources().getString(R.string.cheat_counter, mCheatCounter));
        updateQuestion();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK){
            return;
        }

        if (requestCode == REQUEST_CODE_CHEAT){
            if (data == null){
                return;
            }

            mIsCheater = CheatActivity.wasAnswerShown(data);
            if (mIsCheater)
                mCheatCounter++;
            if (mCheatCounter > 2){
                mCheatButton.setEnabled(false);
            }
            mCheatCountTextView.setText(getResources().getString(R.string.cheat_counter, mCheatCounter));
        }
    }

    private void updateQuestion(){
        Question question = mQuestionBank[mCurrentIndex];
        mQuestionTextView.setText(question.getTextResId());

    }

    private void checkAnswer(boolean userPressedTrue){

        int messageResId = 0;

        if (mIsCheater){
            messageResId = R.string.judgment_toast;
        }else{
            if (mQuestionBank[mCurrentIndex].isAnswerTrue() == userPressedTrue){
                messageResId = R.string.correct_toast;
            }
            else
                messageResId = R.string.incorrect_toast;
        }

        Toast.makeText(QuizActivity.this, messageResId, Toast.LENGTH_SHORT).show();
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
        outState.putInt(KEY_CHEAT_COUNT, mCheatCounter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }
}
