package com.sky.aspect;

import com.fasterxml.jackson.databind.ser.Serializers;
import com.sky.annotation.AutoFill;
import com.sky.constant.AutoFillConstant;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

@Aspect
@Component
@Slf4j
public class AutoFillAspect {
    // JointPointCut
    @Pointcut("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.annotation.AutoFill)")
    public void autoFillPointCut(){}

    // ProceedingJoinPoint
    @Before("autoFillPointCut()")
    public void autoFill(JoinPoint joinPoint){
        log.info("开始自动填充公共字段...");
        // 1. 获取操作类型、方法参数
        // 从JoinPoint里面获取方法的参数
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        // 拿到方法对应的注解
        AutoFill autoFill = signature.getMethod().getAnnotation(AutoFill.class);
        OperationType operation = autoFill.value();
        Object[] args = joinPoint.getArgs();
        Object entity = args[0];

        // 2. 获取当前时间、操作人
        LocalDateTime now = LocalDateTime.now();
        Long currentId = BaseContext.getCurrentId();

        // 3. 根据操作类型，填充对应的字段
        if(operation == OperationType.INSERT){
            // 自动填充 创建时间、更新时间、创建人、更新人
            try{
                Method setCreateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
                Method setCreateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_USER, Long.class);
                Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);
                setCreateTime.invoke(entity, now);
                setCreateUser.invoke(entity, currentId);
                setUpdateTime.invoke(entity, now);
                setUpdateUser.invoke(entity, currentId);
            }catch (Exception error){
                error.printStackTrace();
            }
        }else{
             // 自动填充 更新时间、更新人
              try{
                Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);
                setUpdateTime.invoke(entity, now);
                setUpdateUser.invoke(entity, currentId);
            }catch (Exception error){
                error.printStackTrace();
            }
        }
    }
}
