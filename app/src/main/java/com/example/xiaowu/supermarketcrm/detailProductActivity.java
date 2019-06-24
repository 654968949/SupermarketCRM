package com.example.xiaowu.supermarketcrm;

import android.app.Activity;
import android.content.Context;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class detailProductActivity extends Activity {
    Context context=this;
    Handler handler;
    String json="";
    Product prt;
    EditText prt_name,prt_saleprice,prt_jinjia,prt_quantities,prt_jinhuodan;
    Spinner prt_category,prt_jijianfangshi,prt_gongyingshang;
    Button updateButton,exitButton;
    String jsonData="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_details);
        json=getIntent().getStringExtra("person_data");
        Gson gson= new Gson();
        prt=gson.fromJson(json,Product.class);
        System.out.println("商品详情页数据为："+json);
        prt_name=findViewById(R.id.prtName_input_ud);
        prt_saleprice=findViewById(R.id.prtSaleprice_input_ud);
        prt_jinjia=findViewById(R.id.prtJinjia_input_ud);
        prt_quantities=findViewById(R.id.prtQuantities_input_ud);
        prt_jinhuodan=findViewById(R.id.prtJinhuodanhao_input_ud);
        prt_category=findViewById(R.id.prtCategory_input_ud);
        prt_jijianfangshi=findViewById(R.id.prtJijianfangshi_input_ud);
        prt_gongyingshang=findViewById(R.id.prtGongyingshang_input_id);
        updateButton=findViewById(R.id.save_id);
        exitButton=findViewById(R.id.exit_id);

        prt_name.setText(prt.getProduct_name());
        prt_saleprice.setText(String.valueOf(prt.getProduct_saleprice()));
        prt_jinjia.setText(String.valueOf(prt.getProduct_jinghuoprice()));
        prt_quantities.setText(String.valueOf(prt.getProduct_quantities()));
        prt_jinhuodan.setText(String.valueOf(prt.getProduct_jinghuodanhao()));

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
                    prt_category.setAdapter(catAdapter);
                    prt_category.setSelection(catList.indexOf(prt.getProduct_category()));
                    ArrayAdapter<String> gongAdapter =new ArrayAdapter<>(
                            context,android.R.layout.simple_spinner_dropdown_item,gongList
                    );
                    prt_gongyingshang.setAdapter(gongAdapter);
                    prt_gongyingshang.setSelection(gongList.indexOf(prt.getProduct_gonghuoshang()));
                    ArrayAdapter<String> jijianAdapter =new ArrayAdapter<>(
                            context,android.R.layout.simple_spinner_dropdown_item,jijianList
                    );
                    prt_jijianfangshi.setAdapter(jijianAdapter);
                    prt_jijianfangshi.setSelection(jijianList.indexOf(prt.getProduct_jijianfanshi()));
                }
                else if(msg.what==0x22)
                {
                    if (jsonData.equals("OK")) {
                        Toast.makeText(context, "修改成功", Toast.LENGTH_LONG).show();
                        detailProductActivity.this.finish();
                        Intent intent=new Intent(context,AllProductActivity.class);
                        startActivity(intent);
                    }
                    else if (jsonData.equals("FAIL"))
                    {
                        Toast.makeText(context, "修改失败", Toast.LENGTH_LONG).show();
                        detailProductActivity.this.finish();
                    }
                }
            }
        };

        //点击条目，进行更新
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String target="http://10.0.2.2:8080/super-crm/product/mobileUpdate.json";
                        HttpURLConnection conn= null;
                        try {
                            Product prtupdata=new Product();
                            prtupdata.setProduct_id(prt.getProduct_id());
                            prtupdata.setProduct_name(prt_name.getText().toString());
                            prtupdata.setProduct_saleprice(Double.valueOf(prt_saleprice.getText().toString()));
                            prtupdata.setProduct_jinghuoprice(Double.valueOf(prt_jinjia.getText().toString()));
                            prtupdata.setProduct_jinghuodanhao(prt_jinhuodan.getText().toString());
                            prtupdata.setProduct_quantities(Integer.valueOf(prt_quantities.getText().toString()));
                            prtupdata.setProduct_category(prt_category.getSelectedItem().toString());
                            prtupdata.setProduct_gonghuoshang(prt_gongyingshang.getSelectedItem().toString());
                            prtupdata.setProduct_jijianfanshi(prt_jijianfangshi.getSelectedItem().toString());
                            Gson gson=new Gson();
                            String In=gson.toJson(prtupdata);//把Product bean类型转换为Json格式的数据
                            System.out.println(In);
                            URL url=new URL(target);
                            conn=(HttpURLConnection)url.openConnection();
                            conn.setConnectTimeout(5000);
                            conn.setDoOutput(true);
                            conn.setRequestMethod("POST");
                            conn.setRequestProperty("ser-Agent", "Fiddler");
                            conn.setRequestProperty("Content-Type","application/json");
                            //写输出流，将要转的参数写入流里
                            OutputStream os=conn.getOutputStream();
                            os.write(In.getBytes());
                            os.close();
                            int code=conn.getResponseCode();
                            if (code==200){
                                System.out.println("修改操作响应成功");
                                InputStream inputStream=conn.getInputStream();
                                jsonData= NetUtils.readBytes(inputStream);  //把我们得到byte流转换成字符串，NetUtils就是干这样的事
                                System.out.println(jsonData);
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

        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detailProductActivity.this.finish();
            }
        });







    }
}
