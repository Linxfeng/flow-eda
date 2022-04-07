<template>
  <div v-click-outside="hideDetail" class="node-detail">
    <div class="detail-header">
      <div class="title">{{ node.typeName }}</div>
      <el-button class="button el-button--small" style="float: right" type="primary" @click="submitNode(detailFormRef)">
        保存
      </el-button>
    </div>
    <div class="detail-body">
      <el-form ref="detailFormRef" :model="detailForm" :rules="rules" class="row" label-position="top">
        <el-form-item class="item" label="名称：" prop="name">
          <el-input v-model="detailForm.name" class="input"/>
        </el-form-item>
        <el-form-item v-for="p in data.params" :id="p" :key="p" :label="p+'：'" :prop="p" class="item">
          <el-input v-model="detailForm[p]" class="input"/>
        </el-form-item>
        <el-form-item class="item" label="备注：" prop="remark">
          <el-input v-model="detailForm.remark" autosize="" class="input" type="textarea"/>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
import {reactive, ref} from "vue";
import vClickOutside from 'click-outside-vue3'
import {ElMessage} from "element-plus";

export default {
  name: "NodeDetail",
  props: {
    node: Object
  },
  directives: {
    clickOutside: vClickOutside.directive
  },
  setup(props, context) {
    // 节点参数，必填参数
    const data = reactive({
      params: [],
      required: []
    });
    if (props.node.required) {
      data.required = props.node.required.split(",");
    }

    // 表单内容
    const form = {name: props.node.nodeName, remark: props.node.remark};
    if (props.node.parameter) {
      data.params = props.node.parameter.split(",");
      data.params.forEach(p => {
        if (props.node.params) {
          form[p] = props.node.params[p];
        } else {
          form[p] = null;
        }
      });
    }
    const detailForm = reactive(form);
    const detailFormRef = ref(null);

    // 表单校验规则
    const ruleForm = {name: [{required: true, trigger: 'blur'}]};
    data.required.forEach(r => {
      ruleForm[r] = [{required: true, trigger: 'blur'}]
    });
    const rules = reactive(ruleForm);

    const hideDetail = () => {
      context.emit("showNodeDetail", props.node, false);
    };

    // 提交表单节点信息
    const submitNode = (formEl) => {
      formEl.validate((valid) => {
        if (valid) {
          // 将节点信息和节点属性信息封装好传给编辑器
          const params = {};
          Object.keys(detailForm).map(k => {
            if (k !== 'name' && k !== 'remark' && detailForm[k] && detailForm[k] !== null) {
              params[k] = detailForm[k];
            }
          });
          props.node.nodeName = detailForm.name;
          if (detailForm.remark) {
            props.node.remark = detailForm.remark;
          }
          context.emit("updateNode", props.node, params);
          ElMessage.success("保存成功");
        }
      });
    };

    return {
      data,
      detailForm,
      rules,
      detailFormRef,
      hideDetail,
      submitNode
    };
  }
};
</script>

<style lang="less">
.node-detail {
  height: 100%;
  overflow: auto;
  background: #ffffff;
  flex: 0 0 auto;
  float: left;
  width: 20%;
  border-right: 1px solid #E9E9E9;
  border-bottom: 1px solid #E9E9E9;

  .detail-header {
    padding: 12px 12px 10px 12px;
    min-height: 32px;
    border-bottom: 1px solid #DCE3E8;

    .title {
      float: left;
      font-size: 18px;
      text-align: center;
      line-height: 32px;
    }

    .button {
      font-size: 14px;
      padding: 7px 15px
    }
  }

  .detail-body {
    .row {
      padding: 8px 8px 8px 16px;

      .item {
        width: 98%;
        margin-bottom: 12px;
      }

      .input {
        width: 98%;
      }
    }
  }
}

.el-form--label-top .el-form-item__label {
  width: 98%;
  padding: 0 0 2px;
}

.el-input__inner {
  padding: 0 10px;
}
</style>
