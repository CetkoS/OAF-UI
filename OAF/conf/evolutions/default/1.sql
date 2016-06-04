# --- Created by Slick DDL
# To stop Slick DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table `USER` (`id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,`first_name` VARCHAR(254) NOT NULL,`last_name` VARCHAR(254) NOT NULL,`username` VARCHAR(254) NOT NULL,`status` INTEGER NOT NULL,`company_id` BIGINT,`role` INTEGER NOT NULL,`password` VARCHAR(254));

# --- !Downs

drop table `USER`;

