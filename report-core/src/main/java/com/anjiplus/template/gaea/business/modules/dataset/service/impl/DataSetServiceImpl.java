
package com.anjiplus.template.gaea.business.modules.dataset.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.anji.plus.gaea.constant.Enabled;
import com.anji.plus.gaea.curd.mapper.GaeaBaseMapper;
import com.anji.plus.gaea.exception.BusinessExceptionBuilder;
import com.anji.plus.gaea.utils.GaeaAssert;
import com.anji.plus.gaea.utils.GaeaBeanUtils;
import com.anjiplus.template.gaea.business.code.ResponseCode;
import com.anjiplus.template.gaea.business.enums.SetTypeEnum;
import com.anjiplus.template.gaea.business.modules.dataset.controller.dto.OriginalDataDto;
import com.anjiplus.template.gaea.business.modules.dataset.controller.dto.DataSetDto;
import com.anjiplus.template.gaea.business.modules.dataset.dao.DataSetMapper;
import com.anjiplus.template.gaea.business.modules.dataset.dao.entity.DataSet;
import com.anjiplus.template.gaea.business.modules.dataset.service.DataSetService;
import com.anjiplus.template.gaea.business.modules.datasetparam.controller.dto.DataSetParamDto;
import com.anjiplus.template.gaea.business.modules.datasetparam.dao.entity.DataSetParam;
import com.anjiplus.template.gaea.business.modules.datasetparam.service.DataSetParamService;
import com.anjiplus.template.gaea.business.modules.datasettransform.controller.dto.DataSetTransformDto;
import com.anjiplus.template.gaea.business.modules.datasettransform.dao.entity.DataSetTransform;
import com.anjiplus.template.gaea.business.modules.datasettransform.service.DataSetTransformService;
import com.anjiplus.template.gaea.business.modules.datasource.controller.dto.DataSourceDto;
import com.anjiplus.template.gaea.business.modules.datasource.dao.entity.DataSource;
import com.anjiplus.template.gaea.business.modules.datasource.service.DataSourceService;
import com.anjiplus.template.gaea.business.util.JdbcConstants;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
* @desc DataSet ?????????????????????
* @author Raod
* @date 2021-03-18 12:11:31.150755900
**/
@Service
//@RequiredArgsConstructor
@Slf4j
public class DataSetServiceImpl implements DataSetService {

    @Autowired
    private DataSetMapper dataSetMapper;

    @Autowired
    private DataSetParamService dataSetParamService;

    @Autowired
    private DataSetTransformService dataSetTransformService;

    @Autowired
    private DataSourceService dataSourceService;

    @Override
    public GaeaBaseMapper<DataSet> getMapper() {
      return dataSetMapper;
    }

    /**
     * ????????????
     *
     * @param id
     * @return
     */
    @Override
    public DataSetDto detailSet(Long id) {
        DataSetDto dto = new DataSetDto();
        DataSet result = selectOne(id);
        String setCode = result.getSetCode();
        GaeaBeanUtils.copyAndFormatter(result, dto);
        return getDetailSet(dto, setCode);
    }

    /**
     * ????????????
     *
     * @param setCode
     * @return
     */
    @Override
    public DataSetDto detailSet(String setCode) {
        DataSetDto dto = new DataSetDto();
        DataSet result = selectOne("set_code", setCode);
        GaeaBeanUtils.copyAndFormatter(result, dto);
        return getDetailSet(dto, setCode);
    }

    public DataSetDto getDetailSet(DataSetDto dto, String setCode) {
        //????????????
        List<DataSetParam> dataSetParamList = dataSetParamService.list(
                new QueryWrapper<DataSetParam>()
                        .lambda()
                        .eq(DataSetParam::getSetCode, setCode)
        );
        List<DataSetParamDto> dataSetParamDtoList = new ArrayList<>();
        dataSetParamList.forEach(dataSetParam -> {
            DataSetParamDto dataSetParamDto = new DataSetParamDto();
            GaeaBeanUtils.copyAndFormatter(dataSetParam, dataSetParamDto);
            dataSetParamDtoList.add(dataSetParamDto);
        });
        dto.setDataSetParamDtoList(dataSetParamDtoList);

        //????????????

        List<DataSetTransform> dataSetTransformList = dataSetTransformService.list(
                new QueryWrapper<DataSetTransform>()
                    .lambda()
                    .eq(DataSetTransform::getSetCode, setCode)
                    .orderByAsc(DataSetTransform::getOrderNum)
        );
        List<DataSetTransformDto> dataSetTransformDtoList = new ArrayList<>();
        dataSetTransformList.forEach(dataSetTransform -> {
            DataSetTransformDto dataSetTransformDto = new DataSetTransformDto();
            GaeaBeanUtils.copyAndFormatter(dataSetTransform, dataSetTransformDto);
            dataSetTransformDtoList.add(dataSetTransformDto);
        });
        dto.setDataSetTransformDtoList(dataSetTransformDtoList);

        if (StringUtils.isNotBlank(dto.getCaseResult())) {
            try {
                JSONArray jsonArray = JSONArray.parseArray(dto.getCaseResult());
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                dto.setSetParamList(jsonObject.keySet());
            } catch (Exception e) {
                log.error("error",e);
            }
        }
        return dto;
    }


