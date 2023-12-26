SET NAMES utf8mb4;
SET
    FOREIGN_KEY_CHECKS = 0;
CREATE
    database IF NOT EXISTS db2019;
USE
    db2019;

-- ----------------------------
-- Table structure for payment
-- ----------------------------
DROP TABLE IF EXISTS `payment`;
CREATE TABLE `payment`
(
    `id`     bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `serial` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '支付流水号',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci COMMENT = '支付表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of payment
-- ----------------------------
INSERT INTO `payment`
VALUES (31, '尚硅谷111');
INSERT INTO `payment`
VALUES (32, 'atguigu002');
INSERT INTO `payment`
VALUES (34, 'atguigu002');
INSERT INTO `payment`
VALUES (35, 'atguigu002');

SET
    FOREIGN_KEY_CHECKS = 1;

-- - seata_order
CREATE
    DATABASE IF NOT EXISTS seata_order;
USE
    seata_order;
DROP TABLE IF EXISTS `t_order`;
CREATE TABLE `t_order`
(
    `id`        bigint(11) NOT NULL AUTO_INCREMENT,
    `user_id`    bigint(20)     DEFAULT NULL COMMENT '用户id',
    `product_id` bigint(11)     DEFAULT NULL COMMENT '产品id',
    `count`      int(11)        DEFAULT NULL COMMENT '数量',
    `money`      decimal(11, 0) DEFAULT NULL COMMENT '金额',
    `status`     int(1)         DEFAULT NULL COMMENT '订单状态:  0:创建中 1:已完结',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci COMMENT = '订单表'
  ROW_FORMAT = Dynamic;

DROP TABLE IF EXISTS `undo_log`;
CREATE TABLE `undo_log`
(
    `id`            bigint(20)   NOT NULL AUTO_INCREMENT,
    `branch_id`     bigint(20)   NOT NULL,
    `xid`           varchar(100) NOT NULL,
    `context`       varchar(128) NOT NULL,
    `rollback_info` longblob     NOT NULL,
    `log_status`    int(11)      NOT NULL,
    `log_created`   datetime     NOT NULL,
    `log_modified`  datetime     NOT NULL,
    `ext`           varchar(100) DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `ux_undo_log` (`xid`, `branch_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8;

create
    database IF NOT EXISTS seata_storage;
USE
    seata_storage;
DROP TABLE IF EXISTS `t_storage`;
CREATE TABLE `t_storage`
(
    `id`        bigint(11) NOT NULL AUTO_INCREMENT,
    `product_id` bigint(11) DEFAULT NULL COMMENT '产品id',
    `total`      int(11)    DEFAULT NULL COMMENT '总库存',
    `used`       int(11)    DEFAULT NULL COMMENT '已用库存',
    `residue`    int(11)    DEFAULT NULL COMMENT '剩余库存',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci COMMENT = '库存'
  ROW_FORMAT = Dynamic;
INSERT INTO `t_storage`
VALUES (1, 1, 100, 0, 100);

DROP TABLE IF EXISTS `undo_log`;
CREATE TABLE `undo_log`
(
    `id`            bigint(20)   NOT NULL AUTO_INCREMENT,
    `branch_id`     bigint(20)   NOT NULL,
    `xid`           varchar(100) NOT NULL,
    `context`       varchar(128) NOT NULL,
    `rollback_info` longblob     NOT NULL,
    `log_status`    int(11)      NOT NULL,
    `log_created`   datetime     NOT NULL,
    `log_modified`  datetime     NOT NULL,
    `ext`           varchar(100) DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `ux_undo_log` (`xid`, `branch_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8;

CREATE
    database IF NOT EXISTS seata_account;
USE
    seata_account;
DROP TABLE IF EXISTS `t_account`;
CREATE TABLE `t_account`
(
    `id`      bigint(11) NOT NULL COMMENT 'id',
    `user_id` bigint(11)     DEFAULT NULL COMMENT '用户id',
    `total`   decimal(10, 0) DEFAULT NULL COMMENT '总额度',
    `used`    decimal(10, 0) DEFAULT NULL COMMENT '已用余额',
    `residue` decimal(10, 0) DEFAULT NULL COMMENT '剩余可用额度',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci COMMENT = '账户表'
  ROW_FORMAT = Dynamic;

INSERT INTO `t_account`
VALUES (1, 1, 1000, 0, 1000);

DROP TABLE IF EXISTS `undo_log`;
CREATE TABLE `undo_log`
(
    `id`            bigint(20)   NOT NULL AUTO_INCREMENT,
    `branch_id`     bigint(20)   NOT NULL,
    `xid`           varchar(100) NOT NULL,
    `context`       varchar(128) NOT NULL,
    `rollback_info` longblob     NOT NULL,
    `log_status`    int(11)      NOT NULL,
    `log_created`   datetime     NOT NULL,
    `log_modified`  datetime     NOT NULL,
    `ext`           varchar(100) DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `ux_undo_log` (`xid`, `branch_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8;