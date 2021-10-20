CREATE TABLE `to2`.`url_code` (
	`id` BIGINT(20) NOT NULL AUTO_INCREMENT,
	`code` VARCHAR(255) NULL DEFAULT NULL,
	`created_datetime` DATETIME(6) NOT NULL,
	`url` VARCHAR(500) NOT NULL,
	PRIMARY KEY (`id`) USING BTREE,
	UNIQUE INDEX `UK__url_code__code` (`code`) USING BTREE,
	INDEX `IDX__url_code__url` (`url`) USING BTREE
) COLLATE='utf8mb4_bin' ENGINE=InnoDB;
