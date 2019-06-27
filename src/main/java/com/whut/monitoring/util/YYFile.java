//package com.whut.monitoring.util;
//
//import com.mathworks.toolbox.javabuilder.MWClassID;
//import com.mathworks.toolbox.javabuilder.MWNumericArray;
//
//import java.io.*;
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * @Auther: 13657
// * @Date: 2019/1/3 19:20
// * @Description:
// */
//public class YYFile {
//
//    public static MWNumericArray loadFromCSV(String filename)
//    {
//        File file = new File(filename);
//        try {
//            FileReader reader = new FileReader(file);
//            BufferedReader bufferedReader = new BufferedReader(reader);
//            String line = bufferedReader.readLine();
//            int w = line.split(",").length;
//            List<double[]> arrayList = new ArrayList<>();
//            while (line!=null)
//            {
//
//                String[] strings = line.split(",");
//                if (w != strings.length) continue;
//                double[] doubles = new double[w];
//                for (int i = 0; i!=w;++i)
//                {
//                    doubles[i] = Double.parseDouble(strings[i]);
//                }
//                arrayList.add(doubles);
//                line = bufferedReader.readLine();
//            }
//            return new MWNumericArray(arrayList.toArray(new double[arrayList.size()][w]), MWClassID.DOUBLE);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    public static MWNumericArray loadFromCSV(String filename, int col1, int col2) {
//
//        if (col1 <= 0 || col2 <= 0 || col1 > col2) return null;
//
//        File file = new File(filename);
//        try {
//
//            int w = col2 - col1 + 1;
//            List<double[]> arrayList = new ArrayList<>();
//            FileReader reader = new FileReader(file);
//            BufferedReader bufferedReader = new BufferedReader(reader);
//            String line;
//            while ((line = bufferedReader.readLine())!=null)
//            {
//                int y1 = 1;
//                int y2 = 0;
//                String[] strings = line.split(",");
//                double[] array = new double[w];
//                for (int i = 0; i!=strings.length;++i)
//                {
//                    if (y1<col1){
//                        ++y1;
//                    }
//                    else if (y1>col2) {
//                        break;
//                    }
//                    else {
//                        array[y2] = Double.parseDouble(strings[i]);
//                        ++y1;
//                        ++y2;
//                    }
//                }
//                arrayList.add(array);
//            }
//            return new MWNumericArray(arrayList.toArray(new double[arrayList.size()][w]), MWClassID.DOUBLE);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    public static MWNumericArray loadFromCSV(String filename, int row1, int row2, int col1, int col2)
//    {
//        if (row1 <= 0 || col1 <= 0 || row2 <= 0 || col2 <= 0 || row1 > row2 || col1 > col2) return null;
//
//        File file = new File(filename);
//        try {
//            int h = row2 - row1 + 1;
//            int w = col2 - col1 + 1;
//            double[][] array = new double[h][w];
//            FileReader reader = new FileReader(file);
//            BufferedReader bufferedReader = new BufferedReader(reader);
//            String line;
//            int x1=1;
//            int x2=0;
//            while ((line = bufferedReader.readLine())!=null)
//            {
//                if(x1 < row1){
//                    ++x1;
//                }
//                else if (x1 > row2) {
//                    break;
//                }
//                else {
//                    int y1 = 1;
//                    int y2 = 0;
//                    String[] strings = line.split(",");
//                    for (int i = 0; i!=strings.length;++i)
//                    {
//                        if (y1<col1){
//                            ++y1;
//                        }
//                        else if (y1>col2) {
//                            break;
//                        }
//                        else {
//                            array[x2][y2] = Double.parseDouble(strings[i]);
//                            ++y1;
//                            ++y2;
//                        }
//                    }
//                    ++x1;
//                    ++x2;
//                }
//            }
//            return new MWNumericArray(array,MWClassID.DOUBLE);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//}
