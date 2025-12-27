--liquibase formatted sql

--changeset shyngys:1
CREATE TABLE if not exists writers
(
    id         INT UNSIGNED NOT NULL AUTO_INCREMENT,
    first_name VARCHAR(100) NOT NULL,
    last_name  VARCHAR(100),
    PRIMARY KEY (id)
);