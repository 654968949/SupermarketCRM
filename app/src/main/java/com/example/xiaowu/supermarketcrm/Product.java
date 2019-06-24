package com.example.xiaowu.supermarketcrm;

import java.io.Serializable;
import java.util.Date;

public class Product implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer product_id;
    private String product_name;
    private double product_saleprice;
    private double product_jinghuoprice;
    private String product_category;
    private String product_jijianfanshi;
    private Integer product_quantities;
    private String product_jinghuodanhao;
    private String product_gonghuoshang;
    private Date product_create_time;    // 创建时间
    private Integer product_create_id;  // 创建人
    private Integer start;
    private Integer rows;
    public Integer getProduct_id() {
        return product_id;
    }
    public void setProduct_id(Integer product_id) {
        this.product_id = product_id;
    }
    public String getProduct_name() {
        return product_name;
    }
    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }
    public double getProduct_saleprice() {
        return product_saleprice;
    }
    public void setProduct_saleprice(double product_saleprice) {
        this.product_saleprice = product_saleprice;
    }
    public double getProduct_jinghuoprice() {
        return product_jinghuoprice;
    }
    public void setProduct_jinghuoprice(double product_jinghuoprice) {
        this.product_jinghuoprice = product_jinghuoprice;
    }
    public String getProduct_category() {
        return product_category;
    }
    public void setProduct_category(String product_category) {
        this.product_category = product_category;
    }
    public String getProduct_jijianfanshi() {
        return product_jijianfanshi;
    }
    public void setProduct_jijianfanshi(String product_jijianfanshi) {
        this.product_jijianfanshi = product_jijianfanshi;
    }
    public String getProduct_jinghuodanhao() {
        return product_jinghuodanhao;
    }
    public void setProduct_jinghuodanhao(String product_jinghuodanhao) {
        this.product_jinghuodanhao = product_jinghuodanhao;
    }
    public String getProduct_gonghuoshang() {
        return product_gonghuoshang;
    }
    public void setProduct_gonghuoshang(String product_gonghuoshang) {
        this.product_gonghuoshang = product_gonghuoshang;
    }


    public Integer getStart() {
        return start;
    }
    public void setStart(Integer start) {
        this.start = start;
    }
    public Integer getRows() {
        return rows;
    }
    public void setRows(Integer rows) {
        this.rows = rows;
    }
    public Integer getProduct_create_id() {
        return product_create_id;
    }
    public void setProduct_create_id(Integer product_create_id) {
        this.product_create_id = product_create_id;
    }
    public Integer getProduct_quantities() {
        return product_quantities;
    }
    public void setProduct_quantities(Integer product_quantities) {
        this.product_quantities = product_quantities;
    }
    public Date getProduct_create_time() {
        return product_create_time;
    }
    public void setProduct_create_time(Date product_create_time) {
        this.product_create_time = product_create_time;
    }




}
