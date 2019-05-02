package com.mm.gemini.base.pojo;


import lombok.Data;

import java.util.List;

@Data
public class ResponseListVO<T> extends ResponseBaseVO {
    private long pageNum = 1;//当前页
    private long pageSize = 10;// 分页大小
    private long pages;//总页数
    /*private long total;//总记录数
    private long NextPage;//下一页
    private long PrePage;//上一页
    private long firstPage;//第一页
    private long lastPage;//最后页*/
    private List<T> records;

    public ResponseListVO(long pageNum, long pageSize, long pages, List<T> records) {
        this.pageNum =pageNum;
        this.pageSize =pageSize;
        this.pages =pages;
        this.records = records;
    }
}
