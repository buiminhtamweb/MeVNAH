package com.example.toant.googlemap.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.toant.googlemap.R;
import com.example.toant.googlemap.utils.NetworkProcessor;
import com.example.toant.googlemap.utils.Utils;

import java.util.ArrayList;
import java.util.LinkedHashSet;

public class LoginActivity extends BaseActivity {

    private EditText edtUsername,edtPassword;
    private Button btnLogin;

    @Override
    public void initUI() {
        setUpContent();
        resetLayout();

    }

    private void resetLayout() {
        Intent intent = getIntent();
        if(intent!=null){
            edtUsername.setText("");
            edtPassword.setText("");
        }
    }

    @SuppressLint("ResourceType")
    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_login;
    }

    private void setUpContent() {
        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);
    }

    private void logIn(String username, String password){
        NetworkProcessor.GET(this,ID_API_LOGIN, API_LOGIN+"?username="+username+"&password="+password, null,true,this,false);
    }

    @Override
    public void onClick(View v) {

        if(Utils.isConnectionAvailable(this)){
            String username = edtUsername.getText().toString();
            String password = edtPassword.getText().toString();
            if(username.isEmpty()){
                Toast.makeText(this, getString(R.string.msg_empty_username), Toast.LENGTH_SHORT).show();
            }else if(password.isEmpty()){
                Toast.makeText(this, getString(R.string.msg_empty_password), Toast.LENGTH_SHORT).show();
            }else {
                logIn(username,password);
            }
        }else {
            Utils.showMessageOK(this,getString(R.string.msg_sorry_no_internet),null,false);
        }

//        logIn("bvlam","123456");
        super.onClick(v);
    }

    @Override
    public void downloadSuccess(int processId, Object data) {
        String str = (String) data;
        if(str.toLowerCase().contains("error")){
            Toast.makeText(this, getString(R.string.msg_incorrect_login), Toast.LENGTH_SHORT).show();
        }else {
            startActivity(new Intent(this,MapsActivity.class));
        }
        super.downloadSuccess(processId, data);
    }

    @Override
    public void downloadError(int processId, String msg) {
//        Toast.makeText(this, getString(R.string.msg_incorrect_login), Toast.LENGTH_SHORT).show();
        super.downloadError(processId, msg);
    }
}
