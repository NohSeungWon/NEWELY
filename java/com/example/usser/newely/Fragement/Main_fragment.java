package com.example.usser.newely.Fragement;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.usser.newely.Adapter.Adater_main_info;
import com.example.usser.newely.Interface.Server_connect;
import com.example.usser.newely.R;
import com.example.usser.newely.Read_dogInfo;
import com.example.usser.newely.array_constructor.Main_dog_info;
import com.example.usser.newely.array_constructor.Userinfo;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import retrofit2.Retrofit;

public class Main_fragment extends Fragment {

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

//    /// 리사이클러뷰 선언 하는 곳
    RecyclerView recyclerView_main; //  리사이클러뷰 생성
    RecyclerView.LayoutManager manager_recyclerView_main; //  레이아웃 매니저 생성
//
//    // 유기동물 api 가져올 때 쓸 어레이 선언
    ArrayList <String> arrlist_popfile = new ArrayList<>(); // 이미지
    ArrayList <String> arrlist_careAddr = new ArrayList<>(); // 보호장소
    ArrayList <String> arrlist_careNm = new ArrayList<>(); // 보호소이름
    ArrayList <String> arrlist_kindCd = new ArrayList<>(); // 품종
    ArrayList <String> arrlist_specialMark = new ArrayList<>(); // 특징
    ArrayList <String> arrlist_age = new ArrayList<>(); // 나이
    ArrayList <String> arrlist_orgNm= new ArrayList<>(); // 지역
    ArrayList <String> arrlist_sexCd= new ArrayList<>(); // 성별
    ArrayList <String> arrlist_caretel= new ArrayList<>(); // 보호소 전화번호
    ArrayList <String> arrlist_desertionNo= new ArrayList<>(); // 공고번호
    ArrayList <Main_dog_info> arrayList_main_dog = new ArrayList<>(); // 모든 배열 합쳐서 넣는 곳

    // 필터로 선택시 url 변경하기 위해 전역변수로 선언
    String upr_cd = "&upr_cd=";
    String upking = "&upkind=417000";
    int add = 1; // 아래 페이지를 증가시키기 위한 선언
    String pageNo = "&pageNo=";
    String kind = "";
    View rootView;

    @Override
    public View onCreateView( LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.main_fragment, container, false);

        final Get_api get_api= new Get_api(); // 유기동물 api 긁어오는 앤싱크 선언
        get_api.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);; // 앤싱크 시작

