package com.anjiplus.template.gaea.business.modules.report.controller;

import com.anji.plus.gaea.annotation.Permission;
import com.anji.plus.gaea.annotation.log.GaeaAuditLog;
import com.anji.plus.gaea.bean.ResponseBean;
import com.anji.plus.gaea.curd.controller.GaeaBaseController;
import com.anji.plus.gaea.curd.service.GaeaBaseService;
import com.anji.plus.gaea.holder.UserContentHolder;
import com.anji.plus.gaea.utils.GaeaBeanUtils;
import com.anjiplus.template.gaea.business.modules.accessuser.dao.entity.AccessUser;
import com.anjiplus.template.gaea.business.modules.accessuser.service.AccessUserService;
import com.anjiplus.template.gaea.business.modules.report.controller.dto.ReportDto;
import com.anjiplus.template.gaea.business.modules.report.controller.param.ReportParam;
import com.anjiplus.template.gaea.business.modules.report.dao.entity.Report;
import com.anjiplus.template.gaea.business.modules.report.service.ReportService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * TODO
 *
 * @author chenkening
 * @date 2021/3/26 10:19
 */
@RestController
@Api(tags = "报表数据管理")
@Permission(code = "reportManage", name = "报表管理")
@RequestMapping("/report")
public class ReportController extends GaeaBaseController<ReportParam, Report, ReportDto> {

    @Autowired
    private ReportService reportService;

    @Override
    public GaeaBaseService<ReportParam, Report> getService() {
        return reportService;
    }

    @Override
    public Report getEntity() {
        return new Report();
    }

    @Override
    public ReportDto getDTO() {
        return new ReportDto();
    }

    @Autowired
    private AccessUserService accessUserService;

    @GetMapping({"/pageList2"})
    @Permission(
            code = "query",
            name = "查询"
    )
    @GaeaAuditLog(
            pageTitle = "查询"
    )
    public ResponseBean pageList(ReportParam param) {
        IPage<Report> iPage = getService().page(param);
        List<Report> records = iPage.getRecords();
        List<ReportDto> list = GaeaBeanUtils.copyList(records, ReportDto.class);
        this.pageResultHandler(list);

        List<String> createbyList = list.stream().map(k -> k.getCreateBy()).distinct().collect(Collectors.toList());
        QueryWrapper<AccessUser> accessUserQueryWrapper = new QueryWrapper<>();
        accessUserQueryWrapper.in("login_name",createbyList);
        List<AccessUser> users = accessUserService.list(accessUserQueryWrapper);
        Map<String, String> map = users.stream().collect(Collectors.toMap(AccessUser::getLoginName, AccessUser::getRealName));

        list.stream().forEach(k->{
            k.setCreateByName("".equals(map.get(k.getCreateBy())) ? "" : map.get(k.getCreateBy()));
        });

        Page<ReportDto> pageDto = new Page();
        pageDto.setCurrent(iPage.getCurrent()).setRecords(list).setPages(iPage.getPages()).setTotal(iPage.getTotal()).setSize(iPage.getSize());
        return this.responseSuccessWithData(pageDto);
    }

    @PostMapping("/copy")
    @Permission(code = "copy", name = "复制")
    @GaeaAuditLog(pageTitle = "复制")
    public ResponseBean copy(@RequestBody ReportDto dto) {
        reportService.copy(dto);
        return ResponseBean.builder().build();
    }

    @PostMapping("/reportAdd")
    @Permission(
            code = "insert",
            name = "新增"
    )
    @GaeaAuditLog(
            pageTitle = "新增"
    )
    public ResponseBean insert(@Validated @RequestBody ReportDto dto) {
        ResponseBean responseBean = this.responseSuccess();
        Report entity = getEntity();
        BeanUtils.copyProperties(dto, entity);
        entity.setReportAuthor(UserContentHolder.getContext().getUsername());
        this.getService().insert(entity);
        return responseBean;
    }

    @GetMapping("/viewAdd/{reportCode}")
    public ResponseBean viewAdd(@PathVariable("reportCode")String reportCode){
        reportService.viewAdd(reportCode);
        return ResponseBean.builder().build();
    }



}
