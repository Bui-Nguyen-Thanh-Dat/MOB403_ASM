package com.example.asm.Fragment;

import static com.example.asm.RetrofitRequest.getRetrofit;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.FragmentTransaction;

import com.example.asm.Interface.UserService;
import com.example.asm.MainActivity;
import com.example.asm.MainActivity2;
import com.example.asm.Model.User;
import com.example.asm.R;
import com.google.android.material.snackbar.Snackbar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
public class LoginFragment extends androidx.fragment.app.Fragment implements View.OnClickListener
{
    private AppCompatButton btn_login;
    private EditText edt_email, edt_password;
    private TextView tv_register;
    private ProgressBar progressBar;
    private SharedPreferences pref;

    UserService userService;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container,
                false);
        initViews(view);
        return view;
    }
    private void initViews(View view) {
        pref = getActivity().getPreferences(0);
        btn_login = (AppCompatButton) view.findViewById(R.id.btn_login);
        edt_email = (EditText) view.findViewById(R.id.et_email);
        edt_password = (EditText) view.findViewById(R.id.et_password);
        tv_register = (TextView) view.findViewById(R.id.tv_register);
        progressBar = (ProgressBar) view.findViewById(R.id.progress);
        tv_register.setOnClickListener(this);
        btn_login.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.btn_login){
            String email = edt_email.getText().toString();
            String password = edt_password.getText().toString();
            if(!email.isEmpty() && !password.isEmpty()){
                progressBar.setVisibility(View.VISIBLE);
                loginProcess(email,password);
            }else {
                Snackbar.make(getView(),"Fields are empty !",Snackbar.LENGTH_LONG).show();
            }
        }
        if (view.getId()==R.id.tv_register){
            Snackbar.make(getView(),"Hello",Snackbar.LENGTH_LONG).show();
            goToRegister();
        }



    }
    private void loginProcess(String email, String password){
        userService=getRetrofit().create(UserService.class);
        Call<User> call=userService.getUser(email,password);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()) {
                    Toast.makeText(getContext(), "Login successfull", Toast.LENGTH_LONG).show();
                    Intent intent=new Intent(getContext(),MainActivity.class);
                    intent.putExtra("email",email);
                    intent.putExtra("password",password);
                    startActivity(intent);
                }else {
                    Toast.makeText(getContext(), "Login fail", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });

    }
    private void goToRegister() {
        RegisterFragment login = new RegisterFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_frame,login);
        ft.commit();
    }
    private void goToProfile() {
//        RegisterFragment login = new RegisterFragment();
//        FragmentTransaction ft = getFragmentManager().beginTransaction();
//        ft.replace(R.id.fragment_frame,login);
//        ft.commit();
    }
}