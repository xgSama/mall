package com.xgsama.mall.ware.service.impl;

import com.xgsama.common.constant.WareConstant;
import com.xgsama.mall.ware.entity.PurchaseDetailEntity;
import com.xgsama.mall.ware.service.PurchaseDetailService;
import com.xgsama.mall.ware.vo.MergeVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xgsama.common.utils.PageUtils;
import com.xgsama.common.utils.Query;

import com.xgsama.mall.ware.dao.PurchaseDao;
import com.xgsama.mall.ware.entity.PurchaseEntity;
import com.xgsama.mall.ware.service.PurchaseService;
import org.springframework.transaction.annotation.Transactional;


@Service("purchaseService")
public class PurchaseServiceImpl extends ServiceImpl<PurchaseDao, PurchaseEntity> implements PurchaseService {

    @Autowired
    private PurchaseDetailService detailService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {


        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params),
                new QueryWrapper<>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPageUnreceivedPurchase(Map<String, Object> params) {
        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params),
                // 采购状态只能是0,1 ：新建,已分配
                new QueryWrapper<PurchaseEntity>().eq("status", 0)
                        .or().eq("status", 1)
        );

        return new PageUtils(page);
    }

    @Transactional
    @Override
    public void mergePurchase(MergeVo mergeVo) {
        Long purchaseId = mergeVo.getPurchaseId();
        if (purchaseId == null) {
            // 新建的采购单
            PurchaseEntity purchaseEntity = new PurchaseEntity();
            purchaseEntity.setStatus(WareConstant.PurchaseStatusEnum.CREATED.getCode());
            baseMapper.insert(purchaseEntity);
            purchaseId = purchaseEntity.getId();
        } else {
            // 确认采购单状态为0或者1
            PurchaseEntity purchaseEntity = baseMapper.selectById(purchaseId);
            if (purchaseEntity.getStatus().compareTo(WareConstant.PurchaseStatusEnum.CREATED.getCode()) != 0
                    && purchaseEntity.getStatus().compareTo(WareConstant.PurchaseStatusEnum.ASSIGNED.getCode()) != 0) {
                log.error("采购单状态不对，无法合并");
                return;
            }
        }
        List<Long> items = mergeVo.getItems();
        Long finalPurchaseId = purchaseId;
        List<PurchaseDetailEntity> purchaseDetailEntityList = items.stream()
                .map(item -> {
                    PurchaseDetailEntity purchaseDetailEntity = new PurchaseDetailEntity();
                    purchaseDetailEntity.setId(item);
                    purchaseDetailEntity.setPurchaseId(finalPurchaseId);
                    purchaseDetailEntity.setStatus(WareConstant.PurchaseDetailStatusEnum.ASSIGNED.getCode());
                    return purchaseDetailEntity;
                }).collect(Collectors.toList());
        detailService.updateBatchById(purchaseDetailEntityList);

        PurchaseEntity purchaseEntity = new PurchaseEntity();
        purchaseEntity.setId(finalPurchaseId);
        purchaseEntity.setUpdateTime(new Date());
        this.updateById(purchaseEntity);
    }

}