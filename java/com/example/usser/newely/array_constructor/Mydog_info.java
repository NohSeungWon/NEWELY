package com.example.usser.newely.array_constructor;

public class Mydog_info {

    String popfile ; // 이미지
    String careAddr ; // 보호장소
    String careNm ; // 보호소이름
    String kindCd  ; // 품종
    String specialMark ; // 특징
    String age ; // 나이
    String orgNm ; // 지역
    String sexCd; // 성별
    String careTel; // 전화번호
    String desertionNo; //공고번호

    public Mydog_info(String popfile, String careAddr, String careNm, String kindCd, String specialMark , String age, String orgNm,
                         String sexCd, String careTel, String desertionNo){
        this.popfile = popfile;
        this.careAddr = careAddr;
        this.careNm = careNm;
        this.kindCd = kindCd;
        this.specialMark = specialMark;
        this.age = age;
        this.orgNm = orgNm;
        this.sexCd = sexCd;
        this.careTel = careTel;
        this.desertionNo = desertionNo;
    }

    public String getPopfile() {
        return popfile;
    }
    public String getCareAddr() {
        return careAddr;
    }
    public String getCareNm() {
        return careNm;
    }
    public String getKindCd() {
        return kindCd;
    }
    public String getSpecialMark() {
        return specialMark;
    }
    public String getAge() {
        return age;
    }
    public String getOrgNm() {
        return orgNm;
    }
    public String getSexCd() {
        return sexCd;
    }
    public String getCareTel() {
        return careTel;
    }
    public String getDesertionNo() {
        return desertionNo;
    }
}
