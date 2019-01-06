package com.example.usser.newely;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.usser.newely.Interface.Server_connect;
import com.example.usser.newely.array_constructor.Userinfo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


/** 로그인 액티비티 */
public class Login extends Activity {
    Retrofit retrofit; // 레트로핏
    Server_connect server_connect; // 서버접속 인터페이스
    Gson gson;

    EditText id;
    EditText password;
    Button joinmember; // 회원가입 액티비티로 이동
    Button login; // 로그인 버튼


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        /** 참조 선언 */
        id = (EditText)findViewById(R.id.et_input_email);
        password =(EditText)findViewById(R.id.et_input_password);
        joinmember = (Button)findViewById(R.id.btn_joinmember);
        login =(Button)findViewById(R.id.btn_login);



        joinmember.setOnClickListener(new View.OnClickListener() { //회원가입 버튼 눌렀을 때
            @Override
            public void onClick(View view) {
                choice_member();
            }
        });


        login.setOnClickListener(new View.OnClickListener() { // 로그인 버튼을 눌렀을 때
            @Override
            public void onClick(View view) {
                final String user_id = id.getText().toString(); // 에딧텍스트에 사용자가 입력한 아이디
                final String user_password = password.getText().toString(); // 에딧텍스트에 사용자가 입력한 패스워드

                if(user_id.getBytes().length <= 0 && user_password.getBytes().length <= 0){ // 아이디,비밀번호 둘다 없을 때
                    Toast.makeText(Login.this, "아이디와 비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show();

                }else if (user_password.getBytes().length <= 0){//비밀번호에 아무것도 입력안했을 때 처리
                    Toast.makeText(Login.this, "비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show();

                }else if (user_id.getBytes().length <= 0){ //아이디에 아무것도 입력안했을 때 처리
                    Toast.makeText(Login.this, "아이디를 입력해주세요", Toast.LENGTH_SHORT).show();

                }else {

                    retrofit = new Retrofit.Builder().baseUrl(Server_connect.URL).build();
                    server_connect = retrofit.create(Server_connect.class);


                    Call<ResponseBody> connect = server_connect.login(user_id, user_password);
                    connect.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            try {
                                int check = Integer.parseInt(response.body().string()); // 넘어오는 값이 int 임
                                int success = 1; // 정보가 일치하면 php에서 1을 보냄
                                int fail = 2; // 정보가 불일치하면 php에서 2를 보냄
//                            Log.e("체크", String.valueOf(check));
                                if (success == check) { //일치할 때 처리

                                    // gson으로 아이디, 비번 배열에 넣기
                                    // 로그인 체크시 활용하기 위함
                                    Userinfo userinfo = new Userinfo();
                                    userinfo.setId(user_id);
                                    userinfo.setPassword(user_password);
                                    gson = new GsonBuilder().create(); // 인스턴스 생성
                                    String login_array = gson.toJson(userinfo); // json 변환

                                    SharedPreferences share_id = getSharedPreferences("user_info", 0); // 아이디 쉐어에 저장하기
                                    final SharedPreferences.Editor edit_share_id = share_id.edit();  //에디트 선언
                                    edit_share_id.putString("info", login_array); // 쉐어에 아이디넣기
                                    edit_share_id.apply(); // 저장하기

                                    String user_info = share_id.getString("info", null); //쉐어값이 있다면 키값과 함께 넣고, 없다면 디폴트값은 null
//                                    Log.e("쉐어값", user_info);
                                    userinfo = gson.fromJson(user_info,Userinfo.class); // 변환
                                    String user_id = userinfo.getId();
                                    String user_password = userinfo.getPassword();
//                                    Log.e("유저정보확인",user_id+user_password);

                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);  // 메인으로 이동시킨다.
                                    startActivity(intent); // 메인으로 이동하기
                                } else if (fail == check) { // 일치하지 않을 때 처리
                                    Toast.makeText(Login.this, "아이디 비밀번호를 확인하세요", Toast.LENGTH_SHORT).show();
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {

                        }
                    });
                }// else end
            } //onclick end
        }); //login end


    } // oncreate end


    void choice_member(){  // 회원가입 버튼 누르면
        // 일반 사용자 할건지
        // 보호기관 할건지 물어보는 다이얼로그

        final List<String> ListItems = new ArrayList<>();
        ListItems.add("일반");
        ListItems.add("보호기관");
        final CharSequence[] items =  ListItems.toArray(new String[ ListItems.size()]);

        final List SelectedItems  = new ArrayList();
        int defaultItem = 0;
        SelectedItems.add(defaultItem);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("회원가입 유형을 선택하세요 \n (보호기관은 사업자번호 필요)");
        builder.setSingleChoiceItems(items, defaultItem,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SelectedItems.clear();
                        SelectedItems.add(which);
                    }
                });
        builder.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        int a = (int) SelectedItems.get(0); // 사용자 선택값 담기
//                        Log.e("선택", String.valueOf(a));
                        if (a ==0){ // 일반 선택
                            Intent intent = new Intent(getApplicationContext(),Join_member.class);
                            startActivity(intent);
                        }else { // 사업자 선택
                            Intent intent = new Intent(getApplicationContext(),Join_memner_protecter.class);
                            startActivity(intent);
                        }
                    }
                });
        builder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        builder.show();
    } // 다이얼로그 종료

} //main end




//retrofit = new Retrofit.Builder().baseUrl(Get.API_URL).build(); // get 인터페이스에 전역으로 설정되어있는 url로 빌드를 시도한다
//        get = retrofit.create(Get.class); // 겟이라는 인터페이스에 레트로핏을 연결시킨다?
//
//        String user_id = id.getText().toString(); // 에딧텍스트에 사용자가 입력한 아이디
//        String user_password = password.getText().toString(); // 에딧텍스트에 사용자가 입력한 패스워드
//
//
//        Call<ResponseBody> geting = get.geting(user_id,user_password);
//        // 안드로이드에서 get으로 보내는 것 같다. getting() 괄호안에 있는 것이 인터페이스에서 설정해놓은
//        // 변수들의 이름에 들어간다.
//        // Call<ResponseBody> geting  여기서의 getting은 단지 이름 실제 인터페이스에 설정된 이름은
//        // get.getting <- 이거다
//
//        geting.enqueue(new Callback<ResponseBody>() { // 접속하고 난 다음
//@Override
//public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) { // 여기는 성공하면
//        try {
//        //Response<ResponseBody> response 이놈이 php 파일 전체를 긁어오는 것 같다.
//        String get_text = response.body().string(); // 스트링에 긁어온 파일을 넣는다.
//        Log.e("뭔가확인",response.body().string());
////                            Change(a);
//        } catch (IOException e) {
//        e.printStackTrace();
//        Log.e("오류익셉션",e.toString());
//        }
//        }
//@Override
//public void onFailure(Call<ResponseBody> call, Throwable t) { // 여기는 실패하면
//        Log.e("통신실패",t.toString());
//        }
//        });