    /**
     * ???????????????????????????????????????????????????
     *
     * @param dto
     */
    @Override
    @Transactional
    public DataSetDto insertSet(DataSetDto dto) {
        List<DataSetParamDto> dataSetParamDtoList = dto.getDataSetParamDtoList();
        List<DataSetTransformDto> dataSetTransformDtoList = dto.getDataSetTransformDtoList();

        //1.???????????????
        DataSet dataSet = new DataSet();
        BeanUtils.copyProperties(dto, dataSet);
        insert(dataSet);
        //2.??????????????????
        dataSetParamBatch(dataSetParamDtoList, dto.getSetCode());

        //3.??????????????????
        dataSetTransformBatch(dataSetTransformDtoList, dto.getSetCode());
        return dto;
    }

    /**
     * ???????????????????????????????????????????????????
     *
     * @param dto
     */
    @Override
    @Transactional
    public void updateSet(DataSetDto dto) {
        List<DataSetParamDto> dataSetParamDtoList = dto.getDataSetParamDtoList();
        List<DataSetTransformDto> dataSetTransformDtoList = dto.getDataSetTransformDtoList();
        //1.???????????????
        DataSet dataSet = new DataSet();
        BeanUtils.copyProperties(dto, dataSet);
        update(dataSet);

        //2.??????????????????
        dataSetParamBatch(dataSetParamDtoList, dto.getSetCode());

        //3.??????????????????
        dataSetTransformBatch(dataSetTransformDtoList, dto.getSetCode());
    }


    /**
     * ???????????????????????????????????????????????????
     *
     * @param id
     */
    @Override
    public void deleteSet(Long id) {
        DataSet dataSet = selectOne(id);
        String setCode = dataSet.getSetCode();
        //1.???????????????
        deleteById(id);

        //2.??????????????????
        dataSetParamService.delete(
                new QueryWrapper<DataSetParam>()
                        .lambda()
                        .eq(DataSetParam::getSetCode, setCode)
        );

        //3.??????????????????
        dataSetTransformService.delete(
                new QueryWrapper<DataSetTransform>()
                        .lambda()
                        .eq(DataSetTransform::getSetCode, setCode)
        );
    }

    /**
     * ????????????
     *
     * @param dto
     * @return
     */
    @Override
    public OriginalDataDto getData(DataSetDto dto) {

        OriginalDataDto originalDataDto = new OriginalDataDto();
        String setCode = dto.getSetCode();
        //1.?????????????????????????????????????????????
        DataSetDto dataSetDto = detailSet(setCode);
        String dynSentence = dataSetDto.getDynSentence();
        //2.???????????????
        DataSource dataSource;
        if (StringUtils.isNotBlank(dataSetDto.getSetType())
                && dataSetDto.getSetType().equals(SetTypeEnum.HTTP.getCodeValue())) {
            //http????????????????????????????????????????????????http????????????????????????DataSource
            dataSource = new DataSource();
            dataSource.setSourceConfig(dynSentence);
            dataSource.setSourceType(JdbcConstants.HTTP);
            String body = JSONObject.parseObject(dynSentence).getString("body");
            if (StringUtils.isNotBlank(body)) {
                dynSentence = body;
            }else {
                dynSentence = "{}";
            }

        }else {
            dataSource  = dataSourceService.selectOne("source_code", dataSetDto.getSourceCode());
        }

        //3.????????????
        //3.1????????????
        log.debug("????????????????????????{}", dto.getContextData());
        boolean verification = dataSetParamService.verification(dataSetDto.getDataSetParamDtoList(), dto.getContextData());
        if (!verification) {
            throw BusinessExceptionBuilder.build(ResponseCode.RULE_FIELDS_CHECK_ERROR);
        }
        dynSentence = dataSetParamService.transform(dto.getContextData(), dynSentence);
        log.debug("????????????????????????{}", dto.getContextData());
        //4.????????????
        DataSourceDto dataSourceDto = new DataSourceDto();
        BeanUtils.copyProperties(dataSource, dataSourceDto);
        dataSourceDto.setDynSentence(dynSentence);
        dataSourceDto.setContextData(dto.getContextData());
        //??????total,??????contextData???????????????????????????
        if (null != dto.getContextData()
                && dto.getContextData().containsKey("pageNumber")
                && dto.getContextData().containsKey("pageSize")) {
            long total = dataSourceService.total(dataSourceDto, dto);
            originalDataDto.setTotal(total);
        }
        List<JSONObject> data = dataSourceService.execute(dataSourceDto);
        //5.????????????
        List<JSONObject> transform = dataSetTransformService.transform(dataSetDto.getDataSetTransformDtoList(), data);
        originalDataDto.setData(transform);
        return originalDataDto;
    }

