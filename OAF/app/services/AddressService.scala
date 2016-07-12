package services

import com.oaf.dal.dal.AddressDAO
import models.Address
import play.api.db.slick._

object AddressService {

  def findById(id: Long)(implicit session: Session): Option[Address] = {
    AddressDAO.findById(id).map(u => u)
  }

  def update(id: Long, address: Address)(implicit session: Session): Unit = {
    AddressDAO.update(id, address)
  }

  def delete(id: Long)(implicit session: Session): Unit = {
    AddressDAO.delete(id)
  }

}
