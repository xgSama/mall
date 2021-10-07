package com.xgsama.mall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.xgsama.common.constant.ProductConstant;
import com.xgsama.mall.product.dao.AttrAttrgroupRelationDao;
import com.xgsama.mall.product.dao.AttrGroupDao;
import com.xgsama.mall.product.dao.CategoryDao;
import com.xgsama.mall.product.entity.*;
import com.xgsama.mall.product.service.CategoryService;
import com.xgsama.mall.product.vo.AttrGroupRelationVo;
import com.xgsama.mall.product.vo.AttrRespVo;
import com.xgsama.mall.product.vo.AttrVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xgsama.common.utils.PageUtils;
import com.xgsama.common.utils.Query;

import com.xgsama.mall.product.dao.AttrDao;
import com.xgsama.mall.product.service.AttrService;
import org.springframework.transaction.annotation.Transactional;


@Service("attrService")
public class AttrServiceImpl extends ServiceImpl<AttrDao, AttrEntity> implements AttrService {

    @Autowired
    AttrAttrgroupRelationDao relationDao;

    @Autowired
    AttrGroupDao attrGroupDao;

    @Autowired
    CategoryDao categoryDao;

    @Autowired
    CategoryService categoryService;

    @Transactional
    @Override
    public void saveAttr(AttrVo attrVo) {
        AttrEntity attrEntity = new AttrEntity();
        BeanUtils.copyProperties(attrVo, attrEntity);
        //1、保存基本数据
        this.save(attrEntity);
        //2、保存关联关系
        if (attrVo.getAttrType() == ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode() &&
                attrVo.getAttrGroupId() != null) {
            AttrAttrgroupRelationEntity relationEntity = new AttrAttrgroupRelationEntity();
            relationEntity.setAttrGroupId(attrVo.getAttrGroupId());
            relationEntity.setAttrId(attrEntity.getAttrId());
            relationEntity.setAttrSort(0);
            relationDao.insert(relationEntity);
        }
    }