//        // 리사이클러뷰 작업
        recyclerView_main = rootView.findViewById(R.id.recycler_main); // 선언된 리사클뷰에 xml에 사용된 리사이클뷰 아이디값 적용
        manager_recyclerView_main = new LinearLayoutManager(getActivity());
        recyclerView_main.setLayoutManager(manager_recyclerView_main); // 리사이클뷰에 레이아웃을 위에 선언된 라이너 레이아웃으로 셋팅해줌

        final Adater_main_info adater_main_info = new Adater_main_info(arrayList_main_dog);
        recyclerView_main.setAdapter(adater_main_info);


       //  스크롤 이벤트 (페이징)
        recyclerView_main.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged( RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
//                Log.e("뭐지", String.valueOf(newState));
            }

            @Override
            public void onScrolled( RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                // 레이아웃의 끝일 때 포지션 알려줌 +1을 안하면 아이템 숫자보다 1이 적음
                int lastPosiTion = ((LinearLayoutManager)recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition()+1;
                int itemTotalCount = recyclerView.getAdapter().getItemCount();
                Get_api re = new Get_api();
                if (lastPosiTion == itemTotalCount){
                    if (itemTotalCount>=10) {
                        get_api.cancel(true); // 처음 시작된 앤싱크 종료
                        add = add + 1; // 페이지 증가시키기
                        setRetainInstance(true);
                        re.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    }
                }
            }
        });

         //리사이클러뷰 아이템 클릭시
         //인텐트로 해당하는 유기동물 정보를 read 액티비티로 인텐트를 통해 넘긴다.
        recyclerView_main.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            // 여러번 클릭되지 않게 처리 하는 것 GestureDetector
            final GestureDetector gestureDetector =
                    new GestureDetector(getActivity(), new GestureDetector.SimpleOnGestureListener() {
                        @Override
                        public boolean onSingleTapUp(MotionEvent e) {
                            return true;
                        }
            });

            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                View child = rv.findChildViewUnder(e.getX(), e.getY());
                int potision = rv.getChildAdapterPosition(child); // 클릭시 리사이클러뷰 포지션 값을 가져온다
                if (child != null && gestureDetector.onTouchEvent(e)) { // 중복클릭안되게 하는 것
//                    Log.e("포지션", String.valueOf(potision));
                    // 자세한 정보를 볼 수 있는 액티비티로 이동 시킬 것.
                    // 인텐트로 다 넘긴다.
                    Intent intent = new Intent(getActivity(),Read_dogInfo.class);
                    intent.putExtra("Popfile",arrayList_main_dog.get(potision).getPopfile()); // 이미지
                    intent.putExtra("kindcd",arrayList_main_dog.get(potision).getKindCd()); // 품종
                    intent.putExtra("age",arrayList_main_dog.get(potision).getAge()); // 나이
                    intent.putExtra("sexcd",arrayList_main_dog.get(potision).getSexCd()); // 성별
                    intent.putExtra("specialMark",arrayList_main_dog.get(potision).getSpecialMark()); // 특징
                    intent.putExtra("careaddr",arrayList_main_dog.get(potision).getCareAddr()); // 상세주소
                    intent.putExtra("caretel",arrayList_main_dog.get(potision).getCareTel()); // 보호소 전화번호
                    intent.putExtra("desertionno",arrayList_main_dog.get(potision).getDesertionNo()); // 보호소 전화번호
                    intent.putExtra("CareNm",arrayList_main_dog.get(potision).getCareNm()); // 보호소 전화번호
                    intent.putExtra("orgnm",arrayList_main_dog.get(potision).getOrgNm()); // 지역
                    startActivity(intent);

                }
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView recyclerView,  MotionEvent motionEvent) {

            }
            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean b) {

            }

        }); // binding.recyclerMain.addOnItemTouchListener end


