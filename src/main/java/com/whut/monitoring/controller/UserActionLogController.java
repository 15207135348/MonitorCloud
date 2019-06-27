package com.whut.monitoring.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mongodb.client.FindIterable;
import com.whut.common.dao.mongodb.IMongoDBDao;
import com.whut.common.util.TimeFormatUtil;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/log")
public class UserActionLogController {

    private final
    IMongoDBDao mongoDBDao;
    @Autowired
    public UserActionLogController(IMongoDBDao mongoDBDao) {
        this.mongoDBDao = mongoDBDao;
    }

    @PostMapping(value = "/findLog")
    @ResponseBody
    public Object findLog(HttpServletRequest request){

        String mac = request.getParameter("mac");
        String time = request.getParameter("time");
        long timestamp = TimeFormatUtil.date2Timestamp(time,null);
        FindIterable<Document> iterable = mongoDBDao.findDocumentByTimestamp(mac,timestamp);
        JSONObject object = new JSONObject();
        JSONArray array = new JSONArray();
        for(Document document : iterable) {
            document.remove("_id");
            array.add(document.toJson());
        }
        object.put("code",0);
        object.put("msg","");
        object.put("count",array.size());
        object.put("data",array);
        return object;
    }
}
