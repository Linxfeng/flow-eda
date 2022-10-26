/*
 Navicat Premium Data Transfer

 Source Server Type    : MySQL
 Source Server Version : 80027
 Source Schema         : flow_eda

 Target Server Type    : MySQL
 Target Server Version : 80027
 File Encoding         : 65001

 Date: 26/10/2022 15:47:51
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for eda_flow
-- ----------------------------
DROP TABLE IF EXISTS `eda_flow`;
CREATE TABLE `eda_flow`  (
  `id` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'id',
  `name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '名称',
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',
  `username` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户名',
  `status` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '状态',
  `create_date` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_date` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `index_create_date`(`create_date`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of eda_flow
-- ----------------------------
INSERT INTO `eda_flow` VALUES ('10j5nc7cz5k000', 'WebSocket客户端示例', '使用WebSocket客户端节点连接WebSocket服务，接收消息。前置条件：需要先运行流程[自定义WebSocket服务端]', 'test', 'FINISHED', '2022-05-30 16:40:58', '2022-06-04 18:13:10');
INSERT INTO `eda_flow` VALUES ('13mwrf5znqv400', 'MQTT订阅消息示例', '订阅MQTT消息，输出接收到的消息内容', 'test', 'FINISHED', '2022-06-01 18:01:21', '2022-06-01 18:01:21');
INSERT INTO `eda_flow` VALUES ('18rwqz520iio00', '自定义WebSocket服务端', '创建自定义WebSocket服务端，提供WebSocket服务', 'test', 'FINISHED', '2022-05-30 16:40:18', '2022-05-30 16:40:18');
INSERT INTO `eda_flow` VALUES ('1geaido0ecww00', '解析XML示例', '用于演示HTTP请求返回xml格式，以及XML格式内容的解析', 'test', 'FINISHED', '2022-10-19 17:57:10', '2022-10-19 17:57:28');
INSERT INTO `eda_flow` VALUES ('1p8nhh3c4aio00', '解析HTTP请求结果', '包含HTTP请求-解析器节点，解析请求结果并输出', 'test', 'FINISHED', '2022-05-09 16:03:39', '2022-05-09 16:03:39');
INSERT INTO `eda_flow` VALUES ('1pdye9g349ls00', '子流程示例-子流程', '用于演示嵌套流程的运行，本流程作为子流程', 'test', 'FINISHED', '2022-09-30 16:49:20', '2022-09-30 16:49:20');
INSERT INTO `eda_flow` VALUES ('28pm30nbgs2s00', 'MongoDB节点使用示例', '使用MongoDB节点执行自定义命令语句，输出执行结果。前置条件：需要具备或已知mongodb数据库地址，更新节点属性后再运行流程', 'test', 'FAILED', '2022-06-08 15:13:25', '2022-06-08 15:26:21');
INSERT INTO `eda_flow` VALUES ('2dzjjpdtijb400', '定时发起HTTP请求并附带输出时间', '定时器+HTTP请求+解析器+延时器，发起请求，延迟输出结果并附带请求时间', 'test', 'FINISHED', '2022-06-19 17:58:29', '2022-06-19 17:58:29');
INSERT INTO `eda_flow` VALUES ('2fnllj4jorfo00', '解析HTML示例', '用于演示HTTP请求返回html格式，以及HTML格式内容的解析', 'test', 'FINISHED', '2022-10-18 17:25:45', '2022-10-18 17:26:21');
INSERT INTO `eda_flow` VALUES ('34224hnftcm000', 'HTTP请求示例', '包含HTTP请求节点，发送网络请求，并输出响应结果', 'test', 'FINISHED', '2022-05-09 15:57:18', '2022-05-09 16:01:16');
INSERT INTO `eda_flow` VALUES ('395ecb7kvk8000', '发送邮件示例', '演示如何发送电子邮件，公开环境中如需测试，建议新建流程测试，请勿留下授权码以免被其他人利用', 'test', 'FINISHED', '2022-10-25 15:40:06', '2022-10-26 15:40:53');
INSERT INTO `eda_flow` VALUES ('3f1qhocnot6000', 'Redis节点使用示例', '使用Redis节点连接至指定的Redis服务器，执行自定义操作，查看执行结果和输出内容。前置条件：需要具备或已知Redis服务器地址，更新到节点属性上再运行流程', 'test', 'FAILED', '2022-06-08 17:20:44', '2022-06-08 17:22:24');
INSERT INTO `eda_flow` VALUES ('3gsaefsrjvc000', 'MQTT发布消息示例', '自定义发布消息到指定的MQTT中，并输出发送的消息内容', 'test', 'FINISHED', '2022-06-01 18:02:27', '2022-06-01 18:02:27');
INSERT INTO `eda_flow` VALUES ('3osi3b97pa4000', '延时器示例', '包含开始-延时器-输出等节点', 'test', 'FINISHED', '2022-05-09 15:16:18', '2022-05-09 15:16:18');
INSERT INTO `eda_flow` VALUES ('3uldr6qij5o000', '流程运行状态示例', '可查看流程运行状态，鼠标悬停状态图标，可查看详细信息', 'test', 'FINISHED', '2022-05-09 17:51:02', '2022-05-09 17:51:02');
INSERT INTO `eda_flow` VALUES ('3wccjaqq9y0000', '拼接节点示例', '使用拼接节点对分散的数据进行拼接', 'test', 'FINISHED', '2022-06-09 16:38:37', '2022-07-09 16:38:37');
INSERT INTO `eda_flow` VALUES ('41as9fmikt8000', '中止运行示例', '可在流程运行过程中，点击停止运行按钮，中止运行', 'test', 'FINISHED', '2022-05-09 17:47:20', '2022-05-09 17:47:20');
INSERT INTO `eda_flow` VALUES ('48r380g6qc4000', '解析器示例', '包含开始-解析器-输出等节点', 'test', 'FINISHED', '2022-05-09 15:11:38', '2022-05-09 15:11:38');
INSERT INTO `eda_flow` VALUES ('4d3es9ekt6m000', '分割节点示例', '使用分割节点对数据进行分割并输出', 'test', 'FINISHED', '2022-06-09 16:40:38', '2022-07-09 16:40:38');
INSERT INTO `eda_flow` VALUES ('4ufazyee92c000', 'Mysql节点使用示例', '使用Mysql节点连接至指定的mysql数据库，并执行自定义sql语句，查看执行结果。前置条件：需要具备或已知mysql数据库地址，更新到节点URL属性上再运行流程', 'test', 'FAILED', '2022-06-04 18:11:50', '2022-06-04 18:20:14');
INSERT INTO `eda_flow` VALUES ('4z9vdqsd5kg000', '增删改查-接口请求模型示例', '通过发送HTTP请求，对用户做增删改查操作的业务模型。前置条件：需要先运行流程[接口服务模型示例]', 'test', 'FINISHED', '2022-07-04 14:48:43', '2022-07-09 16:32:32');
INSERT INTO `eda_flow` VALUES ('5czjn0rra74000', '增删改查-接口服务模型示例', '展示了用户增删改查接口服务的业务模型。（先运行此流程后，再运行接口请求模型示例）', 'test', 'FINISHED', '2022-07-03 10:48:40', '2022-07-09 16:35:14');
INSERT INTO `eda_flow` VALUES ('5epeyaoy9ac000', '请求自定义HTTP服务', '发起HTTP请求，用于检查自定义HTTP服务的结果。前置条件：需要先运行流程[自定义HTTP服务示例]', 'test', 'FINISHED', '2022-06-19 15:13:06', '2022-06-19 15:13:06');
INSERT INTO `eda_flow` VALUES ('5pubb7joxmc000', '子流程示例-父流程', '用于演示嵌套流程的运行，本流程作为父流程', 'test', 'FINISHED', '2022-09-30 16:49:45', '2022-09-30 16:50:06');
INSERT INTO `eda_flow` VALUES ('6a088jl98eg00', '定时器示例', '仅包含定时器-输出两个节点', 'test', 'FINISHED', '2022-05-09 13:40:33', '2022-05-09 13:40:33');
INSERT INTO `eda_flow` VALUES ('ihvivrh5j1k00', '自定义HTTP服务示例', '创建自定义HTTP服务，处理请求并自定义响应数据', 'test', 'FINISHED', '2022-06-19 15:06:57', '2022-06-19 15:06:57');
INSERT INTO `eda_flow` VALUES ('m6o3aghqfrk00', '并行运行示例', '同一流程内可多条线路并行运行', 'test', 'FINISHED', '2022-05-09 17:58:27', '2022-05-09 17:58:27');
INSERT INTO `eda_flow` VALUES ('mk3eo1ewkk000', '节点参数传递', '任意节点之间都可以进行自定义参数传递，仅能从上一节点传递至下一节点，不可跨节点传递', 'test', 'FINISHED', '2022-05-09 15:20:00', '2022-05-09 15:56:06');
INSERT INTO `eda_flow` VALUES ('nm8hriar4ds00', '条件判断节点示例', '可使用条件节点组成与或等逻辑，可实现各种场景下的条件判断逻辑', 'test', 'FINISHED', '2022-07-05 10:05:57', '2022-07-05 15:08:32');
INSERT INTO `eda_flow` VALUES ('rjj5vbam81s00', '序列节点示例', '使用序列节点产生序列数并输出', 'test', 'FINISHED', '2022-06-09 16:37:05', '2022-07-09 16:37:05');
INSERT INTO `eda_flow` VALUES ('xu7fsb4whw000', '简单示例', '仅包含开始-输出两个节点', 'test', 'INIT', '2022-05-09 03:32:31', '2022-05-09 03:32:31');

-- ----------------------------
-- Table structure for eda_flow_node_data
-- ----------------------------
DROP TABLE IF EXISTS `eda_flow_node_data`;
CREATE TABLE `eda_flow_node_data`  (
  `id` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '节点id',
  `node_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '节点名称',
  `flow_id` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '流id',
  `type_id` bigint NULL DEFAULT NULL COMMENT '节点类型id',
  `top` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '当前节点位置top',
  `left` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '当前节点位置left',
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '节点备注信息',
  `params` json NULL COMMENT '节点属性参数',
  `payload` json NULL COMMENT '自定义参数',
  `from` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '连线起始点id',
  `to` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '连线结束点id',
  `version` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据版本',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `index_flow_id`(`flow_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of eda_flow_node_data
-- ----------------------------
INSERT INTO `eda_flow_node_data` VALUES ('10g0jr55t97k00', NULL, '28pm30nbgs2s00', NULL, NULL, NULL, NULL, NULL, NULL, '51ghbw0d2pg000', '30bxvr7v880000', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('10ku6qoplgow00', NULL, '3f1qhocnot6000', NULL, NULL, NULL, NULL, NULL, NULL, '50tl9rn2u0o000', '1rxk06czqjy800', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('117er0nxdy3k00', '名称未传', '5czjn0rra74000', 5, '80px', '335px', NULL, '{\"field\": \"name\", \"value\": \"null\", \"condition\": \"=\"}', NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('122wnmuawvgw00', NULL, '3uldr6qij5o000', NULL, NULL, NULL, NULL, NULL, NULL, '1rdi0c65pyhs00', '4nhpgowo3mg000', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('13baatlzz27400', '开始请求', '4z9vdqsd5kg000', 1, '60px', '50px', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('154kjyfueaf400', NULL, '18rwqz520iio00', NULL, NULL, NULL, NULL, NULL, NULL, '5sfvkcmh4a0000', '19iwhrcrtxy800', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('15xjxewyz0g000', NULL, '1geaido0ecww00', NULL, NULL, NULL, NULL, NULL, NULL, '5gy6u69jvs4000', 'q3gss728upc00', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('16q07apqfi7400', 'HTTP响应', '5czjn0rra74000', 33, '265px', '490px', NULL, '{\"uri\": \"/api/http/test/user\", \"method\": \"GET\", \"resData\": \"{\\\"result\\\": \\\"查询失败！名称不能为空\\\"}\"}', NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('17lf5km6vn2800', 'HTTP接收', 'ihvivrh5j1k00', 32, '310px', '265px', NULL, '{\"uri\": \"/api/http/test2\", \"method\": \"POST\"}', NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('17xim81temm800', NULL, '5czjn0rra74000', NULL, NULL, NULL, NULL, NULL, NULL, '498r7jvg1xq000', '2brn0wxuo0is00', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('17zy5yhhmn3400', '分割', '4d3es9ekt6m000', 8, '530px', '555px', NULL, '{\"field\": \"httpResult.data.list\", \"outputWay\": \"多次输出单值\"}', NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('183um31kt4ow00', '删除-输出', '4z9vdqsd5kg000', 4, '410px', '495px', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('18fl3883e75s00', 'MQTT发布', '3gsaefsrjvc000', 38, '55px', '320px', NULL, '{\"topic\": \"/mqtt/test/a\", \"broker\": \"tcp://broker.emqx.io,1883\", \"message\": \"a: ${timestamp}\", \"clientId\": \"mqttx_9793d5c8\"}', NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('18sa1eux7gn400', '校验名称', '5czjn0rra74000', 5, '200px', '335px', NULL, '{\"field\": \"name\", \"value\": \"null\", \"condition\": \"!=\"}', NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('18ungqluatj400', NULL, '1pdye9g349ls00', NULL, NULL, NULL, NULL, NULL, NULL, '5ladpnlftek000', '4l42m65xah4000', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('18yvqfdjxqio00', NULL, '5epeyaoy9ac000', NULL, NULL, NULL, NULL, NULL, NULL, '411ip98he9m000', '5bzdqxyeypw000', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('19bxchzilodc00', NULL, '5czjn0rra74000', NULL, NULL, NULL, NULL, NULL, NULL, '1dfxnb4vcxfk00', '498r7jvg1xq000', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('19iwhrcrtxy800', '输出', '18rwqz520iio00', 4, '205px', '410px', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('1acrzdqrg8g000', '异常输出', '5czjn0rra74000', 4, '405px', '645px', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('1ai2eg1oclsw00', '输出', '4d3es9ekt6m000', 4, '380px', '555px', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('1ap72p9gkadc00', NULL, '4z9vdqsd5kg000', NULL, NULL, NULL, NULL, NULL, NULL, '1uvffcb7y6v400', '34rhcpn8azc000', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('1aqv4dp478n400', '异常输出', '5czjn0rra74000', 4, '140px', '645px', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('1dfxnb4vcxfk00', '删除用户', '5czjn0rra74000', 32, '480px', '170px', NULL, '{\"uri\": \"/api/http/test/user\", \"method\": \"DELETE\"}', NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('1dslfud1adxc00', '发送html格式邮件', '395ecb7kvk8000', 1, '95px', '65px', NULL, NULL, '{\"html\": \"<h1>《青溪》</h1><h5>作者：王维</h5><div>  <p>言入黄花川，每逐青溪水。</p>  <p>随山将万转，趣途无百里。</p>  <p>声喧乱石中，色静深松里。</p>  <p>漾漾泛菱荇，澄澄映葭苇。</p>  <p>我心素已闲，清川澹如此。</p>  <p>请留盘石上，垂钓将已矣。</p></div>\"}', NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('1f2mfwal1o2o00', '输出1', 'm6o3aghqfrk00', 4, '195px', '90px', NULL, NULL, '{\"a\": \"${a}\"}', NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('1fmu962j2sg000', NULL, 'nm8hriar4ds00', NULL, NULL, NULL, NULL, NULL, NULL, '3qaytav7tx0000', '3jkj8klpbec000', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('1fukchv3s31c00', 'Mysql', '4ufazyee92c000', 51, '225px', '345px', NULL, '{\"sql\": \"SELECT * FROM t_user\", \"url\": \"jdbc:mysql://127.0.0.1:3306/flow_eda_test\", \"password\": \"123456\", \"username\": \"root\"}', NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('1g5eivmtkbi800', '开始1', 'm6o3aghqfrk00', 1, '95px', '90px', NULL, NULL, '{\"a\": \"xxx-1\"}', NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('1gd04j0vgqg000', NULL, '3osi3b97pa4000', NULL, NULL, NULL, NULL, NULL, NULL, 'brjl0erogio00', 'zzlzi607jv400', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('1hljr228155s00', '分割', '4d3es9ekt6m000', 8, '70px', '300px', NULL, '{\"field\": \"params.data\"}', NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('1igvaxw4v26800', NULL, '5czjn0rra74000', NULL, NULL, NULL, NULL, NULL, NULL, '4o5c49zbtx8000', '5jw0u8ido0w000', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('1jlsdvqqle8w00', NULL, '10j5nc7cz5k000', NULL, NULL, NULL, NULL, NULL, NULL, '2m9tp4kwxka000', '5aamavmwhb4000', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('1js10mqz518g00', '输出4', 'm6o3aghqfrk00', 4, '195px', '730px', NULL, NULL, '{\"a\": \"${a}\"}', NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('1kkfxrxf1ri800', '发送文本格式邮件', '395ecb7kvk8000', 1, '285px', '65px', NULL, NULL, '{\"text\": \"您好：\\n    这是一封测试邮件，此处为邮件正文内容。\\n\\n感谢支持！\\nfrom: flow-eda\"}', NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('1kwi6wlpiz8g00', NULL, 'rjj5vbam81s00', NULL, NULL, NULL, NULL, NULL, NULL, '2erc5oeof3bw00', '5by08g691os000', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('1kyy9g2veups00', NULL, '2dzjjpdtijb400', NULL, NULL, NULL, NULL, NULL, NULL, '5r7cwz1vxk4000', '5t6dx3m3ras000', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('1lfx1fykqyrk00', NULL, 'nm8hriar4ds00', NULL, NULL, NULL, NULL, NULL, NULL, '4ujp6bqblq0000', '4rqhngznbfg000', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('1n52uwx88z280', NULL, 'm6o3aghqfrk00', NULL, NULL, NULL, NULL, NULL, NULL, '2h0b5raks2s000', '2ryt3dgbcxa000', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('1n5cxdr0tmdc00', '输出', '3uldr6qij5o000', 4, '285px', '645px', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('1oa6uw3p01og00', NULL, 'm6o3aghqfrk00', NULL, NULL, NULL, NULL, NULL, NULL, '3g6glxdv31k000', '52y7k5m4788000', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('1p163gjqywn400', '输出', '3wccjaqq9y0000', 4, '160px', '495px', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('1pzzfeot9s9s00', 'HTTP响应', '5czjn0rra74000', 33, '480px', '795px', NULL, '{\"uri\": \"/api/http/test/user\", \"method\": \"POST\", \"resData\": \"{\\\"result\\\": \\\"删除成功\\\"}\"}', NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('1qgmqxt2lzy800', '成功输出', '5czjn0rra74000', 4, '265px', '795px', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('1rdi0c65pyhs00', '开始', '3uldr6qij5o000', 1, '165px', '215px', NULL, NULL, '{\"a\": \"xxx\"}', NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('1rxk06czqjy800', 'Redis', '3f1qhocnot6000', 53, '210px', '305px', NULL, '{\"uri\": \"127.0.0.1:6379\", \"args\": \"test\", \"method\": \"getDel\"}', NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('1sr3iq1cv1vk00', NULL, '2fnllj4jorfo00', NULL, NULL, NULL, NULL, NULL, NULL, '4gpcx6xl5gk000', '5ipjxxvv0co000', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('1tp9a7gjvmhs00', '输出', '4d3es9ekt6m000', 4, '175px', '555px', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('1tvikak80uow00', NULL, '5czjn0rra74000', NULL, NULL, NULL, NULL, NULL, NULL, 'u13z5xhhdz400', '2anvx77i7mck00', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('1u3cl7n0dlq800', NULL, 'm6o3aghqfrk00', NULL, NULL, NULL, NULL, NULL, NULL, 'qfki3k8xu4000', '52y7k5m4788000', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('1uvffcb7y6v400', '删除用户请求', '4z9vdqsd5kg000', 31, '410px', '255px', NULL, '{\"url\": \"/api/http/test/user\", \"method\": \"DELETE\", \"params\": \"id=${httpResult.result.id}\"}', NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('1uyde8bz0kn400', 'HTTP响应', 'ihvivrh5j1k00', 33, '170px', '455px', NULL, '{\"uri\": \"/api/http/test1\", \"method\": \"GET\"}', NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('1v8jtqz60kps00', NULL, '3uldr6qij5o000', NULL, NULL, NULL, NULL, NULL, NULL, '1rdi0c65pyhs00', '4tnr4rsnzdw000', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('1vrlpkfsd0xs00', '解析结果', '5czjn0rra74000', 21, '200px', '645px', NULL, '{\"parseKey\": \"result\"}', NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('1vym9mvg117k00', '输出', '6a088jl98eg00', 4, '190px', '520px', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('1wb2ubmere8w00', '开始', 'xu7fsb4whw000', 1, '105px', '305px', '将此节点的自定义参数传递至下一节点', NULL, '{\"a\": \"xxx\"}', NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('1whclji69jr400', '查询用户', '5czjn0rra74000', 32, '200px', '170px', NULL, '{\"uri\": \"/api/http/test/user\", \"method\": \"GET\"}', NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('1wlyvnsoyjcw00', NULL, '1geaido0ecww00', NULL, NULL, NULL, NULL, NULL, NULL, 'q3gss728upc00', '3gk5d3qtw5o000', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('1wq99npn0scg00', '输出', 'ihvivrh5j1k00', 4, '425px', '265px', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('1wskr9swx1mo00', NULL, '4z9vdqsd5kg000', NULL, NULL, NULL, NULL, NULL, NULL, '59tktp4osh8000', '5d4whpuzl5k000', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('1x87ug825pj400', NULL, 'mk3eo1ewkk000', NULL, NULL, NULL, NULL, NULL, NULL, '3m52bl5i5pc000', '69mzkmq9x4g00', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('1xlaitqctv9c00', NULL, '34224hnftcm000', NULL, NULL, NULL, NULL, NULL, NULL, '5erg81rz21s000', '4hq5bc15yy0000', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('1xz6zrozzo6800', '校验id', '5czjn0rra74000', 5, '480px', '335px', NULL, '{\"field\": \"id\", \"value\": \"null\", \"condition\": \"!=\"}', NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('1yzp3l5t8xhc00', NULL, 'nm8hriar4ds00', NULL, NULL, NULL, NULL, NULL, NULL, '3jkj8klpbec000', '2vqz4rju3k6000', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('1zf3801jf3cw00', '输出', '3gsaefsrjvc000', 4, '240px', '520px', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('1zv25by68fwg00', '输出', '395ecb7kvk8000', 4, '385px', '210px', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('2185e2qca4rk00', '输出', '1geaido0ecww00', 4, '400px', '545px', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('21qtdsmw9vds00', '异常响应', '5czjn0rra74000', 33, '405px', '490px', NULL, '{\"uri\": \"/api/http/test/user\", \"method\": \"PUT\", \"resData\": \"{\\\"result\\\": \\\"更新失败！id不能为空\\\"}\"}', NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('25011ay5u9og00', '输出', '4d3es9ekt6m000', 4, '70px', '555px', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('25drumgr0eio00', '输出', '4ufazyee92c000', 4, '140px', '550px', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('25oxq4xoglq800', '延时1秒', '3gsaefsrjvc000', 3, '145px', '150px', NULL, '{\"delay\": \"1,SECONDS\"}', '{\"timestamp\": \"${timestamp}\"}', NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('26qf0znlzpk000', '开始', '3f1qhocnot6000', 1, '85px', '105px', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('26w3qby6cj4000', '输出', 'nm8hriar4ds00', 4, '80px', '555px', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('27k4z0xei05c00', '输出', '13mwrf5znqv400', 4, '120px', '375px', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('27s1wzggeb400', '开始', '10j5nc7cz5k000', 1, '150px', '30px', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('27tpw0xsb2f400', NULL, '2dzjjpdtijb400', NULL, NULL, NULL, NULL, NULL, NULL, '5r7cwz1vxk4000', 'g8ihojph78000', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('27yk8bgtjcg000', NULL, '5czjn0rra74000', NULL, NULL, NULL, NULL, NULL, NULL, '43a35b7dfte000', '2xb46vc15ny000', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('290zgf5i43r400', '输出', 'nm8hriar4ds00', 4, '460px', '555px', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('29cput2xohwk00', '输出3', 'm6o3aghqfrk00', 4, '195px', '505px', NULL, NULL, '{\"a\": \"${a}\"}', NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('29diviebjd0k00', NULL, '395ecb7kvk8000', NULL, NULL, NULL, NULL, NULL, NULL, '1dslfud1adxc00', '3zvkwj9xxz6000', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('2amxn5fho2jo00', NULL, '18rwqz520iio00', NULL, NULL, NULL, NULL, NULL, NULL, '2j6e6bbqmnm00', '4q46w3pes0m000', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('2anvx77i7mck00', '校验id', '5czjn0rra74000', 5, '335px', '335px', NULL, '{\"field\": \"id\", \"value\": \"null\", \"condition\": \"!=\"}', NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('2atrjgiljvwg00', NULL, '5epeyaoy9ac000', NULL, NULL, NULL, NULL, NULL, NULL, '43pjrbrrcbk000', '5cd592qljf4000', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('2atxruw6rr4000', NULL, '5czjn0rra74000', NULL, NULL, NULL, NULL, NULL, NULL, '117er0nxdy3k00', '35zmdn1sy12000', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('2bk45mph7xno00', '输出', '3uldr6qij5o000', 4, '60px', '645px', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('2bogsv077ls000', '异常输出', '5czjn0rra74000', 4, '265px', '645px', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('2brn0wxuo0is00', '异常响应', '5czjn0rra74000', 33, '550px', '490px', NULL, '{\"uri\": \"/api/http/test/user\", \"method\": \"DELETE\", \"resData\": \"{\\\"result\\\": \\\"删除失败！id不能为空\\\"}\"}', NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('2c9uhbkimbb400', '输出', 'ihvivrh5j1k00', 4, '170px', '265px', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('2cxstxskz2sk00', '拼接', '3wccjaqq9y0000', 7, '300px', '265px', NULL, '{\"field\": \"index\"}', NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('2d8yal0m4oro00', '输出', 'ihvivrh5j1k00', 4, '170px', '655px', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('2di5j5yhnhno00', NULL, '1geaido0ecww00', NULL, NULL, NULL, NULL, NULL, NULL, '4i0ng2xc5oc000', '5lrcvc6z3l8000', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('2egomnt9fffo00', NULL, '4d3es9ekt6m000', NULL, NULL, NULL, NULL, NULL, NULL, '1hljr228155s00', '25011ay5u9og00', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('2eo9v47nmtgk00', NULL, '4z9vdqsd5kg000', NULL, NULL, NULL, NULL, NULL, NULL, '76jspatw86800', '59tktp4osh8000', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('2eqcm8iq2mm800', '定时器', '6a088jl98eg00', 2, '80px', '380px', NULL, '{\"times\": \"3\", \"period\": \"2,SECONDS\", \"timestamp\": \"HH:mm:ss\"}', '{\"a\": \"xxx\"}', NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('2erc5oeof3bw00', '序列', 'rjj5vbam81s00', 6, '290px', '315px', NULL, '{\"end\": \"10\", \"start\": \"1\"}', NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('2euqp5sbs1xc00', NULL, '28pm30nbgs2s00', NULL, NULL, NULL, NULL, NULL, NULL, '45ho9eos0jk000', '51ghbw0d2pg000', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('2fdpbt1wnias00', NULL, '2dzjjpdtijb400', NULL, NULL, NULL, NULL, NULL, NULL, '5t6dx3m3ras000', 'lxwmugqrwlc00', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('2fn9nu160uv400', NULL, 'm6o3aghqfrk00', NULL, NULL, NULL, NULL, NULL, NULL, '1f2mfwal1o2o00', '3dyid08c2ei000', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('2fwt1w3fxa1w00', '查询-输出', '4z9vdqsd5kg000', 4, '495px', '495px', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('2fzb3iuepadc00', '输出', '3gsaefsrjvc000', 4, '55px', '520px', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('2h0b5raks2s000', '开始2', 'm6o3aghqfrk00', 1, '95px', '295px', NULL, NULL, '{\"a\": \"xxx-2\"}', NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('2h13acff68e00', NULL, '1pdye9g349ls00', NULL, NULL, NULL, NULL, NULL, NULL, '4nc3gc5i8zy000', '3fk8iqt33l0000', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('2hgaqx0klnq000', '删除数据', '5czjn0rra74000', 51, '480px', '490px', NULL, '{\"sql\": \"DELETE FROM t_user WHERE `id`=\'${id}\';\", \"url\": \"jdbc:mysql://127.0.0.1:3306/flow_eda_test\", \"password\": \"123456\", \"username\": \"root\"}', NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('2ht62wzl5qg000', NULL, '4z9vdqsd5kg000', NULL, NULL, NULL, NULL, NULL, NULL, '55g0z7vdyrk000', '76jspatw86800', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('2it9oviuhxa000', NULL, 'nm8hriar4ds00', NULL, NULL, NULL, NULL, NULL, NULL, '2tkojjhfr6k000', '4ujp6bqblq0000', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('2j3i3kzpzfc000', NULL, 'm6o3aghqfrk00', NULL, NULL, NULL, NULL, NULL, NULL, '29cput2xohwk00', '5pxuhe3ufy8000', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('2j6e6bbqmnm00', '定时器', '18rwqz520iio00', 2, '125px', '120px', NULL, '{\"times\": \"3\", \"period\": \"10,SECONDS\", \"timestamp\": \"HH:mm:ss\"}', NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('2jj7ukeunlu000', NULL, '3gsaefsrjvc000', NULL, NULL, NULL, NULL, NULL, NULL, '3wgobbd6ol4000', '1zf3801jf3cw00', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('2k4hia9c8as000', NULL, 'nm8hriar4ds00', NULL, NULL, NULL, NULL, NULL, NULL, '2tkojjhfr6k000', 'zffco5t1e9c00', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('2lh8qa2f8x4000', NULL, 'nm8hriar4ds00', NULL, NULL, NULL, NULL, NULL, NULL, '4rqhngznbfg000', '26w3qby6cj4000', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('2lrton3eqms000', NULL, '3gsaefsrjvc000', NULL, NULL, NULL, NULL, NULL, NULL, '4c8mzac4do6000', '25oxq4xoglq800', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('2m8fjvz59g8000', NULL, '3wccjaqq9y0000', NULL, NULL, NULL, NULL, NULL, NULL, '2rj98hbteum000', '1p163gjqywn400', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('2m9tp4kwxka000', 'WS客户端2', '10j5nc7cz5k000', 35, '220px', '185px', NULL, '{\"path\": \"/w/test?name=zhang\", \"sendAfterConnect\": \"hello server! I am zhang.\"}', NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('2mu3x58m5qu000', NULL, '41as9fmikt8000', NULL, NULL, NULL, NULL, NULL, NULL, '5bhkyaxsw7o000', 'bgid30y68q000', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('2nlfrc4xpqq000', NULL, '5czjn0rra74000', NULL, NULL, NULL, NULL, NULL, NULL, '5boelqzz2j4000', '36tf4sqel52000', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('2nwem6we9ni000', NULL, 'ihvivrh5j1k00', NULL, NULL, NULL, NULL, NULL, NULL, '3aiwboej4io000', '4f6s4osxhts000', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('2og9qsc4cbm000', NULL, 'ihvivrh5j1k00', NULL, NULL, NULL, NULL, NULL, NULL, '2c9uhbkimbb400', '1uyde8bz0kn400', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('2ojyy2yxe6g000', '请求成功', 'nm8hriar4ds00', 5, '460px', '380px', NULL, '{\"field\": \"httpResult.code\", \"value\": \"200\", \"condition\": \"=\"}', NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('2omkofzgbi8000', '开始', '1p8nhh3c4aio00', 1, '105px', '115px', NULL, NULL, '{\"a\": \"xxx\", \"q\": \"api/v1\"}', NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('2pjrhd01zaw000', NULL, '5epeyaoy9ac000', NULL, NULL, NULL, NULL, NULL, NULL, '9756rhigrok00', '43pjrbrrcbk000', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('2ps0q19lj9c000', '更新数据', '5czjn0rra74000', 51, '335px', '490px', NULL, '{\"sql\": \"UPDATE t_user SET `name`=\'${name}\' WHERE `id`=\'${id}\';\", \"url\": \"jdbc:mysql://127.0.0.1:3306/flow_eda_test\", \"password\": \"123456\", \"username\": \"root\"}', NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('2pujxm26szq000', NULL, '4d3es9ekt6m000', NULL, NULL, NULL, NULL, NULL, NULL, 't3126jimym800', '1tp9a7gjvmhs00', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('2qojctqhx10000', NULL, '1geaido0ecww00', NULL, NULL, NULL, NULL, NULL, NULL, '5chegqfj09w000', '2185e2qca4rk00', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('2qyczc2rvhc000', NULL, 'nm8hriar4ds00', NULL, NULL, NULL, NULL, NULL, NULL, '4186ftn260o000', '5cql4jfpft4000', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('2rj98hbteum000', '序列', '3wccjaqq9y0000', 6, '160px', '265px', NULL, '{\"end\": \"10\", \"start\": \"1\"}', NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('2rm1j96dqw2000', '输出', '2dzjjpdtijb400', 4, '250px', '645px', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('2rm699cuvh60', 'id未传', '5czjn0rra74000', 5, '405px', '335px', NULL, '{\"field\": \"id\", \"value\": \"null\", \"condition\": \"=\"}', NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('2ryt3dgbcxa000', '输出2', 'm6o3aghqfrk00', 4, '195px', '295px', NULL, NULL, '{\"a\": \"${a}\"}', NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('2saklu2u4jc000', NULL, '3gsaefsrjvc000', NULL, NULL, NULL, NULL, NULL, NULL, '4c8mzac4do6000', '5ugkbmc5hmo000', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('2svs614ygns000', '成功输出', '5czjn0rra74000', 4, '405px', '795px', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('2tkojjhfr6k000', '开始', 'nm8hriar4ds00', 1, '80px', '45px', NULL, NULL, '{\"a\": \"xxx\"}', NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('2tzqefwvm4g000', '新增-输出', '4z9vdqsd5kg000', 4, '60px', '495px', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('2uf89nhu44a000', NULL, '5czjn0rra74000', NULL, NULL, NULL, NULL, NULL, NULL, '1whclji69jr400', '35dlfubp3vm000', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('2v89h2x2fy2000', NULL, 'nm8hriar4ds00', NULL, NULL, NULL, NULL, NULL, NULL, '997ksch8axg00', '5rb9xhczuk0000', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('2va1l6btxuo000', NULL, '5czjn0rra74000', NULL, NULL, NULL, NULL, NULL, NULL, '2anvx77i7mck00', '2ps0q19lj9c000', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('2vqz4rju3k6000', '输出', 'nm8hriar4ds00', 4, '530px', '555px', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('2vsmnqhpwpk000', NULL, '5czjn0rra74000', NULL, NULL, NULL, NULL, NULL, NULL, '1whclji69jr400', '18sa1eux7gn400', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('2wyvgvfidvy000', '更新-输出', '4z9vdqsd5kg000', 4, '240px', '495px', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('2x82lwrg35u000', NULL, '5czjn0rra74000', NULL, NULL, NULL, NULL, NULL, NULL, '1pzzfeot9s9s00', '30w8roj1bm800', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('2x8fkxaliqi000', NULL, '4d3es9ekt6m000', NULL, NULL, NULL, NULL, NULL, NULL, '44jgeaxixf8000', '3151fnzygru000', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('2xb46vc15ny000', '新增用户', '5czjn0rra74000', 32, '80px', '170px', NULL, '{\"uri\": \"/api/http/test/user\", \"method\": \"POST\"}', NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('2z6pgokgpoy000', NULL, '3gsaefsrjvc000', NULL, NULL, NULL, NULL, NULL, NULL, '18fl3883e75s00', '2fzb3iuepadc00', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('305ivvon2pe000', NULL, '10j5nc7cz5k000', NULL, NULL, NULL, NULL, NULL, NULL, '5plrpukibww000', '32f3jhztrau000', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('308b6jzmpcy000', '子流程', '5pubb7joxmc000', 100, '255px', '205px', NULL, '{\"subflow\": \"1pdye9g349ls00\"}', NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('30b3ekkx5f4000', '开始', '4d3es9ekt6m000', 1, '70px', '65px', NULL, NULL, '{\"data\": \"1,2,3,4,5,6,7,8,9,10\"}', NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('30bxvr7v880000', '输出', '28pm30nbgs2s00', 4, '120px', '455px', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('30ip6fad25y000', NULL, '4d3es9ekt6m000', NULL, NULL, NULL, NULL, NULL, NULL, '17zy5yhhmn3400', '1ai2eg1oclsw00', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('30vsiq5jvy6000', NULL, 'rjj5vbam81s00', NULL, NULL, NULL, NULL, NULL, NULL, '53lszmnskwc000', '2erc5oeof3bw00', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('30w8roj1bm800', '成功输出', '5czjn0rra74000', 4, '550px', '795px', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('3151fnzygru000', 'HTTP请求', '4d3es9ekt6m000', 31, '530px', '65px', NULL, '{\"url\": \"https://blog.csdn.net/community/home-api/v1/get-business-list\", \"method\": \"GET\", \"params\": \"page=1&size=5&businessType=blog&orderby=&noMore=false&year=&month=&username=Lin_xiaofeng\"}', NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('31roajzcjsm000', NULL, '3gsaefsrjvc000', NULL, NULL, NULL, NULL, NULL, NULL, '4grlw4cm5d6000', '4moatwj5x98000', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('325wnirydx6000', NULL, '4ufazyee92c000', NULL, NULL, NULL, NULL, NULL, NULL, '4wochfvng22000', '1fukchv3s31c00', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('32f3jhztrau000', '输出1', '10j5nc7cz5k000', 4, '80px', '370px', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('32f9ixt25ii000', '输出', 'xu7fsb4whw000', 4, '140px', '620px', '打印出上一节点传递的参数', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('33sh5115jig000', NULL, 'ihvivrh5j1k00', NULL, NULL, NULL, NULL, NULL, NULL, '58e16w5a8zo000', '5pntlgksw1s000', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('34rhcpn8azc000', '查询用户请求', '4z9vdqsd5kg000', 31, '495px', '255px', NULL, '{\"url\": \"/api/http/test/user\", \"method\": \"GET\", \"params\": \"name=test1\"}', NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('35aqj8dngws000', '输出', '2fnllj4jorfo00', 4, '440px', '460px', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('35dlfubp3vm000', '名称未传', '5czjn0rra74000', 5, '265px', '335px', NULL, '{\"field\": \"name\", \"value\": \"null\", \"condition\": \"=\"}', NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('35q6xnzvsbg000', 'HTTP响应', '5czjn0rra74000', 33, '335px', '795px', NULL, '{\"uri\": \"/api/http/test/user\", \"method\": \"PUT\", \"resData\": \"{\\\"result\\\": \\\"更新成功\\\"}\"}', NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('35zmdn1sy12000', 'HTTP响应', '5czjn0rra74000', 33, '140px', '490px', NULL, '{\"uri\": \"/api/http/test/user\", \"method\": \"POST\", \"resData\": \"{\\\"result\\\": \\\"新增失败！必传字段缺失\\\"}\"}', NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('36tf4sqel52000', 'HTTP响应', '5czjn0rra74000', 33, '80px', '795px', NULL, '{\"uri\": \"/api/http/test/user\", \"method\": \"POST\", \"resData\": \"{\\\"result\\\": \\\"新增成功\\\"}\"}', NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('38jbpyhliqe000', NULL, '5czjn0rra74000', NULL, NULL, NULL, NULL, NULL, NULL, '18sa1eux7gn400', '4gy8a8rjq90000', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('38xa79tc79g000', '开始', '1pdye9g349ls00', 1, '175px', '130px', NULL, NULL, '{\"name\": \"xxx\"}', NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('39axs0ei7cq000', 'HTTP请求', '2fnllj4jorfo00', 31, '335px', '260px', NULL, '{\"url\": \"https://blog.csdn.net/Lin_xiaofeng/article/details/126106706\", \"header\": \"Content-Type: text/html;charset=utf-8\", \"method\": \"GET\"}', NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('3a89na7i2ng000', NULL, 'nm8hriar4ds00', NULL, NULL, NULL, NULL, NULL, NULL, '997ksch8axg00', '4186ftn260o000', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('3aiwboej4io000', 'HTTP响应', 'ihvivrh5j1k00', 33, '310px', '450px', NULL, '{\"uri\": \"/api/http/test2\", \"method\": \"POST\", \"resData\": \"{\\\"result\\\":\\\"Hello ${name}\\\"}\"}', NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('3bwq386g87g000', '查询-输出', '4z9vdqsd5kg000', 4, '320px', '495px', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('3c28aipjefw000', '输出', '3f1qhocnot6000', 4, '210px', '495px', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('3dxtemcmplk000', NULL, 'm6o3aghqfrk00', NULL, NULL, NULL, NULL, NULL, NULL, '2ryt3dgbcxa000', 'qfki3k8xu4000', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('3dyid08c2ei000', '延时1秒', 'm6o3aghqfrk00', 3, '310px', '90px', NULL, '{\"delay\": \"1,SECONDS\"}', '{\"a\": \"${a}\"}', NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('3e4j6g0omua000', '输出', '3uldr6qij5o000', 4, '165px', '645px', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('3fk8iqt33l0000', '输出', '1pdye9g349ls00', 4, '265px', '325px', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('3fu8m0ttgiy000', NULL, 'mk3eo1ewkk000', NULL, NULL, NULL, NULL, NULL, NULL, '69mzkmq9x4g00', '48cfy4dsudg000', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('3g0gxjqohmg000', '解析器', '4d3es9ekt6m000', 21, '530px', '300px', NULL, '{\"parseKey\": \"httpResult.data.list\"}', NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('3g6glxdv31k000', '延时4秒', 'm6o3aghqfrk00', 3, '310px', '730px', NULL, '{\"delay\": \"4,SECONDS\"}', '{\"a\": \"${a}\"}', NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('3gk5d3qtw5o000', '输出', '1geaido0ecww00', 4, '280px', '545px', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('3glw49fmibm000', NULL, '4z9vdqsd5kg000', NULL, NULL, NULL, NULL, NULL, NULL, '5d4whpuzl5k000', '1uvffcb7y6v400', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('3jkj8klpbec000', '请求失败', 'nm8hriar4ds00', 5, '530px', '380px', NULL, '{\"field\": \"httpResult.code\", \"value\": \"200\", \"condition\": \"!=\"}', NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('3k37rvwkv9c000', NULL, 'm6o3aghqfrk00', NULL, NULL, NULL, NULL, NULL, NULL, '3le77waupza000', '1js10mqz518g00', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('3k8ghibbcwk000', NULL, '5czjn0rra74000', NULL, NULL, NULL, NULL, NULL, NULL, 'u13z5xhhdz400', '2rm699cuvh60', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('3k8oafcwmrc000', NULL, '5epeyaoy9ac000', NULL, NULL, NULL, NULL, NULL, NULL, '9756rhigrok00', '411ip98he9m000', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('3le77waupza000', '定时器2', 'm6o3aghqfrk00', 2, '95px', '730px', NULL, '{\"times\": \"1\", \"period\": \"3,SECONDS\", \"timestamp\": \"HH:mm:ss\"}', '{\"a\": \"xxx-4\"}', NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('3lwlmlke4o4000', NULL, '13mwrf5znqv400', NULL, NULL, NULL, NULL, NULL, NULL, '4obd502qfr0000', '27k4z0xei05c00', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('3m52bl5i5pc000', '开始-a', 'mk3eo1ewkk000', 1, '60px', '210px', NULL, NULL, '{\"a\": \"xxx\"}', NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('3m9uwgwewya000', NULL, 'ihvivrh5j1k00', NULL, NULL, NULL, NULL, NULL, NULL, '1uyde8bz0kn400', '2d8yal0m4oro00', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('3n04sv7dzl4000', '解析器', '1p8nhh3c4aio00', 21, '215px', '490px', NULL, '{\"parseKey\": \"httpResult.data.list.$0.title,httpResult.data.list.$0.description\"}', '{\"c\": \"${b}-c\"}', NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('3n47e8pov4k000', NULL, '34224hnftcm000', NULL, NULL, NULL, NULL, NULL, NULL, '51c692u0630000', '5erg81rz21s000', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('3o5fe4chv4i000', NULL, '3wccjaqq9y0000', NULL, NULL, NULL, NULL, NULL, NULL, '2rj98hbteum000', '2cxstxskz2sk00', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('3o635oa8gmw000', NULL, '5pubb7joxmc000', NULL, NULL, NULL, NULL, NULL, NULL, '5fvm2s3j5yw000', '308b6jzmpcy000', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('3ogjg21pojs000', NULL, '2fnllj4jorfo00', NULL, NULL, NULL, NULL, NULL, NULL, '39axs0ei7cq000', '4y7a2ybfkek000', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('3ohw4blopb4000', NULL, '1pdye9g349ls00', NULL, NULL, NULL, NULL, NULL, NULL, '3fk8iqt33l0000', '5ladpnlftek000', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('3oygkuykszk000', NULL, '1geaido0ecww00', NULL, NULL, NULL, NULL, NULL, NULL, '5lrcvc6z3l8000', '4k4fdwv2f7y000', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('3q2epx7o1ak000', NULL, '3wccjaqq9y0000', NULL, NULL, NULL, NULL, NULL, NULL, '2cxstxskz2sk00', 'sdhpa1o4x2o00', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('3qaytav7tx0000', 'HTTP请求', 'nm8hriar4ds00', 31, '460px', '215px', NULL, '{\"url\": \"https://blog.csdn.net/community/home-api/v1/get-business-list\", \"method\": \"GET\", \"params\": \"page=1&size=20&businessType=blog&orderby=&noMore=false&year=&month=&username=Lin_xiaofeng\"}', NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('3qr0stao6fs000', '输出', '2fnllj4jorfo00', 4, '220px', '325px', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('3s2pbcg8q28000', NULL, '1p8nhh3c4aio00', NULL, NULL, NULL, NULL, NULL, NULL, '5tlseeuot2k000', '3n04sv7dzl4000', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('3sssatqloli000', NULL, 'xu7fsb4whw000', NULL, NULL, NULL, NULL, NULL, NULL, '1wb2ubmere8w00', '32f9ixt25ii000', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('3su4fw2tj2o000', '输出', 'nm8hriar4ds00', 4, '260px', '555px', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('3sw73nv8cq2000', 'a=null', 'nm8hriar4ds00', 5, '340px', '215px', NULL, '{\"field\": \"params.a\", \"value\": \"null\", \"condition\": \"=\"}', NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('3tcfukszxmw000', '开始', '2fnllj4jorfo00', 1, '335px', '60px', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('3tvncu69hpm000', '查询-输出', '4z9vdqsd5kg000', 4, '155px', '495px', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('3u8e1n8jh8c000', NULL, 'mk3eo1ewkk000', NULL, NULL, NULL, NULL, NULL, NULL, '48cfy4dsudg000', '5pyn5hp1o44000', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('3wa09thtp2w000', '输出', '48r380g6qc4000', 4, '105px', '605px', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('3wgobbd6ol4000', 'MQTT发布', '3gsaefsrjvc000', 38, '240px', '320px', NULL, '{\"topic\": \"/mqtt/test/c\", \"broker\": \"tcp://broker.emqx.io,1883\", \"message\": \"c: ${timestamp}\", \"clientId\": \"mqttx_092ff908\"}', NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('3y3gd69qage000', NULL, '5czjn0rra74000', NULL, NULL, NULL, NULL, NULL, NULL, '5jw0u8ido0w000', 'zb214bmz4g000', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('3yml93th2ai000', NULL, 'ihvivrh5j1k00', NULL, NULL, NULL, NULL, NULL, NULL, '17lf5km6vn2800', '1wq99npn0scg00', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('3zs0yhk0r8q000', NULL, '10j5nc7cz5k000', NULL, NULL, NULL, NULL, NULL, NULL, '27s1wzggeb400', '2m9tp4kwxka000', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('3zvkwj9xxz6000', '发送邮件--公开环境中请勿留下授权码，建议自己新建流程测试', '395ecb7kvk8000', 39, '95px', '455px', NULL, '{\"text\": \"${html}\", \"isHtml\": \"HTML格式\", \"subject\": \"测试邮件-主题\"}', NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('411ip98he9m000', 'HTTP请求', '5epeyaoy9ac000', 31, '145px', '320px', NULL, '{\"url\": \"/${q}/test1\", \"method\": \"GET\", \"params\": \"name=张三\"}', '{\"p\": \"http请求test1\"}', NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('4186ftn260o000', 'a=xxx', 'nm8hriar4ds00', 5, '260px', '215px', NULL, '{\"field\": \"params.a\", \"value\": \"xxx\", \"condition\": \"=\"}', NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('41amqxi2fmg000', NULL, 'm6o3aghqfrk00', NULL, NULL, NULL, NULL, NULL, NULL, '1js10mqz518g00', '3g6glxdv31k000', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('41anb465v0g000', NULL, '2fnllj4jorfo00', NULL, NULL, NULL, NULL, NULL, NULL, '57zr71magh4000', '3qr0stao6fs000', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('43a35b7dfte000', '服务开始', '5czjn0rra74000', 1, '265px', '10px', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('43pjrbrrcbk000', 'HTTP请求', '5epeyaoy9ac000', 31, '295px', '320px', NULL, '{\"url\": \"/${q}/test2\", \"body\": \"{\\\"name\\\": \\\"李四\\\"}\", \"method\": \"POST\"}', '{\"p\": \"http请求test2\"}', NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('4491s2zd43s000', NULL, '5czjn0rra74000', NULL, NULL, NULL, NULL, NULL, NULL, 'zdm2xcamdds00', '35q6xnzvsbg000', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('44jgeaxixf8000', '开始', '4d3es9ekt6m000', 1, '380px', '65px', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('451mkbeyuaw00', '发送邮件--公开环境中请勿留下授权码，建议自己新建流程测试', '395ecb7kvk8000', 39, '285px', '455px', NULL, '{\"text\": \"${text}\", \"subject\": \"测试邮件-主题\"}', NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('45hir776ob2000', NULL, '5czjn0rra74000', NULL, NULL, NULL, NULL, NULL, NULL, '2xb46vc15ny000', 'm257yf0g9c000', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('45ho9eos0jk000', '开始', '28pm30nbgs2s00', 1, '120px', '155px', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('463pzntyc0u000', NULL, '3osi3b97pa4000', NULL, NULL, NULL, NULL, NULL, NULL, 'zzlzi607jv400', '5mefz3prh1s000', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('47jgknx0d78000', NULL, '2dzjjpdtijb400', NULL, NULL, NULL, NULL, NULL, NULL, '4xfrjiw9kkk000', '5r7cwz1vxk4000', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('47q6jh9bscm000', NULL, '1p8nhh3c4aio00', NULL, NULL, NULL, NULL, NULL, NULL, '3n04sv7dzl4000', '4pexljq4hlk000', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('486521g7uns000', NULL, 'mk3eo1ewkk000', NULL, NULL, NULL, NULL, NULL, NULL, '5pyn5hp1o44000', '4cplvt9y7wy000', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('48cfy4dsudg000', '输出-b', 'mk3eo1ewkk000', 4, '175px', '425px', NULL, NULL, '{\"c\": \"${b}\"}', NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('498r7jvg1xq000', 'id未传', '5czjn0rra74000', 5, '550px', '335px', NULL, '{\"field\": \"id\", \"value\": \"null\", \"condition\": \"=\"}', NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('49x980z8iw8000', NULL, '5czjn0rra74000', NULL, NULL, NULL, NULL, NULL, NULL, '21qtdsmw9vds00', '1acrzdqrg8g000', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('4c8mzac4do6000', '定时器', '3gsaefsrjvc000', 2, '55px', '50px', NULL, '{\"times\": \"2\", \"period\": \"5,SECONDS\", \"timestamp\": \"HH:mm:ss\"}', NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('4ccnzr06zuo000', '开始', '48r380g6qc4000', 1, '80px', '200px', NULL, NULL, '{\"a\": \"xxx-a\", \"b\": \"xxx-b\"}', NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('4cplvt9y7wy000', '输出d', 'mk3eo1ewkk000', 4, '60px', '650px', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('4e5bd1apnuw000', NULL, '5czjn0rra74000', NULL, NULL, NULL, NULL, NULL, NULL, '4gy8a8rjq90000', '1vrlpkfsd0xs00', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('4f6s4osxhts000', '输出', 'ihvivrh5j1k00', 4, '310px', '655px', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('4fufoo306fe000', '输出', '2dzjjpdtijb400', 4, '160px', '645px', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('4gpcx6xl5gk000', '开始', '2fnllj4jorfo00', 1, '120px', '60px', NULL, NULL, '{\"html\": \"<!DOCTYPE html>\\n<html lang=\\\"en\\\">\\n  \\n  <head>\\n    <meta charset=\\\"UTF-8\\\">\\n    <meta http-equiv=\\\"X-UA-Compatible\\\" content=\\\"IE=edge\\\">\\n    <meta name=\\\"viewport\\\" content=\\\"width=device-width, initial-scale=1.0\\\">\\n    <title>Document</title>\\n    <style>h1 , h5{ text-align: center; } div { text-align: center; }</style>\\n  </head>\\n  \\n  <body>\\n    <h1>\\n      <p id=\\\"p1\\\">《青溪》</p>\\n    </h1>\\n    <h5>\\n      <p id=\\\"p2\\\">作者：王维</p>\\n    </h5>\\n    <div>\\n      <p class=\\\"p3\\\">言入黄花川，每逐青溪水。</p>\\n      <p class=\\\"p3\\\">随山将万转，趣途无百里。</p>\\n      <p class=\\\"p3\\\">声喧乱石中，色静深松里。</p>\\n      <p class=\\\"p3\\\">漾漾泛菱荇，澄澄映葭苇。</p>\\n      <p class=\\\"p3\\\">我心素已闲，清川澹如此。</p>\\n      <p class=\\\"p3\\\">请留盘石上，垂钓将已矣。</p>\\n    </div>\\n  </body>\\n\\n</html>\"}', NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('4gqkkz1h9ey000', NULL, '13mwrf5znqv400', NULL, NULL, NULL, NULL, NULL, NULL, 'y8kabypgz7k00', '4obd502qfr0000', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('4grlw4cm5d6000', 'MQTT发布', '3gsaefsrjvc000', 38, '145px', '320px', NULL, '{\"topic\": \"/mqtt/test/b\", \"broker\": \"tcp://broker.emqx.io,1883\", \"message\": \"b: ${timestamp}\", \"clientId\": \"mqttx_feeb5235\"}', NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('4gy8a8rjq90000', '查询数据', '5czjn0rra74000', 51, '200px', '490px', NULL, '{\"sql\": \"SELECT * FROM t_user WHERE `name`=\'${name}\';\", \"url\": \"jdbc:mysql://127.0.0.1:3306/flow_eda_test\", \"password\": \"123456\", \"username\": \"root\"}', NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('4hq5bc15yy0000', '输出', '34224hnftcm000', 4, '150px', '590px', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('4i0ng2xc5oc000', '开始', '1geaido0ecww00', 1, '95px', '80px', NULL, NULL, '{\"xml\": \"<?xml version=\'1.0\' encoding=\'UTF-8\'?><books><book id=\'1\'><name>程序设计</name><info>从入门到精通，带你玩转java编程</info></book><book id=\'2\'><name>架构设计</name><info>从零开始，手把手教你设计大型网站架构</info></book></books>\"}', NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('4i2kawup4v4000', '输出', '5pubb7joxmc000', 4, '255px', '405px', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('4in34ayw2n0000', NULL, '5czjn0rra74000', NULL, NULL, NULL, NULL, NULL, NULL, '35q6xnzvsbg000', '2svs614ygns000', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('4j21avbh94m000', NULL, 'ihvivrh5j1k00', NULL, NULL, NULL, NULL, NULL, NULL, '4w26nl86ee0000', '3aiwboej4io000', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('4jh6xqzb6ws000', NULL, '5czjn0rra74000', NULL, NULL, NULL, NULL, NULL, NULL, '2rm699cuvh60', '21qtdsmw9vds00', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('4jqa57104lm000', NULL, '5czjn0rra74000', NULL, NULL, NULL, NULL, NULL, NULL, '2ps0q19lj9c000', 'zdm2xcamdds00', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('4k4fdwv2f7y000', '输出', '1geaido0ecww00', 4, '95px', '545px', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('4k5stjubriu000', NULL, 'nm8hriar4ds00', NULL, NULL, NULL, NULL, NULL, NULL, '5cql4jfpft4000', '3su4fw2tj2o000', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('4kldprey2pa000', NULL, '5pubb7joxmc000', NULL, NULL, NULL, NULL, NULL, NULL, '308b6jzmpcy000', '4i2kawup4v4000', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('4l42m65xah4000', '输出', '1pdye9g349ls00', 4, '370px', '520px', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('4lnyjpqmn0q000', NULL, '6a088jl98eg00', NULL, NULL, NULL, NULL, NULL, NULL, '2eqcm8iq2mm800', '1vym9mvg117k00', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('4moatwj5x98000', '输出', '3gsaefsrjvc000', 4, '145px', '520px', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('4nc3gc5i8zy000', '子输入', '1pdye9g349ls00', 101, '175px', '325px', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('4nhpgowo3mg000', '延时器', '3uldr6qij5o000', 3, '60px', '435px', '当流程发生异常时，整个流程会中止运行，节点会停滞在当前状态', '{\"delay\": \"8,SECONDS\"}', NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('4nkaxp68wjk000', NULL, '5czjn0rra74000', NULL, NULL, NULL, NULL, NULL, NULL, '16q07apqfi7400', '2bogsv077ls000', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('4o5c49zbtx8000', '校验名称', '5czjn0rra74000', 5, '20px', '335px', NULL, '{\"field\": \"name\", \"value\": \"null\", \"condition\": \"!=\"}', NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('4o8boyqnhsm000', NULL, '4z9vdqsd5kg000', NULL, NULL, NULL, NULL, NULL, NULL, '55g0z7vdyrk000', '2tzqefwvm4g000', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('4obd502qfr0000', 'MQTT订阅', '13mwrf5znqv400', 37, '210px', '210px', NULL, '{\"topic\": \"/mqtt/test/+\", \"broker\": \"tcp://broker.emqx.io,1883\", \"clientId\": \"mqttx_7e232df2\"}', NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('4ocf9scbqpe000', NULL, '4d3es9ekt6m000', NULL, NULL, NULL, NULL, NULL, NULL, '3g0gxjqohmg000', '17zy5yhhmn3400', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('4p32ry074ni000', NULL, '5czjn0rra74000', NULL, NULL, NULL, NULL, NULL, NULL, '1vrlpkfsd0xs00', '5suu5vrlsrk000', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('4pexljq4hlk000', '输出', '1p8nhh3c4aio00', 4, '110px', '555px', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('4q46w3pes0m000', '输出', '18rwqz520iio00', 4, '125px', '410px', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('4qhafzihvwo000', NULL, '48r380g6qc4000', NULL, NULL, NULL, NULL, NULL, NULL, 'wvzmjwfdtuo00', '3wa09thtp2w000', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('4r24k4t2vpu000', NULL, '5czjn0rra74000', NULL, NULL, NULL, NULL, NULL, NULL, '1xz6zrozzo6800', '2hgaqx0klnq000', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('4rqhngznbfg000', 'b=null', 'nm8hriar4ds00', 5, '80px', '380px', NULL, '{\"field\": \"params.b\", \"value\": \"null\", \"condition\": \"=\"}', NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('4st4lxjwle0000', NULL, 'm6o3aghqfrk00', NULL, NULL, NULL, NULL, NULL, NULL, '4yj8cf9mgxg000', '29cput2xohwk00', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('4tnr4rsnzdw000', 'HTTP请求', '3uldr6qij5o000', 31, '285px', '435px', NULL, '{\"url\": \"http://localhost:8081/test\", \"method\": \"GET\"}', NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('4ujp6bqblq0000', 'a=xxx', 'nm8hriar4ds00', 5, '80px', '215px', NULL, '{\"field\": \"params.a\", \"value\": \"xxx\", \"condition\": \"=\"}', NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('4v1wf1va5w6000', NULL, '48r380g6qc4000', NULL, NULL, NULL, NULL, NULL, NULL, '4ccnzr06zuo000', 'wvzmjwfdtuo00', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('4v2svtiak90000', NULL, '3f1qhocnot6000', NULL, NULL, NULL, NULL, NULL, NULL, '1rxk06czqjy800', '3c28aipjefw000', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('4vi9dek637c000', NULL, '4z9vdqsd5kg000', NULL, NULL, NULL, NULL, NULL, NULL, '1uvffcb7y6v400', '183um31kt4ow00', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('4vmfwkfwm32000', '输出', '3f1qhocnot6000', 4, '85px', '495px', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('4w26nl86ee0000', '解析器', 'ihvivrh5j1k00', 21, '425px', '450px', NULL, '{\"parseKey\": \"name\"}', NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('4wochfvng22000', '开始', '4ufazyee92c000', 1, '140px', '130px', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('4xfrjiw9kkk000', '定时器', '2dzjjpdtijb400', 2, '65px', '190px', NULL, '{\"times\": \"3\", \"period\": \"3,SECONDS\", \"timestamp\": \"HH:mm:ss\"}', '{\"q\": \"api/v1\"}', NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('4xq0bbt4dj4000', NULL, 'nm8hriar4ds00', NULL, NULL, NULL, NULL, NULL, NULL, '997ksch8axg00', '3sw73nv8cq2000', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('4y7a2ybfkek000', '输出', '2fnllj4jorfo00', 4, '335px', '460px', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('4yj8cf9mgxg000', '定时器1', 'm6o3aghqfrk00', 2, '95px', '505px', NULL, '{\"times\": \"1\", \"period\": \"2,SECONDS\", \"timestamp\": \"HH:mm:ss\"}', '{\"a\": \"xxx-3\"}', NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('4zb06q8dimw000', NULL, '395ecb7kvk8000', NULL, NULL, NULL, NULL, NULL, NULL, '1kkfxrxf1ri800', '451mkbeyuaw00', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('4zbxf9h29x0000', NULL, '5czjn0rra74000', NULL, NULL, NULL, NULL, NULL, NULL, '2xb46vc15ny000', '117er0nxdy3k00', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('50cyo8btzls000', NULL, '2dzjjpdtijb400', NULL, NULL, NULL, NULL, NULL, NULL, '5t6dx3m3ras000', '4fufoo306fe000', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('50jx0cgrceo000', NULL, '2dzjjpdtijb400', NULL, NULL, NULL, NULL, NULL, NULL, 'lxwmugqrwlc00', '2rm1j96dqw2000', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('50t5mnhcrt0000', NULL, 'ihvivrh5j1k00', NULL, NULL, NULL, NULL, NULL, NULL, 'csqjv8qyuhs00', '17lf5km6vn2800', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('50tl9rn2u0o000', 'Redis', '3f1qhocnot6000', 53, '85px', '305px', NULL, '{\"uri\": \"127.0.0.1:6379\", \"args\": \"test,Hello World! \", \"method\": \"set\"}', NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('51c692u0630000', '开始', '34224hnftcm000', 1, '100px', '160px', NULL, NULL, '{\"a\": \"xxx\"}', NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('51ghbw0d2pg000', 'MongoDB', '28pm30nbgs2s00', 52, '205px', '280px', NULL, '{\"db\": \"test_db\", \"url\": \"mongodb://root:admin@127.0.0.1:27017/admin\", \"command\": \"{\'find\': \'test_coll\', \'filter\': {\'name\': \'test\'}, \'limit\': 2}\"}', NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('52y7k5m4788000', '总输出', 'm6o3aghqfrk00', 4, '515px', '405px', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('534q7kvhge8000', NULL, '5czjn0rra74000', NULL, NULL, NULL, NULL, NULL, NULL, '2hgaqx0klnq000', '552o5n26x3s000', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('53lszmnskwc000', '开始', 'rjj5vbam81s00', 1, '210px', '135px', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('53v0rlwo1ac000', NULL, '3gsaefsrjvc000', NULL, NULL, NULL, NULL, NULL, NULL, '25oxq4xoglq800', '4grlw4cm5d6000', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('54cqiur37zo000', '开始', '41as9fmikt8000', 1, '115px', '195px', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('552o5n26x3s000', '删除成功', '5czjn0rra74000', 5, '480px', '645px', NULL, '{\"field\": \"result\", \"value\": \"Affected rows: 1\", \"condition\": \"=\"}', NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('556r0guzpi0000', NULL, '5czjn0rra74000', NULL, NULL, NULL, NULL, NULL, NULL, '552o5n26x3s000', '1pzzfeot9s9s00', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('559mc8p3pq0000', '输出', '5pubb7joxmc000', 4, '145px', '405px', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('55g0z7vdyrk000', '新增用户请求', '4z9vdqsd5kg000', 31, '60px', '255px', NULL, '{\"url\": \"/api/http/test/user\", \"body\": \"{\\\"name\\\":\\\"test1\\\", \\\"phone\\\":\\\"13312341234\\\"}\", \"method\": \"POST\", \"params\": \"remark=备注\"}', NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('55u9e20jg7o000', NULL, '3f1qhocnot6000', NULL, NULL, NULL, NULL, NULL, NULL, '50tl9rn2u0o000', '4vmfwkfwm32000', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('57zr71magh4000', 'HTML解析', '2fnllj4jorfo00', 22, '220px', '140px', NULL, '{\"parseKey\": \"params.html\", \"selector\": \"p,tag\"}', NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('58diu037oeo000', NULL, '5czjn0rra74000', NULL, NULL, NULL, NULL, NULL, NULL, '43a35b7dfte000', '1whclji69jr400', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('58e16w5a8zo000', '开始', 'ihvivrh5j1k00', 1, '85px', '65px', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('59a40yecno8000', NULL, '4d3es9ekt6m000', NULL, NULL, NULL, NULL, NULL, NULL, '3g0gxjqohmg000', '5jzeddw7wds000', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('59tktp4osh8000', '更新用户请求', '4z9vdqsd5kg000', 31, '240px', '255px', NULL, '{\"url\": \"/api/http/test/user\", \"body\": \"{\\\"name\\\":\\\"testxxx\\\"}\", \"method\": \"PUT\", \"params\": \"id=${httpResult.result.id}\"}', NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('59xe5h6n234000', '输出', '395ecb7kvk8000', 4, '195px', '210px', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('5a2dlel7y1s000', NULL, 'ihvivrh5j1k00', NULL, NULL, NULL, NULL, NULL, NULL, '1wq99npn0scg00', '4w26nl86ee0000', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('5aamavmwhb4000', '输出2', '10j5nc7cz5k000', 4, '220px', '370px', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('5bhkyaxsw7o000', '延时器', '41as9fmikt8000', 3, '240px', '280px', '中止运行后，鼠标悬停在红叉图标上可查看流程中止原因', '{\"delay\": \"10,SECONDS\"}', '{\"a\": \"xxx\"}', NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('5boelqzz2j4000', '插入成功', '5czjn0rra74000', 5, '20px', '795px', NULL, '{\"field\": \"result\", \"value\": \"Affected rows: 1\", \"condition\": \"=\"}', NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('5by08g691os000', '输出', 'rjj5vbam81s00', 4, '200px', '505px', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('5bzdqxyeypw000', '输出', '5epeyaoy9ac000', 4, '145px', '560px', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('5cd592qljf4000', '输出', '5epeyaoy9ac000', 4, '295px', '560px', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('5chegqfj09w000', 'XML解析', '1geaido0ecww00', 23, '400px', '320px', NULL, '{\"parseKey\": \"httpResult.xml\"}', NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('5cql4jfpft4000', 'b>1', 'nm8hriar4ds00', 5, '260px', '380px', NULL, '{\"field\": \"params.b\", \"value\": \"1\", \"condition\": \">\"}', NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('5d4whpuzl5k000', '查询用户请求', '4z9vdqsd5kg000', 31, '320px', '255px', NULL, '{\"url\": \"/api/http/test/user\", \"method\": \"GET\", \"params\": \"name=testxxx\"}', NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('5d5xuu2bek4000', NULL, '4d3es9ekt6m000', NULL, NULL, NULL, NULL, NULL, NULL, '30b3ekkx5f4000', 't3126jimym800', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('5d88135ki9o000', NULL, '3gsaefsrjvc000', NULL, NULL, NULL, NULL, NULL, NULL, '5ugkbmc5hmo000', '3wgobbd6ol4000', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('5dljgxw72bk000', 'HTML解析', '2fnllj4jorfo00', 22, '440px', '260px', NULL, '{\"parseKey\": \"httpResult.html\", \"selector\": \"title-article,class\"}', NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('5e2g29k15x0000', NULL, '5czjn0rra74000', NULL, NULL, NULL, NULL, NULL, NULL, '35zmdn1sy12000', '1aqv4dp478n400', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('5ecbdq2qilw000', NULL, '2fnllj4jorfo00', NULL, NULL, NULL, NULL, NULL, NULL, '5dljgxw72bk000', '35aqj8dngws000', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('5erg81rz21s000', 'HTTP请求', '34224hnftcm000', 31, '195px', '350px', NULL, '{\"url\": \"https://blog.csdn.net/community/home-api/v1/get-business-list\", \"header\": \"Accept:application/json\", \"method\": \"GET\", \"params\": \"page=1&size=20&businessType=blog&orderby=&noMore=false&year=&month=&username=Lin_xiaofeng\"}', '{\"b\": \"${a}-b\"}', NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('5fpd25a8r98000', NULL, 'nm8hriar4ds00', NULL, NULL, NULL, NULL, NULL, NULL, '2ojyy2yxe6g000', '290zgf5i43r400', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('5fvm2s3j5yw000', '开始', '5pubb7joxmc000', 1, '145px', '75px', NULL, NULL, '{\"name\": \"test-sub\"}', NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('5grdto48m4w000', NULL, '41as9fmikt8000', NULL, NULL, NULL, NULL, NULL, NULL, '54cqiur37zo000', '5bhkyaxsw7o000', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('5guhby5zjh4000', NULL, '1geaido0ecww00', NULL, NULL, NULL, NULL, NULL, NULL, 'q3gss728upc00', '5chegqfj09w000', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('5gy6u69jvs4000', '开始', '1geaido0ecww00', 1, '280px', '80px', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('5hnp0jfza90000', NULL, '3uldr6qij5o000', NULL, NULL, NULL, NULL, NULL, NULL, '4nhpgowo3mg000', '2bk45mph7xno00', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('5i8x7z1xkfs000', NULL, '4z9vdqsd5kg000', NULL, NULL, NULL, NULL, NULL, NULL, '76jspatw86800', '3tvncu69hpm000', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('5ipjxxvv0co000', '输出', '2fnllj4jorfo00', 4, '120px', '325px', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('5jrc3awcj5k000', NULL, '4z9vdqsd5kg000', NULL, NULL, NULL, NULL, NULL, NULL, '5d4whpuzl5k000', '3bwq386g87g000', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('5jw0u8ido0w000', '校验电话', '5czjn0rra74000', 5, '20px', '490px', NULL, '{\"field\": \"phone\", \"value\": \"null\", \"condition\": \"!=\"}', NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('5jzeddw7wds000', '输出', '4d3es9ekt6m000', 4, '380px', '300px', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('5k68wmw57hg000', '异常输出', '5czjn0rra74000', 4, '550px', '645px', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('5ladpnlftek000', '子输出', '1pdye9g349ls00', 102, '265px', '520px', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('5lga1m2chpc000', '成功输出', '5czjn0rra74000', 4, '140px', '795px', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('5lk2ltlk8og000', NULL, '3wccjaqq9y0000', NULL, NULL, NULL, NULL, NULL, NULL, 'dyn9zobl34w00', '2rj98hbteum000', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('5lr4tno5a7w000', NULL, 'nm8hriar4ds00', NULL, NULL, NULL, NULL, NULL, NULL, '3qaytav7tx0000', '2ojyy2yxe6g000', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('5lrcvc6z3l8000', 'XML解析', '1geaido0ecww00', 23, '95px', '320px', NULL, '{\"parseKey\": \"params.xml\"}', NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('5mcrx13q9xs000', NULL, '4z9vdqsd5kg000', NULL, NULL, NULL, NULL, NULL, NULL, '59tktp4osh8000', '2wyvgvfidvy000', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('5mefz3prh1s000', '输出', '3osi3b97pa4000', 4, '205px', '620px', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('5mh4yxk6tzo000', NULL, '4d3es9ekt6m000', NULL, NULL, NULL, NULL, NULL, NULL, '3151fnzygru000', '3g0gxjqohmg000', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('5namxuh4jto000', NULL, '4d3es9ekt6m000', NULL, NULL, NULL, NULL, NULL, NULL, '30b3ekkx5f4000', '1hljr228155s00', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('5on32kiaf9k000', NULL, 'nm8hriar4ds00', NULL, NULL, NULL, NULL, NULL, NULL, '6370qq93ees00', '3qaytav7tx0000', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('5p0mz9na1ss000', NULL, '4ufazyee92c000', NULL, NULL, NULL, NULL, NULL, NULL, '1fukchv3s31c00', '25drumgr0eio00', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('5plrpukibww000', 'WS客户端1', '10j5nc7cz5k000', 35, '80px', '185px', NULL, '{\"path\": \"/w/test?name=lin\", \"sendAfterConnect\": \"hello server! I am lin.\"}', NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('5pntlgksw1s000', 'HTTP接收', 'ihvivrh5j1k00', 32, '85px', '265px', NULL, '{\"uri\": \"/api/http/test1\", \"method\": \"GET\"}', NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('5puoz4vwlb4000', NULL, '5czjn0rra74000', NULL, NULL, NULL, NULL, NULL, NULL, '2brn0wxuo0is00', '5k68wmw57hg000', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('5pxuhe3ufy8000', '延时3秒', 'm6o3aghqfrk00', 3, '310px', '505px', NULL, '{\"delay\": \"3,SECONDS\"}', '{\"a\": \"${a}\"}', NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('5pyn5hp1o44000', '输出-c', 'mk3eo1ewkk000', 4, '175px', '650px', NULL, NULL, '{\"d\": \"${c}\"}', NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('5q4zicl8o9g000', NULL, 'm6o3aghqfrk00', NULL, NULL, NULL, NULL, NULL, NULL, '5pxuhe3ufy8000', '52y7k5m4788000', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('5qguok13ub4000', NULL, '5czjn0rra74000', NULL, NULL, NULL, NULL, NULL, NULL, '36tf4sqel52000', '5lga1m2chpc000', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('5r43xrlgtsc000', NULL, '3gsaefsrjvc000', NULL, NULL, NULL, NULL, NULL, NULL, '4c8mzac4do6000', '18fl3883e75s00', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('5r7cwz1vxk4000', 'HTTP请求', '2dzjjpdtijb400', 31, '160px', '190px', NULL, '{\"url\": \"https://blog.csdn.net/community/home-${q}/get-business-list\", \"header\": \"Accept:application/json\", \"method\": \"GET\", \"params\": \"page=1&size=20&businessType=blog&orderby=&noMore=false&year=&month=&username=Lin_xiaofeng\"}', '{\"timestamp\": \"${timestamp}\"}', NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('5rb9xhczuk0000', '输出', 'nm8hriar4ds00', 4, '340px', '45px', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('5sfvkcmh4a0000', 'WS服务端', '18rwqz520iio00', 34, '205px', '235px', NULL, '{\"path\": \"/w/test\", \"query\": \"name\", \"fanout\": \"发送\", \"sendAfterConnect\": \"hello ${name}!\"}', NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('5suu5vrlsrk000', 'HTTP响应', '5czjn0rra74000', 33, '200px', '795px', NULL, '{\"uri\": \"/api/http/test/user\", \"method\": \"GET\"}', NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('5t6dx3m3ras000', '解析器', '2dzjjpdtijb400', 21, '160px', '420px', NULL, '{\"parseKey\": \"httpResult.data.list.$0.title\"}', '{\"timestamp\": \"${timestamp}\"}', NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('5tlseeuot2k000', 'HTTP请求', '1p8nhh3c4aio00', 31, '215px', '305px', NULL, '{\"url\": \"https://blog.csdn.net/community/home-${q}/get-business-list\", \"header\": \"Accept:application/json\", \"method\": \"GET\", \"params\": \"page=1&size=20&businessType=blog&orderby=&noMore=false&year=&month=&username=Lin_xiaofeng\"}', '{\"b\": \"${a}-b\"}', NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('5u9p54sev5w000', NULL, '10j5nc7cz5k000', NULL, NULL, NULL, NULL, NULL, NULL, '27s1wzggeb400', '5plrpukibww000', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('5ugkbmc5hmo000', '延时2秒', '3gsaefsrjvc000', 3, '240px', '50px', NULL, '{\"delay\": \"2,SECONDS\"}', '{\"timestamp\": \"${timestamp}\"}', NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('6370qq93ees00', '开始', 'nm8hriar4ds00', 1, '460px', '45px', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('69mzkmq9x4g00', '输出a', 'mk3eo1ewkk000', 4, '175px', '210px', NULL, NULL, '{\"b\": \"${a}\"}', NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('76jspatw86800', '查询用户请求', '4z9vdqsd5kg000', 31, '155px', '255px', NULL, '{\"url\": \"/api/http/test/user\", \"method\": \"GET\", \"params\": \"name=test1\"}', NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('7yp0zl9m2r000', NULL, '5pubb7joxmc000', NULL, NULL, NULL, NULL, NULL, NULL, '5fvm2s3j5yw000', '559mc8p3pq0000', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('7zztidwrrww00', NULL, '4z9vdqsd5kg000', NULL, NULL, NULL, NULL, NULL, NULL, '34rhcpn8azc000', '2fwt1w3fxa1w00', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('87tr2y5pga000', NULL, '1p8nhh3c4aio00', NULL, NULL, NULL, NULL, NULL, NULL, '2omkofzgbi8000', '5tlseeuot2k000', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('8qdohfc7y6000', NULL, '2fnllj4jorfo00', NULL, NULL, NULL, NULL, NULL, NULL, '4gpcx6xl5gk000', '57zr71magh4000', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('9756rhigrok00', '开始', '5epeyaoy9ac000', 1, '145px', '65px', NULL, NULL, '{\"q\": \"api/http\"}', NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('997ksch8axg00', '开始', 'nm8hriar4ds00', 1, '260px', '45px', NULL, NULL, '{\"a\": \"xxx\", \"b\": 3}', NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('a9or85u1ygs00', NULL, '1pdye9g349ls00', NULL, NULL, NULL, NULL, NULL, NULL, '38xa79tc79g000', '4nc3gc5i8zy000', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('ah97irtcqu800', NULL, '18rwqz520iio00', NULL, NULL, NULL, NULL, NULL, NULL, '2j6e6bbqmnm00', '5sfvkcmh4a0000', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('b1riso164kc00', NULL, '5czjn0rra74000', NULL, NULL, NULL, NULL, NULL, NULL, '1dfxnb4vcxfk00', '1xz6zrozzo6800', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('bgid30y68q000', '输出', '41as9fmikt8000', 4, '240px', '500px', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('brjl0erogio00', '开始', '3osi3b97pa4000', 1, '60px', '240px', NULL, NULL, '{\"a\": \"xxx-a\"}', NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('csqjv8qyuhs00', '开始', 'ihvivrh5j1k00', 1, '310px', '65px', NULL, NULL, '{\"q\": \"api/http\"}', NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('d1ybuhnxfz400', '输出', '2dzjjpdtijb400', 4, '65px', '420px', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('dyn9zobl34w00', '开始', '3wccjaqq9y0000', 1, '160px', '60px', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('ftcvzew502o00', NULL, 'm6o3aghqfrk00', NULL, NULL, NULL, NULL, NULL, NULL, '3dyid08c2ei000', '52y7k5m4788000', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('g8ihojph78000', '输出', '2dzjjpdtijb400', 4, '250px', '190px', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('gseue0fgd4g00', NULL, '395ecb7kvk8000', NULL, NULL, NULL, NULL, NULL, NULL, '1kkfxrxf1ri800', '1zv25by68fwg00', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('h0xtw26xuiw00', NULL, '2fnllj4jorfo00', NULL, NULL, NULL, NULL, NULL, NULL, '39axs0ei7cq000', '5dljgxw72bk000', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('hki9e5msxwo00', NULL, '5czjn0rra74000', NULL, NULL, NULL, NULL, NULL, NULL, '5suu5vrlsrk000', '1qgmqxt2lzy800', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('k4xdjcfntbk00', '输出', '1pdye9g349ls00', 4, '265px', '130px', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('lert2wiuqvk00', NULL, 'ihvivrh5j1k00', NULL, NULL, NULL, NULL, NULL, NULL, '5pntlgksw1s000', '2c9uhbkimbb400', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('lgf469nnkcg00', NULL, '2fnllj4jorfo00', NULL, NULL, NULL, NULL, NULL, NULL, '3tcfukszxmw000', '39axs0ei7cq000', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('lxwmugqrwlc00', '延时器', '2dzjjpdtijb400', 3, '250px', '420px', NULL, '{\"delay\": \"2,SECONDS\"}', '{\"result\": \"${httpResult.data.list.$0.title}\", \"timestamp\": \"${timestamp}\"}', NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('m257yf0g9c000', '电话未传', '5czjn0rra74000', 5, '140px', '335px', NULL, '{\"field\": \"phone\", \"value\": \"null\", \"condition\": \"=\"}', NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('miy96f7b80000', NULL, '5czjn0rra74000', NULL, NULL, NULL, NULL, NULL, NULL, '43a35b7dfte000', '1dfxnb4vcxfk00', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('opwqhh9o48000', NULL, '5czjn0rra74000', NULL, NULL, NULL, NULL, NULL, NULL, '2xb46vc15ny000', '4o5c49zbtx8000', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('pjffvx258ww00', NULL, '3uldr6qij5o000', NULL, NULL, NULL, NULL, NULL, NULL, '4tnr4rsnzdw000', '1n5cxdr0tmdc00', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('pw9o7doar4w00', NULL, '1pdye9g349ls00', NULL, NULL, NULL, NULL, NULL, NULL, '38xa79tc79g000', 'k4xdjcfntbk00', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('q0azp1wqxn400', NULL, 'nm8hriar4ds00', NULL, NULL, NULL, NULL, NULL, NULL, '3sw73nv8cq2000', '5cql4jfpft4000', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('q3gss728upc00', 'HTTP请求', '1geaido0ecww00', 31, '280px', '320px', NULL, '{\"url\": \"https://maven.apache.org/xsd/remote-resources-1.1.0.xsd\", \"method\": \"GET\"}', NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('qfki3k8xu4000', '延时2秒', 'm6o3aghqfrk00', 3, '310px', '295px', NULL, '{\"delay\": \"2,SECONDS\"}', '{\"a\": \"${a}\"}', NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('sdhpa1o4x2o00', '输出', '3wccjaqq9y0000', 4, '300px', '495px', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('t3126jimym800', '分割', '4d3es9ekt6m000', 8, '175px', '300px', NULL, '{\"field\": \"params.data\", \"outputWay\": \"多次输出单值\"}', NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('tfp7f13eolc00', NULL, '3uldr6qij5o000', NULL, NULL, NULL, NULL, NULL, NULL, '1rdi0c65pyhs00', '3e4j6g0omua000', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('twl53yv6zmo00', NULL, '5czjn0rra74000', NULL, NULL, NULL, NULL, NULL, NULL, '43a35b7dfte000', 'u13z5xhhdz400', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('u13z5xhhdz400', '更新用户', '5czjn0rra74000', 32, '335px', '170px', NULL, '{\"uri\": \"/api/http/test/user\", \"method\": \"PUT\"}', NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('u2sfezbryog00', NULL, '4z9vdqsd5kg000', NULL, NULL, NULL, NULL, NULL, NULL, '13baatlzz27400', '55g0z7vdyrk000', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('w1dt0ng4o7400', NULL, '2dzjjpdtijb400', NULL, NULL, NULL, NULL, NULL, NULL, '4xfrjiw9kkk000', 'd1ybuhnxfz400', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('wvzmjwfdtuo00', '解析器', '48r380g6qc4000', 21, '165px', '395px', '解析上一节点的参数并传递至下一节点', '{\"parseKey\": \"params.a\"}', '{\"p\": \"${b}\"}', NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('x7ag6qo8bpc00', NULL, 'm6o3aghqfrk00', NULL, NULL, NULL, NULL, NULL, NULL, '1g5eivmtkbi800', '1f2mfwal1o2o00', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('xdh6en0hq4w00', NULL, '395ecb7kvk8000', NULL, NULL, NULL, NULL, NULL, NULL, '1dslfud1adxc00', '59xe5h6n234000', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('xpbbl9krtxc00', NULL, '5czjn0rra74000', NULL, NULL, NULL, NULL, NULL, NULL, 'zb214bmz4g000', '5boelqzz2j4000', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('y8kabypgz7k00', '开始', '13mwrf5znqv400', 1, '120px', '55px', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('ysurpdcrvw000', NULL, '5czjn0rra74000', NULL, NULL, NULL, NULL, NULL, NULL, 'm257yf0g9c000', '35zmdn1sy12000', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('zb214bmz4g000', '插入数据', '5czjn0rra74000', 51, '20px', '645px', NULL, '{\"sql\": \"INSERT INTO t_user(`name`, phone, remark, create_date, update_date) VALUES(\'${name}\', \'${phone}\', \'${remark}\', NOW(), NOW());\", \"url\": \"jdbc:mysql://127.0.0.1:3306/flow_eda_test\", \"password\": \"123456\", \"username\": \"root\"}', NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('zdm2xcamdds00', '更新成功', '5czjn0rra74000', 5, '335px', '645px', NULL, '{\"field\": \"result\", \"value\": \"Affected rows: 1\", \"condition\": \"=\"}', NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('zffco5t1e9c00', '输出', 'nm8hriar4ds00', 4, '160px', '45px', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('zifq0ffb5mo00', NULL, '3f1qhocnot6000', NULL, NULL, NULL, NULL, NULL, NULL, '26qf0znlzpk000', '50tl9rn2u0o000', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('zqjgh7hqd8000', NULL, '5czjn0rra74000', NULL, NULL, NULL, NULL, NULL, NULL, '35dlfubp3vm000', '16q07apqfi7400', NULL);
INSERT INTO `eda_flow_node_data` VALUES ('zzlzi607jv400', '延时器', '3osi3b97pa4000', 3, '145px', '415px', NULL, '{\"delay\": \"3,SECONDS\"}', '{\"b\": \"${a}\"}', NULL, NULL, NULL);

-- ----------------------------
-- Table structure for eda_flow_node_type
-- ----------------------------
DROP TABLE IF EXISTS `eda_flow_node_type`;
CREATE TABLE `eda_flow_node_type`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `type` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '节点类型',
  `type_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '名称',
  `menu` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '节点菜单分类',
  `svg` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '图标',
  `background` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '背景色',
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 103 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of eda_flow_node_type
-- ----------------------------
INSERT INTO `eda_flow_node_type` VALUES (1, 'start', '开始', '基础', '/svg/start.svg', 'rgb(0 128 0 / 20%)', '在一个流程中，开始节点是触发流程执行的起始节点');
INSERT INTO `eda_flow_node_type` VALUES (2, 'timer', '定时器', '基础', '/svg/timer.svg', 'rgb(0 128 88 / 25%)', '可作为起始节点，用于定时触发流程周期性执行，可指定执行次数，可输出指定格式的时间戳；亦可作为非起始节点，由上游节点触发执行。');
INSERT INTO `eda_flow_node_type` VALUES (3, 'delay', '延时器', '基础', '/svg/delay.svg', 'rgb(8 155 16 / 25%)', '延时节点，可设定延迟时间，用于延迟其下游节点的运行');
INSERT INTO `eda_flow_node_type` VALUES (4, 'output', '输出', '基础', '/svg/output.svg', 'rgb(145 188 130 / 75%)', '输出节点，可展示其上游节点的输出参数信息');
INSERT INTO `eda_flow_node_type` VALUES (5, 'condition', '条件', '运算', '/svg/condition.svg', 'rgb(220 200 80 / 60%)', '条件节点，用于判断条件是否满足，可使用多个此节点组成与或等逻辑通路，条件满足后会继续向下游节点执行输出');
INSERT INTO `eda_flow_node_type` VALUES (6, 'sequence', '序列', '运算', '/svg/sequence.svg', 'rgb(170 150 50 / 40%)', '序列节点，可按自定义间隔大小进行递增或递减，依次输出整数序列');
INSERT INTO `eda_flow_node_type` VALUES (7, 'splice', '拼接', '运算', '/svg/splice.svg', 'rgb(200 180 75 / 60%)', '拼接节点，在一定时间内持续接收上游节点的输出参数，对指定字段的值进行拼接并输出');
INSERT INTO `eda_flow_node_type` VALUES (8, 'split', '分割', '运算', '/svg/split.svg', 'rgb(180 150 20 / 50%)', '分割节点，对指定字段的值进行分割后输出，可指定输出方式');
INSERT INTO `eda_flow_node_type` VALUES (21, 'json_parser', 'JSON解析', '解析', '/svg/json_parser.svg', 'rgb(180 197 125 / 60%)', '用于解析json格式的内容，可解析上游节点的输出参数，获取用户需要的参数信息');
INSERT INTO `eda_flow_node_type` VALUES (22, 'html_parser', 'HTML解析', '解析', '/svg/html_parser.svg', 'rgb(180 197 125 / 60%)', '用于解析html格式的文本内容，可指定css选择器解析出目标元素，输出html内容或文本内容');
INSERT INTO `eda_flow_node_type` VALUES (23, 'xml_parser', 'XML解析', '解析', '/svg/xml_parser.svg', 'rgb(180 197 125 / 60%)', '用于解析xml格式的文本内容，可转化为json格式的参数信息输出');
INSERT INTO `eda_flow_node_type` VALUES (31, 'http_request', 'HTTP请求', '网络', '/svg/http_request.svg', 'rgb(235 186 73 / 75%)', '可以发起HTTP请求，支持所有的请求类型，支持携带各种请求参数信息以及token等请求头信息');
INSERT INTO `eda_flow_node_type` VALUES (32, 'http_receive', 'HTTP接收', '网络', '/svg/http_receive.svg', 'rgb(235 186 73 / 75%)', '可创建HTTP服务，用于接收HTTP请求，解析出请求参数，向下游输出。可根据请求参数处理业务逻辑，与HTTP响应节点搭配使用');
INSERT INTO `eda_flow_node_type` VALUES (33, 'http_response', 'HTTP响应', '网络', '/svg/http_response.svg', 'rgb(200 195 60 / 70%)', '可响应HTTP请求，与HTTP接收节点搭配使用，在处理完业务逻辑后，响应请求并返回数据，可根据上游节点的输出结果动态响应数据');
INSERT INTO `eda_flow_node_type` VALUES (34, 'ws_server', 'WS服务端', '网络', '/svg/ws_server.svg', 'rgb(220 160 25 / 75%)', 'WebSocket服务端节点，可自定义路径创建websocket服务，自定义发送消息内容和发送时机，可广播发送上游节点的输出参数，收到客户端消息后亦会向下游输出。本节点为阻塞节点，运行后需要手动停止');
INSERT INTO `eda_flow_node_type` VALUES (35, 'ws_client', 'WS客户端', '网络', '/svg/ws_client.svg', 'rgb(215 140 40 / 50%)', 'WebSocket客户端节点，可连接指定路径的websocket服务，自定义发送消息内容和发送时机，收到服务端消息后会立即向下游输出');
INSERT INTO `eda_flow_node_type` VALUES (37, 'mqtt_sub', 'MQTT订阅', '网络', '/svg/mqtt_sub.svg', 'rgb(140 180 40 / 50%)', 'MQTT订阅节点，可订阅指定topic中的消息，接收到消息后会向下游输出。本节点为阻塞节点，运行后需要手动停止');
INSERT INTO `eda_flow_node_type` VALUES (38, 'mqtt_pub', 'MQTT发布', '网络', '/svg/mqtt_pub.svg', 'rgb(130 160 50 / 60%)', 'MQTT发布节点，可向指定topic中发送MQTT消息，发送的消息内容会向下游节点输出');
INSERT INTO `eda_flow_node_type` VALUES (39, 'email', '发送邮件', '网络', '/svg/email.svg', 'rgb(220 190 110 / 80%)', '可以发送电子邮件，支持抄送、密送，支持发送html格式的邮件');
INSERT INTO `eda_flow_node_type` VALUES (51, 'mysql', 'Mysql', '数据库', '/svg/mysql.svg', 'rgb(220 180 50 / 50%)', 'Mysql节点，可连接mysql数据库，执行自定义sql语句，支持任意类型的多条sql语句，输出每条语句的执行结果和内容');
INSERT INTO `eda_flow_node_type` VALUES (52, 'mongodb', 'MongoDB', '数据库', '/svg/mongodb.svg', 'rgb(230 170 60 / 70%)', 'MongoDB节点，可连接MongoDB数据库，执行自定义命令语句，输出执行结果和内容');
INSERT INTO `eda_flow_node_type` VALUES (53, 'redis', 'Redis', '数据库', '/svg/redis.svg', 'rgb(235 180 10 / 50%)', 'Redis节点，可连接redis服务器，执行自定义操作，输出执行结果和内容');
INSERT INTO `eda_flow_node_type` VALUES (100, 'subflow', '子流程', '子流程', '/svg/subflow.svg', 'rgb(200 70 180 / 62%)', '子流程节点，可选择其他流程作为子流程来执行，本节点的输入参数可传递至子流程中的[子输入节点]，子流程中的[子输出节点]可将输出参数返回至本节点作为输出参数');
INSERT INTO `eda_flow_node_type` VALUES (101, 'sub_input', '子输入', '子流程', '/svg/sub_input.svg', 'rgb(190 70 50 / 42%)', '子流程输入节点，用于子流程中，可接收关联的[子流程节点]的输入参数');
INSERT INTO `eda_flow_node_type` VALUES (102, 'sub_output', '子输出', '子流程', '/svg/sub_output.svg', 'rgb(120 150 90 / 42%)', '子流程输出节点，用于子流程中，可将本节点的输出参数传递给关联的[子流程节点]作为输出参数');

-- ----------------------------
-- Table structure for eda_flow_node_type_param
-- ----------------------------
DROP TABLE IF EXISTS `eda_flow_node_type_param`;
CREATE TABLE `eda_flow_node_type_param`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `type_id` bigint NOT NULL COMMENT '节点类型id',
  `key` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '参数key',
  `name` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '参数名称',
  `required` tinyint(1) NULL DEFAULT 0 COMMENT '参数是否必填',
  `in_type` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '参数输入类型',
  `option` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '下拉选项内容，多个值以逗号分隔',
  `placeholder` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '参数值提示性内容',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `index_type_id`(`type_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 76 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of eda_flow_node_type_param
-- ----------------------------
INSERT INTO `eda_flow_node_type_param` VALUES (1, 2, 'period', '执行周期', 1, 'select', 'SECONDS,MINUTES,HOURS,DAYS,CRON', '1,MINUTES');
INSERT INTO `eda_flow_node_type_param` VALUES (2, 2, 'times', '执行次数', 1, 'input', NULL, '输入0表示不限制次数');
INSERT INTO `eda_flow_node_type_param` VALUES (3, 2, 'timestamp', '输出时间', 0, 'select', 'timestamp,yyyy-MM-dd HH:mm:ss,yyyy-MM-dd\'T\'HH:mm:ss\'Z\',yyyyMMdd,HHmmss,HH:mm:ss,yyyy-MM-dd,yyyy/MM/dd,yyyyMMddHHmmss', NULL);
INSERT INTO `eda_flow_node_type_param` VALUES (4, 3, 'delay', '延迟时间', 1, 'select', 'MILLISECONDS,SECONDS,MINUTES,HOURS,DAYS', '5,SECONDS');
INSERT INTO `eda_flow_node_type_param` VALUES (5, 21, 'parseKey', '解析参数', 1, 'input', NULL, 'httpResult.$0.name,params.a');
INSERT INTO `eda_flow_node_type_param` VALUES (6, 31, 'url', 'URL', 1, 'input', NULL, '/api/http/test');
INSERT INTO `eda_flow_node_type_param` VALUES (7, 31, 'method', '请求方式', 1, 'select', 'GET,POST,PUT,DELETE,HEAD,OPTIONS,TRACE,PATCH', NULL);
INSERT INTO `eda_flow_node_type_param` VALUES (8, 31, 'params', '请求参数', 0, 'input', NULL, 'id=1&name=xx');
INSERT INTO `eda_flow_node_type_param` VALUES (9, 31, 'body', '请求体', 0, 'input', NULL, '{\'id\': 1, \'name\': \'xx\'}');
INSERT INTO `eda_flow_node_type_param` VALUES (10, 31, 'header', '请求头', 0, 'input', NULL, 'Authorization:xxx,Content-Type:(默认json)');
INSERT INTO `eda_flow_node_type_param` VALUES (11, 33, 'uri', 'URI', 1, 'input', NULL, '/api/http/test');
INSERT INTO `eda_flow_node_type_param` VALUES (12, 33, 'method', '请求方式', 1, 'select', 'GET,POST,PUT,DELETE,HEAD,OPTIONS,TRACE,PATCH', NULL);
INSERT INTO `eda_flow_node_type_param` VALUES (14, 33, 'resData', '响应数据', 0, 'input', NULL, '{\"result\":\"OK\"} (不填默认返回上游节点的输出)');
INSERT INTO `eda_flow_node_type_param` VALUES (15, 34, 'path', '路径', 1, 'input', NULL, '/w/test');
INSERT INTO `eda_flow_node_type_param` VALUES (16, 34, 'query', '请求参数', 0, 'input', NULL, 'name,id');
INSERT INTO `eda_flow_node_type_param` VALUES (17, 34, 'fanout', '输出参数发送（默认不发送）', 0, 'select', '发送,不发送', NULL);
INSERT INTO `eda_flow_node_type_param` VALUES (18, 34, 'sendAfterConnect', '建立连接后发送', 0, 'input', NULL, 'eg: hello ${name}! balabala...');
INSERT INTO `eda_flow_node_type_param` VALUES (19, 34, 'sendAfterReceive', '收到消息后发送', 0, 'input', NULL, 'eg: received! balabala...');
INSERT INTO `eda_flow_node_type_param` VALUES (20, 35, 'path', '路径', 1, 'input', NULL, '/w/test?name=test1');
INSERT INTO `eda_flow_node_type_param` VALUES (21, 35, 'sendAfterConnect', '建立连接后发送', 0, 'input', NULL, 'eg: hello server! balabala...');
INSERT INTO `eda_flow_node_type_param` VALUES (22, 37, 'clientId', 'Client ID', 1, 'input', NULL, 'mqtt_xxxxxx');
INSERT INTO `eda_flow_node_type_param` VALUES (23, 37, 'broker', '服务器地址', 1, 'select', '1883,8083,8883,8084', 'tcp://broker.emqx.io,1883');
INSERT INTO `eda_flow_node_type_param` VALUES (24, 37, 'topic', 'Topic', 1, 'input', NULL, '/test/+/xx/#');
INSERT INTO `eda_flow_node_type_param` VALUES (25, 37, 'username', '用户名', 0, 'input', NULL, NULL);
INSERT INTO `eda_flow_node_type_param` VALUES (26, 37, 'password', '密码', 0, 'input', NULL, NULL);
INSERT INTO `eda_flow_node_type_param` VALUES (27, 38, 'clientId', 'Client ID', 1, 'input', NULL, 'mqtt_xxxxxx');
INSERT INTO `eda_flow_node_type_param` VALUES (28, 38, 'broker', '服务器地址', 1, 'select', '1883,8083,8883,8084', 'tcp://broker.emqx.io,1883');
INSERT INTO `eda_flow_node_type_param` VALUES (29, 38, 'topic', 'Topic', 1, 'input', NULL, '/test/xx');
INSERT INTO `eda_flow_node_type_param` VALUES (30, 38, 'message', '发送消息', 1, 'input', NULL, NULL);
INSERT INTO `eda_flow_node_type_param` VALUES (31, 38, 'username', '用户名', 0, 'input', NULL, NULL);
INSERT INTO `eda_flow_node_type_param` VALUES (32, 38, 'password', '密码', 0, 'input', NULL, NULL);
INSERT INTO `eda_flow_node_type_param` VALUES (33, 51, 'url', 'URL地址', 1, 'input', NULL, 'jdbc:mysql://127.0.0.1:3306/flow_eda_test');
INSERT INTO `eda_flow_node_type_param` VALUES (34, 51, 'username', '用户名', 1, 'input', NULL, 'root');
INSERT INTO `eda_flow_node_type_param` VALUES (35, 51, 'password', '密码', 1, 'input', NULL, NULL);
INSERT INTO `eda_flow_node_type_param` VALUES (36, 51, 'sql', '执行SQL语句', 1, 'input', NULL, '支持单条或多条sql语句，多条语句之间使用分号相隔');
INSERT INTO `eda_flow_node_type_param` VALUES (37, 52, 'url', 'URL地址', 1, 'input', NULL, 'mongodb://root:admin@127.0.0.1:27017/admin');
INSERT INTO `eda_flow_node_type_param` VALUES (38, 52, 'db', '数据库名称', 1, 'input', NULL, NULL);
INSERT INTO `eda_flow_node_type_param` VALUES (39, 52, 'command', '执行语句', 1, 'input', NULL, '{\'find\': \'test_c\', \'filter\': {\'name\': \'test\'}, \'limit\': 1}');
INSERT INTO `eda_flow_node_type_param` VALUES (40, 53, 'uri', '服务器地址', 1, 'input', NULL, '127.0.0.1:6379');
INSERT INTO `eda_flow_node_type_param` VALUES (41, 53, 'method', '操作', 1, 'select', 'set,get,del,exists,getSet,getDel,hset,hget,hdel,hgetAll,hexists', NULL);
INSERT INTO `eda_flow_node_type_param` VALUES (42, 53, 'args', '参数', 1, 'input', NULL, 'testxx,Hello World! (多个参数用,分隔)');
INSERT INTO `eda_flow_node_type_param` VALUES (43, 53, 'username', '用户名', 0, 'input', NULL, NULL);
INSERT INTO `eda_flow_node_type_param` VALUES (44, 53, 'password', '密码', 0, 'input', NULL, NULL);
INSERT INTO `eda_flow_node_type_param` VALUES (45, 53, 'database', '数据库', 0, 'input', NULL, '1');
INSERT INTO `eda_flow_node_type_param` VALUES (46, 5, 'field', '字段名', 1, 'input', NULL, '仅可填写单个字段名');
INSERT INTO `eda_flow_node_type_param` VALUES (47, 5, 'condition', '判断逻辑', 1, 'select', '=,!=,>,>=,<,<=', NULL);
INSERT INTO `eda_flow_node_type_param` VALUES (48, 5, 'value', '判断值', 1, 'input', NULL, '支持null，表示该字段不存在或值为null');
INSERT INTO `eda_flow_node_type_param` VALUES (49, 32, 'uri', 'URI', 1, 'input', NULL, '/api/http/test');
INSERT INTO `eda_flow_node_type_param` VALUES (50, 32, 'method', '请求方式', 1, 'select', 'GET,POST,PUT,DELETE,HEAD,OPTIONS,TRACE,PATCH', NULL);
INSERT INTO `eda_flow_node_type_param` VALUES (51, 6, 'start', '初始值', 1, 'input', NULL, '请输入整数（产生的序列包含此值）');
INSERT INTO `eda_flow_node_type_param` VALUES (52, 6, 'end', '结束值', 1, 'input', NULL, '请输入整数（产生的序列包含此值）');
INSERT INTO `eda_flow_node_type_param` VALUES (53, 6, 'action', '递进方式(默认递增)', 0, 'select', '递增,递减', NULL);
INSERT INTO `eda_flow_node_type_param` VALUES (54, 6, 'step', '递进间隔', 0, 'input', NULL, '请输入大于0的正整数（默认值为1）');
INSERT INTO `eda_flow_node_type_param` VALUES (55, 7, 'field', '字段名', 1, 'input', NULL, NULL);
INSERT INTO `eda_flow_node_type_param` VALUES (56, 7, 'filter', '是否过滤空值（默认过滤）', 0, 'select', '过滤,不过滤', NULL);
INSERT INTO `eda_flow_node_type_param` VALUES (57, 8, 'field', '字段名', 1, 'input', NULL, NULL);
INSERT INTO `eda_flow_node_type_param` VALUES (58, 8, 'separator', '分割符', 0, 'input', NULL, '默认以,分割');
INSERT INTO `eda_flow_node_type_param` VALUES (59, 8, 'outputWay', '输出方式（默认输出数组）', 0, 'select', '单次输出数组,多次输出单值', NULL);
INSERT INTO `eda_flow_node_type_param` VALUES (60, 100, 'subflow', '选择子流程', 1, 'api', NULL, NULL);
INSERT INTO `eda_flow_node_type_param` VALUES (61, 22, 'parseKey', '解析参数（html来源）', 1, 'input', NULL, 'httpResult.html');
INSERT INTO `eda_flow_node_type_param` VALUES (62, 22, 'selector', 'CSS选择器', 1, 'select', 'class,tag,id', 'x-xx (className),class');
INSERT INTO `eda_flow_node_type_param` VALUES (63, 22, 'filterTags', '二次过滤标签', 0, 'input', NULL, 'div');
INSERT INTO `eda_flow_node_type_param` VALUES (64, 22, 'outputType', '输出内容（默认输出文本内容）', 0, 'select', '输出文本内容,输出html内容', NULL);
INSERT INTO `eda_flow_node_type_param` VALUES (65, 23, 'parseKey', '解析参数（xml来源）', 1, 'input', NULL, 'httpResult.xml');
INSERT INTO `eda_flow_node_type_param` VALUES (66, 23, 'attr', '标签属性名称', 0, 'input', NULL, '默认为@attributes');
INSERT INTO `eda_flow_node_type_param` VALUES (67, 39, 'host', '邮件服务器', 1, 'input', NULL, 'smtp.qq.com');
INSERT INTO `eda_flow_node_type_param` VALUES (68, 39, 'fromEmail', '发件人邮箱', 1, 'input', NULL, 'xxx@qq.com');
INSERT INTO `eda_flow_node_type_param` VALUES (69, 39, 'authCode', '邮箱授权码', 1, 'input', NULL, '邮箱开启SMTP服务会有一个授权码');
INSERT INTO `eda_flow_node_type_param` VALUES (70, 39, 'toEmail', '收件人邮箱', 1, 'input', NULL, '多个邮箱之间使用,分隔');
INSERT INTO `eda_flow_node_type_param` VALUES (71, 39, 'subject', '邮件主题', 1, 'input', NULL, '填写邮件的主题/标题');
INSERT INTO `eda_flow_node_type_param` VALUES (72, 39, 'text', '邮件正文', 1, 'input', NULL, '填写邮件的正文内容，支持html等格式');
INSERT INTO `eda_flow_node_type_param` VALUES (73, 39, 'isHtml', '发送格式（默认文本格式）', 0, 'select', '文本格式,HTML格式', NULL);
INSERT INTO `eda_flow_node_type_param` VALUES (74, 39, 'ccEmail', '抄送邮箱', 0, 'input', NULL, '多个邮箱之间使用,分隔');
INSERT INTO `eda_flow_node_type_param` VALUES (75, 39, 'bccEmail', '密送邮箱', 0, 'input', NULL, '多个邮箱之间使用,分隔');

SET FOREIGN_KEY_CHECKS = 1;
