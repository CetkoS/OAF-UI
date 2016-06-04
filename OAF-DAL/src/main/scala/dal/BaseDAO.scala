package com.oaf.dal.dal

/**
  * Generic DAO.
  *
  * @tparam Model Entity model
  * @tparam ID Auto-generated ID
  * @tparam Session Session value
  */
abstract class BaseDAO[Model, ID, Session] {

  def findAll(implicit session: Session): List[Model]

  def findById(id: ID)(implicit session: Session): Option[Model]

  def create(model: Model)(implicit session: Session): ID

  def update(id: ID, model: Model)(implicit session: Session): Unit

  def delete(id: ID)(implicit session: Session): Unit
}