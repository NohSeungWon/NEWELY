package com.example.usser.newely;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;

import com.example.usser.newely.Adapter.Adater_main_info;
import com.example.usser.newely.Fragement.Main_fragment;
import com.example.usser.newely.Fragement.Missing_Protect_fragment;
import com.example.usser.newely.Interface.Server_connect;
import com.example.usser.newely.Test.test;
import com.example.usser.newely.array_constructor.Main_dog_info;
import com.example.usser.newely.array_constructor.Userinfo;
import com.example.usser.newely.databinding.ActivityMainBinding;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;  // 데이터바인딩 선언

    // 기타 전역선언
    Retrofit retrofit; // 레트로핏 선언
    Server_connect server_connect; // 레트로핏 서버 통신 인터페이스 선언
//    Gson gson; 여기다 선언하면 자꾸 null 이 나는데 더 자세한건 check_login 메소드 주석확인


    // 로그인 확인하기 위한 String 만들어 놓기
    Userinfo userinfo; // 이것은 생성자!
    String user_id;
    String user_password;
    String user_nickname;
    int user_distinguish;


    // 필터로 선택시 url 변경하기 위해 전역변수로 선언
    String upr_cd = "&upr_cd=";
    String upking = "&upkind=417000";
    int add = 1; // 아래 페이지를 증가시키기 위한 선언
    String pageNo = "&pageNo=";
    String kind = "";

    // 프래그먼트 선언
    FragmentManager fm;
    FragmentTransaction tran;
    Main_fragment main_fragment = new Main_fragment();
    Missing_Protect_fragment missing_protect_fragment = new Missing_Protect_fragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main); // 데이터 바인딩.

