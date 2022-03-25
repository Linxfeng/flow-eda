<template>
  <div>
    <div class="crumbs">
      <el-breadcrumb separator="/">
        <el-breadcrumb-item>
          <i class="el-icon-lx-apps"></i> 工作流管理
        </el-breadcrumb-item>
      </el-breadcrumb>
    </div>
    <div class="container">
      <div class="handle-box">
        <el-input v-model="params.name" placeholder="名称" class="handle-input mr10"></el-input>
        <el-select v-model="params.status" placeholder="状态" class="handle-select mr10">
          <el-option key="1" label="启用" value="true"></el-option>
          <el-option key="0" label="禁用" value="false"></el-option>
        </el-select>
        <el-button type="primary" icon="el-icon-search" @click="handleSearch">查询</el-button>
        <el-button type="primary" icon="el-icon-refresh" @click="cleanSearch">重置</el-button>
        <el-button type="primary" icon="el-icon-delete" style="float: right" @click="delAllSelection"
                   :disabled="!hasSelection">批量删除
        </el-button>
        <el-button type="primary" icon="el-icon-plus" style="float: right" @click="handleAdd">新增</el-button>
      </div>
      <el-table :data="tableData"
                border
                class="table"
                ref="multipleTable"
                header-cell-class-name="table-header"
                @selection-change="handleSelectionChange"
                size="small">
        <el-table-column type="selection" width="55" align="center"></el-table-column>
        <el-table-column prop="name" label="名称" width="250" show-overflow-tooltip></el-table-column>
        <el-table-column prop="status" :formatter="statusFormat" label="状态" width="65"></el-table-column>
        <el-table-column prop="content" label="内容" show-overflow-tooltip></el-table-column>
        <el-table-column prop="description" label="描述" show-overflow-tooltip></el-table-column>
        <el-table-column prop="createAt" :formatter="dateFormat" label="创建日期" width="170"></el-table-column>
        <el-table-column prop="updateAt" :formatter="dateFormat" label="更新日期" width="170"></el-table-column>
        <el-table-column label="操作" width="280" align="center">
          <template #default="scope">
            <el-switch v-bind:value="scope.row.status" active-color="#13ce66" inactive-color="#dcdfe6"
                       @click="handleEnable(scope.row)" class="mr10"></el-switch>
            <el-button type="text" icon="el-icon-search" @click="handleShow(scope.$index, scope.row)">查看</el-button>
            <el-button type="text" icon="el-icon-edit" @click="handleEdit(scope.$index, scope.row)">编辑</el-button>
            <el-button type="text" icon="el-icon-delete" class="red" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="pagination">
        <el-pagination background layout="total, prev, pager, next" :current-page="params.page + 1"
                       :page-size="params.limit" :total="pageTotal" @current-change="handlePageChange"></el-pagination>
      </div>
    </div>
  </div>
</template>

<script>
import {reactive, ref} from "vue";
import {ElMessage, ElMessageBox} from "element-plus";
import {listFlows} from "../api/flow";
import Moment from "moment";

export default {
  name: "Flows",
  setup() {
    const params = reactive({
      page: 0,
      limit: 10,
    });
    const tableData = ref([]);
    const pageTotal = ref(0);

    // 查询工作流列表
    function getData() {
      listFlows(params).then(res => {
        if (res.message !== undefined) {
          ElMessage.error(res.message);
        } else {
          tableData.value = res.result;
          pageTotal.value = res.total;
        }
      });
    }

    getData();
    // 查询操作
    const handleSearch = () => {
      params.page = 0;
      getData();
    };
    const cleanSearch = () => {
      params.page = 0;
      params.name = undefined;
      params.status = undefined;
      getData();
    };
    // 新增操作
    const handleAdd = () => {
      params.page = 0;
      getData();
    };
    // 分页导航
    const handlePageChange = (val) => {
      params.page = val - 1;
      getData();
    };
    // 启用/禁用操作
    const handleEnable = (row) => {
      // 二次确认弹框
      let msg;
      if (row.status) {
        msg = "是否确定禁用？禁用后该工作流会立即停止运行！";
      } else {
        msg = "是否确定启用？启用后该工作流会立即开始运行！";
      }
      ElMessageBox.confirm(msg, "提示", {
        type: "warning",
      }).then(() => {
        row.status = !row.status;
        ElMessage.success("操作成功");
      }).catch(err => {
      });
    };
    // 编辑操作
    const handleEdit = (index, row) => {
      console.log(index)
      console.log(row.name)
    };
    // 删除操作
    const handleDelete = (row) => {
      console.log(row)
    };
    // 多选操作
    let multipleSelection = [];
    let hasSelection = ref(false);
    const handleSelectionChange = (val) => {
      multipleSelection = val.map(i => i._id);
      hasSelection.value = multipleSelection.length > 0;
    };
    // 批量删除
    const delAllSelection = () => {
    };
    // 状态字段转换
    const statusFormat = (row) => {
      if (row.status === true) {
        return "启用";
      } else {
        return "禁用";
      }
    };
    // 日期格式化
    const dateFormat = (row, column) => {
      return Moment(row[column.property]).format('YYYY-MM-DD HH:mm:ss')
    };
    return {
      params,
      tableData,
      pageTotal,
      hasSelection,
      handleSearch,
      cleanSearch,
      handleAdd,
      handlePageChange,
      handleEnable,
      handleEdit,
      handleDelete,
      handleSelectionChange,
      delAllSelection,
      statusFormat,
      dateFormat
    };
  },
};
</script>

<style scoped>
.handle-box {
  margin-bottom: 20px;
}

.handle-select {
  width: 120px;
}

.handle-input {
  width: 300px;
  display: inline-block;
}

.table {
  width: 100%;
  font-size: 14px;
}

.red {
  color: #ff0000;
}

.mr10 {
  margin-right: 10px;
}
</style>