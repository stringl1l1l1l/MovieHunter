/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80025
 Source Host           : localhost:3306
 Source Schema         : movie_hunter

 Target Server Type    : MySQL
 Target Server Version : 80025
 File Encoding         : 65001

 Date: 19/02/2024 14:55:57
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for comment
-- ----------------------------
DROP TABLE IF EXISTS `comment`;
CREATE TABLE `comment`  (
  `comment_id` int NOT NULL AUTO_INCREMENT,
  `movie_id` int NOT NULL,
  `user_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `rating` tinyint NOT NULL DEFAULT 5,
  `content` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `comment_time` datetime NOT NULL ON UPDATE CURRENT_TIMESTAMP,
  `votes` int NOT NULL DEFAULT 0,
  PRIMARY KEY (`comment_id`) USING BTREE,
  INDEX `fk_comment_movie_1`(`movie_id` ASC) USING BTREE,
  INDEX `comment_ibfk_1`(`user_id` ASC) USING BTREE,
  CONSTRAINT `comment_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_comment_movie_1` FOREIGN KEY (`movie_id`) REFERENCES `movie` (`movie_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 1942011794 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for favorite
-- ----------------------------
DROP TABLE IF EXISTS `favorite`;
CREATE TABLE `favorite`  (
  `favorite_id` int NOT NULL AUTO_INCREMENT,
  `user_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `name` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '未命名',
  `update_time` datetime NULL DEFAULT NULL,
  `del_flag` tinyint NOT NULL DEFAULT 0,
  PRIMARY KEY (`favorite_id`) USING BTREE,
  INDEX `fk_favourite_user_1`(`user_id` ASC) USING BTREE,
  CONSTRAINT `favorite_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for favorite_movie
-- ----------------------------
DROP TABLE IF EXISTS `favorite_movie`;
CREATE TABLE `favorite_movie`  (
  `favorite_id` int NOT NULL,
  `movie_id` int NOT NULL,
  PRIMARY KEY (`favorite_id`, `movie_id`) USING BTREE,
  INDEX `fk_favourite_items_favourite_1`(`favorite_id` ASC) USING BTREE,
  INDEX `fk_favourite_items_movie_1`(`movie_id` ASC) USING BTREE,
  CONSTRAINT `fk_favorite_items_movie_1` FOREIGN KEY (`movie_id`) REFERENCES `movie` (`movie_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_favourite_items_favorite_1` FOREIGN KEY (`favorite_id`) REFERENCES `favorite` (`favorite_id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for likes
-- ----------------------------
DROP TABLE IF EXISTS `likes`;
CREATE TABLE `likes`  (
  `user_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `comment_id` int NOT NULL,
  PRIMARY KEY (`user_id`, `comment_id`) USING BTREE,
  INDEX `likes_ibfk_2`(`comment_id` ASC) USING BTREE,
  CONSTRAINT `likes_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `likes_ibfk_2` FOREIGN KEY (`comment_id`) REFERENCES `comment` (`comment_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for menu
-- ----------------------------
DROP TABLE IF EXISTS `menu`;
CREATE TABLE `menu`  (
  `menu_id` int NOT NULL AUTO_INCREMENT,
  `menu_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `permission` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `del_flag` tinyint NOT NULL DEFAULT 0,
  PRIMARY KEY (`menu_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for movie
-- ----------------------------
DROP TABLE IF EXISTS `movie`;
CREATE TABLE `movie`  (
  `movie_id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `alias` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `cover` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'http://localhost:8081/MovieHunterWeb/static/movie.jpeg' COMMENT '海报链接',
  `directors` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `actors` varchar(400) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '主演',
  `score` float NOT NULL DEFAULT 0 COMMENT '评分',
  `genres` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '电影种类',
  `genres_mask` int NOT NULL DEFAULT 0,
  `duration` int NOT NULL DEFAULT 0 COMMENT '时长',
  `languages` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '语言',
  `regions` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '地区',
  `regions_mask` int NOT NULL DEFAULT 0,
  `release_date` date NULL DEFAULT NULL,
  `storyline` varchar(8000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '简介',
  `tags` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `year` int NULL DEFAULT NULL,
  `del_flag` tinyint NOT NULL DEFAULT 0,
  PRIMARY KEY (`movie_id`, `name`) USING BTREE,
  INDEX `movie_id`(`movie_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 34782629 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role`  (
  `role_id` int NOT NULL AUTO_INCREMENT,
  `role_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `del_flag` tinyint NOT NULL DEFAULT 0,
  PRIMARY KEY (`role_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for role_menu
-- ----------------------------
DROP TABLE IF EXISTS `role_menu`;
CREATE TABLE `role_menu`  (
  `role_id` int NOT NULL,
  `menu_id` int NOT NULL,
  PRIMARY KEY (`role_id`, `menu_id`) USING BTREE,
  INDEX `fk_role_menu_role1_idx`(`role_id` ASC) USING BTREE,
  INDEX `fk_role_menu_menu1_idx`(`menu_id` ASC) USING BTREE,
  CONSTRAINT `fk_role_menu_menu1` FOREIGN KEY (`menu_id`) REFERENCES `menu` (`menu_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_role_menu_role1` FOREIGN KEY (`role_id`) REFERENCES `role` (`role_id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `user_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '',
  `username` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `phone_num` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `email` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `password` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `avatar` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif',
  `del_flag` tinyint NOT NULL DEFAULT 0,
  PRIMARY KEY (`user_id`) USING BTREE,
  UNIQUE INDEX `mail`(`email` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for user_info
-- ----------------------------
DROP TABLE IF EXISTS `user_info`;
CREATE TABLE `user_info`  (
  `user_info_id` int NOT NULL AUTO_INCREMENT,
  `user_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `introduction` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `avatar` varchar(400) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif',
  `name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'admin',
  PRIMARY KEY (`user_info_id`) USING BTREE,
  INDEX `fk_user_info_user_id_idx`(`user_id` ASC) USING BTREE,
  CONSTRAINT `user_info_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for user_role
-- ----------------------------
DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role`  (
  `user_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `role_id` int NOT NULL,
  PRIMARY KEY (`user_id`, `role_id`) USING BTREE,
  INDEX `fk_user_role_user1_idx`(`user_id` ASC) USING BTREE,
  INDEX `fk_user_role_role1_idx`(`role_id` ASC) USING BTREE,
  CONSTRAINT `fk_user_role_role1` FOREIGN KEY (`role_id`) REFERENCES `role` (`role_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `user_role_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- View structure for view_role_perm
-- ----------------------------
DROP VIEW IF EXISTS `view_role_perm`;
CREATE ALGORITHM = UNDEFINED SQL SECURITY DEFINER VIEW `view_role_perm` AS select distinct `rm`.`role_id` AS `role_id`,`rm`.`menu_id` AS `menu_id`,`r`.`role_name` AS `role_name`,`m`.`menu_name` AS `menu_name`,`m`.`permission` AS `permission` from ((`role_menu` `rm` left join `menu` `m` on((`rm`.`menu_id` = `m`.`menu_id`))) left join `role` `r` on((`rm`.`role_id` = `r`.`role_id`))) where ((`r`.`del_flag` = 0) and (`m`.`del_flag` = 0));

-- ----------------------------
-- View structure for view_user_perm
-- ----------------------------
DROP VIEW IF EXISTS `view_user_perm`;
CREATE ALGORITHM = UNDEFINED SQL SECURITY DEFINER VIEW `view_user_perm` AS select distinct `ur`.`user_id` AS `user_id`,`m`.`permission` AS `permission` from (((`user_role` `ur` left join `role` `r` on((`ur`.`role_id` = `r`.`role_id`))) left join `role_menu` `rm` on((`ur`.`role_id` = `rm`.`role_id`))) left join `menu` `m` on((`rm`.`menu_id` = `m`.`menu_id`))) where ((`r`.`del_flag` = 0) and (`m`.`del_flag` = 0));

-- ----------------------------
-- View structure for view_user_role
-- ----------------------------
DROP VIEW IF EXISTS `view_user_role`;
CREATE ALGORITHM = UNDEFINED SQL SECURITY DEFINER VIEW `view_user_role` AS select `ur`.`role_id` AS `role_id`,`ur`.`user_id` AS `user_id`,`u`.`username` AS `username`,`r`.`role_name` AS `role_name` from ((`user_role` `ur` left join `user` `u` on((`u`.`user_id` = `ur`.`user_id`))) left join `role` `r` on((`r`.`role_id` = `ur`.`role_id`))) where ((`r`.`del_flag` = 0) and (`u`.`del_flag` = 0));

-- ----------------------------
-- Triggers structure for table likes
-- ----------------------------
DROP TRIGGER IF EXISTS `after_insert_like`;
delimiter ;;
CREATE TRIGGER `after_insert_like` AFTER INSERT ON `likes` FOR EACH ROW BEGIN
    UPDATE comment
    SET votes = votes + 1
    WHERE comment_id = NEW.comment_id;
END
;;
delimiter ;

-- ----------------------------
-- Triggers structure for table likes
-- ----------------------------
DROP TRIGGER IF EXISTS `after_delete_like`;
delimiter ;;
CREATE TRIGGER `after_delete_like` AFTER DELETE ON `likes` FOR EACH ROW BEGIN
    UPDATE comment
    SET votes = votes - 1
    WHERE comment_id = OLD.comment_id;
END
;;
delimiter ;

SET FOREIGN_KEY_CHECKS = 1;
