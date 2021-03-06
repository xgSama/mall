package com.xgsama.mall.product.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xgsama.common.utils.PageUtils;
import com.xgsama.common.utils.Query;
import com.xgsama.mall.product.dao.CategoryDao;
import com.xgsama.mall.product.entity.CategoryEntity;
import com.xgsama.mall.product.service.CategoryBrandRelationService;
import com.xgsama.mall.product.service.CategoryService;
import com.xgsama.mall.product.vo.Catalog2Vo;
import com.xgsama.mall.product.vo.Catalog3Vo;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    @Autowired
    CategoryBrandRelationService categoryBrandRelationService;

    @Autowired
    StringRedisTemplate redisTemplate;

    @Autowired
    RedissonClient redisson;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<CategoryEntity> listWithTree() {
        // ??????????????????
        List<CategoryEntity> entities = baseMapper.selectList(null);

        List<CategoryEntity> level1Menus = entities.stream()
                .filter(categoryEntity -> {
                    return categoryEntity.getParentCid() == 0;
                })
                .map(menu -> {
                    menu.setChildren(getChildren(menu, entities));
                    return menu;
                })
                .sorted(Comparator.comparingInt(categoryEntity ->
                        (categoryEntity.getSort() == null ? 0 : categoryEntity.getSort())))
                .collect(Collectors.toList());

        return level1Menus;
    }

    @Override
    public void removeMenuByIds(List<Long> asList) {
        // TODO ??????????????????????????????????????????????????????
        baseMapper.deleteBatchIds(asList);
    }

    @Override
    public Long[] findCatelogPath(Long catelogId) {
        CategoryEntity byId = this.getById(catelogId);
        List<Long> paths = new ArrayList<>();
        List<Long> parentPath = findParentPath(catelogId, paths);

        Collections.reverse(parentPath);
        return parentPath.toArray(new Long[0]);
    }

    /**
     * ????????????
     *
     * @param category
     */
//    @Caching(
//            evict = {
//                    @CacheEvict(value = "category", key = "'getLevel1Categories'"),
//                    @CacheEvict(value = "category", key = "'getCatalogJson'")
//            }
//    )
    @CacheEvict(value = {"category"}, allEntries = true)
