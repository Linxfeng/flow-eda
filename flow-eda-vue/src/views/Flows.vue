<template>
  <div>
    <div class="crumbs">
      <el-breadcrumb separator="/">
        <el-breadcrumb-item>
          <span class="icon-lx-flow"></span> 工作流管理
        </el-breadcrumb-item>
      </el-breadcrumb>
    </div>
    <div class="container">
      <div class="handle-box">
        <el-input v-model="params.name" class="handle-input mr10" placeholder="名称"></el-input>
        <el-select v-model="params.status" class="handle-select mr10" placeholder="状态">
          <el-option key="INIT" label="未运行" value="INIT"></el-option>
          <el-option key="RUNNING" label="运行中" value="RUNNING"></el-option>
          <el-option key="FINISHED" label="运行完成" value="FINISHED"></el-option>
          <el-option key="FAILED" label="运行失败" value="FAILED"></el-option>
        </el-select>
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
        <el-table-column label="名称" prop="name" show-overflow-tooltip width="250"></el-table-column>
        <el-table-column :formatter="statusFormat" label="状态" prop="status" width="120"></el-table-column>
        <el-table-column label="描述" prop="description" show-overflow-tooltip></el-table-column>
        <el-table-column :formatter="dateFormat" label="创建时间" prop="createDate" width="180"></el-table-column>
        <el-table-column :formatter="dateFormat" label="更新时间" prop="updateDate" width="180"></el-table-column>
        <el-table-column align="center" label="操作" width="240">
          <template #default="scope">
            <el-button icon="el-icon-search" type="text" @click="handleShow(scope.row.id)">查看</el-button>
            <el-button icon="el-icon-edit" type="text" @click="handleEdit(scope.$index, scope.row)">编辑</el-button>
            <el-button class="red" icon="el-icon-delete" type="text" @click="handleDelete(scope.row.id)">删除</el-button>
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
import {computed, reactive, ref} from "vue";
import {ElMessage, ElMessageBox} from "element-plus";
import Moment from "moment";
import {addFlow, deleteFlow, listFlow, updateFlow} from "../api/flow";
import {generateUniqueID} from "../utils/util.js";
import {useRouter} from "vue-router";
import {useStore} from "vuex";

export default {
  name: "Flows",
  setup() {
    const params = reactive({page: 1});
    const tableData = ref([]);
    const pageTotal = ref(0);

    // 查询工作流列表
    const getData = () => {
      listFlow(params).then(res => {
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
      params.page = 1;
      getData();
    };
    // 重置查询
    const cleanSearch = () => {
      params.page = 1;
      params.name = undefined;
      params.status = undefined;
      getData();
    };
    // 新增操作
    const dialogVisible = ref(false);
    let form = ref({});
    const handleAdd = () => {
      form.value = {};
      form.value.title = "新增工作流";
      dialogVisible.value = true;
    };
    // 分页导航
    const handlePageChange = (val) => {
      params.page = val;
      getData();
    };

    const router = useRouter();
    const store = useStore();
    const tagsList = computed(() => store.state.tagsList);

    // 查看详情，打开流编辑器
    const handleShow = (id) => {
      const path = "/flows/editor?flowId=";
      // 是否已经存在打开的编辑器页面
      const tags = tagsList.value.filter(i => i.path.startsWith(path));
      if (tags.length > 0 && tags[0].path.split("=")[1] !== id.toString()) {
        ElMessageBox.confirm("您有未关闭编辑页，是否打开新的编辑页？这将会丢失未保存的数据！", "提示", {
          type: "warning",
        }).then(() => {
          // 先销毁，再重新加载组件
          store.commit("delTagsItem", {index: tagsList.value.indexOf(tags[0])});
          router.push({path: path + id, query: {flowId: id}});
        }).catch(err => {
        });
      } else {
        router.push({path: path + id, query: {flowId: id}});
      }
    };
    // 编辑操作
    const handleEdit = (index, row) => {
      form.value.id = row.id;
      form.value.name = row.name;
      form.value.description = row.description;
      form.value.title = "编辑工作流";
      dialogVisible.value = true;
    };
    //新增/编辑弹框提交表单
    const submitForm = () => {
      let body = {
        id: form.value.id,
        name: form.value.name,
        description: form.value.description
      }
      if (form.value.title === "新增工作流") {
        body.id = generateUniqueID(8);
        addFlow(body).then(res => {
          if (res.message !== undefined) {
            ElMessage.error(res.message);
          } else {
            ElMessage.success("操作成功");
            dialogVisible.value = false;
            getData();
          }
        });
      } else {
        updateFlow(body).then(res => {
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
    const handleDelete = (id) => {
      let msg = "确定要删除吗？删除后将无法恢复！";
      let ids = [id];
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
        deleteFlow(ids).then(res => {
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

    // 状态字段转换
    const statusFormat = (row) => {
      if (row.status === 'INIT') {
        return "未运行";
      } else if (row.status === 'RUNNING') {
        return "运行中";
      } else if (row.status === 'FINISHED') {
        return "运行完成";
      } else if (row.status === 'FAILED') {
        return "运行失败";
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
      dialogVisible,
      form,
      handleSearch,
      cleanSearch,
      handleAdd,
      handlePageChange,
      handleShow,
      handleEdit,
      handleDelete,
      handleSelectionChange,
      delAllSelection,
      submitForm,
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
