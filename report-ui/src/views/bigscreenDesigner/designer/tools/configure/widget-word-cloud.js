export const widgetWordCloud = {
  code: 'widgetWordCloud',
  type: 'chart',
  label: '词云图',
  icon: 'iconciyuntu',
  options: {
    // 配置
    setup: [
      {
        type: 'el-input-text',
        label: '图层名称',
        name: 'layerName',
        required: false,
        placeholder: '',
        value: '词云图',
      },
      {
        type: 'vue-color',
        label: '背景颜色',
        name: 'background',
        required: false,
        placeholder: '',
        value: ''
      },
      [
        {
          name: '标题设置',
          list: [
            {
              type: 'el-switch',
              label: '标题',
              name: 'isNoTitle',
              required: false,
              placeholder: '',
              value: true
            },
            {
              type: 'el-input-text',
              label: '标题',
              name: 'titleText',
              required: false,
              placeholder: '',
              value: ''
            },
            {
              type: 'vue-color',
              label: '字体颜色',
              name: 'textColor',
              required: false,
              placeholder: '',
              value: '#fff'
            },
            {
              type: 'el-select',
              label: '字体粗细',
              name: 'textFontWeight',
              required: false,
              placeholder: '',
              selectOptions: [
                {code: 'normal', name: '正常'},
                {code: 'bold', name: '粗体'},
                {code: 'bolder', name: '特粗体'},
                {code: 'lighter', name: '细体'}
              ],
              value: 'normal'
            },
            {
              type: 'el-input-number',
              label: '字体大小',
              name: 'textFontSize',
              required: false,
              placeholder: '',
              value: 20
            },
            {
              type: 'el-select',
              label: '字体位置',
              name: 'textAlign',
              required: false,
              placeholder: '',
              selectOptions: [
                {code: 'center', name: '居中'},
                {code: 'left', name: '左对齐'},
                {code: 'right', name: '右对齐'},
              ],
              value: 'left'
            },
            {
              type: 'el-input-text',
              label: '副标题',
              name: 'subText',
              required: false,
              placeholder: '',
              value: ''
            },
            {
              type: 'vue-color',
              label: '字体颜色',
              name: 'subTextColor',
              required: false,
              placeholder: '',
              value: ''
            },
            {
              type: 'el-select',
              label: '字体粗细',
              name: 'subTextFontWeight',
              required: false,
              placeholder: '',
              selectOptions: [
                {code: 'normal', name: '正常'},
                {code: 'bold', name: '粗体'},
                {code: 'bolder', name: '特粗体'},
                {code: 'lighter', name: '细体'}
              ],
              value: 'normal'
            },
            {
              type: 'el-input-number',
              label: '字体大小',
              name: 'subTextFontSize',
              required: false,
              placeholder: '',
              value: 12
            },
          ],
        },
        {
          name: '词云范围',
          list: [
            {
              type: 'el-input-number',
              label: '最大范围',
              name: 'maxRangeSize',
              required: false,
              placeholder: '',
              value: 25
            },
            {
              type: 'el-input-number',
              label: '最小范围',
              name: 'minRangeSize',
              required: false,
              placeholder: '',
              value: 2
            },
          ],
        },
        {
          name: '词云角度',
          list: [
            {
              type: 'el-input-number',
              label: '最大角度',
              name: 'maxRotationRange',
              required: false,
              placeholder: '',
              value: 90
            },
            {
              type: 'el-input-number',
              label: '最小角度',
              name: 'minRotationRange',
              required: false,
              placeholder: '',
              value: -45
            },
          ],
        },
        {
          name: '提示语设置',
          list: [
            {
              type: 'el-input-number',
              label: '字体大小',
              name: 'fontSize',
              required: false,
              placeholder: '',
              value: 12
            },
            {
              type: 'vue-color',
              label: '字体颜色',
              name: 'lineColor',
              required: false,
              placeholder: '',
              value: '#00FEFF'
            },
          ],
        },
      ],
    ],
    // 数据
    data: [
      // {
      //   type: 'el-radio-group',
      //   label: '数据类型',
      //   name: 'dataType',
      //   require: false,
      //   placeholder: '',
      //   selectValue: true,
      //   selectOptions: [
      //     {
      //       code: 'staticData',
      //       name: '静态数据',
      //     },
      //     {
      //       code: 'dynamicData',
      //       name: '动态数据',
      //     },
      //   ],
      //   value: 'staticData',
      // },
      {
        type: 'el-input-number',
        label: '刷新时间(毫秒)',
        name: 'refreshTime',
        // relactiveDom: 'dataType',
        // relactiveDomValue: 'dynamicData',
        value: 60000
      },
      {
        type: 'el-button',
        label: '静态数据',
        name: 'staticData',
        required: false,
        placeholder: '',
        relactiveDom: '',
        relactiveDomValue: '',
        value: [
          {name: "占道", value: 284},
          {name: "水质", value: 71},
          {name: "无水", value: 71},
          {name: "停供", value: 21},
          {name: "停气", value: 11},
          {name: "占道", value: 11},
          {name: "Nancy", value: 520},
          {name: "Jayfee", value: 666},
          {name: "生活资源", value: 999},
          {name: "供热管理", value: 888},
          {name: "供气质量", value: 777},
          {name: "社会保障", value: 407},
          {name: "交通运输", value: 516},
          // {name: "城市交通", value: 515},
          // {name: "环境保护", value: 483},
          // {name: "城乡建设", value: 449},
          // {name: "公共安全", value: 406},
          // {name: "供热管理", value: 375},
          // {name: "市容环卫", value: 355},
          // {name: "粉尘污染", value: 335},
          // {name: "噪声污染", value: 324},
          // {name: "医疗卫生", value: 284},
          // {name: "供热发展", value: 254},
          // {name: "房地产管理", value: 462},
          // {name: "生活噪音", value: 253},
          // {name: "城市供电", value: 223},
          // {name: "大气污染", value: 223},
          // {name: "房屋安全", value: 223},
          // {name: "文化活动", value: 223},
          // {name: "拆迁管理", value: 223},
          // {name: "公共设施", value: 223},
          // {name: "供气质量", value: 223},
          // {name: "供电管理", value: 223},
          // {name: "燃气管理", value: 152},
          // {name: "教育管理", value: 152},
          // {name: "医疗纠纷", value: 152},
          // {name: "执法监督", value: 152},
          // {name: "设备安全", value: 152},
          // {name: "政务建设", value: 152},
          // {name: "宏观经济", value: 152},
          // {name: "教育管理", value: 112},
          // {name: "社会保障", value: 112},
          // {name: "分类列表", value: 112},
          // {name: "农业生产", value: 112},
          // {name: "物业服务", value: 92},
          // {name: "物业管理", value: 92},
          // {name: "低保管理", value: 92},
          // {name: "执法争议", value: 72},
          // {name: "占道堆放", value: 71},
          // {name: "地上设施", value: 71},
          // {name: "主网原因", value: 71},
          // {name: "集中供热", value: 71},
          // {name: "客运管理", value: 71},
          // {name: "治安案件", value: 71},
          // {name: "群众健身", value: 41},
          // {name: "市场收费", value: 41},
          // {name: "生产资金", value: 41},
          // {name: "生产噪声", value: 41},
          // {name: "农村低保", value: 41},
          // {name: "劳动争议", value: 41},
          // {name: "医疗事故", value: 21},
          // {name: "基础教育", value: 21},
          // {name: "职业教育", value: 21},
          // {name: "拆迁补偿", value: 21},
          // {name: "设施维护", value: 21},
          // {name: "市场外溢", value: 11},
          // {name: "占道经营", value: 11},
          // {name: "树木管理", value: 11},
          // {name: "供气质量", value: 11},
          // {name: "燃气管理", value: 11},
          // {name: "市容环卫", value: 11},
          // {name: "新闻传媒", value: 11},
          // {name: "人才招聘", value: 11},
          // {name: "市场环境", value: 11},
          // {name: "城市交通", value: 11},
          // {name: "物业服务", value: 11},
          // {name: "物业管理", value: 11},
          // {name: "园林绿化", value: 11},
          // {name: "有线电视", value: 11},
          // {name: "社会治安", value: 11},
          // {name: "林业资源", value: 11},
          // {name: "体育活动", value: 11},
          // {name: "低保管理", value: 11},
          // {name: "劳动争议", value: 11},
          // {name: "粉煤灰污染", value: 284},
          // {name: "人行道管理", value: 71},
          // {name: "身份证管理", value: 71},
          // {name: "房地产开发", value: 11},
          // {name: "经营性收费", value: 11},
          // {name: "一次供水问题", value: 11},
          // {name: "工业粉尘污染", value: 71},
          // {name: "工业排放污染", value: 41},
          // {name: "破坏森林资源", value: 41},
          // {name: "生活用水管理", value: 688},
          // {name: "一次供水问题", value: 588},
          // {name: "公交运输管理", value: 386},
          // {name: "自然资源管理", value: 355},
          // {name: "土地资源管理", value: 304},
          // {name: "生活用水管理", value: 112},
          // {name: "供热单位影响", value: 253},
          // {name: "二次供水问题", value: 112},
          // {name: "城市公共设施", value: 92},
          // {name: "拆迁政策咨询", value: 92},
          // {name: "县区、开发区", value: 152},
          // {name: "文娱市场管理", value: 72},
          // {name: "商业烟尘污染", value: 72},
          // {name: "供热单位影响", value: 71},
          // {name: "压力容器安全", value: 71},
          // {name: "劳动合同争议", value: 41},
          // {name: "物业资质管理", value: 21},
          // {name: "农村基础设施", value: 11},
          // {name: "行政事业收费", value: 11},
          // {name: "房屋配套问题", value: 11},
          // {name: "公交运输管理", value: 11},
          // {name: "社会福利及事务", value: 11},
          // {name: "食品安全与卫生", value: 11},
          // {name: "物业服务与管理", value: 112},
          // {name: "文体与教育管理", value: 406},
          // {name: "社会保障与福利", value: 429},
          // {name: "出租车运营管理", value: 385},
          // {name: "物业服务与管理", value: 304},
          // {name: "房屋质量与安全", value: 223},
          // {name: "劳动报酬与福利", value: 41},
          // {name: "食品安全与卫生", value: 11},
          // {name: "房屋与图纸不符", value: 11},
          // {name: "其他行政事业收费", value: 11},
          // {name: "农村土地规划管理", value: 254},
          // {name: "社会保障保险管理", value: 92},
          // {name: "城市交通秩序管理", value: 72},
          // {name: "户籍管理及身份证", value: 11},
          // {name: "公路（水路）交通", value: 11},
          // {name: "国有公交（大巴）管理", value: 71},
          // {name: "有线电视安装及调试维护", value: 11},
          // {name: "市政府工作部门（含部门管理机构、直属单位）", value: 11},
        ],
      },
      {
        type: 'dycustComponents',
        label: '',
        name: 'dynamicData',
        required: false,
        placeholder: '',
        // relactiveDom: 'dataType',
        chartType: 'widget-piechart',
        dictKey: 'PIE_PROPERTIES',
        // relactiveDomValue: 'dynamicData',
        value: '',
      },
    ],
    // 坐标
    position: [
      {
        type: 'el-input-number',
        label: '左边距',
        name: 'left',
        required: false,
        placeholder: '',
        value: 0,
      },
      {
        type: 'el-input-number',
        label: '上边距',
        name: 'top',
        required: false,
        placeholder: '',
        value: 0,
      },
      {
        type: 'el-input-number',
        label: '宽度',
        name: 'width',
        required: false,
        placeholder: '该容器在1920px大屏中的宽度',
        value: 500,
      },
      {
        type: 'el-input-number',
        label: '高度',
        name: 'height',
        required: false,
        placeholder: '该容器在1080px大屏中的高度',
        value: 300,
      },
    ],
  }
}
