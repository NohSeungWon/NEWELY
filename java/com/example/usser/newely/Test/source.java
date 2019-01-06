package com.example.usser.newely.Test;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.example.usser.newely.Adapter.Adater_main_info;
import com.example.usser.newely.MainActivity;
import com.example.usser.newely.array_constructor.Main_dog_info;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

//public class source {

/**
 * 어레이 리스트를 json 어레이로 변형시시키
 */
// try {
//         JSONArray jArray = new JSONArray();//배열
//         for (int i = 0; i < arrayList_main_dog.size(); i++) {
//         Log.e("for","문");
//         JSONObject sObject = new JSONObject();//배열 내에 들어갈 json
//         sObject.put("orgname", arrayList_main_dog.get(i).getOrgNm());
//         sObject.put("age", arrayList_main_dog.get(i).getAge());
//         sObject.put("carenm", arrayList_main_dog.get(i).getCareNm());
//         jArray.put(sObject);
//         }
//         Log.e("JSON Test", jArray.toString());
//
//         } catch (JSONException e) {
//         e.printStackTrace();
//         }


/**
 * xml 파싱 하기
 */

//public class Get_api extends AsyncTask<String, String, String>{ // 유기동물 api 가져오는 앤싱크
//
//    // 진행중 다이얼로그 띄우기 위해 전역으로 선언
//    // 그래야 마무리 작업이 이루어질 때 다이얼로그를 dismiss 할 수 있음
//    ProgressDialog asyncDialog = new ProgressDialog(
//            MainActivity.this);
//
//    @Override
//    protected String doInBackground(String ... objects) {
//
//        StringBuffer buffer=new StringBuffer();
//
////        String str= edit.getText().toString();//EditText에 작성된 Text얻어오기
////        String location = URLEncoder.encode(str);//한글의 경우 인식이 안되기에 utf-8 방식으로 encoding     //지역 검색 위한 변수
//
//        String queryUr= "http://openapi.animal.go.kr/openapi/service/rest/abandonmentPublicSrvc/abandonmentPublic?serviceKey=" +
//                "Ph%2BHrpb%2F1MGqD1yZRKFPiCtSyIN9EH4Yv%2FOsnLqaOuF0on%2BzdQia8g5BNyJIiTa9Btnf5HRPvcf5iO7fXGwu8Q%3D%3D" +
//                "&bgnde=20160101" +
//                "&endde=20181231" +
//                upr_cd +
//                kind +
//                "&upkind=417000" +
//                "&state=notice" +
//                "&pageNo=1" +
//                "&numOfRows=10" ;
////                    "&pageSize=1";
//        try {
////                Log.e("앤싱크","트라이 시작");
//            URL url= new URL(queryUr);//문자열로 된 요청 url을 URL 객체로 생성.
//            InputStream is= url.openStream(); //url위치로 입력스트림 연결
//
//            XmlPullParserFactory factory= XmlPullParserFactory.newInstance();
//            XmlPullParser xpp= factory.newPullParser();
//            xpp.setInput( new InputStreamReader(is, "UTF-8") ); //inputstream 으로부터 xml 입력받기
//
//            String tag; // 태그이름을 저장하기 위해 선언
//            xpp.next(); // 무엇을 의미하는지 모르겠음, 그냥 다음단계로 진행이라는 의미인가,,
//
//
//            int eventType= xpp.getEventType(); // 이벤트 타입이 뭔지 모르겠음 왜 int 이지...
//
//            while( eventType != XmlPullParser.END_DOCUMENT ){ // 도큐멘트가 끝날 때 까지 실행하라는 의미..
//                // 스위치가 이벤트 타입인데. case에 스타트들이 int 형식임
//                // 정확히는 모르겠으나 내부적으로 어떤 xml의 형태들을 int로 해놓고
//                // 그에 해당하는 숫자를 만나면 파싱을 시작하는 것 같음
//
//                switch( eventType ){
//                    case XmlPullParser.START_DOCUMENT:
//                        buffer.append("파싱 시작...\n\n");
////                        Log.e("로그","1");
//                        break;
//
//                    case XmlPullParser.START_TAG:
//                        tag= xpp.getName();//태그 이름 얻어오기
////                            Log.e("로그",tag);
//
//                        if(tag.equals("item")) ;// 첫번째 검색결과
//                        else if(tag.equals("filename")){ // 이미지
//                            // 피카소로 uri를 비트맵으로 변경 하고 배열 저장
//                            xpp.next();
//                            bitmap = Picasso.with(getApplicationContext()).load(Uri.parse(xpp.getText())).get();
//                            arrlist_filename.add(bitmap);
//                        }
//                        else if(tag.equals("careAddr")){ // 보호장소
//                            xpp.next();
//                            arrlist_careAddr.add(xpp.getText());
//                        }
//                        else if(tag.equals("careNm")){ //보호소 이름
//                            xpp.next();
//                            arrlist_careNm.add(xpp.getText());
//                        }
//                        else if(tag.equals("kindCd")){ // 품종
//                            xpp.next();
//                            String a = xpp.getText();
//                            String b = a.replace("[개]","");
//                            arrlist_kindCd.add(b);
//                        }
//                        else if(tag.equals("specialMark")){ // 특징
//                            xpp.next();
//                            arrlist_specialMark.add(xpp.getText());
//                        }
//                        else if(tag.equals("age")){ // 나이
//                            xpp.next();
//                            arrlist_age.add(xpp.getText());
//                            buffer.append(xpp.getText());
//                        }
//                        else if(tag.equals("orgNm")){ // 지역
//                            xpp.next();
//                            arrlist_orgNm.add(xpp.getText());
//                            buffer.append(xpp.getText());
//                        }
//                        else if(tag.equals("sexCd")){ // 성별
//                            xpp.next();
//                            arrlist_sexCd.add(xpp.getText());
//                        }
//                        else if(tag.equals("officetel")){ // 전화번호
//                            xpp.next();
//                            arrlist_officetel.add(xpp.getText());
//                        }
//                        break;
//
//                    case XmlPullParser.TEXT:
////                        Log.e("로그","7");
//                        break;
//
//                    case XmlPullParser.END_TAG:
//                        tag= xpp.getName(); //태그 이름 얻어오기
//                        if(tag.equals("item")) buffer.append("\n");// 첫번째 검색결과종료..줄바꿈
//                        break;
//                } // switch end
//                eventType= xpp.next();
//            } // while end
//
//        } catch (Exception e) {
//            // TODO Auto-generated catch blocke.printStackTrace();
//        } // catch end
//
////            Log.e("앤싱크","리턴 줌");
//        return buffer.toString();//StringBuffer 문자열 객체 반환
//
//    } // doinbackground end
//
//    @Override
//    protected void onPreExecute() {
//
//        asyncDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//        asyncDialog.setMessage("로딩중입니다..");
//        // show dialog
//        asyncDialog.show();
//        super.onPreExecute();
//    }
//
//
//    @Override
//    protected void onPostExecute(String s) {  // 마무리작업
//        super.onPostExecute(s);
////            Log.e("앤싱크","마무리");
//        for (int a=0; a<arrlist_age.size(); a++) { // 총합 배열에다가 값 다 넣기
////                    Log.e("엥", arrlist_filename.get(a));
//            arrayList_main_dog.add(
//                    new Main_dog_info(arrlist_filename.get(a),
//                            arrlist_careAddr.get(a),
//                            arrlist_careNm.get(a),
//                            arrlist_kindCd.get(a),
//                            arrlist_specialMark.get(a),
//                            arrlist_age.get(a),
//                            arrlist_orgNm.get(a),
//                            arrlist_sexCd.get(a),
//                            arrlist_officetel.get(a))
//            );
//        } // for end
//        final Adater_main_info adater_main_info = new Adater_main_info(arrayList_main_dog);
//        recyclerView_main.setAdapter(adater_main_info);
//        adater_main_info.notifyDataSetChanged();
//        asyncDialog.dismiss();
//    } // onPostExecute end
//
//}// asyncktask end


