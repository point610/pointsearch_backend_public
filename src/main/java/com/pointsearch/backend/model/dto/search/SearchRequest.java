package com.pointsearch.backend.model.dto.search;

import com.pointsearch.backend.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 查询请求
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SearchRequest extends PageRequest implements Serializable {

    /**
     * 搜索词
     */
    private String searchText;

    /**
     * 搜索的类型
     */
    private String type;

    private static final long serialVersionUID = 1L;
}