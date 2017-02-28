package com.oaf.dal.enums

object OrderStatus extends DBEnum {

  type OrderStatus = Value

  val Pending = Value(0)
  val Active = Value(1)
  val Ready = Value(2)
  val Completed = Value(3)
  val Inactive = Value(4)
  val New = Value(5)

}
