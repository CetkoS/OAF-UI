package com.oaf.dal.enums

abstract class DBEnum extends Enumeration {

import play.api.db.slick.Config.driver.MappedJdbcType
import play.api.db.slick.Config.driver.simple._

  implicit val enumMapper = MappedJdbcType.base[Value, Int](_.id, this.apply)
}