//    @CachePut
    @Transactional
    @Override
    public void updateCascade(CategoryEntity category) {
        this.updateById(category);
        categoryBrandRelationService.updateCategory(category.getCatId(), category.getName());
    }

    @Override
    public Long[] findCategoryPath(Long catelogId) {
        List<Long> path = new ArrayList<>();
        List<Long> parentPath = findParentPath(catelogId, path);
        Collections.reverse(parentPath);
        return parentPath.toArray(new Long[0]);
    }

    @Cacheable(value = {"category"}, key = "#root.method.name")
    @Override
    public List<CategoryEntity> getLevel1Categories() {
        List<CategoryEntity> entities = baseMapper.selectList(new QueryWrapper<CategoryEntity>().eq("parent_cid", 0));
        return entities;
    }

    //    private Map<String, Object> cache = new HashMap<>();

    @Cacheable(value = "category", key = "#root.methodName")
    @Override
    public Map<String, List<Catalog2Vo>> getCatalogJson() {
        /*
         * 1.??????????????? ??????????????????
         * 2.?????????????????? ??????????????????
         * 3.?????? ??????????????????
         */
        // 1. ??????????????????????????????????????????json?????????
        String catalogJson = redisTemplate.opsForValue().get("catalogJson");
        if (StringUtils.isEmpty(catalogJson)) {
            // 2. ???????????????
            Map<String, List<Catalog2Vo>> catalogJsonFromDB = getCatalogJsonFromDBWithRedisLock();
            // 3. ??????????????????????????????????????????jsonString
            String catalogJsonString = JSON.toJSONString(catalogJsonFromDB);
            // redisTemplate.opsForValue().set("catalogJson", catalogJsonString, 1, TimeUnit.DAYS);
            return catalogJsonFromDB;
        }
        Map<String, List<Catalog2Vo>> result = JSON.parseObject(
                catalogJson,
                new TypeReference<Map<String, List<Catalog2Vo>>>() {
                });
        return result;
    }

    /**
     * redis????????? ???????????????
     */
    @SuppressWarnings("unchecked")
    public Map<String, List<Catalog2Vo>> getDataFromDB() {

        String catalogJSON = redisTemplate.opsForValue().get("catalogJson");
        if (!StringUtils.isEmpty(catalogJSON)) {
            return JSON.parseObject(catalogJSON, new TypeReference<Map<String, List<Catalog2Vo>>>() {
            });
        }
        // ??????????????????????????????
        List<CategoryEntity> entityList = baseMapper.selectList(null);
        // ????????????????????????
        // 1???????????????????????????
        List<CategoryEntity> level1Categories = getParent_cid(entityList, 0L);

        // 2???????????????
        Map<String, List<Catalog2Vo>> parent_cid = level1Categories.stream().collect(Collectors.toMap(
                // ??????????????????????????? ?????????????????????????????????
                k -> k.getCatId().toString(),
                v -> {
                    List<CategoryEntity> entities = getParent_cid(entityList, v.getCatId());
                    List<Catalog2Vo> catalog2Vos = null;
                    if (entities != null) {
                        // ????????????????????????????????????
                        catalog2Vos = entities.stream().map(l2 -> {
                            Catalog2Vo catalog2Vo = new Catalog2Vo(l2.getCatId().toString(),
                                    l2.getName(),
                                    v.getCatId().toString(),
                                    null);

                            List<CategoryEntity> level3Catalog = getParent_cid(entityList, l2.getCatId());
                            // ?????????????????????????????????
                            if (level3Catalog != null) {
                                List<Catalog3Vo> collect = level3Catalog.stream()
                                        .map(l3 -> {
                                            Catalog3Vo catalog3Vo = new Catalog3Vo(l3.getCatId().toString(), l3.getName(),
                                                    l2.getCatId().toString());
                                            return catalog3Vo;
                                        }).collect(Collectors.toList());
                                catalog2Vo.setCatalog3List(collect);
                            }

                            return catalog2Vo;
                        }).collect(Collectors.toList());
                    }
                    return catalog2Vos;
                }));

        // ??????????????????????????????????????????????????????????????????
        redisTemplate.opsForValue().set("catalogJson", JSON.toJSONString(parent_cid), 1, TimeUnit.DAYS);
        return parent_cid;
    }

    /**
     * redis???????????? ??????DB [?????????????????????]
     */
    public Map<String, List<Catalog2Vo>> getCatalogJsonFromDBWithLocalLock() {
        synchronized (this) {
            // ???????????? ???????????????
            return getDataFromDB();
        }
    }

    /**
     * ????????????
     * ????????????????????????????????????????????????
     * 1?????????
     * 2???????????????
     *
     * @return
     */
    public Map<String, List<Catalog2Vo>> getCatalogJsonFromDBWithRedisLock() {
        // 1????????????????????????redis??????
        String uuid = UUID.randomUUID().toString();
        Boolean lock = redisTemplate.opsForValue().setIfAbsent("lock", uuid, 300, TimeUnit.SECONDS);
        if (lock) {
            Map<String, List<Catalog2Vo>> catalogJsonFromDB;
            try {
                catalogJsonFromDB = getDataFromDB();
            } finally {
                String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
                redisTemplate.execute(new DefaultRedisScript<>(script, Long.class), Collections.singletonList("lock"), uuid);
            }

            return catalogJsonFromDB;
        } else {
            // ???????????????????????????
            return getCatalogJsonFromDBWithRedisLock(); // ??????????????????
        }
    }

    public Map<String, List<Catalog2Vo>> getCatalogJsonFromDBWithRedissonLock() {
        // 1????????????????????????redis??????

        RLock lock = redisson.getLock("catalogJson-lock");
        lock.lock();

        Map<String, List<Catalog2Vo>> catalogJsonFromDB;
        try {
            catalogJsonFromDB = getDataFromDB();
        } finally {
            lock.unlock();
        }

        return catalogJsonFromDB;

    }


    private List<CategoryEntity> getParent_cid(List<CategoryEntity> entityList, Long parent_cid) {
//        return baseMapper.selectList(new QueryWrapper<CategoryEntity>().eq("parent_cid", v.getCatId()));
        return entityList.stream()
                .filter(item ->
                        item.getParentCid().longValue() == parent_cid.longValue()).collect(Collectors.toList());
    }

    private List<Long> findParentPath(Long catelogId, List<Long> paths) {
        // ??????????????????ID
        paths.add(catelogId);
        CategoryEntity byId = this.getById(catelogId);
        if (byId.getParentCid() != 0) {
            findParentPath(byId.getParentCid(), paths);
        }

        return paths;
    }

    /**
     * ??????????????????????????????????????????
     *
     * @param root
     * @param all
     * @return
     */
    private List<CategoryEntity> getChildren(CategoryEntity root, List<CategoryEntity> all) {
        List<CategoryEntity> children = all.stream()
                .filter(categoryEntity -> {
                    return categoryEntity.getParentCid().longValue() == root.getCatId().longValue();
                })
                .map(categoryEntity -> {
                    // 1.???????????????
                    categoryEntity.setChildren(getChildren(categoryEntity, all));
                    return categoryEntity;
                })
                .sorted(Comparator.comparingInt(categoryEntity ->
                        (categoryEntity.getSort() == null ? 0 : categoryEntity.getSort())))
                .collect(Collectors.toList());

        return children;
    }
}