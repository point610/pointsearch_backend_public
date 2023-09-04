package com.pointsearch.backend.model.dto.postcomment;

import lombok.Data;

import java.io.Serializable;

/**
 * 创建请求
 */
@Data
public class PostCommentAddRequest implements Serializable {

    /**
     * 内容
     */
    private String content;

    /**
     * 回复的评论的id
     */
    private Long fatherId;

    /**
     * 帖子 id
     */
    private Long postId;



    private static final long serialVersionUID = 1L;
}