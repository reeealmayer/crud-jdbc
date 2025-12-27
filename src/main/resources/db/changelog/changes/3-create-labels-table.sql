--liquibase formatted sql

--changeset shyngys:1
CREATE TABLE labels
(
    id   INT UNSIGNED NOT NULL AUTO_INCREMENT,
    name VARCHAR(100),
    PRIMARY KEY (id)
);