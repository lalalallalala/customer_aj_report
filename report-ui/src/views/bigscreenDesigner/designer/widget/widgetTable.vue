<template>
  <div :style="styleObj">
    <superslide v-if="hackReset" :options="options" class="txtScroll-top">
      <!--表头-->
      <div class="title">
        <div
          v-for="(item, index) in header"
          :style="[headerTableStlye, tableFiledWidth(index), tableRowHeight()]"
          :key="index"
        >
          {{ item.name }}
        </div>
      </div>
      <!--数据-->
      <div class="bd">
        <ul class="infoList">
          <li
            v-for="(item, index) in list"
            :key="index"
            :style="tableRowHeight()"
          >
            <div
              v-for="(itemChild, idx) in header"
              :key="idx"
              :style="[
                bodyTableStyle,
                bodyTable(index),
                tableFiledWidth(idx),
                tableRowHeight()
              ]"
            >
              {{ item[itemChild.key] }}
            </div>
          </li>
        </ul>
      </div>
    </superslide>
  </div>
</template>
<script>
import vue from "vue";
import VueSuperSlide from "vue-superslide";
vue.use(VueSuperSlide);
export default {
  props: {
    value: Object,
    ispreview: Boolean
  },
  data() {
    return {
      hackReset: true,
      options: {
        titCell: ".hd ul",
        mainCell: ".bd ul",
        effect: "topLoop",
        autoPage: true,
        //effect: "top",
        autoPlay: true,
        vis: 5,
        rowHeight: "50px"
      },
      header: [],
      list: [],
      optionsSetUp: {},
      optionsPosition: {},
      optionsData: {},
      tableFieldConfig:[]
    };
  },
  computed: {
    styleObj() {
      const allStyle = this.optionsPosition;
      return {
        position: this.ispreview ? "absolute" : "static",
        width: allStyle.width + "px",
        height: allStyle.height + "px",
        left: allStyle.left + "px",
        top: allStyle.top + "px",
        background: this.optionsSetUp.tableBgColor
      };
    },
    headerTableStlye() {
      const headStyle = this.optionsSetUp;
      return {
        "text-align": headStyle.textAlign,
        "font-size": headStyle.fontSize + "px",
        "border-style": headStyle.isLine ? "solid" : "none",
        "border-width": headStyle.borderWidth + "px",
        "border-color": headStyle.borderColor,
        display: headStyle.isHeader ? "block" : "none",
        color: headStyle.headColor,
        "background-color": headStyle.headBackColor
      };
    },
    bodyTableStyle() {
      const bodyStyle = this.optionsSetUp;
      return {
        "text-align": bodyStyle.textAlign,
        "font-size": bodyStyle.fontSize + "px",
        "border-style": bodyStyle.isLine ? "solid" : "none",
        "border-width": bodyStyle.borderWidth + "px",
        "border-color": bodyStyle.borderColor,
        color: bodyStyle.bodyColor,
        "background-color": bodyStyle.tableBgColor
      };
    }
  },
  watch: {
    value: {
      handler(val) {
        this.optionsSetUp = val.setup;
        this.optionsPosition = val.position;
        this.optionsData = val.data;
        this.initData();
      },
      deep: true
    }
  },
  mounted() {
    this.optionsSetUp = this.value.setup;
    this.optionsPosition = this.value.position;
    this.optionsData = this.value.data;
    this.initData();
  },
  methods: {
    initData() {
      this.handlerRollFn();
      //设置表头
      this.handlerHead();
      //填充数据
      this.handlerData();
      this.visConfig();
    },
    visConfig() {
      this.options.vis = this.optionsSetUp.vis;
    },
    handlerRollFn() {
      const options = this.options;
      const rollSet = this.optionsSetUp;
      options.autoPlay = rollSet.isRoll;
      options.effect = rollSet.effect;
      options.interTime = rollSet.interTime;
      options.delayTime = rollSet.delayTime;
      options.scroll = rollSet.scroll;
      this.options = options;
      this.hackResetFun();
    },
    //设置表头
    handlerHead() {
      const head = this.optionsData.dynamicData == undefined || this.optionsData.dynamicData == ''
      ?
      this.optionsSetUp.dynamicAddTable:this.tableFieldConfig;
      this.header = head;
    },
    handlerData() {
      const tableData = this.optionsData;
      tableData.dynamicData == undefined || tableData.dynamicData == ''
        ? this.handlerStaticData(tableData.staticData)
        : this.handlerDynamicData(tableData.dynamicData, tableData.refreshTime);
    },
    handlerStaticData(data) {
        //使用自定义的字段
      this.tableFieldConfig = this.optionsSetUp.dynamicAddTable;
      this.list = data;
    
     
    },
    handlerDynamicData(data, refreshTime) {
      if (!data) return;
      if (this.ispreview) {
        this.getEchartData(data);
        this.flagInter = setInterval(() => {
          this.getEchartData(data);
        }, refreshTime);
      } else {
        this.getEchartData(data);
      }
    },
    getEchartData(val) {
      // console.log('val',val);
      const data = this.queryEchartsData(val);
      data.then(res => {
        this.list = res;

        //chartProperties 由于每次选定后值不会消失。会变成"",所以需要过滤
        console.log('chartProperties',this.optionsData.dynamicData.chartProperties);

        let chartProperties = this.optionsData.dynamicData.chartProperties;
        chartProperties = Object.keys(chartProperties).forEach(item=>{
          if(chartProperties[item]===''){
            delete chartProperties[item];
          }
        })
        // console.log('chartProperties1111',chartProperties);
        //按照结果遍历返回存在的值值
        let tableFieldConfig = Object.keys(this.optionsData.dynamicData.chartProperties)
                        .map(item =>({key:item,name:item,width:'100%'}))
        
        console.log('tableFieldConfig',tableFieldConfig);
        //最新的表头值配置
        this.tableFieldConfig =  tableFieldConfig;
       //设置表头
        this.handlerHead();
        this.hackResetFun();
      });
    },
    // vue hack 之强制刷新组件
    hackResetFun() {
      this.hackReset = false;
      this.$nextTick(() => {
        this.hackReset = true;
      });
    },
    // 计算 奇偶背景色
    bodyTable(index) {
      let styleJson = {};
      if (index % 2) {
        styleJson["background-color"] = this.optionsSetUp.eventColor;
      } else {
        styleJson["background-color"] = this.optionsSetUp.oldColor;
      }
      return styleJson;
    },
    tableRowHeight() {
      let styleJson = {};
      if (this.optionsSetUp.rowHeight) {
        styleJson["height"] = this.optionsSetUp.rowHeight + "px";
        styleJson["line-height"] = this.optionsSetUp.rowHeight + "px";
      } else {
        styleJson["height"] = this.options.rowHeight;
        styleJson["line-height"] = this.optionsSetUp.rowHeight + "px";
      }
      return styleJson;
    },
    //表格每个距离参数
    tableFiledWidth(index) {
      let styleJson = {};
      // if (this.optionsSetUp.dynamicAddTable[index].width) {
      //   styleJson["width"] = this.optionsSetUp.dynamicAddTable[index].width;
      // }
      console.log('tableFieldConfig '+ index,this.tableFieldConfig);
      if(this.tableFieldConfig){
        return;
      }
      if (this.tableFieldConfig[index].width) {
        styleJson["width"] = this.tableFieldConfig[index].width;
      }
      return styleJson;
    }
  }
};
</script>
<style lang="scss" scoped>
/* 本例子css */
.txtScroll-top {
  overflow: hidden;
  position: relative;
}

.title {
  display: flex;
  flex-direction: row;
  width: 100%;
}

.title > div {
  height: 50px;
  line-height: 50px;
  width: 100%;
}

.txtScroll-top .bd {
  width: 100%;
}

.txtScroll-top .infoList li {
  height: 50px;
  line-height: 50px;
  display: flex;
  flex-direction: row;
}

.txtScroll-top .infoList li > div {
  width: 100%;
}

/*.txtScroll-top .infoList li:nth-child(n) {
  background: rgb(0, 59, 81);
}

.txtScroll-top .infoList li:nth-child(2n) {
  background: rgb(10, 39, 50);
}*/
</style>
