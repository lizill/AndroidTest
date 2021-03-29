package com.example.chatapp2021;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    EditText etId, etPassword;
    ProgressBar progressBar;
    String userID, userPW;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etId = (EditText) findViewById(R.id.etID);
        etPassword = (EditText) findViewById(R.id.etPassword);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);

        // getString의 첫 번째 인자는 저장될 키, 두 번쨰 인자는 값입니다.
        // 첨엔 값이 없으므로 키값은 원하는 것으로 하시고 값을 null을 줍니다.
        String loginId = auto.getString("inputId",null);
        String loginPwd = auto.getString("inputPwd",null);

        if(loginId !=null && loginPwd != null) {

            Response.Listener<String> responseLister = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        boolean success = jsonResponse.getBoolean("success");
                        if (success) {
                            Toast.makeText(MainActivity.this, loginId +"님 자동로그인 입니다.", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MainActivity.this, ChatActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                            builder.setMessage("계정을 다시 확인하세요.")
                                    .setNegativeButton("다시 시도", null)
                                    .create()
                                    .show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            LoginRequest loginRequest = new LoginRequest(loginId, loginPwd, responseLister);
            RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
            queue.add(loginRequest);
        }
        //id와 pwd가 null이면 Mainactivity가 보여짐.
        else if(loginId == null && loginPwd == null) {

            Button btnLogin = (Button) findViewById(R.id.btnLogin);
            btnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    userID = etId.getText().toString();
                    userPW = etPassword.getText().toString();

                    if (userID.isEmpty()) {
                        Toast.makeText(MainActivity.this, "Please insert Email", Toast.LENGTH_LONG).show();
                        return;
                    }
                    if (userPW.isEmpty()) {
                        Toast.makeText(MainActivity.this, "Please insert Password", Toast.LENGTH_LONG).show();
                        return;
                    }
                    progressBar.setVisibility(View.VISIBLE);

                    Response.Listener<String> responseLister = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean success = jsonResponse.getBoolean("success");
                                if (success) {
                                    //auto의 loginId와 loginPwd에 값을 저장
                                    SharedPreferences.Editor autoLogin = auto.edit();
                                    autoLogin.putString("inputId", userID);
                                    autoLogin.putString("inputPwd", userPW);
                                    autoLogin.commit();
                                    Intent intent = new Intent(MainActivity.this, GroupActivity.class);
                                    MainActivity.this.startActivity(intent);
                                    finish();
                                } else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                    builder.setMessage("계정을 다시 확인하세요.")
                                            .setNegativeButton("다시 시도", null)
                                            .create()
                                            .show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    LoginRequest loginRequest = new LoginRequest(userID, userPW, responseLister);
                    RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                    queue.add(loginRequest);
//                Toast.makeText(MainActivity.this, "Login", Toast.LENGTH_LONG).show();
                }
            });
        }

        Button btnRegister = (Button) findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userID = etId.getText().toString();
                String userPassword = etPassword.getText().toString();
                String userName = "testUser";

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(userID.isEmpty()) {
                            Toast.makeText(MainActivity.this, "Please insert Email", Toast.LENGTH_LONG).show();
                            return;
                        }
                        if(userPassword.isEmpty()) {
                            Toast.makeText(MainActivity.this, "Please insert Password", Toast.LENGTH_LONG).show();
                            return;
                        }
                        progressBar.setVisibility(View.VISIBLE);

                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if(success) {
                                progressBar.setVisibility(View.GONE);
                                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                builder.setMessage("회원 등록에 성공했습니다.")
                                        .setPositiveButton("확인", null)
                                        .create()
                                        .show();
                            }
                            else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                builder.setMessage("회원 등록에 실패했습니다.")
                                        .setNegativeButton("다시 시도", null)
                                        .create()
                                        .show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                        RegisterRequest registerRequest = new RegisterRequest(userID, userPassword, userName, responseListener);
                        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                        queue.add(registerRequest);
//                Toast.makeText(MainActivity.this, "Email : " + stEmail + ", Password : " + stPassword, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
//        updateUI(currentUser);
    }
}