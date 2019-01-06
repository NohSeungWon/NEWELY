package com.example.usser.newely.Test;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.usser.newely.Interface.Server_connect;
import com.example.usser.newely.R;
import com.squareup.picasso.Picasso;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import retrofit2.Retrofit;


public class test extends Activity {
    Retrofit retrofit;
    Server_connect server_connect;
    TextView test_result;
    Button test_btn;
    ImageView imageView;
    String data;
    Bitmap bitmap;
    ArrayList <Bitmap> bitmaps_arr = new ArrayList<>();
    private Context context;

//    ArrayList<Test_info> test_infos = new ArrayList<>();

    ArrayList <String> image_array = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        final StringBuffer buffer=new StringBuffer();

        imageView = (ImageView)findViewById(R.id.imageView);

        test_btn = (Button) findViewById(R.id.test_btn);
        test_result = (TextView)findViewById(R.id.test_result);

        test_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Get_info();
//                imageView.setImageBitmap(bitmaps_arr.get(0));
            }
        });
//            Picasso.with(this).load("http://www.animal.go.kr/files/shelter/2018/10/201811131411934_s.jpg").into(imageView);
//

     } // oncreate end


    void Get_info(){ // 유기동물 api 파싱처리 실행시키는 메소드
        new Thread(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                data= getXmlData();//아래 메소드를 호출하여 XML data를 파싱해서 String 객체로 얻어오기

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
//                        test_result.setText(data); //TextView에 문자열  data 출력
                        Log.e("비트맵 사이즈", String.valueOf(bitmaps_arr.size()));
                        for (int a= 0; a<bitmaps_arr.size(); a++){
                            Log.e("비트맵 돌아가기", String.valueOf(bitmaps_arr.get(a)));
                        }

                    }
                });
            }
        }).start();

    } // get info end


    String getXmlData(){ // 유기동물 api xml 파싱 처리

        StringBuffer buffer=new StringBuffer();
        ImageView imageView = (ImageView)findViewById(R.id.imageView);

//        String str= edit.getText().toString();//EditText에 작성된 Text얻어오기
//        String location = URLEncoder.encode(str);//한글의 경우 인식이 안되기에 utf-8 방식으로 encoding     //지역 검색 위한 변수

//        String queryUrl="http://openapi.kepco.co.kr/service/evInfoService/getEvSearchList?"//요청 URL
//                +"addr="+location
//                +"&pageNo=1&numOfRows=1000&ServiceKey=" + key;
        String queryUr= "http://openapi.animal.go.kr/openapi/service/rest/abandonmentPublicSrvc/abandonmentPublic?serviceKey=Ph%2BHrpb%2F1MGqD1yZRKFPiCtSyIN9EH4Yv%2FOsnLqaOuF0on%2BzdQia8g5BNyJIiTa9Btnf5HRPvcf5iO7fXGwu8Q%3D%3D&bgnde=20180101&endde=20181010&upkind=417000&state=notice&pageNo=1&startPage=1&numOfRows=10&pageSize=10";
        try {
            URL url= new URL(queryUr);//문자열로 된 요청 url을 URL 객체로 생성.
            InputStream is= url.openStream(); //url위치로 입력스트림 연결

            XmlPullParserFactory factory= XmlPullParserFactory.newInstance();
            XmlPullParser xpp= factory.newPullParser();
            xpp.setInput( new InputStreamReader(is, "UTF-8") ); //inputstream 으로부터 xml 입력받기

            String tag;
            xpp.next();
            Log.e("로그",xpp.getText());

            int eventType= xpp.getEventType();
            while( eventType != XmlPullParser.END_DOCUMENT ){
                switch( eventType ){
                    case XmlPullParser.START_DOCUMENT:
                        buffer.append("파싱 시작...\n\n");
//                        Log.e("로그","1");
                        break;

                    case XmlPullParser.START_TAG:
                        tag= xpp.getName();//태그 이름 얻어오기

                        if(tag.equals("item")) ;// 첫번째 검색결과
                        else if(tag.equals("age")){
                            buffer.append("나이 : ");
                            xpp.next();
//                            Log.e("age로그",a);
//                            test_infos.add(new Test_info("",""));
                            buffer.append(xpp.getText());
                            buffer.append("\n"); //줄바꿈 문자 추가
                            Log.e("로그","2");
                        }
                        else if(tag.equals("kindCd")){
                            buffer.append("품종 : ");
                            xpp.next();
//                            Log.e("age로그","품종");
                            buffer.append(xpp.getText());
                            buffer.append("\n"); //줄바꿈 문자 추가
//                            Log.e("로그","3");
                        }
                        else if(tag.equals("filename")){
                            buffer.append("사진 : ");
                            xpp.next();
//                            buffer.append(xpp.getText());
                            bitmap = Picasso.with(this).load(Uri.parse( xpp.getText() )).get();
                            bitmaps_arr.add(bitmap);
//                            buffer.append("\n"); //줄바꿈 문자 추가
//                            Uri uri_img = Uri.parse(xpp.getText());
//                            image_array.add();
//                            Picasso.with(this).load(xpp.getText()).into(imageView);
//                            Log.e("로그","4");
                        }
                        else if(tag.equals("careNm")){
                            buffer.append("보호소 : ");
                            xpp.next();
                            buffer.append(xpp.getText());
                            buffer.append("\n"); //줄바꿈 문자 추가
//                            Log.e("로그","5");
                        }
                        else if(tag.equals("specialMark")){
                            buffer.append("특징 : ");
                            xpp.next();
                            buffer.append(xpp.getText());
                            buffer.append("\n"); //줄바꿈 문자 추가
//                            Log.e("로그","6");
                        }
                        break;

                    case XmlPullParser.TEXT:
//                        Log.e("로그","7");
                        break;

                    case XmlPullParser.END_TAG:
                        tag= xpp.getName(); //태그 이름 얻어오기
                        if(tag.equals("item")) buffer.append("\n");// 첫번째 검색결과종료..줄바꿈
//                        Log.e("로그",a);
//                        Log.e("로그","8");
                        break;
                }
//                Log.e("로그",a);
//                Log.e("로그","9");
                eventType= xpp.next();

            }

        } catch (Exception e) {
            // TODO Auto-generated catch blocke.printStackTrace();
        }

//        buffer.append("파싱 끝\n");
        return buffer.toString();//StringBuffer 문자열 객체 반환
    } // getxml end




} // main end




