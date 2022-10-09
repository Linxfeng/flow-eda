<template>
  <div class="node-detail">
    <div class="detail-header">
      <div class="title">{{ node.nodeType.typeName }}</div>
      <el-button class="button el-button--small" style="float: right" type="primary" @click="submitNode(detailFormRef)">
        保存
      </el-button>
    </div>
    <div class="detail-body">
      <div v-if="node.output" class="item">
        <json-viewer :expand-depth=3 :value="node.output" copyable/>
        <hr/>
      </div>
      <el-form ref="detailFormRef" :model="detailForm" :rules="rules" class="row" label-position="top">
        <el-form-item class="item" label="名称：" prop="name">
          <el-input v-model="detailForm.name" class="input"/>
        </el-form-item>
        <el-form-item v-for="p in node.nodeType.params" :id="p.key" :key="p.key" :label="p.name+'：'" :prop="p.key"
                      class="item">
          <el-input v-if="p.inType==='input'" v-model="detailForm[p.key]" :placeholder="p.placeholder" class="input"/>
          <el-input v-if="p.inType==='select' && p.placeholder" v-model="detailForm[p.key]"
                    :placeholder="p.placeholder.split(',')[0]"
                    class="input-left"/>
          <el-select v-if="p.inType==='select' && p.placeholder" v-model="detailForm[p.key+'-o']" class="input-right">
            <el-option v-for="op in p.option.split(',')" :key="op" :label="op" :value="op"></el-option>
          </el-select>
          <el-select v-if="p.inType==='select' && !p.placeholder" v-model="detailForm[p.key]" clearable class="input">
            <el-option v-for="op in p.option.split(',')" :key="op" :label="op" :value="op"></el-option>
          </el-select>
          <el-select v-if="p.inType==='api'" v-model="detailForm[p.key]" clearable class="input">
            <el-option v-if="optionFlows.length" v-for="op in optionFlows" :key="op.id" :label="op.name"
                       :value="op.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item class="item" prop="payload">
          <span class="span-box">
              <el-tooltip content="参数为json格式，可传递至下一节点，使用${xx}接收，例如${httpResult.$0.name}"
                          placement="top">
                <i class="el-icon-question" style="color: #c0c4cc;"></i>
              </el-tooltip>
              <span style="color: #606266;"> 自定义参数：</span>
            </span>
          <el-input v-model="detailForm.payload" autosize="" class="input" placeholder="{'a':'xx','b':'123'}"
                    type="textarea"/>
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
import {ElMessage} from "element-plus";
import JsonViewer from 'vue-json-viewer';
import {listFlow} from "../../api/flow";

export default {
  name: "NodeDetail",
  props: {
    node: Object
  },
  components: {
    JsonViewer
  },
  setup(props, context) {
    // 节点参数定义
    const ps = props.node.nodeType.params;
    // 节点参数数据
    const pv = props.node.params;
    // 表单参数数据
    const form = {name: props.node.nodeName, remark: props.node.remark};
    if (props.node.payload) {
      form.payload = JSON.stringify(props.node.payload);
    }
    if (pv) {
      Object.keys(pv).forEach(k => form[k] = pv[k]);
    }
    const detailForm = reactive(form);
    const detailFormRef = ref(null);
    const optionFlows = reactive([]);

    // 校验输入的字符串是否是json格式
    const checkJson = (rule, value, callback) => {
      if (!value) {
        callback();
      }
      if (value.startsWith("{") && value.endsWith("}")) {
        try {
          JSON.parse(value);
          callback();
        } catch (ignore) {
        }
      }
      callback(new Error("Please input json format!"))
    };

    // 表单校验规则
    const ruleForm = {
      name: [{required: true, trigger: 'blur'}],
      payload: [{validator: checkJson, trigger: 'blur'}]
    };
    if (ps && ps.length > 0) {
      ps.forEach(p => {
        // 处理输入框+选择框=单个参数的情况，需要在form中拆成两个参数
        if (p.inType === 'select' && p.placeholder) {
          if (form[p.key]) {
            form[p.key + '-o'] = form[p.key].split(',')[1];
            form[p.key] = form[p.key].split(',')[0];
          } else {
            form[p.key + '-o'] = p.placeholder.split(',')[1];
          }
        }
        // 处理子流程等外部API调用结果
        if (p.inType === 'api') {
          listFlow({page: 1, limit: 1000}).then((res) => {
            if (res) {
              // 请求流程列表，排除自己
              res.result.forEach(f => {
                if (f.id !== props.node.flowId) {
                  optionFlows.push({id: f.id, name: f.name});
                }
              });
            }
          });
        }
        // 参数必填规则
        if (p.required === true) {
          ruleForm[p.key] = [{required: true, trigger: 'blur'}]
        }
      });
    }
    const rules = reactive(ruleForm);

    // 提交表单节点信息
    const submitNode = (formEl) => {
      formEl.validate((valid) => {
        if (valid) {
          // 将节点信息和节点属性信息封装好传给编辑器
          let params = {};
          Object.keys(detailForm).forEach(k => {
            if (k !== 'name' && k !== 'remark' && k !== 'payload'
                && detailForm[k] && detailForm[k] !== null) {
              if (detailForm[k + '-o']) {
                params[k] = detailForm[k] + ',' + detailForm[k + '-o'];
              } else if (!k.endsWith('-o')) {
                params[k] = detailForm[k];
              }
            }
          });
          props.node.nodeName = detailForm.name;
          if (detailForm.remark) {
            props.node.remark = detailForm.remark;
          }
          if (detailForm.payload) {
            props.node.payload = JSON.parse(detailForm.payload);
          } else {
            props.node.payload = null;
          }
          if (Object.keys(params).length === 0) {
            params = undefined;
          }
          context.emit("updateNode", props.node, params);
          ElMessage.success("保存成功");
        }
      });
    };

    return {
      detailForm,
      rules,
      detailFormRef,
      optionFlows,
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
  width: 28%;
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
      padding: 8px 8px 8px 14px;

      .item {
        margin-bottom: 12px;
      }

      .input {
        width: 100%;
      }

      .input-left {
        float: left;
        width: 50%;
      }

      .input-right {
        float: right;
        width: 50%;
      }
    }
  }
}
</style>
