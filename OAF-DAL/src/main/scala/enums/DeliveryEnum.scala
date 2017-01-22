package com.oaf.dal.enums

object DeliveryEnum extends DBEnum {

  type DeliveryEnum = Value

  val Taxi = Value(0)
  val Company = Value(1)
  val No = Value(2)

}
