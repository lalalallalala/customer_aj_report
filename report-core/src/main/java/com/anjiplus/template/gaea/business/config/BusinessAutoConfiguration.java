package com.anjiplus.template.gaea.business.config;

import com.anji.plus.gaea.cache.CacheHelper;
import com.anjiplus.template.gaea.business.cache.ReportCacheHelper;
import com.anjiplus.template.gaea.business.runner.ApplicationInitRunner;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.subject.PrincipalCollection;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cache.CacheManager;
import org.springframework.cache.ehcache.EhCacheCache;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

/**
 * business配置类
 *
 * @author lr
 * @since 2021-04-08
 */
@Configuration
public class BusinessAutoConfiguration {

    @Value("${ajreport.cache.name:otherCache1}")
    String cacheName;

    public BusinessAutoConfiguration(){
        System.out.println("=============BusinessAutoConfiguration==============");
        System.out.println(this);
    }
//
//    @Autowired
//    @Qualifier("reportEhCacheManagerFactoryBean")
//    private EhCacheManagerFactoryBean reportEhCacheManagerFactoryBean;
//    @Autowired
//    @Qualifier("reportCacheManager")
//    private EhCacheCacheManager reportCacheManager;
//


    /**
     * 系统启动完执行
     *
     * @return
     */
    @Bean
    public ApplicationInitRunner applicationInitRunner() {
        System.out.println("========================applicationInitRunner");
        return new ApplicationInitRunner();
    }

    @Bean
    public CacheHelper gaeaCacheHelper() {
        System.out.println("=========================gaeaCacheHelper");
        return new ReportCacheHelper();
    }

    @Bean(name = "reportEhCacheCache")
    public EhCacheCache reportEhCacheCache(CacheManager cacheManager) {
        System.out.println("========================reportEhCacheCache");
        EhCacheCache cache = (EhCacheCache) cacheManager.getCache(cacheName);
        return cache;
    }

//
//    @Bean
//    public EhCacheCacheManager reportCacheManager() {
//        System.out.println("========================reportCacheManager");
//        EhCacheCacheManager ehCacheCacheManager = new EhCacheCacheManager();
//        ehCacheCacheManager.setCacheManager(reportEhCacheManagerFactoryBean().getObject());
//        return ehCacheCacheManager;
//    }
//
//
//    /**
//     * cacheManager工厂类，指定ehcache.xml的位置
//     *
//     * @return
//     */
//    @Bean
//    public EhCacheManagerFactoryBean reportEhCacheManagerFactoryBean() {
//        System.out.println("========================reportEhCacheManagerFactoryBean");
//        EhCacheManagerFactoryBean cacheManagerFactoryBean = new EhCacheManagerFactoryBean();
//        cacheManagerFactoryBean.setShared(true);
//        // 如果根目录下存在配置，则优先采用
//        ClassPathResource resource = new ClassPathResource("aj-report-ehcache.xml");
//        if (resource.exists()) {
//            cacheManagerFactoryBean.setConfigLocation(resource);
//        } else {
//            cacheManagerFactoryBean.setConfigLocation(new ClassPathResource("ehcache.xml"));
//        }
//        return cacheManagerFactoryBean;
//    }
}
