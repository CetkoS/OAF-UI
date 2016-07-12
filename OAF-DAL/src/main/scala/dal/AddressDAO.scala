package com.oaf.dal.dal

import com.oaf.dal.models._
import models.AddressDBModel
import play.api.db.slick.Config.driver.simple._
import play.api.db.slick.Session


object AddressDAO extends BaseDAO[AddressDBModel,Long, Session]{


  def findAll(implicit session: Session): List[AddressDBModel] = {
    addresses.list
  }

  def update(id: Long, model: AddressDBModel)(implicit session: Session): Unit = {
    addresses.filter(_.id === id).update(model)
  }

  def findById(id: Long)(implicit session: Session): Option[AddressDBModel] = {
    addresses.filter(_.id === id).firstOption
  }

  def delete(id: Long)(implicit session: Session): Unit = {
      ???
  }

  def create(model: AddressDBModel)(implicit session: Session): Long = {
    (addresses returning addresses.map(_.id)) += model
  }

}
