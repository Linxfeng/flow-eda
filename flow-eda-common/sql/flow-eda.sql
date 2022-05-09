/*
 Navicat Premium Data Transfer

 Source Server         : my-vps
 Source Server Type    : MySQL
 Source Server Version : 80028
 Source Host           : 124.221.84.22:3306
 Source Schema         : flow-eda

 Target Server Type    : MySQL
 Target Server Version : 80028
 File Encoding         : 65001

 Date: 09/05/2022 18:34:05
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
  `status` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '状态',
  `create_date` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_date` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `index_create_date`(`create_date`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of eda_flow
-- ----------------------------
INSERT INTO `eda_flow` VALUES ('1p8nhh3c4aio00', '解析HTTP请求结果', '包含HTTP请求-解析器节点，解析请求结果并输出', 'FINISHED', '2022-05-09 16:03:39', '2022-05-09 16:03:39');
INSERT INTO `eda_flow` VALUES ('2dzjjpdtijb400', '定时发起HTTP请求并附带输出时间', '定时器+HTTP请求+解析器+延时器，发起请求，延迟输出结果并附带请求时间', 'FINISHED', '2022-05-09 17:58:29', '2022-05-09 17:58:29');
INSERT INTO `eda_flow` VALUES ('34224hnftcm000', 'HTTP请求示例', '包含HTTP请求节点，发送网络请求，并输出响应结果', 'FINISHED', '2022-05-09 15:57:18', '2022-05-09 16:01:16');
INSERT INTO `eda_flow` VALUES ('3osi3b97pa4000', '延时器示例', '包含开始-延时器-输出等节点', 'FINISHED', '2022-05-09 15:16:18', '2022-05-09 15:16:18');
INSERT INTO `eda_flow` VALUES ('3uldr6qij5o000', '流程运行状态示例', '可查看流程运行状态，鼠标悬停状态图标，可查看详细信息', 'FAILED', '2022-05-09 17:51:02', '2022-05-09 17:51:02');
INSERT INTO `eda_flow` VALUES ('41as9fmikt8000', '中止运行示例', '可在流程运行过程中，点击停止运行按钮，中止运行', 'FAILED', '2022-05-09 17:47:20', '2022-05-09 17:47:20');
INSERT INTO `eda_flow` VALUES ('48r380g6qc4000', '解析器示例', '包含开始-解析器-输出等节点', 'FINISHED', '2022-05-09 15:11:38', '2022-05-09 15:11:38');
INSERT INTO `eda_flow` VALUES ('6a088jl98eg00', '定时器示例', '仅包含定时器-输出两个节点', 'FINISHED', '2022-05-09 13:40:33', '2022-05-09 13:40:33');
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
INSERT INTO `eda_flow_node_data` VALUES ('14kdcxrmp8ik00', NULL, 'aev4yh5kjgk00', NULL, NULL, NULL, NULL, NULL, NULL, 't0l3rxz2w4w00', '58zsx5ui7go000');
INSERT INTO `eda_flow_node_data` VALUES ('14qwqy3gdnr400', NULL, '5j8nx6kzbp4000', NULL, NULL, NULL, NULL, NULL, NULL, 'urixfauc1j400', 'donj4a3saso00');
INSERT INTO `eda_flow_node_data` VALUES ('17hko8p19sqk00', '开始', '3xgebwpxll6000', 1, '80px', '20px', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('1b12llyt5lgg00', NULL, '34224hnftcm000', NULL, NULL, NULL, NULL, NULL, NULL, '5apz8nnsgpc000', '2ikmbc80e58000');
INSERT INTO `eda_flow_node_data` VALUES ('1b2kny4tj6dc00', NULL, '58zsx5ui7go000', NULL, NULL, NULL, NULL, NULL, NULL, '1ctdtf4xdj5s00', '4m3ei749g2k000');
INSERT INTO `eda_flow_node_data` VALUES ('1ces2o3r4yzk00', '开始', '5rsmqlkp7fk000', 1, '195px', '210px', NULL, NULL, '{\"a\": \"xxx\"}', NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('1ctdtf4xdj5s00', '延时器', '58zsx5ui7go000', 6, '115px', '465px', NULL, '{\"delay\": \"1,SECONDS\"}', '{\"a\": \"${timestamp}\"}', NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('1d0p9dhu59gg00', '定时器2', '5rsmqlkp7fk000', 2, '60px', '415px', NULL, '{\"times\": \"2\", \"period\": \"*/2 * * * * ? ,CRON\"}', '{\"c\": \"b-${a}-1\"}', NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('1f2mfwal1o2o00', '输出1', 'm6o3aghqfrk00', 4, '195px', '90px', NULL, NULL, '{\"a\": \"${a}\"}', NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('1g5eivmtkbi800', '开始1', 'm6o3aghqfrk00', 1, '95px', '90px', NULL, NULL, '{\"a\": \"xxx-1\"}', NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('1gd04j0vgqg000', NULL, '3osi3b97pa4000', NULL, NULL, NULL, NULL, NULL, NULL, 'brjl0erogio00', 'zzlzi607jv400');
INSERT INTO `eda_flow_node_data` VALUES ('1hs0i1qye8u800', NULL, '41fwmav2z3k000', NULL, NULL, NULL, NULL, NULL, NULL, 'wuklet6bdow00', '2nlknl5lncs000');
INSERT INTO `eda_flow_node_data` VALUES ('1js10mqz518g00', '输出4', 'm6o3aghqfrk00', 4, '195px', '730px', NULL, NULL, '{\"a\": \"${a}\"}', NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('1mc1hll7nl7k00', NULL, '1p8nhh3c4aio00', NULL, NULL, NULL, NULL, NULL, NULL, '4vf94lchs1s000', '4t6ekm5bvmo000');
INSERT INTO `eda_flow_node_data` VALUES ('1mlwguk8misg00', NULL, '2dzjjpdtijb400', NULL, NULL, NULL, NULL, NULL, NULL, '3xmkyojs654000', 'q7b6o1iy3cg00');
INSERT INTO `eda_flow_node_data` VALUES ('1n52uwx88z280', NULL, 'm6o3aghqfrk00', NULL, NULL, NULL, NULL, NULL, NULL, '2h0b5raks2s000', '2ryt3dgbcxa000');
INSERT INTO `eda_flow_node_data` VALUES ('1n5cxdr0tmdc00', '输出', '3uldr6qij5o000', 4, '285px', '645px', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('1oa6uw3p01og00', NULL, 'm6o3aghqfrk00', NULL, NULL, NULL, NULL, NULL, NULL, '3g6glxdv31k000', '52y7k5m4788000');
INSERT INTO `eda_flow_node_data` VALUES ('1p020gwnaie800', NULL, '5rsmqlkp7fk000', NULL, NULL, NULL, NULL, NULL, NULL, '1d0p9dhu59gg00', '2s6c2p0lca2000');
INSERT INTO `eda_flow_node_data` VALUES ('1rdi0c65pyhs00', '开始', '3uldr6qij5o000', 1, '165px', '215px', NULL, NULL, '{\"a\": \"xxx\"}', NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('1u3cl7n0dlq800', NULL, 'm6o3aghqfrk00', NULL, NULL, NULL, NULL, NULL, NULL, 'qfki3k8xu4000', '52y7k5m4788000');
INSERT INTO `eda_flow_node_data` VALUES ('1v8jtqz60kps00', NULL, '3uldr6qij5o000', NULL, NULL, NULL, NULL, NULL, NULL, '1rdi0c65pyhs00', '4tnr4rsnzdw000');
INSERT INTO `eda_flow_node_data` VALUES ('1vnqd0ukj7gg00', '输出', 'bx6f3o3rxmg00', 4, '120px', '650px', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('1vym9mvg117k00', '输出', '6a088jl98eg00', 4, '190px', '520px', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('1wb2ubmere8w00', '开始', 'xu7fsb4whw000', 1, '105px', '305px', '将此节点的自定义参数传递至下一节点', NULL, '{\"a\": \"xxx\"}', NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('1x87ug825pj400', NULL, 'mk3eo1ewkk000', NULL, NULL, NULL, NULL, NULL, NULL, '3m52bl5i5pc000', '69mzkmq9x4g00');
INSERT INTO `eda_flow_node_data` VALUES ('21jfwadp7tcw00', '开始', 'bx6f3o3rxmg00', 1, '80px', '180px', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('27jwy17xpzr400', '输出', '2dzjjpdtijb400', 4, '250px', '645px', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('29cput2xohwk00', '输出3', 'm6o3aghqfrk00', 4, '195px', '505px', NULL, NULL, '{\"a\": \"${a}\"}', NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('2bk45mph7xno00', '输出', '3uldr6qij5o000', 4, '60px', '645px', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('2c69av6r0cu800', '输出', '4ccr0y2xtwu000', 4, '100px', '525px', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('2cqgzb9sjz0g00', '输出', '34224hnftcm000', 4, '150px', '590px', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('2eqcm8iq2mm800', '定时器', '6a088jl98eg00', 2, '80px', '380px', NULL, '{\"times\": \"3\", \"period\": \"2,SECONDS\", \"timestamp\": \"HH:mm:ss\"}', '{\"a\": \"xxx\"}', NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('2fn9nu160uv400', NULL, 'm6o3aghqfrk00', NULL, NULL, NULL, NULL, NULL, NULL, '1f2mfwal1o2o00', '3dyid08c2ei000');
INSERT INTO `eda_flow_node_data` VALUES ('2h0b5raks2s000', '开始2', 'm6o3aghqfrk00', 1, '95px', '295px', NULL, NULL, '{\"a\": \"xxx-2\"}', NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('2ikmbc80e58000', 'HTTP请求', '34224hnftcm000', 3, '195px', '350px', NULL, '{\"url\": \"http://localhost:8081/${q}/node/type\", \"header\": \"Accept:application/json\", \"method\": \"GET\", \"params\": \"limit=1\"}', '{\"b\": \"${a}-b\"}', NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('2j3i3kzpzfc000', NULL, 'm6o3aghqfrk00', NULL, NULL, NULL, NULL, NULL, NULL, '29cput2xohwk00', '5pxuhe3ufy8000');
INSERT INTO `eda_flow_node_data` VALUES ('2jo1w8462ia000', NULL, '34224hnftcm000', NULL, NULL, NULL, NULL, NULL, NULL, '2ikmbc80e58000', '2cqgzb9sjz0g00');
INSERT INTO `eda_flow_node_data` VALUES ('2lshxmfvcic000', '开始', 'giu8wqaa68000', 1, '80px', '180px', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('2miqbo2gqt0000', NULL, '5j8nx6kzbp4000', NULL, NULL, NULL, NULL, NULL, NULL, '5rsmqlkp7fk000', '39uiocn1szc000');
INSERT INTO `eda_flow_node_data` VALUES ('2mu3x58m5qu000', NULL, '41as9fmikt8000', NULL, NULL, NULL, NULL, NULL, NULL, '5bhkyaxsw7o000', 'bgid30y68q000');
INSERT INTO `eda_flow_node_data` VALUES ('2nlknl5lncs000', '输出', '41fwmav2z3k000', 4, '180px', '360px', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('2ryt3dgbcxa000', '输出2', 'm6o3aghqfrk00', 4, '195px', '295px', NULL, NULL, '{\"a\": \"${a}\"}', NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('2s6c2p0lca2000', '输出', '5rsmqlkp7fk000', 4, '175px', '675px', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('2svq189ky1c000', '延时器', 'bx6f3o3rxmg00', 6, '210px', '400px', NULL, '{\"delay\": \"10,SECONDS\"}', '{\"a\": \"xxx\"}', NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('2tnh6ti7qk2000', NULL, '3xgebwpxll6000', NULL, NULL, NULL, NULL, NULL, NULL, '17hko8p19sqk00', '4ye2ivm0ysg000');
INSERT INTO `eda_flow_node_data` VALUES ('2uydhlhqpug000', '延时器', '2dzjjpdtijb400', 6, '250px', '420px', NULL, '{\"delay\": \"2,SECONDS\"}', '{\"result\": \"${httpResult.result.$0.type}\", \"timestamp\": \"${timestamp}\"}', NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('313b3a0dn14000', '输出', '1p8nhh3c4aio00', 4, '105px', '740px', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('32f9ixt25ii000', '输出', 'xu7fsb4whw000', 4, '140px', '620px', '打印出上一节点传递的参数', NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('32fdry9yng6000', '输出3', '5j8nx6kzbp4000', 4, '270px', '795px', NULL, NULL, '{\"out\": \"${d}\"}', NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('37vdswbm6p4000', NULL, 'bx6f3o3rxmg00', NULL, NULL, NULL, NULL, NULL, NULL, '2svq189ky1c000', '1vnqd0ukj7gg00');
INSERT INTO `eda_flow_node_data` VALUES ('39uiocn1szc000', '发送请求', '5j8nx6kzbp4000', 3, '295px', '375px', '请求节点类型列表${b}', '{\"url\": \"http://localhost:8081/${q}/node/type\", \"header\": \"Accept:application/json\", \"method\": \"GET\", \"params\": \"limit=3\"}', '{\"b\": \"${a}\"}', NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('39z32j316d0000', NULL, '2dzjjpdtijb400', NULL, NULL, NULL, NULL, NULL, NULL, 'q7b6o1iy3cg00', '2uydhlhqpug000');
INSERT INTO `eda_flow_node_data` VALUES ('3bq2xtvor62000', '延时器', '4ccr0y2xtwu000', 6, '180px', '260px', NULL, '{\"delay\": \"3,SECONDS\"}', '{\"b\": \"b-${a}\"}', NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('3d9ijmn9nuk000', NULL, 'bx6f3o3rxmg00', NULL, NULL, NULL, NULL, NULL, NULL, '21jfwadp7tcw00', '2svq189ky1c000');
INSERT INTO `eda_flow_node_data` VALUES ('3dxtemcmplk000', NULL, 'm6o3aghqfrk00', NULL, NULL, NULL, NULL, NULL, NULL, '2ryt3dgbcxa000', 'qfki3k8xu4000');
INSERT INTO `eda_flow_node_data` VALUES ('3dyid08c2ei000', '延时1秒', 'm6o3aghqfrk00', 6, '310px', '90px', NULL, '{\"delay\": \"1,SECONDS\"}', '{\"a\": \"${a}\"}', NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('3e4j6g0omua000', '输出', '3uldr6qij5o000', 4, '165px', '645px', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('3fu8m0ttgiy000', NULL, 'mk3eo1ewkk000', NULL, NULL, NULL, NULL, NULL, NULL, '69mzkmq9x4g00', '48cfy4dsudg000');
INSERT INTO `eda_flow_node_data` VALUES ('3g6glxdv31k000', '延时4秒', 'm6o3aghqfrk00', 6, '310px', '730px', NULL, '{\"delay\": \"4,SECONDS\"}', '{\"a\": \"${a}\"}', NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('3jawstqlogg000', NULL, '4ccr0y2xtwu000', NULL, NULL, NULL, NULL, NULL, NULL, '4tw8gi7jrqu000', '3bq2xtvor62000');
INSERT INTO `eda_flow_node_data` VALUES ('3k37rvwkv9c000', NULL, 'm6o3aghqfrk00', NULL, NULL, NULL, NULL, NULL, NULL, '3le77waupza000', '1js10mqz518g00');
INSERT INTO `eda_flow_node_data` VALUES ('3le77waupza000', '定时器2', 'm6o3aghqfrk00', 2, '95px', '730px', NULL, '{\"times\": \"1\", \"period\": \"3,SECONDS\", \"timestamp\": \"HH:mm:ss\"}', '{\"a\": \"xxx-4\"}', NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('3m52bl5i5pc000', '开始-a', 'mk3eo1ewkk000', 1, '60px', '280px', NULL, NULL, '{\"a\": \"xxx\"}', NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('3ndda6kci5c000', '输出1', '5j8nx6kzbp4000', 4, '360px', '795px', NULL, NULL, '{\"out\": \"${b}\"}', NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('3rga9qwrek800', '输出', '58zsx5ui7go000', 4, '40px', '520px', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('3sssatqloli000', NULL, 'xu7fsb4whw000', NULL, NULL, NULL, NULL, NULL, NULL, '1wb2ubmere8w00', '32f9ixt25ii000');
INSERT INTO `eda_flow_node_data` VALUES ('3sz5q32f3b4000', NULL, '5j8nx6kzbp4000', NULL, NULL, NULL, NULL, NULL, NULL, '39uiocn1szc000', 'urixfauc1j400');
INSERT INTO `eda_flow_node_data` VALUES ('3tx74wv9vt8000', '输出', '2dzjjpdtijb400', 4, '160px', '645px', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('3u8e1n8jh8c000', NULL, 'mk3eo1ewkk000', NULL, NULL, NULL, NULL, NULL, NULL, '48cfy4dsudg000', '5pyn5hp1o44000');
INSERT INTO `eda_flow_node_data` VALUES ('3uty3few80w000', '延时器', '5j8nx6kzbp4000', 6, '155px', '595px', NULL, '{\"delay\": \"5,SECONDS\"}', '{\"d\": \"d-${b}\"}', NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('3wa09thtp2w000', '输出', '48r380g6qc4000', 4, '105px', '650px', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('3xgebwpxll6000', NULL, '5rsmqlkp7fk000', NULL, NULL, NULL, NULL, NULL, NULL, '41fwmav2z3k000', '5j8nx6kzbp4000');
INSERT INTO `eda_flow_node_data` VALUES ('3xmkyojs654000', 'HTTP请求', '2dzjjpdtijb400', 3, '160px', '190px', NULL, '{\"url\": \"http://localhost:8081/${q}/node/type\", \"header\": \"Accept:application/json\", \"method\": \"GET\", \"params\": \"limit=1\"}', '{\"timestamp\": \"${timestamp}\"}', NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('41amqxi2fmg000', NULL, 'm6o3aghqfrk00', NULL, NULL, NULL, NULL, NULL, NULL, '1js10mqz518g00', '3g6glxdv31k000');
INSERT INTO `eda_flow_node_data` VALUES ('41fwmav2z3k000', '定时器1', '5rsmqlkp7fk000', 2, '265px', '440px', NULL, '{\"times\": \"2\", \"period\": \"3,SECONDS\"}', '{\"b\": \"b-${a}-1\"}', NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('463pzntyc0u000', NULL, '3osi3b97pa4000', NULL, NULL, NULL, NULL, NULL, NULL, 'zzlzi607jv400', '5mefz3prh1s000');
INSERT INTO `eda_flow_node_data` VALUES ('46d9ughr1fs000', NULL, 'giu8wqaa68000', NULL, NULL, NULL, NULL, NULL, NULL, '2lshxmfvcic000', '4twz7kylrri000');
INSERT INTO `eda_flow_node_data` VALUES ('486521g7uns000', NULL, 'mk3eo1ewkk000', NULL, NULL, NULL, NULL, NULL, NULL, '5pyn5hp1o44000', '4cplvt9y7wy000');
INSERT INTO `eda_flow_node_data` VALUES ('48cfy4dsudg000', '输出-b', 'mk3eo1ewkk000', 4, '175px', '525px', NULL, NULL, '{\"c\": \"${b}\"}', NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('4ccnzr06zuo000', '开始', '48r380g6qc4000', 1, '80px', '200px', NULL, NULL, '{\"a\": \"xxx-a\", \"b\": \"xxx-b\"}', NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('4ccr0y2xtwu000', NULL, '5rsmqlkp7fk000', NULL, NULL, NULL, NULL, NULL, NULL, '1ces2o3r4yzk00', '41fwmav2z3k000');
INSERT INTO `eda_flow_node_data` VALUES ('4cplvt9y7wy000', '输出d', 'mk3eo1ewkk000', 4, '60px', '770px', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('4k9axq5cv7o000', '定时器', '58zsx5ui7go000', 2, '60px', '260px', NULL, '{\"times\": \"3\", \"period\": \"2,SECONDS\", \"timestamp\": \"HH:mm:ss\"}', '{\"x\": \"xxx\"}', NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('4lnyjpqmn0q000', NULL, '6a088jl98eg00', NULL, NULL, NULL, NULL, NULL, NULL, '2eqcm8iq2mm800', '1vym9mvg117k00');
INSERT INTO `eda_flow_node_data` VALUES ('4m3ei749g2k000', '输出', '58zsx5ui7go000', 4, '100px', '695px', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('4nhpgowo3mg000', '延时器', '3uldr6qij5o000', 6, '60px', '435px', '当流程发生异常时，整个流程会中止运行，节点会停滞在当前状态', '{\"delay\": \"8,SECONDS\"}', NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('4qhafzihvwo000', NULL, '48r380g6qc4000', NULL, NULL, NULL, NULL, NULL, NULL, 'wvzmjwfdtuo00', '3wa09thtp2w000');
INSERT INTO `eda_flow_node_data` VALUES ('4rdsenizj2g000', NULL, '2dzjjpdtijb400', NULL, NULL, NULL, NULL, NULL, NULL, '5hzea2bl9lo000', '3xmkyojs654000');
INSERT INTO `eda_flow_node_data` VALUES ('4rwm617s1r0000', NULL, '58zsx5ui7go000', NULL, NULL, NULL, NULL, NULL, NULL, '4k9axq5cv7o000', '3rga9qwrek800');
INSERT INTO `eda_flow_node_data` VALUES ('4st4lxjwle0000', NULL, 'm6o3aghqfrk00', NULL, NULL, NULL, NULL, NULL, NULL, '4yj8cf9mgxg000', '29cput2xohwk00');
INSERT INTO `eda_flow_node_data` VALUES ('4t6ekm5bvmo000', 'HTTP请求', '1p8nhh3c4aio00', 3, '215px', '305px', NULL, '{\"url\": \"http://localhost:8081/${q}/node/type\", \"header\": \"Accept:application/json\", \"method\": \"GET\", \"params\": \"limit=1\"}', '{\"b\": \"${a}-b\"}', NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('4tnr4rsnzdw000', 'HTTP请求', '3uldr6qij5o000', 3, '285px', '435px', NULL, '{\"url\": \"http://localhost:8081/test\", \"method\": \"GET\"}', NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('4tw8gi7jrqu000', '开始', '4ccr0y2xtwu000', 1, '40px', '80px', NULL, NULL, '{\"a\": \"123\"}', NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('4twz7kylrri000', '输出', 'giu8wqaa68000', 4, '160px', '360px', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('4v1wf1va5w6000', NULL, '48r380g6qc4000', NULL, NULL, NULL, NULL, NULL, NULL, '4ccnzr06zuo000', 'wvzmjwfdtuo00');
INSERT INTO `eda_flow_node_data` VALUES ('4vf94lchs1s000', '开始', '1p8nhh3c4aio00', 1, '105px', '115px', NULL, NULL, '{\"a\": \"xxx\", \"q\": \"api/v1\"}', NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('4ye2ivm0ysg000', '输出', '3xgebwpxll6000', 4, '150px', '330px', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('4yj8cf9mgxg000', '定时器1', 'm6o3aghqfrk00', 2, '95px', '505px', NULL, '{\"times\": \"1\", \"period\": \"2,SECONDS\", \"timestamp\": \"HH:mm:ss\"}', '{\"a\": \"xxx-3\"}', NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('52y7k5m4788000', '总输出', 'm6o3aghqfrk00', 4, '515px', '405px', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('54cqiur37zo000', '开始', '41as9fmikt8000', 1, '115px', '195px', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('58zsx5ui7go000', '输出', 'aev4yh5kjgk00', 4, '185px', '270px', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('5apz8nnsgpc000', '开始', '34224hnftcm000', 1, '100px', '160px', NULL, NULL, '{\"a\": \"xxx\", \"q\": \"api/v1\"}', NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('5bhkyaxsw7o000', '延时器', '41as9fmikt8000', 6, '240px', '280px', '中止运行后，鼠标悬停在红叉图标上可查看流程中止原因', '{\"delay\": \"10,SECONDS\"}', '{\"a\": \"xxx\"}', NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('5cw6qbksnl0000', NULL, '58zsx5ui7go000', NULL, NULL, NULL, NULL, NULL, NULL, '4k9axq5cv7o000', '1ctdtf4xdj5s00');
INSERT INTO `eda_flow_node_data` VALUES ('5d2fusmqm6k000', NULL, '2dzjjpdtijb400', NULL, NULL, NULL, NULL, NULL, NULL, 'q7b6o1iy3cg00', '3tx74wv9vt8000');
INSERT INTO `eda_flow_node_data` VALUES ('5grdto48m4w000', NULL, '41as9fmikt8000', NULL, NULL, NULL, NULL, NULL, NULL, '54cqiur37zo000', '5bhkyaxsw7o000');
INSERT INTO `eda_flow_node_data` VALUES ('5hnp0jfza90000', NULL, '3uldr6qij5o000', NULL, NULL, NULL, NULL, NULL, NULL, '4nhpgowo3mg000', '2bk45mph7xno00');
INSERT INTO `eda_flow_node_data` VALUES ('5hzea2bl9lo000', '定时器', '2dzjjpdtijb400', 2, '65px', '190px', NULL, '{\"times\": \"3\", \"period\": \"2,SECONDS\", \"timestamp\": \"HH:mm:ss\"}', '{\"q\": \"api/v1\"}', NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('5j8nx6kzbp4000', '输出', '5rsmqlkp7fk000', 4, '285px', '670px', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('5mefz3prh1s000', '输出', '3osi3b97pa4000', 4, '205px', '620px', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('5mlqqqj6l4k000', NULL, '2dzjjpdtijb400', NULL, NULL, NULL, NULL, NULL, NULL, '2uydhlhqpug000', '27jwy17xpzr400');
INSERT INTO `eda_flow_node_data` VALUES ('5pxuhe3ufy8000', '延时3秒', 'm6o3aghqfrk00', 6, '310px', '505px', NULL, '{\"delay\": \"3,SECONDS\"}', '{\"a\": \"${a}\"}', NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('5pyn5hp1o44000', '输出-c', 'mk3eo1ewkk000', 4, '175px', '770px', NULL, NULL, '{\"d\": \"${c}\"}', NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('5q4zicl8o9g000', NULL, 'm6o3aghqfrk00', NULL, NULL, NULL, NULL, NULL, NULL, '5pxuhe3ufy8000', '52y7k5m4788000');
INSERT INTO `eda_flow_node_data` VALUES ('5rsmqlkp7fk000', '开始', '5j8nx6kzbp4000', 1, '155px', '135px', NULL, NULL, '{\"a\": \"xxx\", \"q\": \"api/v1\"}', NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('69mzkmq9x4g00', '输出a', 'mk3eo1ewkk000', 4, '175px', '280px', NULL, NULL, '{\"b\": \"${a}\"}', NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('aev4yh5kjgk00', NULL, '5j8nx6kzbp4000', NULL, NULL, NULL, NULL, NULL, NULL, '39uiocn1szc000', '3uty3few80w000');
INSERT INTO `eda_flow_node_data` VALUES ('bgid30y68q000', '输出', '41as9fmikt8000', 4, '240px', '520px', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('brjl0erogio00', '开始', '3osi3b97pa4000', 1, '60px', '240px', NULL, NULL, '{\"a\": \"xxx-a\"}', NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('bx6f3o3rxmg00', NULL, '5rsmqlkp7fk000', NULL, NULL, NULL, NULL, NULL, NULL, '1ces2o3r4yzk00', '1d0p9dhu59gg00');
INSERT INTO `eda_flow_node_data` VALUES ('donj4a3saso00', '输出2', '5j8nx6kzbp4000', 4, '455px', '800px', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('ftcvzew502o00', NULL, 'm6o3aghqfrk00', NULL, NULL, NULL, NULL, NULL, NULL, '3dyid08c2ei000', '52y7k5m4788000');
INSERT INTO `eda_flow_node_data` VALUES ('giu8wqaa68000', NULL, '5j8nx6kzbp4000', NULL, NULL, NULL, NULL, NULL, NULL, '3uty3few80w000', '32fdry9yng6000');
INSERT INTO `eda_flow_node_data` VALUES ('pjffvx258ww00', NULL, '3uldr6qij5o000', NULL, NULL, NULL, NULL, NULL, NULL, '4tnr4rsnzdw000', '1n5cxdr0tmdc00');
INSERT INTO `eda_flow_node_data` VALUES ('q7b6o1iy3cg00', '解析器', '2dzjjpdtijb400', 5, '160px', '420px', NULL, '{\"parseKey\": \"httpResult.result.$0.type\"}', '{\"timestamp\": \"${timestamp}\"}', NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('qfki3k8xu4000', '延时2秒', 'm6o3aghqfrk00', 6, '310px', '295px', NULL, '{\"delay\": \"2,SECONDS\"}', '{\"a\": \"${a}\"}', NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('rt5ae2sgdog00', NULL, '1p8nhh3c4aio00', NULL, NULL, NULL, NULL, NULL, NULL, '4t6ekm5bvmo000', 'ui0qaqbv8qo00');
INSERT INTO `eda_flow_node_data` VALUES ('t0l3rxz2w4w00', '定时器', 'aev4yh5kjgk00', 2, '90px', '105px', NULL, '{\"times\": \"5\", \"period\": \"5,SECONDS\"}', '{\"a\": \"xxx\"}', NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('tfp7f13eolc00', NULL, '3uldr6qij5o000', NULL, NULL, NULL, NULL, NULL, NULL, '1rdi0c65pyhs00', '3e4j6g0omua000');
INSERT INTO `eda_flow_node_data` VALUES ('ui0qaqbv8qo00', '解析器', '1p8nhh3c4aio00', 5, '215px', '560px', NULL, '{\"parseKey\": \"httpResult.result.$0.type\"}', '{\"c\": \"${b}-c\"}', NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('urixfauc1j400', '解析器', '5j8nx6kzbp4000', 5, '510px', '515px', NULL, '{\"parseKey\": \"httpResult.result.$1.type,params.b\"}', '{\"c\": \"xxx-${b}-c\"}', NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('w2t1xrmw33k00', NULL, '4ccr0y2xtwu000', NULL, NULL, NULL, NULL, NULL, NULL, '3bq2xtvor62000', '2c69av6r0cu800');
INSERT INTO `eda_flow_node_data` VALUES ('w6jel6hfev400', NULL, '5j8nx6kzbp4000', NULL, NULL, NULL, NULL, NULL, NULL, '39uiocn1szc000', '3ndda6kci5c000');
INSERT INTO `eda_flow_node_data` VALUES ('wd5ytd6iqsw00', NULL, '1p8nhh3c4aio00', NULL, NULL, NULL, NULL, NULL, NULL, 'ui0qaqbv8qo00', '313b3a0dn14000');
INSERT INTO `eda_flow_node_data` VALUES ('wuklet6bdow00', '开始', '41fwmav2z3k000', 1, '100px', '160px', NULL, NULL, '{\"a\": \"xxx\"}', NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('wvzmjwfdtuo00', '解析器', '48r380g6qc4000', 5, '125px', '415px', '解析上一节点的参数并传递至下一节点', '{\"parseKey\": \"params.a\"}', '{\"p\": \"${b}\"}', NULL, NULL);
INSERT INTO `eda_flow_node_data` VALUES ('x7ag6qo8bpc00', NULL, 'm6o3aghqfrk00', NULL, NULL, NULL, NULL, NULL, NULL, '1g5eivmtkbi800', '1f2mfwal1o2o00');
INSERT INTO `eda_flow_node_data` VALUES ('zzlzi607jv400', '延时器', '3osi3b97pa4000', 6, '145px', '415px', NULL, '{\"delay\": \"3,SECONDS\"}', '{\"b\": \"${a}\"}', NULL, NULL);

-- ----------------------------
-- Table structure for eda_flow_node_type
-- ----------------------------
DROP TABLE IF EXISTS `eda_flow_node_type`;
CREATE TABLE `eda_flow_node_type`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `type` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '节点类型',
  `type_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '名称',
  `svg` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '图标',
  `background` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '背景色',
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of eda_flow_node_type
-- ----------------------------
INSERT INTO `eda_flow_node_type` VALUES (1, 'start', '开始', '/svg/start.svg', 'rgba(0, 128, 0, 0.2)', '在一个流程中，开始节点作为该流程的触发起始节点');
INSERT INTO `eda_flow_node_type` VALUES (2, 'timer', '定时器', '/svg/timer.svg', 'rgba(0, 128, 0, 0.2)', '可作为起始节点，用于定时触发流程周期性执行；若作为非起始节点，则由上游节点进行触发执行');
INSERT INTO `eda_flow_node_type` VALUES (3, 'http', 'HTTP请求', '/svg/http.svg', '#e6b23c', '可以发起HTTP请求，支持所有的请求类型，支持携带各种请求参数信息以及token等请求头信息');
INSERT INTO `eda_flow_node_type` VALUES (4, 'output', '输出', '/svg/output.svg', 'rgb(135, 169, 128)', '输出节点，可展示其上游节点的输出参数信息');
INSERT INTO `eda_flow_node_type` VALUES (5, 'parser', '解析器', '/svg/parser.svg', '#e6b23c', '用于解析上游节点的输出参数，获取用户需要的参数信息');
INSERT INTO `eda_flow_node_type` VALUES (6, 'delay', '延时器', '/svg/delay.svg', 'rgba(0, 128, 0, 0.2)', '延时节点，可设定延迟时间，用于延迟其下游节点的运行');

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
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of eda_flow_node_type_param
-- ----------------------------
INSERT INTO `eda_flow_node_type_param` VALUES (1, 3, 'url', 'URL', 1, 'input', NULL, 'http://');
INSERT INTO `eda_flow_node_type_param` VALUES (2, 3, 'method', '请求方式', 1, 'select', 'GET,POST,PUT,DELETE,HEAD,OPTIONS,TRACE,PATCH', NULL);
INSERT INTO `eda_flow_node_type_param` VALUES (3, 3, 'params', '请求参数', 0, 'input', NULL, 'a=1&b=2');
INSERT INTO `eda_flow_node_type_param` VALUES (4, 3, 'body', '请求内容', 0, 'input', NULL, '{a:1,b:\'2\'}');
INSERT INTO `eda_flow_node_type_param` VALUES (5, 3, 'header', '请求头', 0, 'input', NULL, 'token:xxx,Accept:application/json');
INSERT INTO `eda_flow_node_type_param` VALUES (6, 2, 'period', '执行周期', 1, 'select', 'SECONDS,MINUTES,HOURS,DAYS,CRON', '1,MINUTES');
INSERT INTO `eda_flow_node_type_param` VALUES (7, 2, 'times', '执行次数', 1, 'input', NULL, '输入0表示不限制次数');
INSERT INTO `eda_flow_node_type_param` VALUES (8, 2, 'timestamp', '输出时间', 0, 'select', 'timestamp,yyyy-MM-dd HH:mm:ss,yyyy-MM-dd\'T\'HH:mm:ss\'Z\',yyyyMMdd,HHmmss,HH:mm:ss,yyyy-MM-dd,yyyy/MM/dd,yyyyMMddHHmmss', NULL);
INSERT INTO `eda_flow_node_type_param` VALUES (9, 5, 'parseKey', '解析参数', 1, 'input', NULL, 'httpResult.$0.name,params.a');
INSERT INTO `eda_flow_node_type_param` VALUES (10, 6, 'delay', '延迟时间', 1, 'select', 'MILLISECONDS,SECONDS,MINUTES,HOURS,DAYS', '5,SECONDS');

SET FOREIGN_KEY_CHECKS = 1;
