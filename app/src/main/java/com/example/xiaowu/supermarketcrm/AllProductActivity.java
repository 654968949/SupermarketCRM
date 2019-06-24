package com.example.xiaowu.supermarketcrm;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.widget.Toast;

import org.json.JSONObject;

public class AllProductActivity extends Activity {

    ListView listview;
    Context context=this;
    String jsonData="";
    Handler handler;
    ArrayList<Product> productList;
    allProductAdapter productAdapter;
    Product prt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview_product);
        listview=findViewById(R.id.list_product);
/*
获取全部商品信息的线程
 */
        handler =new Handler()
        {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what==0x11)
                {
                    Gson gson=new Gson();
                    productList=gson.fromJson(jsonData, new TypeToken<List<Product>>(){}.getType());
                    productAdapter=new allProductAdapter(context,productList);
                    listview.setAdapter(productAdapter);
                }
            }
        };
    jsonData=getIntent().getStringExtra("selectbyRequireData");
    if(jsonData==null)
    {
      new Thread(new Runnable() {
        @Override
        public void run() {
            String target = "http://10.0.2.2:8080/super-crm/product/listAll.json";
            HttpURLConnection conn = null;
            try {
                URL url = new URL(target);
                conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(5000);
                conn.setDoOutput(true);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("ser-Agent", "Fiddler");
                conn.setRequestProperty("Content-Type", "application/json");
                //写输出流，将要转的参数写入流里
                int code = conn.getResponseCode();
                if (code == 200) {
                    System.out.println("响应成功");
                    InputStream inputStream = conn.getInputStream();
                    jsonData = NetUtils.readBytes(inputStream);//把我们得到byte流转换成字符串，NetUtils就是干这样的事
                    Message ms = handler.obtainMessage();
                    ms.what = 0x11;
                    handler.sendMessage(ms);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
      }).start();
    }
    else {
        Message ms = handler.obtainMessage();
        ms.what = 0x11;
        handler.sendMessage(ms);
    }


        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent=new Intent(context,detailProductActivity.class);
                Gson gson=new Gson();
                String json=gson.toJson(productList.get(position));
                System.out.println("传到商品详情页的数据为："+json);
                intent.putExtra("person_data",json);
                startActivity(intent);
            }
        });
        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new Builder(context);
                builder.setTitle("删除商品");
                builder.setMessage("是否要删除该商品？");
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                builder.setPositiveButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String target="http://10.0.2.2:8080/super-crm/product/mobileDelete.json";
                                String product_id = String.valueOf(productList.get(position).getProduct_id());

                                HttpURLConnection conn= null;
                                try {
                                    URL url=new URL(target);
                                    JSONObject jsonObject=new JSONObject();
                                    jsonObject.put("product_id",product_id);
                                    String content=String.valueOf(jsonObject);
                                    System.out.println(content);
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
                                    System.out.println(code);
                                    if (code==200){
                                        System.out.println("删除响应成功");
                                        InputStream inputStream=conn.getInputStream();
                                        jsonData= NetUtils.readBytes(inputStream);//把我们得到byte流转换成字符串，NetUtils就是干这样的事
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
                                if (msg.what==0x33)
                                {
                                    if(jsonData.equals("OK"))
                                    {
                                        Toast.makeText(context,"删除成功",Toast.LENGTH_LONG).show();
                                        productList.remove(position);
                                        productAdapter.notifyDataSetChanged();
                                    }
                                    else if(jsonData.equals("FAIL"))
                                    {
                                        Toast.makeText(context,"删除失败",Toast.LENGTH_LONG).show();
                                    }
                                }
                            }
                        };
                            }
                        });
                builder.create().show();

                return true;
            }
        });
        {

        }




    }
}
