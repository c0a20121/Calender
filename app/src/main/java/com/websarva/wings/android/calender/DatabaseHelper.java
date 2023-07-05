package com.websarva.wings.android.calender;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import static com.websarva.wings.android.calender.DBContract.DBEntry;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "cocktailmemo.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        //db.execSQL("DROP TABLE IF EXISTS " + DBEntry.TABLE_NAME);
        //onCreate(db);
        //StringBuilder sb = new StringBuilder();
        //sb.append("CREATE TABLE" +DBEntry.TABLE_NAME+"(");
        //sb.append(DBEntry._ID + "INTEGER PRIMARY KEY");
        //sb.append(DBEntry.CALENDER_YEAR + "INTEGER");
        //sb.append(DBEntry.CALENDER_MONTH + "INTEGER");
        //sb.append(DBEntry.CALENDER_DAY + "INTEGER");
        //sb.append(DBEntry.COLUMN_NAME_TITLE + "TEXT");
        //sb.append(DBEntry.COLUMN_NAME_CONTENTS + "TEXT");
        //sb.append(DBEntry.COLUMN_NAME_UPDATE + " INTEGER DEFAULT (datetime(CURRENT_TIMESTAMP,'localtime'))) ");
        //sb.append(");");
        //String sql = sb.toString();
        db.execSQL(
                "CREATE TABLE "+ DBEntry.TABLE_NAME + " (" +
                        DBEntry._ID + " INTEGER PRIMARY KEY, " +
                        DBEntry.CALENDER_YEAR + " INTEGER , " +
                        DBEntry.CALENDER_MONTH + " INTEGER , " +
                        DBEntry.CALENDER_DAY + " INTEGER , " +
                        DBEntry.COLUMN_NAME_TITLE + " TEXT default 'タイトル', " +
                        DBEntry.COLUMN_NAME_CONTENTS + " TEXT default '',"+
                        DBEntry.COLUMN_NAME_UPDATE + " INTEGER DEFAULT (datetime(CURRENT_TIMESTAMP,'localtime'))) ");


        //db.execSQL(
        //       "CREATE TRIGGER trigger_samp_tbl_update AFTER UPDATE ON " + DBEntry.TABLE_NAME +
        //              " BEGIN "+
        //            " UPDATE " + DBEntry.TABLE_NAME + " SET up_date = DATETIME('now', 'localtime') WHERE rowid == NEW.rowid; "+
        //          " END;");

        int year = 2022;
        int month = 12;
        int day = 1;
        ContentValues cv = new ContentValues();
        cv.put(DBEntry.CALENDER_YEAR, year);
        cv.put(DBEntry.CALENDER_MONTH, month);
        cv.put(DBEntry.CALENDER_DAY,day);
        cv.put(DBEntry.COLUMN_NAME_TITLE,"あめんぼ");
        db.insert(DBEntry.TABLE_NAME, null, cv);
        //  db.execSQL(
        //        "CREATE TABLE "+ DBEntry.TABLE_NAME + " (" +
        //              DBEntry._ID + " INTEGER PRIMARY KEY, " +
        //            DBEntry.COLUMN_NAME_TITLE + " TEXT default 'タイトル', " +
        //          DBEntry.COLUMN_NAME_CONTENTS + " TEXT default '', " +
        //        DBEntry.COLUMN_NAME_UPDATE + " INTEGER DEFAULT (datetime(CURRENT_TIMESTAMP,'localtime'))) ");

        // トリガーを作成
        db.execSQL(
                "CREATE TRIGGER trigger_samp_tbl_update AFTER UPDATE ON " + DBEntry.TABLE_NAME +
                        " BEGIN "+
                        " UPDATE " + DBEntry.TABLE_NAME + " SET up_date = DATETIME('now', 'localtime') WHERE rowid == NEW.rowid; "+
                        " END;");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1){
        db.execSQL("DROP TABLE IF EXISTS " + DBEntry.TABLE_NAME);
        onCreate(db);
    }
}