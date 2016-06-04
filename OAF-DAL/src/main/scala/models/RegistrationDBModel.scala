package com.oaf.dal.models

import com.oaf.dal.enums.RegistrationStatus

import scala.slick.lifted.Tag
import play.api.db.slick.Config.driver.simple._

  case class RegistrationDBModel(id: Option[Long], userId: Long, hash: String, status: RegistrationStatus.Value, email: String)


  class RegistrationTable(tag: Tag) extends Table[RegistrationDBModel](tag, "REGISTRATION") {

    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def userId = column[Long]("user_id", O.NotNull)
    def hash = column[String]("hash", O.NotNull)
    def status = column[RegistrationStatus.Value]("status", O.NotNull)
    def email = column[String]("email", O.NotNull)

    def * = (id.?, userId, hash, status, email) <> (RegistrationDBModel.tupled, RegistrationDBModel.unapply _)

    def userForeignKey = foreignKey("fk_REGISTRATION_1", userId, users)(_.id)

  }


