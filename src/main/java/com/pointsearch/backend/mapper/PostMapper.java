package com.pointsearch.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pointsearch.backend.model.entity.Post;

import java.util.Date;
import java.util.List;

/**
* @author Administrator
* @description 针对表【post(帖子)】的数据库操作Mapper
* @createDate 2023-08-21 14:53:08
* @Entity generator.domain.Post
*/
public interface PostMapper extends BaseMapper<Post> {
    /**
     * 查询帖子列表（包括已被删除的数据）
     */
    List<Post> listPostWithDelete(Date minUpdateTime);

}




