package com.example.board;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashActivity extends AppCompatActivity implements Animation.AnimationListener {

    ImageView iv15;
    TextView tvPuzzle;
    Animation animFadeIn;
    boolean first = true;//אומר אם כבר הראנו את ה"15"
    private final int SPLASH_DISPLAY_LENGTH = 2500;//הזמן שהמסך מופיע

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_Board);
        setContentView(R.layout.activity_splash);
        init();
        iv15.startAnimation(animFadeIn);//הפעלת האנימציה


        //מחכה את כמות הזמן שהגדרנו לו ולאחר מכן מעביר אותנו למסך הראשי
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                // יצירת intent שיעביר אותנו למסך הראשי
                Intent mainIntent = new Intent(SplashActivity.this,MainActivity.class);
                SplashActivity.this.startActivity(mainIntent);
                SplashActivity.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }

    //פעולה שמאתחלת את האובייקטים ומגדירה כמה הגדרות
    private void init(){


        getSupportActionBar().hide();//מסתיר את הסרגל למעלה
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//הופף את הactivity למסך מלא
        iv15 = findViewById(R.id.tv15);

        tvPuzzle = findViewById(R.id.tvPuzzle);
        animFadeIn= AnimationUtils.loadAnimation(this,R.anim.fade_in);
        animFadeIn.setAnimationListener(this);

    }

    //פעולה שמופעלת מתי שהאנימציה מתחילה
    @Override
    public void onAnimationStart(Animation animation) {

    }
    //פעולה שמופעלת מתי שהאנימציה נגמרת
    @Override
    public void onAnimationEnd(Animation animation) {
        if (first)//אחרי האנימציה הראשונה
        {
            iv15.setVisibility(View.VISIBLE);
            iv15.clearAnimation();//מחיקת האנימציה מהתמונה בשביל שלא תופעל שוב

            tvPuzzle.startAnimation(animFadeIn);//הפעלת האנימציה על הטקסט "puzzle"

            first = false;
        }
        else tvPuzzle.setVisibility(View.VISIBLE);//אחרי האנימציה השניה
    }

    //פעולה שמופעלת מתי שהאנימציה חוזרת על עצמה
    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}