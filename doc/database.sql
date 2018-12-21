USE consign;
ALTER TABLE `consign`.`task`
  ADD `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
COMMENT '创建时间';

USE consign;
ALTER TABLE `consign`.`task`
  MODIFY COLUMN `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
  COMMENT '更新时间';

ALTER TABLE `consign`.`car`
  ADD INDEX idx_carSeries_carModel (`car_series`, `car_model`);

ALTER TABLE `consign`.`car`
  ADD UNIQUE `idx_carSeries_carModel` (`car_series`, `car_model`);

ALTER TABLE `consign`.`car`
  ADD PRIMARY KEY (`id`);

ALTER TABLE `consign`.`car`
  DROP PRIMARY KEY;

ALTER TABLE `consign`.`car`
  DROP INDEX `idx_carSeries_carModel`;

ALTER TABLE `consign`.`discount_pool_log`
  DROP COLUMN `guarantee_clue_id`,
  DROP COLUMN `kill_clue_id`;

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

/**
    创建用户
 */
CREATE USER reader@'%'
  IDENTIFIED BY 'reader_naodian12300';

/**
    删除用户
 */
DROP USER reader@'%';

/**
    查看用户权限
 */
SHOW GRANTS FOR reader@'%';

/**
    分配权限
 */
GRANT SELECT ON *.* TO reader@'%';
GRANT ALL ON *.* TO reader@'%';

/**
    撤销权限
 */
REVOKE ALL ON *.* FROM reader@'%';

/**
    修改秘密
 */
USE mysql;

UPDATE `user` t
SET
  PASSWORD = PASSWORD('963721')
WHERE
  t.User = 'reader' AND t.Host = '%';

FLUSH PRIVILEGES;

