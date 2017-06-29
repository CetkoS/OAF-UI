package com.oaf.dal.models

import scala.slick.lifted.Tag
import play.api.db.slick.Config.driver.simple._

  case class AddressDBModel(id: Option[Long], addressLine: String, city: String,
                            postalCode: String, country: String, longitude: Option[Double], latitude: Option[Double])


  class AddressTable(tag: Tag) extends Table[AddressDBModel](tag, "ADDRESS") {

    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def addressLine = column[String]("address_line", O.NotNull)
    def city = column[String]("city", O.NotNull)
    def postalCode = column[String]("postal_code", O.NotNull)
    def country = column[String]("country", O.NotNull)
    def longitude = column[Double]("longitude", O.Nullable)
    def latitude = column[Double]("latitude", O.Nullable)

    def * = (id.?, addressLine, city, postalCode, country, longitude.?, latitude.?) <> (AddressDBModel.tupled, AddressDBModel.unapply _)

  }


