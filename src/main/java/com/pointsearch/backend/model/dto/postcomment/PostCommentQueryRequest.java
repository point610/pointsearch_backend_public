package com.pointsearch.backend.model.dto.postcomment;

import com.pointsearch.backend.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 查询请求
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PostCommentQueryRequest extends PageRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * id
     */
    private Long notId;

    /**
     * 内容
     */
    private String content;
    /**
     * 帖子 id
     */
    private Long postId;
    /**
     * 创建用户 id
     */
    private Long userId;


    private static final long serialVersionUID = 1L;
}