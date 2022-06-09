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
  `status` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '状态',
  `create_date` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_date` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `index_create_date`(`create_date`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of eda_flow
-- ----------------------------
INSERT INTO `eda_flow` VALUES ('10j5nc7cz5k000', 'WebSocket客户端示例', '使用WebSocket客户端节点连接WebSocket服务，接收消息。前置条件：需要先运行流程[自定义WebSocket服务端]', 'FAILED', '2022-05-30 16:40:58', '2022-06-04 18:13:10');
INSERT INTO `eda_flow` VALUES ('13mwrf5znqv400', 'MQTT订阅消息示例', '订阅MQTT消息，输出接收到的消息内容', 'FAILED', '2022-06-01 18:01:21', '2022-06-01 18:01:21');
INSERT INTO `eda_flow` VALUES ('18rwqz520iio00', '自定义WebSocket服务端', '创建自定义WebSocket服务端，提供WebSocket服务', 'FAILED', '2022-05-30 16:40:18', '2022-05-30 16:40:18');
INSERT INTO `eda_flow` VALUES ('1p8nhh3c4aio00', '解析HTTP请求结果', '包含HTTP请求-解析器节点，解析请求结果并输出', 'FINISHED', '2022-05-09 16:03:39', '2022-05-09 16:03:39');
INSERT INTO `eda_flow` VALUES ('2dzjjpdtijb400', '定时发起HTTP请求并附带输出时间', '定时器+HTTP请求+解析器+延时器，发起请求，延迟输出结果并附带请求时间', 'FINISHED', '2022-05-09 17:58:29', '2022-05-09 17:58:29');
INSERT INTO `eda_flow` VALUES ('34224hnftcm000', 'HTTP请求示例', '包含HTTP请求节点，发送网络请求，并输出响应结果', 'FINISHED', '2022-05-09 15:57:18', '2022-05-09 16:01:16');
INSERT INTO `eda_flow` VALUES ('3gsaefsrjvc000', 'MQTT发布消息示例', '自定义发布消息到指定的MQTT中，并输出发送的消息内容', 'FINISHED', '2022-06-01 18:02:27', '2022-06-01 18:02:27');
INSERT INTO `eda_flow` VALUES ('3osi3b97pa4000', '延时器示例', '包含开始-延时器-输出等节点', 'FINISHED', '2022-05-09 15:16:18', '2022-05-09 15:16:18');
INSERT INTO `eda_flow` VALUES ('3uldr6qij5o000', '流程运行状态示例', '可查看流程运行状态，鼠标悬停状态图标，可查看详细信息', 'FAILED', '2022-05-09 17:51:02', '2022-05-09 17:51:02');
INSERT INTO `eda_flow` VALUES ('41as9fmikt8000', '中止运行示例', '可在流程运行过程中，点击停止运行按钮，中止运行', 'FAILED', '2022-05-09 17:47:20', '2022-05-09 17:47:20');
INSERT INTO `eda_flow` VALUES ('48r380g6qc4000', '解析器示例', '包含开始-解析器-输出等节点', 'FINISHED', '2022-05-09 15:11:38', '2022-05-09 15:11:38');
INSERT INTO `eda_flow` VALUES ('4ufazyee92c000', 'Mysql节点使用示例', '使用Mysql节点连接至指定的mysql数据库，并执行自定义sql语句，查看执行结果。前置条件：需要具备或已知mysql数据库地址，更新到节点URL属性上再运行流程', 'INIT', '2022-06-04 18:11:50', '2022-06-04 18:20:14');
INSERT INTO `eda_flow` VALUES ('5epeyaoy9ac000', '请求自定义HTTP服务', '发起HTTP请求，用于检查自定义HTTP服务的结果。前置条件：需要先运行流程[HTTP响应示例]', 'FINISHED', '2022-05-26 15:13:06', '2022-06-04 18:13:58');
INSERT INTO `eda_flow` VALUES ('6a088jl98eg00', '定时器示例', '仅包含定时器-输出两个节点', 'FINISHED', '2022-05-09 13:40:33', '2022-05-09 13:40:33');
INSERT INTO `eda_flow` VALUES ('ihvivrh5j1k00', 'HTTP响应示例', '创建HTTP服务，自定义响应数据', 'FAILED', '2022-05-26 15:06:57', '2022-05-26 15:06:57');
INSERT INTO `eda_flow` VALUES ('m6o3aghqfrk00', '并行运行示例', '同一流程内可多条线路并行运行', 'FINISHED', '2022-05-09 17:58:27', '2022-05-09 17:58:27');
INSERT INTO `eda_flow` VALUES ('mk3eo1ewkk000', '节点参数传递', '任意节点之间都可以进行自定义参数传递，仅能从上一节点传递至下一节点，不可跨节点传递', 'FINISHED', '2022-05-09 15:20:00', '2022-05-09 15:56:06');
INSERT INTO `eda_flow` VALUES ('xu7fsb4whw000', '简单示例', '仅包含开始-输出两个节点', 'INIT', '2022-05-09 03:32:31', '2022-05-09 03:32:31');

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
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `index_flow_id`(`flow_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of eda_flow_node_data
-- ----------------------------
INSERT INTO `eda_flow_node_data` VALUES ('122wnmuawvgw00', NULL, '3uldr6qij5o000', NULL, NULL, NULL, NULL, NULL, NULL, '1rdi0c65pyhs00', '4nhpgowo3mg000');
INSERT INTO `eda_flow_node_data` VALUES ('154kjyfueaf400', NULL, '18rwqz520iio00', NULL, NULL, NULL, NULL, NULL, NULL, '5sfvkcmh4a0000', '19iwhrcrtxy800');
INSERT INTO `eda_flow_node_data` VALUES ('188n1ugkvshs00', 'HTTP请求', '5epeyaoy9ac000', 9, '190px', '290px', NULL, '{\"url\": \"/api/v1/test2\", \"method\": \"POST\", \"params\": \"name=张三\"}', NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('18fl3883e75s00', 'MQTT发布', '3gsaefsrjvc000', 14, '55px', '320px', NULL, '{\"topic\": \"/mqtt/test/a\", \"broker\": \"tcp://broker.emqx.io,1883\", \"message\": \"a: ${timestamp}\", \"clientId\": \"mqttx_9793d5c8\"}', NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('19iwhrcrtxy800', '输出', '18rwqz520iio00', 4, '205px', '410px', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('1b12llyt5lgg00', NULL, '34224hnftcm000', NULL, NULL, NULL, NULL, NULL, NULL, '5apz8nnsgpc000', '2ikmbc80e58000');
INSERT INTO `eda_flow_node_data` VALUES ('1f0d6j4txbb400', 'HTTP请求', '5epeyaoy9ac000', 9, '305px', '290px', NULL, '{\"url\": \"/${q}/test3\", \"method\": \"GET\", \"params\": \"a=test3\"}', NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('1f2mfwal1o2o00', '输出1', 'm6o3aghqfrk00', 4, '195px', '90px', NULL, NULL, '{\"a\": \"${a}\"}', NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('1fukchv3s31c00', 'Mysql', '4ufazyee92c000', 15, '225px', '345px', NULL, '{\"sql\": \"SELECT id,type_name FROM eda_flow_node_type WHERE id=1\", \"url\": \"jdbc:mysql://127.0.0.1:3306/flow_eda\", \"password\": \"123456\", \"username\": \"root\"}', NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('1g5eivmtkbi800', '开始1', 'm6o3aghqfrk00', 1, '95px', '90px', NULL, NULL, '{\"a\": \"xxx-1\"}', NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('1gd04j0vgqg000', NULL, '3osi3b97pa4000', NULL, NULL, NULL, NULL, NULL, NULL, 'brjl0erogio00', 'zzlzi607jv400');
INSERT INTO `eda_flow_node_data` VALUES ('1jlsdvqqle8w00', NULL, '10j5nc7cz5k000', NULL, NULL, NULL, NULL, NULL, NULL, '2m9tp4kwxka000', '5aamavmwhb4000');
INSERT INTO `eda_flow_node_data` VALUES ('1js10mqz518g00', '输出4', 'm6o3aghqfrk00', 4, '195px', '730px', NULL, NULL, '{\"a\": \"${a}\"}', NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('1ld91r0t489s00', '开始', 'ihvivrh5j1k00', 1, '80px', '90px', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('1mc1hll7nl7k00', NULL, '1p8nhh3c4aio00', NULL, NULL, NULL, NULL, NULL, NULL, '4vf94lchs1s000', '4t6ekm5bvmo000');
INSERT INTO `eda_flow_node_data` VALUES ('1mlwguk8misg00', NULL, '2dzjjpdtijb400', NULL, NULL, NULL, NULL, NULL, NULL, '3xmkyojs654000', 'q7b6o1iy3cg00');
INSERT INTO `eda_flow_node_data` VALUES ('1n52uwx88z280', NULL, 'm6o3aghqfrk00', NULL, NULL, NULL, NULL, NULL, NULL, '2h0b5raks2s000', '2ryt3dgbcxa000');
INSERT INTO `eda_flow_node_data` VALUES ('1n5cxdr0tmdc00', '输出', '3uldr6qij5o000', 4, '285px', '645px', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('1oa6uw3p01og00', NULL, 'm6o3aghqfrk00', NULL, NULL, NULL, NULL, NULL, NULL, '3g6glxdv31k000', '52y7k5m4788000');
INSERT INTO `eda_flow_node_data` VALUES ('1rdi0c65pyhs00', '开始', '3uldr6qij5o000', 1, '165px', '215px', NULL, NULL, '{\"a\": \"xxx\"}', NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('1u3cl7n0dlq800', NULL, 'm6o3aghqfrk00', NULL, NULL, NULL, NULL, NULL, NULL, 'qfki3k8xu4000', '52y7k5m4788000');
INSERT INTO `eda_flow_node_data` VALUES ('1v8jtqz60kps00', NULL, '3uldr6qij5o000', NULL, NULL, NULL, NULL, NULL, NULL, '1rdi0c65pyhs00', '4tnr4rsnzdw000');
INSERT INTO `eda_flow_node_data` VALUES ('1vym9mvg117k00', '输出', '6a088jl98eg00', 4, '190px', '520px', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('1wb2ubmere8w00', '开始', 'xu7fsb4whw000', 1, '105px', '305px', '将此节点的自定义参数传递至下一节点', NULL, '{\"a\": \"xxx\"}', NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('1wgtbylvhf3400', NULL, '5epeyaoy9ac000', NULL, NULL, NULL, NULL, NULL, NULL, '5351auncrso000', '4f3d04uopr4000');
INSERT INTO `eda_flow_node_data` VALUES ('1x87ug825pj400', NULL, 'mk3eo1ewkk000', NULL, NULL, NULL, NULL, NULL, NULL, '3m52bl5i5pc000', '69mzkmq9x4g00');
INSERT INTO `eda_flow_node_data` VALUES ('1xb4qn68m0tc00', NULL, 'ihvivrh5j1k00', NULL, NULL, NULL, NULL, NULL, NULL, '1ld91r0t489s00', 'o0d0ag47vls00');
INSERT INTO `eda_flow_node_data` VALUES ('1zf3801jf3cw00', '输出', '3gsaefsrjvc000', 4, '240px', '520px', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('25drumgr0eio00', '输出', '4ufazyee92c000', 4, '140px', '550px', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('25oxq4xoglq800', '延时1秒', '3gsaefsrjvc000', 3, '145px', '150px', NULL, '{\"delay\": \"1,SECONDS\"}', '{\"timestamp\": \"${timestamp}\"}', NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('27jwy17xpzr400', '输出', '2dzjjpdtijb400', 4, '250px', '645px', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('27k4z0xei05c00', '输出', '13mwrf5znqv400', 4, '120px', '375px', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('27s1wzggeb400', '开始', '10j5nc7cz5k000', 1, '150px', '30px', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('29cput2xohwk00', '输出3', 'm6o3aghqfrk00', 4, '195px', '505px', NULL, NULL, '{\"a\": \"${a}\"}', NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('2amxn5fho2jo00', NULL, '18rwqz520iio00', NULL, NULL, NULL, NULL, NULL, NULL, '2j6e6bbqmnm00', '4q46w3pes0m000');
INSERT INTO `eda_flow_node_data` VALUES ('2bk45mph7xno00', '输出', '3uldr6qij5o000', 4, '60px', '645px', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('2cqgzb9sjz0g00', '输出', '34224hnftcm000', 4, '150px', '590px', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('2eqcm8iq2mm800', '定时器', '6a088jl98eg00', 2, '80px', '380px', NULL, '{\"times\": \"3\", \"period\": \"2,SECONDS\", \"timestamp\": \"HH:mm:ss\"}', '{\"a\": \"xxx\"}', NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('2fn9nu160uv400', NULL, 'm6o3aghqfrk00', NULL, NULL, NULL, NULL, NULL, NULL, '1f2mfwal1o2o00', '3dyid08c2ei000');
INSERT INTO `eda_flow_node_data` VALUES ('2fzb3iuepadc00', '输出', '3gsaefsrjvc000', 4, '55px', '520px', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('2h0b5raks2s000', '开始2', 'm6o3aghqfrk00', 1, '95px', '295px', NULL, NULL, '{\"a\": \"xxx-2\"}', NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('2ikmbc80e58000', 'HTTP请求', '34224hnftcm000', 9, '195px', '350px', NULL, '{\"url\": \"http://localhost:8081/${q}/flow\", \"header\": \"Accept:application/json\", \"method\": \"GET\", \"params\": \"limit=1\"}', '{\"b\": \"${a}-b\"}', NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('2j3i3kzpzfc000', NULL, 'm6o3aghqfrk00', NULL, NULL, NULL, NULL, NULL, NULL, '29cput2xohwk00', '5pxuhe3ufy8000');
INSERT INTO `eda_flow_node_data` VALUES ('2j6e6bbqmnm00', '定时器', '18rwqz520iio00', 2, '125px', '120px', NULL, '{\"times\": \"0\", \"period\": \"10,SECONDS\", \"timestamp\": \"HH:mm:ss\"}', NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('2jgmdybrjdq000', NULL, '5epeyaoy9ac000', NULL, NULL, NULL, NULL, NULL, NULL, '5kluo5odtl0000', '5351auncrso000');
INSERT INTO `eda_flow_node_data` VALUES ('2jj7ukeunlu000', NULL, '3gsaefsrjvc000', NULL, NULL, NULL, NULL, NULL, NULL, '3wgobbd6ol4000', '1zf3801jf3cw00');
INSERT INTO `eda_flow_node_data` VALUES ('2jo1w8462ia000', NULL, '34224hnftcm000', NULL, NULL, NULL, NULL, NULL, NULL, '2ikmbc80e58000', '2cqgzb9sjz0g00');
INSERT INTO `eda_flow_node_data` VALUES ('2l7vxf73d9o000', NULL, '1p8nhh3c4aio00', NULL, NULL, NULL, NULL, NULL, NULL, '4t6ekm5bvmo000', 'ui0qaqbv8qo00');
INSERT INTO `eda_flow_node_data` VALUES ('2ln3y2asa2a000', '输出', '5epeyaoy9ac000', 4, '190px', '485px', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('2lrton3eqms000', NULL, '3gsaefsrjvc000', NULL, NULL, NULL, NULL, NULL, NULL, '4c8mzac4do6000', '25oxq4xoglq800');
INSERT INTO `eda_flow_node_data` VALUES ('2m9tp4kwxka000', 'WS客户端2', '10j5nc7cz5k000', 12, '220px', '185px', NULL, '{\"path\": \"/ws/test?name=zhang\", \"sendAfterConnect\": \"hello server! I am zhang.\"}', NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('2mu3x58m5qu000', NULL, '41as9fmikt8000', NULL, NULL, NULL, NULL, NULL, NULL, '5bhkyaxsw7o000', 'bgid30y68q000');
INSERT INTO `eda_flow_node_data` VALUES ('2nsda7hue8o000', NULL, '5epeyaoy9ac000', NULL, NULL, NULL, NULL, NULL, NULL, '5kluo5odtl0000', '1f0d6j4txbb400');
INSERT INTO `eda_flow_node_data` VALUES ('2ryt3dgbcxa000', '输出2', 'm6o3aghqfrk00', 4, '195px', '295px', NULL, NULL, '{\"a\": \"${a}\"}', NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('2saklu2u4jc000', NULL, '3gsaefsrjvc000', NULL, NULL, NULL, NULL, NULL, NULL, '4c8mzac4do6000', '5ugkbmc5hmo000');
INSERT INTO `eda_flow_node_data` VALUES ('2uydhlhqpug000', '延时器', '2dzjjpdtijb400', 3, '250px', '420px', NULL, '{\"delay\": \"2,SECONDS\"}', '{\"result\": \"${httpResult.result.$0.name}\", \"timestamp\": \"${timestamp}\"}', NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('2w1obcovahq000', NULL, 'ihvivrh5j1k00', NULL, NULL, NULL, NULL, NULL, NULL, '56vn2n3a380000', '4fi2qqp3gmm000');
INSERT INTO `eda_flow_node_data` VALUES ('2z6pgokgpoy000', NULL, '3gsaefsrjvc000', NULL, NULL, NULL, NULL, NULL, NULL, '18fl3883e75s00', '2fzb3iuepadc00');
INSERT INTO `eda_flow_node_data` VALUES ('305ivvon2pe000', NULL, '10j5nc7cz5k000', NULL, NULL, NULL, NULL, NULL, NULL, '5plrpukibww000', '32f3jhztrau000');
INSERT INTO `eda_flow_node_data` VALUES ('313b3a0dn14000', '输出', '1p8nhh3c4aio00', 4, '105px', '560px', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('31roajzcjsm000', NULL, '3gsaefsrjvc000', NULL, NULL, NULL, NULL, NULL, NULL, '4grlw4cm5d6000', '4moatwj5x98000');
INSERT INTO `eda_flow_node_data` VALUES ('325wnirydx6000', NULL, '4ufazyee92c000', NULL, NULL, NULL, NULL, NULL, NULL, '4wochfvng22000', '1fukchv3s31c00');
INSERT INTO `eda_flow_node_data` VALUES ('32f3jhztrau000', '输出1', '10j5nc7cz5k000', 4, '80px', '370px', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('32f9ixt25ii000', '输出', 'xu7fsb4whw000', 4, '140px', '620px', '打印出上一节点传递的参数', NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('39z32j316d0000', NULL, '2dzjjpdtijb400', NULL, NULL, NULL, NULL, NULL, NULL, 'q7b6o1iy3cg00', '2uydhlhqpug000');
INSERT INTO `eda_flow_node_data` VALUES ('3dxtemcmplk000', NULL, 'm6o3aghqfrk00', NULL, NULL, NULL, NULL, NULL, NULL, '2ryt3dgbcxa000', 'qfki3k8xu4000');
INSERT INTO `eda_flow_node_data` VALUES ('3dyid08c2ei000', '延时1秒', 'm6o3aghqfrk00', 3, '310px', '90px', NULL, '{\"delay\": \"1,SECONDS\"}', '{\"a\": \"${a}\"}', NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('3e4j6g0omua000', '输出', '3uldr6qij5o000', 4, '165px', '645px', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('3fu8m0ttgiy000', NULL, 'mk3eo1ewkk000', NULL, NULL, NULL, NULL, NULL, NULL, '69mzkmq9x4g00', '48cfy4dsudg000');
INSERT INTO `eda_flow_node_data` VALUES ('3g6glxdv31k000', '延时4秒', 'm6o3aghqfrk00', 3, '310px', '730px', NULL, '{\"delay\": \"4,SECONDS\"}', '{\"a\": \"${a}\"}', NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('3k37rvwkv9c000', NULL, 'm6o3aghqfrk00', NULL, NULL, NULL, NULL, NULL, NULL, '3le77waupza000', '1js10mqz518g00');
INSERT INTO `eda_flow_node_data` VALUES ('3le77waupza000', '定时器2', 'm6o3aghqfrk00', 2, '95px', '730px', NULL, '{\"times\": \"1\", \"period\": \"3,SECONDS\", \"timestamp\": \"HH:mm:ss\"}', '{\"a\": \"xxx-4\"}', NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('3lwlmlke4o4000', NULL, '13mwrf5znqv400', NULL, NULL, NULL, NULL, NULL, NULL, '4obd502qfr0000', '27k4z0xei05c00');
INSERT INTO `eda_flow_node_data` VALUES ('3m52bl5i5pc000', '开始-a', 'mk3eo1ewkk000', 1, '60px', '280px', NULL, NULL, '{\"a\": \"xxx\"}', NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('3sssatqloli000', NULL, 'xu7fsb4whw000', NULL, NULL, NULL, NULL, NULL, NULL, '1wb2ubmere8w00', '32f9ixt25ii000');
INSERT INTO `eda_flow_node_data` VALUES ('3tx74wv9vt8000', '输出', '2dzjjpdtijb400', 4, '160px', '645px', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('3u8e1n8jh8c000', NULL, 'mk3eo1ewkk000', NULL, NULL, NULL, NULL, NULL, NULL, '48cfy4dsudg000', '5pyn5hp1o44000');
INSERT INTO `eda_flow_node_data` VALUES ('3wa09thtp2w000', '输出', '48r380g6qc4000', 4, '105px', '650px', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('3wgobbd6ol4000', 'MQTT发布', '3gsaefsrjvc000', 14, '240px', '320px', NULL, '{\"topic\": \"/mqtt/test/c\", \"broker\": \"tcp://broker.emqx.io,1883\", \"message\": \"c: ${timestamp}\", \"clientId\": \"mqttx_092ff908\"}', NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('3xmkyojs654000', 'HTTP请求', '2dzjjpdtijb400', 9, '160px', '190px', NULL, '{\"url\": \"http://localhost:8081/${q}/flow\", \"header\": \"Accept:application/json\", \"method\": \"GET\", \"params\": \"limit=1\"}', '{\"timestamp\": \"${timestamp}\"}', NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('3zs0yhk0r8q000', NULL, '10j5nc7cz5k000', NULL, NULL, NULL, NULL, NULL, NULL, '27s1wzggeb400', '2m9tp4kwxka000');
INSERT INTO `eda_flow_node_data` VALUES ('41amqxi2fmg000', NULL, 'm6o3aghqfrk00', NULL, NULL, NULL, NULL, NULL, NULL, '1js10mqz518g00', '3g6glxdv31k000');
INSERT INTO `eda_flow_node_data` VALUES ('42lw6tsj5mo000', '开始', 'ihvivrh5j1k00', 1, '170px', '90px', NULL, NULL, '{\"q\": \"api/v1\"}', NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('44zzp3bzqxi000', NULL, 'ihvivrh5j1k00', NULL, NULL, NULL, NULL, NULL, NULL, '42lw6tsj5mo000', '4w1sp11wc42000');
INSERT INTO `eda_flow_node_data` VALUES ('463pzntyc0u000', NULL, '3osi3b97pa4000', NULL, NULL, NULL, NULL, NULL, NULL, 'zzlzi607jv400', '5mefz3prh1s000');
INSERT INTO `eda_flow_node_data` VALUES ('486521g7uns000', NULL, 'mk3eo1ewkk000', NULL, NULL, NULL, NULL, NULL, NULL, '5pyn5hp1o44000', '4cplvt9y7wy000');
INSERT INTO `eda_flow_node_data` VALUES ('48cfy4dsudg000', '输出-b', 'mk3eo1ewkk000', 4, '175px', '525px', NULL, NULL, '{\"c\": \"${b}\"}', NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('4bwxih5eufc000', NULL, '5epeyaoy9ac000', NULL, NULL, NULL, NULL, NULL, NULL, '1f0d6j4txbb400', '4rqevj449nu000');
INSERT INTO `eda_flow_node_data` VALUES ('4c8mzac4do6000', '定时器', '3gsaefsrjvc000', 2, '55px', '50px', NULL, '{\"times\": \"2\", \"period\": \"5,SECONDS\", \"timestamp\": \"HH:mm:ss\"}', NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('4ccnzr06zuo000', '开始', '48r380g6qc4000', 1, '80px', '200px', NULL, NULL, '{\"a\": \"xxx-a\", \"b\": \"xxx-b\"}', NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('4cplvt9y7wy000', '输出d', 'mk3eo1ewkk000', 4, '60px', '770px', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('4cu2y5tc7s6000', NULL, '5epeyaoy9ac000', NULL, NULL, NULL, NULL, NULL, NULL, '5kluo5odtl0000', '188n1ugkvshs00');
INSERT INTO `eda_flow_node_data` VALUES ('4dtq5fo453q000', NULL, '1p8nhh3c4aio00', NULL, NULL, NULL, NULL, NULL, NULL, 'ui0qaqbv8qo00', '313b3a0dn14000');
INSERT INTO `eda_flow_node_data` VALUES ('4f3d04uopr4000', '输出', '5epeyaoy9ac000', 4, '75px', '485px', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('4fi2qqp3gmm000', 'HTTP响应', 'ihvivrh5j1k00', 10, '270px', '345px', NULL, '{\"uri\": \"/api/v1/test3\", \"query\": \"a\", \"method\": \"GET\", \"resData\": \"{\\\"reslut\\\": {\\\"a\\\": \\\"${a}\\\"}, \\\"total\\\": 1}\"}', NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('4gqkkz1h9ey000', NULL, '13mwrf5znqv400', NULL, NULL, NULL, NULL, NULL, NULL, 'y8kabypgz7k00', '4obd502qfr0000');
INSERT INTO `eda_flow_node_data` VALUES ('4grlw4cm5d6000', 'MQTT发布', '3gsaefsrjvc000', 14, '145px', '320px', NULL, '{\"topic\": \"/mqtt/test/b\", \"broker\": \"tcp://broker.emqx.io,1883\", \"message\": \"b: ${timestamp}\", \"clientId\": \"mqttx_feeb5235\"}', NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('4lnyjpqmn0q000', NULL, '6a088jl98eg00', NULL, NULL, NULL, NULL, NULL, NULL, '2eqcm8iq2mm800', '1vym9mvg117k00');
INSERT INTO `eda_flow_node_data` VALUES ('4moatwj5x98000', '输出', '3gsaefsrjvc000', 4, '145px', '520px', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('4nhpgowo3mg000', '延时器', '3uldr6qij5o000', 3, '60px', '435px', '当流程发生异常时，整个流程会中止运行，节点会停滞在当前状态', '{\"delay\": \"8,SECONDS\"}', NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('4obd502qfr0000', 'MQTT订阅', '13mwrf5znqv400', 13, '210px', '210px', NULL, '{\"topic\": \"/mqtt/test/+\", \"broker\": \"tcp://broker.emqx.io,1883\", \"clientId\": \"mqttx_7e232df2\"}', NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('4q46w3pes0m000', '输出', '18rwqz520iio00', 4, '125px', '410px', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('4qhafzihvwo000', NULL, '48r380g6qc4000', NULL, NULL, NULL, NULL, NULL, NULL, 'wvzmjwfdtuo00', '3wa09thtp2w000');
INSERT INTO `eda_flow_node_data` VALUES ('4rdsenizj2g000', NULL, '2dzjjpdtijb400', NULL, NULL, NULL, NULL, NULL, NULL, '5hzea2bl9lo000', '3xmkyojs654000');
INSERT INTO `eda_flow_node_data` VALUES ('4rqevj449nu000', '输出', '5epeyaoy9ac000', 4, '305px', '485px', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('4sqmq7hhdfg000', NULL, '5epeyaoy9ac000', NULL, NULL, NULL, NULL, NULL, NULL, '188n1ugkvshs00', '2ln3y2asa2a000');
INSERT INTO `eda_flow_node_data` VALUES ('4st4lxjwle0000', NULL, 'm6o3aghqfrk00', NULL, NULL, NULL, NULL, NULL, NULL, '4yj8cf9mgxg000', '29cput2xohwk00');
INSERT INTO `eda_flow_node_data` VALUES ('4t6ekm5bvmo000', 'HTTP请求', '1p8nhh3c4aio00', 9, '215px', '305px', NULL, '{\"url\": \"http://localhost:8081/${q}/flow\", \"header\": \"Accept:application/json\", \"method\": \"GET\", \"params\": \"limit=1\"}', '{\"b\": \"${a}-b\"}', NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('4tnr4rsnzdw000', 'HTTP请求', '3uldr6qij5o000', 9, '285px', '435px', NULL, '{\"url\": \"http://localhost:8081/test\", \"method\": \"GET\"}', NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('4v1wf1va5w6000', NULL, '48r380g6qc4000', NULL, NULL, NULL, NULL, NULL, NULL, '4ccnzr06zuo000', 'wvzmjwfdtuo00');
INSERT INTO `eda_flow_node_data` VALUES ('4vf94lchs1s000', '开始', '1p8nhh3c4aio00', 1, '105px', '115px', NULL, NULL, '{\"a\": \"xxx\", \"q\": \"api/v1\"}', NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('4w1sp11wc42000', 'HTTP响应', 'ihvivrh5j1k00', 10, '170px', '345px', NULL, '{\"uri\": \"/${q}/test2\", \"query\": \"name\", \"method\": \"POST\", \"resData\": \"{\\\"name\\\": \\\"${name}\\\"}\"}', NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('4wochfvng22000', '开始', '4ufazyee92c000', 1, '140px', '130px', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('4yj8cf9mgxg000', '定时器1', 'm6o3aghqfrk00', 2, '95px', '505px', NULL, '{\"times\": \"1\", \"period\": \"2,SECONDS\", \"timestamp\": \"HH:mm:ss\"}', '{\"a\": \"xxx-3\"}', NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('52y7k5m4788000', '总输出', 'm6o3aghqfrk00', 4, '515px', '405px', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('5351auncrso000', 'HTTP请求', '5epeyaoy9ac000', 9, '75px', '290px', NULL, '{\"url\": \"http://localhost:8088/api/v1/test1\", \"method\": \"GET\"}', '{\"name\": \"http请求test1\"}', NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('53v0rlwo1ac000', NULL, '3gsaefsrjvc000', NULL, NULL, NULL, NULL, NULL, NULL, '25oxq4xoglq800', '4grlw4cm5d6000');
INSERT INTO `eda_flow_node_data` VALUES ('54cqiur37zo000', '开始', '41as9fmikt8000', 1, '115px', '195px', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('56vn2n3a380000', '开始', 'ihvivrh5j1k00', 1, '270px', '90px', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('5aamavmwhb4000', '输出2', '10j5nc7cz5k000', 4, '220px', '370px', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('5apz8nnsgpc000', '开始', '34224hnftcm000', 1, '100px', '160px', NULL, NULL, '{\"a\": \"xxx\", \"q\": \"api/v1\"}', NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('5bhkyaxsw7o000', '延时器', '41as9fmikt8000', 3, '240px', '280px', '中止运行后，鼠标悬停在红叉图标上可查看流程中止原因', '{\"delay\": \"10,SECONDS\"}', '{\"a\": \"xxx\"}', NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('5d2fusmqm6k000', NULL, '2dzjjpdtijb400', NULL, NULL, NULL, NULL, NULL, NULL, 'q7b6o1iy3cg00', '3tx74wv9vt8000');
INSERT INTO `eda_flow_node_data` VALUES ('5d88135ki9o000', NULL, '3gsaefsrjvc000', NULL, NULL, NULL, NULL, NULL, NULL, '5ugkbmc5hmo000', '3wgobbd6ol4000');
INSERT INTO `eda_flow_node_data` VALUES ('5grdto48m4w000', NULL, '41as9fmikt8000', NULL, NULL, NULL, NULL, NULL, NULL, '54cqiur37zo000', '5bhkyaxsw7o000');
INSERT INTO `eda_flow_node_data` VALUES ('5hnp0jfza90000', NULL, '3uldr6qij5o000', NULL, NULL, NULL, NULL, NULL, NULL, '4nhpgowo3mg000', '2bk45mph7xno00');
INSERT INTO `eda_flow_node_data` VALUES ('5hzea2bl9lo000', '定时器', '2dzjjpdtijb400', 2, '65px', '190px', NULL, '{\"times\": \"3\", \"period\": \"2,SECONDS\", \"timestamp\": \"HH:mm:ss\"}', '{\"q\": \"api/v1\"}', NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('5kluo5odtl0000', '开始', '5epeyaoy9ac000', 1, '190px', '60px', NULL, NULL, '{\"q\": \"api/v1\"}', NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('5mefz3prh1s000', '输出', '3osi3b97pa4000', 4, '205px', '620px', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('5mlqqqj6l4k000', NULL, '2dzjjpdtijb400', NULL, NULL, NULL, NULL, NULL, NULL, '2uydhlhqpug000', '27jwy17xpzr400');
INSERT INTO `eda_flow_node_data` VALUES ('5p0mz9na1ss000', NULL, '4ufazyee92c000', NULL, NULL, NULL, NULL, NULL, NULL, '1fukchv3s31c00', '25drumgr0eio00');
INSERT INTO `eda_flow_node_data` VALUES ('5plrpukibww000', 'WS客户端1', '10j5nc7cz5k000', 12, '80px', '185px', NULL, '{\"path\": \"/ws/test?name=lin\", \"sendAfterConnect\": \"hello server! I am lin.\"}', NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('5pxuhe3ufy8000', '延时3秒', 'm6o3aghqfrk00', 3, '310px', '505px', NULL, '{\"delay\": \"3,SECONDS\"}', '{\"a\": \"${a}\"}', NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('5pyn5hp1o44000', '输出-c', 'mk3eo1ewkk000', 4, '175px', '770px', NULL, NULL, '{\"d\": \"${c}\"}', NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('5q4zicl8o9g000', NULL, 'm6o3aghqfrk00', NULL, NULL, NULL, NULL, NULL, NULL, '5pxuhe3ufy8000', '52y7k5m4788000');
INSERT INTO `eda_flow_node_data` VALUES ('5r43xrlgtsc000', NULL, '3gsaefsrjvc000', NULL, NULL, NULL, NULL, NULL, NULL, '4c8mzac4do6000', '18fl3883e75s00');
INSERT INTO `eda_flow_node_data` VALUES ('5sfvkcmh4a0000', 'WS服务端', '18rwqz520iio00', 11, '205px', '235px', NULL, '{\"path\": \"/ws/test\", \"query\": \"name\", \"fanout\": \"Send\", \"sendAfterConnect\": \"hello ${name}!\"}', NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('5u9p54sev5w000', NULL, '10j5nc7cz5k000', NULL, NULL, NULL, NULL, NULL, NULL, '27s1wzggeb400', '5plrpukibww000');
INSERT INTO `eda_flow_node_data` VALUES ('5ugkbmc5hmo000', '延时2秒', '3gsaefsrjvc000', 3, '240px', '50px', NULL, '{\"delay\": \"2,SECONDS\"}', '{\"timestamp\": \"${timestamp}\"}', NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('69mzkmq9x4g00', '输出a', 'mk3eo1ewkk000', 4, '175px', '280px', NULL, NULL, '{\"b\": \"${a}\"}', NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('ah97irtcqu800', NULL, '18rwqz520iio00', NULL, NULL, NULL, NULL, NULL, NULL, '2j6e6bbqmnm00', '5sfvkcmh4a0000');
INSERT INTO `eda_flow_node_data` VALUES ('bgid30y68q000', '输出', '41as9fmikt8000', 4, '240px', '520px', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('brjl0erogio00', '开始', '3osi3b97pa4000', 1, '60px', '240px', NULL, NULL, '{\"a\": \"xxx-a\"}', NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('ftcvzew502o00', NULL, 'm6o3aghqfrk00', NULL, NULL, NULL, NULL, NULL, NULL, '3dyid08c2ei000', '52y7k5m4788000');
INSERT INTO `eda_flow_node_data` VALUES ('o0d0ag47vls00', 'HTTP响应', 'ihvivrh5j1k00', 10, '80px', '345px', NULL, '{\"uri\": \"/api/v1/test1\", \"method\": \"GET\", \"resData\": \"{\\\"result\\\": \\\"OK\\\"}\"}', NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('pjffvx258ww00', NULL, '3uldr6qij5o000', NULL, NULL, NULL, NULL, NULL, NULL, '4tnr4rsnzdw000', '1n5cxdr0tmdc00');
INSERT INTO `eda_flow_node_data` VALUES ('q7b6o1iy3cg00', '解析器', '2dzjjpdtijb400', 5, '160px', '420px', NULL, '{\"parseKey\": \"httpResult.result.$0.name\"}', '{\"timestamp\": \"${timestamp}\"}', NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('qfki3k8xu4000', '延时2秒', 'm6o3aghqfrk00', 3, '310px', '295px', NULL, '{\"delay\": \"2,SECONDS\"}', '{\"a\": \"${a}\"}', NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('tfp7f13eolc00', NULL, '3uldr6qij5o000', NULL, NULL, NULL, NULL, NULL, NULL, '1rdi0c65pyhs00', '3e4j6g0omua000');
INSERT INTO `eda_flow_node_data` VALUES ('ui0qaqbv8qo00', '解析器', '1p8nhh3c4aio00', 5, '215px', '535px', NULL, '{\"parseKey\": \"httpResult.result.$0.name\"}', '{\"c\": \"${b}-c\"}', NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('wvzmjwfdtuo00', '解析器', '48r380g6qc4000', 5, '125px', '415px', '解析上一节点的参数并传递至下一节点', '{\"parseKey\": \"params.a\"}', '{\"p\": \"${b}\"}', NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('x7ag6qo8bpc00', NULL, 'm6o3aghqfrk00', NULL, NULL, NULL, NULL, NULL, NULL, '1g5eivmtkbi800', '1f2mfwal1o2o00');
INSERT INTO `eda_flow_node_data` VALUES ('y8kabypgz7k00', '开始', '13mwrf5znqv400', 1, '120px', '55px', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('zzlzi607jv400', '延时器', '3osi3b97pa4000', 3, '145px', '415px', NULL, '{\"delay\": \"50,SECONDS\"}', '{\"b\": \"${a}\"}', NULL, NULL);

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
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of eda_flow_node_type
-- ----------------------------
INSERT INTO `eda_flow_node_type` VALUES (1, 'start', '开始', '基础', '/svg/start.svg', 'rgb(0 128 0 / 20%)', '在一个流程中，开始节点作为该流程的触发起始节点');
INSERT INTO `eda_flow_node_type` VALUES (2, 'timer', '定时器', '基础', '/svg/timer.svg', 'rgb(0 128 88 / 25%)', '可作为起始节点，用于定时触发流程周期性执行；若作为非起始节点，则由上游节点进行触发执行');
INSERT INTO `eda_flow_node_type` VALUES (3, 'delay', '延时器', '基础', '/svg/delay.svg', 'rgb(8 155 16 / 25%)', '延时节点，可设定延迟时间，用于延迟其下游节点的运行');
INSERT INTO `eda_flow_node_type` VALUES (4, 'output', '输出', '基础', '/svg/output.svg', 'rgb(145 188 130 / 75%)', '输出节点，可展示其上游节点的输出参数信息');
INSERT INTO `eda_flow_node_type` VALUES (5, 'parser', '解析器', '解析', '/svg/parser.svg', 'rgb(180 197 125 / 60%)', '用于解析上游节点的输出参数，获取用户需要的参数信息');
INSERT INTO `eda_flow_node_type` VALUES (9, 'http_request', 'HTTP请求', '网络', '/svg/http_request.svg', 'rgb(235 186 73 / 75%)', '可以发起HTTP请求，支持所有的请求类型，支持携带各种请求参数信息以及token等请求头信息');
INSERT INTO `eda_flow_node_type` VALUES (10, 'http_response', 'HTTP响应', '网络', '/svg/http_response.svg', 'rgb(200 195 60 / 70%)', '可创建http服务，指定请求参数和响应内容，可根据请求参数动态响应数据。本节点为阻塞节点，运行后需要手动停止');
INSERT INTO `eda_flow_node_type` VALUES (11, 'ws_server', 'WS服务端', '网络', '/svg/ws_server.svg', 'rgb(220 160 25 / 75%)', 'WebSocket服务端节点，可自定义路径创建websocket服务，自定义发送消息内容和发送时机，可广播发送上游节点的输出参数，收到客户端消息后亦会向下游输出。本节点为阻塞节点，运行后需要手动停止');
INSERT INTO `eda_flow_node_type` VALUES (12, 'ws_client', 'WS客户端', '网络', '/svg/ws_client.svg', 'rgb(215 140 40 / 50%)', 'WebSocket客户端节点，可连接指定路径的websocket服务，自定义发送消息内容和发送时机，收到服务端消息后会立即向下游输出');
INSERT INTO `eda_flow_node_type` VALUES (13, 'mqtt_sub', 'MQTT订阅', '网络', '/svg/mqtt_sub.svg', 'rgb(140 180 40 / 50%)', 'MQTT订阅节点，可订阅指定topic中的消息，接收到消息后会向下游输出。本节点为阻塞节点，运行后需要手动停止');
INSERT INTO `eda_flow_node_type` VALUES (14, 'mqtt_pub', 'MQTT发布', '网络', '/svg/mqtt_pub.svg', 'rgb(130 160 50 / 60%)', 'MQTT发布节点，可向指定topic中发送MQTT消息，发送的消息内容会向下游节点输出');
INSERT INTO `eda_flow_node_type` VALUES (15, 'mysql', 'Mysql', '数据库', '/svg/mysql.svg', 'rgb(220 180 50 / 50%)', 'Mysql节点，可连接至mysql数据库，执行自定义sql语句，支持任意类型的多条sql语句，输出每条语句的执行结果和内容');

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
) ENGINE = InnoDB AUTO_INCREMENT = 33 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of eda_flow_node_type_param
-- ----------------------------
INSERT INTO `eda_flow_node_type_param` VALUES (1, 2, 'period', '执行周期', 1, 'select', 'SECONDS,MINUTES,HOURS,DAYS,CRON', '1,MINUTES');
INSERT INTO `eda_flow_node_type_param` VALUES (2, 2, 'times', '执行次数', 1, 'input', NULL, '输入0表示不限制次数');
INSERT INTO `eda_flow_node_type_param` VALUES (3, 2, 'timestamp', '输出时间', 0, 'select', 'timestamp,yyyy-MM-dd HH:mm:ss,yyyy-MM-dd\'T\'HH:mm:ss\'Z\',yyyyMMdd,HHmmss,HH:mm:ss,yyyy-MM-dd,yyyy/MM/dd,yyyyMMddHHmmss', NULL);
INSERT INTO `eda_flow_node_type_param` VALUES (4, 3, 'delay', '延迟时间', 1, 'select', 'MILLISECONDS,SECONDS,MINUTES,HOURS,DAYS', '5,SECONDS');
INSERT INTO `eda_flow_node_type_param` VALUES (5, 5, 'parseKey', '解析参数', 1, 'input', NULL, 'httpResult.$0.name,params.a');
INSERT INTO `eda_flow_node_type_param` VALUES (6, 9, 'url', 'URL', 1, 'input', NULL, 'http://localhost:8088/');
INSERT INTO `eda_flow_node_type_param` VALUES (7, 9, 'method', '请求方式', 1, 'select', 'GET,POST,PUT,DELETE,HEAD,OPTIONS,TRACE,PATCH', NULL);
INSERT INTO `eda_flow_node_type_param` VALUES (8, 9, 'params', '请求参数', 0, 'input', NULL, 'a=1&b=2');
INSERT INTO `eda_flow_node_type_param` VALUES (9, 9, 'body', '请求内容', 0, 'input', NULL, '{\'a\': 1, \'b\': \'2\'}');
INSERT INTO `eda_flow_node_type_param` VALUES (10, 9, 'header', '请求头', 0, 'input', NULL, 'token:xxx,Accept:application/json');
INSERT INTO `eda_flow_node_type_param` VALUES (11, 10, 'uri', 'URI', 1, 'input', NULL, '/api/v1/test');
INSERT INTO `eda_flow_node_type_param` VALUES (12, 10, 'method', '请求方式', 1, 'select', 'GET,POST,PUT,DELETE,HEAD,OPTIONS,TRACE,PATCH', NULL);
INSERT INTO `eda_flow_node_type_param` VALUES (13, 10, 'query', '请求参数', 0, 'input', NULL, 'a,name');
INSERT INTO `eda_flow_node_type_param` VALUES (14, 10, 'resData', '响应数据', 1, 'input', NULL, '{\"result\": {\"name\": \"${name}\"} , \"total\": 10}');
INSERT INTO `eda_flow_node_type_param` VALUES (15, 11, 'path', '路径', 1, 'input', NULL, '/ws/test');
INSERT INTO `eda_flow_node_type_param` VALUES (16, 11, 'query', '请求参数', 0, 'input', NULL, 'name,id');
INSERT INTO `eda_flow_node_type_param` VALUES (17, 11, 'fanout', '节点输出参数发送', 1, 'select', 'Don\'t Send,Send', NULL);
INSERT INTO `eda_flow_node_type_param` VALUES (18, 11, 'sendAfterConnect', '建立连接后发送', 0, 'input', NULL, 'eg: hello ${name}! balabala...');
INSERT INTO `eda_flow_node_type_param` VALUES (19, 11, 'sendAfterReceive', '收到消息后发送', 0, 'input', NULL, 'eg: received! balabala...');
INSERT INTO `eda_flow_node_type_param` VALUES (20, 12, 'path', '路径', 1, 'input', NULL, '/ws/test?name=test1');
INSERT INTO `eda_flow_node_type_param` VALUES (21, 12, 'sendAfterConnect', '建立连接后发送', 0, 'input', NULL, 'eg: hello server! balabala...');
INSERT INTO `eda_flow_node_type_param` VALUES (22, 13, 'clientId', 'Client ID', 1, 'input', NULL, 'mqtt_xxxxxx');
INSERT INTO `eda_flow_node_type_param` VALUES (23, 13, 'broker', '服务器地址', 1, 'select', '1883,8083,8883,8084', 'tcp://broker.emqx.io,1883');
INSERT INTO `eda_flow_node_type_param` VALUES (24, 13, 'topic', 'Topic', 1, 'input', NULL, '/test/+/xx/#');
INSERT INTO `eda_flow_node_type_param` VALUES (25, 13, 'username', '用户名', 0, 'input', NULL, NULL);
INSERT INTO `eda_flow_node_type_param` VALUES (26, 13, 'password', '密码', 0, 'input', NULL, NULL);
INSERT INTO `eda_flow_node_type_param` VALUES (27, 14, 'clientId', 'Client ID', 1, 'input', NULL, 'mqtt_xxxxxx');
INSERT INTO `eda_flow_node_type_param` VALUES (28, 14, 'broker', '服务器地址', 1, 'select', '1883,8083,8883,8084', 'tcp://broker.emqx.io,1883');
INSERT INTO `eda_flow_node_type_param` VALUES (29, 14, 'topic', 'Topic', 1, 'input', NULL, '/test/xx');
INSERT INTO `eda_flow_node_type_param` VALUES (30, 14, 'message', '发送消息', 1, 'input', NULL, NULL);
INSERT INTO `eda_flow_node_type_param` VALUES (31, 14, 'username', '用户名', 0, 'input', NULL, NULL);
INSERT INTO `eda_flow_node_type_param` VALUES (32, 14, 'password', '密码', 0, 'input', NULL, NULL);
INSERT INTO `eda_flow_node_type_param` VALUES (33, 15, 'url', 'URL地址', 1, 'input', NULL, 'jdbc:mysql://127.0.0.1:3306/flow_eda');
INSERT INTO `eda_flow_node_type_param` VALUES (34, 15, 'username', '用户名', 1, 'input', NULL, 'root');
INSERT INTO `eda_flow_node_type_param` VALUES (35, 15, 'password', '密码', 1, 'input', NULL, NULL);
INSERT INTO `eda_flow_node_type_param` VALUES (36, 15, 'sql', '执行SQL语句', 1, 'input', NULL, '支持单条或多条sql语句，多条语句之间使用分号相隔');

SET FOREIGN_KEY_CHECKS = 1;
