package com.example.usser.newely.Fragement;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.usser.newely.Interface.Server_connect;
import com.example.usser.newely.Missing_content_add;
import com.example.usser.newely.R;
import com.example.usser.newely.array_constructor.Userinfo;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import net.daum.android.map.MapViewEventListener;
import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapReverseGeoCoder;
import net.daum.mf.map.api.MapView;
import android.support.v4.app.FragmentActivity;
import retrofit2.Retrofit;
import com.google.android.gms.maps.GoogleMap;

public class Missing_Protect_fragment extends Fragment implements OnMapReadyCallback{

    View rootView;
    MapView mapView;
    private GoogleMap map;

    Retrofit retrofit;
    Server_connect server_connect;

    /// 리사이클러뷰 선언 하는 곳
    RecyclerView recyclerView_missing; //  리사이클러뷰 생성
    RecyclerView.LayoutManager manager_recyclerView_missing; //  레이아웃 매니저 생성

    Button missingBtnAdd;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        rootView = inflater.inflate(R.layout.missing_protect_fragment, container, false);

//        SupportMapFragment mapFragment = (SupportMapFragment)this.getChildFragmentManager().findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);
//        mapView = (MapView)rootView.findViewById(R.id.map);
//        mapView.getMapAsync(this);
        mapView = (MapView) rootView.findViewById(R.id.map);


//        MapView mapView = new MapView(getActivity()); // 맵뷰 선언
//        ViewGroup mapViewContainer = (ViewGroup)rootView.findViewById(R.id.map_view); // 맵뷰가 띄워질 아이디값을참조
//        mapViewContainer.addView(mapView); // 런칭 ?
//        mapView.setMapViewEventListener(new MapViewEventListener() {
//            @Override
//            public void onLoadMapView() {
//                Log.e("맵 클릭시","내가나온다");
//            }
//        });

        // 새글 쓰기 버튼
        missingBtnAdd = (Button)rootView.findViewById(R.id.missing_btn_add);
        missingBtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),Missing_content_add.class);
                startActivity(intent);
            }
        });

        return rootView;
    } // oncreateView end
    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onLowMemory();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//액티비티가 처음 생성될 때 실행되는 함수

        if(mapView != null)
        {
            mapView.onCreate(savedInstanceState);
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

    }
} // main end
