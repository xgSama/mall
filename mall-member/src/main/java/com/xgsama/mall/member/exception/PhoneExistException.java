package com.xgsama.mall.member.exception;

/**
 * PhoneExistException
 *
 * @author : xgSama
 * @date : 2021/10/24 19:39:36
 */
public class PhoneExistException extends RuntimeException{
    public PhoneExistException() {
        super("手机号存在");
    }
}
