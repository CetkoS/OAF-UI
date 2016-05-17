package com.oaf.dal.models

import com.oaf.dal.enums.{UserRole, UserStatus}

import scala.slick.lifted.Tag
import play.api.db.slick.Config.driver.simple._

  case class UserDBModel(id: Option[Long], firstName: String, lastName: String,
                  username: String, status: UserStatus.Value, companyId: Option[Long], role: UserRole.Value, password: Option[String])


  class UserTable(tag: Tag) extends Table[UserDBModel](tag, "USER") {

    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def firstName = column[String]("first_name", O.NotNull)
    def lastName = column[String]("last_name", O.NotNull)
    def username = column[String]("username", O.NotNull)
    def status = column[UserStatus.Value]("status", O.NotNull)
    def companyId = column[Long]("company_id", O.Nullable)
    def role = column[UserRole.Value]("role", O.NotNull)
    def password = column[String]("password",O.Nullable)

    def * = (id.?, firstName, lastName, username, status, companyId.?, role, password.?) <> (UserDBModel.tupled, UserDBModel.unapply _)
  }


