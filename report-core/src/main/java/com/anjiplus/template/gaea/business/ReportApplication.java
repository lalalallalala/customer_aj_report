package com.anjiplus.template.gaea.business;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * 业务模板
 * @author lr
 * @since 2021-02-03
 */
@SpringBootApplication(scanBasePackages = {
        "com.anjiplus.template.gaea",
        "com.anji.plus",
        "com.ruijie"
})
@MapperScan(basePackages = {
//        "com.anjiplus.template.gaea.business.modules.*.dao",
        "com.anjiplus.template.gaea.business.modules.**.dao",
        "com.anji.plus.gaea.*.module.*.dao"
})
@EnableAspectJAutoProxy(exposeProxy = true)
public class ReportApplication {
    public static void main( String[] args ) throws URISyntaxException, IOException {
        SpringApplication.run(ReportApplication.class);
        System.out.println("done");
    }
}
