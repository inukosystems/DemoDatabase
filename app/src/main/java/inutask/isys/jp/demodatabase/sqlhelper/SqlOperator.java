package inutask.isys.jp.demodatabase.sqlhelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Yellow on 2015/05/24.
 *
 * 概要
 * Sqliteのデータベースの生成と操作を行うクラス
 * 追加・更新・削除・一覧の処理であれば、このクラスさえインスタンスすれば、他のSQLiteクラスは触らなくて良い
 *
 * TODO:
 *  -SQLInjection対策
 */
public class SqlOperator {
    private static SQLiteDatabase db;
    private static SQLiteOpenHelper dbHelper;
    private static final String SEARCH_ALL = "SELECT * FROM " + FeedReaderContract.FeedEntry.TABLE_NAME;

    public SqlOperator(Context context) {
        dbHelper = new FeedReaderDbHelper(context);
    }

    public Cursor searchDB(String[] projection, String selection, String[] selectionArgs, String group, String having, String orderby, String limit) {
        db = dbHelper.getReadableDatabase();

        Cursor cur = db.query(FeedReaderContract.FeedEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                group,
                having,
                orderby,
                limit
        );

        return cur;
    }

    public Cursor searchAllDB() {
        db = dbHelper.getReadableDatabase();
        Cursor cur = db.rawQuery(SEARCH_ALL, null);
        return cur;

    }

    public long insertDB(ContentValues values) {
        db = dbHelper.getWritableDatabase();
        long l = db.insert(
                FeedReaderContract.FeedEntry.TABLE_NAME,
                FeedReaderContract.FeedEntry.COLUMN_NAME_NULLABLE,
                values
        );

        db.close();
        return l;
    }

    public int deleteDB(String selection, String[] selectionArgs) {
        db = dbHelper.getWritableDatabase();
        int r = db.delete(FeedReaderContract.FeedEntry.TABLE_NAME, selection, selectionArgs);
        db.close();
        return r;
    }

    public int updateDB(ContentValues values, String selection, String[] selectionArgs) {
        db = dbHelper.getReadableDatabase();
        int r = db.update(FeedReaderContract.FeedEntry.TABLE_NAME, values, selection, selectionArgs);
        db.close();
        return r;
    }

    public void close() {
        if (db != null) {
            db.close();
        }

    }
}
