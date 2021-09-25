package com.xgsama.mall.product.controller;

import com.xgsama.common.utils.PageUtils;
import com.xgsama.common.utils.R;
import com.xgsama.mall.product.entity.AttrEntity;
import com.xgsama.mall.product.entity.AttrGroupEntity;
import com.xgsama.mall.product.service.AttrAttrgroupRelationService;
import com.xgsama.mall.product.service.AttrGroupService;
import com.xgsama.mall.product.service.AttrService;
import com.xgsama.mall.product.service.CategoryService;
import com.xgsama.mall.product.vo.AttrGroupRelationVo;
import com.xgsama.mall.product.vo.AttrGroupWithAttrsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


/**
 * 属性分组
 *
 * @author xgsama
 * @email china_cyzyc@163.com
 * @date 2021-09-09 19:51:53
 */
@RestController
@RequestMapping("product/attrgroup")
public class AttrGroupController {
    @Autowired
    private AttrGroupService attrGroupService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private AttrService attrService;

    @Autowired
    private AttrAttrgroupRelationService relationService;

    /**
     * 获取分类下所有分组&关联属性
     */
    @GetMapping("/{catelogId}/withattr")
    public R getGroupAndAttr(@PathVariable("catelogId") Long catelogId) {
        // 1、查出当前分类下的所有属性分组
        // 2、查出每个属性分组的所有属性
        List<AttrGroupWithAttrsVo> vos = attrGroupService.getAttrGroupWithAttrByCatelogId(catelogId);


        return R.ok().put("data", vos);
    }

    /**
     * 删除属性与分组的关联关系
     */
    @PostMapping("/attr/relation/delete")
    public R deleteRelation(@RequestBody AttrGroupRelationVo[] attrGroupRelationVo) {
        attrService.deleteRelation(attrGroupRelationVo);
        return R.ok();
    }

    /**
     * 获取属性分组没有关联的其他属性
     */
    @GetMapping("/{attrgroupId}/noattr/relation")
    public R getNoRelation(@PathVariable("attrgroupId") Long attrgroupId,
                           @RequestParam Map<String, Object> params) {
        PageUtils pageUtils = attrService.getNoRelation(attrgroupId, params);
        return R.ok().put("page", pageUtils);
    }


    /**
     * 获取属性分组的关联的所有属性
     */
    @GetMapping("/{attrgroupId}/attr/relation")
    public R list(@PathVariable("attrgroupId") Long attrgroupId) {
        List<AttrEntity> attrList = attrService.getRelationAttr(attrgroupId);
        return R.ok().put("data", attrList);
    }

    /**
     * 添加属性与分组关联关系
     */
    @PostMapping("/attr/relation")
    public R saveRelation(@RequestBody List<AttrGroupRelationVo> attrGroupRelationVos) {
        relationService.saveBatch(attrGroupRelationVos);
        return R.ok();
    }


    /**
     * 列表
     */
    @RequestMapping("/list/{catelogId}")
    // @RequiresPermissions("product:attrgroup:list")
    public R list(@RequestParam Map<String, Object> params,
                  @PathVariable("catelogId") Long catelogId) {
//        PageUtils page = attrGroupService.queryPage(params);

        PageUtils page = attrGroupService.queryPage(params, catelogId);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{attrGroupId}")
    // @RequiresPermissions("product:attrgroup:info")
    public R info(@PathVariable("attrGroupId") Long attrGroupId) {
        AttrGroupEntity attrGroup = attrGroupService.getById(attrGroupId);
        Long catelogId = attrGroup.getCatelogId();
        Long[] path = categoryService.findCatelogPath(catelogId);

        attrGroup.setCatelogPath(path);

        return R.ok().put("attrGroup", attrGroup);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    // @RequiresPermissions("product:attrgroup:save")
    public R save(@RequestBody AttrGroupEntity attrGroup) {
        attrGroupService.save(attrGroup);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    // @RequiresPermissions("product:attrgroup:update")
    public R update(@RequestBody AttrGroupEntity attrGroup) {
        attrGroupService.updateById(attrGroup);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    // @RequiresPermissions("product:attrgroup:delete")
    public R delete(@RequestBody Long[] attrGroupIds) {

        // TODO 删除前判断是否有关联
        for (Long attrGroupId : attrGroupIds) {
            List<AttrEntity> relationAttr = attrService.getRelationAttr(attrGroupId);

            if (relationAttr == null || relationAttr.size() == 0) {
                attrGroupService.removeById(attrGroupId);
            }
        }
//        attrGroupService.removeByIds(Arrays.asList(attrGroupIds));

        return R.ok();
    }

}
