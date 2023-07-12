CREATE DATABASE IF NOT EXISTS game_fun DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE game_fun;
SET NAMES utf8;

SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for ap_user
-- ----------------------------
DROP TABLE IF EXISTS `ap_user`;
CREATE TABLE `ap_user`
(
    `id`                         int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
    `name`                       varchar(20) COLLATE utf8mb4_unicode_ci  DEFAULT NULL COMMENT '用户名',
    `password`                   varchar(32) COLLATE utf8mb4_unicode_ci  DEFAULT NULL COMMENT '密码，BCryptPasswordEncoder进行密码加密',
    `phone`                      varchar(11) COLLATE utf8mb4_unicode_ci  DEFAULT NULL COMMENT '手机号',
    `image`                      varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '头像',
    `sex`                        tinyint(1) unsigned                     DEFAULT NULL COMMENT '0 男\r\n            1 女\r\n            2 未知',
    `is_identity_authentication` tinyint(1)                              DEFAULT NULL COMMENT '是否身份认证',
    `status`                     tinyint(1) unsigned                     DEFAULT NULL COMMENT '1正常\r\n            0锁定',
    `flag`                       tinyint(1) unsigned                     DEFAULT NULL COMMENT '0 普通用户\r\n            1 博主\r\n            2 大V',
    `created_time`               datetime                                DEFAULT NULL COMMENT '注册时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 7
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci
  ROW_FORMAT = DYNAMIC COMMENT ='APP用户信息表';



-- ----------------------------
-- Table structure for ap_user_fan
-- ----------------------------
DROP TABLE IF EXISTS `ap_user_fan`;
CREATE TABLE `ap_user_fan`
(
    `id`                int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
    `user_id`           int(11) unsigned                       DEFAULT NULL COMMENT '用户ID',
    `fans_id`           int(11) unsigned                       DEFAULT NULL COMMENT '粉丝ID',
    `fans_name`         varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '粉丝昵称',
    `level`             tinyint(1) unsigned                    DEFAULT NULL COMMENT '粉丝忠实度\r\n            0 普通\r\n            1 铜牌\r\n            2 银牌\r\n            3 金牌\r\n            4 老铁',
    `created_time`      datetime                               DEFAULT NULL COMMENT '创建时间',
    `is_display`        tinyint(1) unsigned                    DEFAULT NULL COMMENT '是否可见我动态',
    `is_shield_letter`  tinyint(1) unsigned                    DEFAULT NULL COMMENT '是否屏蔽私信',
    `is_shield_comment` tinyint(1) unsigned                    DEFAULT NULL COMMENT '是否屏蔽评论',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci
  ROW_FORMAT = DYNAMIC COMMENT ='APP用户粉丝信息表';

-- ----------------------------
-- Records of ap_user_fan
-- ----------------------------

-- ----------------------------
-- Table structure for ap_user_follow
-- ----------------------------
DROP TABLE IF EXISTS `ap_user_follow`;
CREATE TABLE `ap_user_follow`
(
    `id`           int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
    `user_id`      int(11) unsigned                       DEFAULT NULL COMMENT '用户ID',
    `follow_id`    int(11) unsigned                       DEFAULT NULL COMMENT '关注作者ID',
    `follow_name`  varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '粉丝昵称',
    `level`        tinyint(1) unsigned                    DEFAULT NULL COMMENT '关注度\r\n            0 偶尔感兴趣\r\n            1 一般\r\n            2 经常\r\n            3 高度',
    `is_notice`    tinyint(1) unsigned                    DEFAULT NULL COMMENT '是否动态通知',
    `created_time` datetime                               DEFAULT NULL COMMENT '创建时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci
  ROW_FORMAT = DYNAMIC COMMENT ='APP用户关注信息表';

-- ----------------------------
-- Records of ap_user_follow
-- ----------------------------

-- ----------------------------
-- Table structure for ap_user_realname
-- ----------------------------
DROP TABLE IF EXISTS `ap_user_realname`;
CREATE TABLE `ap_user_realname`
(
    `id`            int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
    `user_id`       int(11) unsigned                        DEFAULT NULL COMMENT '账号ID',
    `name`          varchar(20) CHARACTER SET utf8mb4       DEFAULT NULL COMMENT '用户名称',
    `idno`          varchar(20) COLLATE utf8mb4_unicode_ci  DEFAULT NULL COMMENT '资源名称',
    `font_image`    varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '正面照片',
    `back_image`    varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '背面照片',
    `hold_image`    varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '手持照片',
    `live_image`    varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '活体照片',
    `status`        tinyint(1) unsigned                     DEFAULT NULL COMMENT '状态\r\n            0 创建中\r\n            1 待审核\r\n            2 审核失败\r\n            9 审核通过',
    `reason`        varchar(50) COLLATE utf8mb4_unicode_ci  DEFAULT NULL COMMENT '拒绝原因',
    `created_time`  datetime                                DEFAULT NULL COMMENT '创建时间',
    `submited_time` datetime                                DEFAULT NULL COMMENT '提交时间',
    `updated_time`  datetime                                DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 6
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci
  ROW_FORMAT = DYNAMIC COMMENT ='APP实名认证信息表';
