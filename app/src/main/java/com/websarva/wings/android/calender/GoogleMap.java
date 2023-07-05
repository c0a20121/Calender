package com.websarva.wings.android.calender;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.content.Intent;
import android.net.Uri;
import java.util.Locale;

import java.util.Locale;

public class GoogleMap extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        test0();
    }

    // 地名を入れて経路を検索
    private void test0(){
        // 起点
        String start = "八王子駅";

        // 目的地
        String destination = "東京工科大学";

        // 移動手段：電車:r, 車:d, 歩き:w
        String[] dir = {"r", "d", "w"};

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setClassName("com.google.android.apps.maps",
                "com.google.android.maps.MapsActivity");

        // 出発地, 目的地, 交通手段
        String str = String.format(Locale.US,
                "http://maps.google.com/maps?saddr=%s&daddr=%s&dirflg=%s",
                start, destination, dir[1]);

        intent.setData(Uri.parse(str));
        startActivity(intent);

    }
}