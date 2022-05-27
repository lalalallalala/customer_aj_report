<template>
  <el-dialog
    :title="caseResultTitle"
    :close-on-click-modal="false"
    :visible.sync="visib"
    width="70%"
    :before-close="closeDialog"
  >
<!--    <vue-json-editor-->
<!--      v-model="caseResultContent"-->
<!--      :show-btns="false"-->
<!--      :mode="'code'"-->
<!--      lang="zh"-->
<!--      class="my-editor"-->
<!--      @json-change="onJsonChange"-->
<!--      @json-save="onJsonSave"-->
<!--    />-->

    <el-table
      :data="tableDataSql"
      stripe
      border
      ref="tables"
      height="500"
      style="width: 100%;">
      <template v-for="item in fieldList">
        <el-table-column
          :prop="item"
          :label="item"
        >
        </el-table-column>
      </template>
    </el-table>
    <span slot="footer" class="dialog-footer">
      <el-button type="primary" @click="closeDialog">关闭</el-button>
    </span>
  </el-dialog>
</template>
<script>
import vueJsonEditor from "vue-json-editor";
import {testTransformSet} from "@/api/report";
export default {
  name: "Support",
  components: { vueJsonEditor },
  props: {
    visib: {
      required: true,
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      fieldList:[],
      tableDataSql: [],
      dialogCaseResult: false,
      caseResultTitle: "",
      caseResultContent: null
    };
  },
  methods: {
    dataViewPreview(caseResultTitle, caseResultContent, caseResultFields) {
      // this.caseResultTitle = caseResultTitle;
      // this.caseResultContent = caseResultContent;

      this.tableDataSql = caseResultContent;
      this.fieldList = caseResultFields;
    },

    // 关闭模态框
    closeDialog() {
      this.$emit("handleClose");
    },
    onJsonChange(value) {},
    onJsonSave(value) {}
  }
};
</script>
