package inutask.isys.jp.demodatabase;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import inutask.isys.jp.demodatabase.sqlhelper.SqlOperator;

/**
 * データベースのデータを一覧表示するActivity
 */
public class DataListActivity extends ActionBarActivity implements OnClickListener{
    Button btn_rtn;
    private static SqlOperator operator;
    private Cursor cursor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_list);

        operator = new SqlOperator(this);

    }

    @Override
    public void onStart(){
        super.onStart();
        btn_rtn = (Button)findViewById(R.id.button5);
        btn_rtn.setOnClickListener(this);

        ListView listview = (ListView)findViewById(R.id.listView);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        adapter.clear();

        // DBから全件取得 + アダプターへ追加
        cursor = operator.searchAllDB();
        cursor.moveToFirst();
        while(cursor.moveToNext()){
            adapter.add(cursor.getString(1));
        }

        listview.setAdapter(adapter);
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
        if(v == btn_rtn){
            cursor.close();
            operator.close();
            Intent intent = new Intent();
            intent.setClassName("inutask.isys.jp.demodatabase","inutask.isys.jp.demodatabase.MainActivity");
            startActivity(intent);
        }
    }
}
