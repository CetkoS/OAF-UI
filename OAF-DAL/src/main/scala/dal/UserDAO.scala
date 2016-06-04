package com.oaf.dal.dal

import com.oaf.dal.enums.UserStatus
import com.oaf.dal.models.UserDBModel
import play.api.db.slick.Session
import com.oaf.dal.models._
import play.api.db.slick.Config.driver.simple._


object UserDAO extends BaseDAO[UserDBModel,Long, Session]{


  def findAll(implicit session: Session): List[UserDBModel] = {
    users.filter(u => u.status === UserStatus.Active).list
  }

  def update(id: Long, model: UserDBModel)(implicit session: Session): Unit = {
    users.filter(_.id === id).update(model)
  }

  def findById(id: Long)(implicit session: Session): Option[UserDBModel] = {
    users.filter(_.id === id).firstOption
  }

  def delete(id: Long)(implicit session: Session): Unit = {
    val q = for { u <- users if u.id === id } yield u.status
    q.update(UserStatus.Inactive).run
  }

  def create(model: UserDBModel)(implicit session: Session): Long = {
    (users returning users.map(_.id)) += model
  }

  def findByUsername(username: String)(implicit session: Session): Option[UserDBModel] = {
    users.filter(_.username === username).firstOption
  }


}
