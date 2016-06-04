package com.oaf.dal.enums

object RegistrationStatus extends DBEnum {

  type RegistrationStatus = Value

  val Inactive = Value(0)
  val Active = Value(1)

}
