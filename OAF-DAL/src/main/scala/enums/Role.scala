package com.oaf.dal.enums

sealed trait Role

object Role {

  case object Administrator extends Role
  case object Employee extends Role
  case object Customer extends Role

  def valueOf(userRole: UserRole.Value): Role = userRole match {
    case UserRole.Administrator => Administrator
    case UserRole.Employee    => Employee
    case UserRole.Customer    => Customer
    case _ => throw new IllegalArgumentException()
  }

}