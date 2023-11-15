package com.example.asm;

import static com.example.asm.RetrofitRequest.getRetrofit;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ComicAdapter comicAdapter;
    private ComicService comicService;
    FloatingActionButton fab;
    Dialog dialog;

    EditText edName,edTitle,edChapter,edImage;
    Button btnSave,btnCancel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        comicService=getRetrofit().create(ComicService.class);
        Call<List<Comic>> call=comicService.getComic();

        call.enqueue(new Callback<List<Comic>>() {
            @Override
            public void onResponse(Call<List<Comic>> call, Response<List<Comic>> response) {
                List<Comic> comicList=null;

                if (response.isSuccessful() && response.body() != null){
                    comicList=response.body();
                    comicAdapter.setComicList(comicList);
                    Log.e("e", "Photo...." + comicList);
                    comicAdapter.notifyDataSetChanged();
                }else {
                    Log.e("e", "Photo...." + comicList);
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
        edImage=dialog.findViewById(R.id.edimage);
        btnCancel=dialog.findViewById(R.id.btnCancel);
        btnSave=dialog.findViewById(R.id.btnSave);


        edName.setText(comic.getName());
        edTitle.setText(comic.getTitle());
        edChapter.setText(comic.getChapter());
        edImage.setText(comic.getImage());

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
                String image=edImage.getText().toString();



                Comic comic=new Comic(title,name,chapter,image);
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
        edImage=dialog.findViewById(R.id.edimage);
        edChapter=dialog.findViewById(R.id.edchapter);
        btnCancel=dialog.findViewById(R.id.btnCancel);
        btnSave=dialog.findViewById(R.id.btnSave);



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
                String image=edImage.getText().toString();

                Comic comic=new Comic(title,name,chapter,image);
                Call<Comic> call=comicService.postComic(comic);
                call.enqueue(new Callback<Comic>() {
                    @Override
                    public void onResponse(Call<Comic> call, Response<Comic> response) {
                        if (response.isSuccessful()){
                            loadData();
                            Toast.makeText(getApplicationContext(),"Them comic thanh cong",Toast.LENGTH_LONG).show();
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


}