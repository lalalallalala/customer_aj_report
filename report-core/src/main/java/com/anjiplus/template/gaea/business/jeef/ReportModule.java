package com.anjiplus.template.gaea.business.jeef;

import cn.hutool.core.util.StrUtil;
import com.ruijie.jeef.core.annotation.AppModule;
import com.ruijie.jeef.core.modularization.BaseAppModule;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author 林波
 * @date 2022/2/25
 */
@AppModule(ReportModuleConfig.MODULE_CODE)
public class ReportModule extends BaseAppModule {

    @Autowired
    ReportModuleConfig moduleConfig;

    @Override
    @NotNull
    public String getCode() {
        return ReportModuleConfig.MODULE_CODE;
    }



    @Override
    public ReportModuleConfig getModuleConfig() {
        return moduleConfig;
    }

    @Override
    public Map<String, List<String>> resourceHandlerConfig() {
        Map<String, List<String>> config = super.resourceHandlerConfig();
        String rootResource = getModuleConfig().getRootResource();

        if (!rootResource.endsWith("/")) {
            rootResource = rootResource + "/";
        }

        config.put("/report/static/**", Arrays.asList(StrUtil.format("classpath:{}", rootResource)
                , StrUtil.format("classpath*:{}", rootResource)));

        return config;
    }
}
