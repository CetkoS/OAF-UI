package com.oaf.dal.models

import scala.slick.lifted.Tag
import play.api.db.slick.Config.driver.simple._

  case class OrderedArticleDBModel(id: Option[Long], articleId: Long, orderId: Long)


  class OrderedArticleTable(tag: Tag) extends Table[OrderedArticleDBModel](tag, "ORDERED_ARTICLE") {

    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def articleId = column[Long]("article_id", O.NotNull)
    def orderId = column[Long]("order_id", O.NotNull)


    def * = (id.?, articleId, orderId) <> (OrderedArticleDBModel.tupled, OrderedArticleDBModel.unapply _)

  }


