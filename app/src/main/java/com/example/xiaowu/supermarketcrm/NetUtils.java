package com.example.xiaowu.supermarketcrm;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class NetUtils {
    public static String readBytes(InputStream is){
        try {
            byte[] buffer = new byte[1024];
            int len = -1 ;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            while((len = is.read(buffer)) != -1){
                baos.write(buffer, 0, len);
            }
            baos.close();
            return baos.toString("utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null ;
    }


}
