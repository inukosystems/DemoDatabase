package inutask.isys.jp.demodatabase;


/**
 * TODO:
 * 　SQLiteの副問合せを実行する
 * 　queryメソッドだと、副次問合せ用のパラメータが準備されていない
 * 　直接selectionに記載する
 */


import android.content.ContentValues;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import inutask.isys.jp.demodatabase.sqlhelper.FeedReaderContract;
import inutask.isys.jp.demodatabase.sqlhelper.SqlOperator;


public class MainActivity extends ActionBarActivity implements OnClickListener {
    private static SqlOperator operator;
    Button btn_insert,btn_edit,btn_delete,btn_get;
    EditText text1,text2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        operator = new SqlOperator(this);
    }

    @Override
    public void onStart(){
        super.onStart();
        btn_insert = (Button) findViewById(R.id.button1);
        btn_edit = (Button) findViewById(R.id.button2);
        btn_delete = (Button) findViewById(R.id.button3);
        btn_get = (Button) findViewById(R.id.button4);


        btn_insert.setOnClickListener(this);
        btn_edit.setOnClickListener(this);
        btn_delete.setOnClickListener(this);
        btn_get.setOnClickListener(this);

        text1 = (EditText) findViewById(R.id.editText);
        text2 = (EditText) findViewById(R.id.editText2);

    }


    public void onClick(View v){

        // 追加処理
        if(v == btn_insert){

            CharSequence text = text1.getText();

            ContentValues values = new ContentValues();
            values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE,text.toString());

            long result = operator.insertDB(values);
            if(result >= 1){
                Toast.makeText(this, "追加成功", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(this, "追加失敗", Toast.LENGTH_LONG).show();
            }

        }

        // 更新処理
        if(v == btn_edit){

            CharSequence origin = text1.getText();
            CharSequence edited =  text2.getText();

            ContentValues values = new ContentValues();
            values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE, edited.toString());

            String selection = FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE +"='"+origin.toString()+"'";

            int result = operator.updateDB(values, selection, null);
            if(result >= 1){
                Toast.makeText(this, "更新成功", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(this, "更新失敗", Toast.LENGTH_LONG).show();
            }

        }

        //削除処理
        if(v == btn_delete){

            CharSequence text = text1.getText();

            String selection = FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE +"='"+text.toString()+"'";
            int result = operator.deleteDB(selection, null);

            if(result >= 1){
                Toast.makeText(this, "削除成功", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(this, "削除失敗", Toast.LENGTH_LONG).show();
            }

        }

        if(v == btn_get){
            // 画面遷移
            Intent intent = new Intent();
            intent.setClassName("inutask.isys.jp.demodatabase","inutask.isys.jp.demodatabase.DataListActivity");
            startActivity(intent);
        }
    }




    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();


        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
