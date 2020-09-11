package com.example.myapplication;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity {
    TextView txt2,textView;
    EditText editEmail,editPass;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btnOk= (Button) findViewById(R.id.buttonOk);

        txt2 = findViewById(R.id.txt2);
        textView = findViewById(R.id.txt);
        editEmail = findViewById(R.id.editEmail);
        editPass = findViewById(R.id.editPass);
        View.OnClickListener onClickListenert = new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String email;
                String pass;

                email = String.valueOf(editEmail.getText());
                pass = String.valueOf(editPass.getText());

                doAuth(email,pass);
            }
        };

        btnOk.setOnClickListener(onClickListenert);

    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    void doConnect(String token){
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url("http://109.68.213.41:8000/transactions")
                .method("GET", null)
                .addHeader("Authorization", "Bearer " +token)
                .build();
        try {
            Response response = client.newCall(request).execute();
            txt2.setText(response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    void doAuth(String email, String password){
        Token token = new Token();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\r\n  \"email\": \""+email+"\",\r\n  \"password\":\""+password+"\"\r\n}");
        Request request = new Request.Builder()
                .url("http://109.68.213.41:8000/auth/login")
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .build();
        try( Response response = client.newCall(request).execute()){
            Gson g = new Gson();

            Token t= g.fromJson(response.body().string(), Token.class);
            textView.setText(t.getAccessToken());
            doConnect(t.getAccessToken());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}