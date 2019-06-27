package com.whut.monitoring.netty.handler;

import com.alibaba.fastjson.JSONObject;
import com.whut.common.dao.mongodb.IMongoDBDao;
import com.whut.common.util.FileUtil;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import org.apache.log4j.Logger;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;


/**
 * Created by YY on 2018-03-25.
 */
@Service("NettyServerHandler4")
@ChannelHandler.Sharable
public class NettyServerHandler4 extends ChannelInboundHandlerAdapter
{
    private static final Logger logger = Logger.getLogger(NettyServerHandler4.class);

    private final
    IMongoDBDao mongoDBDao;

    @Autowired
    public NettyServerHandler4(IMongoDBDao mongoDBDao) {

        this.mongoDBDao = mongoDBDao;
    }
    @Override
    public void channelRead(ChannelHandlerContext channelHandlerContext, Object msg)
    {
        JSONObject object = (JSONObject)msg;
        //监控用户操作日志
        if(object.getString("H").equals("4"))
        {
            String mac = object.getString("M");
            object.remove("T");
            object.remove("M");
            object.remove("H");
            for (String filename : object.keySet())
            {
                String content = object.getString(filename);
                FileUtil.createFile(filename,content);
//                int pos = filename.indexOf("@");
//                String user = filename.substring(0,pos);
//                filename = filename.substring(pos+1);
//                pos = filename.indexOf("[");
//                String ip = filename.substring(0,pos);
//                filename = filename.substring(pos+1);
//                pos = filename.indexOf("-");
//                String start = filename.substring(0,pos);
//                filename = filename.substring(pos+1);
//                pos = filename.indexOf("]");
//                String stop = filename.substring(0,pos);
//                mongoDBDao.insertDocument(mac,new Document().append("user",user).append("ip",ip).append("start",start).append("stop",stop).append("content",content));
                logger.info("add 1 user action log");
            }
        }
        else
        {
            channelHandlerContext.fireChannelRead(msg);
        }
    }
}
