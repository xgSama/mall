package com.xgsama.common.valid;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.HashSet;
import java.util.Set;

/**
 * ListValueConstraintValidator
 *
 * @author : xgSama
 * @date : 2021/9/14 22:37:12
 */

/*
 * public interface ConstraintValidator<A extends Annotation, T>
 * 类型参数：
 * <A> – 实现处理的注解类型
 * <T> – 实现支持的目标类型
 */
public class ListValueConstraintValidator implements ConstraintValidator<ListValue, Integer> {
    private final Set<Integer> set = new HashSet<>();

    @Override
    public void initialize(ListValue constraintAnnotation) {
        int[] values = constraintAnnotation.values();
        for (int value : values) {
            set.add(value);
        }
    }

    @Override
    public boolean isValid(Integer integer, ConstraintValidatorContext constraintValidatorContext) {
        return set.contains(integer);
    }
}
