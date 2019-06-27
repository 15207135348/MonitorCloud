package com.whut.monitoring.netty.handler;

import com.alibaba.fastjson.JSONObject;
import com.whut.common.util.TimeFormatUtil;
import com.whut.monitoring.entity.MonitoredHost;
import com.whut.monitoring.service.MonitoredHostService;
import com.whut.common.wesocket.PollWebSocketServlet;
import io.netty.channel.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by YY on 2018-03-25.
 */
@Service("NettyServerHandler1")
@ChannelHandler.Sharable
public class NettyServerHandler1 extends SimpleChannelInboundHandler<String>
{
    private static final Logger logger = Logger.getLogger(NettyServerHandler1.class);
    //采集客户端连接ID和对应的主机MAC
    private static Map<ChannelId, String> channelMacMap = new HashMap<>();

    private final
    MonitoredHostService monitoredHostService;

    private final
    PollWebSocketServlet webSocketServlet;
    @Autowired
    @Lazy
    public NettyServerHandler1(MonitoredHostService monitoredHostService, PollWebSocketServlet webSocketServlet) {

        this.monitoredHostService = monitoredHostService;
        this.webSocketServlet = webSocketServlet;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String msg)
    {
        JSONObject object = JSONObject.parseObject(msg);
        //监控主机的基本信息
        if(object.getString("H").equals("1"))
        {
            String username = object.getString("UN");
            String macAddress = object.getString("M");
            String machineName = object.getString("MN");
            String systemName = object.getString("SN");
            String ipv4Address = object.getString("IP");
            String cpu = object.getString("CN");
            String ram = object.getString("MT");
            String disk = object.getString("DT");
            MonitoredHost monitoredHost =
                    new MonitoredHost(machineName, systemName, macAddress, ipv4Address,
                            cpu, ram, disk, true, System.currentTimeMillis(), username);
            monitoredHostService.updateHostByMac(monitoredHost.getMac(),monitoredHost);
            webSocketServlet.sendText(username,"ok");
            channelMacMap.put(channelHandlerContext.channel().id(),macAddress);
            logger.info("monitored host information");
        }
        else
        {
            //进入下一个处理逻辑
            channelHandlerContext.fireChannelRead(object);
        }
    }
    /*
     * 建立连接时，返回消息
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception
    {
        System.out.println("连接的客户端地址:" + ctx.channel().remoteAddress());
        super.channelActive(ctx);
    }

    /*
     * 建立连接时，返回消息
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception
    {
        String mac = channelMacMap.get(ctx.channel().id());
        monitoredHostService.setConnectedByMac(mac,false);
        channelMacMap.remove(ctx.channel().id());
        super.channelActive(ctx);
    }
}
