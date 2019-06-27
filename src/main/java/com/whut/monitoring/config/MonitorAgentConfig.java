package com.whut.monitoring.config;

import com.whut.common.util.FileUtil;
import com.whut.monitoring.netty.handler.NettyServerHandler1;
import com.whut.monitoring.util.IptablesUtil;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

/**
 * @author yy
 * @describe
 * @time 18-8-7 下午1:38
 */
public class MonitorAgentConfig {

    private static final String SERVER_HOST = getServerHost();
    private static final int SERVER_PORT = 8880;
    private static final int TCP_PORT = 8879;

    private static final String AGENT_URL = String.format(
            "http://%s:%d/download/MonitorAgent.tar",
            SERVER_HOST,SERVER_PORT
    );
    private static final String SCRIPT_FORMAT =
            "rm -rf /usr/local/MonitorAgent\r\n" +
            "wget %s\r\n" +
            "tar -xvf MonitorAgent.tar -C /usr/local\r\n" +
            "rm -rf MonitorAgent.tar\r\n" +
            "cd /usr/local/MonitorAgent\r\n" +
            "nohup ./AppRun --ip %s --port %d --internal %d --selfstart %b --username %s >/dev/null 2>&1 &\r\n";
    static {
        String[] list = FileUtil.listFiles("/var/download");
        if (list!= null) {
            boolean flag = false;
            for(String file : list) {
                if(file.equals("MonitorAgent.tar")){
                    flag = true;
                }
            }
            if(!flag){
                try {
                    throw new Exception("/var/download目录下未添加MonitorAgent.tar下载资源!");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    /**
     * @since 18-8-26 上午10:56
     * @author 杨赟
     * @describe 自动获得服务器地址
     */
    private static String getServerHost(){
        List<String> ipv4List = new ArrayList<>();
        Enumeration allNetInterfaces = null;
        try {
            allNetInterfaces = NetworkInterface.getNetworkInterfaces();
        } catch (java.net.SocketException e) {
            e.printStackTrace();
        }
        InetAddress ip = null;
        assert allNetInterfaces != null;
        while (allNetInterfaces.hasMoreElements())
        {
            //获取网卡
            NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
            //获取该网卡下的IP地址
            Enumeration addresses = netInterface.getInetAddresses();
            //获得所有IPv4地址
            while (addresses.hasMoreElements())
            {
                ip = (InetAddress) addresses.nextElement();
                if (ip != null && ip instanceof Inet4Address)
                {
                    String address = ip.getHostAddress();
                    if(address.equals("127.0.0.1")){
                        continue;
                    }
                    ipv4List.add(address);
                }
            }
        }
        //从所有IPv4地址中寻找最可能是外网地址的IP
        for(String ipv4 : ipv4List)
        {
            //学校校园网IPv4地址
            if(ipv4.contains("10.139.")||ipv4.contains("10.137.")){
                return ipv4;
            }
            //实验室路由器局域网 IPv4地址
            if(ipv4.contains("192.168.1.")&&Integer.valueOf(ipv4.substring(10))>12){
                return ipv4;
            }
        }
        //如果都不是,则设置为默认IPv4地址
        return "59.69.101.206";
    }
    /**
     * @since 18-8-7 下午2:09
     * @author 杨赟
     * @describe 生成安装脚本
     */
    public static String getScript(int internal, boolean selfStart, String username){
        return String.format(SCRIPT_FORMAT,AGENT_URL,SERVER_HOST,TCP_PORT,internal,selfStart,username);
    }
}
