package com.oaf.dal

import com.oaf.dal.models._

import scala.slick.driver.MySQLDriver.simple._

object Hello {
  def get(id: Long): Option[UserDBModel] = {
    Database.forURL("jdbc:mysql://localhost:3306/oaf", driver="com.mysql.jdbc.Driver", user="root", password="admin").withSession { implicit session =>
    users.filter(user => user.id === id).firstOption
    }
  }
}
