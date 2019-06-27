package com.whut.monitoring.util;

public class FormatKbUtil {

    public static String formatKb(double kb) {
        double tb = kb / 1024.0 / 1024.0 / 1024.0;
        if(tb >= 1.0) {
            return String.format("%.2f", tb)+ "TB";
        }
        double gb = kb / 1024.0 / 1024.0;
        if(gb >= 1.0) {
            return String.format("%.2f", tb)+ "GB";
        }
        double mb = kb / 1024.0;
        if(mb >= 1.0) {
            return String.format("%.2f", tb)+ "MB";
        }
        return String.format("%.2f", tb)+ "KB";
    }
}
