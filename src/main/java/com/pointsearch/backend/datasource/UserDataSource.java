package com.pointsearch.backend.datasource;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pointsearch.backend.model.dto.user.UserQueryRequest;
import com.pointsearch.backend.model.vo.UserVO;
import com.pointsearch.backend.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 继承DataSource<T>为适配器模式
 * 用户服务实现
 */
@Service
@Slf4j
public class UserDataSource implements DataSource<UserVO> {

    @Resource
    private UserService userService;

    @Override
    public Page<UserVO> doSearch(String searchText, long pageNum, long pageSize, HttpServletRequest request) {
        UserQueryRequest userQueryRequest = new UserQueryRequest();
        userQueryRequest.setUserName(searchText);
        userQueryRequest.setCurrent(pageNum);
        userQueryRequest.setPageSize(pageSize);
        Page<UserVO> userVOPage = userService.listUserVOByPage(userQueryRequest);
        return userVOPage;
    }
}
