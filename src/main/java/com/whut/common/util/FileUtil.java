package com.whut.common.util;

import java.io.*;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

public class FileUtil {

    public static void createDir(String name)
    {
        File file=new File(name);
        if(!file.exists()){//如果文件夹不存在
            file.mkdir();//创建文件夹
        }
    }
    public static String[] listFiles(String path)
    {
        File file = new File(path);
        if (file.isDirectory()) {
            return file.list();
        }
        return null;
    }
    public static void appendFile(String filename,String content)
    {
        File file = new File(filename);
        try {
            FileWriter writer = new FileWriter(file,true);
            writer.write(content.concat("\r\n"));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void createFile(String filename,String content)
    {
        File file = new File(filename);
        try {
            FileWriter writer = new FileWriter(file);
            writer.write(content);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
