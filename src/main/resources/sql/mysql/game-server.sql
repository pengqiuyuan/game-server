/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50528
Source Host           : localhost:3306
Source Database       : game-server

Target Server Type    : MYSQL
Target Server Version : 50528
File Encoding         : 65001

Date: 2015-01-23 19:58:00
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `game_enum`
-- ----------------------------
DROP TABLE IF EXISTS `game_enum`;
CREATE TABLE `game_enum` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `cr_date` datetime DEFAULT NULL,
  `up_date` datetime DEFAULT NULL,
  `status` int(1) DEFAULT NULL,
  `enum_role` varchar(5) DEFAULT NULL,
  `enum_name` varchar(255) DEFAULT NULL,
  `game_name` varchar(50) DEFAULT NULL,
  `game_id` int(10) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of game_enum
-- ----------------------------
INSERT INTO `game_enum` VALUES ('1', '2015-01-20 09:44:58', '2015-01-20 09:45:03', '1', '1', '服务器搜索与查看', '', null);
INSERT INTO `game_enum` VALUES ('2', '2015-01-20 09:45:03', '2015-01-20 09:45:03', '1', '2', '服务器配置与开关', '', null);
INSERT INTO `game_enum` VALUES ('3', '2015-01-20 09:45:03', '2015-01-20 09:45:03', '1', '3', '游戏玩家列表查看与搜索', null, null);
INSERT INTO `game_enum` VALUES ('4', '2015-01-20 09:45:03', '2015-01-20 09:45:03', '1', '4', '角色数据修改与登录封禁', null, null);
INSERT INTO `game_enum` VALUES ('5', '2015-01-20 09:45:03', '2015-01-20 09:45:03', '1', '5', '邮件查看与检索', null, null);
INSERT INTO `game_enum` VALUES ('6', '2015-01-20 09:45:03', '2015-01-20 09:45:03', '1', '6', '新增邮件', null, null);
INSERT INTO `game_enum` VALUES ('7', '2015-01-20 09:45:03', '2015-01-20 09:45:03', '1', '7', '统计日志查看', null, null);
INSERT INTO `game_enum` VALUES ('8', '2015-01-20 09:45:03', '2015-01-20 09:45:03', '1', '8', '数据分析查看', null, null);

-- ----------------------------
-- Table structure for `game_log`
-- ----------------------------
DROP TABLE IF EXISTS `game_log`;
CREATE TABLE `game_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `content` text,
  `cr_user` varchar(64) DEFAULT NULL,
  `cr_date` datetime DEFAULT NULL,
  `type` varchar(64) DEFAULT NULL,
  `status` int(1) DEFAULT NULL,
  `up_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=996 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of game_log
