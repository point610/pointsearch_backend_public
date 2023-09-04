package com.pointsearch.backend.manager;

import com.pointsearch.backend.model.dto.search.SearchRequest;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pointsearch.backend.common.ErrorCode;
import com.pointsearch.backend.datasource.*;
import com.pointsearch.backend.exception.BusinessException;
import com.pointsearch.backend.exception.ThrowUtils;
import com.pointsearch.backend.model.dto.post.PostQueryRequest;
import com.pointsearch.backend.model.dto.user.UserQueryRequest;
import com.pointsearch.backend.model.entity.Picture;
import com.pointsearch.backend.model.enums.SearchTypeEnum;
import com.pointsearch.backend.model.vo.PostVO;
import com.pointsearch.backend.model.vo.SearchVO;
import com.pointsearch.backend.model.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.CompletableFuture;

/**
 * 搜索门面
 */
@Component
@Slf4j
public class SearchFacade {

    @Resource
    private PostDataSource postDataSource;

    @Resource
    private UserDataSource userDataSource;

    @Resource
    private PictureDataSource pictureDataSource;

    @Resource
    private DataSourceRegistry dataSourceRegistry;

    /**
     * 前端同一调用searchAll，使用了门面模式
     *
     * @param searchRequest
     * @param request
     * @Author point
     * @Date 15:50 2023/8/19
     **/
    public SearchVO searchAll(@RequestBody SearchRequest searchRequest, HttpServletRequest request) {
        // 搜索的类型
        String type = searchRequest.getType();
        SearchTypeEnum searchTypeEnum = SearchTypeEnum.getEnumByValue(type);
        ThrowUtils.throwIf(StringUtils.isBlank(type), ErrorCode.PARAMS_ERROR);
        // 搜索内容
        String searchText = searchRequest.getSearchText();
        // 分页信息
        long current = searchRequest.getCurrent();
        long pageSize = searchRequest.getPageSize();

        // 查询的类型为null，表示需要搜索出所有数据
        if (searchTypeEnum == null) {
            // 用户的信息
            CompletableFuture<Page<UserVO>> userTask = CompletableFuture.supplyAsync(() -> {
                UserQueryRequest userQueryRequest = new UserQueryRequest();
                userQueryRequest.setUserName(searchText);
                Page<UserVO> userVOPage = userDataSource.doSearch(searchText, current, pageSize, request);
                return userVOPage;
            });

            // 帖子的信息
            CompletableFuture<Page<PostVO>> postTask = CompletableFuture.supplyAsync(() -> {
                PostQueryRequest postQueryRequest = new PostQueryRequest();
                postQueryRequest.setSearchText(searchText);
                Page<PostVO> postVOPage = postDataSource.doSearch(searchText, current, pageSize, request);
                return postVOPage;
            });

            // 图片的信息
            CompletableFuture<Page<Picture>> pictureTask = CompletableFuture.supplyAsync(() -> {
                Page<Picture> picturePage = pictureDataSource.doSearch(searchText, 1, 10, request);
                return picturePage;
            });

            // 使用 CompletableFuture.allOf 方法来等待多个 CompletableFuture 对象的完成，
            // 并使用 join 方法来阻塞当前线程直到所有任务完成。
            CompletableFuture.allOf(userTask, postTask, pictureTask).join();
            try {
                // 获取查询的对象
                Page<UserVO> userVOPage = userTask.get();
                Page<PostVO> postVOPage = postTask.get();
                Page<Picture> picturePage = pictureTask.get();

                // 封装返回结果
                SearchVO searchVO = new SearchVO();
                searchVO.setUserList(userVOPage.getRecords());
                searchVO.setPostList(postVOPage.getRecords());
                searchVO.setPictureList(picturePage.getRecords());
                return searchVO;
            } catch (Exception e) {
                log.error("查询异常", e);
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "查询异常");
            }

        } else {
            // 存在指定的搜索类型
            // 这里使用了适配器模式，注册器模式
            SearchVO searchVO = new SearchVO();
            DataSource<?> dataSource = dataSourceRegistry.getDataSourceByType(type);
            Page<?> page = dataSource.doSearch(searchText, current, pageSize, request);
            searchVO.setDataList(page.getRecords());
            return searchVO;
        }
    }
}
