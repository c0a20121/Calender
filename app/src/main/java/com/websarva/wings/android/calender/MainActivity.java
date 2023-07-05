package com.websarva.wings.android.calender;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;
import android.widget.AdapterView;
import android.content.Intent;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import static com.websarva.wings.android.calender.DBContract.DBEntry;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {
    // private int _cocktailId = -1;
    //private String _cocktailName ="cocktailmemos";
    private DatabaseHelper _helper = null;
    MainListAdapter sc_adapter;
    private int p_year;
    private int p_month;
    private int p_day;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // CalendarViewにリスナーを設定
        ((CalendarView) findViewById(R.id.calendarView)).setOnDateChangeListener(listener);
        Calendar cal = Calendar.getInstance();
        p_year = cal.get(Calendar.YEAR);
        p_month = cal.get(Calendar.MONTH)+1;
        p_day = cal.get(Calendar.DAY_OF_MONTH);
        onShow(p_year, p_month, p_day);
    }

    @Override
    protected void onResume() {
        super.onResume();

        //データを一覧表示
        onShow(p_year,p_month,p_day);
    }

    CalendarView.OnDateChangeListener listener = new CalendarView.OnDateChangeListener() {

        /**
         * 日付部分タップ時に実行される処理
         * @param view 押下されたカレンダーのインスタンス
         * @param year タップされた日付の「年」
         * @param month タップされた日付の「月」※月は0月から始まるから、+1して調整が必要
         * @param dayOfMonth タップされた日付の「日」
         *
         */
        @Override
        public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
            // とりあえずトースト表示してみる
            // Toast.makeText(getApplicationContext(), year + "年" + (month + 1) + "月" + dayOfMonth + "日がクリックされました", Toast.LENGTH_SHORT).show();
            p_year = year;
            p_month = month+1;
            p_day = dayOfMonth;
            onShow(year, month + 1, dayOfMonth);
            // TODO:
            // 引数の year, month, dayOfMonthを利用して、DBから予定表を取得
            // ListViewに予定表リストを突っ込む


        }
    };

    protected void onShow(int year, int month, int day) {

        // データベースヘルパーを準備
        _helper = new DatabaseHelper(this);

        // データベースを検索する項目を定義
        //String[] cols = {DBEntry._ID, DBEntry.CALENDER_YEAR, DBEntry.CALENDER_MONTH, DBEntry.CALENDER_DAY};
        String[] cols = {DBEntry._ID, DBEntry.COLUMN_NAME_TITLE, DBEntry.COLUMN_NAME_CONTENTS};

        // 読み込みモードでデータベースをオープン
        try (SQLiteDatabase db = _helper.getReadableDatabase()) {

            // データベースを検索
            Cursor cursor = db.query(DBEntry.TABLE_NAME, cols, DBEntry.CALENDER_YEAR + "=" + year + " and " + DBEntry.CALENDER_MONTH + "=" + month + " and " + DBEntry.CALENDER_DAY + "=" + day,
                    null, null, null, null, null);

            // 検索結果から取得する項目を定義
            String[] from = {DBEntry.COLUMN_NAME_TITLE};

            // データを設定するレイアウトのフィールドを定義
            int[] to = {R.id.title};

            // ListViewの1行分のレイアウト(row_main.xml)と検索結果を関連付け
            sc_adapter = new MainListAdapter(
                    this, R.layout.row_main, cursor, from, to, 0);

            // activity_main.xmlに定義したListViewオブジェクトを取得
            ListView list = findViewById(R.id.mainlist);

            // ListViewにアダプターを設定
            list.setAdapter(sc_adapter);

            // リストの項目をクリックしたときの処理
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView av, View view, int position, long id) {

                    //　クリックされた行のデータを取得
                    Cursor cursor = (Cursor) av.getItemAtPosition(position);

                    // テキスト登録画面 Activity へのインテントを作成
                    Intent intent = new Intent(MainActivity.this, com.websarva.wings.android.calender.TextActivity.class);

                    intent.putExtra(DBEntry._ID, cursor.getInt(0));
                    intent.putExtra(DBEntry.COLUMN_NAME_TITLE, cursor.getString(1));
                    intent.putExtra(DBEntry.COLUMN_NAME_CONTENTS, cursor.getString(2));

                    // アクティビティを起動
                    startActivity(intent);
                }
            });
        }
    }

    public void btnDel_onClick(View view) {

        // MainListAdapterで設定されたリスト内の位置を取得
        int pos = (Integer) view.getTag();

        // アダプターから、_idの値を取得
        int id = ((Cursor) sc_adapter.getItem(pos)).getInt(0);

        // データを削除
        try (SQLiteDatabase db = _helper.getWritableDatabase()) {
            db.delete(DBEntry.TABLE_NAME, DBEntry._ID + " = ?", new String[]{String.valueOf(id)});
        }

        // データを一覧表示
        onShow(p_year, p_month, p_day);
    }

    public void fab_reg_onClick(View view) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        // 表示させる文字列
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "音声を入力");
        // インテント開始
        try {
            startActivityForResult(intent, 1234);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
            Snackbar.make(view, "音声認識でエラーがおきました", Snackbar.LENGTH_SHORT).show();
        }


        // テキスト登録画面 Activity へのインテントを作成
        //  Intent intent  = new Intent(MainActivity.this, com.websarva.wings.android.samplistview.TextActivity.class);

        // アクティビティを起動
        //startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1234 && resultCode == RESULT_OK) {
// 全ての結果を配列に受け取る
            ArrayList<String> speechToChar = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);
            String spokenString = "";
// ここでは、認識結果が複数あった場合1つに結合しています
            // for (int i = 0; i < speechToChar.size(); i++) {
            //   spokenString += speechToChar.get(i);
            //}
            spokenString = speechToChar.get(0);
            if (speechToChar.size() > 0) {
                int id = 0;
                Intent intent = getIntent();
                id = intent.getIntExtra(DBEntry._ID,0);
                try (SQLiteDatabase db = _helper.getWritableDatabase()) {
                    ContentValues cv = new ContentValues();
                    cv.put(DBEntry.CALENDER_YEAR, p_year);
                    cv.put(DBEntry.CALENDER_MONTH, p_month);
                    cv.put(DBEntry.CALENDER_DAY, p_day);
                    cv.put(DBEntry.COLUMN_NAME_TITLE, spokenString);
                    //db.insert(DBEntry.TABLE_NAME, null, cv);
                    if(id == 0) {
                        // データ新規登録
                        db.insert(DBEntry.TABLE_NAME, null, cv);
                    } else {
                        // データ更新
                        db.update(DBEntry.TABLE_NAME, cv, DBEntry._ID + " = ?", new String[] {String.valueOf(id)});
                    }

                    // 入力されたタイトルとコンテンツをContentValuesに設定
                    // ContentValuesは、項目名と値をセットで保存できるオブジェクト
                    // onShow(p_year,p_month,p_day);

                }
                // TextActivityを終了


                //finish();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}