use chatdb;

CREATE TABLE `ai_chat_memory` (
                                  `id` BIGINT       NOT NULL AUTO_INCREMENT,
                                  `conversation_id` VARCHAR(255) NOT NULL,
                                  `content`         TEXT            NOT NULL,
                                  `type`            VARCHAR(50)     NOT NULL,
                                  `timestamp`       TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                  PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;