package com.websarva.wings.android.calender;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class TextActivity extends AppCompatActivity implements View.OnClickListener {
    private int id = 0;
    private EditText editTitle = null;
    private EditText editContents = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text);

        // ビューオブジェクトを取得
        editTitle = findViewById(R.id.editTitle);
        editContents = findViewById(R.id.editContents);

        // インテントを取得
        Intent intent = getIntent();

        //intentのデータを取得(データがない場合、第２引数の 0 が返る)
        id = intent.getIntExtra(DBContract.DBEntry._ID,0);
        String title = intent.getStringExtra(DBContract.DBEntry.COLUMN_NAME_TITLE);
        String contents = intent.getStringExtra(DBContract.DBEntry.COLUMN_NAME_CONTENTS);

        // データ更新の場合
        if (id > 0){
            editTitle.setText(title);
            editContents.setText(contents);
        }
    }

    // 「登録」ボタン　タップ時に呼び出されるメソッド
    public void btnReg_onClick(View view) {

        // ヘルパーを準備
        DatabaseHelper helper = new DatabaseHelper(this);

        // 入力欄に入力されたタイトルとコンテンツを取得
        String title    = editTitle.getText().toString();
        String contents = editContents.getText().toString();

        // 書き込みモードでデータベースをオープン
        try (SQLiteDatabase db = helper.getWritableDatabase()) {

            // 入力されたタイトルとコンテンツをContentValuesに設定
            // ContentValuesは、項目名と値をセットで保存できるオブジェクト
            ContentValues cv = new ContentValues();
            cv.put(DBContract.DBEntry.COLUMN_NAME_TITLE, title);
            cv.put(DBContract.DBEntry.COLUMN_NAME_CONTENTS, contents);

            if(id == 0) {
                // データ新規登録
                db.insert(DBContract.DBEntry.TABLE_NAME, null, cv);
            } else {
                // データ更新
                db.update(DBContract.DBEntry.TABLE_NAME, cv, DBContract.DBEntry._ID + " = ?", new String[] {String.valueOf(id)});
            }
        }

        // TextActivityを終了
        finish();
    }

    // 「キャンセル」ボタン　タップ時に呼び出されるメソッド
    public void btnCancel_onClick(View view) {

        // TextActivityを終了
        finish();
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(TextActivity.this, GoogleMap.class);
        startActivity(intent);
    }

}