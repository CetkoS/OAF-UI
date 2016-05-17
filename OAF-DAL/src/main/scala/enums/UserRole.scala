package com.oaf.dal.enums

object UserRole extends DBEnum {

  type UserRole = Value

  val Administrator = Value(0)
  val Employee = Value(1)
  val Customer = Value(2)

}
