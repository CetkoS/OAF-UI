package com.oaf.dal

import _root_.models.{AdditiveTable, ArticleTable, AddressTable, CompanyTable}

import scala.slick.lifted.TableQuery

package object models {

  val users = TableQuery[UserTable]
  val registrations = TableQuery[RegistrationTable]
  val companies = TableQuery[CompanyTable]
  val addresses = TableQuery[AddressTable]
  val articles = TableQuery[ArticleTable]
  val additives = TableQuery[AdditiveTable]

}
