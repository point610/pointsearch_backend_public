package com.pointsearch.backend.model.dto.postcomment;

import lombok.Data;

import java.io.Serializable;

/**
 * 更新请求
 */
@Data
public class PostCommentUpdateRequest implements Serializable {

    /**
     * id
     */
    private Long id;


    /**
     * 内容
     */
    private String content;

    private static final long serialVersionUID = 1L;
}