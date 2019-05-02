package com.mm.gemini.helper.utils;

import com.alibaba.fastjson.JSON;

import java.util.HashMap;

/**
 * @author qiulong
 * @date 2018/8/24
 */
public class ResponseUtil extends HashMap<String, Object> {
    private static final long serialVersionUID = -4084284580782416020L;


    public static ResponseUtil success() {
        ResponseUtil response = new ResponseUtil();
        response.put("status", true);
        response.put("reason", "操作成功");
        return response;
    }

    public static ResponseUtil success(Long pageNum, Long pageSize, Long pages, Object records) {
        ResponseUtil response = new ResponseUtil();
        response.put("pageNum", pageNum);
        response.put("pageSize", pageSize);
        response.put("pages", pages);
        response.put("records", records);
        response.put("status", true);
        response.put("reason", "操作成功");
        return response;
    }

    public static ResponseUtil success(Object records) {
        ResponseUtil response = new ResponseUtil();
        response.put("records", records);
        response.put("status", true);
        response.put("reason", "操作成功");
        return response;
    }

    public static ResponseUtil fail() {
        ResponseUtil response = new ResponseUtil();
        response.put("status", false);
        response.put("reason", "操作失败");
        return response;
    }

    public static ResponseUtil fail404() {
        ResponseUtil response = new ResponseUtil();
        response.put("status", false);
        response.put("reason", "404未找到");
        return response;
    }

    public static ResponseUtil error(String reason) {
        ResponseUtil response = new ResponseUtil();
        response.put("status", false);
        response.put("reason", "操作错误：" + reason);
        return response;
    }

    @Override
    public ResponseUtil put(String key, Object value) {
        super.put(key, value);
        return this;
    }

}
