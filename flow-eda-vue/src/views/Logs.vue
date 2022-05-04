<template>
  <div>
    <div class="crumbs">
      <el-breadcrumb separator="/">
        <el-breadcrumb-item>
          <span class="icon-lx-logs"></span> 日志管理
        </el-breadcrumb-item>
      </el-breadcrumb>
    </div>
    <div class="container">
      <div class="handle-box">
        <el-select v-model="params.type" class="handle-select mr10">
          <el-option key="RUNNING" label="运行日志" value="RUNNING"/>
          <el-option key="OPERATION" label="操作日志" value="OPERATION"/>
        </el-select>
        <el-button icon="el-icon-search" type="primary" @click="handleSearch">查询</el-button>
        <el-button :disabled="!hasSelection" icon="el-icon-delete" style="float: right" type="primary"
                   @click="delAllSelection">批量删除
        </el-button>
      </div>
      <el-table ref="multipleTable"
                :data="tableData"
                border
                class="table"
                header-cell-class-name="table-header"
                size="small"
                @selection-change="handleSelectionChange">
        <el-table-column align="center" type="selection" width="55"></el-table-column>
        <el-table-column label="日志类型" prop="type" show-overflow-tooltip width="345"></el-table-column>
        <el-table-column label="日志日期" prop="date" show-overflow-tooltip width="300"></el-table-column>
        <el-table-column label="流程名称" prop="flow" show-overflow-tooltip></el-table-column>
        <el-table-column label="文件大小(单位：kb)" prop="size" show-overflow-tooltip width="300"></el-table-column>
        <el-table-column align="center" label="操作" width="250">
          <template #default="scope">
            <el-button icon="el-icon-search" type="text" @click="handleShow(scope.row.path)">查看</el-button>
            <el-button icon="el-icon-delete" style="color: #ff0000" type="text" @click="handleDelete(scope.row.path)">
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="pagination">
        <el-pagination :current-page="params.page" :total="pageTotal" background layout="total, prev, pager, next"
                       @current-change="handlePageChange"></el-pagination>
      </div>
    </div>
  </div>
</template>

<script>
import {computed, reactive, ref} from "vue";
import {listLogs} from "../api/logs.js";
import {useRouter} from "vue-router";
import {useStore} from "vuex";

export default {
  name: "Logs",
  setup() {
    // 查询参数
    const params = reactive({
      page: 1,
      type: 'RUNNING'
    });
    const tableData = ref([]);
    const pageTotal = ref(0);

    // 查询日志列表
    const getData = () => {
      listLogs(params).then(res => {
        if (res) {
          pageTotal.value = res.total;
          tableData.value = res.result;
          tableData.value.forEach(v => {
            if (params.type === 'RUNNING') {
              v.type = "运行日志";
            } else {
              v.type = "操作日志";
            }
            if (!v.flow) {
              v.flow = "-";
            }
          });
        }
      });
    }
    getData();

    // 查询操作
    const handleSearch = () => {
      params.page = 1;
      getData();
    };

    // 分页导航
    const handlePageChange = (val) => {
      params.page = val;
      getData();
    };

    const router = useRouter();
    const store = useStore();
    const tagsList = computed(() => store.state.tagsList);

    // 查看详情，打开日志详情页
    const handleShow = (id) => {

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
    };


    return {
      params,
      tableData,
      pageTotal,
      hasSelection,
      handleSearch,
      handlePageChange,
      handleShow,
      handleSelectionChange,
      handleDelete,
      delAllSelection
    };
  },
};
</script>

<style scoped>
.handle-box {
  margin-bottom: 20px;
}

.handle-select {
  width: 300px;
}

.table {
  width: 100%;
  font-size: 14px;
}

.mr10 {
  margin-right: 10px;
}
</style>
