package com.zhu.mygame.db;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.zhu.mygame.bean.ScoreBean;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DbManager {

    private static class Holder {
        private static final DbManager INSTANCE = new DbManager();
    }

    private DbManager() {

    }

    public static DbManager getInstance() {
        return Holder.INSTANCE;
    }

    public void insert(int score, String spendTime) {

        String sql = "INSERT INTO "
                + ScoreDao.TABLE_NAME + "(" + ScoreDao.COLUMN_SCORE + ", " + ScoreDao.COLUMN_SPEND_TIME + ", " + ScoreDao.COLUMN_GAME_TIME + ") " +
                "VALUES(" + score + ", '" + spendTime + "', '" + getCurrentTime() + "')";

        SQLiteDatabase db = DbOpenHelper.getInstance().getWritableDatabase();
        if (db.isOpen()) {
            db.execSQL(sql);
        }

    }

    public List<ScoreBean> selectAll() {

        List<ScoreBean> list = new ArrayList<>();

        String sql = "SELECT * FROM " + ScoreDao.TABLE_NAME;
        SQLiteDatabase db = DbOpenHelper.getInstance().getReadableDatabase();
        if (db.isOpen()) {
            Cursor cursor = db.rawQuery(sql, null);
            if (cursor != null) {
                int index = -1;
                while (cursor.moveToNext()) {
                    ScoreBean bean = new ScoreBean();
                    if ((index = cursor.getColumnIndex(ScoreDao.COLUMN_ID)) != -1) {
                        bean.setId(cursor.getInt(index));
                    }

                    if ((index = cursor.getColumnIndex(ScoreDao.COLUMN_SCORE)) != -1) {
                        bean.setScore(cursor.getInt(index));
                    }

                    if ((index = cursor.getColumnIndex(ScoreDao.COLUMN_SPEND_TIME)) != -1) {
                        bean.setSpendTime(cursor.getString(index));
                    }

                    if ((index = cursor.getColumnIndex(ScoreDao.COLUMN_GAME_TIME)) != -1) {
                        bean.setGameTime(cursor.getString(index));
                    }

                    list.add(bean);
                }
                cursor.close();

            }
        }

        return list;
    }

    public int getMaxScore() {
        String sql = "SELECT MAX(" + ScoreDao.COLUMN_SCORE + ") FROM " + ScoreDao.TABLE_NAME;
        SQLiteDatabase db = DbOpenHelper.getInstance().getReadableDatabase();
        int result = 0;
        if (db.isOpen()) {
            Cursor cursor = db.rawQuery(sql, null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    result = cursor.getInt(0);
                }
            }
        }
        return result;
    }

    private String getCurrentTime() {
        Date date = new Date();
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
    }

}
