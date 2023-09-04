package com.pointsearch.backend.model.vo;

import com.pointsearch.backend.model.entity.Picture;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 聚合搜索
 */
@Data
public class SearchVO implements Serializable {

    // 没有指定类型的搜索
    private List<UserVO> userList;

    private List<PostVO> postList;

    private List<Picture> pictureList;

    // 指定类型的搜索
    private List<?> dataList;

    private static final long serialVersionUID = 1L;

}
