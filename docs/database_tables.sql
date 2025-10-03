;;
;; Note:
;; if you want email notification when inventory is low you will need
;; quartz database, look quartz.properties.example to set it up
;; after you change the parameters, you can drop this file in the
;; ./src/resources/ folder with log4j2.xml or log4j.properties
;; you need all quartz table installed download from their website
;;
;; for quartz you will need the following tables
;; QRTZ_BLOB_TRIGGERS, QRTZ_CALENDARS, QRTZ_CRON_TRIGGERS,
;; QRTZ_FIRED_TRIGGERS  , QRTZ_JOB_DETAILS, QRTZ_LOCKS               
;; QRTZ_PAUSED_TRIGGER_GRPS, QRTZ_SCHEDULER_STATE, QRTZ_SIMPLE_TRIGGERS     
;; QRTZ_SIMPROP_TRIGGERS, QRTZ_TRIGGERS
CREATE TABLE `batches` (
  `id` int NOT NULL AUTO_INCREMENT,
  `conf_id` int DEFAULT NULL,
  `batch_size` int DEFAULT NULL,
  `status` enum('Printed','Waiting') DEFAULT NULL,
  `start_seq` int DEFAULT NULL,
  `date` date DEFAULT NULL,
  `user_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `conf_id` (`conf_id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `batches_ibfk_1` FOREIGN KEY (`conf_id`) REFERENCES `buck_confs` (`id`),
  CONSTRAINT `batches_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2583 DEFAULT CHARSET=utf8mb3;
;;
 CREATE TABLE `buck_confs` (
  `id` int NOT NULL AUTO_INCREMENT,
  `value` int DEFAULT NULL,
  `type_id` int DEFAULT NULL,
  `date` date DEFAULT NULL,
  `donor_max_value` int NOT NULL,
  `user_id` int DEFAULT NULL,
  `name` varchar(50) DEFAULT NULL,
  `gl_account` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  KEY `type_id` (`type_id`),
  CONSTRAINT `buck_confs_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `buck_confs_ibfk_2` FOREIGN KEY (`type_id`) REFERENCES `buck_types` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb3 ;
;;
CREATE TABLE `buck_seq` (
  `id` int NOT NULL AUTO_INCREMENT,
  `batch_id` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=207913 DEFAULT CHARSET=utf8mb3
;;
CREATE TABLE `buck_types` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb3;
;;
CREATE TABLE `cancelled_bucks` (
  `id` int NOT NULL,
  `user_id` int DEFAULT NULL,
  `date_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `cancelled_bucks_ibfk_1` FOREIGN KEY (`id`) REFERENCES `bucks` (`id`),
  CONSTRAINT `cancelled_bucks_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ;
;;
 CREATE TABLE `disputes` (
  `id` int NOT NULL AUTO_INCREMENT,
  `redeem_id` int NOT NULL,
  `buck_id` int NOT NULL,
  `status` enum('Waiting','Rejected','Resolved') DEFAULT NULL,
  `reason` enum('Expired','Not Exist','Not Issued','Redeemed','Voided') DEFAULT NULL,
  `user_id` int DEFAULT NULL,
  `date_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `suggestions` varchar(500) DEFAULT NULL,
  `notes` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `redeem_id` (`redeem_id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `disputes_ibfk_1` FOREIGN KEY (`redeem_id`) REFERENCES `redeems` (`id`),
  CONSTRAINT `disputes_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1624 DEFAULT CHARSET=utf8mb3;
;;
CREATE TABLE `ebt_bucks` (
  `ebt_id` int NOT NULL,
  `buck_id` int NOT NULL,
  PRIMARY KEY (`buck_id`),
  KEY `ebt_id` (`ebt_id`),
  CONSTRAINT `ebt_bucks_ibfk_1` FOREIGN KEY (`ebt_id`) REFERENCES `ebts` (`id`),
  CONSTRAINT `ebt_bucks_ibfk_2` FOREIGN KEY (`buck_id`) REFERENCES `bucks` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ;
;;
 CREATE TABLE `ebts` (
  `id` int NOT NULL AUTO_INCREMENT,
  `amount` int DEFAULT NULL,
  `approve` varchar(30) DEFAULT NULL,
  `card_last_4` varchar(30) DEFAULT NULL,
  `user_id` int DEFAULT NULL,
  `date_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `dmb_amount` int DEFAULT NULL,
  `cancelled` char(1) DEFAULT NULL,
  `ebt_donor_max` int DEFAULT '18',
  `ebt_buck_value` int DEFAULT '3',
  `dispute_resolution` char(1) DEFAULT NULL,
  `include_double` char(1) DEFAULT 'y',
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `ebts_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8631 DEFAULT CHARSET=utf8mb3 ;
;;
 CREATE TABLE `export_redeems` (
  `export_id` int NOT NULL,
  `redeem_id` int NOT NULL,
  PRIMARY KEY (`redeem_id`),
  KEY `export_id` (`export_id`),
  CONSTRAINT `export_redeems_ibfk_1` FOREIGN KEY (`export_id`) REFERENCES `exports` (`id`),
  CONSTRAINT `export_redeems_ibfk_2` FOREIGN KEY (`redeem_id`) REFERENCES `redeems` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
;;
CREATE TABLE `exports` (
  `id` int NOT NULL AUTO_INCREMENT,
  `date_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `user_id` int DEFAULT NULL,
  `nw_batch_name` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `exports_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=331 DEFAULT CHARSET=utf8mb3;
;;
 CREATE TABLE `fmnp_seniors` (
  `id` int NOT NULL AUTO_INCREMENT,
  `ticket_num` varchar(20) DEFAULT NULL,
  `amount` int DEFAULT NULL,
  `senior_max_amount` int DEFAULT NULL,
  `user_id` int DEFAULT NULL,
  `date_time` datetime DEFAULT NULL,
  `cancelled` char(1) DEFAULT NULL,
  `dispute_resolution` char(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `fmnp_seniors_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=761 DEFAULT CHARSET=utf8mb3 ;
;;
CREATE TABLE `fmnp_wics` (
  `id` int NOT NULL AUTO_INCREMENT,
  `ticket_num` varchar(20) DEFAULT NULL,
  `amount` int DEFAULT NULL,
  `wic_max_amount` int DEFAULT NULL,
  `user_id` int DEFAULT NULL,
  `date_time` datetime DEFAULT NULL,
  `cancelled` char(1) DEFAULT NULL,
  `dispute_resolution` char(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `fmnp_wics_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1012 DEFAULT CHARSET=utf8mb3 ;
;;
CREATE TABLE `gift_bucks` (
  `gift_id` int NOT NULL,
  `buck_id` int NOT NULL,
  PRIMARY KEY (`buck_id`),
  KEY `gift_id` (`gift_id`),
  CONSTRAINT `gift_bucks_ibfk_1` FOREIGN KEY (`gift_id`) REFERENCES `gifts` (`id`),
  CONSTRAINT `gift_bucks_ibfk_2` FOREIGN KEY (`buck_id`) REFERENCES `bucks` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ;
;;
CREATE TABLE `gifts` (
  `id` int NOT NULL AUTO_INCREMENT,
  `amount` int DEFAULT NULL,
  `pay_type` enum('Cash','Check','Money Order','Credit Card','Honorary','Dispute Resolution') DEFAULT NULL,
  `check_no` varchar(20) DEFAULT NULL,
  `user_id` int DEFAULT NULL,
  `date_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `cancelled` char(1) DEFAULT NULL,
  `dispute_resolution` char(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `gifts_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3429 DEFAULT CHARSET=utf8mb3;
;;
CREATE TABLE `gl_accounts` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb3;
;;
CREATE TABLE `mail_notifications` (
  `id` int NOT NULL,
  `super_user` char(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `mail_notifications_ibfk_1` FOREIGN KEY (`id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
;;
CREATE TABLE `market_rx` (
  `id` int NOT NULL AUTO_INCREMENT,
  `voucher_num` int DEFAULT NULL,
  `amount` int DEFAULT NULL,
  `rx_max_amount` int DEFAULT NULL,
  `user_id` int DEFAULT NULL,
  `date_time` datetime DEFAULT NULL,
  `cancelled` char(1) DEFAULT NULL,
  `dispute_resolution` char(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `market_rx_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb3 ;
;;
 CREATE TABLE `redeem_bucks` (
  `redeem_id` int NOT NULL,
  `buck_id` int NOT NULL,
  PRIMARY KEY (`buck_id`),
  KEY `redeem_id` (`redeem_id`),
  CONSTRAINT `redeem_bucks_ibfk_1` FOREIGN KEY (`redeem_id`) REFERENCES `redeems` (`id`),
  CONSTRAINT `redeem_bucks_ibfk_2` FOREIGN KEY (`buck_id`) REFERENCES `bucks` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
;;
CREATE TABLE `redeems` (
  `id` int NOT NULL AUTO_INCREMENT,
  `date_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `vendor_id` int DEFAULT NULL,
  `user_id` int DEFAULT NULL,
  `status` enum('Open','Completed') DEFAULT NULL,
  `notes` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  KEY `vendor_id` (`vendor_id`),
  CONSTRAINT `redeems_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `redeems_ibfk_2` FOREIGN KEY (`vendor_id`) REFERENCES `vendors` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3105 DEFAULT CHARSET=utf8mb3;
;;
CREATE TABLE `resolutions` (
  `id` int NOT NULL AUTO_INCREMENT,
  `dispute_id` int NOT NULL,
  `conf_id` int DEFAULT NULL,
  `value` int DEFAULT NULL,
  `approve` varchar(30) DEFAULT NULL,
  `card_last_4` varchar(4) DEFAULT NULL,
  `pay_type` varchar(30) DEFAULT NULL,
  `check_no` varchar(20) DEFAULT NULL,
  `expire_date` date DEFAULT NULL,
  `user_id` int DEFAULT NULL,
  `date_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `status` enum('Success','Failure') DEFAULT NULL,
  `new_buck_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `dispute_id` (`dispute_id`),
  KEY `conf_id` (`conf_id`),
  KEY `user_id` (`user_id`),
  KEY `new_buck_id` (`new_buck_id`),
  CONSTRAINT `resolutions_ibfk_1` FOREIGN KEY (`dispute_id`) REFERENCES `disputes` (`id`),
  CONSTRAINT `resolutions_ibfk_2` FOREIGN KEY (`conf_id`) REFERENCES `buck_confs` (`id`),
  CONSTRAINT `resolutions_ibfk_3` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `resolutions_ibfk_4` FOREIGN KEY (`new_buck_id`) REFERENCES `bucks` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1287 DEFAULT CHARSET=utf8mb3 ;
;;
CREATE TABLE `rx_bucks` (
  `rx_id` int NOT NULL DEFAULT '0',
  `buck_id` int NOT NULL DEFAULT '0',
  PRIMARY KEY (`rx_id`,`buck_id`),
  UNIQUE KEY `buck_id` (`buck_id`),
  CONSTRAINT `rx_bucks_ibfk_1` FOREIGN KEY (`rx_id`) REFERENCES `market_rx` (`id`),
  CONSTRAINT `rx_bucks_ibfk_2` FOREIGN KEY (`buck_id`) REFERENCES `bucks` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ;
;;
CREATE TABLE `senior_bucks` (
  `senior_id` int NOT NULL DEFAULT '0',
  `buck_id` int NOT NULL DEFAULT '0',
  PRIMARY KEY (`senior_id`,`buck_id`),
  UNIQUE KEY `buck_id` (`buck_id`),
  CONSTRAINT `senior_bucks_ibfk_1` FOREIGN KEY (`senior_id`) REFERENCES `fmnp_seniors` (`id`),
  CONSTRAINT `senior_bucks_ibfk_2` FOREIGN KEY (`buck_id`) REFERENCES `bucks` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ;
;;
CREATE TABLE `snap_purchases` (
  `id` int NOT NULL AUTO_INCREMENT,
  `date` datetime DEFAULT NULL,
  `card_number` varchar(10) DEFAULT NULL,
  `authorization` varchar(10) DEFAULT NULL,
  `snap_amount` double(10,2) DEFAULT NULL,
  `ebt_amount` double(10,2) DEFAULT NULL,
  `dbl_amount` double(10,2) DEFAULT NULL,
  `dbl_max` double(10,2) DEFAULT NULL,
  `include_double` char(1) DEFAULT 'y',
  `user_id` int DEFAULT NULL,
  `cancelled` char(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `snap_purchases_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=333 DEFAULT CHARSET=utf8mb3 ;
;;
CREATE TABLE `sweep_bucks` (
  `sweep_id` int NOT NULL,
  `buck_id` int NOT NULL,
  PRIMARY KEY (`buck_id`),
  KEY `sweep_id` (`sweep_id`),
  CONSTRAINT `sweep_bucks_ibfk_1` FOREIGN KEY (`sweep_id`) REFERENCES `sweeps` (`id`),
  CONSTRAINT `sweep_bucks_ibfk_2` FOREIGN KEY (`buck_id`) REFERENCES `bucks` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
;;
CREATE TABLE `sweeps` (
  `id` int NOT NULL AUTO_INCREMENT,
  `date_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `user_id` int DEFAULT NULL,
  `sweep_name` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `sweeps_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ;
;;
CREATE TABLE `users` (
  `id` int NOT NULL AUTO_INCREMENT,
  `userid` varchar(50) DEFAULT NULL,
  `fullname` varchar(70) DEFAULT NULL,
  `role` enum('Edit','Edit:Delete','Admin:Edit:Delete') DEFAULT NULL,
  `inactive` char(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `userid` (`userid`),
  UNIQUE KEY `userid_2` (`userid`)
) ENGINE=InnoDB AUTO_INCREMENT=89 DEFAULT CHARSET=utf8mb3;
;;
CREATE TABLE `vendor_updates` (
  `id` int NOT NULL AUTO_INCREMENT,
  `date` date DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=340 DEFAULT CHARSET=utf8mb3 ;
;;
 CREATE TABLE `vendors` (
  `id` int NOT NULL AUTO_INCREMENT,
  `vendor_num` int DEFAULT NULL,
  `lname` varchar(50) DEFAULT NULL,
  `fname` varchar(30) DEFAULT NULL,
  `business_name` varchar(150) DEFAULT NULL,
  `payType` enum('MB:GC','GC') DEFAULT 'MB:GC',
  `active` char(1) DEFAULT NULL,
  UNIQUE KEY `id` (`id`),
  UNIQUE KEY `vendor_num` (`vendor_num`)
) ENGINE=InnoDB AUTO_INCREMENT=54561 DEFAULT CHARSET=utf8mb3;
;;






