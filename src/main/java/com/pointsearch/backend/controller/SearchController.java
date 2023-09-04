package com.pointsearch.backend.controller;

import com.pointsearch.backend.common.BaseResponse;
import com.pointsearch.backend.common.ResultUtils;
import com.pointsearch.backend.manager.SearchFacade;
import com.pointsearch.backend.model.dto.search.SearchRequest;
import com.pointsearch.backend.model.vo.SearchVO;
import com.pointsearch.backend.service.PictureService;
import com.pointsearch.backend.service.PostService;
import com.pointsearch.backend.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 图片接口
 */
@RestController
@RequestMapping("/search")
@Slf4j
public class SearchController {

    @Resource
    private UserService userService;

    @Resource
    private PostService postService;

    @Resource
    private PictureService pictureService;

    @Resource
    private SearchFacade searchFacade;

    @PostMapping("/all")
    public BaseResponse<SearchVO> searchAll(@RequestBody SearchRequest searchRequest, HttpServletRequest request) {
        return ResultUtils.success(searchFacade.searchAll(searchRequest, request));
    }

}
