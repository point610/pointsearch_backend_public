package com.pointsearch.backend.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pointsearch.backend.model.dto.postcomment.PostCommentAddRequest;
import com.pointsearch.backend.model.dto.postcomment.PostCommentQueryRequest;
import com.pointsearch.backend.model.entity.PostComment;
import com.pointsearch.backend.model.vo.PostCommentVO;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Administrator
 * @description 针对表【post_comment(帖子评论)】的数据库操作Service
 * @createDate 2023-08-21 02:15:59
 */
public interface PostCommentService extends IService<PostComment> {
    /**
     * 获取查询条件
     *
     * @param postCommentQueryRequest
     * @return com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<com.pointapi.backend.model.entity.PostComment>
     * @Author point
     * @Date 12:23 2023/8/21
     **/
    QueryWrapper<PostComment> getQueryWrapper(PostCommentQueryRequest postCommentQueryRequest);

    /**
     * 添加评论
     *
     * @param postCommentAddRequest
     * @param request
     * @return java.lang.Boolean
     * @Author point
     * @Date 12:23 2023/8/21
     **/
    int addPostComment(PostCommentAddRequest postCommentAddRequest, HttpServletRequest request);

    /**
     * 处理帖子评论的分页对象
     *
     * @param postCommentPage
     * @param request
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<com.pointapi.backend.model.vo.PostCommentVO>
     * @Author point
     * @Date 12:42 2023/8/21
     **/
    Page<PostCommentVO> getPostCommentVOPage(Page<PostComment> postCommentPage, HttpServletRequest request);

    /**
     * 封装了事务的方法
     * 操作了两张表，PostFavour , Post
     *
     * @param userId
     * @param postCommentAddRequest
     * @return int
     * @Author point
     * @Date 15:11 2023/8/21
     **/
    int addPostCommentInner(Long userId, PostCommentAddRequest postCommentAddRequest);
}
