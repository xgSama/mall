package com.xgsama.mall.product.exception;

import com.xgsama.common.exception.BizCodeEnum;
import com.xgsama.common.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;


/**
 * MallExceptionControllerAdvice
 *
 * @author : xgSama
 * @date : 2021/9/14 21:44:40
 */
@Slf4j
@RestControllerAdvice(basePackages = "com.xgsama.mall.product.controller")
public class MallExceptionControllerAdvice {


    @ExceptionHandler(value = Exception.class)
    public R handleValidException(MethodArgumentNotValidException e) {
        log.error("数据检验出现问题：{},异常类型：{}", e.getMessage(), e.getClass());
        BindingResult bindingResult = e.getBindingResult();
        Map<String, String> errMap = new HashMap<>();
        bindingResult.getFieldErrors().forEach(item -> {
            errMap.put(item.getField(), item.getDefaultMessage());
        });
        return R.error(BizCodeEnum.VALID_EXCEPTION.getCode(), BizCodeEnum.VALID_EXCEPTION.getMsg()).put("data", errMap);
    }

    @ExceptionHandler(value = Throwable.class)
    public R handleException(Throwable t) {
        return R.error(BizCodeEnum.UNKNOWN_EXCEPTION.getCode(), BizCodeEnum.UNKNOWN_EXCEPTION.getMsg());
    }
}