-- ----------------------------
INSERT INTO `game_log` VALUES ('953', '修改:com.enlight.game.entity.User@b097da[loginName=<null>,name=test10000000,password=<null>,salt=<null>,roles=business,registerDate=<null>,storeId=21,email=<null>,upDate=<null>,status=1,plainPassword=<null>,id=25]', 'admin', '2014-12-11 15:09:03', '3', '1', null);
INSERT INTO `game_log` VALUES ('954', '添加:com.enlight.game.entity.User@1ddac04[loginName=lalala,name=lalala,password=0e4fc91d96d90593a90ba7ea842e7d8de6f43802,salt=b5990dbd9097daf7,roles=store_business,registerDate=Thu Dec 11 15:25:09 CST 2014,storeId=21,email=<null>,upDate=<null>,status=1,plainPassword=lalala,id=26]', 'admin', '2014-12-11 15:25:09', '3', '1', null);
INSERT INTO `game_log` VALUES ('955', '重置密码:user', 'admin', '2014-12-11 15:27:37', '3', '1', null);
INSERT INTO `game_log` VALUES ('956', '冻结:lalala', 'admin', '2014-12-11 16:46:43', '3', '1', null);
INSERT INTO `game_log` VALUES ('957', '修改:com.enlight.game.entity.User@c41d96[loginName=<null>,name=lalala,password=<null>,salt=<null>,roles=store_business,registerDate=<null>,storeId=21,email=<null>,upDate=<null>,status=1,plainPassword=<null>,id=26]', 'admin', '2014-12-11 16:46:50', '3', '1', null);
INSERT INTO `game_log` VALUES ('958', '修改:com.enlight.game.entity.Stores@106b791[id=1,name=游戏二组,addr=北京东城区,thumb=http://127.0.0.1/game/2014/12/2014121116J6D0H.origin.png,tel=83410650,status=<null>,sort=0,createDate=<null>,upDate=<null>,users=[]]', 'admin', '2014-12-11 16:55:15', '4', '1', null);
INSERT INTO `game_log` VALUES ('959', '新增:com.enlight.game.entity.Stores@c75c22[id=28,name=游戏一组,addr=北京东城区,thumb=http://127.0.0.1/game/2014/12/2014121116VQ8KA.origin.png,tel=23423423,status=1,sort=2,createDate=<null>,upDate=<null>,users=[]]', 'admin', '2014-12-11 16:55:45', '4', '1', null);
INSERT INTO `game_log` VALUES ('960', '修改:com.enlight.game.entity.User@3f29ee[loginName=<null>,name=admin,password=<null>,salt=<null>,roles=admin,registerDate=<null>,storeId=1,email=<null>,upDate=<null>,status=1,plainPassword=<null>,id=1]', 'admin', '2014-12-11 16:56:35', '3', '1', null);
INSERT INTO `game_log` VALUES ('961', '添加:com.enlight.game.entity.User@622800[loginName=test1,name=测试_总业务员,password=5e3d1caf3625369a0e3b80dddf3fdabd14a49b7f,salt=b72c3282a999080e,roles=business,registerDate=Thu Dec 11 16:57:35 CST 2014,storeId=1,email=<null>,upDate=<null>,status=1,plainPassword=admin,id=27]', 'admin', '2014-12-11 16:57:35', '3', '1', null);
INSERT INTO `game_log` VALUES ('962', '添加:com.enlight.game.entity.User@b1f43b[loginName=test2,name=测试_部门管理员,password=0d786c046c0c78a50a78f26436e7c07e67e28599,salt=ec55095d4097f055,roles=store_admin,registerDate=Thu Dec 11 16:58:37 CST 2014,storeId=1,email=<null>,upDate=<null>,status=1,plainPassword=admin,id=28]', 'admin', '2014-12-11 16:58:37', '3', '1', null);
INSERT INTO `game_log` VALUES ('963', '添加:com.enlight.game.entity.User@10eacbe[loginName=test3,name=测试_部门业务员,password=b08afeaedea2e18bf6760a36c38369d4b226bb0e,salt=a7fb9c95550dd2c8,roles=admin,registerDate=Thu Dec 11 16:58:56 CST 2014,storeId=1,email=<null>,upDate=<null>,status=1,plainPassword=admin,id=29]', 'admin', '2014-12-11 16:58:56', '3', '1', null);
INSERT INTO `game_log` VALUES ('964', '添加:com.enlight.game.entity.User@1aa0b38[loginName=test4,name=测试_总业务员,password=5f7e9b7d54f290fcab34c9193b1d55a15d0480d6,salt=d48a6b98297ddeca,roles=business,registerDate=Thu Dec 11 17:00:24 CST 2014,storeId=28,email=<null>,upDate=<null>,status=1,plainPassword=admin,id=30]', 'admin', '2014-12-11 17:00:24', '3', '1', null);
INSERT INTO `game_log` VALUES ('965', '修改:com.enlight.game.entity.User@1b2c16[loginName=<null>,name=测试_总业务员,password=<null>,salt=<null>,roles=business,registerDate=<null>,storeId=28,email=<null>,upDate=<null>,status=1,plainPassword=<null>,id=27]', 'admin', '2014-12-11 17:01:39', '3', '1', null);
INSERT INTO `game_log` VALUES ('966', '冻结:测试_总业务员', 'admin', '2014-12-11 17:01:46', '3', '1', null);
INSERT INTO `game_log` VALUES ('967', '修改:com.enlight.game.entity.User@b0bbb0[loginName=<null>,name=测试_部门业务员,password=<null>,salt=<null>,roles=store_business,registerDate=<null>,storeId=1,email=<null>,upDate=<null>,status=1,plainPassword=<null>,id=29]', '测试_部门业务员', '2014-12-11 17:03:05', '3', '1', null);
INSERT INTO `game_log` VALUES ('968', '修改:com.enlight.game.entity.User@2ccea8[loginName=<null>,name=测试_部门业务员,password=<null>,salt=<null>,roles=store_business,registerDate=<null>,storeId=1,email=<null>,upDate=<null>,status=1,plainPassword=<null>,id=29]', '测试_部门业务员', '2014-12-11 17:06:33', '3', '1', null);
INSERT INTO `game_log` VALUES ('969', '修改:com.enlight.game.entity.User@1b94e08[loginName=<null>,name=测试_总业务员1,password=<null>,salt=<null>,roles=business,registerDate=<null>,storeId=28,email=<null>,upDate=<null>,status=1,plainPassword=<null>,id=27]', 'admin', '2014-12-11 17:13:22', '3', '1', null);
INSERT INTO `game_log` VALUES ('970', '修改:com.enlight.game.entity.User@b5a817[loginName=<null>,name=测试_部门管理员1,password=<null>,salt=<null>,roles=store_admin,registerDate=<null>,storeId=28,email=<null>,upDate=<null>,status=1,plainPassword=<null>,id=28]', 'admin', '2014-12-11 17:13:37', '3', '1', null);
INSERT INTO `game_log` VALUES ('971', '修改:com.enlight.game.entity.User@33953[loginName=<null>,name=测试_部门业务员1,password=<null>,salt=<null>,roles=store_business,registerDate=<null>,storeId=28,email=<null>,upDate=<null>,status=1,plainPassword=<null>,id=29]', 'admin', '2014-12-11 17:13:48', '3', '1', null);
INSERT INTO `game_log` VALUES ('972', '修改:com.enlight.game.entity.User@113cec9[loginName=<null>,name=测试_总业务员2,password=<null>,salt=<null>,roles=business,registerDate=<null>,storeId=1,email=<null>,upDate=<null>,status=1,plainPassword=<null>,id=30]', 'admin', '2014-12-11 17:14:05', '3', '1', null);
INSERT INTO `game_log` VALUES ('973', '添加:com.enlight.game.entity.User@1e963dd[loginName=test5,name=测试_部门管理员2,password=450355cf7b686786bc5f23f865e5f6703b187a33,salt=d103a97e8c5855f4,roles=store_admin,registerDate=Thu Dec 11 17:14:31 CST 2014,storeId=1,email=<null>,upDate=<null>,status=1,plainPassword=admin,id=31]', 'admin', '2014-12-11 17:14:31', '3', '1', null);
INSERT INTO `game_log` VALUES ('974', '添加:com.enlight.game.entity.User@d86fe2[loginName=test6,name=测试_部门业务员2,password=d177b477f5af57aac4716dfbe8637552dde9636f,salt=3f993b1b46558be9,roles=store_business,registerDate=Thu Dec 11 17:15:18 CST 2014,storeId=1,email=<null>,upDate=<null>,status=1,plainPassword=admin,id=32]', 'admin', '2014-12-11 17:15:18', '3', '1', null);
INSERT INTO `game_log` VALUES ('975', '修改:com.enlight.game.entity.Stores@1e26b7f[id=28,name=游戏一组,addr=北京东城区,thumb=http://127.0.0.1/game/2014/12/2014121116VQ8KA.origin.png,tel=23423423,status=<null>,sort=0,createDate=<null>,upDate=<null>,users=[]]', '测试_部门管理员1', '2014-12-11 17:53:46', '4', '1', null);
INSERT INTO `game_log` VALUES ('976', '新增:com.enlight.game.entity.Stores@976292[id=29,name=阿什顿,addr=随碟附送,thumb=<null>,tel=,status=1,sort=3,createDate=<null>,upDate=<null>,users=[]]', 'admin', '2015-01-20 12:18:35', '4', '1', null);
INSERT INTO `game_log` VALUES ('977', '添加:com.enlight.game.entity.User@59ec7e[loginName=1213,name=admin,password=98edb4d6078c6a161b75c95e84a3392198c4e063,salt=6181a87ef7c7e12e,roles=admin,registerDate=Wed Jan 21 10:52:43 CST 2015,storeId=1,email=<null>,upDate=<null>,status=1,plainPassword=123123,id=33]', 'admin', '2015-01-21 10:52:43', '3', '1', null);
INSERT INTO `game_log` VALUES ('978', '冻结:12121', 'admin', '2015-01-22 14:43:38', '3', '1', null);
INSERT INTO `game_log` VALUES ('979', '激活:12121', 'admin', '2015-01-22 14:44:00', '3', '1', null);
INSERT INTO `game_log` VALUES ('980', '添加:com.enlight.game.entity.User@eb3965[loginName=2,name=2,password=1ece616c32ac3d2a53f4db353d7b3b57bef105a5,salt=7431f9ed6411ce40,roles=,registerDate=Thu Jan 22 15:04:35 CST 2015,storeId=29,28,1,email=<null>,upDate=<null>,status=1,plainPassword=11111,id=38]', 'admin', '2015-01-22 15:04:35', '3', '1', null);
INSERT INTO `game_log` VALUES ('981', '添加:com.enlight.game.entity.User@11bc06a[loginName=3,name=3,password=3f299ac6874bac0d30e48cd966cc8a634cdfa119,salt=02acd7f561a5eccd,roles=,registerDate=Thu Jan 22 15:09:25 CST 2015,storeId=29,28,1,email=<null>,upDate=<null>,status=1,plainPassword=11111,id=39]', 'admin', '2015-01-22 15:09:25', '3', '1', null);
INSERT INTO `game_log` VALUES ('982', '修改:com.enlight.game.entity.User@cad81c[loginName=<null>,name=3,password=<null>,salt=<null>,roles=<null>,registerDate=<null>,storeId=阿什顿 ,德玛西亚 ,超神学院 ,email=<null>,upDate=<null>,status=1,plainPassword=<null>,id=39]', 'admin', '2015-01-22 17:11:10', '3', '1', null);
INSERT INTO `game_log` VALUES ('983', '修改:com.enlight.game.entity.User@be49a5[loginName=<null>,name=啦啦啦啦啦啦,password=<null>,salt=<null>,roles=<null>,registerDate=<null>,storeId=<null>,email=<null>,upDate=<null>,status=1,plainPassword=<null>,id=39]', 'admin', '2015-01-22 17:18:07', '3', '1', null);
INSERT INTO `game_log` VALUES ('984', '修改:com.enlight.game.entity.User@4392c2[loginName=<null>,name=啦啦啦啦啦啦,password=<null>,salt=<null>,roles=<null>,registerDate=<null>,storeId=<null>,email=<null>,upDate=<null>,status=1,plainPassword=<null>,id=39]', 'admin', '2015-01-22 17:18:41', '3', '1', null);
INSERT INTO `game_log` VALUES ('985', '修改:com.enlight.game.entity.User@1baadd4[loginName=<null>,name=啦啦啦啦啦啦,password=<null>,salt=<null>,roles=<null>,registerDate=<null>,storeId=29,28,1,email=<null>,upDate=<null>,status=1,plainPassword=<null>,id=39]', 'admin', '2015-01-22 17:21:18', '3', '1', null);
INSERT INTO `game_log` VALUES ('986', '修改:com.enlight.game.entity.User@12205c0[loginName=<null>,name=啦啦啦啦啦啦,password=<null>,salt=<null>,roles=<null>,registerDate=<null>,storeId=29,28,1,email=<null>,upDate=<null>,status=1,plainPassword=<null>,id=39]', 'admin', '2015-01-22 17:21:23', '3', '1', null);
INSERT INTO `game_log` VALUES ('987', '修改:com.enlight.game.entity.User@9c5258[loginName=<null>,name=啦啦啦啦啦啦,password=<null>,salt=<null>,roles=<null>,registerDate=<null>,storeId=29,28,1,email=<null>,upDate=<null>,status=1,plainPassword=<null>,id=39]', 'admin', '2015-01-22 17:21:28', '3', '1', null);
INSERT INTO `game_log` VALUES ('988', '修改:com.enlight.game.entity.User@1a941fe[loginName=<null>,name=啦啦啦啦啦啦,password=<null>,salt=<null>,roles=<null>,registerDate=<null>,storeId=29,28,1,email=<null>,upDate=<null>,status=2,plainPassword=<null>,id=39]', 'admin', '2015-01-22 17:39:54', '3', '1', null);
INSERT INTO `game_log` VALUES ('989', '修改:com.enlight.game.entity.User@7d4add[loginName=<null>,name=啦啦啦啦啦啦,password=<null>,salt=<null>,roles=<null>,registerDate=<null>,storeId=29,28,1,email=<null>,upDate=<null>,status=1,plainPassword=<null>,id=39]', 'admin', '2015-01-22 17:40:11', '3', '1', null);
INSERT INTO `game_log` VALUES ('990', '添加:com.enlight.game.entity.User@4d7266[loginName=test1,name=测试1,password=99b95edd89ba82913c80426e2d8bd59cd4466446,salt=aa6e5acfe5fb4e28,roles=,registerDate=Thu Jan 22 17:46:07 CST 2015,storeId=1,email=<null>,upDate=<null>,status=1,plainPassword=asdasd,id=42]', 'admin', '2015-01-22 17:46:07', '3', '1', null);
INSERT INTO `game_log` VALUES ('991', '添加:com.enlight.game.entity.User@1f43da0[loginName=test,name=啦啦啦啦,password=21b1db1c8447969c0555185118ab9c9596337a74,salt=d595600b3cc056e2,roles=,registerDate=Thu Jan 22 17:54:04 CST 2015,storeId=1,email=<null>,upDate=<null>,status=1,plainPassword=admin,id=43]', 'admin', '2015-01-22 17:54:04', '3', '1', null);
INSERT INTO `game_log` VALUES ('992', '新增:com.enlight.game.entity.Stores@1067755[id=30,name=1111,addr=111,thumb=<null>,tel=8666232,status=1,sort=4,createDate=<null>,upDate=<null>,users=[]]', 'admin', '2015-01-22 20:00:53', '4', '1', null);
INSERT INTO `game_log` VALUES ('993', '添加:com.enlight.game.entity.User@1126b00[loginName=test1,name=test1,password=1c7cb5586108fb02e6b494cb0dccb6d09783a489,salt=36696ee7b1c1a868,roles=,registerDate=Fri Jan 23 15:33:28 CST 2015,storeId=29,28,1,30,email=<null>,upDate=<null>,status=1,plainPassword=admin,id=44]', 'admin', '2015-01-23 15:33:28', '3', '1', null);
INSERT INTO `game_log` VALUES ('994', '添加:com.enlight.game.entity.User@807edf[loginName=test2,name=test2,password=5b2fc096b215317fceef8d2c29eec2a57c811e1f,salt=14223bbadd019120,roles=,registerDate=Fri Jan 23 16:29:16 CST 2015,storeId=30,29,28,1,email=<null>,upDate=<null>,status=1,plainPassword=admin,id=45]', 'admin', '2015-01-23 16:29:16', '3', '1', null);
INSERT INTO `game_log` VALUES ('995', '添加:com.enlight.game.entity.User@db4f9c[loginName=test3,name=test3,password=c848cda57cd4d5bf0c95df4b7dec8cc9bf72157d,salt=1f257305058e6d95,roles=,registerDate=Fri Jan 23 19:56:57 CST 2015,storeId=30,29,28,1,email=<null>,upDate=<null>,status=1,plainPassword=admin,id=46]', 'admin', '2015-01-23 19:56:57', '3', '1', null);

