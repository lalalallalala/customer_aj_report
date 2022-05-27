<template>
  <div>
    <!-- <el-input
      clearable
      v-model="uploadImgUrl"
      size="mini"
      @change="changeInput"
    /> -->
    <el-divider  />

    <!-- 自定义组件-图片上传 -->
    <el-form label-width="100px" label-position="left">
      <el-form-item label="图片选择">
        <!-- <el-button size="small" @click="handleImgUpload()"  type="primary"></el-button> -->
       <el-upload
        class="el-upload"
        ref="upload"
        :action="uploadUrl"
        :headers="headers"
        :on-success="handleUpload"
        :on-error="handleError"
        :show-file-list="false"
        :before-upload="handleBeforeUpload"
        :limit="1"
      >
        <el-button
          type="primary"
          icon="el-icon"
          v-permission="'fileManage:upload'"
          >上传新图片</el-button
        >
      </el-upload>
      
      
      </el-form-item>
    </el-form>

    <!-- 图片列表List -->
    <el-row  style="height:450px;" >
      <el-col  :span="11" :style="{marginTop:'5px'}" v-for="(o, index) in list" :key="o.id" :offset="index%2==0? 0:1">
         <el-image style="width: 100%; height: 100px;" 
          fit="fill" 
          :src="o.urlPath" 
          @click="handleImgClick(o, index)" />

      </el-col>
    </el-row> 
 
    <el-row :style="{margin:'-5px'}">
      <!-- 默认: page-size=10 pager-count>7 自动省略号  超过8条才有分页  -->
      <el-pagination small
          :total="totalCount"
          :page-size="params.pageSize"
          :current-page="params.pageNumber"
          :hide-on-single-page=true
          layout="prev, pager, next"
          @current-change="handleCurrentChange"
          />
    </el-row>
  </div>
</template>
<script>
import { fileList } from "@/api/file";
import { getToken } from "@/utils/auth";

export default {
  model: {
    prop: "value",
    event: "input"
  },
 props: {
    value: {
      type: "",
      default: ""
    }
  },
  data() {
    return {
      //图片列表加载参数
      params: {
        pageNumber: 1,
        pageSize: 8, //适配性比较高的用法
        order: "DESC",
        sort: "update_time"
      },
      totalCount: 0,
      totalPage: 0,
      list: [],
      //图片上传功能参数
      headers: {
        Authorization: getToken() // 直接从本地获取token就行
      },
      uploadUrl: process.env.BASE_API + "/file/upload",
      uploadImgUrl:"",
    };
  },
  created() {
    this.uploadImgUrl = this.value;
    this.queryByPageForImgList();
  },

  methods: {
    //加载图片列表方法
    async queryByPageForImgList(){
      const res = await fileList(this.params);
      if (res.code != "200") return;
      this.list = res.data.records;
      this.totalCount = res.data.total;
      this.totalPage = res.data.pages;

      // console.log("List", this.list);
    },
     //分页改变数据改变
    handleCurrentChange(val) {
        this.params.pageNumber = val;
        this.queryByPageForImgList();
    },
    //点击图片图片地址参数应该要跟着替换
    handleImgClick(o,index) {
      console.log(o.urlPath);
      // console.log(o);
      this.uploadImgUrl = o.urlPath;
      this.$emit("input", this.uploadImgUrl);
      this.$emit("change", this.uploadImgUrl);
    },

    // 上传检验 两种方式均可判断校验
    handleBeforeUpload(file) {
      const extension = file.name
        .split(".")
        [file.name.split(".").length - 1].toLowerCase();
      const extensionList = [
        "png",
        "jpg",
        "gif",
        "jpeg",
        "bmp",
      ];
      if (extensionList.indexOf(extension) < 0) {
        this.$message.warning("请上传正确的格式文件的图片");
        return false;
      }
      return true;
      //图片为icon 存在问题
      // let extension = file.name.split(".")
      // let type = file.type.split("/")[0];
      //  if (type === "image") {
      //     return true;
      //  }
      //  this.$message.warning("请上传正确的格式文件的图片");
      //  return false;
    },


    changeInput(e) {
      if (e) {
        this.uploadImgUrl = e;
      } else {
        // this.$refs.files.value = "";
        this.uploadImgUrl = "";
      }
      this.$emit("input", this.uploadImgUrl);
      this.$emit("change", this.uploadImgUrl);
    },

      // 上传成功的回调
    handleUpload(response, file, fileList) {
      // 触发查询按钮
      // this.$refs.listPage.handleQueryForm();
      //清除el-upload组件中的文件
       this.$message({
        message: "上传成功！",
        type: "success"
      });
      this.$refs.upload.clearFiles();
      this.queryByPageForImgList();
    },
    handleError() {
      this.$message({
        message: "上传失败！",
        type: "error"
      });
    },

  }
};
</script>
<style lang="scss" scoped>
</style>
