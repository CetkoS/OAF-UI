package models

import com.oaf.dal.enums.{ArticleGroup, ArticleStatus}
import com.oaf.dal.models.ArticleDBModel

case class Article
(
  id: Option[Long],
  name: String,
  description: Option[String],
  price: Double,
  pictureLocation: String,
  weight: Option[Double],
  companyId: Long,
  status: ArticleStatus.Value,
  group: ArticleGroup.Value
)

object Article {

  implicit def convertToModel(articleDbModel: ArticleDBModel) : Article = {
    Article(articleDbModel.id, articleDbModel.name, articleDbModel.description, articleDbModel.price,
      articleDbModel.pictureLocation, articleDbModel.weight, articleDbModel.companyId, articleDbModel.status, articleDbModel.group)
  }

  implicit def convertToDBModel(article: Article) : ArticleDBModel = {
    ArticleDBModel(article.id, article.name, article.description, article.price,
      article.pictureLocation, article.weight, article.companyId, article.status, article.group)
  }
}
