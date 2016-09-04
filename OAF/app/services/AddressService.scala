package services

import com.oaf.dal.dal.AddressDAO
import models.Address
import play.api.db.slick._
import play.api.Play.current

object AddressService {

  def findById(id: Long): Option[Address] = {
    play.api.db.slick.DB.withTransaction { implicit session =>
      AddressDAO.findById(id).map(u => u)
    }
  }

  def update(id: Long, address: Address): Unit = {
    play.api.db.slick.DB.withTransaction { implicit session =>
      AddressDAO.update(id, address)
    }
  }

  def delete(id: Long): Unit = {
    play.api.db.slick.DB.withTransaction { implicit session =>
      AddressDAO.delete(id)
    }
  }

}
