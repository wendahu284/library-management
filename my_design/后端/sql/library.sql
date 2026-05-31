-- ============================================================
-- 图书管理系统 - 数据库初始化脚本
-- 版本: 1.0.0
-- 数据库: MySQL 8.0+
-- ============================================================

-- 创建数据库
CREATE DATABASE IF NOT EXISTS `library_db`
    DEFAULT CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE `library_db`;

-- ============================================================
-- 1. 用户表 (区分管理员与读者角色)
-- ============================================================
DROP TABLE IF EXISTS `borrow_record`;
DROP TABLE IF EXISTS `book`;
DROP TABLE IF EXISTS `category`;
DROP TABLE IF EXISTS `announcement`;
DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
    `id`            BIGINT          NOT NULL AUTO_INCREMENT  COMMENT '用户主键ID',
    `username`      VARCHAR(50)     NOT NULL                 COMMENT '登录用户名',
    `password`      VARCHAR(255)    NOT NULL                 COMMENT '登录密码（BCrypt加密存储）',
    `real_name`     VARCHAR(50)     DEFAULT NULL             COMMENT '真实姓名',
    `phone`         VARCHAR(20)     DEFAULT NULL             COMMENT '联系电话',
    `email`         VARCHAR(100)    DEFAULT NULL             COMMENT '电子邮箱',
    `role`          TINYINT         NOT NULL DEFAULT 0       COMMENT '角色：0=普通读者 1=管理员',
    `status`        TINYINT         NOT NULL DEFAULT 1       COMMENT '账号状态：0=已禁用 1=正常',
    `create_time`   DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`   DATETIME        DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`),
    KEY `idx_role` (`role`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- ============================================================
-- 6. 权限管理表
-- ============================================================
DROP TABLE IF EXISTS `role_permission`;
DROP TABLE IF EXISTS `permission`;

CREATE TABLE `permission` (
    `id`            BIGINT          NOT NULL AUTO_INCREMENT  COMMENT '权限主键ID',
    `name`          VARCHAR(50)     NOT NULL                 COMMENT '权限名称',
    `code`          VARCHAR(50)     NOT NULL                 COMMENT '权限标识码',
    `description`   VARCHAR(255)    DEFAULT NULL             COMMENT '权限描述',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='权限表';

CREATE TABLE `role_permission` (
    `id`            BIGINT          NOT NULL AUTO_INCREMENT  COMMENT '关联主键ID',
    `role`          INT             NOT NULL                 COMMENT '角色：0=读者 1=管理员',
    `permission_id` BIGINT          NOT NULL                 COMMENT '权限ID',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_role_perm` (`role`, `permission_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色权限关联表';

-- ============================================================
-- 5. 系统参数表
-- ============================================================
DROP TABLE IF EXISTS `system_config`;

CREATE TABLE `system_config` (
    `id`            BIGINT          NOT NULL AUTO_INCREMENT  COMMENT '参数主键ID',
    `config_key`    VARCHAR(100)    NOT NULL                 COMMENT '参数键名',
    `config_value`  VARCHAR(500)    NOT NULL                 COMMENT '参数值',
    `description`   VARCHAR(255)    DEFAULT NULL             COMMENT '参数说明',
    `editable`      TINYINT         NOT NULL DEFAULT 1       COMMENT '1=可在线修改 0=仅可读',
    `create_time`   DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`   DATETIME        DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_config_key` (`config_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统参数表';

-- ============================================================
-- 1.1 公告表
-- ============================================================
CREATE TABLE `announcement` (
    `id`            BIGINT          NOT NULL AUTO_INCREMENT  COMMENT '公告主键ID',
    `title`         VARCHAR(200)    NOT NULL                 COMMENT '公告标题',
    `content`       TEXT            DEFAULT NULL             COMMENT '公告内容',
    `active`        TINYINT         NOT NULL DEFAULT 1       COMMENT '状态：1=显示 0=隐藏',
    `create_time`   DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`   DATETIME        DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='公告表';

-- ============================================================
-- 2. 图书分类表
-- ============================================================
DROP TABLE IF EXISTS `operation_log`;

CREATE TABLE `operation_log` (
    `id`            BIGINT          NOT NULL AUTO_INCREMENT  COMMENT '日志主键ID',
    `user_id`       BIGINT          DEFAULT NULL             COMMENT '操作用户ID',
    `username`      VARCHAR(50)     DEFAULT NULL             COMMENT '操作用户名',
    `operation`     VARCHAR(50)     DEFAULT NULL             COMMENT '操作类型',
    `target`        VARCHAR(100)    DEFAULT NULL             COMMENT '操作目标',
    `detail`        VARCHAR(500)    DEFAULT NULL             COMMENT '操作详情',
    `ip`            VARCHAR(50)     DEFAULT NULL             COMMENT '操作IP地址',
    `create_time`   DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
    PRIMARY KEY (`id`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='操作日志表';

-- ============================================================
-- 2. 图书分类表
-- ============================================================
CREATE TABLE `category` (
    `id`            BIGINT          NOT NULL AUTO_INCREMENT  COMMENT '分类主键ID',
    `name`          VARCHAR(50)     NOT NULL                 COMMENT '分类名称',
    `description`   VARCHAR(255)    DEFAULT NULL             COMMENT '分类描述',
    `create_time`   DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`   DATETIME        DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='图书分类表';

-- ============================================================
-- 3. 图书信息表
-- ============================================================
CREATE TABLE `book` (
    `id`                BIGINT          NOT NULL AUTO_INCREMENT  COMMENT '图书主键ID',
    `isbn`              VARCHAR(20)     DEFAULT NULL             COMMENT 'ISBN编号',
    `title`             VARCHAR(200)    NOT NULL                 COMMENT '图书名称',
    `author`            VARCHAR(100)    DEFAULT NULL             COMMENT '作者',
    `publisher`         VARCHAR(100)    DEFAULT NULL             COMMENT '出版社',
    `category_id`       BIGINT          DEFAULT NULL             COMMENT '所属分类ID',
    `description`       TEXT            DEFAULT NULL             COMMENT '图书简介',
    `cover_url`         VARCHAR(255)    DEFAULT NULL             COMMENT '封面图片URL',
    `total_quantity`    INT             NOT NULL DEFAULT 1       COMMENT '馆藏总数量',
    `available_quantity` INT            NOT NULL DEFAULT 1       COMMENT '当前可借数量',
    `create_time`       DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '入库时间',
    `update_time`       DATETIME        DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_isbn` (`isbn`),
    KEY `idx_category_id` (`category_id`),
    KEY `idx_title` (`title`),
    KEY `idx_author` (`author`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='图书信息表';

-- ============================================================
-- 4. 图书借阅记录表
-- ============================================================
CREATE TABLE `borrow_record` (
    `id`            BIGINT          NOT NULL AUTO_INCREMENT  COMMENT '借阅记录主键ID',
    `user_id`       BIGINT          NOT NULL                 COMMENT '借阅用户ID',
    `book_id`       BIGINT          NOT NULL                 COMMENT '借阅图书ID',
    `borrow_time`   DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '借阅时间',
    `due_time`      DATETIME        NOT NULL                 COMMENT '应还时间（借阅时间+30天）',
    `return_time`   DATETIME        DEFAULT NULL             COMMENT '实际归还时间',
    `overdue_days`  INT             DEFAULT 0                COMMENT '逾期天数（归还时计算）',
    `status`        TINYINT         NOT NULL DEFAULT 0       COMMENT '借阅状态：0=借阅中 1=已归还 2=已逾期',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_book_id` (`book_id`),
    KEY `idx_status` (`status`),
    KEY `idx_due_time` (`due_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='图书借阅记录表';

-- ============================================================
-- 测试初始化数据（用户账号由 DataInitializer 在启动时自动创建）
-- 管理员: admin / admin123
-- 读者:   zhangsan / 123456 | lisi / 123456 | wangwu / 123456
-- ============================================================

-- 图书分类
INSERT INTO `category` (`name`, `description`) VALUES
('计算机科学', '涵盖编程语言、算法、人工智能、操作系统等领域'),
('文学小说',   '中外经典名著、现代文学作品'),
('历史人文',   '历史典籍、哲学思想、人文社科'),
('自然科学',   '数学、物理、化学、生物等基础科学'),
('经济管理',   '经济学理论、企业管理、市场营销'),
('艺术设计',   '绘画、摄影、平面设计、建筑艺术');

-- 测试图书（含封面图片URL，来自Open Library Covers API）
INSERT INTO `book` (`isbn`, `title`, `author`, `publisher`, `category_id`, `description`, `cover_url`, `total_quantity`, `available_quantity`) VALUES
('978-7-111-68451-2', 'Java核心技术（第12版）',   'Cay S. Horstmann', '机械工业出版社', 1, '全面讲解Java语言核心概念与编程实践，是Java开发者案头必备参考书。', 'https://picsum.photos/seed/java-core/400/500', 5, 5),
('978-7-115-58168-4', 'Spring Boot实战之旅',       'Craig Walls',      '人民邮电出版社', 1, '从零开始掌握Spring Boot框架，涵盖RESTful API、数据访问、安全等核心主题。', 'https://picsum.photos/seed/spring-boot/400/500', 3, 3),
('978-7-302-58169-1', '算法（第4版）',            'Robert Sedgewick', '清华大学出版社', 1, '经典算法教材，以Java语言实现，讲解排序、搜索、图算法等核心内容。', 'https://picsum.photos/seed/algorithm/400/500', 4, 4),
('978-7-02-000220-7', '红楼梦',                    '曹雪芹',           '人民文学出版社', 2, '中国古典四大名著之首，以贾宝玉和林黛玉的爱情悲剧为主线，展现封建社会的兴衰。', 'https://picsum.photos/seed/hongloumeng/400/500', 10, 10),
('978-7-5321-5432-7', '活着',                      '余华',             '上海文艺出版社', 2, '讲述了一个人历尽世间沧桑和磨难的一生，展现生命的坚韧与意义。', 'https://picsum.photos/seed/huozhe/400/500', 8, 8),
('978-7-5086-8721-6', '人类简史：从动物到上帝',     '尤瓦尔·赫拉利',   '中信出版社',     3, '从认知革命到科学革命，回顾人类发展历程，思考未来方向。', 'https://picsum.photos/seed/sapiens/400/500', 6, 6),
('978-7-111-53569-0', '深入理解计算机系统',        'Randal E. Bryant', '机械工业出版社', 1, '从程序员视角深入理解计算机系统底层原理，是系统编程的权威教材。', 'https://picsum.photos/seed/csapp/400/500', 3, 3),
('978-7-100-17010-5', '百年孤独',                  '加西亚·马尔克斯',   '商务印书馆',     2, '魔幻现实主义文学代表作，描绘布恩迪亚家族七代人的传奇与拉美历史变迁。', 'https://picsum.photos/seed/100-years/400/500', 7, 7),
('978-7-121-38901-0', 'MySQL技术内幕（第5版）',    'Paul DuBois',      '电子工业出版社', 1, 'MySQL官方推荐参考书，深入讲解SQL优化、存储引擎、复制等高阶主题。', 'https://picsum.photos/seed/mysql/400/500', 3, 3),
('978-7-5086-9823-6', '原则',                      '瑞·达利欧',        '中信出版社',     5, '桥水基金创始人的毕生经验总结，分享生活与工作原则的思考框架。', 'https://picsum.photos/seed/principles/400/500', 5, 5);

-- ============================================================
-- 更新已有数据的封面URL（重跑脚本已drop表则无需执行）
-- ============================================================
UPDATE `book` SET `cover_url` = 'https://picsum.photos/seed/java-core/400/500' WHERE `isbn` = '978-7-111-68451-2' AND (`cover_url` IS NULL OR `cover_url` = '');
UPDATE `book` SET `cover_url` = 'https://picsum.photos/seed/spring-boot/400/500' WHERE `isbn` = '978-7-115-58168-4' AND (`cover_url` IS NULL OR `cover_url` = '');
UPDATE `book` SET `cover_url` = 'https://picsum.photos/seed/algorithm/400/500' WHERE `isbn` = '978-7-302-58169-1' AND (`cover_url` IS NULL OR `cover_url` = '');
UPDATE `book` SET `cover_url` = 'https://picsum.photos/seed/hongloumeng/400/500' WHERE `isbn` = '978-7-02-000220-7' AND (`cover_url` IS NULL OR `cover_url` = '');
UPDATE `book` SET `cover_url` = 'https://picsum.photos/seed/huozhe/400/500' WHERE `isbn` = '978-7-5321-5432-7' AND (`cover_url` IS NULL OR `cover_url` = '');
UPDATE `book` SET `cover_url` = 'https://picsum.photos/seed/sapiens/400/500' WHERE `isbn` = '978-7-5086-8721-6' AND (`cover_url` IS NULL OR `cover_url` = '');
UPDATE `book` SET `cover_url` = 'https://picsum.photos/seed/csapp/400/500' WHERE `isbn` = '978-7-111-53569-0' AND (`cover_url` IS NULL OR `cover_url` = '');
UPDATE `book` SET `cover_url` = 'https://picsum.photos/seed/100-years/400/500' WHERE `isbn` = '978-7-100-17010-5' AND (`cover_url` IS NULL OR `cover_url` = '');
UPDATE `book` SET `cover_url` = 'https://picsum.photos/seed/mysql/400/500' WHERE `isbn` = '978-7-121-38901-0' AND (`cover_url` IS NULL OR `cover_url` = '');
UPDATE `book` SET `cover_url` = 'https://picsum.photos/seed/principles/400/500' WHERE `isbn` = '978-7-5086-9823-6' AND (`cover_url` IS NULL OR `cover_url` = '');
