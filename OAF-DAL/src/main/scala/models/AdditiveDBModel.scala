package com.oaf.dal.models

import com.oaf.dal.enums.{ArticleGroup, AdditiveStatus}
import com.oaf.dal.enums.ArticleGroup.ArticleGroup

import scala.slick.lifted.Tag
import play.api.db.slick.Config.driver.simple._

  case class AdditiveDBModel(id: Option[Long], name: String, description: Option[String],
                             status: AdditiveStatus.Value, companyId: Long, articleGroup: ArticleGroup.Value)


  class AdditiveTable(tag: Tag) extends Table[AdditiveDBModel](tag, "ADDITIVE") {

    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def name = column[String]("name", O.NotNull)
    def description = column[Option[String]]("description", O.Nullable)
    def status = column[AdditiveStatus.Value]("status", O.NotNull)
    def companyId = column[Long]("company_id", O.NotNull)
    def articleGroup = column[ArticleGroup.Value]("article_group", O.NotNull)


    def * = (id.?, name, description, status, companyId, articleGroup) <> (AdditiveDBModel.tupled, AdditiveDBModel.unapply _)

  }


