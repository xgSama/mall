package com.xgsama.mall.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xgsama.common.utils.PageUtils;
import com.xgsama.mall.member.entity.MemberEntity;
import com.xgsama.mall.member.exception.PhoneExistException;
import com.xgsama.mall.member.exception.UserNameExistException;
import com.xgsama.mall.member.vo.MemberLoginVo;
import com.xgsama.mall.member.vo.UserRegisterVo;

import java.util.Map;

/**
 * 会员
 *
 * @author xgsama
 * @email china_cyzyc@163.com
 * @date 2021-09-08 22:19:57
 */
public interface MemberService extends IService<MemberEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void register(UserRegisterVo userRegisterVo) throws PhoneExistException, UserNameExistException;

    void checkPhone(String phone) throws PhoneExistException;

    void checkUserName(String username) throws UserNameExistException;

    MemberEntity login(MemberLoginVo vo);
}

