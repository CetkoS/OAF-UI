package services

import com.oaf.dal.dal.{OrderedArticleDAO, OrderDAO}
import com.oaf.dal.enums.{PaymentMethodEnum, DeliveryEnum, OrderStatus}
import forms.customer.AddToCartData
import models.{OrderFull, OrderedArticle, Order}
import play.api.Play.current



object CustomerService {

  def createPendingOrder(sessionId: String, companyId: Long): Long = {
    play.api.db.slick.DB.withTransaction { implicit session =>
      val order = Order(None, OrderStatus.Pending, "", DeliveryEnum.No, PaymentMethodEnum.Cache, "", sessionId, None, companyId)
      OrderDAO.create(order)
    }
  }

  def getOrderBySessionId(companyId: Long, sessionId: String): Option[Order] = {
    play.api.db.slick.DB.withTransaction { implicit session =>
        OrderDAO.findBySessionId(companyId, sessionId).map(Order.convertToModel(_))
    }
  }

  def createOrderedArticle(orderId: Long, addToCartData: AddToCartData): Long = {
    play.api.db.slick.DB.withTransaction { implicit session =>
      val article = ArticleService.findById(addToCartData.articleId)
      val additives = addToCartData.additiveIds.map(AdditiveService.findById(_).get)
      val orderedArticle = OrderedArticle(None, orderId, article.get, additives)
      OrderedArticleDAO.create(orderedArticle)
    }
  }

  def getFullOrderBySessionId(companyId: Long, sessionId: String): Option[OrderFull] = {
    play.api.db.slick.DB.withTransaction { implicit session =>
      getOrderBySessionId(companyId, sessionId) match {
        case None => None
        case Some(order) => {
          val orderedArticles = OrderedArticleDAO.findByOrderId(order.id.get).map(OrderedArticle.convertToModel(_))
          val totalSum = orderedArticles.foldRight(0.0)((a,b) => a.articleInfo.price + b)
          Some(OrderFull(order, orderedArticles, totalSum))
        }
      }
    }
  }

}
