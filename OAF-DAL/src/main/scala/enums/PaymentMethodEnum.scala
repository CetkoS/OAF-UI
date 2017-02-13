package com.oaf.dal.enums

object PaymentMethodEnum extends DBEnum {

  type PaymentMethodEnum = Value

  val Cash = Value(0)
  val Card = Value(1)

}
