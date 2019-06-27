package com.whut.monitoring.netty.handler;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.whut.common.dao.hbase.IHBaseDao;
import com.whut.common.service.EmailService;
import com.whut.common.util.HBaseDaoUtil;
import com.whut.common.wesocket.PollWebSocketServlet;
import com.whut.common.config.HBaseConfig;
import com.whut.monitoring.entity.IPTables;
import com.whut.monitoring.service.AnomalyRecordService;
import com.whut.monitoring.service.DetectionService;
import com.whut.monitoring.service.IHBaseService;
import com.whut.monitoring.service.MonitoredHostService;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by YY on 2018-03-25.
 */
@Service("NettyServerHandler2")
@ChannelHandler.Sharable
@EnableScheduling
public class NettyServerHandler2 extends ChannelInboundHandlerAdapter
{
    private static final Logger logger = Logger.getLogger(NettyServerHandler2.class);

    private static final ConcurrentHashMap<String,Long> macAndTime = new ConcurrentHashMap<>();

    private static final ConcurrentHashMap<String,String> macAndName = new ConcurrentHashMap<>();

    private static final ConcurrentHashMap<String,Float> macAndCpus = new ConcurrentHashMap<>();


    private final
    PollWebSocketServlet webSocketServlet;
    private final
    IHBaseDao hBaseDao;

    private final
    DetectionService detectionService;

    private final
    AnomalyRecordService anomalyRecordService;

    private final
    IHBaseService ihBaseService;

    private final
    MonitoredHostService monitoredHostService;

    private final
    EmailService emailService;

    @Autowired
    @Lazy
    public NettyServerHandler2(PollWebSocketServlet webSocketServlet,
                               IHBaseDao hBaseDao,
                               DetectionService detectionService,
                               AnomalyRecordService anomalyRecordService, IHBaseService ihBaseService, MonitoredHostService monitoredHostService, EmailService emailService) {
        this.webSocketServlet = webSocketServlet;
        this.hBaseDao = hBaseDao;
        this.detectionService = detectionService;
        this.anomalyRecordService = anomalyRecordService;
        this.ihBaseService = ihBaseService;
        this.monitoredHostService = monitoredHostService;
        this.emailService = emailService;
    }

    @Override
    public void channelRead(ChannelHandlerContext channelHandlerContext, Object msg)
    {
        JSONObject object = (JSONObject) msg;
        //监控主机的系统度量数据
        if(object.getString("H").equals("2"))
        {
            object.remove("H");

            String mac = object.getString("M");
            object.remove("M");
            //send real-time data
            webSocketServlet.sendJson(mac,object);
            //add HBase
            long timestamp = object.getLong("T");
            macAndTime.put(mac,timestamp);
            object.remove("T");
            String rowKey = String.format(HBaseConfig.ROW_KEY, mac, timestamp);
            Map<String,Object> map = object.getInnerMap();
            hBaseDao.add(HBaseConfig.TABLE_NAME, HBaseDaoUtil.rowPut(rowKey, HBaseConfig.FAMILY, map));

            //anomaly detection
            if (detectionService.isAbnormal(mac, timestamp, object)) {
//                int id = anomalyRecordService.addRecord(new AnomalyRecord(mac,timestamp));
                logger.info("anomaly data");
            }
        }
        //如果不是基本信息,那么就是主机的动态数据
        else
        {
            //进入下一个处理逻辑
            channelHandlerContext.fireChannelRead(msg);
        }
    }

    /*
     * 每3min检查一次,如果系统负载过高，则发送邮件报警
     */
    private final int PER_TIME = 300000;
    @Scheduled(fixedRate = PER_TIME)
    private void check2email()
    {
        try {
            for (String mac : macAndTime.keySet()){
                String name = macAndName.get(mac);
                if (name == null){
                    name = monitoredHostService.findHostByMac(mac).getHostname();
                    macAndName.put(mac,name);
                }
                Long time = macAndTime.get(mac);
                JSONArray array = ihBaseService.findCpuByTime(mac,time-PER_TIME/1000,time);
                int n = array.size();
                int count6 = 0;
                int count9 = 0;
                for (int i = 0; i < n; ++i){
                    JSONObject object = array.getJSONObject(i).getJSONObject("O");
                    float a = Float.parseFloat(object.getString("a"));
                    float b = Float.parseFloat(object.getString("b"));
                    float c = Float.parseFloat(object.getString("c"));
                    float d = Float.parseFloat(object.getString("d"));
                    float e = Float.parseFloat(object.getString("e"));
                    float f = Float.parseFloat(object.getString("f"));
                    float g = Float.parseFloat(object.getString("g"));
                    float cpuUsage = 1-(d/(a+b+c+d+e+f+g));
                    if (cpuUsage > 0.6){
                        count6 += 1;
                    }
                    if (cpuUsage > 0.9){
                        count9 += 1;
                    }
                }
                if (count6 > n * 0.5){
                    String subject = String.format("%sCPU异常", name);
                    String content = "轻微告警：" + name + "CPU使用率持续3分钟大于60%!!!";
                    emailService.sendHtmlEmail("1365733349@qq.com",subject ,content);
//                emailService.sendHtmlEmail("710229096@qq.com",subject ,content);
                }
                if (count9 > n * 0.5){
                    String subject = String.format("%sCPU异常", name);
                    String content = "严重告警：" + name + "CPU使用率持续3分钟大于90%!!!";
                    emailService.sendHtmlEmail("1365733349@qq.com",subject ,content);
                    emailService.sendHtmlEmail("710229096@qq.com",subject ,content);
                }
            }
            logger.info("定时检查CPU异常");
        }catch (Exception e){
            logger.warn(e.getMessage());
        }
    }
}