-- ----------------------------
-- Table structure for `game_role_function`
-- ----------------------------
DROP TABLE IF EXISTS `game_role_function`;
CREATE TABLE `game_role_function` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `cr_date` datetime DEFAULT NULL,
  `up_date` datetime DEFAULT NULL,
  `status` int(1) DEFAULT NULL,
  `game_id` bigint(20) DEFAULT NULL,
  `role` varchar(50) DEFAULT NULL,
  `function` varchar(50) DEFAULT NULL,
  `game_name` varchar(50) DEFAULT NULL,
  `function_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=74 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of game_role_function
-- ----------------------------
INSERT INTO `game_role_function` VALUES ('1', '2015-01-20 15:10:43', '2015-01-20 17:35:15', '1', '29', '1', '2', '阿什顿', '服务器配置与开关');
INSERT INTO `game_role_function` VALUES ('2', '2015-01-20 15:20:51', '2015-01-20 17:07:58', '1', '29', '2', '7', '阿什顿', '统计日志查看');
INSERT INTO `game_role_function` VALUES ('3', '2015-01-20 15:24:02', '2015-01-20 15:24:02', '1', '28', '3', '7', '德玛西亚', '统计日志查看');
INSERT INTO `game_role_function` VALUES ('8', '2015-01-20 16:16:31', '2015-01-20 17:35:01', '1', '29', '77', '1', '阿什顿', '服务器搜索与查看');
INSERT INTO `game_role_function` VALUES ('10', '2015-01-20 16:39:52', '2015-01-20 16:39:52', '1', '1', '1', '8', '超神学院', '数据分析查看');
INSERT INTO `game_role_function` VALUES ('21', '2015-01-20 19:39:19', '2015-01-20 19:39:19', '1', '28', '22', '1', '德玛西亚', '服务器搜索与查看');
INSERT INTO `game_role_function` VALUES ('22', '2015-01-20 19:39:19', '2015-01-20 19:39:19', '1', '28', '22', '2', '德玛西亚', '服务器配置与开关');
INSERT INTO `game_role_function` VALUES ('25', '2015-01-20 20:14:11', '2015-01-20 20:14:11', '1', '29', '3', '3', '阿什顿', '游戏玩家列表查看与搜索');
INSERT INTO `game_role_function` VALUES ('26', '2015-01-20 20:14:11', '2015-01-20 20:14:11', '1', '29', '3', '4', '阿什顿', '角色数据修改与登录封禁');
INSERT INTO `game_role_function` VALUES ('42', '2015-01-21 10:42:33', '2015-01-21 10:42:33', '1', '29', '3', '5', '阿什顿', '邮件查看与检索');
INSERT INTO `game_role_function` VALUES ('43', '2015-01-21 10:42:33', '2015-01-21 10:42:33', '1', '29', '3', '6', '阿什顿', '新增邮件');
INSERT INTO `game_role_function` VALUES ('44', '2015-01-21 10:42:34', '2015-01-21 10:42:34', '1', '29', '3', '7', '阿什顿', '统计日志查看');
INSERT INTO `game_role_function` VALUES ('45', '2015-01-21 11:29:59', '2015-01-21 11:29:59', '1', '29', '3', '8', '阿什顿', '数据分析查看');
INSERT INTO `game_role_function` VALUES ('46', '2015-01-21 11:42:18', '2015-01-21 11:42:18', '1', '28', '1', '1', '德玛西亚', '服务器搜索与查看');
INSERT INTO `game_role_function` VALUES ('50', '2015-01-21 11:53:37', '2015-01-21 11:53:37', '1', '28', '2', '3', '德玛西亚', '游戏玩家列表查看与搜索');
INSERT INTO `game_role_function` VALUES ('51', '2015-01-21 12:02:54', '2015-01-21 12:02:54', '1', '28', '5', '7', '德玛西亚', '统计日志查看');
INSERT INTO `game_role_function` VALUES ('52', '2015-01-21 12:10:29', '2015-01-21 12:10:29', '1', '28', '6', '8', '德玛西亚', '数据分析查看');
INSERT INTO `game_role_function` VALUES ('53', '2015-01-21 12:12:27', '2015-01-21 12:12:27', '1', '28', '7', '1', '德玛西亚', '服务器搜索与查看');
INSERT INTO `game_role_function` VALUES ('54', '2015-01-21 12:12:44', '2015-01-21 12:12:44', '1', '29', '4', '6', '阿什顿', '新增邮件');
INSERT INTO `game_role_function` VALUES ('55', '2015-01-21 12:14:02', '2015-01-21 12:14:02', '1', '29', '4', '1', '阿什顿', '服务器搜索与查看');
INSERT INTO `game_role_function` VALUES ('56', '2015-01-21 12:19:25', '2015-01-21 12:19:25', '1', '1', '2', '6', '超神学院', '新增邮件');
INSERT INTO `game_role_function` VALUES ('57', '2015-01-21 12:20:10', '2015-01-21 12:20:10', '1', '1', '4', '7', '超神学院', '统计日志查看');
INSERT INTO `game_role_function` VALUES ('58', '2015-01-21 12:20:24', '2015-01-21 12:20:24', '1', '1', '4', '1', '超神学院', '服务器搜索与查看');
INSERT INTO `game_role_function` VALUES ('59', '2015-01-21 12:20:24', '2015-01-21 12:20:24', '1', '1', '4', '2', '超神学院', '服务器配置与开关');
INSERT INTO `game_role_function` VALUES ('60', '2015-01-21 12:28:19', '2015-01-21 12:28:19', '1', '1', '4', '3', '超神学院', '游戏玩家列表查看与搜索');
INSERT INTO `game_role_function` VALUES ('61', '2015-01-21 12:28:19', '2015-01-21 12:28:19', '1', '1', '4', '4', '超神学院', '角色数据修改与登录封禁');
INSERT INTO `game_role_function` VALUES ('62', '2015-01-21 12:28:28', '2015-01-21 12:28:28', '1', '1', '1', '1', '超神学院', '服务器搜索与查看');
INSERT INTO `game_role_function` VALUES ('63', '2015-01-21 13:45:32', '2015-01-21 13:45:32', '1', '29', '9', '3', '阿什顿', '游戏玩家列表查看与搜索');
INSERT INTO `game_role_function` VALUES ('64', '2015-01-21 13:45:32', '2015-01-21 13:45:32', '1', '29', '9', '4', '阿什顿', '角色数据修改与登录封禁');
INSERT INTO `game_role_function` VALUES ('65', '2015-01-21 17:53:23', '2015-01-21 17:53:23', '1', '28', '3', '1', '德玛西亚', '服务器搜索与查看');
INSERT INTO `game_role_function` VALUES ('66', '2015-01-22 20:00:20', '2015-01-22 20:00:20', '1', '1', '1', '2', '超神学院', '服务器配置与开关');
INSERT INTO `game_role_function` VALUES ('67', '2015-01-22 20:00:20', '2015-01-22 20:00:20', '1', '1', '1', '3', '超神学院', '游戏玩家列表查看与搜索');
INSERT INTO `game_role_function` VALUES ('68', '2015-01-22 20:01:14', '2015-01-22 20:01:14', '1', '30', '1', '1', '1111', '服务器搜索与查看');
INSERT INTO `game_role_function` VALUES ('69', '2015-01-22 20:01:15', '2015-01-22 20:01:15', '1', '30', '1', '2', '1111', '服务器配置与开关');
INSERT INTO `game_role_function` VALUES ('70', '2015-01-23 15:05:29', '2015-01-23 15:05:29', '1', '30', '11212', '1', '1111', '服务器搜索与查看');
INSERT INTO `game_role_function` VALUES ('71', '2015-01-23 15:05:56', '2015-01-23 15:05:56', '1', '30', '11212', '2', '1111', '服务器配置与开关');
INSERT INTO `game_role_function` VALUES ('72', '2015-01-23 15:05:56', '2015-01-23 15:05:56', '1', '30', '11212', '3', '1111', '游戏玩家列表查看与搜索');
INSERT INTO `game_role_function` VALUES ('73', '2015-01-23 15:05:56', '2015-01-23 15:05:56', '1', '30', '11212', '4', '1111', '角色数据修改与登录封禁');

-- ----------------------------
-- Table structure for `game_server_zone`
-- ----------------------------
DROP TABLE IF EXISTS `game_server_zone`;
CREATE TABLE `game_server_zone` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `server_name` varchar(50) DEFAULT NULL,
  `cr_date` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `up_date` timestamp NULL DEFAULT NULL,
  `status` int(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of game_server_zone
-- ----------------------------
INSERT INTO `game_server_zone` VALUES ('1', 'IOS官方', null, null, '1');
INSERT INTO `game_server_zone` VALUES ('2', 'IOS越狱', null, null, '1');
INSERT INTO `game_server_zone` VALUES ('3', '安卓官方', null, null, '1');
INSERT INTO `game_server_zone` VALUES ('4', '安卓滚服', null, null, '1');

-- ----------------------------
-- Table structure for `game_stores`
-- ----------------------------
DROP TABLE IF EXISTS `game_stores`;
CREATE TABLE `game_stores` (
  `store_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `addr` varchar(25) DEFAULT NULL,
  `thumb` varchar(255) DEFAULT NULL,
  `cr_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `up_date` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `status` int(1) DEFAULT NULL,
  `tel` varchar(64) DEFAULT NULL,
  `sort` int(11) DEFAULT NULL,
  PRIMARY KEY (`store_id`)
) ENGINE=MyISAM AUTO_INCREMENT=31 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of game_stores
-- ----------------------------
INSERT INTO `game_stores` VALUES ('1', '超神学院', '北京东城区', 'http://127.0.0.1/game/2014/12/2014121116J6D0H.origin.png', '2015-01-14 14:13:31', '2014-04-08 14:12:27', '1', '83410650', '1');
INSERT INTO `game_stores` VALUES ('28', '德玛西亚', '北京东城区', 'http://127.0.0.1/game/2014/12/2014121116VQ8KA.origin.png', '2015-01-14 14:13:42', '2014-12-11 16:55:45', '1', '23423423', '3');
INSERT INTO `game_stores` VALUES ('29', '阿什顿', '北京东城区', null, '2015-01-22 09:28:24', '2015-01-20 12:18:35', '1', '', '2');
INSERT INTO `game_stores` VALUES ('30', '1111', '111', null, '2015-01-22 20:00:53', '2015-01-22 20:00:53', '1', '8666232', '4');

-- ----------------------------
-- Table structure for `game_user`
-- ----------------------------
DROP TABLE IF EXISTS `game_user`;
CREATE TABLE `game_user` (
  `store_id` varchar(255) DEFAULT NULL,
  `login_name` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `salt` varchar(255) DEFAULT NULL,
  `roles` varchar(255) DEFAULT '',
  `register_date` date DEFAULT NULL,
  `up_date` date DEFAULT NULL,
  `status` int(1) DEFAULT NULL,
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=47 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of game_user
-- ----------------------------
INSERT INTO `game_user` VALUES ('0', 'admin', 'admin', null, '379575b2b3f04f5be35d86062e4188829cf74b44', '012a17813269fb7d', 'admin', '2014-04-04', null, '1', '1');
INSERT INTO `game_user` VALUES ('1', 'test', '啦啦啦啦', null, '21b1db1c8447969c0555185118ab9c9596337a74', 'd595600b3cc056e2', '', '2015-01-22', null, '1', '43');
INSERT INTO `game_user` VALUES ('29,28,1,30', 'test1', 'test1', null, '1c7cb5586108fb02e6b494cb0dccb6d09783a489', '36696ee7b1c1a868', '', '2015-01-23', null, '1', '44');
INSERT INTO `game_user` VALUES ('30,29,28,1', 'test2', 'test2', null, '5b2fc096b215317fceef8d2c29eec2a57c811e1f', '14223bbadd019120', '', '2015-01-23', null, '1', '45');
INSERT INTO `game_user` VALUES ('30,29,28,1', 'test3', 'test3', null, 'c848cda57cd4d5bf0c95df4b7dec8cc9bf72157d', '1f257305058e6d95', '', '2015-01-23', null, '1', '46');

-- ----------------------------
-- Table structure for `game_user_role`
-- ----------------------------
DROP TABLE IF EXISTS `game_user_role`;
CREATE TABLE `game_user_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) DEFAULT NULL,
  `cr_date` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `up_date` timestamp NULL DEFAULT NULL,
  `status` int(1) DEFAULT NULL,
  `server_zone` varchar(255) DEFAULT NULL,
  `store_id` bigint(20) DEFAULT NULL,
  `role` varchar(10) DEFAULT NULL,
  `functions` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of game_user_role
