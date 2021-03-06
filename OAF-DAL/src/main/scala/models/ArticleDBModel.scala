package models

import com.oaf.dal.enums.ArticleStatus

import scala.slick.lifted.Tag
import play.api.db.slick.Config.driver.simple._

  case class ArticleDBModel(id: Option[Long], name: String, description: Option[String], price: Double,
                            pictureLocation: Option[String], weight: Option[Double], companyId: Long, status: ArticleStatus.Value)


  class ArticleTable(tag: Tag) extends Table[ArticleDBModel](tag, "ARTICLE") {

    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def name = column[String]("name", O.NotNull)
    def description = column[Option[String]]("description", O.NotNull)
    def price = column[Double]("price", O.Nullable)
    def pictureLocation = column[Option[String]]("picture_location", O.Nullable)
    def weight = column[Option[Double]]("weight", O.Nullable)
    def companyId = column[Long]("company_id", O.NotNull)
    def status = column[ArticleStatus.Value]("status", O.NotNull)


    def * = (id.?, name, description, price, pictureLocation, weight, companyId, status) <> (ArticleDBModel.tupled, ArticleDBModel.unapply _)

  }


