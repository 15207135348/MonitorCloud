package com.whut.common.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CompareOperator;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.util.Map;

/**
 * Created by 杨赟 on 2018-07-16.
 */
public class HBaseDaoUtil {

    //增加
    public static Put cellPut(String rowKey, String family, String qualifier, String value)
    {
        return new Put(Bytes.toBytes(rowKey)).
                addColumn(family.getBytes(), qualifier.getBytes(), value.getBytes());
    }
    public static Put rowPut(String rowKey, String family, Map<String,Object> qv)
    {
        Put put = new Put(Bytes.toBytes(rowKey));
        for(String q : qv.keySet()) {
            put.addColumn(family.getBytes(),q.getBytes(),Bytes.toBytes(String.valueOf(qv.get(q))));
        }
        return put;
    }
    public static Put rowPut(String rowKey, Map<String,Map<String,Object>> fqv)
    {
        Put put = new Put(Bytes.toBytes(rowKey));
        for(String f : fqv.keySet()){
            Map<String,Object> qv = fqv.get(f);
            for(String q : qv.keySet()) {
                put.addColumn(f.getBytes(),q.getBytes(),Bytes.toBytes(String.valueOf(qv.get(q))));
            }
        }
        return put;
    }
    //删除
    public static Delete cellDelete(String rowKey, String family, String qualifier)
    {
        return new Delete(Bytes.toBytes(rowKey)).
                addColumn(family.getBytes(), qualifier.getBytes());
    }
    public static Delete rowDelete(String rowKey)
    {
        return new Delete(Bytes.toBytes(rowKey));
    }
    //查
    public static Get cellGet(String rowKey, String family, String qualifier)
    {
        return new Get(Bytes.toBytes(rowKey)).
                addColumn(family.getBytes(),qualifier.getBytes());
    }
    public static Get rowGet(String rowKey)
    {
        return new Get(Bytes.toBytes(rowKey));
    }
    //扫描
    public static Scan setReversal(Scan scan) { return scan.setReversed(true); }
    public static Scan rowStart(Scan scan, String start)
    {
        return scan.withStartRow(start.getBytes());
    }
    public static Scan rowStop(Scan scan, String stop)
    {
        return scan.withStopRow(stop.getBytes());
    }
    public static Scan rowPrefix(Scan scan, String prefix)
    {
        return scan.setFilter(new RowFilter(CompareOperator.EQUAL,
                new BinaryPrefixComparator(prefix.getBytes())));
    }
    public static Scan rowSubstring(Scan scan, String substring)
    {
        return scan.setFilter(new RowFilter(CompareOperator.EQUAL,
                new SubstringComparator(substring)));
    }
    public static Scan rowRegex(Scan scan, String regex)
    {
        return scan.setFilter(new RowFilter(CompareOperator.EQUAL,
                new RegexStringComparator(regex)));
    }
    public static Scan addFamily(Scan scan, String family)
    {
        return scan.addFamily(family.getBytes());
    }
    public static Scan addColumn(Scan scan, String family, String qualifier)
    {
        return scan.addColumn(family.getBytes(),qualifier.getBytes());
    }
    public static Scan qualifierPrefix(Scan scan, String prefix)
    {
        return scan.setFilter(new ColumnPrefixFilter(prefix.getBytes()));
    }
    public static Scan qualifierRange(Scan scan, String mix, String max)
    {
        return scan.setFilter(new ColumnRangeFilter(mix.getBytes(),
                true,max.getBytes(),true));
    }
    public static Scan qualifierRegex(Scan scan, String regex)
    {
        return scan.setFilter(new QualifierFilter(CompareOperator.EQUAL,
                new RegexStringComparator(regex)));
    }
    public static Scan qualifierPagination(Scan scan, String start, int limit)
    {
        return scan.setFilter(new ColumnPaginationFilter(limit,start.getBytes()));
    }
    public static Scan qualifierPagination(Scan scan, int offset, int limit)
    {
        return scan.setFilter(new ColumnPaginationFilter(limit,offset));
    }
    public static Scan qualifierValueGreater(Scan scan, String family, String qualifier, String start)
    {
        return scan.setFilter(new ColumnValueFilter(
                family.getBytes(),
                qualifier.getBytes(),
                CompareOperator.GREATER_OR_EQUAL,
                start.getBytes()));
    }
    public static Scan qualifierValueLess(Scan scan, String family, String qualifier, String end)
    {
        return scan.setFilter(new ColumnValueFilter(
                family.getBytes(),
                qualifier.getBytes(),
                CompareOperator.LESS_OR_EQUAL,
                end.getBytes()));
    }
    public static Scan valueGreater(Scan scan, String start)
    {
        return scan.setFilter(new ValueFilter(
                CompareOperator.GREATER_OR_EQUAL,new BinaryComparator(start.getBytes())));
    }
    public static Scan valueLess(Scan scan, String end)
    {
        return scan.setFilter(new ValueFilter(
                CompareOperator.LESS_OR_EQUAL,new BinaryComparator(end.getBytes())));
    }

    private static JSONObject parseRow(Result row)
    {
        JSONObject jsonObject = new JSONObject();
        JSONObject object = new JSONObject();
        String timestamp = Bytes.toString(row.getRow()).substring(17,27);
        Cell[] cells = row.rawCells();
        if(cells !=null && cells.length>0)
        {
            for (Cell cell : cells) {
                String q = Bytes.toString(cell.getQualifierArray(),cell.getQualifierOffset(),cell.getQualifierLength());
                String v = Bytes.toString(cell.getValueArray(), cell.getValueOffset(), cell.getValueLength());
                object.put(q,v);
            }
            jsonObject.put("O",object);
            jsonObject.put("T",timestamp);
        }
        return jsonObject;
    }

    public static JSONArray parseScanner(ResultScanner scanner)
    {
        JSONArray array = new JSONArray();
        for (Result result : scanner) {
            array.add(parseRow(result));
        }
        return array;
    }
}
