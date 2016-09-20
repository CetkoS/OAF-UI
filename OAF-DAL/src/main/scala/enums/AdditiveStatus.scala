package com.oaf.dal.enums

object AdditiveStatus extends DBEnum {

  type AdditiveStatus = Value

  val Inactive = Value(0)
  val Active = Value(1)
}
