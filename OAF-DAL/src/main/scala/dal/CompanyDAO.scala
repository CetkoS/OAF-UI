package com.oaf.dal.dal

import com.oaf.dal.models._
import models.CompanyDBModel
import play.api.db.slick.Config.driver.simple._
import play.api.db.slick.Session


object CompanyDAO extends BaseDAO[CompanyDBModel,Long, Session]{


  def findAll(implicit session: Session): List[CompanyDBModel] = {
    companies.list
  }

  def update(id: Long, model: CompanyDBModel)(implicit session: Session): Unit = {
    companies.filter(_.id === id).update(model)
  }

  def findById(id: Long)(implicit session: Session): Option[CompanyDBModel] = {
    companies.filter(_.id === id).firstOption
  }

  def delete(id: Long)(implicit session: Session): Unit = {
      ???
  }

  def create(model: CompanyDBModel)(implicit session: Session): Long = {
    (companies returning companies.map(_.id)) += model
  }

}
