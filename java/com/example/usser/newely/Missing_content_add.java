package com.example.usser.newely;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.usser.newely.databinding.ActivityMissingContentAddBinding;

import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Missing_content_add extends AppCompatActivity {
    ActivityMissingContentAddBinding binding;

    private String currentPhotoPath;//실제 사진 파일 경로
    String mImageCaptureName;//이미지 이름
    Uri photoUri;
    int pic_num = 111;
    int select_num = 222;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_missing_content_add);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_missing_content_add); // 데이터 바인딩.

        binding.btnFindImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choice_img(); // 이미지를 올릴 때 갤러리에서 가져올지 바로 사진을 찍을지 선택하게 하는 다이얼로그
            } // onclick end
        }); // findimg end

        Button btnfindmap = (Button)findViewById(R.id.btn_find_map);
        btnfindmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),daum_adress_api.class);
                startActivityForResult(intent,3000);
            }
        });

        binding.radioState.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.radio_missing:
//                        Log.e("되나요", String.valueOf(binding.radioMissing.getText()));
                }
            }
        });

    } // oncreate end

    /** 올릴 사진을 앨범에서 가져올건지 사진으로 찍을건지 결정하라는 다이얼로그 */
    void choice_img()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("경로를 선택하세요");
//        builder.setMessage("AlertDialog Content");
        builder.setPositiveButton("앨범에서 선택",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) { // 앨범선택
                        selectGallery();
                    }
                });
        builder.setNegativeButton("사진찍기",
                new DialogInterface.OnClickListener() { // 사진선택
                    public void onClick(DialogInterface dialog, int which) {
                        picphoto();
                    }
                });
        builder.show();
    }

    /** 앨범이나 사진을 선택하고 난 후 결과를 처리하는 곳 */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == pic_num) {
                binding.contentImgv.setImageURI(photoUri);
            }
            else if (requestCode == select_num) {
                String a = getRealPathFromURI(data.getData());
                binding.contentImgv.setImageURI(Uri.parse(a));
            }
            else if(requestCode == 3000) {
                binding.etFindPlaceSet.setText(data.getStringExtra("adress"));
            }
        }
    }

    /** 갤러리에서 가져오기를 선택한 경우 */
    private void selectGallery() {
//        Log.e("어디냐","셀렉트");
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, select_num);
    }

    /** 절대경로로 변환시키는 메소드 */
    private String getRealPathFromURI(Uri contentUri) {
        int column_index=0;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if(cursor.moveToFirst()){
            column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        }

        return cursor.getString(column_index);
    }

    /** 사진을 찍어서 올릴 경우 */
    private void picphoto() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (intent.resolveActivity(getPackageManager()) != null) {
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException ex) {

                }
                if (photoFile != null) {
                    photoUri = FileProvider.getUriForFile(this, getPackageName(), photoFile);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                    startActivityForResult(intent, pic_num);
                }
            }
        }
    }


    private File createImageFile() throws IOException {
        File dir = new File(Environment.getExternalStorageDirectory() + "/path/");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        mImageCaptureName = timeStamp + ".png";
        File storageDir = new File(Environment.getExternalStorageDirectory().getAbsoluteFile() + "/path/"
                + mImageCaptureName);
        currentPhotoPath = storageDir.getAbsolutePath();
        return storageDir;
    }



}// main end
