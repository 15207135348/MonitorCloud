package com.whut.monitoring.netty;

import com.whut.monitoring.netty.handler.NettyServerHandler1;
import com.whut.monitoring.netty.handler.NettyServerHandler2;
import com.whut.monitoring.netty.handler.NettyServerHandler3;
import com.whut.monitoring.netty.handler.NettyServerHandler4;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;


/**
 * @author 杨赟
 * @describe Netty 服务端配置
 */
@Component
public class NettyServer implements ApplicationListener {

    private static final Logger logger = Logger.getLogger(NettyServer.class);

    // TCP端口号
    private static final int PORT = 79;

    private static ServerBootstrap bootstrap = null;
    private static EventLoopGroup bossGroup = null;
    private static EventLoopGroup workerGroup = null;

    private final
    NettyServerHandler1 nettyServerHandler1;
    private final
    NettyServerHandler2 nettyServerHandler2;
    private final
    NettyServerHandler3 nettyServerHandler3;
    private final
    NettyServerHandler4 nettyServerHandler4;

    @Autowired
    public NettyServer(NettyServerHandler1 nettyServerHandler1,
                       NettyServerHandler3 nettyServerHandler3,
                       NettyServerHandler2 nettyServerHandler2,
                       NettyServerHandler4 nettyServerHandler4) {
        this.nettyServerHandler1 = nettyServerHandler1;
        this.nettyServerHandler2 = nettyServerHandler2;
        this.nettyServerHandler3 = nettyServerHandler3;
        this.nettyServerHandler4 = nettyServerHandler4;
    }


    private synchronized void start() {
        if (isAccepting()) {
            logger.info("NETTY SERVER IS ALREADY STARTED!");
            return;
        }
        try {
            bootstrap = new ServerBootstrap();
            bossGroup = new NioEventLoopGroup(1);
            workerGroup = new NioEventLoopGroup();
            bootstrap.group(bossGroup, workerGroup);
            bootstrap.channel(NioServerSocketChannel.class);
            bootstrap.option(ChannelOption.SO_BACKLOG, 128);
            // 通过NoDelay禁用Nagle,使消息立即发出去，不用等待到一定的数据量才发出去
            bootstrap.option(ChannelOption.TCP_NODELAY, true);
            // 保持长连接状态
            bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
            bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    ChannelPipeline p = socketChannel.pipeline();
                    // Tcp分组发送
                    ByteBuf delimiter = Unpooled.copiedBuffer("@end".getBytes());
                    //增大缓冲区间大小20MB
                    p.addLast("package", new DelimiterBasedFrameDecoder(20 * 1024 * 1024, delimiter));
                    // 设置编解码器
                    p.addLast("encoder", new StringEncoder(Charset.forName("UTF-8")));
                    p.addLast("decoder", new StringDecoder(Charset.forName("UTF-8")));
                    //设置处理类
                    p.addLast("handler1", nettyServerHandler1);
                    p.addLast("handler2", nettyServerHandler2);
                    p.addLast("handler3", nettyServerHandler3);
                    p.addLast("handler4", nettyServerHandler4);
                }
            });
            // 服务端绑定端口
            ChannelFuture future = bootstrap.bind(PORT).sync();
            if (future.isSuccess()) {
                logger.info("NETTY SERVER IS STARTED!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("NETTY SERVER START FAILED!");
            close();
        }
    }

    private static boolean isAccepting() {
        return bootstrap != null && !bossGroup.isShutdown() && !workerGroup.isShutdown();
    }

    private synchronized void close() {
        // 关闭Netty
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
        // 先显式得设为null
        bootstrap = null;
        logger.info("NETTY SERVER CLOSED!");
        logger.info("NETTY IS ACCEPTING=" + isAccepting());
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof ContextRefreshedEvent && ((ContextRefreshedEvent) event).getApplicationContext().getParent() == null) {
            System.out.println("开启netty...");
            start();
        } else if (event instanceof ContextClosedEvent) {
            System.out.println("关闭netty");
            close();
        }
    }

}
