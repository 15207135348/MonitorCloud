package com.whut.monitoring.util;

import com.alibaba.fastjson.JSONObject;
import com.whut.monitoring.netty.handler.NettyServerHandler3;
import io.netty.channel.ChannelHandlerContext;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author 杨赟
 * @describe
 * @since 19-1-19 下午2:02
 */
public class IptablesUtil {

    private static boolean notIP(String ip) {
        String str = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";
        Pattern pattern = Pattern.compile(str);
        Matcher matcher = pattern.matcher(ip);
        return !matcher.matches();
    }

    public static boolean enableIP(String ip)
    {
        if(notIP(ip)) return false;
        JSONObject object = new JSONObject();
        JSONObject para = new JSONObject();
        para.put("ip",ip);
        object.put("method","/iptables/enable");
        object.put("para",para);
        for (ChannelHandlerContext ctx : NettyServerHandler3.contextList)
        {
            ctx.writeAndFlush(object.toJSONString());
        }
        return true;
    }

    public static boolean disableIP(String ip)
    {
        if(notIP(ip)) return false;
        JSONObject object = new JSONObject();
        JSONObject para = new JSONObject();
        para.put("ip",ip);
        object.put("method","/iptables/disable");
        object.put("para",para);
        for (ChannelHandlerContext ctx : NettyServerHandler3.contextList)
        {
            ctx.writeAndFlush(object.toJSONString());
        }
        return true;
    }
}
