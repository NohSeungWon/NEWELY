package com.example.usser.newely;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.usser.newely.Interface.Server_connect;
import com.example.usser.newely.databinding.ActivityJoinMemberBinding;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


/** 회원가입 액티비티 */
public class Join_member extends Activity {
    ActivityJoinMemberBinding binding;  // 데이터바인딩 선언

    Retrofit retrofit;
    Server_connect server_connect;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_member);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_join_member); // 데이터 바인딩.


        binding.btnAgreeJoin.setOnClickListener(new View.OnClickListener() { // 눌렀을 때 처리
            @Override
            public void onClick(View view) { // 동의하고 회원가입 버튼 눌렀을 때
                String email = binding.inputEmail.getText().toString();
                String nickname = binding.inputNikname.getText().toString();
                String password = binding.inputPassword.getText().toString();
                String password_check = binding.inputPasswordCheck.getText().toString();

                if(email.getBytes().length <= 0 && nickname.getBytes().length <= 0 && password.getBytes().length <= 0 && password_check.getBytes().length <= 0){ // 전부 입력없을 때
                    Toast.makeText(Join_member.this, "정보를 입력해주세요", Toast.LENGTH_SHORT).show();

                }else if(email.getBytes().length <= 0){// 이메일 입력안했을 때
                    Toast.makeText(Join_member.this, "아이디를 입력해주세요", Toast.LENGTH_SHORT).show();

                }else if(nickname.getBytes().length <= 0){// 닉네임 입력안했을 때
                    Toast.makeText(Join_member.this, "닉네임을 입력해주세요", Toast.LENGTH_SHORT).show();

                }else if(password.getBytes().length <= 0){// 비밀번호 입력안했을 때
                    Toast.makeText(Join_member.this, "비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show();

                }else if(password_check.getBytes().length <= 0){// 비밀번호확인 입력안했을 때
                    Toast.makeText(Join_member.this, "확인용 비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show();

                }else {
                    int distinguish = 1; // 일반사용자 구분 번호
                    retrofit = new Retrofit.Builder().baseUrl(Server_connect.URL).build();
                    server_connect = retrofit.create(Server_connect.class);


                    if (password.equals(password_check)) { // 패스워드가 맞을 때

                        Call<ResponseBody> sending = server_connect.create(email, nickname, password, distinguish);
                        sending.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                try {
                                    int check = Integer.parseInt(response.body().string()); // 넘어오는 값이 int 임
//                                    Log.e("구분값 체크", String.valueOf(check));
                                    int success = 1; // 중복되는 게 없으면
                                    int fail = 2; // 중복이 있으면
//                                Log.e("체크", String.valueOf(check));
                                    if (success == check) { //일치할 때 처리
                                        // 로그인 액티비티로 이동시킨다.
                                        Intent intent = new Intent(getApplicationContext(), Login.class);
                                        startActivity(intent);
                                    } else if (fail == check) { // 일치하지 않을 때 처리
                                        Toast.makeText(Join_member.this, "아이디가 중복됩니다.", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }


                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {

                            }
                        });

                    } else { // 비번이 일치하지 않을 때
                        Toast.makeText(Join_member.this, "일치하지 않는 비밀번호 입니다.", Toast.LENGTH_SHORT).show();
                    } // 비밀번호 교차 확인 else end

                }//else end


            }
        });




    }  // oncreate end




} // main end
