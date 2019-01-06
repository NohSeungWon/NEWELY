package com.example.usser.newely;

import android.content.SharedPreferences;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.usser.newely.Interface.Server_connect;
import com.example.usser.newely.array_constructor.Userinfo;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Volunteer extends AppCompatActivity {

    // 로그인 확인하기 위한 String 만들어 놓기
    Userinfo userinfo; // 이것은 생성자!
    String user_id;
    String user_password;
    String user_nickname;
    int user_distinguish;

    Retrofit retrofit;
    Server_connect server_connect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer);

//        final DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout); // 슬라이드메뉴 참조
//        final View drawerView = (View) findViewById(R.id.drawer); // 슬라이드 메뉴 참조

//        check_login(); // 로그인 확인 메소드
    }

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
//            Log.e("쉐어 값", user_info);
            Gson gson = new Gson();
            // 여기서(바로위) 제이슨을 선언 하지 않고 메인 바로 아래에 전역으로 설정하면 자꾸 Userinfo.class가 null 이라고 뜬다 도대체 왜그런지 모르겠다.
            userinfo = gson.fromJson(user_info, Userinfo.class); // item_main이라는 배열을 제이슨을 사용해서 값을 대입시킨다.
            user_id = userinfo.getId();
            user_password = userinfo.getPassword();
//            Log.e("제이슨으로 생성 된 것 ", user_id + user_password);
//            binding.missingBtnLogin.setText("로그아웃");


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
                        // 새글 생성하기 버튼 나오게하기
//            if (user_distinguish ==2){ // 보호소 사용자 일때만 2가 보호소 사용자 식별 번호
//                binding.btnNewtext.setEnabled(true);
//                binding.btnNewtext.setVisibility(View.VISIBLE);
//            }
//            Log.e("아이디",user_id);
//                        Log.e("비번",user_password);
//                        Log.e("닉네임",user_nickname);
//                        Log.e("구분", String.valueOf(user_distinguish));
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


}
