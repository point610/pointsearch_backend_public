package com.pointsearch.backend.model.enums;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 接口信息状态枚举
 */
public enum PostCommentEnum {

    TOP_COMMENT("回复帖子的", 0L);

    private final String text;

    private final Long value;

    PostCommentEnum(String text, Long value) {
        this.text = text;
        this.value = value;
    }

    /**
     * 获取值列表
     *
     * @return
     */
    public static List<Long> getValues() {
        return Arrays.stream(values()).map(item -> item.value).collect(Collectors.toList());
    }

    public Long getValue() {
        return value;
    }

    public String getText() {
        return text;
    }
}
