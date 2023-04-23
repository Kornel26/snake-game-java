-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema assignment_snake
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema assignment_snake
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `assignment_snake` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci ;
USE `assignment_snake` ;

-- -----------------------------------------------------
-- Table `assignment_snake`.`snake`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `assignment_snake`.`snake` ;

CREATE TABLE IF NOT EXISTS `assignment_snake`.`snake` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `score` INT NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;

USE `assignment_snake` ;

-- -----------------------------------------------------
-- Placeholder table for view `assignment_snake`.`vw_snake`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `assignment_snake`.`vw_snake` (`id` INT, `name` INT, `score` INT);

-- -----------------------------------------------------
-- View `assignment_snake`.`vw_snake`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `assignment_snake`.`vw_snake`;
DROP VIEW IF EXISTS `assignment_snake`.`vw_snake` ;
USE `assignment_snake`;
CREATE  OR REPLACE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `assignment_snake`.`vw_snake` AS select `assignment_snake`.`snake`.`id` AS `id`,`assignment_snake`.`snake`.`name` AS `name`,`assignment_snake`.`snake`.`score` AS `score` from `assignment_snake`.`snake`;

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
