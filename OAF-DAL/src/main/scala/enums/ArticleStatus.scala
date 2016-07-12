package com.oaf.dal.enums

object ArticleStatus extends DBEnum {

  type ArticleStatus = Value

  val Inactive = Value(0)
  val Active = Value(1)
  val Invited = Value(2)

}
