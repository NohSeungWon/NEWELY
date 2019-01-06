package com.example.usser.newely.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.usser.newely.Interface.Server_connect;
import com.example.usser.newely.R;
import com.example.usser.newely.array_constructor.Main_dog_info;
import com.example.usser.newely.array_constructor.Mydog_info;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Adater_mydog extends RecyclerView.Adapter<Adater_mydog.MyviewHolder> {

    Bitmap bitmap;
    Context context;
    static ArrayList <Mydog_info> arrayList_mydog;

    Retrofit retrofit;
    Server_connect server_connect;

    String user_info;
    String desertionno; // 공고번호


    public Adater_mydog(ArrayList<Mydog_info> arrayList_mydog){
        this.arrayList_mydog = arrayList_mydog;
    }

    final static Adater_mydog adater_mydog = new Adater_mydog(arrayList_mydog);



    public class MyviewHolder extends RecyclerView.ViewHolder {
        // 어레이 리스트가 바뀌면 변할 값들 선언
        TextView tv_mydog_kindcd_carenm_recycle;
        TextView tv_mydog_age_sexcd_recycle;
        TextView tv_mydog_desertionNo_recycle;
        TextView tv_mydog_specialMark_recycle;
        TextView tv_mydog_careAddr_recycle;
        TextView tv_mydog_careTel_recycle;

        Button btn_mydog_delete;
        ImageView img_mydog_read_recycle;


        public MyviewHolder(final View itemView) {
            super(itemView);
            tv_mydog_kindcd_carenm_recycle = itemView.findViewById(R.id.tv_mydog_kindcd_carenm_recycle);
            tv_mydog_age_sexcd_recycle = itemView.findViewById(R.id.tv_mydog_age_sexcd_recycle);
            tv_mydog_desertionNo_recycle = itemView.findViewById(R.id.tv_mydog_desertionNo_recycle);
            tv_mydog_specialMark_recycle = itemView.findViewById(R.id.tv_mydog_specialMark_recycle);
            tv_mydog_careAddr_recycle = itemView.findViewById(R.id.tv_mydog_careAddr_recycle);
            tv_mydog_careTel_recycle = itemView.findViewById(R.id.tv_mydog_careTel_recycle);
            img_mydog_read_recycle = itemView.findViewById(R.id.img_mydog_read_recycle);

            // 삭제 버튼 누르면 아이템 삭제하기
            // 클릭시 얻는 포지션 값을 공고번호 캐치하고
            // 아이디 값과 캐치한 공고번호로 서버에 보내 삭제시킨다.
            btn_mydog_delete = itemView.findViewById(R.id.btn_mydog_delete);

            btn_mydog_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SharedPreferences share_id = view.getContext().getSharedPreferences("user_info",view.getContext().MODE_PRIVATE); // 로그인 한 후 저장된 쉐어 불러오기
                    SharedPreferences.Editor edit_share_id = share_id.edit();  //에디트 선언
                    user_info = share_id.getString("info", null); //쉐어값이 있다면 키값과 함께 넣고, 없다면 디폴트값은 null
                    // 파서로 id 값만 뽑아낸다.
                    JsonParser parser = new JsonParser();
                    JsonElement element = parser.parse(user_info);
                    user_info = element.getAsJsonObject().get("id").getAsString();
                    // 서버에서 데이터를 알맞게 픽 할 수 있는 공고 번호 가져오고
                    desertionno = arrayList_mydog.get(getAdapterPosition()).getDesertionNo();
                    // 아이템 지우고
                    arrayList_mydog.remove(getAdapterPosition());
                    mydog_delete(); // 서버에서도 지우고 난 후
                    notifyDataSetChanged(); // 갱신 아래걸로 하면 오류남 전체 갱신을 해줘야함
//                    notifyItemChanged(getAdapterPosition()); // 갱신해준다.
                }
            });
        }
    }


    @Override
    public Adater_mydog.MyviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_mydog_view,parent,false);
        return new Adater_mydog.MyviewHolder(v);
    }


    public void onBindViewHolder(final Adater_mydog.MyviewHolder holder, final int position) {
        final Adater_mydog.MyviewHolder myviewHolder =(Adater_mydog.MyviewHolder) holder;
//        Log.e("로그가 나오나", "로그");

        myviewHolder.tv_mydog_kindcd_carenm_recycle.setText(arrayList_mydog.get(position).getKindCd()+" ("+arrayList_mydog.get(position).getCareNm()+")");
        myviewHolder.tv_mydog_age_sexcd_recycle.setText(arrayList_mydog.get(position).getSexCd()+" / "+ arrayList_mydog.get(position).getAge());
        myviewHolder.tv_mydog_desertionNo_recycle.setText(arrayList_mydog.get(position).getDesertionNo());
        myviewHolder.tv_mydog_specialMark_recycle.setText(arrayList_mydog.get(position).getSpecialMark());
        myviewHolder.tv_mydog_careAddr_recycle.setText(arrayList_mydog.get(position).getCareAddr());
        myviewHolder.tv_mydog_careTel_recycle.setText(arrayList_mydog.get(position).getCareTel());
//        myviewHolder.img_main_filename.setImageBitmap(bitmap);
        Picasso.with(myviewHolder.img_mydog_read_recycle.getContext())
                .load(arrayList_mydog.get(position).getPopfile())
                .into(myviewHolder.img_mydog_read_recycle);

    }


    public int getItemCount() {
        return arrayList_mydog.size();
    }


    /**
     *  관심동물 삭제 버튼 눌렀을 때
     */
    public void mydog_delete(){

        retrofit = new Retrofit.Builder().baseUrl(Server_connect.URL).build();
        server_connect = retrofit.create(Server_connect.class);
        Call<ResponseBody> connect = server_connect.mydog_delete(user_info,desertionno);
        connect.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String str = response.body().string();
//                    Log.e("mydog_delete","성공");
//                    Log.e("mydog_delete",str);
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("익셉션", String.valueOf(e));
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("보내기","실패");
            }
        });
    } //mydog_delete end

}
