package com.example.xiaowu.supermarketcrm;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class ListByRequestActivity extends Activity {
    EditText selectbyname,selectbyjinhuodanhao;
    Spinner selectbycategory,selectbygonghuoshang;
    Button select,exit;
    Product prt;
    Handler handler;
    String jsonData="";
    String selectbyRequire="";
    Context context=this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_by_request);
        selectbyname=findViewById(R.id.selectprtName_input_ud);
        selectbyjinhuodanhao=findViewById(R.id.selectprtJinhuodanhao_input_ud);
        selectbycategory=findViewById(R.id.selectprtCategory_input_ud);
        selectbygonghuoshang=findViewById(R.id.selectprtGongyingshang_input_id);
        select=findViewById(R.id.selectbyRequring);
        exit=findViewById(R.id.selectExit);

        //加载下拉框信息
        new Thread(new Runnable() {
            @Override
            public void run() {
                String target="http://10.0.2.2:8080/super-crm/product/Elements.json";
                HttpURLConnection conn= null;
                try {
                    URL url=new URL(target);
                    conn=(HttpURLConnection)url.openConnection();
                    conn.setConnectTimeout(5000);
                    conn.setDoOutput(true);
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("ser-Agent", "Fiddler");
                    conn.setRequestProperty("Content-Type","application/json");
                    //写输出流，将要转的参数写入流里
                    int code=conn.getResponseCode();
                    if (code==200){
                        System.out.println("响应成功");
                        InputStream inputStream=conn.getInputStream();
                        jsonData= NetUtils.readBytes(inputStream);  //把我们得到byte流转换成字符串，NetUtils就是干这样的事
                        Message ms=handler.obtainMessage();
                        ms.what=0x11;
                        handler.sendMessage(ms);
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }).start();
        handler =new Handler()
        {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what==0x11)
                {
                    Gson gson=new Gson();
                    ArrayList<ArrayList<String>> list = gson.fromJson(jsonData, new TypeToken<ArrayList<ArrayList<String>>>(){}.getType());
                    ArrayList<String> catList=list.get(0);
                    ArrayList<String> gongList=list.get(1);
                    ArrayList<String> jijianList=list.get(2);
                    ArrayAdapter<String> catAdapter =new ArrayAdapter<>(
                            context,android.R.layout.simple_spinner_dropdown_item,catList
                    );
                    selectbycategory.setAdapter(catAdapter);
                    selectbycategory.setSelection(0);
                    ArrayAdapter<String> gongAdapter =new ArrayAdapter<>(
                            context,android.R.layout.simple_spinner_dropdown_item,gongList
                    );
                    selectbygonghuoshang.setAdapter(gongAdapter);
                    selectbygonghuoshang.setSelection(0);
                }
                else if(msg.what==0x22)
                {
                    Intent intent=new Intent(context,AllProductActivity.class);
                    intent.putExtra("selectbyRequireData",selectbyRequire);
                    startActivity(intent);

                }
            }
        };
        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                         prt=new Product();
                        if (TextUtils.isEmpty(selectbyname.getText())==false){prt.setProduct_name(selectbyname.getText().toString());}
                        if (TextUtils.isEmpty(selectbyjinhuodanhao.getText())==false){prt.setProduct_jinghuodanhao(selectbyjinhuodanhao.getText().toString());}
                        prt.setProduct_category(selectbycategory.getSelectedItem().toString());
                        prt.setProduct_gonghuoshang(selectbygonghuoshang.getSelectedItem().toString());

                        String target="http://10.0.2.2:8080/super-crm/product/listbyRequest.json";
                        HttpURLConnection conn= null;
                        try {
                            URL url=new URL(target);
                            Gson gson=new Gson();
                            String content=gson.toJson(prt);
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
                                selectbyRequire= NetUtils.readBytes(inputStream);//按需查询得到的数据
                                Message ms=handler.obtainMessage();
                                ms.what=0x22;
                                handler.sendMessage(ms);

                            }
                        }catch (Exception e) {
                            e.printStackTrace();
                        }


                    }


                }).start();
            }
        });
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListByRequestActivity.this.onDestroy();
                ListByRequestActivity.this.finish();
            }
        });
    }
}
