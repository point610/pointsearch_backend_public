package com.pointsearch.backend.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 帖子视图
 */
@Data
public class PostCommentVO implements Serializable {


    /**
     * id
     */
    private Long id;

    /**
     * 帖子 id
     */
    private Long postId;

    /**
     * 创建用户 id
     */
    private Long userId;

    /**
     * 内容
     */
    private String content;

    /**
     * 创建人信息
     */
    private UserVO user;

    /**
     * 回复的评论的id
     */
    private Long fatherId;

    /**
     * 帖子的子评论
     **/
    private List<PostCommentVO> son;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

}
