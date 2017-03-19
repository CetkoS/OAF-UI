package com.oaf.dal.enums

object ArticleGroup extends DBEnum {

  type ArticleGroup = Value

  val Dessert = Value(0)
  val Meal = Value(1)
  val Drink = Value(2)

}
