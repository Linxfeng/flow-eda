<template>
  <div>
    <div class="crumbs">
      <el-breadcrumb separator="/">
        <el-breadcrumb-item>
          <span class="icon-lx-picklist_type"></span> 节点类型管理
        </el-breadcrumb-item>
      </el-breadcrumb>
    </div>
    <div class="container">
      <div class="handle-box">
        <el-input v-model="params.name" class="handle-input mr10" placeholder="名称"></el-input>
        <el-button icon="el-icon-search" type="primary" @click="handleSearch">查询</el-button>
        <el-button icon="el-icon-refresh" type="primary" @click="cleanSearch">重置</el-button>
        <el-button :disabled="!hasSelection" icon="el-icon-delete" style="float: right" type="primary"
                   @click="delAllSelection">批量删除
        </el-button>
        <el-button icon="el-icon-plus" style="float: right" type="primary" @click="handleAdd">新增</el-button>
      </div>
      <el-table ref="multipleTable"
                :data="tableData"
                border
                class="table"
                header-cell-class-name="table-header"
                size="small"
                @selection-change="handleSelectionChange">
        <el-table-column align="center" type="selection" width="55"></el-table-column>
        <el-table-column label="名称" prop="typeName" show-overflow-tooltip width="250"></el-table-column>
        <el-table-column label="描述" prop="description" show-overflow-tooltip></el-table-column>
        <el-table-column align="center" label="操作" width="280">
          <template #default="scope">
            <el-button icon="el-icon-edit" type="text" @click="handleEdit(scope.$index, scope.row)">编辑</el-button>
            <el-button class="red" icon="el-icon-delete" type="text" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="pagination">
        <el-pagination :current-page="params.page" :total="pageTotal" background layout="total, prev, pager, next"
                       @current-change="handlePageChange"></el-pagination>
      </div>
    </div>
    <!-- 新增/编辑弹出框 -->
    <el-dialog v-model="dialogVisible" :title="form.title" center width="30%">
      <el-form label-width="70px">
        <el-form-item label="名称">
          <el-input v-model="form.name"></el-input>
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="form.description"></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="submitForm">确 定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script>

import {reactive, ref} from "vue";
import {addNodeType, deleteNodeType, getNodeTypes, updateNodeType} from "../api/nodeType.js";
import {ElMessage, ElMessageBox} from "element-plus";

export default {
  name: "NodeTypes",
  setup() {

    const params = reactive({
      name: undefined,
      page: 1
    });
    const tableData = ref([]);
    const pageTotal = ref(0);

    // 查询节点类型列表
    function getData() {
      getNodeTypes(params).then(res => {
        if (res.message !== undefined) {
          ElMessage.error(res.message);
        } else {
          tableData.value = res.result;
          pageTotal.value = res.total;
        }
      });
    }

    // 进入页面加载列表
    getData();
    // 查询操作
    const handleSearch = () => {
      params.page = 1;
      getData();
    };
    // 重置查询
    const cleanSearch = () => {
      params.page = 1;
      params.name = undefined;
      getData();
    };
    // 定义弹出框数据
    const dialogVisible = ref(false);
    let form = ref({
      title: "",
      id: undefined,
      name: undefined,
      description: undefined
    });
    // 新增操作
    const handleAdd = () => {
      form.value.name = undefined;
      form.value.description = undefined;
      form.value.title = "新增节点类型";
      dialogVisible.value = true;
    };
    // 分页导航
    const handlePageChange = (val) => {
      params.page = val;
      getData();
    };
    // 编辑操作
    const handleEdit = (index, row) => {
      form.value.name = row.typeName;
      form.value.description = row.description;
      form.value.title = "编辑工作流";
      dialogVisible.value = true;
    };
    // 新增/编辑弹框提交表单
    const submitForm = () => {
      let body = {
        id: undefined,
        name: form.value.name,
        description: form.value.description
      }
      if (form.value.title === "新增工作流") {
        addNodeType(body).then(res => {
          if (res.message !== undefined) {
            ElMessage.error(res.message);
          } else {
            ElMessage.success("操作成功");
            dialogVisible.value = false;
            getData();
          }
        });
      } else {
        body.id = form.value.id;
        updateNodeType(body).then(res => {
          if (res.message !== undefined) {
            ElMessage.error(res.message);
          } else {
            ElMessage.success("操作成功");
            dialogVisible.value = false;
            getData();
          }
        });
      }
    };
    // 多选操作
    let multipleSelection = [];
    let hasSelection = ref(false);
    const handleSelectionChange = (val) => {
      multipleSelection = val.map(i => i.id);
      hasSelection.value = multipleSelection.length > 0;
    };
    // 删除操作
    const handleDelete = (row) => {
      let msg = "确定要删除吗？删除后将无法恢复！";
      let ids = [row.id];
      deleteBatch(msg, ids);
    };
    // 批量删除操作
    const delAllSelection = () => {
      let msg = "确定要删除所选中的项吗？删除后将无法恢复！";
      deleteBatch(msg, multipleSelection);
    };
    const deleteBatch = (msg, ids) => {
      params.page = 1;
      multipleSelection = [];
      ElMessageBox.confirm(msg, "提示", {
        type: "warning",
      }).then(() => {
        deleteNodeType(ids).then(res => {
          if (res.message !== undefined) {
            ElMessage.error(res.message);
          } else {
            ElMessage.success("操作成功");
            getData();
          }
        });
      }).catch(err => {
      });
    };

    return {
      params,
      tableData,
      pageTotal,
      hasSelection,
      dialogVisible,
      form,
      handleSearch,
      cleanSearch,
      handleAdd,
      handlePageChange,
      handleEdit,
      handleDelete,
      handleSelectionChange,
      delAllSelection,
      submitForm,
    };
  },
};
</script>

<style scoped>
.handle-box {
  margin-bottom: 20px;
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