    /**
     * @param dto
     * @return
     */
    @Override
    public OriginalDataDto testTransform(DataSetDto dto) {
        String dynSentence = dto.getDynSentence();

        OriginalDataDto originalDataDto = new OriginalDataDto();
        String sourceCode = dto.getSourceCode();
        //1.???????????????
        DataSource dataSource;
        if (dto.getSetType().equals(SetTypeEnum.HTTP.getCodeValue())) {
            //http????????????????????????????????????????????????http????????????????????????DataSource
            dataSource = new DataSource();
            dataSource.setSourceConfig(dynSentence);
            dataSource.setSourceType(JdbcConstants.HTTP);
            String body = JSONObject.parseObject(dynSentence).getString("body");
            if (StringUtils.isNotBlank(body)) {
                dynSentence = body;
            }else {
                dynSentence = "{}";
            }

        }else {
          dataSource  = dataSourceService.selectOne("source_code", sourceCode);
        }

        //3.????????????
        //3.1????????????
        boolean verification = dataSetParamService.verification(dto.getDataSetParamDtoList(), null);
        if (!verification) {
            throw BusinessExceptionBuilder.build(ResponseCode.RULE_FIELDS_CHECK_ERROR);
        }

        dynSentence = dataSetParamService.transform(dto.getDataSetParamDtoList(), dynSentence);
        //4.????????????
        DataSourceDto dataSourceDto = new DataSourceDto();
        BeanUtils.copyProperties(dataSource, dataSourceDto);
        dataSourceDto.setDynSentence(dynSentence);
        dataSourceDto.setContextData(setContextData(dto.getDataSetParamDtoList()));

        //??????total,??????DataSetParamDtoList???????????????????????????
        Map<String, Object> collect = dto.getDataSetParamDtoList().stream().collect(Collectors.toMap(DataSetParamDto::getParamName, DataSetParamDto::getSampleItem));
        if (collect.containsKey("pageNumber") && collect.containsKey("pageSize")) {
            dto.setContextData(collect);
            long total = dataSourceService.total(dataSourceDto, dto);
            originalDataDto.setTotal(total);
        }else{
            String sentence = dataSourceDto.getDynSentence();
            if(sentence.contains("select") || sentence.contains("SELECT")){
                //????????????????????????
                collect.put("pageNumber","1");
                collect.put("pageSize","10");
                dto.setContextData(collect);
                dataSourceService.total(dataSourceDto, dto);
            }
        }

        List<JSONObject> data = dataSourceService.execute(dataSourceDto);

        //ES???HTTP ???????????????????????????fields
        List<String> fields = dataSourceService.executeFields(dataSourceDto);
        if(CollectionUtils.isEmpty(fields) && fields.size() == 0){
            JSONObject jsonObject = data.get(0);
            Set<String> strings = jsonObject.keySet();
            List<String> strings2 = new ArrayList<>(strings);
            originalDataDto.setSetList(strings2);
        }else{
            originalDataDto.setSetList(fields);
        }
        //5.????????????
        List<JSONObject> transform = dataSetTransformService.transform(dto.getDataSetTransformDtoList(), data);
        originalDataDto.setData(transform);
        return originalDataDto;
    }

    public Set<String> arrayToSet(List<String> lists){
        if(CollectionUtils.isEmpty(lists) || lists.size() == 0){
            return null;
        }
        return new HashSet<>(lists);
    }


    /**
     * ?????????????????????
     *
     * @return
     */
    @Override
    public List<DataSet> queryAllDataSet() {
        LambdaQueryWrapper<DataSet> wrapper = Wrappers.lambdaQuery();
        wrapper.select(DataSet::getSetCode, DataSet::getSetName, DataSet::getSetDesc, DataSet::getId)
                .eq(DataSet::getEnableFlag, Enabled.YES.getValue());
        wrapper.orderByDesc(DataSet::getUpdateTime);
        return dataSetMapper.selectList(wrapper);
    }

