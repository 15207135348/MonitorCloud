package com.whut.monitoring.domain;

import com.alibaba.fastjson.JSONObject;

/**
 * @author 杨赟
 * @describe
 * @since 19-1-22 上午10:39
 */
public class AnomalyWindows {

    private long startTime;
    private long stopTime;
    private boolean lastFlag = false;

    public AnomalyWindows(){

    }

    public boolean isAbnormal(long timestamp, JSONObject metrics)
    {
        if (!lastFlag) {
            startTime = timestamp;
        }
        boolean curFlag = abnormal(metrics);
        if (curFlag) {
            stopTime = timestamp;
        }
        return lastFlag && curFlag;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getStopTime() {
        return stopTime;
    }

    public void setStopTime(long stopTime) {
        this.stopTime = stopTime;
    }

    private boolean abnormal(JSONObject metrics)
    {
        double user = metrics.getDouble("a");
        double nice = metrics.getDouble("b");
        double system = metrics.getDouble("c");
        double idle = metrics.getDouble("d");
        double wait = metrics.getDouble("e");
        double irq = metrics.getDouble("f");
        double softIrq = metrics.getDouble("g");

        double cached = metrics.getDouble("h");
        double buffers = metrics.getDouble("i");
        double actual = metrics.getDouble("j");

        double read = metrics.getDouble("k");
        double write = metrics.getDouble("l");

        double rCount = metrics.getDouble("m");
        double wCount = metrics.getDouble("n");

        double in = metrics.getDouble("o");
        double out = metrics.getDouble("p");

        return false;
    }

}
