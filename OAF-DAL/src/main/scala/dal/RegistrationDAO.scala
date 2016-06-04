package com.oaf.dal.dal

import com.oaf.dal.enums.RegistrationStatus
import play.api.db.slick.Config.driver.simple._
import play.api.db.slick.Session
import com.oaf.dal.models._


object RegistrationDAO extends BaseDAO[RegistrationDBModel,Long, Session]{


  def findAll(implicit session: Session): List[RegistrationDBModel] = {
    registrations.filter(u => u.status === RegistrationStatus.Active).list
  }

  def update(id: Long, model: RegistrationDBModel)(implicit session: Session): Unit = {
    registrations.filter(_.id === id).update(model)
  }

  def findById(id: Long)(implicit session: Session): Option[RegistrationDBModel] = {
    registrations.filter(_.id === id).firstOption
  }

  def delete(id: Long)(implicit session: Session): Unit = {
    val q = for { u <- registrations if u.id === id } yield u.status
    q.update(RegistrationStatus.Inactive).run
  }

  def create(model: RegistrationDBModel)(implicit session: Session): Long = {
    (registrations returning registrations.map(_.id)) += model
  }
}
