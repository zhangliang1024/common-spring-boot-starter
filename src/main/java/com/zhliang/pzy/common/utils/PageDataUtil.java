package com.zhliang.pzy.common.utils;

import com.github.pagehelper.Page;
import com.zhliang.pzy.common.core.protocol.response.QueryData;

import java.util.List;

public class PageDataUtil {

    public static QueryData wrapPageDataDTO(Page page, List list) {
        QueryData data = new QueryData();
        data.setPageNum(page.getPageNum());
        data.setPageSize(page.getPageSize());
        data.setTotal(page.getTotal());
        data.setList(list);
        return data;
    }
}
