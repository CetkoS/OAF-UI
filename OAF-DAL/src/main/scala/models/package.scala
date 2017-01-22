package com.oaf.dal

import scala.slick.lifted.TableQuery

package object models {

  val users = TableQuery[UserTable]
  val registrations = TableQuery[RegistrationTable]
  val companies = TableQuery[CompanyTable]
  val addresses = TableQuery[AddressTable]
  val articles = TableQuery[ArticleTable]
  val additives = TableQuery[AdditiveTable]
  val orders = TableQuery[OrderTable]
  val orderedArticles = TableQuery[OrderedArticleTable]
  val orderedArticleAdditive = TableQuery[OrderedArticleAdditiveTable]

}
