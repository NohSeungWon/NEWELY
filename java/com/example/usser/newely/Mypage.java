package com.example.usser.newely;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.usser.newely.Interface.Server_connect;
import com.example.usser.newely.array_constructor.Userinfo;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Mypage extends AppCompatActivity {

    Retrofit retrofit;
    Server_connect server_connect;
    String id ;
    EditText et_mypage_input_nikname;
    EditText et_protect_mypage__password;
    EditText et_mypage_input_adress;

    String nikname;
    String password;
    String input_adress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);

        et_mypage_input_nikname = (EditText)findViewById(R.id.et_mypage_input_nikname);
        et_protect_mypage__password = (EditText)findViewById(R.id.et_protect_mypage__password);
        et_mypage_input_adress = (EditText)findViewById(R.id.et_mypage_input_adress);

        Intent get = getIntent();
        id = get.getStringExtra("id");

        // gson 파일을 변경시킴
        // gson 타입으로 만들어지는 건데 그냥 이름이고 형태가 json이 맞나봄 ..
        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(id);
        id = element.getAsJsonObject().get("id").getAsString();
//        Log.e("아이디가 없나?",id);
        check_login();


    }// oncreate end

    public void check_login(){
         retrofit = new Retrofit.Builder().baseUrl(Server_connect.URL).build();
         server_connect = retrofit.create(Server_connect.class);

            Call<ResponseBody> connect = server_connect.distinguish(id);
//            Log.e("통신 전", user_id + user_password);
            connect.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        String str = response.body().string();
                        try {
                            JSONObject jsonObject = new JSONObject(str); //오브젝트 생성
                            JSONArray Result = jsonObject.getJSONArray("results"); //results로 되어있는 값 가져오기
                            for (int i = 0; i < Result.length(); i++) {
//                                Log.e("체인지","포문 실행됨");
                                JSONObject jsonObject2 = Result.getJSONObject(i);
                                id= jsonObject2.getString("id");
                                nikname = jsonObject2.getString("nickname");
                                password = jsonObject2.getString("password");
                                // adress 값이 없는상태라면 로그 찍었을 때 none 이 뜬다.
                                input_adress = jsonObject2.getString("adress");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        et_mypage_input_adress.setText(input_adress);
                        et_mypage_input_nikname.setText(nikname);
                        et_protect_mypage__password.setText(password);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });

    } //check_login end



} // main end
