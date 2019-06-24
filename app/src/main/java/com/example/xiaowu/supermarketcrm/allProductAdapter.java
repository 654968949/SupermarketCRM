package com.example.xiaowu.supermarketcrm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class allProductAdapter extends BaseAdapter {
    LayoutInflater inflater;
    Context context;
    ArrayList<Product> listIn;
    @Override
    public int getCount() {
        return listIn.size();
    }
    public allProductAdapter(Context context,ArrayList<Product>list) {
        super();
        this.context=context;
        this.inflater=LayoutInflater.from(context);
        this.listIn=list;
    }
    @Override
    public Object getItem(int pos) {
        return listIn.get(pos);
    }
    @Override
    public long getItemId(int pos) {
        return pos;
    }
    @Override
    public View getView(int pos, View view, ViewGroup parent){
        ViewHolder holder=null;
        if(view==null){
            view=inflater.inflate(R.layout.showproducts,null);
            holder=new ViewHolder();
            holder.product_id= (TextView) view.findViewById(R.id.product_id);
            holder.product_name=(TextView)view.findViewById(R.id.product_name);
            holder.product_quantities= (TextView)view.findViewById(R.id.product_quantities);
            view.setTag(holder);
        }else{
            holder=(ViewHolder) view.getTag();
        }
        Product prt=listIn.get(pos);
        holder.product_id.setText(String.valueOf(prt.getProduct_id()));
        holder.product_name.setText(prt.getProduct_name());
        holder.product_quantities.setText(String.valueOf(prt.getProduct_quantities()));
        return view;
    }
    class ViewHolder{
        TextView product_id,product_name,product_quantities;
    }

}
