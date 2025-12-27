--liquibase formatted sql

--changeset shyngys:1
CREATE TABLE IF NOT EXISTS posts
(
    id        INT UNSIGNED NOT NULL AUTO_INCREMENT,
    content   TEXT,
    created   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated   DATETIME ON UPDATE CURRENT_TIMESTAMP,
    status    VARCHAR(30)  NOT NULL,
    writer_id INT UNSIGNED NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (writer_id) REFERENCES writers (id) ON DELETE CASCADE
);