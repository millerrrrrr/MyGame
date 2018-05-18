package com.zhu.mygame.view;


import android.content.Context;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;

import com.zhu.mygame.R;

import java.util.ArrayList;
import java.util.List;

public class GameLayout extends FrameLayout {

    private Context mContext;

    private MediaPlayer mPlayer;

    private List<Point> emptyPoints;

    private GameItem[][] mGameMap;

    private int mItemCount = 4;

    private int mItemWidth, mItemHeight;

    private int mItemSpan = 10;

    private int startX, startY, offsetX, offsetY;

    private int mRandomCount = 2;

    private int mThreshold;

    private int mTotalScore;

    private boolean isGameOver;

    private boolean isFirstLayout = true;

    private int mDuration = 100;

    private AnimationSet mAnimationSet;
    private ScaleAnimation mScaleAnimation;
    private AlphaAnimation mAlphaAnimation;

    public GameLayout(Context context) {
        this(context, null);
    }

    public GameLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GameLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mContext = getContext();
        mPlayer = MediaPlayer.create(mContext, R.raw.move);
        emptyPoints = new ArrayList<>();
        mThreshold = ViewConfiguration.get(mContext).getScaledTouchSlop();

    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mItemWidth = (w - (mItemCount + 1) * mItemSpan) / mItemCount;
        mItemHeight = (h - (mItemCount + 1) * mItemSpan) / mItemCount;

        mGameMap = new GameItem[mItemCount][mItemCount];

