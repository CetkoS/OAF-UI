package com.oaf.dal.models

import com.oaf.dal.enums._

import scala.slick.lifted.Tag
import play.api.db.slick.Config.driver.simple._

  case class OrderDBModel(id: Option[Long], status: OrderStatus.Value, customerName: String,
                          delivery: DeliveryEnum.Value, paymentMethod: PaymentMethodEnum.Value,
                          customerPhoneNumber: String, sessionId: String, addressId: Option[Long], companyId: Long)


  class OrderTable(tag: Tag) extends Table[OrderDBModel](tag, "ORDER") {

    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def status = column[OrderStatus.Value]("status", O.NotNull)
    def customerName = column[String]("customer_name", O.Nullable)
    def delivery = column[DeliveryEnum.Value]("delivery", O.Nullable)
    def paymentMethod = column[PaymentMethodEnum.Value]("payment_method", O.Nullable)
    def customerPhoneNumber = column[String]("customer_phone_number", O.Nullable)
    def sessionId = column[String]("session_id", O.NotNull)
    def addressId = column[Long]("address_id",O.Nullable)
    def companyId = column[Long]("company_id", O.NotNull)

    def * = (id.?, status, customerName, delivery, paymentMethod, customerPhoneNumber, sessionId, addressId.?, companyId) <> (OrderDBModel.tupled, OrderDBModel.unapply _)
  }


