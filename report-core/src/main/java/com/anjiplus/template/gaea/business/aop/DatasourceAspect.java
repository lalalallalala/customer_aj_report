package com.anjiplus.template.gaea.business.aop;

import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author 林波
 */
@Aspect
@Component
@Slf4j
public class DatasourceAspect {

    @Value("${ajreport.datasource.default:aj-report}")
    String defaultDatasource;
//    @Pointcut("execution (public * com.anjiplus..*.controller..*.*.*(..))")
//    @Pointcut("within(com.anji.plus.gaea.curd.controller.GaeaBaseController+) || execution(public * com.anjiplus..*.*Controller.*(..))")
    @Pointcut("execution(public * com.anjiplus..*.*Controller.*(..)) || execution(public * com.anji.plus..*.*Controller.*(..))")
    public void pointcut() {
    }

    @Before("pointcut()")
    public void before() {
        log.debug("切换数据源：{}", defaultDatasource);
        DynamicDataSourceContextHolder.push(defaultDatasource);
    }

    @After("pointcut()")
    public void after() {
        log.debug("恢复数据源：{}", defaultDatasource);
        DynamicDataSourceContextHolder.poll();
    }


}