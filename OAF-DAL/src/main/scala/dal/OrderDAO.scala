package com.oaf.dal.dal

import com.oaf.dal.enums.OrderStatus
import com.oaf.dal.models._
import play.api.db.slick.Config.driver.simple._
import play.api.db.slick.Session


object OrderDAO extends BaseDAO[OrderDBModel,Long, Session]{



  def findAll(implicit session: Session): List[OrderDBModel] = {
    orders.list
  }

  def update(id: Long, model: OrderDBModel)(implicit session: Session): Unit = {
    orders.filter(_.id === id).update(model)
  }

  def findById(id: Long)(implicit session: Session): Option[OrderDBModel] = {
    orders.filter(_.id === id).firstOption
  }

  def delete(id: Long)(implicit session: Session): Unit = {
    val q = for { u <- orders if u.id === id } yield u.status
    q.update(OrderStatus.Inactive).run
  }

  def create(model: OrderDBModel)(implicit session: Session): Long = {
    (orders returning orders.map(_.id)) += model
  }

  def findAllByStatus(companyId: Long, status: OrderStatus.Value)(implicit session: Session): List[OrderDBModel] = {
    orders.filter(a => a.status === status && a.companyId === companyId).list
  }

  def findBySessionId(companyId: Long, sessionId: String)(implicit session: Session): Option[OrderDBModel] = {
    orders.filter(a => a.companyId === companyId && a.sessionId === sessionId && a.status === OrderStatus.Pending).firstOption
  }

  def acceptOrder(id: Long, timeToBeReady: Long)(implicit session: Session): Unit = {
    val q = for { order <- orders if order.id === id } yield (order.status, order.timeToBeReady)
    q.update(OrderStatus.Active, timeToBeReady)
  }

  def updateOrderStatus(id: Long, status: OrderStatus.Value)(implicit session: Session): Unit = {
    val q = for { order <- orders if order.id === id } yield (order.status)
    q.update(status)
  }

}
