<template>
  <div>
    <div class="container">
      <div class="handle-box">
        <el-input
          v-model="params.name"
          class="handle-input mr10"
          placeholder="名称"
          @change="handleSearch"
        />
        <el-select
          v-model="params.status"
          class="handle-select mr10"
          placeholder="状态"
        >
          <el-option key="INIT" label="未运行" value="INIT" />
          <el-option key="RUNNING" label="运行中" value="RUNNING" />
          <el-option key="FINISHED" label="运行完成" value="FINISHED" />
          <el-option key="FAILED" label="运行失败" value="FAILED" />
        </el-select>
        <el-button :icon="Search" type="primary" @click="handleSearch"
          >查询</el-button
        >
        <el-button :icon="Refresh" type="primary" @click="cleanSearch"
          >重置</el-button
        >
        <el-button
          :disabled="!hasSelection"
          :icon="Delete"
          style="float: right"
          type="primary"
          @click="delAllSelection"
          >批量删除
        </el-button>
        <el-button
          :icon="Plus"
          style="float: right"
          type="primary"
          @click="handleAdd"
          >新增</el-button
        >
      </div>
      <el-table
        ref="multipleTable"
        :data="tableData"
        border
        class="table"
        header-cell-class-name="table-header"
        size="small"
        @selection-change="handleSelectionChange"
      >
        <el-table-column align="center" type="selection" width="45" />
        <el-table-column
          label="名称"
          prop="name"
          show-overflow-tooltip
          width="210"
        />
        <el-table-column
          :formatter="statusFormat"
          label="状态"
          prop="status"
          width="120"
        />
        <el-table-column
          label="描述"
          prop="description"
          show-overflow-tooltip
        />
        <el-table-column
          :formatter="dateFormat"
          label="创建时间"
          prop="createDate"
          width="170"
        />
        <el-table-column
          :formatter="dateFormat"
          label="更新时间"
          prop="updateDate"
          width="170"
        />
        <el-table-column align="center" label="操作" width="255">
          <template #default="scope">
            <el-button
              :icon="Search"
              type="primary"
              link
              @click="handleShow(scope.row.id)"
              >查看</el-button
            >
            <el-button
              :icon="Edit"
              type="primary"
              link
              @click="handleEdit(scope.$index, scope.row)"
              >修改</el-button
            >
            <el-button
              :icon="Tickets"
              type="primary"
              link
              @click="handleLogs(scope.row.id)"
              >日志</el-button
            >
            <el-button
              :icon="Delete" 
              type="danger"
              link
              @click="handleDelete(scope.row.id)"
              >删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="pagination">
        <el-pagination
          :current-page="params.page"
          :total="pageTotal"
          background
          layout="total, prev, pager, next"
          @current-change="handlePageChange"
        />
      </div>
    </div>
    <!-- 新增/编辑弹出框 -->
    <el-dialog v-model="dialogVisible" :title="form.title" center width="30%">
      <el-form label-width="70px">
        <el-form-item label="名称">
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="form.description" />
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
import { computed, reactive, ref } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import Moment from "moment";
import { addFlow, deleteFlow, listFlow, updateFlow } from "../api/flow";
import { generateUniqueID } from "../utils/util.js";
import { useRouter } from "vue-router";
import { useStore } from "vuex";
import { Delete, Edit, Search, Tickets, Refresh, Plus } from '@element-plus/icons-vue'

export default {
  name: "Flows",
  components: { Delete, Edit, Tickets, Search, Refresh, Plus }, 
  setup() {
    const params = reactive({ page: 1 });
    const tableData = ref([]);
    const pageTotal = ref(0);

    // 查询流程列表
    const getData = () => {
      listFlow(params).then((res) => {
        if (res) {
          tableData.value = res.result;
          pageTotal.value = res.total;
        }
      });
    };
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
      form.value.title = "新增流程";
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
      const tags = tagsList.value.filter((i) => i.path.startsWith(path));
      if (tags.length > 0 && tags[0].path.split("=")[1] !== id.toString()) {
        ElMessageBox.confirm(
          "您有未关闭编辑页，是否打开新的编辑页？这将会丢失未保存的数据！",
          "提示",
          {
            type: "warning",
          }
        )
          .then(() => {
            // 先销毁，再重新加载组件
            store.commit("delTagsItem", {
              index: tagsList.value.indexOf(tags[0]),
            });
            router.push({ path: path + id, query: { flowId: id } });
          })
          .catch((err) => {});
      } else {
        router.push({ path: path + id, query: { flowId: id } });
      }
    };
    // 编辑操作
    const handleEdit = (index, row) => {
      form.value.id = row.id;
      form.value.name = row.name;
      form.value.description = row.description;
      form.value.title = "修改流程";
      dialogVisible.value = true;
    };
    //新增/编辑弹框提交表单
    const submitForm = () => {
      let body = {
        id: form.value.id,
        name: form.value.name,
        description: form.value.description,
      };
      if (form.value.title === "新增流程") {
        body.id = generateUniqueID(8);
        addFlow(body).then((res) => {
          if (res) {
            ElMessage.success("操作成功");
            dialogVisible.value = false;
            getData();
          }
        });
      } else {
        updateFlow(body).then((res) => {
          if (res) {
            ElMessage.success("操作成功");
            dialogVisible.value = false;
            getData();
          }
        });
      }
    };

    // 查看流程日志
    const handleLogs = (id) => {
      const date = Moment().format("YYYY-MM-DD");
      const path = "/logs/running/" + id + "/" + date + ".log";
      router.push({ path: "/logs/detail", query: { path } });
    };

    // 多选操作
    let multipleSelection = [];
    let hasSelection = ref(false);
    const handleSelectionChange = (val) => {
      multipleSelection = val.map((i) => i.id);
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
      ElMessageBox.confirm(msg, "提示", {
        type: "warning",
      })
        .then(() => {
          deleteFlow(ids).then((res) => {
            if (res) {
              ElMessage.success("操作成功");
              params.page = 1;
              multipleSelection = [];
              getData();
            }
          });
        })
        .catch((err) => {});
    };

    // 状态字段转换
    const statusFormat = (row) => {
      if (row.status === "INIT") {
        return "未运行";
      } else if (row.status === "RUNNING") {
        return "运行中";
      } else if (row.status === "FINISHED") {
        return "运行完成";
      } else if (row.status === "FAILED") {
        return "运行失败";
      }
    };

    // 日期格式化
    const dateFormat = (row, column) => {
      return Moment(row[column.property]).format("YYYY-MM-DD HH:mm:ss");
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
      handleLogs,
      handleDelete,
      handleSelectionChange,
      delAllSelection,
      submitForm,
      statusFormat,
      dateFormat,
      Delete,
      Edit,
      Tickets,
      Search,
      Refresh,
      Plus
    };
  },
};
</script>

<style scoped>
.handle-box {
  margin-bottom: 20px;
}

.handle-select {
  width: 110px;
}

.handle-input {
  display: inline-block;
  width: 255px;
}

.table {
  width: 100%;
  font-size: 14px;
}

.mr10 {
  margin-right: 10px;
}

.el-button.is-link{
  padding: 0.2px;
}
</style>
