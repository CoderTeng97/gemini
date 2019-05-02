package com.mm.gemini.base.pojo;

import java.util.HashMap;


public class ResponseMapVO {
    private HashMap<String,Object> map;

    public ResponseMapVO(HashMap<String, Object> map,String msg,int code) {
            map.put("code",code);
            map.put("msg",msg);
            this.map = map;
    }
}
