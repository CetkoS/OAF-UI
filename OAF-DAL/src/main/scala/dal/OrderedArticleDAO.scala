package com.oaf.dal.dal

import com.oaf.dal.models._
import play.api.db.slick.Config.driver.simple._
import play.api.db.slick.Session


object OrderedArticleDAO extends BaseDAO[OrderedArticleFullDBModel, Long, Session]{

  def findAll(implicit session: Session): List[OrderedArticleFullDBModel] = ???

  def update(id: Long, model: OrderedArticleFullDBModel)(implicit session: Session): Unit = {
    orderedArticles.filter(_.id === id).update(model.orderedArticle)
    updateAdditives(model.additives, id)
  }

  def findById(id: Long)(implicit session: Session): Option[OrderedArticleFullDBModel] = {
    val maybeOrderedArticle = orderedArticles.filter(_.id === id).firstOption
    maybeOrderedArticle match {
      case None => None
      case Some(orderedArticle) => fetchFullModel(orderedArticle)
    }
  }

  def delete(id: Long)(implicit session: Session): Unit = {
    orderedArticleAdditive.filter(_.orderedArticleId === id).delete
    orderedArticles.filter(_.id === id).delete
  }

  def create(model: OrderedArticleFullDBModel)(implicit session: Session): Long = {
    val id = (orderedArticles returning orderedArticles.map(_.id)) += model.orderedArticle
    createAdditives(model.additives, id)
    id
  }

  def updateAdditives(additives: List[AdditiveDBModel], orderedArticleId: Long)(implicit session: Session): Unit = {
    orderedArticleAdditive.filter(_.orderedArticleId === orderedArticleId).delete
    additives.foreach(additive =>
      (orderedArticleAdditive returning orderedArticleAdditive.map(_.id)) += OrderedArticleAdditiveDBModel(None, orderedArticleId, additive.id.get)
    )
  }

  def createAdditives(additives: List[AdditiveDBModel], orderedArticleId: Long)(implicit session: Session): Unit = {
    additives.foreach(additive =>
      (orderedArticleAdditive returning orderedArticleAdditive.map(_.id)) += OrderedArticleAdditiveDBModel(None, orderedArticleId, additive.id.get)
    )
  }

  def fetchFullModel(orderedArticle: OrderedArticleDBModel)(implicit session: Session): Option[OrderedArticleFullDBModel] = {
    ArticleDAO.findById(orderedArticle.articleId) match {
      case None => None
      case Some(articleInfo) => {
        val additivesInfo = findAllOrderedAdditives(orderedArticle.id.get) map {orderedAdditive =>
          AdditiveDAO.findById(orderedAdditive.additiveId).get
        }
        Some(OrderedArticleFullDBModel(orderedArticle, additivesInfo, articleInfo))
      }
    }
  }

  def findAllOrderedAdditives(orderedArticleId: Long)(implicit session: Session): List[OrderedArticleAdditiveDBModel] = {
    orderedArticleAdditive.filter(_.orderedArticleId === orderedArticleId).list
  }

  def findByOrderId(orderId: Long)(implicit session: Session): List[OrderedArticleFullDBModel] = {
    val articles = orderedArticles.filter(_.orderId === orderId).list
    articles.map(fetchFullModel(_).get)
  }

}
