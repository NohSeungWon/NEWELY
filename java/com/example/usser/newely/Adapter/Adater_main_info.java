package com.example.usser.newely.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.usser.newely.MainActivity;
import com.example.usser.newely.R;
import com.example.usser.newely.array_constructor.Main_dog_info;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;

public class Adater_main_info extends RecyclerView.Adapter<Adater_main_info.MyviewHolder> {

    Bitmap bitmap;
    Context context;
    static ArrayList <Main_dog_info> arrayList_main_dog ;

    public Adater_main_info(ArrayList<Main_dog_info> arrayList_main_dog){
        this.arrayList_main_dog = arrayList_main_dog;
    }

    final static Adater_main_info adater_main_info = new Adater_main_info(arrayList_main_dog);


    // 컨텍스트 메뉴 사용시 RecyclerView.ViewHolder 를 상속받은 클래스 내에서 리스너를 선언해야한다.
//       static Adater_add adater_add ;



    public class MyviewHolder extends RecyclerView.ViewHolder {
        // 어레이 리스트가 바뀌면 변할 값들 선언
        TextView tv_main_sexcd;
        TextView tv_main_kindcd;
        TextView tv_main_age;
        TextView tv_main_orgNm;
        ImageView img_main_filename;


        public MyviewHolder(final View itemView) {
            super(itemView);
            tv_main_sexcd = itemView.findViewById(R.id.tv_main_sexcd);
            tv_main_kindcd = itemView.findViewById(R.id.tv_main_kindcd);
            tv_main_age = itemView.findViewById(R.id.tv_main_age);
            tv_main_orgNm = itemView.findViewById(R.id.tv_main_orgNm);
            img_main_filename = itemView.findViewById(R.id.img_main_filename);
        }
    }


    @Override
    public MyviewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyle_main_view,parent,false);
        return new MyviewHolder(v);
    }


    public void onBindViewHolder(final MyviewHolder holder, final int position) {
        final MyviewHolder myviewHolder =(MyviewHolder) holder;
//        Log.e("로그가 나오나", "로그");

        myviewHolder.tv_main_sexcd.setText("성별 : "+arrayList_main_dog.get(position).getSexCd());
        myviewHolder.tv_main_kindcd.setText("품종 : "+arrayList_main_dog.get(position).getKindCd());
        myviewHolder.tv_main_age.setText("나이 : "+arrayList_main_dog.get(position).getAge());
        myviewHolder.tv_main_orgNm.setText("위치 : "+arrayList_main_dog.get(position).getOrgNm());
//        myviewHolder.img_main_filename.setImageBitmap(bitmap);
        Picasso.with(myviewHolder.img_main_filename.getContext())
                .load(arrayList_main_dog.get(position).getPopfile())
                .into(myviewHolder.img_main_filename);

    }


    public int getItemCount() {
        return arrayList_main_dog.size();
    }


}
