package com.oaf.dal.dal

import com.oaf.dal.enums.AdditiveStatus
import com.oaf.dal.models._
import play.api.db.slick.Config.driver.simple._
import play.api.db.slick.Session


object AdditiveDAO extends BaseDAO[AdditiveDBModel,Long, Session]{


  def findAll(implicit session: Session): List[AdditiveDBModel] = {
    additives.filter(a => a.status === AdditiveStatus.Active).list
  }

  def update(id: Long, model: AdditiveDBModel)(implicit session: Session): Unit = {
    additives.filter(_.id === id).update(model)
  }

  def findById(id: Long)(implicit session: Session): Option[AdditiveDBModel] = {
    additives.filter(_.id === id).firstOption
  }

  def findByNameAndCompany(name: String, companyId: Long)(implicit session: Session): Option[AdditiveDBModel] = {
    additives.filter(additive => additive.companyId === companyId && additive.name === name).firstOption
  }

  def delete(id: Long)(implicit session: Session): Unit = {
    val q = for { u <- additives if u.id === id } yield u.status
    q.update(AdditiveStatus.Inactive).run
  }

  def create(model: AdditiveDBModel)(implicit session: Session): Long = {
    (additives returning additives.map(_.id)) += model
  }

  def findAllByCompanyId(companyId: Long)(implicit session: Session): List[AdditiveDBModel] = {
    additives.filter(a => a.status === AdditiveStatus.Active && a.companyId === companyId).list
  }

}
