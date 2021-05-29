-- ----------------------------
-- Table structure for job_execution_log
-- ----------------------------
DROP TABLE IF EXISTS `job_execution_log`;
CREATE TABLE `job_execution_log`  (
  `id` varchar(40)  NOT NULL,
  `job_name` varchar(100)  NULL DEFAULT NULL COMMENT '作业名称',
  `task_id` varchar(1000)  NULL DEFAULT NULL COMMENT '任务名称,每次作业运行生成新任务',
  `hostname` varchar(255)  NULL DEFAULT NULL COMMENT '主机名称',
  `ip` varchar(50)  NULL DEFAULT NULL COMMENT '主机IP',
  `sharding_item` int NULL DEFAULT NULL COMMENT '分片项',
  `execution_source` varchar(20)  NULL DEFAULT NULL COMMENT '作业执行来源。可选值为NORMAL_TRIGGER, MISFIRE, FAILOVER',
  `failure_cause` varchar(2000)  NULL DEFAULT NULL COMMENT '执行失败原因',
  `is_success` bit(1) NULL DEFAULT NULL COMMENT '是否执行成功',
  `start_time` datetime NULL DEFAULT NULL COMMENT '作业开始执行时间',
  `complete_time` datetime NULL DEFAULT NULL COMMENT '作业结束执行时间'
);

-- ----------------------------
-- Table structure for job_status_trace_log
-- ----------------------------
DROP TABLE IF EXISTS `job_status_trace_log`;
CREATE TABLE `job_status_trace_log`  (
  `id` varchar(40)  NOT NULL COMMENT '主键',
  `job_name` varchar(100)  NULL DEFAULT NULL COMMENT '作业名称',
  `original_task_id` varchar(1000)  NULL DEFAULT NULL COMMENT '原任务名称',
  `task_id` varchar(1000)  NULL DEFAULT NULL COMMENT '任务名称',
  `slave_id` varchar(1000)  NULL DEFAULT NULL COMMENT '执行作业服务器的名称，Lite版本为服务器的IP地址，Cloud版本为Mesos执行机主键',
  `source` varchar(50)  NULL DEFAULT NULL COMMENT '任务执行源，可选值为CLOUD_SCHEDULER, CLOUD_EXECUTOR, LITE_EXECUTOR',
  `execution_type` varchar(20)  NULL DEFAULT NULL COMMENT '任务执行类型，可选值为NORMAL_TRIGGER, MISFIRE, FAILOVER',
  `sharding_item` varchar(255)  NULL DEFAULT NULL COMMENT '分片项集合，多个分片项以逗号分隔',
  `state` varchar(20)  NULL DEFAULT NULL COMMENT '任务执行状态，可选值为TASK_STAGING, TASK_RUNNING, TASK_FINISHED, TASK_KILLED, TASK_LOST, TASK_FAILED, TASK_ERROR',
  `message` varchar(2000)  NULL DEFAULT NULL COMMENT '相关信息',
  `creation_time` datetime NULL DEFAULT NULL COMMENT '记录创建时间'
);
