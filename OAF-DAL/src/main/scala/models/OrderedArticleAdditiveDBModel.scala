package com.oaf.dal.models

import scala.slick.lifted.Tag
import play.api.db.slick.Config.driver.simple._

  case class OrderedArticleAdditiveDBModel(id: Option[Long], orderedArticleId: Long, additiveId: Long)


  class OrderedArticleAdditiveTable(tag: Tag) extends Table[OrderedArticleAdditiveDBModel](tag, "ORDERED_ARTICLE_ADDITIVE") {

    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def orderedArticleId = column[Long]("ordered_article_id", O.NotNull)
    def additiveId = column[Long]("additive_id", O.NotNull)


    def * = (id.?, orderedArticleId, additiveId) <> (OrderedArticleAdditiveDBModel.tupled, OrderedArticleAdditiveDBModel.unapply _)

  }


