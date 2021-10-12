package com.xgsama.mall.product.controller;

import com.xgsama.common.to.es.SkuEsModel;
import com.xgsama.mall.product.feign.SearchFeignService;
import org.redisson.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * AlphaController
 *
 * @author : xgSama
 * @date : 2021/10/6 19:29:48
 */
@RestController
@RequestMapping("/test")
public class AlphaController {

    @Autowired
    SearchFeignService searchFeignService;

    @Autowired
    private RedissonClient redisson;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @GetMapping("/es")
    public String testF() {

        List<SkuEsModel> List = new ArrayList<>();
        SkuEsModel skuEsModel = new SkuEsModel();
        skuEsModel.setSkuId(1L);
        skuEsModel.setBrandName("测试调用");
        skuEsModel.setCatalogName("你猜");
        List.add(skuEsModel);

        searchFeignService.productStatusUp(List);
        return searchFeignService.test();
    }

    @GetMapping("/redisson")
    public String redissonTest() {
        // 获取一把锁，只要名字一样就是同一把锁
        RLock lock = redisson.getLock("my-lock");

        // 加锁 10s自动解锁
        lock.lock(10, TimeUnit.SECONDS);
        try {
            System.out.println("加锁成功，执行业务。。。。。" + Thread.currentThread().getId());
            Thread.sleep(30000L);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 解锁
            System.out.println("释放锁。。。" + Thread.currentThread().getId());
            lock.unlock();
        }

        return "redisson";
    }

    @GetMapping("/redisson/w")
    public String redissonTestW() {
        RReadWriteLock lock = redisson.getReadWriteLock("rw-lock");

        String s = "";
        RLock rLock = lock.writeLock();
        try {
            rLock.lock();
            s = UUID.randomUUID().toString();
            Thread.sleep(30000L);
            redisTemplate.opsForValue().set("writeValue", s);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            rLock.unlock();
        }

        return s;
    }

    @GetMapping("/redisson/r")
    public String redissonTestR() {

        RReadWriteLock lock = redisson.getReadWriteLock("rw-lock");

        RLock rLock = lock.readLock();
        rLock.lock();
        String s = "";

        try {
            s = redisTemplate.opsForValue().get("writeValue");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            rLock.unlock();
        }

        return s;
    }

    @GetMapping("/redisson/park")
    public String park() throws InterruptedException {
        RSemaphore park = redisson.getSemaphore("park");
        // park.acquire(); // 获取一个信号
        boolean b = park.tryAcquire();
        if (b) {
            return "ok";
        } else {
            return "no";
        }
    }

    @GetMapping("/redisson/go")
    public String go() throws InterruptedException {
        RSemaphore park = redisson.getSemaphore("park");
        park.release(); // 获取一个信号
        return "OK";
    }

    @GetMapping("/redisson/lockDoor")
    public String lockDoor() throws InterruptedException {
        RCountDownLatch door = redisson.getCountDownLatch("door");
        door.trySetCount(5L);
        door.await();
        return "放假了。。。";
    }


    @GetMapping("/redisson/gogo/{id}")
    public String gogo(@PathVariable("id") String id) throws InterruptedException {
        RCountDownLatch door = redisson.getCountDownLatch("door");
        door.countDown();

        return id + "的人都走了。。。";
    }

}
