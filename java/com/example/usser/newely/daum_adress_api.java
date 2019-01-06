package com.example.usser.newely;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class daum_adress_api extends AppCompatActivity {
    WebView webView;
    Handler handler;
    TextView result;

    String id;
    String nickname;
    String password ;
    String password_check;
    String adress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daum_adress_api);
        handler = new Handler();
        // 주소 입력 후 완료를 눌렀을 때 써놓았던 정보들이 날라가기 때문에
        // 인텐트를 사용해서 받고 다시 보내주기 위한 처리
        // 여기는 받는 곳
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        nickname = intent.getStringExtra("nickname");
        password = intent.getStringExtra("password");
        password_check =  intent.getStringExtra("password_check");
//        Log.e("다음 받은 값", id+nickname+password+password_check);
        init_webView(); // 주소 api 실행

    }


    public void init_webView() {
        // WebView 설정
        webView = (WebView) findViewById(R.id.daum_webview);
        // JavaScript 허용
        webView.getSettings().setJavaScriptEnabled(true);
        // JavaScript의 window.open 허용
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        // JavaScript이벤트에 대응할 함수를 정의 한 클래스를 붙여줌
        // 두 번째 파라미터는 사용될 php에도 동일하게 사용해야함
        webView.addJavascriptInterface(new AndroidBridge(), "TestApp");
        // web client 를 chrome 으로 설정
        webView.setWebChromeClient(new WebChromeClient());
        // webview url load
        webView.loadUrl("http://106.10.33.230/Newely/adress_search.php");
    }

    private class AndroidBridge {
            @JavascriptInterface
            public void setAddress(final String arg1, final String arg2, final String arg3) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        final TextView tv_adress_main = (TextView)findViewById(R.id.tv_adress_main);  // 메인주소 입력
                        final EditText et_daum_input_adress = (EditText)findViewById(R.id.et_daum_input_adress); // 상세주소 입력

    //                    Log.e("1", arg1); // 우편번호
    //                    Log.e("2", arg2); // 주소
    //                    Log.e("3", arg3); // 주소 건물번호?
    //                    result.setText(String.format("(%s) %s %s", arg1, arg2, arg3)); 우편번호사용하고 싶으면 이걸로
                        tv_adress_main.setText(String.format("%s %s", arg2, arg3));
                        // WebView를 초기화 하지않으면 재사용할 수 없음
                        init_webView();
                        Button btn_daum_next = (Button)findViewById(R.id.btn_daum_next);
                        btn_daum_next.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent();
                                adress = tv_adress_main.getText().toString() + " " + et_daum_input_adress.getText().toString(); // 주소합치기
//                                Log.e("설마",adress);
                                intent.putExtra("adress", adress);
                                setResult(RESULT_OK,intent);
                                finish();
                            }
                        });
                    }
                });
            }
    }
}