//        final SharedPreferences share_id = getSharedPreferences("user_info", 0); // 로그인 한 후 저장된 쉐어 불러오기
//        final SharedPreferences.Editor edit_share_id = share_id.edit();  //에디트 선언
//        edit_share_id.clear();edit_share_id.apply();
        check_login(); // 로그인 확인 메소드

        final DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout); // 슬라이드메뉴 참조
        final View drawerView = (View) findViewById(R.id.drawer); // 슬라이드 메뉴 참조

        setFrag(0); // 메인프래그먼트 먼저 띄우기

        // 입양 버튼 눌렀을 때
        binding.Adoptbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFrag(0);
            }
        });
        // 실종/보호 눌렀을 때
        binding.Missingbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFrag(1);
            }
        });

        // 마이도그, 관심동물 상세 페이지 클릭시
        binding.btnMydog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final SharedPreferences share_id = getSharedPreferences("user_info", 0); // 로그인 한 후 저장된 쉐어 불러오기
                final SharedPreferences.Editor edit_share_id = share_id.edit();  //에디트 선언
                String user_info = share_id.getString("info", null); //쉐어값이 있다면 키값과 함께 넣고, 없다면 디폴트값은 null
                // 로그인이 안되어있을 경우
                // 즉 쉐어값이 null
                if (user_info == null)
                {
                    Toast.makeText(MainActivity.this, "로그인이 필요한 서비스 입니다.", Toast.LENGTH_SHORT).show();
                }
                // 로그인 되어있을 경우
                else
                {
                    Intent intent = new Intent(getApplicationContext(),Mydog.class);
                    intent.putExtra("id",user_info);
                    startActivity(intent);
                }
            }
        });


        // 마이페이지 버튼 클릭시
        binding.btnMypage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final SharedPreferences share_id = getSharedPreferences("user_info", 0); // 로그인 한 후 저장된 쉐어 불러오기
                final SharedPreferences.Editor edit_share_id = share_id.edit();  //에디트 선언
                String user_info = share_id.getString("info", null); //쉐어값이 있다면 키값과 함께 넣고, 없다면 디폴트값은 null
                // 로그인이 안되어있을 경우
                // 즉 쉐어값이 null
                if (user_info == null)
                {
                    Toast.makeText(MainActivity.this, "로그인이 필요한 서비스 입니다.", Toast.LENGTH_SHORT).show();
                }
                // 로그인 되어있을 경우
                else
                {
                    Intent intent = new Intent(getApplicationContext(),Mypage.class);
                    intent.putExtra("id",user_info);
                    startActivity(intent);
                }
            }
        });

        // 홈 버튼 클릭시
        binding.Homebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { // 홈버튼
                Intent intent = new Intent(getApplicationContext(),test.class);
                startActivity(intent);
            }
        });

        // 로그인 버튼을 눌렀을 때 처리
        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                if (binding.btnLogin.getText().equals("로그인")) {
                    // 로그인이 안되어있는 상태면 로그인 하러이동
                    Intent intent = new Intent(getApplicationContext(), Login.class);
                    startActivity(intent);
                } else {
                    // 쉐어드가 있어서 로그아웃으로 되어있는 상태고 버튼을 누르면
                    // 쉐어를 초기화하고 환영합니다. 지우고 버튼 텍스트도 로그인으로 변경
                    final SharedPreferences share_id = getSharedPreferences("user_info", 0); // 로그인 한 후 저장된 쉐어 불러오기
                    final SharedPreferences.Editor edit_share_id = share_id.edit();  //에디트 선언
                    String user_info = share_id.getString("info", null); //쉐어값이 있다면 키값과 함께 넣고, 없다면 디폴트값은 null
                    edit_share_id.clear();
                    edit_share_id.apply();
                    binding.btnLogin.setText("로그인");
//                    binding.btnNewtext.setEnabled(false);
//                    binding.btnNewtext.setVisibility(View.GONE);
                }
            }
        });

        // 슬라이드 메뉴 나오는 버튼
        binding.Menubtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(drawerView);
            }
        });



    } // onreate end
    /** oncreate end 시점  */

    public void setFrag(int n){    //프래그먼트를 교체하는 작업을 하는 메소드를 만들었습니다
        fm = getSupportFragmentManager();
        tran = fm.beginTransaction();
        switch (n){
            case 0:
                tran.replace(R.id.fragnment_main, main_fragment);  //replace의 매개변수는 (프래그먼트를 담을 영역 id, 프래그먼트 객체) 입니다.
                tran.commit();
                break;
            case 1:
                tran.replace(R.id.fragnment_main, missing_protect_fragment);  //replace의 매개변수는 (프래그먼트를 담을 영역 id, 프래그먼트 객체) 입니다.
                tran.commit();
                break;
            case 2:
//                tran.replace(R.id.main_frame, frag3);  //replace의 매개변수는 (프래그먼트를 담을 영역 id, 프래그먼트 객체) 입니다.
                tran.commit();
                break;
        }
    }




    // 사용자가 로그인을 했는지 체크하는 메소드
    public void check_login(){
        /**
         *  로그인 체크하고
         *  로그인이 되어있다면
         *  id, password, 닉네임 json으로 받고 스트링에 넣기
         */

//        Log.e("f","f");
        // 쉐어드의 존재여부로 로그인을 확인하는 로직

        final SharedPreferences share_id = getSharedPreferences("user_info", 0); // 로그인 한 후 저장된 쉐어 불러오기
        final SharedPreferences.Editor edit_share_id = share_id.edit();  //에디트 선언
        String user_info = share_id.getString("info", null); //쉐어값이 있다면 키값과 함께 넣고, 없다면 디폴트값은 null
//        Log.e("왜 없대 자꾸!!!!", user_info);

        if (user_info != null) { // 쉐어값이 널이 아닐 때 즉, 로그인이 되어있을 때
//            Log.e("쉐어 값??", user_info);
            Gson gson = new Gson();
            // 여기서(바로위) 제이슨을 선언 하지 않고 메인 바로 아래에 전역으로 설정하면 자꾸 Userinfo.class가 null 이라고 뜬다 도대체 왜그런지 모르겠다.
            userinfo = gson.fromJson(user_info, Userinfo.class); // item_main이라는 배열을 제이슨을 사용해서 값을 대입시킨다.
            user_id = userinfo.getId();
//            user_password = userinfo.getPassword();
//            Log.e("제이슨으로 생성 된 것 ", user_id + user_password);
            binding.btnLogin.setText("로그아웃");


//             여기서부터는 웹과 통신해서 유저정보 변수에 담기
            retrofit = new Retrofit.Builder().baseUrl(Server_connect.URL).build();
            server_connect = retrofit.create(Server_connect.class);

            Call<ResponseBody> connect = server_connect.distinguish(user_id);
//            Log.e("통신 전", user_id + user_password);
            connect.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        String str = response.body().string();
//                        Log.e("응답", str);
                        try {
                            JSONObject jsonObject = new JSONObject(str); //오브젝트 생성
                            JSONArray Result = jsonObject.getJSONArray("results"); //results로 되어있는 값 가져오기
                                for (int i = 0; i < Result.length(); i++) {
//                                Log.e("체인지","포문 실행됨");
                                JSONObject jsonObject2 = Result.getJSONObject(i);
                                    user_id= jsonObject2.getString("id");
                                    user_password = jsonObject2.getString("nickname");
                                    user_nickname = jsonObject2.getString("password");
                                    user_distinguish = jsonObject2.getInt("distinguish");
                                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });

        } //if end

    } //check_login end


} // main end

