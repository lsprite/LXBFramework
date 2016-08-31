package com.lxb.util;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class MySQLiteHelper extends SQLiteOpenHelper {
    // 数据库版本
    public static final int VERSION = 1;

    // 调用父类构造器
    public MySQLiteHelper(Context context, String name, CursorFactory factory,
                          int version) {
        super(context, name, factory, version);
    }

    /**
     * 当数据库首次创建时执行该方法，一般将创建表等初始化操作放在该方法中执行. 重写onCreate方法，调用execSQL方法创建表
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table if not exists cache("
                + "idkey varchar primary key," + "json text)");
//        db.execSQL("create table if not exists appversion("
//                + "appversion varchar primary key," + "version varchar)");
    }

    // 当打开数据库时传入的版本号与当前的版本号不同时会调用该方法
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        System.out.println("=========onUpgrade:" + oldVersion + "," + newVersion);
    }

    public void insertCache(String idkey, String jsonstr) {
        // if (deleteArticle(idkey, jsonstr)) {
        SQLiteDatabase db = null;
        try {
            db = getWritableDatabase();
            // String sqlstr = String
            // .format("if exists (select * from cache where idkey='%s') delete from cache where idkey = '%s'",
            // idkey, idkey);
            String sqlstr = String.format(
                    "replace into cache (idkey, json) values ('%s', '%s')",
                    idkey, jsonstr);
            // String sql = String
            // .format("IF NOT EXISTS(SELECT * FROM cache WHERE idkey='%s'.) THEN INSERT INTO cache (idkey, json) values ('%s', '%s') ELSE UPDATE cache SET  json='%s' values where idkey='%s'",
            // idkey, idkey, jsonstr, jsonstr, idkey);
            db.execSQL(sqlstr);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }
        // }
    }

    public void deleteCache(String idkey) {
        // if (deleteArticle(idkey, jsonstr)) {
        System.out.println("删除本地缓存");
        SQLiteDatabase db = null;
        try {
            db = getWritableDatabase();
            String sqlstr = String.format("delete from cache where idkey='%s'",
                    idkey);
            db.execSQL(sqlstr);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }
        // }
    }

    //清空cache表
    public void deleteCache() {
        // if (deleteArticle(idkey, jsonstr)) {
        System.out.println("删除本地缓存");
        SQLiteDatabase db = null;
        try {
            db = getWritableDatabase();
            String sqlstr = "delete from cache";
            db.execSQL(sqlstr);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }
        // }
    }

    public String queryCache(String idkey) {
        String result = "";
        Cursor cursor = null;
        SQLiteDatabase db = null;
        try {
            db = getReadableDatabase();
            String sqlstr = String.format(
                    "SELECT * from cache where idkey = '%s'", idkey);
            cursor = db.rawQuery(sqlstr, null);
            while (cursor.moveToNext()) {
                result = cursor.getString(cursor.getColumnIndex("json"));
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        } finally {
            try {
                if (cursor != null) {
                    cursor.close();
                }
                if (db != null) {
                    db.close();
                }
            } catch (Exception e2) {
                // TODO: handle exception
            }
            return result;
        }
    }

}
