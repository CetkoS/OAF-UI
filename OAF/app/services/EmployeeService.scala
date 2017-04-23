package services

import com.oaf.dal.dal.{OrderedArticleDAO, OrderDAO}
import com.oaf.dal.enums.OrderStatus
import forms.employee.{OrderIdData, AcceptOrderData}
import models.{OrderedArticle, OrderFull, Order}
import play.api.Play.current

object EmployeeService {

  def rejectOrder(orderId: Long): Unit = {
    play.api.db.slick.DB.withTransaction { implicit session =>
      OrderDAO.delete(orderId)
    }
  }
  def acceptOrder(acceptOrderData: AcceptOrderData): Unit = {
    play.api.db.slick.DB.withTransaction { implicit session =>
        OrderDAO.acceptOrder(acceptOrderData.orderId, acceptOrderData.timeToBeReady)
      }
    }

  def updateOrderStatus(orderIdData: OrderIdData, status: OrderStatus.Value): Unit = {
    play.api.db.slick.DB.withTransaction { implicit session =>
      OrderDAO.updateOrderStatus(orderIdData.orderId, status)
    }
  }

  def getOrdersByStatus(companyId: Long, status: String): List[OrderFull] = {
    play.api.db.slick.DB.withTransaction { implicit session =>
      OrderDAO.findAllByStatus(companyId, OrderStatus.withName(status)).map(a => getFullOrdersById(a.id.get))
    }
  }

  def getFullOrdersById(orderId: Long): OrderFull = {
    play.api.db.slick.DB.withTransaction { implicit session =>
          val order = Order.convertToModel(OrderDAO.findById(orderId).getOrElse(throw new IllegalArgumentException()))
          val orderedArticles = OrderedArticleDAO.findByOrderId(orderId).map(OrderedArticle.convertToModel(_))
          val totalSum = orderedArticles.foldRight(0.0)((a,b) => a.articleInfo.price + b)
          OrderFull(order, orderedArticles, totalSum)
    }
  }

}
