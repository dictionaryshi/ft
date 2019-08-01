USE `stock`;
ALTER TABLE `stock`.`user`
  ADD `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
COMMENT '创建时间';

USE `stock`;
ALTER TABLE `stock`.`user`
  CHANGE `updatedAt` `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
COMMENT '更新时间';

ALTER TABLE `stock`.`order_goods`
  ADD INDEX idx_operator_created_at (`operator`, `created_at`);

ALTER TABLE `stock`.`user`
  ADD UNIQUE `uniq_username_password` (`username`, `password`);

ALTER TABLE `stock`.`user`
  ADD PRIMARY KEY (`id`);

ALTER TABLE `stock`.`user`
  DROP INDEX `uniq_username_password`;

ALTER TABLE `stock`.`user`
  DROP COLUMN `username`,
  DROP COLUMN `password`;

/**
    查看mysql进程
 */
SHOW PROCESSLIST;

/**
    查看管理员信息
 */
USE mysql;
SELECT *
FROM `user`;

DROP USER reader_stock;

/**
    创建用户
 */
CREATE USER reader_stock@'%'
  IDENTIFIED BY 'flzx@3qc';

/**
    查看用户权限
 */
SHOW GRANTS FOR reader_stock@'%';

/**
    分配权限
 */
GRANT SELECT ON *.* TO reader_stock@'%';
GRANT ALL ON *.* TO reader_stock@'%';

/**
    撤销权限
 */
REVOKE ALL ON *.* FROM reader_stock@'%';

