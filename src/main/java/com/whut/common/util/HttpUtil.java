package com.whut.common.util;

import com.whut.common.domain.HPair;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by 杨赟 on 2018-06-12.
 */
public class HttpUtil {

    private static HPair<Integer,String> request(String method, String url, Map<String,String> head)
    {
        HPair<Integer,String> pair=new HPair<>();
        Integer responseCode = 0;
        StringBuilder response = new StringBuilder();
        try {
            //设置URL
            URL _url = new URL(url);
            HttpURLConnection conn = (HttpURLConnection)_url.openConnection();
            conn.setConnectTimeout(3000);
            //设置请求方法
            conn.setRequestMethod(method);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(true);
            conn.setInstanceFollowRedirects(true);
            //设置请求头
            for(String key:head.keySet())
            {
                conn.setRequestProperty(key,head.get(key));
            }
            conn.connect();
            //获取返回结果
            responseCode = conn.getResponseCode();
            InputStream is = conn.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String readLine;
            while((readLine =br.readLine()) != null){
                response.append(readLine);
            }
            is.close();
            br.close();
            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        pair.setFirst(responseCode);
        pair.setSecond(response.toString());
        return pair;
    }

    private static HPair<Integer,String> request(String method, String url, Map<String,String> head, String body)
    {
        HPair<Integer,String> pair=new HPair<>();
        Integer responseCode = 0;
        StringBuilder response = new StringBuilder();
        try {
            //设置URL
            URL _url = new URL(url);
            HttpURLConnection conn = (HttpURLConnection)_url.openConnection();
            conn.setConnectTimeout(3000);
            //设置请求方法
            conn.setRequestMethod(method);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(true);
            conn.setInstanceFollowRedirects(true);
            //设置请求头
            for(String key:head.keySet())
            {
                conn.setRequestProperty(key,head.get(key));
            }
            conn.connect();
            //设置请求体
            DataOutputStream out = new DataOutputStream(conn.getOutputStream());
            out.writeBytes(body);
            out.flush();
            out.close();
            //获取返回结果
            responseCode = conn.getResponseCode();
            InputStream is = conn.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String readLine;
            while((readLine =br.readLine()) != null){
                response.append(readLine);
            }
            is.close();
            br.close();
            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        pair.setFirst(responseCode);
        pair.setSecond(response.toString());
        return pair;
    }
    public static HPair<Integer,String> postByJsonAndToken(String url, String token, String body) {
        Map<String,String> head = new TreeMap<>();
        head.put("Content-Type","application/json");
        head.put("Accept","application/json");
        head.put("Charset", "UTF-8");
        head.put("X-Auth-Token",token);
        return request("POST",url,head,body);
    }
    public static HPair<Integer,String> postByJson(String url, String body) {
        Map<String, String> head = new TreeMap<>();
        head.put("Content-Type", "application/json");
        head.put("Accept", "application/json");
        head.put("Charset", "UTF-8");
        return request("POST",url, head, body);
    }

    public static HPair<Integer,String> getByJsonAndToken(String url, String token) {
        Map<String, String> head = new TreeMap<>();
        head.put("Content-Type", "application/json");
        head.put("Accept", "application/json");
        head.put("Charset", "UTF-8");
        head.put("X-Auth-Token", token);
        return request("GET", url, head);
    }
    public static HPair<Integer,String> deleteByJsonAndToken(String url, String token) {
        Map<String,String> head = new TreeMap<>();
        head.put("Content-Type","application/json");
        head.put("Accept","application/json");
        head.put("Charset", "UTF-8");
        head.put("X-Auth-Token",token);
        return request("DELETE",url,head);
    }
}
