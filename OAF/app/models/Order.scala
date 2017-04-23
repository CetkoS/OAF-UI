package models

import java.sql.Timestamp

import com.oaf.dal.enums.{PaymentMethodEnum, DeliveryEnum, OrderStatus}
import com.oaf.dal.models.OrderDBModel


case class Order
(
  id: Option[Long],
  status: OrderStatus.Value,
  customerName: String,
  delivery: DeliveryEnum.Value,
  paymentMethod: PaymentMethodEnum.Value,
  customerPhoneNumber: String,
  sessionId: String,
  addressLine: Option[String],
  companyId: Long,
  timeToBeReady: Option[Long],
  timeCreated: Option[Timestamp]
)

object Order {

  implicit def convertToModel(orderDbModel: OrderDBModel) : Order = {
    Order(orderDbModel.id, orderDbModel.status, orderDbModel.customerName, orderDbModel.delivery,
      orderDbModel.paymentMethod, orderDbModel.customerPhoneNumber, orderDbModel.sessionId, orderDbModel.addressLine,
      orderDbModel.companyId, orderDbModel.timeToBeReady, orderDbModel.timeCreated
    )
  }

  implicit def convertToDBModel(order: Order) : OrderDBModel = {
    OrderDBModel(order.id, order.status, order.customerName, order.delivery,
      order.paymentMethod, order.customerPhoneNumber, order.sessionId, order.addressLine,
      order.companyId, order.timeToBeReady, order.timeCreated
    )
  }
}


