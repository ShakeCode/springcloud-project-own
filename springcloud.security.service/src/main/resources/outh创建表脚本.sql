#
Host: 127.0.0.1  (Version 5.7.21)
# Date: 2018-09-26 15:17:51
# Generator: MySQL-Front 6.0  (Build 2.20)


#
# Structure for table "clientdetails"
#

DROP TABLE IF EXISTS `clientdetails`;
CREATE TABLE `clientdetails`
(
    `appId`                  varchar(128) NOT NULL,
    `resourceIds`            varchar(256)  DEFAULT NULL,
    `appSecret`              varchar(256)  DEFAULT NULL,
    `scope`                  varchar(256)  DEFAULT NULL,
    `grantTypes`             varchar(256)  DEFAULT NULL,
    `redirectUrl`            varchar(256)  DEFAULT NULL,
    `authorities`            varchar(256)  DEFAULT NULL,
    `access_token_validity`  int(11) DEFAULT NULL,
    `refresh_token_validity` int(11) DEFAULT NULL,
    `additionalInformation`  varchar(4096) DEFAULT NULL,
    `autoApproveScopes`      varchar(256)  DEFAULT NULL,
    PRIMARY KEY (`appId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#
# Data for table "clientdetails"
#


#
# Structure for table "oauth_access_token"
#

DROP TABLE IF EXISTS `oauth_access_token`;
CREATE TABLE `oauth_access_token`
(
    `token_id`          varchar(256) DEFAULT NULL,
    `token`             blob,
    `authentication_id` varchar(128) NOT NULL,
    `user_name`         varchar(256) DEFAULT NULL,
    `client_id`         varchar(256) DEFAULT NULL,
    `authentication`    blob,
    `refresh_token`     varchar(256) DEFAULT NULL,
    PRIMARY KEY (`authentication_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#
# Data for table "oauth_access_token"
#


#
# Structure for table "oauth_approvals"
#

DROP TABLE IF EXISTS `oauth_approvals`;
CREATE TABLE `oauth_approvals`
(
    `userId`         varchar(256) DEFAULT NULL,
    `clientId`       varchar(256) DEFAULT NULL,
    `scope`          varchar(256) DEFAULT NULL,
    `status`         varchar(10)  DEFAULT NULL,
    `expiresAt`      datetime     DEFAULT NULL,
    `lastModifiedAt` datetime     DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#
# Data for table "oauth_approvals"
#


#
# Structure for table "oauth_client_details"
#

DROP TABLE IF EXISTS `oauth_client_details`;
CREATE TABLE `oauth_client_details`
(
    `client_id`               varchar(128) NOT NULL,
    `resource_ids`            varchar(256)  DEFAULT NULL,
    `client_secret`           varchar(256)  DEFAULT NULL,
    `scope`                   varchar(256)  DEFAULT NULL,
    `authorized_grant_types`  varchar(256)  DEFAULT NULL,
    `web_server_redirect_uri` varchar(256)  DEFAULT NULL,
    `authorities`             varchar(256)  DEFAULT NULL,
    `access_token_validity`   int(11) DEFAULT NULL,
    `refresh_token_validity`  int(11) DEFAULT NULL,
    `additional_information`  varchar(4096) DEFAULT NULL,
    `autoapprove`             varchar(256)  DEFAULT NULL,
    PRIMARY KEY (`client_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#
# Data for table "oauth_client_details"
#

INSERT INTO `oauth_client_details` VALUES ('client',NULL,'secret','app','authorization_code','http://www.baidu.com',NULL,NULL,NULL,NULL,NULL);

#
# Structure for table "oauth_client_token"
#

DROP TABLE IF EXISTS `oauth_client_token`;
CREATE TABLE `oauth_client_token`
(
    `token_id`          varchar(256) DEFAULT NULL,
    `token`             blob,
    `authentication_id` varchar(128) NOT NULL,
    `user_name`         varchar(256) DEFAULT NULL,
    `client_id`         varchar(256) DEFAULT NULL,
    PRIMARY KEY (`authentication_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#
# Data for table "oauth_client_token"
#


#
# Structure for table "oauth_code"
#

DROP TABLE IF EXISTS `oauth_code`;
CREATE TABLE `oauth_code`
(
    `code`           varchar(256) DEFAULT NULL,
    `authentication` blob
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#
# Data for table "oauth_code"
#


#
# Structure for table "oauth_refresh_token"
#

DROP TABLE IF EXISTS `oauth_refresh_token`;
CREATE TABLE `oauth_refresh_token`
(
    `token_id`       varchar(256) DEFAULT NULL,
    `token`          blob,
    `authentication` blob
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

