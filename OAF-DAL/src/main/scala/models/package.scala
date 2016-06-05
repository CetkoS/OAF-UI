package com.oaf.dal

import _root_.models.CompanyTable

import scala.slick.lifted.TableQuery

package object models {

  val users = TableQuery[UserTable]
  val registrations = TableQuery[RegistrationTable]
  val companies = TableQuery[CompanyTable]

}
