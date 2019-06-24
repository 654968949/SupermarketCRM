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
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class NewProductActivity extends Activity {

    Context context=this;
    Handler handler;
    EditText newprt_name,newprt_saleprice,newprt_jinjia,newprt_quantities,newprt_jinhuodan;
    Spinner newprt_category,newprt_jijianfangshi,newprt_gongyingshang;
    Button newpProductButton,exitButton;
    String jsonData="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_product);
        //加载控件
        newprt_name=findViewById(R.id.newprtName_input_ud);
        newprt_saleprice=findViewById(R.id.newprtSaleprice_input_ud);
        newprt_jinjia=findViewById(R.id.newprtJinjia_input_ud);
        newprt_quantities=findViewById(R.id.newprtQuantities_input_ud);
        newprt_jinhuodan=findViewById(R.id.newprtJinhuodanhao_input_ud);
        newprt_category=findViewById(R.id.newprtCategory_input_ud);
        newprt_jijianfangshi=findViewById(R.id.newprtJijianfangshi_input_ud);
        newprt_gongyingshang=findViewById(R.id.newprtGongyingshang_input_id);
        newpProductButton=findViewById(R.id.newSave_id);
        exitButton=findViewById(R.id.newExit_id);
        //获取user_id
        final String user_id=getIntent().getStringExtra("user_id");
        System.out.println("新建商品页的userid:"+user_id);
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
                    newprt_category.setAdapter(catAdapter);
                    newprt_category.setSelection(0);
                    ArrayAdapter<String> gongAdapter =new ArrayAdapter<>(
                            context,android.R.layout.simple_spinner_dropdown_item,gongList
                    );
                    newprt_gongyingshang.setAdapter(gongAdapter);
                    newprt_gongyingshang.setSelection(0);
                    ArrayAdapter<String> jijianAdapter =new ArrayAdapter<>(
                            context,android.R.layout.simple_spinner_dropdown_item,jijianList
                    );
                    newprt_jijianfangshi.setAdapter(jijianAdapter);
                    newprt_jijianfangshi.setSelection(0);
                }
                else if(msg.what==0x22)
                {
                    if (jsonData.equals("OK")) {
                        Toast.makeText(context, "新建成功", Toast.LENGTH_LONG).show();
                        NewProductActivity.this.finish();
                        Intent intent=new Intent(context,AllProductActivity.class);
                        startActivity(intent);
                    }
                    else if (jsonData.equals("FAIL"))
                    {
                        Toast.makeText(context, "新建失败", Toast.LENGTH_LONG).show();
                        NewProductActivity.this.finish();
                    }
                }
            }
        };
        //新建商品
        newpProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String target="http://10.0.2.2:8080/super-crm/product/productCreate.json";
                        HttpURLConnection conn= null;
                        try {
                            //把填入表格的信息放入product对象
                            Product newProduct=new Product();
                            newProduct.setProduct_create_id(Integer.valueOf(user_id));
                            newProduct.setProduct_name(String.valueOf(newprt_name.getText()));
                            newProduct.setProduct_saleprice(Double.valueOf(newprt_saleprice.getText().toString()));
                            newProduct.setProduct_jinghuoprice(Double.valueOf(newprt_jinjia.getText().toString()));
                            newProduct.setProduct_quantities(Integer.valueOf(newprt_quantities.getText().toString()));
                            newProduct.setProduct_jinghuodanhao(newprt_jinhuodan.getText().toString());
                            newProduct.setProduct_category(newprt_category.getSelectedItem().toString());
                            newProduct.setProduct_jijianfanshi(newprt_jijianfangshi.getSelectedItem().toString());
                            newProduct.setProduct_gonghuoshang(newprt_gongyingshang.getSelectedItem().toString());
                            Gson gson=new Gson();
                            String data=gson.toJson(newProduct);
                            URL url=new URL(target);
                            conn=(HttpURLConnection)url.openConnection();
                            conn.setConnectTimeout(5000);
                            conn.setDoOutput(true);
                            conn.setRequestMethod("POST");
                            conn.setRequestProperty("ser-Agent", "Fiddler");
                            conn.setRequestProperty("Content-Type","application/json");
                            //写输出流，将要转的参数写入流里
                            OutputStream os=conn.getOutputStream();
                            os.write(data.getBytes());
                            os.close();
                            int code=conn.getResponseCode();
                            if (code==200){
                                System.out.println("响应成功");
                                InputStream inputStream=conn.getInputStream();
                                jsonData= NetUtils.readBytes(inputStream);  //把我们得到byte流转换成字符串，NetUtils就是干这样的事
                                Message ms=handler.obtainMessage();
                                ms.what=0x33;
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
                        if(msg.what==0x33)
                        {
                            if (jsonData.equals("OK")) {
                                Toast.makeText(context, "新建成功", Toast.LENGTH_LONG).show();
                                NewProductActivity.this.finish();
                                Intent intent=new Intent(context,AllProductActivity.class);
                                startActivity(intent);
                            }
                            else if (jsonData.equals("FAIL"))
                            {
                                Toast.makeText(context, "新建失败", Toast.LENGTH_LONG).show();
                                NewProductActivity.this.finish();
                            }
                        }
                    }
                };
            }
        });
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewProductActivity.this.onDestroy();
                NewProductActivity.this.finish();
            }
        });

    }
}
