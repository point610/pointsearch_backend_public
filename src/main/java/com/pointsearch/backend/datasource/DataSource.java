package com.pointsearch.backend.datasource;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import javax.servlet.http.HttpServletRequest;

/**
 * 数据源接口（新接入的数据源必须实现）
 * 适配器模式
 *
 * @param <T>
 */
public interface DataSource<T> {

    /**
     * 搜索
     *
     * @param searchText
     * @param pageNum
     * @param pageSize
     * @return
     */
    Page<T> doSearch(String searchText, long pageNum, long pageSize, HttpServletRequest request);
}
