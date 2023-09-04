package com.pointsearch.backend.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pointsearch.backend.model.entity.Picture;

/**
 * 图片服务
 */
public interface PictureService {

    /**
     * 使用爬虫来搜索bing搜索引擎中的图片数据
     *
     * @param searchText
     * @param pageNum
     * @param pageSize
     * @Author point
     * @Date 0:22 2023/8/25
     **/
    Page<Picture> searchPicture(String searchText, long pageNum, long pageSize);
}
