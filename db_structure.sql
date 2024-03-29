-- MySQL dump 10.13  Distrib 5.7.27, for Win64 (x86_64)
--
-- Host: localhost    Database: aisys
-- ------------------------------------------------------
-- Server version	5.7.27

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `article`
--

DROP TABLE IF EXISTS `article`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `article` (
  `article_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '鏂囩珷ID',
  `article_user_id` int(11) unsigned DEFAULT NULL COMMENT '鐢ㄦ埛ID',
  `article_title` varchar(255) DEFAULT NULL COMMENT '鏍囬',
  `article_content` mediumtext COMMENT '鍐呭',
  `article_view_count` int(11) DEFAULT '0' COMMENT '璁块棶閲?,
  `article_comment_count` int(11) DEFAULT '0' COMMENT '璇勮鏁?,
  `article_like_count` int(11) DEFAULT '0' COMMENT '鐐硅禐鏁?,
  `article_is_comment` int(1) unsigned DEFAULT NULL COMMENT '鏄惁鍏佽璇勮',
  `article_status` int(1) unsigned DEFAULT '1' COMMENT '鐘舵€?,
  `article_order` int(11) unsigned DEFAULT NULL COMMENT '鎺掑簭鍊?,
  `article_update_time` datetime DEFAULT NULL COMMENT '鏇存柊鏃堕棿',
  `article_create_time` datetime DEFAULT NULL COMMENT '鍒涘缓鏃堕棿',
  `article_summary` text COMMENT '鎽樿',
  `article_thumbnail` varchar(255) DEFAULT NULL COMMENT '缂╃暐鍥?,
  PRIMARY KEY (`article_id`)
) ENGINE=MyISAM AUTO_INCREMENT=50 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `article_category_ref`
--

DROP TABLE IF EXISTS `article_category_ref`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `article_category_ref` (
  `article_id` int(11) NOT NULL COMMENT '鏂囩珷ID',
  `category_id` int(11) NOT NULL COMMENT '鍒嗙被ID',
  PRIMARY KEY (`article_id`,`category_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `article_tag_ref`
--

DROP TABLE IF EXISTS `article_tag_ref`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `article_tag_ref` (
  `article_id` int(11) NOT NULL COMMENT '鏂囩珷ID',
  `tag_id` int(11) NOT NULL COMMENT '鏍囩ID',
  PRIMARY KEY (`article_id`,`tag_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `category`
--

DROP TABLE IF EXISTS `category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `category` (
  `category_id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '鍒嗙被ID',
  `category_pid` int(11) DEFAULT NULL COMMENT '鍒嗙被鐖禝D',
  `category_name` varchar(50) DEFAULT NULL COMMENT '鍒嗙被鍚嶇О',
  `category_description` varchar(255) DEFAULT NULL COMMENT '鎻忚堪',
  `category_order` int(11) unsigned DEFAULT '1' COMMENT '鎺掑簭鍊?,
  `category_icon` varchar(20) DEFAULT NULL COMMENT '鍥炬爣',
  PRIMARY KEY (`category_id`),
  UNIQUE KEY `category_name` (`category_name`)
) ENGINE=InnoDB AUTO_INCREMENT=100000009 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `comment`
--

DROP TABLE IF EXISTS `comment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `comment` (
  `comment_id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '璇勮ID',
  `comment_pid` int(11) unsigned DEFAULT '0' COMMENT '涓婄骇璇勮ID',
  `comment_pname` varchar(255) DEFAULT NULL COMMENT '涓婄骇璇勮鍚嶇О',
  `comment_article_id` int(11) unsigned NOT NULL COMMENT '鏂囩珷ID',
  `comment_author_name` varchar(50) DEFAULT NULL COMMENT '璇勮浜哄悕绉?,
  `comment_author_email` varchar(50) DEFAULT NULL COMMENT '璇勮浜洪偖绠?,
  `comment_author_url` varchar(50) DEFAULT NULL COMMENT '璇勮浜轰釜浜轰富椤?,
  `comment_author_avatar` varchar(100) DEFAULT NULL COMMENT '璇勮浜哄ご鍍?,
  `comment_content` varchar(1000) DEFAULT NULL COMMENT '鍐呭',
  `comment_agent` varchar(200) DEFAULT NULL COMMENT '娴忚鍣ㄤ俊鎭?,
  `comment_ip` varchar(50) DEFAULT NULL COMMENT 'IP',
  `comment_create_time` datetime DEFAULT NULL COMMENT '璇勮鏃堕棿',
  `comment_role` int(1) DEFAULT NULL COMMENT '瑙掕壊锛屾槸鍚︾鐞嗗憳',
  `comment_user_id` int(11) DEFAULT NULL COMMENT '璇勮ID,鍙兘涓虹┖',
  PRIMARY KEY (`comment_id`)
) ENGINE=InnoDB AUTO_INCREMENT=43 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `default_server`
--

DROP TABLE IF EXISTS `default_server`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `default_server` (
  `user_id` int(11) NOT NULL,
  `server_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `link`
--

DROP TABLE IF EXISTS `link`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `link` (
  `link_id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '閾炬帴ID',
  `link_url` varchar(255) DEFAULT NULL COMMENT 'URL',
  `link_name` varchar(255) DEFAULT NULL COMMENT '濮撳悕',
  `link_image` varchar(255) DEFAULT NULL COMMENT '澶村儚',
  `link_description` varchar(255) DEFAULT NULL COMMENT '鎻忚堪',
  `link_owner_nickname` varchar(40) DEFAULT NULL COMMENT '鎵€灞炰汉鍚嶇О',
  `link_owner_contact` varchar(255) DEFAULT NULL COMMENT '鑱旂郴鏂瑰紡',
  `link_update_time` datetime DEFAULT NULL COMMENT '鏇存柊鏃堕棿',
  `link_create_time` datetime DEFAULT NULL COMMENT '鍒涘缓鏃堕棿',
  `link_order` int(2) unsigned DEFAULT '1' COMMENT '鎺掑簭鍙?,
  `link_status` int(1) unsigned DEFAULT '1' COMMENT '鐘舵€?,
  PRIMARY KEY (`link_id`),
  UNIQUE KEY `link_name` (`link_name`)
) ENGINE=MyISAM AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `menu`
--

DROP TABLE IF EXISTS `menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `menu` (
  `menu_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '鑿滃崟ID',
  `menu_name` varchar(255) DEFAULT NULL COMMENT '鍚嶇О',
  `menu_url` varchar(255) DEFAULT NULL COMMENT 'URL',
  `menu_level` int(11) DEFAULT NULL COMMENT '绛夌骇',
  `menu_icon` varchar(255) DEFAULT NULL COMMENT '鍥炬爣',
  `menu_order` int(11) DEFAULT NULL COMMENT '鎺掑簭鍊?,
  PRIMARY KEY (`menu_id`),
  UNIQUE KEY `menu_name` (`menu_name`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `notice`
--

DROP TABLE IF EXISTS `notice`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `notice` (
  `notice_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '鍏憡ID',
  `notice_title` varchar(255) DEFAULT NULL COMMENT '鍏憡鏍囬',
  `notice_content` varchar(10000) DEFAULT NULL COMMENT '鍐呭',
  `notice_create_time` datetime DEFAULT NULL COMMENT '鍒涘缓鏃堕棿',
  `notice_update_time` datetime DEFAULT NULL COMMENT '鏇存柊鏃堕棿',
  `notice_status` int(1) unsigned DEFAULT '1' COMMENT '鐘舵€?,
  `notice_order` int(2) DEFAULT NULL COMMENT '鎺掑簭鍊?,
  PRIMARY KEY (`notice_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `options`
--

DROP TABLE IF EXISTS `options`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `options` (
  `option_id` int(11) NOT NULL,
  `option_site_title` varchar(255) DEFAULT NULL,
  `option_site_description` varchar(255) DEFAULT NULL,
  `option_meta_description` varchar(255) DEFAULT NULL,
  `option_meta_keyword` varchar(255) DEFAULT NULL,
  `option_aboutsite_avatar` varchar(255) DEFAULT NULL,
  `option_aboutsite_title` varchar(255) DEFAULT NULL,
  `option_aboutsite_content` varchar(255) DEFAULT NULL,
  `option_aboutsite_wechat` varchar(255) DEFAULT NULL,
  `option_aboutsite_qq` varchar(255) DEFAULT NULL,
  `option_aboutsite_github` varchar(255) DEFAULT NULL,
  `option_aboutsite_weibo` varchar(255) DEFAULT NULL,
  `option_tongji` varchar(255) DEFAULT NULL,
  `option_status` int(1) DEFAULT '1',
  PRIMARY KEY (`option_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `page`
--

DROP TABLE IF EXISTS `page`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `page` (
  `page_id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '鑷畾涔夐〉闈D',
  `page_key` varchar(50) DEFAULT NULL COMMENT 'key',
  `page_title` varchar(50) DEFAULT NULL COMMENT '鏍囬',
  `page_content` mediumtext COMMENT '鍐呭',
  `page_create_time` datetime DEFAULT NULL COMMENT '鍒涘缓鏃堕棿',
  `page_update_time` datetime DEFAULT NULL COMMENT '鏇存柊鏃堕棿',
  `page_view_count` int(10) unsigned DEFAULT '0' COMMENT '璁块棶閲?,
  `page_comment_count` int(5) unsigned DEFAULT '0' COMMENT '璇勮鏁?,
  `page_status` int(1) unsigned DEFAULT '1' COMMENT '鐘舵€?,
  PRIMARY KEY (`page_id`),
  UNIQUE KEY `page_key` (`page_key`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `server`
--

DROP TABLE IF EXISTS `server`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `server` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `ip` varchar(255) NOT NULL,
  `port` int(11) NOT NULL,
  `password` varchar(255) NOT NULL,
  `user_id` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tag`
--

DROP TABLE IF EXISTS `tag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tag` (
  `tag_id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '鏍囩ID',
  `tag_name` varchar(50) DEFAULT NULL COMMENT '鏍囩鍚嶇О',
  `tag_description` varchar(100) DEFAULT NULL COMMENT '鎻忚堪',
  PRIMARY KEY (`tag_id`),
  UNIQUE KEY `tag_name` (`tag_name`)
) ENGINE=InnoDB AUTO_INCREMENT=39 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `user_id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '鐢ㄦ埛ID',
  `user_name` varchar(30) NOT NULL DEFAULT '' COMMENT '鐢ㄦ埛鍚?,
  `user_pass` varchar(100) NOT NULL DEFAULT '' COMMENT '瀵嗙爜',
  `user_nickname` varchar(100) NOT NULL DEFAULT '' COMMENT '鏄电О',
  `user_email` varchar(100) DEFAULT '' COMMENT '閭',
  `user_url` varchar(100) DEFAULT '' COMMENT '涓汉涓婚〉',
  `user_avatar` varchar(100) DEFAULT NULL COMMENT '澶村儚',
  `user_last_login_ip` varchar(30) DEFAULT NULL COMMENT '涓婁紶鐧诲綍IP',
  `user_register_time` datetime DEFAULT NULL COMMENT '娉ㄥ唽鏃堕棿',
  `user_last_login_time` datetime DEFAULT NULL COMMENT '涓婁紶鐧诲綍鏃堕棿',
  `user_status` int(1) unsigned DEFAULT '1' COMMENT '鐘舵€?,
  `user_role` varchar(20) NOT NULL DEFAULT 'user' COMMENT '瑙掕壊',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `user_name` (`user_name`),
  UNIQUE KEY `user_email` (`user_email`)
) ENGINE=MyISAM AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-02-02 18:43:51