/**
 * 앤싱크로 값 받기
 */

//    public class Task extends AsyncTask<String, Void, String> {
//
//        String clientKey = "#########################";
//        private final String ID = "########";
//
//        @Override
//        protected void onPostExecute(String s) {
//            super.onPostExecute(s);
////            t.setText(s);
//            if (s == null){
//                t.setText("없음");
//            }else {
//                Log.e("앤싱크", "끝");
//                t.setText(s);
//                foodlistjsonParser(s);
//            }
//        }
//
//        @Override
//        protected String doInBackground(String... params) {
////        Log.e("로그", "백그라운드 실행됨");
//            URL url = null;
//            try {
////            Log.e("로그", "트라이 실행됨");
//                url = new URL("http://52.79.233.199/test.php"); // 주소 입력하기
//
//                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
////            conn.setReadTimeout(5000);
////            conn.setConnectTimeout(5000);
////            conn.setRequestMethod("POST");
////            conn.setDoInput(true);
////            conn.connect();
//
////            Log.e("로그", "이프 전 실행됨");
//                if (conn.getResponseCode() == conn.HTTP_OK) {
////                Log.e("로그", "이프 실행됨");
//                    InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
//                    BufferedReader reader = new BufferedReader(tmp);
//                    StringBuffer buffer = new StringBuffer();
//                    while ((str = reader.readLine()) != null) {
////                    Log.e("로그", "while 실행됨");
//                        buffer.append(str);
//                    }
//                    receiveMsg = buffer.toString();
////                Log.e("receiveMsg : ", receiveMsg);
//
//                    reader.close();
//                } else {
//                    Log.i("통신 결과", conn.getResponseCode() + "에러");
//                }
//            } catch (MalformedURLException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
////        Log.e("로그", "리턴 전 실행됨");
//            return receiveMsg;
//        }
//
//    } // task asynctask end
//
//    public String[] foodlistjsonParser(String jsonString) {
//
//        String name = null;
//        String TAG_JSON = "result";
//        Log.e("넘어온 값",jsonString);
//        String[] arraysum = new String[8];
//        Log.e("파서","실행됨");
//        try {
//            Log.e("파서","트라이 실행됨");
//            for (int a=0; a<2; a++){Log.e("그냥","포문");};
//            JSONObject jsonObject = new JSONObject(jsonString);
//            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);
////            JSONArray jarray = new JSONObject(jsonString).getJSONArray("result");
//            for (int i = 0; i < jsonArray.length(); i++) {
//                Log.e("파서","포문 실행됨"+i);
//                HashMap map = new HashMap<>();
//                JSONObject jObject = jsonArray.getJSONObject(i);
//
//                name = jObject.optString("name");
//
//                arraysum[0] = name;
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//            Log.e("파서익셉션", String.valueOf(e));
//        }
//        Log.e("파서","리턴 실행됨");
//        return arraysum;
//    }

/**
* json 파일 뽑아내기
 */
//        public void Change(String jsonstring) { //json 파일 뽑아내는 메소드
//        try {
//            // {"results":[{"name":"Hellow"},{"name":"ddd"},{"name":"Hdd"},{"name":"wwwwww"}]} 현재 php.test 값 구조
//            Log.e("체인지","실행됨");
//            Log.e("체인지",jsonstring);
//            JSONObject jsonObject = new JSONObject(jsonstring); //오브젝트 생성
//            JSONArray Result = jsonObject.getJSONArray("results"); //results로 되어있는 값 가져오기
////            JSONArray name= new JSONArray(Result);
////            JSONArray jsonArray = new JSONArray(jsonstring);
//                for (int i = 0; i < Result.length(); i++) {
//                    Log.e("체인지","포문 실행됨");
//                    JSONObject jsonObject2 = Result.getJSONObject(i);
//                }
////                t1.setText("" + nameList + "\n");
//                } catch (JSONException e) {
//                    Log.e("체인지","익셉션 실행됨");
//                    e.printStackTrace();
//                    Log.e("익셉션에서 오류", String.valueOf(e));
//        }
//        return ;
//    } //change end


//} //// main end
