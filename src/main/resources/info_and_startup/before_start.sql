
CREATE DATABASE `rockstars-db`;
USE `rockstars-db`;

CREATE USER 'rockstars-user' IDENTIFIED BY 'rockstars123';

GRANT ALL PRIVILEGES ON * . * TO 'rockstars-user';

FLUSH PRIVILEGES;


