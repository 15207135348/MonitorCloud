package com.whut.openstack.util;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Created by YY on 2017-12-28.
 */
public class Base64Util {
    // 编码
    public static String encodeByBase64(String input)
    {
        String asB64="";
        try {
            asB64 = new BASE64Encoder().encode(input.getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return asB64;
    }
    // 解码
    public static String decodeByBase64(String input)
    {
        String str="";
        try {
            BASE64Decoder decoder = new BASE64Decoder();
            byte[] b = decoder.decodeBuffer(input);
            str = new String(b,"utf-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str;
    }

}
