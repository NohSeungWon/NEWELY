package com.example.usser.newely;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.example.usser.newely.Adapter.Adater_main_info;
import com.example.usser.newely.Adapter.Adater_mydog;
import com.example.usser.newely.Interface.Server_connect;
import com.example.usser.newely.array_constructor.Main_dog_info;
import com.example.usser.newely.array_constructor.Mydog_info;
import com.example.usser.newely.databinding.ActivityMydogBinding;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class Mydog extends AppCompatActivity {
    ActivityMydogBinding binding;
    Retrofit retrofit;
    Server_connect server_connect;

    /// 리사이클러뷰 선언 하는 곳
    RecyclerView recyclerView_mydog; // 입금쪽 리사이클러뷰 생성
    RecyclerView.LayoutManager manager_recyclerView_mydog; // 입금쪽 레이아웃 매니저 생성

    String id ;

    ArrayList <Mydog_info> arrayList_mydog = new ArrayList<>(); // 모든 배열 합쳐서 넣는 곳

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_mydog);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_mydog); // 데이터 바인딩.

        // 넘어온 id 값으로 서버와 통신해서 정보를 리사이클러뷰에 뿌려줘야 함으로
        // json형태로 되어있는 값 중 id 값을 추출
        Intent get = getIntent();
        id = get.getStringExtra("id");
        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(id);
        id = element.getAsJsonObject().get("id").getAsString();

        Get_data(); // 받아온 id 값으로 서버에서 저장된 관심동물을 어레이리스트에 넣어주는 메소드

        // 어레이 리스트에 값이 넣어졌으므로 리사이클러뷰 셋팅
        recyclerView_mydog = findViewById(R.id.recycler_mydog); // 선언된 리사클뷰에 xml에 사용된 리사이클뷰 아이디값 적용
        manager_recyclerView_mydog = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        recyclerView_mydog.setLayoutManager(manager_recyclerView_mydog); // 리사이클뷰에 레이아웃을 위에 선언된 라이너 레이아웃으로 셋팅해줌
        Adater_mydog adater_mydog = new Adater_mydog(arrayList_mydog);
        recyclerView_mydog.setAdapter(adater_mydog);

        // 전체삭제 버튼
        binding.btnMydogAllDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                All_delete();
            }
        });


    } // oncreate end


    public void Get_data(){  // 서버에서 관심동물 불러오기
//             여기서부터는 웹과 통신해서 유저정보 변수에 담기
        retrofit = new Retrofit.Builder().baseUrl(Server_connect.URL).build(); // 레트로핏빌드, url은 인터페이스 기본 저장된데로
        server_connect = retrofit.create(Server_connect.class); // 뭔지 잘 모르겠음, 서버커낵트로 만든다라..
        Call<ResponseBody> connect = server_connect.mydog_get(id); // 접속시도하는데 서버커넥트인터페이스에서 지정한 방식을 사용 한다?

        connect.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String str = response.body().string();
//                    Log.e("하우",str);
                    try {
                            JSONObject jsonObject = new JSONObject(str); //오브젝트 생성
                            JSONArray Result = jsonObject.getJSONArray("results"); //results로 되어있는 값 가져오기
                            arrayList_mydog.clear(); // 초기화 한 번 하고
                            for (int i = 0; i < Result.length(); i++) {
//                                Log.e("체인지","포문 실행됨");
                                JSONObject jsonObject2 = Result.getJSONObject(i);
                                arrayList_mydog.add(
                                        new Mydog_info(
                                                jsonObject2.getString("popfile"),
                                                jsonObject2.getString("careaddr"),
                                                jsonObject2.getString("carenm"),
                                                jsonObject2.getString("kindcd"),
                                                jsonObject2.getString("specialMark"),
                                                jsonObject2.getString("age"),
                                                jsonObject2.getString("orgnm"),
                                                jsonObject2.getString("sexcd"),
                                                jsonObject2.getString("caretel"),
                                                jsonObject2.getString("desertionno")
                                        )
                                );
                            }
                            // 관심동물을 아무 것도 체크하지 않았을 때 관심동물이 없습니다 라는 메세지를
                            // 화면에 띄우기 위해 배열 사이즈가 0 일때 없습니다 라는 메세지를 넣어준다.

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("익셉션", String.valueOf(e));
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("보내기","실패");
            }
        });
    } //Get_data end

    public void All_delete(){  // 서버에서 관심동물 불러오기
//             여기서부터는 웹과 통신해서 유저정보 변수에 담기
        retrofit = new Retrofit.Builder().baseUrl(Server_connect.URL).build(); // 레트로핏빌드, url은 인터페이스 기본 저장된데로
        server_connect = retrofit.create(Server_connect.class); // 뭔지 잘 모르겠음, 서버커낵트로 만든다라..
        Call<ResponseBody> connect = server_connect.mydog_all_delete(id); // 접속시도하는데 서버커넥트인터페이스에서 지정한 방식을 사용 한다?

        connect.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String str = response.body().string();
                    // id를 서버에 보내 해당하는 정보를 다 지운다.
                    Adater_mydog adater_mydog = new Adater_mydog(arrayList_mydog);
                    arrayList_mydog.clear();
                    recyclerView_mydog.setAdapter(adater_mydog); // 이걸 안하면 오류가 걸리네.
                    adater_mydog.notifyDataSetChanged();
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("익셉션", String.valueOf(e));
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("보내기","실패");
            }
        });
    } //Get_data end




} // main end
