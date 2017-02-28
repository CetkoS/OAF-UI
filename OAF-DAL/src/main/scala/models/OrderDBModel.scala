package com.oaf.dal.models

import java.sql.Timestamp

import com.oaf.dal.enums._
import org.joda.time.DateTime

import scala.slick.lifted.Tag
import play.api.db.slick.Config.driver.simple._


  case class OrderDBModel(id: Option[Long], status: OrderStatus.Value, customerName: String,
                          delivery: DeliveryEnum.Value, paymentMethod: PaymentMethodEnum.Value,
                          customerPhoneNumber: String, sessionId: String, addressLine: Option[String],
                          companyId: Long, timeToBeReady: Option[Long], timeCreated: Option[Timestamp])


  class OrderTable(tag: Tag) extends Table[OrderDBModel](tag, "ORDER") {

    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def status = column[OrderStatus.Value]("status", O.NotNull)
    def customerName = column[String]("customer_name", O.Nullable)
    def delivery = column[DeliveryEnum.Value]("delivery", O.Nullable)
    def paymentMethod = column[PaymentMethodEnum.Value]("payment_method", O.Nullable)
    def customerPhoneNumber = column[String]("customer_phone_number", O.Nullable)
    def sessionId = column[String]("session_id", O.NotNull)
    def addressLine = column[String]("address_line",O.Nullable)
    def companyId = column[Long]("company_id", O.NotNull)
    def timeToBeReady = column[Long]("time_to_be_ready", O.Nullable)
    def timeCreated = column[Timestamp]("time_created", O.Nullable)

    def * = (id.?, status, customerName, delivery, paymentMethod, customerPhoneNumber, sessionId, addressLine.?, companyId, timeToBeReady.?, timeCreated.?) <> (OrderDBModel.tupled, OrderDBModel.unapply _)
  }


