package com.xgsama.mall.ware.service.impl;

import com.xgsama.common.constant.WareConstant;
import com.xgsama.mall.ware.entity.PurchaseDetailEntity;
import com.xgsama.mall.ware.service.PurchaseDetailService;
import com.xgsama.mall.ware.service.WareSkuService;
import com.xgsama.mall.ware.vo.MergeVo;
import com.xgsama.mall.ware.vo.PurchaseDoneVo;
import com.xgsama.mall.ware.vo.PurchaseItemDoneVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
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

    @Autowired
    private WareSkuService wareSkuService;

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

    @Transactional
    @Override
    public void received(List<Long> ids) {
        if (ids == null || ids.size() == 0) {
            return;
        }
        // 1.确认当前采购单是已分配状态
        List<PurchaseEntity> collect = ids.stream()
                .map(this::getById
                        // 只能采购已分配的
                ).filter(item -> item.getStatus() == WareConstant.PurchaseStatusEnum.ASSIGNED.getCode() ||
                        item.getStatus() == WareConstant.PurchaseStatusEnum.CREATED.getCode()
                ).peek(item -> {
                    item.setStatus(WareConstant.PurchaseStatusEnum.RECEIVE.getCode());
                    item.setUpdateTime(new Date());
                }).collect(Collectors.toList());
        // 2.被领取之后重新设置采购状态
        this.updateBatchById(collect);

        // 3.改变采购项状态
        collect.forEach(item -> {
            List<PurchaseDetailEntity> entities = detailService.listDetailByPurchaseId(item.getId());

            // 收集所有需要更新的采购单id
            List<PurchaseDetailEntity> detailEntities = entities.stream().map(entity -> {
                PurchaseDetailEntity detailEntity = new PurchaseDetailEntity();
                detailEntity.setId(entity.getId());
                detailEntity.setStatus(WareConstant.PurchaseDetailStatusEnum.BUYING.getCode());
                return detailEntity;
            }).collect(Collectors.toList());
            // 根据id  批量更新
            detailService.updateBatchById(detailEntities);
        });

    }

    @Transactional
    @Override
    public void done(PurchaseDoneVo doneVo) {
        // 1.改变采购单状态
        Long id = doneVo.getId();
        boolean flag = true;
        List<PurchaseItemDoneVo> items = doneVo.getItems();
        ArrayList<PurchaseDetailEntity> updates = new ArrayList<>();
        double price;
        double p = 0;
        double sum = 0;
        // 2.改变采购项状态
        for (PurchaseItemDoneVo item : items) {
            // 采购失败的情况
            PurchaseDetailEntity detailEntity = new PurchaseDetailEntity();
            if (item.getStatus() == WareConstant.PurchaseDetailStatusEnum.HASERROR.getCode()) {
                flag = false;
                detailEntity.setStatus(item.getStatus());
            } else {
                detailEntity.setStatus(WareConstant.PurchaseDetailStatusEnum.FINISH.getCode());
                // 3.将成功采购的进行入库
                // 查出当前采购项的详细信息
                PurchaseDetailEntity entity = detailService.getById(item.getItemId());
                // skuId、到那个仓库、sku名字
                price = wareSkuService.addStock(entity.getSkuId(), entity.getWareId(), entity.getSkuNum());
                if (price != p) {
                    p = entity.getSkuNum() * price;
                }
                detailEntity.setSkuPrice(new BigDecimal(p));
                sum += p;
            }
            // 设置采购成功的id
            detailEntity.setId(item.getItemId());
            updates.add(detailEntity);
        }
        // 批量更新采购单
        detailService.updateBatchById(updates);

        // 对采购单的状态进行更新
        PurchaseEntity purchaseEntity = new PurchaseEntity();
        purchaseEntity.setId(id);
        purchaseEntity.setAmount(new BigDecimal(sum));
        purchaseEntity.setStatus(flag ? WareConstant.PurchaseStatusEnum.FINISH.getCode() : WareConstant.PurchaseStatusEnum.HASERROR.getCode());
        purchaseEntity.setUpdateTime(new Date());
        this.updateById(purchaseEntity);
    }

}