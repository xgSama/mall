package com.xgsama.mall.member.exception;

/**
 * UserNameExistException
 *
 * @author : xgSama
 * @date : 2021/10/24 19:39:53
 */
public class UserNameExistException extends RuntimeException {
    public UserNameExistException() {
        super("用户名存在");
    }
}
