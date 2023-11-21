package com.example.asm;

import static com.example.asm.RetrofitRequest.getRetrofit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.asm.Adapter.ComicAdapter;
import com.example.asm.Interface.ComicService;
import com.example.asm.Interface.UserService;
import com.example.asm.Model.Comic;
import com.example.asm.Model.User;
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
    UserService userService;
    User user=new User();

    EditText edName,edTitle,edChapter,edImage,edt_mkcu,edt_mkmoi,edt_nhaplaimk;
    Button btnSave,btnCancel;
    String oldpassword,newpassword,renewpass;
    String email,password;

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
                Call<List<Comic>> call=comicService.postComic(comic);

                call.enqueue(new Callback<List<Comic>>() {
                    @Override
                    public void onResponse(Call<List<Comic>> call, Response<List<Comic>> response) {
                        if (response.isSuccessful()){
                            //loadData();
                            List<Comic> comicList=response.body();
                            comicAdapter.setComicList(comicList);
                            Log.e("e", "Photo...." + comicList);
                            comicAdapter.notifyDataSetChanged();

                            Toast.makeText(getApplicationContext(),"Them comic thanh cong",Toast.LENGTH_LONG).show();
                            dialog.dismiss();
                        }else {

                        }
                    }

                    @Override
                    public void onFailure(Call<List<Comic>> call, Throwable t) {

                    }
                });

            }
        });
        dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_changepassword) {
            changePassword();
            return true;
        } else if (id == R.id.action_logout) {

            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void changePassword(){
        View dialogView = View.inflate(MainActivity.this,R.layout.dialog_chanepassword,null);
        dialog = new Dialog(MainActivity.this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(dialogView);

        Window window = dialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);



        edt_mkcu=dialog.findViewById(R.id.edt_mkcu);
        edt_mkmoi=dialog.findViewById(R.id.edt_mkmoi);
        edt_nhaplaimk= dialog.findViewById(R.id.edt_nhaplaimk);
        Button btnRegister= dialog.findViewById(R.id.btn_register);
        ImageButton img_Close=dialog.findViewById(R.id.img_Close);

        img_Close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 email=getIntent().getStringExtra("email");
                 oldpassword=edt_mkcu.getText().toString().trim();
                 newpassword=edt_mkmoi.getText().toString().trim();
                 renewpass=edt_nhaplaimk.getText().toString().trim();

                if(validate(oldpassword,newpassword,renewpass)){
                    changPass(email,newpassword);
                }

            }
        });
        dialog.show();
    }


    public boolean validate(String oldpassword, String newpassword,String renewpass){
        boolean check=true;
        password=getIntent().getStringExtra("password");
        if (!oldpassword.equals(password)){
            edt_mkcu.setError("Mat khau khong trung khop");
            edt_mkcu.requestFocus();
            check=false;
        }
        if (oldpassword.isEmpty()){
            edt_mkcu.setError("Khong de trong truong nay");
            edt_mkcu.requestFocus();
            check=false;
        }
        if (newpassword.isEmpty()){
            edt_mkmoi.setError("Khong de trong truong nay");
            edt_mkmoi.requestFocus();
            check=false;
        }
        if (renewpass.isEmpty()){
            edt_nhaplaimk.setError("Khong de trong truong nay");
            edt_nhaplaimk.requestFocus();
            check=false;
        }
        if (!newpassword.equals(renewpass)){
            edt_nhaplaimk.setError("Mat khau phai trung khop");
            edt_nhaplaimk.requestFocus();
            check=false;
        }

        return check;
    }

    public void changPass(String email,String newpassword){

        userService=getRetrofit().create(UserService.class);
        Call<User> call=userService.getUser(email,password);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
            user=response.body();
            user.setPassword(newpassword);

            Log.e("eeee",user.toString());
            userService.updateUser(user.getId(),user);
            Toast.makeText(MainActivity.this,"Doi mk thanh cong",Toast.LENGTH_LONG).show();
            dialog.dismiss();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });




    }

}