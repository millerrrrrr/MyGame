package com.zhu.mygame;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zhu.mygame.db.ScoreDao;
import com.zhu.mygame.view.GameItem;
import com.zhu.mygame.view.GameLayout;

public class MainActivity extends AppCompatActivity {

    private static final int CALCULATION_TIME = 100;

    private Context mContext;
    private GameLayout gameLayout;

    private FrameLayout flTranslation;
    private TranslateAnimation mTranslateAnimation;

    private TextView tvScore;
    private TextView tvRecord;
    private TextView tvDuration;
    private TextView tvMaxScore;

    private TextView tvStart;

    private long mTime = 0;

    private ScoreDao mDao;

    private MediaPlayer mPlayer;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case CALCULATION_TIME:
                    mHandler.removeMessages(CALCULATION_TIME);
                    mTime += 1000;

                    tvDuration.setText("DURATION:  " + formatTime());

                    mHandler.sendEmptyMessageDelayed(CALCULATION_TIME, 1000);
                    break;
            }

        }
    };

    private String formatTime() {

        return (mTime / 1000) + " s";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;
        gameLayout = findViewById(R.id.game_layout);
        flTranslation = findViewById(R.id.fl_translation);
        tvScore = findViewById(R.id.tv_score);
        tvRecord = findViewById(R.id.tv_record);
        tvDuration = findViewById(R.id.tv_duration);
        tvStart = findViewById(R.id.tv_start);
        tvMaxScore = findViewById(R.id.tv_max_score);

        mPlayer = MediaPlayer.create(mContext, R.raw.move);

        mDao = new ScoreDao();

        getMaxScore();

        initListener();
    }

    private void getMaxScore() {
        int maxScore = mDao.getMaxScore();
        tvMaxScore.setText("MAX_SCORE:  " + maxScore);
    }

    private void initListener() {
        gameLayout.setOnGameListener(new GameLayout.OnGameListener() {
            @Override
            public void onScoreUpdate(int addScore, int totalScore) {
                tvScore.setText("SCORE:  " + totalScore);
            }

            @Override
            public void onGameOver(int score) {
                mPlayer.start();
                mHandler.removeMessages(CALCULATION_TIME);
                saveRecord(score);
                getMaxScore();
                showGameOverDialog(score);
            }

            @Override
            public void onTranslation(GameItem item, int startX, int endX, int startY, int endY, int duration) {

                flTranslation.addView(item);

                Log.e("TAG", "translation = " + startX + "  " + endX + "   " + startY + "   " + endY);

                mTranslateAnimation = new TranslateAnimation(startX, endX, startY, endY);

                mTranslateAnimation.setDuration(duration);
                mTranslateAnimation.setInterpolator(new AccelerateInterpolator());

                item.setAnimation(null);
                item.startAnimation(mTranslateAnimation);

                mTranslateAnimation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        flTranslation.removeAllViews();
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });

            }

        });

        tvRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, RecordActivity.class);
                startActivity(intent);
            }
        });

        tvStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameLayout.setClickable(true);
                tvStart.setVisibility(View.GONE);
                mHandler.sendEmptyMessageDelayed(CALCULATION_TIME, 0);
            }
        });

    }

    private void saveRecord(int score) {
        mDao.insert(score, (mTime / 1000) + " s");
    }

    private void showGameOverDialog(int score) {

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("GAME OVER");
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setCancelable(false);
        builder.setMessage(getResources().getString(R.string.dialog_msg) + " " + score + " !");
        builder.setPositiveButton("confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                gameLayout.reset();
                reset();
            }
        });

        builder.show();

    }

    private void reset() {
        tvDuration.setText("DURATION:  0 s");
        tvScore.setText("SCORE:  0");
        tvStart.setVisibility(View.VISIBLE);
        gameLayout.setClickable(false);
        mTime = 0;
    }

    private long lastPressTime;

    @Override
    public void onBackPressed() {

        long currentPressTime = System.currentTimeMillis();

        if (lastPressTime == 0 || currentPressTime - lastPressTime > 1000) {
            lastPressTime = currentPressTime;
            Toast.makeText(mContext, "Click again to exit!", Toast.LENGTH_SHORT).show();
            return;
        }

        super.onBackPressed();
    }

}
