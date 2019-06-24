package com.example.xiaowu.supermarketcrm;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.gson.Gson;
import org.json.JSONObject;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginActivity extends Activity {

    String jsonData=null;
    Context context=this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        Button loginButton=findViewById(R.id.login);
        Button resetButton=findViewById(R.id.reset);
        final EditText user_name=findViewById(R.id.user_name);
        final EditText user_password=findViewById(R.id.user_password);

         final Handler handler =new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if (msg.what==0x11)
                {
                    if (jsonData.equals("fail")){

                        Toast.makeText(LoginActivity.this, "用户不存在，请确认您的密码和用户名是否正确", Toast.LENGTH_LONG).show();
                    }
                    else {
                        Gson gson=new Gson();
                        User user=gson.fromJson(jsonData,User.class);//Gson直接把经过转换的字符串数据映射到我们的User对象里去了
                        String name=user.getUser_name();
                        String pass=user.getUser_password();
                        String user_id=user.getUser_id().toString();
                        System.out.println("login界面的userid为："+user_id);
                        Intent i = new Intent(context, HomeActivity.class);
                        i.putExtra("user_id",user_id);
                        startActivity(i);
                    }
                }
            }
        };

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Mythread thread=new Mythread();
                thread.start();
            }

            class  Mythread extends Thread{
                @Override
                public void run() {

                    System.out.println("执行了run方法");
                    String target="http://10.0.2.2:8080/super-crm/loginMobile.json";
                    String u_name=user_name.getText().toString();
                    String u_password=user_password.getText().toString();
                    HttpURLConnection conn= null;
                    try {
                        URL url=new URL(target);
                        JSONObject jsonObject=new JSONObject();
                        jsonObject.put("user_name",u_name);
                        jsonObject.put("user_password",u_password);
                        String content=String.valueOf(jsonObject);
                        conn=(HttpURLConnection)url.openConnection();
                        conn.setConnectTimeout(5000);
                        conn.setDoOutput(true);
                        conn.setRequestMethod("POST");
                        conn.setRequestProperty("ser-Agent", "Fiddler");
                        conn.setRequestProperty("Content-Type","application/json");
                        //写输出流，将要转的参数写入流里
                        OutputStream os=conn.getOutputStream();

                        os.write(content.getBytes()); //字符串写进二进流
                        os.close();
                        int code=conn.getResponseCode();
                        if (code==200){
                            InputStream inputStream=conn.getInputStream();
                            jsonData= NetUtils.readBytes(inputStream);//把我们得到byte流转换成字符串，NetUtils就是干这样的事
                                Message ms=handler.obtainMessage();
                                ms.what=0x11;
                                handler.sendMessage(ms);

                        }
                    }catch (Exception e) {
                        e.printStackTrace();
                    }


            }
        }


        });
    }
}

