package com.pointsearch.backend.datasource;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pointsearch.backend.model.dto.post.PostQueryRequest;
import com.pointsearch.backend.model.entity.Post;
import com.pointsearch.backend.model.vo.PostVO;
import com.pointsearch.backend.service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 继承DataSource<T>为适配器模式
 * 帖子服务实现
 */
@Service
@Slf4j
public class PostDataSource implements DataSource<PostVO> {

    @Resource
    private PostService postService;

    @Override
    public Page<PostVO> doSearch(String searchText, long pageNum, long pageSize,HttpServletRequest request) {
        // 获取构造参数
        PostQueryRequest postQueryRequest = new PostQueryRequest();
        postQueryRequest.setSearchText(searchText);
        postQueryRequest.setCurrent(pageNum);
        postQueryRequest.setPageSize(pageSize);
        // TODO
        Page<Post> postPage = postService.searchFromEs(postQueryRequest);
        return postService.getPostVOPage(postPage, request);
    }
}




