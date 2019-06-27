package com.whut.monitoring.entity;

import com.alibaba.fastjson.JSONObject;
import com.whut.common.util.TimeFormatUtil;

import javax.persistence.*;

/**
 * @author 杨赟
 * @describe
 * @since 19-1-19 下午9:01
 */
@Entity
@Table(name = "anomaly_record")
public class AnomalyRecord {

    @Id
    @GeneratedValue
    private int id;
    @Column(name = "mac")
    private String mac;
    @Column(name = "start")
    private long start;
    @Column(name = "keep")
    private long keep;
    @Column(name = "report")
    private String report;
    @Column(name = "logs")
    private String logs;

    public AnomalyRecord() {

    }

    public AnomalyRecord(String mac, long start, long keep, String report, String logs) {
        this.mac = mac;
        this.start = start;
        this.keep = keep;
        this.report = report;
        this.logs = logs;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getKeep() {
        return keep;
    }

    public void setKeep(long retain) {
        this.keep = retain;
    }

    public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getLogs() {
        return logs;
    }

    public void setLogs(String logs) {
        this.logs = logs;
    }

    public JSONObject toJSON() {

        JSONObject object = new JSONObject();
        object.put("id",id);
        object.put("mac",mac);
        object.put("start", TimeFormatUtil.timestamp2Date(start,null));
        object.put("keep", keep);
        object.put("report",report);
        object.put("logs",logs);
        return object;
    }
}
