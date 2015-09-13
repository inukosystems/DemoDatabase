package inutask.isys.jp.demosubquery.sqlhelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Yellow on 2015/05/24.
 *
 * 概要
 * Sqliteのテーブルの操作を行うクラス
 * 追加・更新・削除・一覧の処理であれば、このクラスさえインスタンスすれば、他のSQLiteクラスは触らなくて良い
 * 対応している操作は以下の通り
 * - 追加・更新・削除・取得
 * - 取得については、副次、比較、外部結合に対応
 *
 * テーブルやデータベースの生成など、設定変更系の操作には対応しない
 *
 *
 * TODO:
 *  - メソッドが多すぎるので、まとめてパフォーマンスを上げる
 *  - searchメソッドのテスト
 */
public class TableOperator {
    private static SQLiteDatabase db;
    private static SQLiteOpenHelper dbHelper;
    private String tableName;

    /* -------------------------------------
      コンストラクタ
    ------------------------------------- */

    public TableOperator(Context context) {
        dbHelper = new DBHelper(context);
        tableName = ColumnsList.FeedEntry.TABLE_NAME;
    }
    public TableOperator(Context context, String tableName){
        dbHelper = new DBHelper(context);
        this.tableName = tableName;
    }
    public TableOperator(DBHelper helper){
        dbHelper = helper;
        tableName = ColumnsList.FeedEntry.TABLE_NAME;
    }

    /* -------------------------------------
      取得メソッド
    ------------------------------------- */

    public Cursor search(SQLiteCode code){
        return search(code, null);
    }

    // プレースホルダ使用
    public Cursor search(SQLiteCode code,String[] selectionArgs) {
        if(code.isSubquery()) {
            return search(code.getColumns(), code.getWhere(), code.getOperator(),code.getSubquery(),selectionArgs, code.getGroupBy(), code.getHaving(), code.getOrderBy(), code.getLimit());
        }else{
            return search(code.getColumns(), code.getWhere(), selectionArgs, code.getGroupBy(), code.getHaving(), code.getOrderBy(), code.getLimit());
        }
    }


    /** サブクエリ対応
     *
     * @param columns
     * @param where
     * @param operator サブクエリを連結させる際の計算演算子
     * @param subquery サブクエリ用のSQL文　通例どおり、単体で完結している必要がある
     * @param group
     * @param having
     * @param orderby
     * @param limit
     * @return
     */
    public Cursor search(String[] columns, String where,String operator, SQLiteCode subquery,String [] selectionArgs,String group, String having, String orderby, String limit){
        return search(columns,where+" "+operator+" ( "+ subquery.toString() + " ) ",selectionArgs,group,having,orderby,limit);
    }

    /**
     *
     * @param columns 検索するカラム名
     * @param selection 検索条件　WHERE句に該当 WHERE句の"WHERE"の文字を抜いたWHERE句を指定可能　例) WHERE A.name = "taro" => "A.name = ?" (selectionArgsで置換)
     * @param selectionArgs プレースホルダ　selectionに書かれた?と置換する情報を配列として受け取る事ができる(C言語のprintf関数に近い) 置換された文字はString型として扱われる
     * @param group 列をグループ化して検索する
     * @param having グループ化したデータを絞り込む
     * @param orderby 並べ替え テーブル名.列名 ASC or DESC
     * @param limit 取得数
     * @return 取得したデータを持つCurosrクラスのインスタンス
     */
    public Cursor search(String[] columns, String selection, String[] selectionArgs, String group, String having, String orderby, String limit) {
        db = dbHelper.getReadableDatabase();

        Cursor cur = db.query(tableName,
                columns,
                selection,
                selectionArgs,
                group,
                having,
                orderby,
                limit
        );

        return cur;
    }


    // 全件取得
    public Cursor search() {
        db = dbHelper.getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT * FROM " + tableName, null);
        return cur;

    }


    /* -------------------------------------
      追加メソッド
    ------------------------------------- */

    public long insert(ContentValues values) {
        db = dbHelper.getWritableDatabase();
        long l = db.insert(
                ColumnsList.FeedEntry.TABLE_NAME,
                ColumnsList.FeedEntry.COLUMN_NAME_NULLABLE,
                values
        );

        db.close();
        return l;
    }

    public void insert(String sql,Object[] bindArgs){
        db = dbHelper.getWritableDatabase();
        db.execSQL(sql,bindArgs);
    }

    /* -------------------------------------
      削除メソッド
    ------------------------------------- */

    public int delete(String selection, String[] selectionArgs) {
        db = dbHelper.getWritableDatabase();
        int r = db.delete(ColumnsList.FeedEntry.TABLE_NAME, selection, selectionArgs);
        db.close();
        return r;
    }

    /* -------------------------------------
      更新メソッド
    ------------------------------------- */

    public int update(ContentValues values, String selection, String[] selectionArgs) {
        db = dbHelper.getReadableDatabase();
        int r = db.update(ColumnsList.FeedEntry.TABLE_NAME, values, selection, selectionArgs);
        db.close();
        return r;
    }


    /* -------------------------------------
      Getter/Setter/Closeメソッド
    ------------------------------------- */

    public void close() {
        if (db != null) {
            db.close();
        }

    }

    public String getTableName() {
        return tableName;
    }

    public String getDBName(){
        return dbHelper.getDatabaseName();
    }
}
