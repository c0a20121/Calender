package com.websarva.wings.android.calender;

import android.provider.BaseColumns;

// データベースのテーブル名・項目名を定義
public final class DBContract {

    // 誤ってインスタンス化しないようにコンストラクタをプライベート宣言
    private DBContract() {}

    // テーブルの内容を定義
    public static class DBEntry implements BaseColumns {
        // BaseColumns インターフェースを実装することで、内部クラスは_IDを継承できる
        public static final String TABLE_NAME           = "calender_tbl";
        public static final String CALENDER_YEAR        = "year";
        public static final String CALENDER_MONTH       = "month";
        public static final String CALENDER_DAY         = "day";
        public static final String COLUMN_NAME_TITLE    = "title";
        public static final String COLUMN_NAME_CONTENTS = "contents";
        public static final String COLUMN_NAME_UPDATE   = "up_date";
    }
}