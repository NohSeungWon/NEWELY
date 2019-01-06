package com.example.usser.newely;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.usser.newely.Adapter.Adater_main_info;
import com.example.usser.newely.Interface.Server_connect;
import com.example.usser.newely.array_constructor.Main_dog_info;
import com.example.usser.newely.array_constructor.Mydog_info;
import com.example.usser.newely.array_constructor.Userinfo;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

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

public class Read_dogInfo extends AppCompatActivity {

    Bitmap bitmap;
    ImageView img_read;
    Retrofit retrofit;
    Server_connect server_connect;

    Gson gson;

    String user_info;
    String popfile; // 이미지
    String kindcd; // 품종
    String age; // 나이
    String sexcd;// 성별
    String specialMark; // 특징
    String careaddr; // 상세주소
    String caretel; // 연락처
    String desertionno; // 공고번호
    String CareNm; // 보호소이름
    String orgnm; // 지역

    ArrayList<String> check_desertionum = new ArrayList<>(); // 사용자가 관심동물 체크를 한 식별번호를 저장하기 위한 리스트

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_dog_info);

        final SharedPreferences share_id = getSharedPreferences("user_info", 0); // 로그인 한 후 저장된 쉐어 불러오기
        final SharedPreferences.Editor edit_share_id = share_id.edit();  //에디트 선언
        user_info = share_id.getString("info", null); //쉐어값이 있다면 키값과 함께 넣고, 없다면 디폴트값은 null

        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(user_info);
        user_info = element.getAsJsonObject().get("id").getAsString();

        // 넘어온 정보 인텐트 받기
        Intent get = getIntent();
        popfile = get.getStringExtra("Popfile"); // 이미지
        kindcd = get.getStringExtra("kindcd"); // 품종
        age = get.getStringExtra("age"); // 나이
        sexcd = get.getStringExtra("sexcd"); // 성별
        specialMark = get.getStringExtra("specialMark"); // 특징
        careaddr = get.getStringExtra("careaddr"); // 상세 주소
        caretel = get.getStringExtra("caretel"); // 연락처
        desertionno = get.getStringExtra("desertionno"); // 공고번호
        CareNm = get.getStringExtra("CareNm"); // 보호소이름
        orgnm = get.getStringExtra("orgnm"); // 지역

        img_read = (ImageView)findViewById(R.id.img_read); // 이미지 셋
        Picasso.with(this).load(popfile).into(img_read);

//        .resize(100,100)
        TextView tv_read_kindcd_carenm = (TextView)findViewById(R.id.tv_read_kindcd_carenm);
        tv_read_kindcd_carenm.setText(kindcd + " ("+CareNm+")");
        TextView tv_read_age_sexcd = (TextView)findViewById(R.id.tv_read_age_sexcd);
        tv_read_age_sexcd.setText("  "+sexcd+ " / "+ age);
        TextView tv_read_desertionNo = (TextView)findViewById(R.id.tv_read_desertionNo);
        tv_read_desertionNo.setText(desertionno);
        TextView tv_read_specialMark = (TextView)findViewById(R.id.tv_read_specialMark);
        tv_read_specialMark.setText(specialMark);
        TextView tv_read_careAddr = (TextView)findViewById(R.id.tv_read_careAddr);
        tv_read_careAddr.setText(careaddr);
        TextView tv_read_careTel = (TextView)findViewById(R.id.tv_read_careTel);
        tv_read_careTel.setText(caretel);


        Check_desertion(); // 사용자가 관심동물로 선택한건지 검사

        // 사용자가 관심동물을 체크할 수 있는 체크 박스
        final CheckBox ck_like_pat = (CheckBox)findViewById(R.id.ck_like_pat);
        ck_like_pat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user_info!=null) { // 아이디가 있을 때
                    if (ck_like_pat.isChecked()) { // 체크되었을 때
                        mydog_send();
                    } else { // 체크 해제 했을 때
                        mydog_delete();
                    }
                }
            }
        });

    } // oncreate end

    /**
     * 등록된 관심동물일시 체크처리하기
     */
    public void Check_desertion(){
        retrofit = new Retrofit.Builder().baseUrl(Server_connect.URL).build(); // 레트로핏빌드, url은 인터페이스 기본 저장된데로
        server_connect = retrofit.create(Server_connect.class); // 뭔지 잘 모르겠음, 서버커낵트로 만든다라..
        Call<ResponseBody> connect = server_connect.mydog_get(user_info); // 접속시도하는데 서버커넥트인터페이스에서 지정한 방식을 사용 한다?

        connect.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String str = response.body().string();
//                    Log.e("하우",str);
                    try {
                        JSONObject jsonObject = new JSONObject(str); //오브젝트 생성
                        JSONArray Result = jsonObject.getJSONArray("results"); //results로 되어있는 값 가져오기
                        for (int i = 0; i < Result.length(); i++) {
//                                Log.e("체인지","포문 실행됨");
                            JSONObject jsonObject2 = Result.getJSONObject(i);
                            check_desertionum.add(
                                            jsonObject2.getString("desertionno")
                            );
                        }
                        // 서버에서 받아온 식별번호와 현재 액티비티에 식별번호 (인텐트로 받은)가 같다면
                        // 관심동물 체크상태로 변환시켜라
                        for (int a=0; a<check_desertionum.size(); a++){
                        if (check_desertionum.get(a).equals(desertionno)){
                            CheckBox ck_like_pat = (CheckBox)findViewById(R.id.ck_like_pat);
                            ck_like_pat.setChecked(true);
                        }
                    }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                catch (IOException e) {
                    e.printStackTrace();
                    Log.e("익셉션", String.valueOf(e));
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("보내기","실패");
            }
        });
    } //Check_desertion end

    /**
     *  관심체크를 취소했을 때
     */
    public void mydog_delete(){
//             여기서부터는 웹과 통신해서 유저정보 변수에 담기
        retrofit = new Retrofit.Builder().baseUrl(Server_connect.URL).build();
        server_connect = retrofit.create(Server_connect.class);
        Call<ResponseBody> connect = server_connect.mydog_delete(user_info,desertionno);
        connect.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String str = response.body().string();
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
    } //mydog_delete end

    /**
     *  관심체크를 했을 때 서버로 해당하는 정보 보냄
     */
    public void mydog_send(){

//             여기서부터는 웹과 통신해서 유저정보 변수에 담기
            retrofit = new Retrofit.Builder().baseUrl(Server_connect.URL).build();
            server_connect = retrofit.create(Server_connect.class);
            Call<ResponseBody> connect = server_connect.mydog_send(user_info,desertionno,specialMark,
                                                                    age,careaddr,sexcd,caretel,kindcd,CareNm,orgnm,popfile);
            connect.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        String str = response.body().string();
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
    } //mydog_send end


} // main end