-- ----------------------------
INSERT INTO `game_user_role` VALUES ('7', '43', '2015-01-23 12:13:37', '2015-01-23 12:13:39', '1', '1,2,3,4', '29', null, '1,2');
INSERT INTO `game_user_role` VALUES ('8', '43', '2015-01-23 14:39:59', '2015-01-22 17:54:04', '1', ',1,2,3,4', '1', '4', '  7,1,2,3,4');
INSERT INTO `game_user_role` VALUES ('12', '44', '2015-01-23 15:33:28', '2015-01-23 15:33:28', '1', ',1,2,3,4', '29', '77', ',1');
INSERT INTO `game_user_role` VALUES ('13', '44', '2015-01-23 15:33:28', '2015-01-23 15:33:28', '1', ',1,2,3,4', '28', '22', ',1,2');
INSERT INTO `game_user_role` VALUES ('14', '44', '2015-01-23 15:33:28', '2015-01-23 15:33:28', '1', ',1,2,3,4', '1', '2', ',6');
INSERT INTO `game_user_role` VALUES ('15', '44', '2015-01-23 15:33:28', '2015-01-23 15:33:28', '1', ',1,2,3,4', '30', '1', ',1,2');
INSERT INTO `game_user_role` VALUES ('16', '45', '2015-01-23 16:29:16', '2015-01-23 16:29:16', '1', ',1,2', '30', '11212', ',1,2,3,4');
INSERT INTO `game_user_role` VALUES ('17', '45', '2015-01-23 16:29:16', '2015-01-23 16:29:16', '1', ',1,2', '29', '3', ',3,4,5,6,7,8');
INSERT INTO `game_user_role` VALUES ('18', '45', '2015-01-23 16:29:16', '2015-01-23 16:29:16', '1', ',1,2', '28', '2', ',3');
INSERT INTO `game_user_role` VALUES ('19', '45', '2015-01-23 16:29:16', '2015-01-23 16:29:16', '1', ',1,2', '1', '4', ',7,1,2,3,4');
INSERT INTO `game_user_role` VALUES ('20', '46', '2015-01-23 19:56:57', '2015-01-23 19:56:57', '1', ',3,4', '30', '1', ',1,2');
INSERT INTO `game_user_role` VALUES ('21', '46', '2015-01-23 19:56:57', '2015-01-23 19:56:57', '1', ',3,4', '29', '1', ',2');
INSERT INTO `game_user_role` VALUES ('22', '46', '2015-01-23 19:56:57', '2015-01-23 19:56:57', '1', ',3,4', '28', '1', ',1');
INSERT INTO `game_user_role` VALUES ('23', '46', '2015-01-23 19:56:57', '2015-01-23 19:56:57', '1', ',3,4', '1', '1', ',8,1,2,3');
