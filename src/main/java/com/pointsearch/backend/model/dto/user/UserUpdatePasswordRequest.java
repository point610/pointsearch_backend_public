package com.pointsearch.backend.model.dto.user;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户更新密码请求
 */
@Data
public class UserUpdatePasswordRequest implements Serializable {
    /**
     * 用户旧密码
     **/
    private String oldPassword;
    /**
     * 用户新密码
     **/
    private String newPassword;
    /**
     * 用户检测密码
     **/
    private String checkPassword;

    private static final long serialVersionUID = 1L;
}