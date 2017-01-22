package com.oaf.dal.enums

object OrderStatus extends DBEnum {

  type OrderStatus = Value

  val Pending = Value(0)
  val Active = Value(1)
  val Completed = Value(2)
  val Delivered = Value(3)
  val Inactive = Value(4)

}
