package com.oaf.dal

import scala.slick.lifted.TableQuery

package object models {

  val users = TableQuery[UserTable]
  val registrations = TableQuery[RegistrationTable]

}
