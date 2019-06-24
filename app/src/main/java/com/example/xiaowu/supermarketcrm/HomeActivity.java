package com.example.xiaowu.supermarketcrm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
public class HomeActivity extends Activity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button allProduct=findViewById(R.id.allProducts);
        Button requireSelect=findViewById(R.id.requireSelect);
        Button newProduct=findViewById(R.id.newProduct);
        Button loginOut=findViewById(R.id.loginOut);
        allProduct.setOnClickListener(this);//用实现监听器接口的方式来注册监听
        requireSelect.setOnClickListener(this);
        newProduct.setOnClickListener(this);
        loginOut.setOnClickListener(this);

    }
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.allProducts:
                Intent allProduct=new Intent(HomeActivity.this,AllProductActivity.class);
                startActivity(allProduct);
                break;
            case R.id.requireSelect:
                Intent requireSelect=new Intent(this,ListByRequestActivity.class);
                startActivity(requireSelect);
                break;
            case R.id.newProduct:
                String user_id=getIntent().getStringExtra("user_id");
                System.out.println("Home界面的userid："+user_id);
                Intent newProduct=new Intent(this,NewProductActivity.class);
                newProduct.putExtra("user_id",user_id);
                startActivity(newProduct);
                break;
            case R.id.loginOut:
                Intent i=new Intent(HomeActivity.this,LoginActivity.class);
                startActivity(i);
                break;
        }


    }
}
