package com.whut.monitoring.netty.handler;

import com.alibaba.fastjson.JSONObject;
import com.whut.monitoring.entity.IPTables;
import com.whut.monitoring.service.IPTablesService;
import com.whut.monitoring.util.IptablesUtil;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by YY on 2018-03-25.
 */
@Service("NettyServerHandler3")
@ChannelHandler.Sharable
@EnableScheduling
public class NettyServerHandler3 extends ChannelInboundHandlerAdapter
{
    private static final Logger logger = Logger.getLogger(NettyServerHandler3.class);

    private static Map<String,Integer> failMap = new HashMap<>();
    public static List<ChannelHandlerContext> contextList = new LinkedList<>();

    private static final int threshold = 20;

    private final
    IPTablesService ipTablesService;

    @Autowired
    @Lazy
    public NettyServerHandler3(IPTablesService ipTablesService) {

        this.ipTablesService = ipTablesService;
    }
    @Override
    public void channelRead(ChannelHandlerContext channelHandlerContext, Object msg)
    {
        JSONObject object = (JSONObject)msg;
        //监控主机5min内登录失败的IP和次数
        if(object.getString("H").equals("3"))
        {
            object.remove("T");
            object.remove("M");
            object.remove("H");
            for (String ip : object.keySet())
            {
                failMap.merge(ip, 1, (a, b) -> a + b);
            }
        }
        else
        {
            channelHandlerContext.fireChannelRead(msg);
        }
    }

    /*
     * 建立连接时，返回消息
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception
    {
        contextList.add(ctx);
        super.channelActive(ctx);
    }

    /*
     * 建立连接时，返回消息
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception
    {
        contextList.remove(ctx);
        super.channelActive(ctx);
    }
    /*
     * 每5min检查一次,如果登录失败次数超过20次,则禁用IP
     */
    @Scheduled(fixedRate = 300000)
    private void fail2ban()
    {
        for (String ip : failMap.keySet())
        {
            if (failMap.get(ip) < threshold)
            {
                continue;
            }
            if(IptablesUtil.disableIP(ip))
            {
                ipTablesService.updateByIp(ip, new IPTables(ip,"5min内多次登录失败"));
                logger.info("disable ip " + ip);
            }
        }
        failMap.clear();
    }
}
