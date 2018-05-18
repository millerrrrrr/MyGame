package com.zhu.mygame.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.zhu.mygame.App;


public class DbOpenHelper extends SQLiteOpenHelper {

    private static class Holder {
        private static final DbOpenHelper INSTANCE = new DbOpenHelper();
    }

    private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + ScoreDao.TABLE_NAME + "("
            + ScoreDao.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + ScoreDao.COLUMN_SCORE + " INTEGER NOT NULL, "
            + ScoreDao.COLUMN_SPEND_TIME + " TEXT, "
            + ScoreDao.COLUMN_GAME_TIME + " TEXT)";

    private static int dbVersion = 1;
    private static String dbName = "zhu.db";

    private DbOpenHelper() {
        this(App.mContext, dbName, null, dbVersion);
    }

    private DbOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public static DbOpenHelper getInstance() {
        return Holder.INSTANCE;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
