package inutask.isys.jp.demosubquery;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.test.mock.MockCursor;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.Collections;

import inutask.isys.jp.demosubquery.sqlhelper.ColumnsList;
import inutask.isys.jp.demosubquery.sqlhelper.ColumnsList.FeedEntry;
import inutask.isys.jp.demosubquery.sqlhelper.DBHelper;
import inutask.isys.jp.demosubquery.sqlhelper.SQLiteCode;
import inutask.isys.jp.demosubquery.sqlhelper.TableOperator;

/**
 * データベースのデータを一覧表示するActivity
 * チェックボックスの値によって、データの取得方法が変わる
 * TODO:
 * - 副次問合せのSQL文作成
 * - ラジオボタンにより処理の切り替え
 * - デバッグ
 */
public class DataListActivity extends ActionBarActivity implements OnClickListener {
    private Button btn_rtn;
    private RadioButton rb1, rb2, rb3;
    private RadioGroup rg;
    private ArrayAdapter<String> adapter;
    private static TableOperator operator;
    private final String PREF_INIT = "initial";
    private final String COMMA_SEP = ",";
    private ArrayList<Product> prod;

    enum Category {
        SUPER,
        REAL
    }


    // 初期データの設定
    private void initDatabaseData() {
        prod = new ArrayList<Product>();

        prod.add(new Product("機動戦艦ナデシコ", Category.REAL.toString(), 1996));
        prod.add(new Product("勇者王ガオガイガー", Category.SUPER.toString(), 1997));
        prod.add(new Product("SDガンダム外伝", Category.REAL.toString(), 1990));
        prod.add(new Product("聖戦士ダンバイン", Category.REAL.toString(), 1988));
        prod.add(new Product("巨神ゴーグ", Category.SUPER.toString(), 1984));
        prod.add(new Product("絶対無敵ライジンオー", Category.SUPER.toString(), 1991));
        prod.add(new Product("機動戦士ガンダムAGE", Category.REAL.toString(), 2012));


        for (Product p : prod) {
            insertData(p.getTitle(), p.getCategory(), p.getYear());
        }
    }

    private void insertData(String title, String category, int year) {
        String sql = "INSERT INTO " +
                FeedEntry.TABLE_NAME +
                "(" + FeedEntry.COLUMN_NAME_TITLE + COMMA_SEP +
                FeedEntry.COLUMN_NAME_CATEGORY + COMMA_SEP +
                FeedEntry.COLUMN_NAME_YEAR + ")" + "VALUES " +
                "(?,?,?)";
        Object[] bindArgs = new Object[]{title, category, year};
        operator.insert(sql, bindArgs);
    }

    private void setViewData(Cursor cur) {
        adapter.clear();
        cur.moveToFirst();
        adapter.add(cur.getString(0));
        while (cur.moveToNext()) {
            adapter.add(cur.getString(0));
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_list);

        operator = new TableOperator(this, FeedEntry.TABLE_NAME);
    }

    @Override
    public void onStart() {
        super.onStart();


        ListView listview = (ListView) findViewById(R.id.listView);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);

        SharedPreferences pref = getSharedPreferences("data", Context.MODE_PRIVATE);
        boolean init = pref.getBoolean(PREF_INIT, false);

        if (init == false) {
            initDatabaseData();
            SharedPreferences.Editor edit = pref.edit();
            edit.putBoolean(PREF_INIT, true);
        }




        String front = "select " + FeedEntry.COLUMN_NAME_TITLE + " from " + FeedEntry.TABLE_NAME + " where ";
        String main = FeedEntry.COLUMN_NAME_CATEGORY + " = (";
        String sub = "select " + FeedEntry.COLUMN_NAME_CATEGORY + " from " + FeedEntry.TABLE_NAME + " where " + FeedEntry.COLUMN_NAME_YEAR + " < 2000 )";

        String all = "select * from " + FeedEntry.TABLE_NAME;

        String[] columns = {"title", "category", "year"};

        testDatabase();

        // DBから全件取得 + アダプターへ追加
        //SQLiteCode sql = new SQLiteCode(FeedEntry.TABLE_NAME, columns, null, null, null, null, null, false);
        SQLiteCode sql = new SQLiteCode(FeedEntry.TABLE_NAME, new String[]{"title"}, null, null, null, null, null, false);

        //setViewData(operator.search(new String[]{"*"},main+sub,null,null,null,null,null));

        Cursor cc = operator.search(sql);
        //setViewData(operator.search(sql));
        setViewData(cc);

