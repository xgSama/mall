package com.xgsama.mall.product.service.impl;

import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xgsama.common.constant.ProductConstant;
import com.xgsama.common.to.SkuReductionTO;
import com.xgsama.common.to.SpuBoundTO;
import com.xgsama.common.to.es.SkuEsModel;
import com.xgsama.common.to.es.SkuHasStockVo;
import com.xgsama.common.utils.PageUtils;
import com.xgsama.common.utils.Query;
import com.xgsama.common.utils.R;
import com.xgsama.mall.product.dao.SpuInfoDao;
import com.xgsama.mall.product.entity.*;
import com.xgsama.mall.product.feign.CouponFeignService;
import com.xgsama.mall.product.feign.SearchFeignService;
import com.xgsama.mall.product.feign.WareFeignService;
import com.xgsama.mall.product.service.*;
import com.xgsama.mall.product.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;


@Service("spuInfoService")
@Slf4j
public class SpuInfoServiceImpl extends ServiceImpl<SpuInfoDao, SpuInfoEntity> implements SpuInfoService {

    @Autowired
    private SpuInfoDescService spuInfoDescService;

    @Autowired
    private SpuImagesService spuImagesService;

    @Autowired
    private AttrService attrService;

    @Autowired
    private ProductAttrValueService attrValueService;

    @Autowired
    private SkuInfoService skuInfoService;

    @Autowired
    private SkuImagesService skuImagesService;

    @Autowired
    private SkuSaleAttrValueService skuSaleAttrValueService;

    @Autowired
    private CouponFeignService couponFeignService;

    @Autowired
    private WareFeignService wareFeignService;

    @Autowired
    SearchFeignService esFeignClient;

    @Autowired
    private BrandService brandService;