    @Override
    public List<String> queryAllDataTable(String sourceCode) {
        GaeaAssert.notEmpty(sourceCode,ResponseCode.PARAM_IS_NULL,"sourceCode????????????");
        LambdaQueryWrapper<DataSource> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(DataSource::getSourceCode,sourceCode);
        DataSource dataSource = dataSourceService.selectOne(queryWrapper);

        if(Objects.isNull(dataSource)){
            return null;
        }
        DataSourceDto dataSourceDto = new DataSourceDto();
        BeanUtils.copyProperties(dataSource, dataSourceDto);

        String sourceType = dataSourceDto.getSourceType();
        switch (sourceType){
            case JdbcConstants.POSTGRESQL:
                dataSourceDto.setDynSentence("select tablename from pg_tables where schemaname='public';");
                break;
            case JdbcConstants.MYSQL:
                dataSourceDto.setDynSentence("show tables");
                break;
            case JdbcConstants.ORACLE:
                dataSourceDto.setDynSentence("select table_name from all_tables;");
            default:
                throw BusinessExceptionBuilder.build(ResponseCode.DATA_SOURCE_TYPE_DOES_NOT_MATCH_TEMPORARILY);
        }

        List<JSONObject> execute = dataSourceService.execute(dataSourceDto);
        ArrayList<String> tableList = new ArrayList<>(execute.size());
        execute.stream().forEach(k->{
            Collection<Object> values = k.values();
            String string = values.toString();
            tableList.add(string.substring(1,string.length()-1));
        });
        return tableList;
    }

    public void analysisRelationalDbConfig(DataSourceDto dto) {
        JSONObject json = JSONObject.parseObject(dto.getSourceConfig());
        GaeaAssert.isFalse(json.containsKey("jdbcUrl"), ResponseCode.PARAM_IS_NULL,"jdbcUrl not empty");
        GaeaAssert.isFalse(json.containsKey("driverName"), ResponseCode.PARAM_IS_NULL,"driverName not empty");
        String jdbcUrl = json.getString("jdbcUrl");
        String username = json.getString("username");
        String password = json.getString("password");
        String driverName = json.getString("driverName");
        dto.setJdbcUrl(jdbcUrl);
        dto.setDriverName(driverName);
        dto.setUsername(username);
        dto.setPassword(password);
    }

    public void dataSetParamBatch(List<DataSetParamDto> dataSetParamDtoList,String setCode){
        dataSetParamService.delete(
                new QueryWrapper<DataSetParam>()
                        .lambda()
                        .eq(DataSetParam::getSetCode, setCode)
        );
        if (null == dataSetParamDtoList || dataSetParamDtoList.size() <= 0) {
            return;
        }
//        List<DataSetParam> dataSetParamList = new ArrayList<>();
        dataSetParamDtoList.forEach(dataSetParamDto -> {
            DataSetParam dataSetParam = new DataSetParam();
            BeanUtils.copyProperties(dataSetParamDto, dataSetParam);
            dataSetParam.setSetCode(setCode);
            //???????????????
            dataSetParamService.insert(dataSetParam);
//            dataSetParamList.add(dataSetParam);
        });
//        dataSetParamService.insertBatch(dataSetParamList);

    }

    public void dataSetTransformBatch(List<DataSetTransformDto> dataSetTransformDtoList,String setCode){
        dataSetTransformService.delete(
                new QueryWrapper<DataSetTransform>()
                        .lambda()
                        .eq(DataSetTransform::getSetCode, setCode)
        );
        if (null == dataSetTransformDtoList || dataSetTransformDtoList.size() <= 0) {
            return;
        }
//        List<DataSetTransform> dataSetTransformList = new ArrayList<>();
        for (int i = 0; i < dataSetTransformDtoList.size(); i++) {
            DataSetTransform dataSetTransform = new DataSetTransform();
            BeanUtils.copyProperties(dataSetTransformDtoList.get(i), dataSetTransform);
            dataSetTransform.setOrderNum(i + 1);
            dataSetTransform.setSetCode(setCode);
            //???????????????
            dataSetTransformService.insert(dataSetTransform);
//            dataSetTransformList.add(dataSetTransform);
        }
//        dataSetTransformService.insertBatch(dataSetTransformList);
    }

    /**
     * dataSetParamDtoList???map
     * @param dataSetParamDtoList
     * @return
     */
    public Map<String, Object> setContextData(List<DataSetParamDto> dataSetParamDtoList){
        Map<String, Object> map = new HashMap<>();
        if (null != dataSetParamDtoList && dataSetParamDtoList.size() > 0) {
            dataSetParamDtoList.forEach(dataSetParamDto -> map.put(dataSetParamDto.getParamName(), dataSetParamDto.getSampleItem()));
        }
        return map;
    }

}
