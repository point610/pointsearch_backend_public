package com.pointsearch.backend.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName ASKVO
 * @Description TODO
 * @Author point
 * @Date 2023/7/26 18:27
 * @Version 1.0
 */
@Data
public class ASKVO implements Serializable {

    /**
     * 用户accessKey
     */
    private String accessKey;

    /**
     * 用户secretKey
     */
    private String secretKey;

    private static final long serialVersionUID = 1L;
}