    @Autowired
    private CategoryService categoryService;


    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                new QueryWrapper<SpuInfoEntity>()
        );

        return new PageUtils(page);
    }

    @Transactional
    @Override
    public void saveSpuInfo(SpuSaveVo vo) {
        // 1.保存spu基本信息 pms_sku_info
        SpuInfoEntity spuInfoEntity = new SpuInfoEntity();
        spuInfoEntity.setCreateTime(new Date());
        spuInfoEntity.setUpdateTime(new Date());
        BeanUtils.copyProperties(vo, spuInfoEntity);
        this.saveBatchSpuInfo(spuInfoEntity);

        // 2.保存spu的表述图片  pms_spu_info_desc
        List<String> decript = vo.getDecript();
        SpuInfoDescEntity spuInfoDescEntity = new SpuInfoDescEntity();
        spuInfoDescEntity.setSpuId(spuInfoEntity.getId());
        // String join 的方式将它们用逗号分隔
        spuInfoDescEntity.setDecript(String.join(",", decript));
        spuInfoDescService.saveSpuInfoDesc(spuInfoDescEntity);

        // 3.保存spu的图片集  pms_sku_images
        // 先获取所有图片
        List<String> images = vo.getImages();
        // 保存图片的时候 并且保存这个是那个spu的图片
        spuImagesService.saveImages(spuInfoEntity.getId(), images);

        // 4.保存spu的规格属性  pms_product_attr_value
        List<BaseAttrs> baseAttrs = vo.getBaseAttrs();
        List<ProductAttrValueEntity> collect = baseAttrs.stream()
                .map(attr -> {
                    ProductAttrValueEntity valueEntity = new ProductAttrValueEntity();
                    valueEntity.setAttrId(attr.getAttrId());
                    // 可能页面没用传入属性名字 根据属性id查到所有属性 给名字赋值
                    AttrEntity attrEntity = attrService.getById(attr.getAttrId());
                    valueEntity.setAttrName(attrEntity.getAttrName());
                    valueEntity.setAttrValue(attr.getAttrValues());
                    valueEntity.setQuickShow(attr.getShowDesc());
                    valueEntity.setSpuId(spuInfoEntity.getId());

                    return valueEntity;
                })
                .collect(Collectors.toList());
        attrValueService.saveProductAttr(collect);
        // 5.保存当前spu对应所有sku信息
        Bounds bounds = vo.getBounds();
        SpuBoundTO spuBoundTO = new SpuBoundTO();
        BeanUtils.copyProperties(bounds, spuBoundTO);
        spuBoundTO.setSpuId(spuInfoEntity.getId());
        R r = couponFeignService.saveSpuBounds(spuBoundTO);
        if (r.getCode() != 0) {
            log.error("远程保存spu积分信息失败");
        }
        // 1).spu的积分信息 sms_spu_bounds
        List<Skus> skus = vo.getSkus();
        if (skus != null && skus.size() > 0) {
            // 提前查找默认图片
            skus.forEach(item -> {
                String defaultImg = "";
                for (Images img : item.getImages()) {
                    if (img.getDefaultImg() == 1) {
                        defaultImg = img.getImgUrl();
                        break;
                    }
                }
                // 2).基本信息的保存 pms_sku_info
                // skuName 、price、skuTitle、skuSubtitle 这些属性需要手动保存
                SkuInfoEntity skuInfoEntity = new SkuInfoEntity();
                BeanUtils.copyProperties(item, skuInfoEntity);
                // 设置spu的品牌id
                skuInfoEntity.setBrandId(spuInfoEntity.getBrandId());
                skuInfoEntity.setCatalogId(spuInfoEntity.getCatalogId());
                skuInfoEntity.setSpuId(spuInfoEntity.getId());
                skuInfoEntity.setSkuDefaultImg(defaultImg);
                skuInfoEntity.setSaleCount(0L);
                skuInfoService.saveSkuInfo(skuInfoEntity);

                // 3).保存sku的图片信息  pms_sku_images
                // sku保存完毕 自增主键就出来了 收集所有图片
                Long skuId = skuInfoEntity.getSkuId();
                List<SkuImagesEntity> imagesEntities = item.getImages().stream()
                        .map(img -> {
                            SkuImagesEntity skuImagesEntity = new SkuImagesEntity();
                            skuImagesEntity.setId(skuId);
                            skuImagesEntity.setImgUrl(img.getImgUrl());
                            skuImagesEntity.setDefaultImg(img.getDefaultImg());
                            return skuImagesEntity;
                        })
                        .filter(entity ->
                                // 返回true就会保存 返回false就会过滤
                                !StringUtils.isEmpty(entity.getImgUrl()))
                        .collect(Collectors.toList());
                skuImagesService.saveBatch(imagesEntities);

                // 4).sku的销售属性  pms_sku_sale_attr_value
                List<Attr> attr = item.getAttr();
                List<SkuSaleAttrValueEntity> skuSaleAttrValueEntities = attr.stream()
                        .map(a -> {
                            // 对拷页面传过来的三个属性
                            SkuSaleAttrValueEntity skuSaleAttrValueEntity = new SkuSaleAttrValueEntity();
                            BeanUtils.copyProperties(a, skuSaleAttrValueEntity);
                            skuSaleAttrValueEntity.setSkuId(skuId);
                            return skuSaleAttrValueEntity;
                        })
                        .collect(Collectors.toList());
                skuSaleAttrValueService.saveBatch(skuSaleAttrValueEntities);

                // 5.) sku的优惠、满减、会员价格等信息  [跨库]
                SkuReductionTO skuReductionTO = new SkuReductionTO();
                BeanUtils.copyProperties(item, skuReductionTO);
                skuReductionTO.setSkuId(skuId);
                if (skuReductionTO.getFullCount() > 0 ||
                        (skuReductionTO.getFullPrice().compareTo(new BigDecimal("0")) > 0)) {
                    R r1 = couponFeignService.saveSkuReduction(skuReductionTO);
                    if (r1.getCode() != 0) {
                        log.error("远程保存sku优惠信息失败");
                    }
                }
            });
        }
    }

    @Override
    public void saveBatchSpuInfo(SpuInfoEntity spuInfoEntity) {
        this.baseMapper.insert(spuInfoEntity);
    }

    @Override
    public PageUtils queryPageByCondition(Map<String, Object> params) {
        QueryWrapper<SpuInfoEntity> wrapper = new QueryWrapper<>();

        // 根据 spu管理带来的条件进行叠加模糊查询
        String key = (String) params.get("key");
        if (!StringUtils.isEmpty(key)) {
            wrapper.and(w -> w.eq("id", key).or().like("spu_name", key));
        }

        String status = (String) params.get("status");
        if (!StringUtils.isEmpty(status)) {
            wrapper.eq("publish_status", status);
        }

        String brandId = (String) params.get("brandId");
        if (!StringUtils.isEmpty(brandId) && !"0".equalsIgnoreCase(brandId)) {
            wrapper.eq("brand_id", brandId);
        }

        String catelogId = (String) params.get("catelogId");
        if (!StringUtils.isEmpty(catelogId) && !"0".equalsIgnoreCase(catelogId)) {
            wrapper.eq("catalog_id", catelogId);
        }

        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                wrapper
        );
        return new PageUtils(page);
    }

    @Override
    public void up(Long spuId) {

        // 1、查出当前 spu 对应的所有sku信息，品牌的名字
        List<SkuInfoEntity> skus = skuInfoService.getSkusBySpuId(spuId);


        //TODO 4、查询当前 sku 的所有可以用来被检索的规格属性
        List<ProductAttrValueEntity> attrValueEntities = attrValueService.baseAttrListForSpu(spuId);
        // 返回所有 attrId
        List<Long> attrIds = attrValueEntities.stream().map(ProductAttrValueEntity::getAttrId).collect(Collectors.toList());

        // 根据 attrIds 查询出 检索属性，pms_attr表中 search_type为一 则是检索属性
        List<Long> searchAttrIds = attrService.selectSearchAttrs(attrIds);

        // 将查询出来的 attr_id 放到 set 集合中 用来判断 attrValueEntities 是否包含 attrId
        Set<Long> idSet = new HashSet<>(searchAttrIds);

        List<SkuEsModel.Attrs> attrList = attrValueEntities.stream()
                .filter(item -> {
                    // 过滤掉不包含在 searchAttrIds集合中的元素
                    return idSet.contains(item.getAttrId());
                }).map(item -> {
                    SkuEsModel.Attrs attrs1 = new SkuEsModel.Attrs();
                    // 属性对拷 将 ProductAttrValueEntity对象与 SkuEsModel.Attrs相同的属性进行拷贝
                    BeanUtils.copyProperties(item, attrs1);
                    return attrs1;
                }).collect(Collectors.toList());


        // TODO 1、发送远程调用，库存系统查询是否有库存
        // 取出 Skuid 组成集合
        List<Long> skuIds = skus.stream().map(SkuInfoEntity::getSkuId).collect(Collectors.toList());
        Map<Long, Boolean> stockMap = null;
        try {
            R r = wareFeignService.getSkuHasStock(skuIds);
            // key 是 SkuHasStockVo的 skuid value是 item的hasStock 是否拥有库存
            TypeReference<List<SkuHasStockVo>> typeReference = new TypeReference<List<SkuHasStockVo>>() {
            };
            stockMap = r.getData(typeReference).stream()
                    .collect(Collectors.toMap(SkuHasStockVo::getSkuId, SkuHasStockVo::getHasStock));
        } catch (Exception e) {
            log.error("库存服务异常：原因：{}", e);
            e.printStackTrace();
        }

        // 2、封装每个 sku 的信息
        Map<Long, Boolean> finalStockMap = stockMap;
        List<SkuEsModel> upProducts = skus.stream().map(sku -> {
            // 组装需要查询的数据
            SkuEsModel skuEsModel = new SkuEsModel();
            BeanUtils.copyProperties(sku, skuEsModel);
            skuEsModel.setSkuPrice(sku.getPrice());
            skuEsModel.setSkuImg(sku.getSkuDefaultImg());

            // 设置属性
            skuEsModel.setAttrs(attrList);

            // 设置库存信息
            if (finalStockMap == null) {
                // 远程服务出现问题，任然设置为null
                skuEsModel.setHasStock(true);
            } else {
                skuEsModel.setHasStock(finalStockMap.get(sku.getSkuId()));
            }
            //TODO 2、热度频分 0
            skuEsModel.setHotScore(0L);

            //TODO 3、查询品牌名字和分类的信息
            BrandEntity brandEntity = brandService.getById(skuEsModel.getBrandId());
            skuEsModel.setBrandName(brandEntity.getName());
            skuEsModel.setBrandImg(brandEntity.getLogo());
            CategoryEntity categoryEntity = categoryService.getById(skuEsModel.getCatalogId());
            skuEsModel.setCatalogName(categoryEntity.getName());

            return skuEsModel;
        }).collect(Collectors.toList());


        //TODO 5、将数据发送给 es保存 ，直接发送给 search服务
        R r = esFeignClient.productStatusUp(upProducts);
        if (r.getCode() == 0) {
            // 远程调用成功
            // TODO 6、修改当前 spu 的状态
            baseMapper.updateSpuStatus(spuId, ProductConstant.StatusEnum.SPU_UP.getCode());
        } else {
            // 远程调用失败
            //TODO 7、重复调用？ 接口冥等性 重试机制

            /**
             * 1、构造请求数据，将对象转成json
             * RequestTemplate template = buildTemplateFromArgs.create(argv);
             * 2、发送请求进行执行(执行成功进行解码)
             *  executeAndDecode(template);
             * 3、执行请求会有重试机制
             *  while (true) {
             *       try {
             *         return executeAndDecode(template);
             *       } catch (RetryableException e) {
             *         try {
             *           retryer.continueOrPropagate(e);
             *         } catch (RetryableException th) {
             *              throw cause;
             *         }
             *         continute
             */
        }
    }

    @Override
    public SpuInfoEntity getSpuInfoBySkuId(Long skuId) {
        return null;
    }

}