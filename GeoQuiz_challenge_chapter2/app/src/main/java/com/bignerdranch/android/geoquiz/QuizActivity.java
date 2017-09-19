package com.bignerdranch.android.geoquiz;

import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class QuizActivity extends AppCompatActivity {

    private Button mTrueButton;
    private Button mFalseButton;
    private ImageButton mNextButton;
    private ImageButton mPreviousButton;
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
    public static final String BUNDLE_KEY_INDEX = "question_index";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        //mCurrentIndex = savedInstanceState.getInt(BUNDLE_KEY_INDEX);

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

        mNextButton = (ImageButton) findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentIndex++;
                mCurrentIndex %= mQuestionBank.length;
                updateQuestion();
            }
        });

        mPreviousButton = (ImageButton) findViewById(R.id.previous_button);
        mPreviousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentIndex--;
                if (mCurrentIndex < 0){
                    mCurrentIndex = mQuestionBank.length - 1;
                }
                updateQuestion();
            }
        });

        mQuestionTextView = (TextView) findViewById(R.id.question_text_view);
        mQuestionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentIndex++;
                mCurrentIndex %= mQuestionBank.length;
                updateQuestion();
            }
        });
        updateQuestion();
    }

    private void updateQuestion(){
        Question question = mQuestionBank[mCurrentIndex];
        mQuestionTextView.setText(question.getTextResId());

    }

    private void checkAnswer(boolean userPressedTrue){

        int messageResId = 0;

        if (mQuestionBank[mCurrentIndex].isAnswerTrue() == userPressedTrue){
            messageResId = R.string.correct_toast;
        }
        else
            messageResId = R.string.incorrect_toast;
        Toast.makeText(QuizActivity.this, messageResId, Toast.LENGTH_SHORT).show();
    }

}