    @Override
    public PageUtils queryBaseAttrPage(Map<String, Object> params, Long catelogId, String type) {
        QueryWrapper<AttrEntity> waWrapper =
                new QueryWrapper<AttrEntity>()
                        .eq("attr_type",
                                "base".equalsIgnoreCase(type) ? ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode()
                                        : ProductConstant.AttrEnum.ATTR_TYPE_SALE.getCode());

        if (catelogId != ProductConstant.AttrEnum.ATTR_TYPE_SALE.getCode()) {
            // 如果是 base 就是基本属性 插入1 否则插入0
            waWrapper.eq("catelog_id", catelogId);
        }
        String key = (String) params.get("key");
        if (!StringUtils.isEmpty(key)) {
            waWrapper.and((w) -> {
                w.eq("attr_id", key).or().like("attr_name", key);
            });
        }
        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                waWrapper
        );
        PageUtils pageUtils = new PageUtils(page);
        // 先查询三级分类名字、分组名字 再封装
        List<AttrEntity> records = page.getRecords();
        // attrRespVos 就是最终封装好的Vo
        List<AttrRespVo> attrRespVos = records.stream()
                .map((attrEntity) -> {
                    AttrRespVo attrRespVo = new AttrRespVo();
                    BeanUtils.copyProperties(attrEntity, attrRespVo);
                    // 1.设置分类和分组的名字  先获取中间表对象  给attrRespVo 封装分组名字
                    if ("base".equalsIgnoreCase(type)) {
                        // attr的关联关系 当它没有分组的时候就不保存了
                        AttrAttrgroupRelationEntity entity = relationDao.selectOne(
                                new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attrEntity.getAttrId()));
                        if (entity != null && entity.getAttrGroupId() != null) {
                            AttrGroupEntity attrGroupEntity = attrGroupDao.selectById(entity);

//                            attrRespVo.setGroupName(attrGroupEntity.getAttrGroupName());

                            if (attrGroupEntity != null) {
                                attrRespVo.setGroupName(attrGroupEntity.getAttrGroupName());
                            } else {
                                // TODO 此处有BUG，之前分组删除未做限制可能关联的分组已经被误删
                            }
                        }
                    }
                    // 2.查询分类id 给attrRespVo 封装三级分类名字
                    CategoryEntity categoryEntity = categoryDao.selectById(attrEntity.getCatelogId());
                    if (categoryEntity != null) {
                        attrRespVo.setCatelogName(categoryEntity.getName());
                    }
                    return attrRespVo;
                }).collect(Collectors.toList());
        pageUtils.setList(attrRespVos);
        return pageUtils;
    }

    @Override
    public AttrRespVo getAttrInfo(Long attrId) {
        AttrEntity attrEntity = baseMapper.selectById(attrId);
        AttrRespVo attrResVo = new AttrRespVo();
        BeanUtils.copyProperties(attrEntity, attrResVo);
        // 设置分组信息
        AttrAttrgroupRelationEntity relationEntity = relationDao.selectOne(
                new QueryWrapper<AttrAttrgroupRelationEntity>()
                        .eq("attr_id", attrEntity.getAttrId()));
        // 将attrGroupId 和 GroupName 返回
        if (relationEntity != null) {
            attrResVo.setAttrGroupId(relationEntity.getAttrGroupId());
            AttrGroupEntity attrGroupEntity = attrGroupDao.selectById(relationEntity.getAttrGroupId());
            if (attrGroupEntity != null) {
                attrResVo.setGroupName(attrGroupEntity.getAttrGroupName());
            }
        }
        // 将catelogPath和 catlogName 返回
        Long[] categoryPath = categoryService.findCategoryPath(attrEntity.getCatelogId());
        attrResVo.setCatelogPath(categoryPath);
        CategoryEntity categoryEntity = categoryDao.selectById(attrEntity.getCatelogId());
        if (categoryEntity != null) {
            attrResVo.setCatelogName(categoryEntity.getName());
        }
        return attrResVo;
    }

    @Transactional
    @Override
    public void updateAttr(AttrVo attr) {
        // 修改基本属性
        AttrEntity attrEntity = new AttrEntity();
        BeanUtils.copyProperties(attr, attrEntity);
        baseMapper.updateById(attrEntity);

        AttrAttrgroupRelationEntity relationEntity = new AttrAttrgroupRelationEntity();
        relationEntity.setAttrGroupId(attr.getAttrGroupId());
        relationEntity.setAttrId(attr.getAttrId());

        Integer count = relationDao.selectCount(new QueryWrapper<AttrAttrgroupRelationEntity>()
                .eq("attr_id", attr.getAttrId()));

        if (count > 0) {
            // 修改分组关联
            relationDao.update(relationEntity,
                    new UpdateWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attr.getAttrId()));
        } else {
            relationDao.insert(relationEntity);
        }


    }

    /**
     * 根据分组id查找关联属性
     */
    @Override
    public List<AttrEntity> getRelationAttr(Long attrgroupId) {
        List<AttrAttrgroupRelationEntity> entities = relationDao.selectList(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_group_id", attrgroupId));
        List<Long> attrIds = entities.stream().map(AttrAttrgroupRelationEntity::getAttrId).collect(Collectors.toList());
        // 根据这个属性查询到的id可能是空的
        if (attrIds == null || attrIds.size() == 0) {
            return null;
        }
        return this.listByIds(attrIds);
    }

    @Override
    public PageUtils getNoRelation(Long attrgroupId, Map<String, Object> params) {
        //1、当前分组只能关联自己所属的分类里面的所有属性
        AttrGroupEntity attrGroupEntity = attrGroupDao.selectById(attrgroupId);
        Long catelogId = attrGroupEntity.getCatelogId();
        // 2、当前分组只能别的分组没有引用的属性																									并且这个分组的id不是我当前正在查的id
        //2.1)、当前分类下的其他分组
        List<AttrGroupEntity> group = attrGroupDao.selectList(new QueryWrapper<AttrGroupEntity>().eq("catelog_id", catelogId));
        // 得到当前分类下面的所有分组id
        List<Long> collect = group.stream().map(item -> {
            return item.getAttrGroupId();
        }).collect(Collectors.toList());

        //2.2)、查询这些分组关联的属性
        List<AttrAttrgroupRelationEntity> groupId = relationDao.selectList(new QueryWrapper<AttrAttrgroupRelationEntity>().in("attr_group_id", collect));
        // 再次获取跟这些分组有关的属性id的集合
        List<Long> attrIds = groupId.stream().map(item -> {
            return item.getAttrId();
        }).collect(Collectors.toList());

        //2.3)、从当前分类的所有属性中移除这些属性；[因这些分组已经存在被选了 就不用再显示了]
        QueryWrapper<AttrEntity> wrapper = new QueryWrapper<AttrEntity>().eq("catelog_id", catelogId).eq("attr_type", ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode());
        if (attrIds != null && attrIds.size() > 0) {
            wrapper.notIn("attr_id", attrIds);
        }
        // 当搜索框中有key并且不为空的时候 进行模糊查询
        String key = (String) params.get("key");
        if (!StringUtils.isEmpty(key)) {
            wrapper.and((w) -> {
                w.eq("attr_id", key).or().like("attr_name", key);
            });
        }
        // 将最后返回的结果进行封装
        IPage<AttrEntity> page = this.page(new Query<AttrEntity>().getPage(params), wrapper);

        PageUtils pageUtils = new PageUtils(page);
        return pageUtils;
    }

    /**
     * SELECT attr_id FROM `pms_attr` WHERE attr_id IN (?) AND search_type = 1
     */
    @Override
    public List<Long> selectSearchAttrs(List<Long> attrIds) {
        return baseMapper.selectSearchAttrIds(attrIds);
    }

    /**
     * 根据attrId和attrGroupId删除关联
     *
     * @param attrGroupRelationVo
     */
    @Override
    public void deleteRelation(AttrGroupRelationVo[] attrGroupRelationVo) {
        List<AttrAttrgroupRelationEntity> entityList = Arrays.stream(attrGroupRelationVo).map((item) -> {
            AttrAttrgroupRelationEntity attrAttrgroupRelationEntity = new AttrAttrgroupRelationEntity();
            BeanUtils.copyProperties(item, attrAttrgroupRelationEntity);
            return attrAttrgroupRelationEntity;
        }).collect(Collectors.toList());
        relationDao.deleteBatchRelation(entityList);
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                new QueryWrapper<AttrEntity>()
        );

        return new PageUtils(page);
    }

}