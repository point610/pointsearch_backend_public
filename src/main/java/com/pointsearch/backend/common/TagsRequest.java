package com.pointsearch.backend.common;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 创建请求
 */
@Data
public class TagsRequest extends PageRequest implements Serializable {

    /**
     * 标签列表
     */
    private List<String> tagNameList;

    private static final long serialVersionUID = 1L;
}