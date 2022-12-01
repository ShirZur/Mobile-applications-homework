package com.example.hw1;

import android.content.Context;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;

public class GameManager {

    public int index = 0;
    private int wrong = 0;
    private Context context;
    private int life;
    private final int NUM_OF_LANES = 3;
    private final int NUM_OF_ROWS = 8;
    private ArrayList<ShapeableImageView> arrOfBirds;
    private LinearLayout panel_lanes;
    private ArrayList<LinearLayout> arrOfLayout;
    private ImageView[][] matOfEagle;
    private boolean[][] isVisible;
    private int curPos = 1;
    private MainActivity mainActivity;
    private Timer timer;
    private Vibrator vibrator;
    private Toast toast;
    private int timeToAddEagle;
    private int ticks = 0;
    private int checkLastLine = 0;
    private Random random = new Random();


    public GameManager(Context context, int life, MainActivity mainActivity, int timeToAddEagle, Toast toast, Vibrator vibrator){
        arrOfBirds = new ArrayList<>(NUM_OF_LANES);
        arrOfLayout = new ArrayList<>(NUM_OF_LANES);
        this.toast = toast;
        this.life = life;
        this.vibrator = vibrator;
        Log.d("abcd","shy");
        this.timeToAddEagle = timeToAddEagle;
        matOfEagle = new ImageView[8][3];
        isVisible = new boolean[8][3];
        for(int i =0;i<8;i++){
            for (int j = 0; j < 3; j++) {
                isVisible[i][j] = false;
            }
        }
        this.context = context;

        this.mainActivity = mainActivity;
    }

    public LinearLayout getPanel_lanes() {
        return panel_lanes;
    }

    public GameManager setPanel_lanes(LinearLayout panel_lanes) {
        this.panel_lanes = panel_lanes;
        return this;
    }

    public int getCurPos() {
        return curPos;
    }

    public GameManager setCurPos(int curPos) {
        this.curPos = curPos;
        return this;
    }

    public int getIndex() {
        return index;
    }

    public GameManager setIndex(int index) {
        this.index = index;
        return this;
    }

    public int getWrong() {
        return wrong;
    }

    public GameManager setWrong(int wrong) {
        this.wrong = wrong;
        return this;
    }

    public int getLife() {
        return life;
    }

    public GameManager setLife(int life) {
        this.life = life;
        return this;
    }

    public boolean[][] getIsVisible() {
        return isVisible;
    }

    public int getNUM_OF_LANES() {
        return NUM_OF_LANES;
    }

    public ArrayList<ShapeableImageView> getArrOfBirds() {
        return arrOfBirds;
    }

    public GameManager setArrOfBirds(ArrayList<ShapeableImageView> arrOfBirds) {
        this.arrOfBirds = arrOfBirds;
        return this;
    }

    public ImageView[][] getMatOfEagle() {
        return matOfEagle;
    }

    public void insertImageView(){
        for(int i =0;i<NUM_OF_LANES;i++){
            ShapeableImageView bird = new ShapeableImageView(context);
            bird.setId(i + 1);
            bird.setImageResource(R.drawable.bird2);
            if (i != 1) {
                bird.setVisibility(View.INVISIBLE);
            }
            arrOfBirds.add(bird);
            LinearLayout linearLayout1 = new LinearLayout(context);
            linearLayout1.setGravity(Gravity.BOTTOM);
            linearLayout1.setOrientation(LinearLayout.VERTICAL);
            linearLayout1.setId(View.generateViewId());
            insertEagle(i,linearLayout1);
            arrOfLayout.add(linearLayout1);
            arrOfLayout.get(i).addView(bird, new   LinearLayout.LayoutParams
                    (LinearLayout.LayoutParams.MATCH_PARENT,   0,1));
            panel_lanes.addView(linearLayout1, new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT,1));
        }
    }


    private void insertEagle(int i, LinearLayout linearLayout1){
        for (int j = 0; j < NUM_OF_ROWS; j++) {
            ImageView imageView = new ImageView(context);
            imageView.setId(View.generateViewId());
            imageView.setImageResource(R.drawable.eagle2);
            imageView.setVisibility(View.INVISIBLE);
            imageView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0,1));
            matOfEagle[j][i] = imageView;
            linearLayout1.addView(imageView);
        }
    }


    public void update() {
        checkCrash();
        ticks++;

        for (int i = NUM_OF_ROWS - 1; i >= 1 ; i--) {
            for (int j = 0; j < 3; j++) {
                isVisible[i][j]= isVisible[i - 1][j];
            }
        }

        for (int i = 0; i < NUM_OF_LANES; i++) {
            isVisible[0][i] = false;
        }

        if(ticks == timeToAddEagle){
            isVisible[0][random.nextInt(3)] = true;
            ticks = 0;
        }
    }

    public void checkCrash(){
        if(isVisible[NUM_OF_ROWS - 1][curPos]){
            wrong++;
            mainActivity.refreshUILifes();
            toast.show();
            vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        }
    }
}
