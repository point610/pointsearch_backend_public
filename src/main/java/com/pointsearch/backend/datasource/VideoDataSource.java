package com.pointsearch.backend.datasource;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * 继承DataSource<T>为适配器模式
 * 视频数据源
 */
@Component
public class VideoDataSource implements DataSource<Object> {

    @Override
    public Page<Object> doSearch(String searchText, long pageNum, long pageSize, HttpServletRequest request) {
        return null;
    }
}
