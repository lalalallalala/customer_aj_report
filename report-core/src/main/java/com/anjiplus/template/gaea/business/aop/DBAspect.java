package com.anjiplus.template.gaea.business.aop;

import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import com.ruijie.jeef.core.util.MybatisPlusContext;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author 林波
 */
@Aspect
@Component
@Slf4j
public class DBAspect {

    @Resource
    HttpServletRequest request;

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
        MybatisPlusContext.createdByField("createBy");
        MybatisPlusContext.createdDateField("createTime");
        MybatisPlusContext.updatedByField("updateBy");
        MybatisPlusContext.updatedDateField("updateTime");
        MybatisPlusContext.fillVersion(true);
        HttpSession session = request.getSession(false);
        if(session != null){
            MybatisPlusContext.fillUser(String.valueOf(session.getAttribute("account")));
        }
    }

    @After("pointcut()")
    public void after() {
        log.debug("恢复数据源：{}", defaultDatasource);
        DynamicDataSourceContextHolder.poll();
    }


}