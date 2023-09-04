package com.pointsearch.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pointsearch.backend.common.ErrorCode;
import com.pointsearch.backend.exception.BusinessException;
import com.pointsearch.backend.mapper.PostCommentMapper;
import com.pointsearch.backend.model.dto.postcomment.PostCommentAddRequest;
import com.pointsearch.backend.model.dto.postcomment.PostCommentQueryRequest;
import com.pointsearch.backend.model.entity.PostComment;
import com.pointsearch.backend.model.entity.User;
import com.pointsearch.backend.model.enums.PostCommentEnum;
import com.pointsearch.backend.model.vo.PostCommentVO;
import com.pointsearch.backend.model.vo.UserVO;
import com.pointsearch.backend.service.PostCommentService;
import com.pointsearch.backend.service.PostService;
import com.pointsearch.backend.service.UserService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Administrator
 * @description 针对表【post_comment(帖子评论)】的数据库操作Service实现
 * @createDate 2023-08-21 02:15:59
 */
@Service
public class PostCommentServiceImpl extends ServiceImpl<PostCommentMapper, PostComment> implements PostCommentService {

    @Resource
    private UserService userService;

    @Resource
    private PostService postService;



    /**
     * 添加帖子评论
     *
     * @param postCommentAddRequest
     * @param request
     * @return java.lang.Boolean
     * @Author point
     * @Date 12:06 2023/8/21
     **/
    @Override
    public int addPostComment(PostCommentAddRequest postCommentAddRequest, HttpServletRequest request) {

        // 检验参数
        String content = postCommentAddRequest.getContent();
        Long postId = postCommentAddRequest.getPostId();
        if (StringUtils.isBlank(content) || StringUtils.isEmpty(content) || postId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        // 获取评论的user
        User loginUser = userService.getLoginUser(request);
        Long userId = loginUser.getId();
        // 每个用户串行帖子收藏
        // 锁必须要包裹住事务方法
        PostCommentService postCommentService = (PostCommentService) AopContext.currentProxy();

        synchronized (String.valueOf(userId).intern()) {
            return postCommentService.addPostCommentInner(userId, postCommentAddRequest);
        }
    }
    /**
     * 封装了事务的方法
     * 操作了两张表，PostComment , Post
     *
     * @param userId
     * @param postCommentAddRequest
     * @return int
     * @Author point
     * @Date 15:10 2023/8/21
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int addPostCommentInner(Long userId, PostCommentAddRequest postCommentAddRequest) {
        // 赋值对象
        PostComment postComment = new PostComment();
        BeanUtils.copyProperties(postCommentAddRequest, postComment);
        Long fatherId = postCommentAddRequest.getFatherId();
        if (fatherId == null) {
            // 评论的是帖子
            postComment.setFatherId(PostCommentEnum.TOP_COMMENT.getValue());
        } else {
            // 评论的是帖子中的评论
            postComment.setFatherId(fatherId);
        }
        postComment.setUserId(userId);

        boolean result = this.save(postComment);
        if (result) {
            // 修改post表中的评论的数量
            result = postService.update().eq("id", postCommentAddRequest.getPostId())
                    .setSql("commentNum = commentNum + 1")
                    .update();
            return result ? 1 : 0;
        } else {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }

    }

    /**
     * 获取查询包装类
     *
     * @param postCommentQueryRequest
     * @return com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<com.pointapi.backend.model.entity.PostComment>
     * @Author point
     * @Date 12:26 2023/8/21
     **/
    @Override
    public QueryWrapper<PostComment> getQueryWrapper(PostCommentQueryRequest postCommentQueryRequest) {
        QueryWrapper<PostComment> queryWrapper = new QueryWrapper<>();
        if (postCommentQueryRequest == null) {
            return queryWrapper;
        }

        // 获取各种参数
        String sortField = postCommentQueryRequest.getSortField();
        String sortOrder = postCommentQueryRequest.getSortOrder();
        Long id = postCommentQueryRequest.getId();
        Long postId = postCommentQueryRequest.getPostId();
        String content = postCommentQueryRequest.getContent();
        Long userId = postCommentQueryRequest.getUserId();
        Long notId = postCommentQueryRequest.getNotId();
        // 拼接查询条件
        queryWrapper.like(StringUtils.isNotBlank(content), "content", content);
        queryWrapper.ne(ObjectUtils.isNotEmpty(notId), "id", notId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(id), "id", id);
        queryWrapper.eq(ObjectUtils.isNotEmpty(postId), "postId", postId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        queryWrapper.orderByAsc("createTime");

        return queryWrapper;
    }

    /**
     * 处理帖子评论的分页对象
     *
     * @param postCommentPage
     * @param request
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<com.pointapi.backend.model.vo.PostCommentVO>
     * @Author point
     * @Date 12:42 2023/8/21
     **/
    @Override
    public Page<PostCommentVO> getPostCommentVOPage(Page<PostComment> postCommentPage, HttpServletRequest request) {

        // 获取父评论
        List<PostComment> fatherComment = postCommentPage.getRecords();
        Page<PostCommentVO> postCommentVOPage = new Page<>(postCommentPage.getCurrent(), postCommentPage.getSize(), postCommentPage.getTotal());
        // 父评论为空，直接返回
        if (CollectionUtils.isEmpty(fatherComment)) {
            return postCommentVOPage;
        }
        List<Long> fatherIds = fatherComment.stream().map(postComment -> postComment.getId()).collect(Collectors.toList());
        Set<Long> userIds = fatherComment.stream().map(PostComment::getUserId).collect(Collectors.toSet());

        // 找子评论
        QueryWrapper<PostComment> postCommentQueryWrapper = new QueryWrapper<>();
        postCommentQueryWrapper.in("fatherId", fatherIds);
        List<PostComment> sonComment = list(postCommentQueryWrapper);
        userIds.addAll(sonComment.stream().map(PostComment::getUserId).collect(Collectors.toSet()));

        // 找用户信息
        List<User> userList = userService.listByIds(userIds);
        Map<Long, List<UserVO>> userVOIds = userService.getUserVO(userList).stream().collect(Collectors.groupingBy(UserVO::getId));

        // 为父子评论赋值用户信息
        List<PostCommentVO> fatherPostCommentVOS = fatherComment.stream().map(postComment -> {
            PostCommentVO postCommentVO = new PostCommentVO();
            // 赋值参数
            BeanUtils.copyProperties(postComment, postCommentVO);
            postCommentVO.setSon(new ArrayList<>());
            postCommentVO.setUser(userVOIds.get(postComment.getUserId()).get(0));
            // 返回
            return postCommentVO;
        }).collect(Collectors.toList());
        Map<Long, List<PostCommentVO>> fatherVOIds = fatherPostCommentVOS.stream().collect(Collectors.groupingBy(PostCommentVO::getId));

        List<PostCommentVO> sonPostCommentVOS = sonComment.stream().map(postComment -> {
            PostCommentVO postCommentVO = new PostCommentVO();
            // 赋值参数
            BeanUtils.copyProperties(postComment, postCommentVO);
            postCommentVO.setUser(userVOIds.get(postComment.getUserId()).get(0));
            // 返回
            return postCommentVO;
        }).collect(Collectors.toList());

        // 将子评论加入父评论中，遍历子评论，将子评论加入父评论中
        sonPostCommentVOS.stream().forEach(postCommentVO -> {
            PostCommentVO fatherVO = fatherVOIds.get(postCommentVO.getFatherId()).get(0);
            fatherVO.getSon().add(postCommentVO);
        });

        // 返回
        return postCommentVOPage.setRecords(fatherPostCommentVOS);
    }
}