//         필터 클릭하면 커스텀 다이얼로그 띄움
        Button mainFilter= (Button)rootView.findViewById(R.id.main_filter);
        mainFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomDialog customDialog = new CustomDialog(getActivity());
                customDialog.callFunction();
            }
        });

        return rootView;

    } // oncreateview end

    // 필터 적용시키는 다이얼로그
    public class CustomDialog {

        Spinner spinner;
        private Context context;

        // 필터에서 선택한 조건을 메인에 띄워주기 위한 텍스트 처리
        String location ; // 위치
        String choice_name ;  // 품종

        public CustomDialog(Context context) {
            this.context = context;
        }

        // 호출할 다이얼로그 함수를 정의한다.
        public void callFunction() { //
            // 사용자가 스피너에서 선택하는 내용과 실제로 api 요청하게 되는 정보를 매칭시키기위해
            // 배열을 2개 만듬
            // 스피너 선택 포지션값과 매칭되게 배열을 만듬
            final String [] local_place = {"없음","서울특별시","부산" +
                    "광역시","대구광역시","인천광역시","광주광역시",
                    "대전광역시","울산광역시"};
            final String [] local_place_num = {"","&upr_cd=6110000","&upr_cd=6260000","upr_cd=6270000","&upr_cd=6280000","&upr_cd=6290000",
                    "&upr_cd=6300000","&upr_cd=6310000"};
            final String [] dog_name = {"없음","골든리트리버","그레이트 덴","기타","닥스훈트","달마시안"
                    ,"미니어쳐 푸들","미디엄 푸들","믹스견","불독","삽살개","시츄"};
            final String [] dog_name_num = {"","&kind=000054","&kind=000055","&kind=000115","&kind=000038","&kind=000039",
                    "&kind=000078","&kind=000074","&kind=000114","&kind=000027","&kind=000001","&kind=000101"};

            // 커스텀 다이얼로그를 정의하기위해 Dialog클래스를 생성한다.
            final Dialog dlg = new Dialog(context);
            // 액티비티의 타이틀바를 숨긴다.
            dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
            // 커스텀 다이얼로그의 레이아웃을 설정한다.
            dlg.setContentView(R.layout.custom_dialog_main_filter_);
            // 커스텀 다이얼로그를 노출한다.
            dlg.show();

            // 스피너의 내용을 배열 내용으로 채움
            // 마지막 local_place가 배열 선언 하는 것
            ArrayAdapter<String> adapter_local = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, local_place);
            Spinner spn_local  = (Spinner)dlg.findViewById(R.id.spinner_local); // 스피너 아이디값 앞에 dig는 다이얼로그에 위치하기 때문에.
            adapter_local.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // 어떤 형식으로 보여줄지
            spn_local.setAdapter(adapter_local); // 어답터를 셋팅 ?
            spn_local.setSelection(0); // 0 번째있는 것으로 스피너 초기값을 셋팅

            spn_local.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    // 스피너의 포지션 값과 배열의 값 순서를 일치시켜서
                    // 아래 값을 셋팅
                    location = local_place[position];
                    upr_cd= local_place_num[position];
                } // onItemSelected end

                @Override
                public void onNothingSelected(AdapterView<?> parent){
                } //onNothingSelected end

            }); // setOnItemSelectedListener end

            // 스피너의 내용을 배열 내용으로 채움
            ArrayAdapter<String> adapter_dog = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, dog_name);
            Spinner spn_dog  = (Spinner)dlg.findViewById(R.id.spinner_dog_name);
            adapter_dog.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spn_dog.setAdapter(adapter_dog);
            spn_dog.setSelection(0);

            spn_dog.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    choice_name = dog_name[position];
                    kind = dog_name_num[position];
                } // onItemSelected end

                @Override
                public void onNothingSelected(AdapterView<?> parent){
                } // onNothingSelected end

            }); // setOnItemSelectedListener end

            Button ok = (Button)dlg.findViewById(R.id.dialog_ok); //적용버튼 누르기
            ok.setOnClickListener(new AdapterView.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // ㅜㅜ 다 초기화 해줌, 안그러면 덮어써지니까
                    // 한번에 xml 파일 긁고 json 변환 안되냐 정말 왜 난 못하냐 하아
                    arrlist_popfile.clear();
                    arrlist_careAddr.clear(); // 보호장소
                    arrlist_careNm.clear(); // 보호소이름
                    arrlist_kindCd.clear(); // 품종
                    arrlist_specialMark.clear(); // 특징
                    arrlist_age.clear(); // 나이
                    arrlist_orgNm.clear(); // 지역
                    arrlist_sexCd.clear(); // 성별
                    arrlist_caretel.clear(); //전번
                    arrayList_main_dog.clear(); // 모든 배열 합쳐서 넣는 곳
                    arrlist_desertionNo.clear();
                    add = 1; // add 초기화

//                    Get_info(); // 다시 api 가져오기 실행
                    Get_api get_api = new Get_api();
                    get_api.execute();
                    TextView textView = (TextView)rootView.findViewById(R.id.tv_main_filter_show);
                    if(location.equals("없음") && choice_name.equals("없음")){
                        textView.setText("필터조건 : 없음 ");
                    }else {
                        textView.setText("위치 : " + location + " / 품종 : " + choice_name);
                    }
                    dlg.dismiss();
                }
            });

            Button cancel = (Button)dlg.findViewById(R.id.dialog_cancel); // 취소버튼 눌르기
            cancel.setOnClickListener(new AdapterView.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dlg.dismiss();
                }
            });
        }

    } // custom 다이얼로그 end

     //유기동물 api 가져오는 앤싱크
    public class Get_api extends AsyncTask<String, String, String>{
    // 진행중 다이얼로그 띄우기 위해 전역으로 선언
    // 그래야 마무리 작업이 이루어질 때 다이얼로그를 dismiss 할 수 있음
    ProgressDialog asyncDialog = new ProgressDialog(getActivity());

    @Override
    protected String doInBackground(String ... objects) {
//        Log.e("dd",upr_cd+kind);
        StringBuffer buffer=new StringBuffer();

        // 페이징하고 데이터가 순차적으로 쌓이기 위한 초기화
        // 만약 안하면 있던 데이터가 또 추가되는 현상 발생
        arrlist_popfile.clear();
        arrlist_careAddr.clear(); // 보호장소
        arrlist_careNm.clear(); // 보호소이름
        arrlist_kindCd.clear(); // 품종
        arrlist_specialMark.clear(); // 특징
        arrlist_age.clear(); // 나이
        arrlist_orgNm.clear(); // 지역
        arrlist_sexCd.clear(); // 성별
        arrlist_caretel.clear(); //전번
        arrlist_desertionNo.clear();

//        String str= edit.getText().toString();//EditText에 작성된 Text얻어오기
//        String location = URLEncoder.encode(str);//한글의 경우 인식이 안되기에 utf-8 방식으로 encoding     //지역 검색 위한 변수

        String queryUr= "http://openapi.animal.go.kr/openapi/service/rest/abandonmentPublicSrvc/abandonmentPublic?serviceKey=" +
                "Ph%2BHrpb%2F1MGqD1yZRKFPiCtSyIN9EH4Yv%2FOsnLqaOuF0on%2BzdQia8g5BNyJIiTa9Btnf5HRPvcf5iO7fXGwu8Q%3D%3D" +
                "&bgnde=20160101" +
                "&endde=20181231" +
                upr_cd +
                kind +
                "&upkind=417000" +
                "&state=notice" +
                pageNo + add +
                "&numOfRows=10" ;
//        Log.e("안되고있지?",pageNo+ add);
        try {
                Log.e("앤싱크","트라이 시작");
            URL url= new URL(queryUr);//문자열로 된 요청 url을 URL 객체로 생성.
            InputStream is= url.openStream(); //url위치로 입력스트림 연결

            XmlPullParserFactory factory= XmlPullParserFactory.newInstance();
            XmlPullParser xpp= factory.newPullParser();
            xpp.setInput( new InputStreamReader(is, "UTF-8") ); //inputstream 으로부터 xml 입력받기

            String tag; // 태그이름을 저장하기 위해 선언
            xpp.next(); // 무엇을 의미하는지 모르겠음, 그냥 다음단계로 진행이라는 의미인가,,

            int eventType= xpp.getEventType(); // 이벤트 타입이 뭔지 모르겠음 왜 int 이지...

            while( eventType != XmlPullParser.END_DOCUMENT ){ // 도큐멘트가 끝날 때 까지 실행하라는 의미..
                // 스위치가 이벤트 타입인데. case에 스타트들이 int 형식임
                // 정확히는 모르겠으나 내부적으로 어떤 xml의 형태들을 int로 해놓고
                // 그에 해당하는 숫자를 만나면 파싱을 시작하는 것 같음

                switch( eventType ){
                    case XmlPullParser.START_DOCUMENT:
                        buffer.append("파싱 시작...\n\n");
//                        Log.e("로그","1");
                        break;

                    case XmlPullParser.START_TAG:
                        tag= xpp.getName();//태그 이름 얻어오기
//                            Log.e("로그",tag);

                        if(tag.equals("item")) ;// 첫번째 검색결과
                        else if(tag.equals("popfile")){ // 이미지
                            // 피카소로 uri를 비트맵으로 변경 하고 배열 저장
                            xpp.next();
                            // picasso 라이브러리를 사용해서 uri로 파싱된 것을 비트맵으로 변경
//                            bitmap =  MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.parse(xpp.getText()));
//                            bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), Uri.parse(xpp.getText()));
//                            bitmap = Picasso.with(getApplicationContext()).load(Uri.parse(xpp.getText())).get();
//                            Log.e("비트맵", String.valueOf(bitmap));
                            arrlist_popfile.add(xpp.getText());
                        }
                        else if(tag.equals("careAddr")){ // 보호장소
                            xpp.next();
                            arrlist_careAddr.add(xpp.getText());
                }
                        else if(tag.equals("careNm")){ //보호소 이름
                            xpp.next();
                            arrlist_careNm.add(xpp.getText());
                        }
                        else if(tag.equals("kindCd")){ // 품종
                            xpp.next();
                            String a = xpp.getText();
                            String b = a.replace("[개]","");
                            arrlist_kindCd.add(b);
                        }
                        else if(tag.equals("specialMark")){ // 특징
                            xpp.next();
                            arrlist_specialMark.add(xpp.getText());
                        }
                        else if(tag.equals("age")){ // 나이
                            xpp.next();
                            arrlist_age.add(xpp.getText());
                        }
                        else if(tag.equals("orgNm")){ // 지역
                            xpp.next();
                            arrlist_orgNm.add(xpp.getText());
                        }
                        else if(tag.equals("sexCd")){ // 성별
                            xpp.next();
                            arrlist_sexCd.add(xpp.getText());
                        }
                        else if(tag.equals("careTel")){ // 전화번호
                            xpp.next();
                            arrlist_caretel.add(xpp.getText());
                        }
                        else if(tag.equals("desertionNo")){ // 공고번호
                            xpp.next();
                            arrlist_desertionNo.add(xpp.getText());
                        }
                        break;

                    case XmlPullParser.TEXT:
//                        Log.e("로그","7");
                        break;

                    case XmlPullParser.END_TAG:
                        tag= xpp.getName(); //태그 이름 얻어오기
                        if(tag.equals("item")) buffer.append("\n");// 첫번째 검색결과종료..줄바꿈
                        break;
                } // switch end
                eventType= xpp.next();
            } // while end

        } catch (Exception e) {
            // TODO Auto-generated catch blocke.printStackTrace();
        } // catch end
        return buffer.toString();//StringBuffer 문자열 객체 반환

    } // doinbackground end
//
    @Override
    protected void onPreExecute() {
        asyncDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        asyncDialog.setMessage("로딩중입니다..");
        // show dialog
        asyncDialog.show();
        super.onPreExecute();
    }


    @Override
    protected void onPostExecute(String s) {  // 마무리작업
        super.onPostExecute(s);
            Log.e("20이겠지..", "온포스트");
        for (int a=0; a<arrlist_age.size(); a++) { // 총합 배열에다가 값 다 넣기
//                    Log.e("엥", arrlist_filename.get(a));
            arrayList_main_dog.add(
                    new Main_dog_info(
                            arrlist_popfile.get(a),
                            arrlist_careAddr.get(a),
                            arrlist_careNm.get(a),
                            arrlist_kindCd.get(a),
                            arrlist_specialMark.get(a),
                            arrlist_age.get(a),
                            arrlist_orgNm.get(a),
                            arrlist_sexCd.get(a),
                            arrlist_caretel.get(a),
                            arrlist_desertionNo.get(a)
                    )
            );
        } // for end

        final Adater_main_info adater_main_info = new Adater_main_info(arrayList_main_dog);
        recyclerView_main.setAdapter(adater_main_info);
        adater_main_info.notifyDataSetChanged();
        asyncDialog.dismiss(); // 로딩 다이얼로그 종료
        // 노티파이시 포커스가 처음으로 가는 문제가 있어서
        // 아래와 같이 포커스를 설정해준다. ()괄호안에 포커스를 줄 포지션을 넣어주면 된다 .
        int focus = arrayList_main_dog.size()-11 ; // 포커스를 맞출 아이템 넘버
        recyclerView_main.getLayoutManager().scrollToPosition (focus);
    } // onPostExecute end

}// asyncktask end


} // main end
