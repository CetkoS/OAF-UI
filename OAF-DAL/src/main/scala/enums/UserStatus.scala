package com.oaf.dal.enums

object UserStatus extends DBEnum {

  type UserStatus = Value

  val Inactive = Value(0)
  val Active = Value(1)
  val Invited = Value(2)

}
