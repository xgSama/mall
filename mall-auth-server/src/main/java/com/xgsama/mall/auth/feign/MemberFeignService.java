package com.xgsama.mall.auth.feign;

import com.xgsama.common.utils.R;
import com.xgsama.mall.auth.vo.SocialUser;
import com.xgsama.mall.auth.vo.UserLoginVo;
import com.xgsama.mall.auth.vo.UserRegisterVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * MemberFeignService
 *
 * @author : xgSama
 * @date : 2021/10/24 19:09:07
 */
@FeignClient("mall-member")
public interface MemberFeignService {

    @PostMapping("/member/member/register")
    R register(@RequestBody UserRegisterVo userRegisterVo);

    @PostMapping("/member/member/login")
    R login(@RequestBody UserLoginVo vo);

    @PostMapping("/member/member/oauth2/login")
    R login(@RequestBody SocialUser socialUser);
}