        listview.setAdapter(adapter);
    }

    void testDatabase() {
        Tests test = new Tests(this);

        // --------- DB HELPER -------------------------------
        Log.d("Test helper " + Tests.TestHelper.NULL, "" + test.testDBHelper(Tests.TestHelper.NULL, null));
        Log.d("Test helper " + Tests.TestHelper.NAME, "" + test.testDBHelper(Tests.TestHelper.NAME, FeedEntry.TABLE_NAME));
        Log.d("Test helper " + Tests.TestHelper.HELPER, "" + test.testDBHelper(Tests.TestHelper.HELPER, new DBHelper(this)));

        // -------- DB SEARCH --------------------------------
        SQLiteCode code = new SQLiteCode(operator.getTableName(), new String[]{FeedEntry.COLUMN_NAME_TITLE},null,null,null,null,null,false);
        Log.d("Test search " + Tests.TestSearch.NULL, "" + test.testSearch(Tests.TestSearch.NULL, code,test.datacount ));

        Log.d("Test search " + Tests.TestSearch.ALL, "" + test.testSearch(Tests.TestSearch.ALL, null, test.datacount));

        SQLiteCode code2 = new SQLiteCode(operator.getTableName(), new String[]{FeedEntry.COLUMN_NAME_YEAR},FeedEntry.COLUMN_NAME_YEAR + " < 2000",null,null,null,null,false);
        code = new SQLiteCode(operator.getTableName(), new String[]{FeedEntry.COLUMN_NAME_TITLE}, FeedEntry.COLUMN_NAME_YEAR,"=" ,code2,null,null,null,null,false);
        Log.d("Test search " + Tests.TestSearch.SUB, "" + test.testSearch(Tests.TestSearch.SUB, code, 6));
        //Log.d("Test search " + Tests.TestSearch.PLACE, "" + test.testSearch(Tests.TestSearch.PLACE, null, 0));


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_data_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if (v == btn_rtn) {
            operator.close();
            Intent intent = new Intent();
            intent.setClassName("inutask.isys.jp.demosubquery", "inutask.isys.jp.demosubquery.MainActivity");
            startActivity(intent);
        }
    }


}

class Tests {
    TableOperator operator;
    Context con;
    public int datacount = 7;

    public Tests(Context con) {
        this.con = con;
    }

    enum TestSearch {
        NULL,
        ALL,
        SUB,
        PLACE
    }

    enum TestHelper {
        NULL,
        NAME,
        HELPER
    }

    enum TestDel {
        NULL

    }

    enum TestIns {
        NULL
    }

    enum TestUpd {
        NULL
    }

    boolean testDBHelper(TestHelper h, Object obj) {
        TableOperator o = null;
        switch (h) {
            case NULL:
                o = new TableOperator(con);
                if (o.getTableName().equals(FeedEntry.TABLE_NAME)) {
                    operator = o;
                    return true;
                } else {
                    return false;
                }
            case NAME:
                o = new TableOperator(con, obj.toString());
                if (obj.toString().equals(o.getTableName())) {
                    operator = o;
                    return true;
                } else {
                    return false;
                }
            case HELPER:
                if (obj instanceof DBHelper == false) return false;
                o = new TableOperator((DBHelper) obj);
                if (((DBHelper) obj).getDatabaseName().equals(o.getDBName())) {
                    operator = o;
                    return true;
                } else {
                    return false;
                }
        }
        return false;

    }

    boolean testSearch(TestSearch s, SQLiteCode testCode, int ans) {
        Cursor c = null;
        switch (s) {
            case ALL:
                c = operator.search();
                if (ans == c.getCount()) {
                    return true;
                } else {
                    return false;
                }

            case NULL:
                c = operator.search(testCode);
                if (ans == c.getCount()) {
                    return true;
                } else {
                    return false;
                }

            case PLACE:
                c = operator.search(testCode);
                if (ans == c.getCount()) {
                    return true;
                } else {
                    return false;
                }
            case SUB:
                c = operator.search(testCode);
                Log.d("Test search sub count",""+c.getCount());
                c.moveToFirst();
                Log.d("Test search sub data", "" + c.getString(0));
                while(c.moveToNext()) {
                    Log.d("Test search sub data", "" + c.getString(0));
                }


                if (ans == c.getCount()) {
                    return true;
                } else {
                    return false;
                }

        }

        return false;

    }

    boolean testDelete(TestDel d) {
        return false;

    }

    boolean testInsert(TestIns i) {
        return false;
    }

    boolean testUpdate(TestUpd u) {
        return false;
    }
}
