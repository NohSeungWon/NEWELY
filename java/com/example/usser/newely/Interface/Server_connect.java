package com.example.usser.newely.Interface;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface Server_connect {

    public static final String URL = "http://106.10.33.230/Newely/";  // 로그인

//    @GET(“api주소”)
    // call이 주고받을 객체
//    Call<ResponseBody>함수이름(@Query(“변수이름”), 안드로이드에서 보낼 변수)
    // ResponseBody 이 부분은 받아올 데이터의 형태

    @GET("login.php") // 로그인시
    Call<ResponseBody> login(@Query("id") String id, @Query("password") String password);

    @FormUrlEncoded
    @POST("distinguish_user.php") // 로그인 체크
    Call<ResponseBody> distinguish (@Field("id") String id);

    @FormUrlEncoded
    @POST("mydog_send.php") // 관심동물 체크시
    Call<ResponseBody> mydog_send (@Field("id") String id,
                                   @Field("desertionno") String desertionno,
                                   @Field("specialMark") String specialMark,
                                   @Field("age") String age,
                                   @Field("careaddr") String careaddr,
                                   @Field("sexcd") String sexcd,
                                   @Field("caretel") String caretel,
                                   @Field("kindcd") String kindcd,
                                   @Field("carenm") String carenm,
                                   @Field("orgnm") String orgnm,
                                   @Field("popfile") String popfile
                                   );

    @FormUrlEncoded
    @POST("mydog_delete.php") // 관심동물 체크해제시
    Call<ResponseBody> mydog_delete (@Field("id") String id,
                                   @Field("desertionno") String desertionno
                                   );

    @FormUrlEncoded
    @POST("mydog_send_data.php") // mydog 페이지 관심동물 모아보기
    Call<ResponseBody> mydog_get (@Field("id") String id);

    @FormUrlEncoded
    @POST("mydog_all_delete.php") // mydog 페이지에서 전체 삭제버튼 누를 시
    Call<ResponseBody> mydog_all_delete (@Field("id") String id);

    @GET("create_user.php") // 일반 사용자 회원가입시
    Call<ResponseBody> create (@Query("id") String id,
                                @Query("nickname") String nickname,
                                @Query("password") String password,
                                @Query("distinguish") int distinguish);

    @GET("create_user.php") // 보호소 회원가입시
    Call<ResponseBody> create_protect (@Query("id") String id,
                               @Query("nickname") String nickname,
                               @Query("password") String password,
                               @Query("distinguish") int distinguish,
                               @Query("adress") String adress);

    // 유기동물 api url
    public static final String pet_URL = "http://openapi.animal.go.kr/openapi/service/rest/abandonmentPublicSrvc/";  // 로그인

    // 필수로 넣어야 하는 정보 들
    // 종료일을 항상 최신으로 유지하기 위해 오늘 날짜를 셋팅

    String key = "";
    String bgnde = "&bgnde=20180101" ; // 보호날짜 시작일
    String endde = "&endde=20181231"; // 종료일
    String upkind = "&upkind=41700"; // 축종종류
    String state = "&state=notice"; // 상태 공고 중 = notice
//    String page = "&page=1";
//    String startpage = "&startpage=";
    String numOfRows="&numOfRows=10"; // 페이지당 보여줄 개수
//    String pagesize ="&pageSize=";


}
