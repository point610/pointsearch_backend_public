package com.pointsearch.backend.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.gson.Gson;

import com.pointsearch.backend.annotation.AuthCheck;
import com.pointsearch.backend.common.BaseResponse;
import com.pointsearch.backend.common.DeleteRequest;
import com.pointsearch.backend.common.ErrorCode;
import com.pointsearch.backend.common.ResultUtils;
import com.pointsearch.backend.constant.UserConstant;
import com.pointsearch.backend.exception.BusinessException;
import com.pointsearch.backend.exception.ThrowUtils;
import com.pointsearch.backend.model.dto.postcomment.PostCommentAddRequest;
import com.pointsearch.backend.model.dto.postcomment.PostCommentQueryRequest;
import com.pointsearch.backend.model.dto.postcomment.PostCommentUpdateRequest;
import com.pointsearch.backend.model.entity.PostComment;
import com.pointsearch.backend.model.entity.User;
import com.pointsearch.backend.model.enums.PostCommentEnum;
import com.pointsearch.backend.model.vo.PostCommentVO;
import com.pointsearch.backend.service.PostCommentService;
import com.pointsearch.backend.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 帖子接口
 */
@RestController
@RequestMapping("/postcomment")
@Slf4j
public class PostCommentController {

    @Resource
    private PostCommentService postCommentService;

    @Resource
    private UserService userService;

    private final static Gson GSON = new Gson();

    // region 增删改查

    /**
     * 创建
     *
     * @param postCommentAddRequest
     * @param request
     * @return com.point.pointapicommon.common.BaseResponse<java.lang.Integer>
     * @Author point
     * @Date 15:20 2023/8/21
     **/
    @PostMapping("/add")
    public BaseResponse<Integer> addPostComment(@RequestBody PostCommentAddRequest postCommentAddRequest, HttpServletRequest request) {
        // 检查参数
        if (postCommentAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        return ResultUtils.success(postCommentService.addPostComment(postCommentAddRequest, request));
    }

    /**
     * 删除
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    @PostMapping("/delete")
    public BaseResponse<Boolean> deletePostComment(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        // 校验参数
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        // 判断是否存在
        long id = deleteRequest.getId();
        PostComment oldPostComment = postCommentService.getById(id);
        ThrowUtils.throwIf(oldPostComment == null, ErrorCode.NOT_FOUND_ERROR);

        // 删除数据库中的数据
        return ResultUtils.success(postCommentService.removeById(id));
    }

    /**
     * 批量删除
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    @PostMapping("/list/delete")
    public BaseResponse<Boolean> deletePostComments(@RequestBody List<DeleteRequest> deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        boolean result = postCommentService.removeByIds(deleteRequest);
        return ResultUtils.success(result);
    }


    /**
     * 更新（仅管理员）
     *
     * @param postCommentUpdateRequest
     * @return com.point.pointapicommon.common.BaseResponse<java.lang.Boolean>
     * @Author point
     * @Date 12:16 2023/8/21
     **/
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updatePostComment(@RequestBody PostCommentUpdateRequest postCommentUpdateRequest) {
        // 校验参数
        if (postCommentUpdateRequest == null || postCommentUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        // 赋值参数
        PostComment postComment = new PostComment();
        BeanUtils.copyProperties(postCommentUpdateRequest, postComment);

        // 判断是否存在
        long id = postCommentUpdateRequest.getId();
        PostComment oldPostComment = postCommentService.getById(id);
        ThrowUtils.throwIf(oldPostComment == null, ErrorCode.NOT_FOUND_ERROR);

        // 修改数据库
        boolean result = postCommentService.updateById(postComment);
        return ResultUtils.success(result);
    }

    /**
     * 根据 id 获取
     *
     * @param id
     * @return
     */
    @GetMapping("/get")
    public BaseResponse<PostComment> getPostCommentById(long id, HttpServletRequest request) {
        // 校验参数
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        PostComment postComment = postCommentService.getById(id);
        if (postComment == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        return ResultUtils.success(postComment);
    }

    /**
     * 根据post的id获取其所有的评论
     *
     * @param postCommentQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page/vo")
    public BaseResponse<Page<PostCommentVO>> listPostCommentVOByPage(@RequestBody PostCommentQueryRequest postCommentQueryRequest, HttpServletRequest request) {
        long current = postCommentQueryRequest.getCurrent();
        long size = postCommentQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<PostComment> postCommentPage = postCommentService.page(new Page<>(current, size),
                postCommentService.getQueryWrapper(postCommentQueryRequest).eq("fatherId", PostCommentEnum.TOP_COMMENT));

        // TODO
        return ResultUtils.success(postCommentService.getPostCommentVOPage(postCommentPage, request));
    }

    /**
     * 分页获取当前用户对帖子的评论
     *
     * @param postCommentQueryRequest
     * @param request
     * @return com.point.pointapicommon.common.BaseResponse<com.baomidou.mybatisplus.extension.plugins.pagination.Page < com.pointapi.backend.model.entity.PostComment>>
     * @Author point
     * @Date 12:24 2023/8/21
     **/
    @PostMapping("/my/list/page")
    public BaseResponse<Page<PostComment>> listMyPostCommentByPage(@RequestBody PostCommentQueryRequest postCommentQueryRequest, HttpServletRequest request) {
        if (postCommentQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        User loginUser = userService.getLoginUser(request);
        postCommentQueryRequest.setUserId(loginUser.getId());
        long current = postCommentQueryRequest.getCurrent();
        long size = postCommentQueryRequest.getPageSize();

        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);

        Page<PostComment> postCommentPage = postCommentService.page(new Page<>(current, size), postCommentService.getQueryWrapper(postCommentQueryRequest));
        return ResultUtils.success(postCommentPage);
    }

    // endregion

}
