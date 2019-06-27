package com.whut.common.util;

import com.whut.common.config.HBaseConfig;
import com.whut.common.dao.hbase.impl.HBaseDaoImpl;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @author yy
 * @describe
 * @time 18-11-15 下午2:55
 */
public class HBase2FileUtil {

    private static Map<String,String> stringMap = new HashMap<>();

    static {
        stringMap.put("Master","48:4D:7E:EB:FB:56");
        stringMap.put("Slave1","18:66:DA:F2:89:4C");
        stringMap.put("Slave2","80:18:44:E7:C3:D8");
        stringMap.put("Slave3","18:66:DA:F2:A4:50");
        stringMap.put("Slave4","18:66:DA:F6:74:9C");
        stringMap.put("Slave5","18:66:DA:F3:7D:6C");
        stringMap.put("Slave7","80:18:44:E6:1A:F0");
        stringMap.put("Slave8","80:18:44:E6:0B:F0");
        stringMap.put("Slave9","80:18:44:E5:54:5C");
    }



    public static String toCSV(String hostname, String startTime, String stopTime, String qualifiers) {

        File file = new File("data/"+hostname+"_test.csv");
        FileWriter writer = null;
        String mac = stringMap.get(hostname);
        String startKey = mac + TimeFormatUtil.date2Timestamp(startTime, null);
        String stopKey = mac + TimeFormatUtil.date2Timestamp(stopTime, null);
        String[] qualifierList = qualifiers.split(",");

        HBaseDaoImpl hBaseDao = new HBaseDaoImpl();
        Scan scan = new Scan();
        scan = HBaseDaoUtil.rowStart(scan, startKey);
        scan = HBaseDaoUtil.rowStop(scan, stopKey);
        for (String q : qualifierList) {
            scan = HBaseDaoUtil.addColumn(scan, HBaseConfig.FAMILY, q);
        }
        ResultScanner scanner = hBaseDao.scan(HBaseConfig.TABLE_NAME, scan);
        try {
            writer = new FileWriter(file);
            for (Result result : scanner) {
                long timestamp = Long.parseLong(Bytes.toString(result.getRow()).substring(12, 22));
                StringBuilder line = new StringBuilder(TimeFormatUtil.timestamp2Date(timestamp, null));
                Cell[] cells = result.rawCells();
                if (cells != null && cells.length > 0) {
                    for (Cell cell : cells) {
                        String v = Bytes.toString(cell.getValueArray(), cell.getValueOffset(), cell.getValueLength());
                        line.append(",").append(v);
                    }
                }
                String[] strings = line.toString().split(",");
                if (strings.length != 14) continue;
                String user = String.format("%.4f",Double.parseDouble(strings[1])/100);
                String system = String.format("%.4f",Double.parseDouble(strings[2])/100);
                String iowait = String.format("%.4f",Double.parseDouble(strings[3])/100);
                String actual = String.format("%.4f",Double.parseDouble(strings[4])/100);
                String cache = String.format("%.4f",Double.parseDouble(strings[5])/100);
                String count = String.format("%.4f",(Double.parseDouble(strings[6])+Double.parseDouble(strings[7]))/1500);
                String ioRate = String.format("%.4f",(Double.parseDouble(strings[8])+Double.parseDouble(strings[9]))/1024/130);
                String netRate = String.format("%.4f",(Double.parseDouble(strings[10])+Double.parseDouble(strings[11]))/1024/100);
                String tcp = String.format("%.4f",Double.parseDouble(strings[12])/8192);
                String pid = String.format("%.4f",Double.parseDouble(strings[13])/4090);
                String string = "%s,%s,%s,%s,%s,%s,%s,%s,%s,%s\r\n";
                writer.write(String.format(string, user,system,iowait,actual,cache,count,ioRate,netRate,tcp,pid));
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file.getAbsolutePath();
    }


    public static void main(String[] args) {
        toCSV("Master","2018/11/15 18:30:00","2019/11/14 20:16:24",
                "001,002,003,011,012,021,022,025,026,031,032,041,061");
        toCSV("Slave1","2018/11/15 18:30:00","2019/11/14 20:16:24",
                "001,002,003,011,012,021,022,025,026,031,032,041,061");
        toCSV("Slave3","2018/11/15 18:30:00","2019/11/14 20:16:24",
                "001,002,003,011,012,021,022,025,026,031,032,041,061");
        toCSV("Slave4","2018/11/15 18:30:00","2019/11/14 20:16:24",
                "001,002,003,011,012,021,022,025,026,031,032,041,061");
        toCSV("Slave5","2018/11/15 18:30:00","2019/11/14 20:16:24",
                "001,002,003,011,012,021,022,025,026,031,032,041,061");
    }
}
