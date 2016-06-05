package models

import scala.slick.lifted.Tag
import play.api.db.slick.Config.driver.simple._

  case class CompanyDBModel(id: Option[Long], name: String, description: String, addressId: Long,
                            logoUrl: String, phoneNumber: String, ownerId: Long)


  class CompanyTable(tag: Tag) extends Table[CompanyDBModel](tag, "COMPANY") {

    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def name = column[String]("name", O.NotNull)
    def description = column[String]("description", O.NotNull)
    def addressId = column[Long]("address_id", O.NotNull)
    def logoUrl = column[String]("logo_url", O.NotNull)
    def phoneNumber = column[String]("phone_number", O.NotNull)
    def ownerId = column[Long]("owner_id", O.NotNull)


    def * = (id.?, name, description, addressId, logoUrl, phoneNumber, ownerId) <> (CompanyDBModel.tupled, CompanyDBModel.unapply _)

   // def userForeignKey = foreignKey("fk_REGISTRATION_1", userId, users)(_.id)

  }


