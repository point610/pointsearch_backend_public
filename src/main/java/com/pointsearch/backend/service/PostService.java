package com.pointsearch.backend.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pointsearch.backend.model.dto.post.PostQueryRequest;
import com.pointsearch.backend.model.entity.Post;
import com.pointsearch.backend.model.vo.PostVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 帖子服务
 */
public interface PostService extends IService<Post> {

    /**
     * 校验
     *
     * @param post
     * @param add
     */
    void validPost(Post post, boolean add);

    /**
     * 获取查询条件
     *
     * @param postQueryRequest
     * @return
     */
    QueryWrapper<Post> getQueryWrapper(PostQueryRequest postQueryRequest);

    /**
     * 从 ES 查询
     *
     * @param postQueryRequest
     * @return
     */
    Page<Post> searchFromEs(PostQueryRequest postQueryRequest);

    /**
     * 获取帖子封装
     *
     * @param post
     * @param request
     * @return
     */
    PostVO getPostVO(Post post, HttpServletRequest request);

    /**
     * 分页获取帖子封装
     *
     * @param postPage
     * @param request
     * @return
     */
    Page<PostVO> getPostVOPage(Page<Post> postPage, HttpServletRequest request);

    /**
     * 根据标签查询帖子
     *
     * @param tagNameList
     * @return java.util.List<com.point.pointapicommon.model.entity.Post>
     * @Author point
     * @Date 21:52 2023/8/19
     **/
    List<Post> searchPostsByTags(List<String> tagNameList);

}
