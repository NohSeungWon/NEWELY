package com.example.usser.newely;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;

import net.daum.android.map.MapViewEventListener;
import net.daum.mf.map.api.MapView;

public class Map extends AppCompatActivity {
    // 다음 지도 api 를 띄우는 액티비티
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);


//        // api 띄우기
//        MapView mapView = new MapView(this); // 액티비티 선언
////        ViewGroup mapViewContainer = (ViewGroup) findViewById(R.id.map_view); // xml 파일 선언 참조
//        mapViewContainer.addView(mapView); // 참조한 곳에 맵뷰를 더함
//
//        mapView.setMapViewEventListener(new MapViewEventListener() {
//            @Override
//            public void onLoadMapView() {
//                Log.e("맵 클릭시","내가나온다");
//            }
//        });

    }
}
