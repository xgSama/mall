package com.xgsama.mall.member.dao;

import com.xgsama.mall.member.entity.MemberEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员
 * 
 * @author xgsama
 * @email china_cyzyc@163.com
 * @date 2021-09-08 22:19:57
 */
@Mapper
public interface MemberDao extends BaseMapper<MemberEntity> {
	
}
