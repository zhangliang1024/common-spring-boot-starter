package com.zhliang.pzy.common.core.protocol.param;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 获取列表的分页请求数据
 */
@ApiModel(value = "获取列表的分页请求数据")
public class QueryParam {
    /**
     * 默认页码，第一页
     */
    private static final int DEFAULT_CURRENT_PAGE = 1;
    /**
     * 默认分页10条记录
     */
    private static final int DEFAULT_PAGE_SIZE = 10;

    /**
     * 当前页码,从1开始，默认1
     */
    @ApiModelProperty(value = "页号", dataType = "int", notes = "首页从1开始")
    protected int pageNum = DEFAULT_CURRENT_PAGE;
    /**
     * 分页大小,默认10
     */
    @ApiModelProperty(value = "页码", dataType = "int", notes = "默认一页显示10个数据")
    protected int pageSize = DEFAULT_PAGE_SIZE;

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

}
