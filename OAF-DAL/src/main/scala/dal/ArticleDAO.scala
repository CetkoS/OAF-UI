package com.oaf.dal.dal

import com.oaf.dal.enums.ArticleStatus
import com.oaf.dal.models._
import models.{ArticleDBModel, CompanyDBModel}
import play.api.db.slick.Config.driver.simple._
import play.api.db.slick.Session


object ArticleDAO extends BaseDAO[ArticleDBModel,Long, Session]{


  def findAll(implicit session: Session): List[ArticleDBModel] = {
    articles.filter(a => a.status === ArticleStatus.Active).list
  }

  def update(id: Long, model: ArticleDBModel)(implicit session: Session): Unit = {
    articles.filter(_.id === id).update(model)
  }

  def findById(id: Long)(implicit session: Session): Option[ArticleDBModel] = {
    articles.filter(_.id === id).firstOption
  }

  def delete(id: Long)(implicit session: Session): Unit = {
    val q = for { u <- articles if u.id === id } yield u.status
    q.update(ArticleStatus.Inactive).run
  }

  def create(model: ArticleDBModel)(implicit session: Session): Long = {
    (articles returning articles.map(_.id)) += model
  }

  def findAllByCompanyId(companyId: Long)(implicit session: Session): List[ArticleDBModel] = {
    articles.filter(a => a.status === ArticleStatus.Active && a.companyId === companyId).list
  }

}
