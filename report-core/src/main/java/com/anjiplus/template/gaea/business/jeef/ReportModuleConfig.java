package com.anjiplus.template.gaea.business.jeef;

import com.ruijie.jeef.core.config.YmlPropertySourceFactory;
import com.ruijie.jeef.core.modularization.ModuleConfig;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @author 林波
 * @date 2022/2/25
 */
@PropertySource( value = "classpath:application-module-config-" + ReportModuleConfig.MODULE_CODE + ".yml", factory = YmlPropertySourceFactory.class)
@ConfigurationProperties("app.module." + ReportModuleConfig.MODULE_CODE)
@Configuration
@Data
@Slf4j
public class ReportModuleConfig extends ModuleConfig {
    public static final String MODULE_CODE = "report";
}
