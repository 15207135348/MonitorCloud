package com.whut.monitoring.entity;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by YY
 */
@Entity
@Table(name = "monitored_host")
public class MonitoredHost {
    @Id
    @GeneratedValue
    private int id;
    @Column(name = "hostname")
    private String hostname;
    @Column(name = "system")
    private String system;
    @Column(name = "mac")
    private String mac;
    @Column(name = "ipv4")
    private String ipv4;
    @Column(name = "cpu")
    private String cpu;
    @Column(name = "ram")
    private String ram;
    @Column(name = "disk")
    private String disk;
    @Column(name = "connected")
    private boolean connected;
    @Column(name = "timestamp")
    private long timestamp;
    @Column(name = "username")
    private String username;


    public MonitoredHost() {
    }

    public MonitoredHost(String hostname, String system, String mac, String ipv4, String cpu, String ram, String disk, boolean connected, long timestamp, String username) {
        this.hostname = hostname;
        this.system = system;
        this.mac = mac;
        this.ipv4 = ipv4;
        this.cpu = cpu;
        this.ram = ram;
        this.disk = disk;
        this.connected = connected;
        this.timestamp = timestamp;
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getIpv4() {
        return ipv4;
    }

    public void setIpv4(String ipv4) {
        this.ipv4 = ipv4;
    }

    public String getCpu() {
        return cpu;
    }

    public void setCpu(String cpu) {
        this.cpu = cpu;
    }

    public String getRam() {
        return ram;
    }

    public void setRam(String ram) {
        this.ram = ram;
    }

    public String getDisk() {
        return disk;
    }

    public void setDisk(String disk) {
        this.disk = disk;
    }

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "MonitoredHost{" +
                "id=" + id +
                ", hostname='" + hostname + '\'' +
                ", system='" + system + '\'' +
                ", mac='" + mac + '\'' +
                ", ipv4='" + ipv4 + '\'' +
                ", cpu='" + cpu + '\'' +
                ", ram='" + ram + '\'' +
                ", disk='" + disk + '\'' +
                ", connected=" + connected +
                ", timestamp=" + timestamp +
                ", username='" + username + '\'' +
                '}';
    }
}