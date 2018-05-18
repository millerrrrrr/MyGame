package com.zhu.mygame.db;


import com.zhu.mygame.bean.ScoreBean;

import java.util.List;

public class ScoreDao {

    public static final String TABLE_NAME = "score";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_SCORE = "_score";
    public static final String COLUMN_SPEND_TIME = "_spendTime";
    public static final String COLUMN_GAME_TIME = "_gameTime";

    public void insert(int score, String spendTime) {
        DbManager.getInstance().insert(score, spendTime);
    }

    public List<ScoreBean> selectAll() {
        return DbManager.getInstance().selectAll();
    }

    public int getMaxScore() {
        return DbManager.getInstance().getMaxScore();
    }

}
