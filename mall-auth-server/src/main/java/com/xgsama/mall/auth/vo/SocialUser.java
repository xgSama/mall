package com.xgsama.mall.auth.vo;

import lombok.Data;

/**
 * SocialUser
 *
 * @author : xgSama
 * @date : 2021/10/24 18:53:44
 */
@Data
public class SocialUser {

    private String accessToken;

    private String remindIn;

    private int expiresIn;

    private String uid;

    private String isRealName;
}
