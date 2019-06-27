package com.whut.common.util;

import com.whut.common.config.HDFSConfig;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.io.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 杨赟 on 2018-07-18.
 */
public class HDFSUtil {

    /**
     * @since 2018-07-18
     * @author 杨赟
     * @describe 上传本地文件到HDFS
     * @param local 本地路径
     * @param hdfs DFS路径
     * @return void
     */
    public static void upload(String local, String hdfs){
        FileSystem fs = HDFSConfig.getFileSystem();
        if(fs == null)
            return;
        try {
            fs.copyFromLocalFile(new Path(local),new Path(hdfs));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @since 2018-07-18
     * @author 杨赟
     * @describe 将hdfs上文件下载到本地
     * @param hdfs HDFS路径
     * @param local 本地路径
     * @return void
     */
    public static void download(String hdfs, String local){
        FileSystem fs = HDFSConfig.getFileSystem();
        if(fs == null)
            return;
        try {
            fs.copyToLocalFile(new Path(hdfs), new Path(local));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @since 2018-07-18
     * @author 杨赟
     * @describe 在hdfs目录下面创建文件夹
     * @param name HDFS文件夹名
     * @return void
     */
    public static void createDir(String name){
        FileSystem fs = HDFSConfig.getFileSystem();
        if(fs == null)
            return;
        try {
            fs.mkdirs(new Path(name));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @since 2018-07-18
     * @author 杨赟
     * @describe 创建新的文件
     * @param name 文件名
     * @param content 内容
     * @return void
     */
    public static void createFile(String name, String content) {
        FileSystem fs = HDFSConfig.getFileSystem();
        if(fs == null)
            return;
        FSDataOutputStream os = null;
        try {
            os = fs.create(new Path(name));
            os.write(content.getBytes());
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * @since 2018-07-18
     * @author 杨赟
     * @describe 向文件写入新的数据
     * @param name 文件名
     * @param content 内容
     * @return void
     */
    public static void append(String name, String content){
        FileSystem fs = HDFSConfig.getFileSystem();
        if(fs == null)
            return;
        try {
            if (fs.exists(new Path(name))) {
                try {
                    InputStream in = new ByteArrayInputStream(content.getBytes());
                    OutputStream out = fs.append(new Path(name));
                    IOUtils.copyBytes(in, out, 4096, true);
                    out.close();
                    in.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                createFile(name, content);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @since 2018-07-18
     * @author 杨赟
     * @describe 删除hdfs上的文件或文件夹
     * @param name 文件或文件夹名
     * @return void
     */
    public static void remove(String name) {
        FileSystem fs = HDFSConfig.getFileSystem();
        if(fs == null)
            return;
        try {
            fs.delete(new Path(name),true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @since 2018-07-18
     * @author 杨赟
     * @describe 列出文件
     * @param path HDFS文件夹名
     * @return 文件列表
     */
    public static List<String> listFile(String path) {

        List<String> list = new ArrayList<>();
        FileSystem fs = HDFSConfig.getFileSystem();
        if(fs == null)
            return list;
        FileStatus[] fileStatuses = new FileStatus[0];
        try {
            fileStatuses = fs.listStatus(new Path(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (FileStatus fileStatus : fileStatuses) {
            if(fileStatus.isFile())
                list.add(path);
        }
        return list;
    }

    /**
     * @since 2018-07-18
     * @author 杨赟
     * @describe 列出文件夹
     * @param path HDFS文件夹名
     * @return 文件夹列表
     */
    public static List<String> listDir(String path){

        List<String> list = new ArrayList<>();
        FileSystem fs = HDFSConfig.getFileSystem();
        if(fs == null)
            return list;

        FileStatus[] fileStatuses = new FileStatus[0];
        try {
            fileStatuses = fs.listStatus(new Path(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (FileStatus fileStatus : fileStatuses) {
            if(fileStatus.isDirectory())
                list.add(path);
        }
        return list;
    }
    /**
     * @since 2018-07-18
     * @author 杨赟
     * @describe 读文件
     * @param name 文件名
     * @return byte[]
     */
    public static byte[] readFile(String name){

        FileSystem fs = HDFSConfig.getFileSystem();
        if(fs == null)
            return null;
        Path path = new Path(name);
        try {
            if (fs.exists(path)) {
                FSDataInputStream is = fs.open(path);
                FileStatus stat = fs.getFileStatus(path);
                byte[] buffer = new byte[Integer.parseInt(String.valueOf(stat.getLen()))];
                is.readFully(0, buffer);
                is.close();
                return buffer;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static void main(String[] args){
        HDFSUtil.download("/dbcarbon/Y0601_20180717000000_20180717010000.csv","/home/yy/桌面");
    }
}
