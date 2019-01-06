package com.example.usser.newely.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.usser.newely.R;
import com.example.usser.newely.array_constructor.Main_dog_info;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Adater_missing extends RecyclerView.Adapter<Adater_missing.MyviewHolder> {

    Bitmap bitmap;
    Context context;
    static ArrayList<Main_dog_info> arrayList_main_dog ;

    public Adater_missing(ArrayList<Main_dog_info> arrayList_main_dog){
        this.arrayList_main_dog = arrayList_main_dog;
    }

    final static Adater_main_info adater_main_info = new Adater_main_info(arrayList_main_dog);


    // 컨텍스트 메뉴 사용시 RecyclerView.ViewHolder 를 상속받은 클래스 내에서 리스너를 선언해야한다.
//       static Adater_add adater_add ;

    public class MyviewHolder extends RecyclerView.ViewHolder {
        // 어레이 리스트가 바뀌면 변할 값들 선언
        TextView tv_main_sexcd;


        public MyviewHolder(final View itemView) {
            super(itemView);
            tv_main_sexcd = itemView.findViewById(R.id.tv_main_sexcd);
        }
    }


    @Override
    public Adater_missing.MyviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyle_main_view,parent,false);
        return new Adater_missing.MyviewHolder(v);
    }


    public void onBindViewHolder(final Adater_missing.MyviewHolder holder, final int position) {
        final Adater_missing.MyviewHolder myviewHolder =(Adater_missing.MyviewHolder) holder;

        myviewHolder.tv_main_sexcd.setText("성별 : "+arrayList_main_dog.get(position).getSexCd());
//        Picasso.with(myviewHolder.img_main_filename.getContext())
//                .load(arrayList_main_dog.get(position).getPopfile())
//                .into(myviewHolder.img_main_filename);

    }


    public int getItemCount() {
        return arrayList_main_dog.size();
    }


}