package services

import java.sql.Timestamp

import com.oaf.dal.dal.{OrderedArticleDAO, OrderDAO}
import com.oaf.dal.enums.{PaymentMethodEnum, DeliveryEnum, OrderStatus}
import forms.customer.{SubmitOrderData, AddToCartData}
import models.{OrderFull, OrderedArticle, Order}
import org.joda.time.DateTime
import play.api.Play.current



object CustomerService {

  def createPendingOrder(sessionId: String, companyId: Long): Long = {
    play.api.db.slick.DB.withTransaction { implicit session =>
      val order = Order(None, OrderStatus.Pending, "", DeliveryEnum.No, PaymentMethodEnum.Cash, "", sessionId, None, companyId, None, None)
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

  def deleteFullOrder(companyId: Long, sessionId: String): Unit = {
    play.api.db.slick.DB.withTransaction { implicit session =>
      getFullOrderBySessionId(companyId, sessionId) match {
        case None => None
        case Some(order) => {
          order.orderedArticles.map(article => OrderedArticleDAO.delete(article.id.get))
          OrderDAO.delete(order.orderInfo.id.get)
        }
      }
    }
  }

  def deleteOrderedArticle(artcleId: Long): Unit = {
    play.api.db.slick.DB.withTransaction { implicit session =>
      OrderedArticleDAO.delete(artcleId)
    }
  }

  def submitOrder(orderData: SubmitOrderData): Unit = {
    play.api.db.slick.DB.withTransaction { implicit session =>
          OrderDAO.findById(orderData.orderId) match {
            case None => throw new IllegalArgumentException()
            case Some(oldOrder) => {
              val newOrder = Order( oldOrder.id,
                OrderStatus.New,
                orderData.customerName,
                DeliveryEnum.withName(orderData.delivery),
                PaymentMethodEnum.withName(orderData.paymentMethod),
                orderData.phoneNumber,
                oldOrder.sessionId,
                orderData.addressLine,
                orderData.companyId,
                oldOrder.timeToBeReady,
                Some(new Timestamp(System.currentTimeMillis())))
              OrderDAO.update(orderData.orderId, newOrder)
            }
          }
      }
    }

}
