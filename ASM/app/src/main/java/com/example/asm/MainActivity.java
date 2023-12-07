package com.example.asm;

import static com.example.asm.RetrofitRequest.getRetrofit;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.asm.Adapter.ComicAdapter;
import com.example.asm.Interface.ComicService;
import com.example.asm.Model.Comic;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ComicAdapter comicAdapter;
    private ComicService comicService;
    FloatingActionButton fab;
    Dialog dialog;
    Uri selectedImageUri;
    String imagePath;

    EditText edName,edTitle,edChapter,edt_status,edt_mkmoi,edt_nhaplaimk;
    Button btnSave,btnCancel;
    String oldpassword,newpassword,renewpass;
    String email,password;
    ImageView img_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        comicService=getRetrofit().create(ComicService.class);

        fab=findViewById(R.id.fab);
        recyclerView=findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        capnhatRCV();
        loadData();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                   openDialog();
            }
        });
    }

    public void capnhatRCV(){
        List<Comic> comicList=new ArrayList<>();
        comicAdapter =new ComicAdapter(comicList);
        comicAdapter.setMainActivity(this);
        recyclerView.setAdapter(comicAdapter);
    }

    public void loadData(){

        Call<List<Comic>> call=comicService.getComic();

        call.enqueue(new Callback<List<Comic>>() {
            @Override
            public void onResponse(Call<List<Comic>> call, Response<List<Comic>> response) {
                List<Comic> comicList=null;

                if (response.isSuccessful() && response.body() != null){
                    comicList=response.body();
                    comicAdapter.setComicList(comicList);
                    comicAdapter.notifyDataSetChanged();
                }else {

                }
            }

            @Override
            public void onFailure(Call<List<Comic>> call, Throwable t) {

            }
        });

    }

    public void deleteComic(String id){
        AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Delete");
        builder.setMessage("Bạn có muốn xóa không ?");
        builder.setCancelable(true);

        builder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Call call= comicService.deleteComic(id);
                        call.enqueue(new Callback() {
                            @Override
                            public void onResponse(Call call, Response response) {
                                if (response.isSuccessful()){
                                    loadData();
                                    Toast.makeText(getApplicationContext(),"Xoa comic thanh cong",Toast.LENGTH_LONG).show();
                                }else {

                                }
                            }

                            @Override
                            public void onFailure(Call call, Throwable t) {

                            }
                        });
                        capnhatRCV();
                        dialog.cancel();
                    }
                });
        builder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert=builder.create();
        builder.show();
    }

    public void editComic(String id,Comic comic){
        dialog=new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.dialog_themcomic);
        edName=dialog.findViewById(R.id.edname);
        edTitle=dialog.findViewById(R.id.edtitle);
        edChapter=dialog.findViewById(R.id.edchapter);
        img_image=dialog.findViewById(R.id.img_image);
        edt_status=dialog.findViewById(R.id.edstatus);
        btnCancel=dialog.findViewById(R.id.btnCancel);
        btnSave=dialog.findViewById(R.id.btnSave);


        edName.setText(comic.getName());
        edTitle.setText(comic.getTitle());
        edChapter.setText(comic.getChapter());
        if (comic.getImage()!=null){
            Picasso.get().load(comic.getImage()).into(img_image);
        }

        edt_status.setText(String.valueOf(comic.getStatus()));



        img_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=edName.getText().toString();
                String title=edTitle.getText().toString();
                String chapter=edChapter.getText().toString();
                String image="";
                int status=Integer.parseInt(edt_status.getText().toString());

                if (selectedImageUri!=null){
                    image=String.valueOf(selectedImageUri);
                }


                Comic comic=new Comic(title,name,chapter,image,status);
                Call<Comic> call=comicService.updateComic(id,comic);
                call.enqueue(new Callback<Comic>() {
                    @Override
                    public void onResponse(Call<Comic> call, Response<Comic> response) {
                        if (response.isSuccessful()){
                            loadData();
                            Toast.makeText(getApplicationContext(),"Sua comic thanh cong",Toast.LENGTH_LONG).show();
                            dialog.dismiss();
                        }else {

                        }
                    }

                    @Override
                    public void onFailure(Call<Comic> call, Throwable t) {

                    }
                });
            }
        });
        dialog.show();
    }
    public void openDialog(){
        dialog=new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.dialog_themcomic);
        edName=dialog.findViewById(R.id.edname);
        edTitle=dialog.findViewById(R.id.edtitle);
        img_image=dialog.findViewById(R.id.img_image);
        edChapter=dialog.findViewById(R.id.edchapter);
        edt_status=dialog.findViewById(R.id.edstatus);
        btnCancel=dialog.findViewById(R.id.btnCancel);
        btnSave=dialog.findViewById(R.id.btnSave);

        img_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=edName.getText().toString();
                String title=edTitle.getText().toString();
                String chapter=edChapter.getText().toString();
                String image="";
                int status= Integer.valueOf(edt_status.getText().toString());



                if (selectedImageUri!=null){
                    image=String.valueOf(selectedImageUri);
                }

                Comic comic=new Comic(title,name,chapter,image,status);

                Call<List<Comic>> call=comicService.addComic(comic);

                call.enqueue(new Callback<List<Comic>>() {
                    @Override
                    public void onResponse(Call<List<Comic>> call, Response<List<Comic>> response) {
                        if (response.isSuccessful()){
                            List<Comic> comicList=response.body();
                            comicAdapter.setComicList(comicList);
                            comicAdapter.notifyDataSetChanged();
                            Toast.makeText(getApplicationContext(),"Them comic thanh cong",Toast.LENGTH_LONG).show();
                            dialog.dismiss();
                        }else {
                            int statusCode = response.code();
                            Log.e("API Error", "Status Code: " + statusCode);
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Comic>> call, Throwable t) {
                        Log.e("API Error", "Request Failure: " + t.getMessage());
                    }
                });


            }
        });
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
           selectedImageUri = data.getData();
           Picasso.get().load(selectedImageUri).into(img_image);
        }
    }

    public String getPathFromUri(Context context, Uri uri) {
        String filePath = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME);
                filePath = cursor.getString(columnIndex);
                cursor.close();
            }
        } else if (uri.getScheme().equals("file")) {
            filePath = uri.getPath();
        }
        return filePath;
    }

}