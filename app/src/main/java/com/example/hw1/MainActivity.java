package com.example.hw1;


import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private ShapeableImageView IMG_left;
    private ShapeableImageView IMG_right;
    private ShapeableImageView[] game_IMG_hearts;
    private final int NUM_OF_LANES = 3;
    private final int NUM_OF_ROWS = 8;
    private LinearLayout panel_lanes;
    private Context context = null;
    private GameManager game;
    private Timer timer;
    private final int DELAY = 500;
    private Toast toast;
    private Vibrator vibrator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar;
        actionBar = getSupportActionBar();
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#C1EBFD"));
        actionBar.setBackgroundDrawable(colorDrawable);
        toast = Toast.makeText(this, "Boom!", Toast.LENGTH_LONG);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        context = this;
        game = new GameManager(context, 3, this,  2);
        timer = new Timer();
        startGame();

    }

    private void startGame() {
        findViews();
        game.setPanel_lanes(panel_lanes);
        game.insertImageView();
        updateTimerUI();
        IMG_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int curPos = game.getCurPos();
                if (game.getArrOfBirds().get(2).getVisibility() != View.VISIBLE) {
                    game.getArrOfBirds().get(curPos + 1).setVisibility(View.VISIBLE);
                    game.getArrOfBirds().get(curPos).setVisibility(View.INVISIBLE);
                    game.setCurPos(curPos + 1);
                }
            }
        });
        IMG_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int curPos = game.getCurPos();
                if (game.getArrOfBirds().get(0).getVisibility() != View.VISIBLE) {
                    game.getArrOfBirds().get(curPos - 1).setVisibility(View.VISIBLE);
                    game.getArrOfBirds().get(curPos).setVisibility(View.INVISIBLE);
                    game.setCurPos(curPos - 1);
                }
            }
        });
    }

    private void findViews() {
        IMG_left = findViewById(R.id.IMG_left);
        IMG_right = findViewById(R.id.IMG_right);
        panel_lanes = findViewById(R.id.panel_lanes);
        game_IMG_hearts = new ShapeableImageView[]{
                findViewById(R.id.game_IMG_heart1),
                findViewById(R.id.game_IMG_heart2),
                findViewById(R.id.game_IMG_heart3)
        };

    }

    private void updateTimerUI() {
        timer = new Timer();
        timer.scheduleAtFixedRate(
                new TimerTask() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                game.update();
                                updateUI();
                            }
                        });
                    }
                }
                , DELAY, DELAY);
    }

    public void refreshUILifes() {
        if (game.getWrong() != 0 && !(game.getWrong() > game.getLife()))
            game_IMG_hearts[game_IMG_hearts.length - game.getWrong()].setVisibility(View.INVISIBLE);
        toast.show();
        vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        //we defined the API to be 26+
    }

    public void updateUI() {
        for (int i = 0; i < NUM_OF_ROWS; i++) {
            for (int j = 0; j < NUM_OF_LANES; j++) {
                if(game.getIsVisible()[i][j]){
                    game.getMatOfEagle()[i][j].setVisibility(View.VISIBLE);
                }
                else{
                    game.getMatOfEagle()[i][j].setVisibility(View.INVISIBLE);
                }
            }

        }
    }


}