        initGameItem();

    }

    private void initGameItem() {

        removeAllViews();

        GameItem item;

        LayoutParams params;

        for (int i = 0; i < mItemCount * mItemCount; i++) {
            item = new GameItem(mContext);
            params = new LayoutParams(mItemWidth, mItemHeight);
            item.setLayoutParams(params);

            addView(item);
        }

    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {

        int childCount = getChildCount();

        int startX = mItemSpan;
        int startY = mItemSpan;

        int x = 0;
        int y = 0;

        for (int i = 0; i < childCount; i++) {

            View child = getChildAt(i);
            child.layout(startX, startY, startX + mItemWidth, startY + mItemHeight);

            startX += mItemSpan + mItemWidth;

            mGameMap[x][y] = (GameItem) child;
            x++;
            if (i % mItemCount == (mItemCount - 1)) {
                x = 0;
                y++;
                startX = mItemSpan;
                startY += mItemSpan + mItemHeight;
            }

        }

        if (isFirstLayout) {
            isFirstLayout = false;
            addNumToRandomEmptyPosition(mRandomCount);
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (isGameOver) {
            return super.onTouchEvent(event);
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = (int) event.getX();
                startY = (int) event.getY();
                break;
            case MotionEvent.ACTION_UP:

                offsetX = (int) (event.getX() - startX);
                offsetY = (int) (event.getY() - startY);

                if (Math.abs(offsetX) > Math.abs(offsetY)) {
                    // moveToLeft or moveToRight

                    if (offsetX < -mThreshold) {
                        moveToLeft();
                    } else if (offsetX > mThreshold) {
                        moveToRight();
                    }

                } else {
                    // moveToTop or moveToBottom

                    if (offsetY < -mThreshold) {
                        moveToTop();
                    } else if (offsetY > mThreshold) {
                        moveToBottom();
                    }

                }

                break;

        }

        return super.onTouchEvent(event);
    }

    private void addNumToRandomEmptyPosition(int count) {

        for (int i = 0; i < count; i++) {

            emptyPoints.clear();

            for (int x = 0; x < mItemCount; x++) {
                for (int y = 0; y < mItemCount; y++) {
                    if (mGameMap[x][y].getNum() <= 0) {
                        emptyPoints.add(new Point(x, y));
                    }
                }
            }

            if (emptyPoints.size() > 0) {
                Point p = emptyPoints.remove((int) (Math.random() * emptyPoints.size()));

                mGameMap[p.x][p.y].setNum(Math.random() > 0.1 ? 1 : 2);

                performScaleAndAlphaAnimation(mGameMap[p.x][p.y]);

            }

        }

    }

    private void moveToLeft() {

        boolean merge = false;

        for (int y = 0; y < mItemCount; y++) {
            for (int x = 0; x < mItemCount; x++) {

                for (int i = x + 1; i < mItemCount; i++) {
                    if (mGameMap[i][y].getNum() > 0) {

                        if (mGameMap[x][y].getNum() <= 0) {

                            performTranslateAnimation(mGameMap[i][y].clone(), new Point(i, y), new Point(x, y));

                            mGameMap[x][y].setNum(mGameMap[i][y].getNum());
                            mGameMap[i][y].setNum(0);

                            x--;
                            merge = true;

                        } else if (mGameMap[x][y].equals(mGameMap[i][y])) {

                            performTranslateAnimation(mGameMap[i][y].clone(), new Point(i, y), new Point(x, y));

                            mGameMap[x][y].setNum(mGameMap[x][y].getNum() * 2);
                            mGameMap[i][y].setNum(0);

                            updateScore(mGameMap[x][y].getNum());

                            merge = true;

                        }

                        break;
                    }
                }
            }
        }

        if (merge) {
            mPlayer.start();
            addNumToRandomEmptyPosition(1);
            checkComplete();
        }

    }

    private void moveToRight() {

        boolean merge = false;

        for (int y = 0; y < mItemCount; y++) {
            for (int x = mItemCount - 1; x >= 0; x--) {

                for (int i = x - 1; i >= 0; i--) {

                    if (mGameMap[i][y].getNum() > 0) {

                        if (mGameMap[x][y].getNum() <= 0) {

                            performTranslateAnimation(mGameMap[i][y].clone(), new Point(i, y), new Point(x, y));

                            mGameMap[x][y].setNum(mGameMap[i][y].getNum());
                            mGameMap[i][y].setNum(0);

                            x++;
                            merge = true;

                        } else if (mGameMap[x][y].equals(mGameMap[i][y])) {

                            performTranslateAnimation(mGameMap[i][y].clone(), new Point(i, y), new Point(x, y));

                            mGameMap[x][y].setNum(mGameMap[x][y].getNum() * 2);
                            mGameMap[i][y].setNum(0);

                            updateScore(mGameMap[x][y].getNum());

                            merge = true;
                        }
                        break;
                    }
                }
            }
        }

        if (merge) {
            mPlayer.start();
            addNumToRandomEmptyPosition(1);
            checkComplete();
        }

    }

    private void moveToTop() {

        boolean merge = false;

        for (int x = 0; x < mItemCount; x++) {
            for (int y = 0; y < mItemCount; y++) {

                for (int i = y + 1; i < mItemCount; i++) {

                    if (mGameMap[x][i].getNum() > 0) {
                        if (mGameMap[x][y].getNum() <= 0) {

                            performTranslateAnimation(mGameMap[x][i].clone(), new Point(x, i), new Point(x, y));

                            mGameMap[x][y].setNum(mGameMap[x][i].getNum());
                            mGameMap[x][i].setNum(0);

                            y--;
                            merge = true;

                        } else if (mGameMap[x][y].equals(mGameMap[x][i])) {

                            performTranslateAnimation(mGameMap[x][i].clone(), new Point(x, i), new Point(x, y));

                            mGameMap[x][y].setNum(mGameMap[x][y].getNum() * 2);
                            mGameMap[x][i].setNum(0);

                            updateScore(mGameMap[x][y].getNum());

                            merge = true;

                        }
                        break;
                    }
                }
            }
        }

        if (merge) {
            mPlayer.start();
            addNumToRandomEmptyPosition(1);
            checkComplete();
        }

    }

    private void moveToBottom() {

        boolean merge = false;

        for (int x = 0; x < mItemCount; x++) {

            for (int y = mItemCount - 1; y >= 0; y--) {

                for (int i = y - 1; i >= 0; i--) {

                    if (mGameMap[x][i].getNum() > 0) {

                        if (mGameMap[x][y].getNum() <= 0) {

                            performTranslateAnimation(mGameMap[x][i].clone(), new Point(x, i), new Point(x, y));

                            mGameMap[x][y].setNum(mGameMap[x][i].getNum());
                            mGameMap[x][i].setNum(0);

                            y++;
                            merge = true;

                        } else if (mGameMap[x][y].equals(mGameMap[x][i])) {

                            performTranslateAnimation(mGameMap[x][i].clone(), new Point(x, i), new Point(x, y));

                            mGameMap[x][y].setNum(mGameMap[x][y].getNum() * 2);
                            mGameMap[x][i].setNum(0);

                            updateScore(mGameMap[x][y].getNum());

                            merge = true;

                        }

                        break;
                    }

                }

            }
        }

        if (merge) {
            mPlayer.start();
            addNumToRandomEmptyPosition(1);
            checkComplete();
        }
    }

    private void checkComplete() {

        boolean complete = true;

        all:
        for (int y = 0; y < mItemCount; y++) {
            for (int x = 0; x < mItemCount; x++) {

                if (mGameMap[x][y].getNum() == 0
                        || (x > 0 && mGameMap[x][y].equals(mGameMap[x - 1][y]))
                        || (x < mItemCount - 1 && mGameMap[x][y].equals(mGameMap[x + 1][y]))
                        || (y > 0 && mGameMap[x][y].equals(mGameMap[x][y - 1]))
                        || (y < mItemCount - 1 && mGameMap[x][y].equals(mGameMap[x][y + 1]))) {

                    complete = false;
                    break all;
                }
            }
        }

        if (complete) {
            isGameOver = true;
            if (onGameListener != null) {
                onGameListener.onGameOver(mTotalScore);
            }
        }

    }

    private void updateScore(int addScore) {

        mTotalScore += addScore;

        if (onGameListener != null) {
            onGameListener.onScoreUpdate(addScore, mTotalScore);
        }

    }

    public void reset() {
        initGameItem();
        isFirstLayout = true;
        isGameOver = false;
        mTotalScore = 0;
    }

    private void performScaleAndAlphaAnimation(View view) {

        if (mAnimationSet == null) {

            mAnimationSet = new AnimationSet(true);

            if (mScaleAnimation == null) {
                mScaleAnimation = new ScaleAnimation(0.1f, 1f, 0.1f, 1f,
                        Animation.RELATIVE_TO_SELF, 0.5f,
                        Animation.RELATIVE_TO_SELF, 0.5f);
            }

            if (mAlphaAnimation == null) {
                mAlphaAnimation = new AlphaAnimation(0f, 1f);
            }

            mAnimationSet.addAnimation(mScaleAnimation);
            mAnimationSet.addAnimation(mAlphaAnimation);
            mAnimationSet.setDuration(mDuration);
        }

        view.setAnimation(null);

        view.startAnimation(mAnimationSet);

    }

    private void performTranslateAnimation(GameItem item, Point from, Point to) {

        int startX = mItemSpan * (from.x + 1) + from.x * mItemWidth;
        int endX = mItemSpan * (to.x + 1) + to.x * mItemWidth;
        int startY = mItemSpan * (from.y + 1) + from.y * mItemWidth;
        int endY = mItemSpan * (to.y + 1) + to.y * mItemWidth;

        if (onGameListener != null) {
            onGameListener.onTranslation(item, startX, endX, startY, endY, mDuration);
        }

    }

    private OnGameListener onGameListener;

    public void setOnGameListener(OnGameListener onGameListener) {
        this.onGameListener = onGameListener;
    }

    public interface OnGameListener {

        void onScoreUpdate(int addScore, int totalScore);

        void onGameOver(int score);

        void onTranslation(GameItem item, int startX, int endX, int startY, int endY, int duration);

    }

}
