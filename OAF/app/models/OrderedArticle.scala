package models

import com.oaf.dal.models.{OrderedArticleDBModel, OrderedArticleFullDBModel}

case class OrderedArticle
(
  id: Option[Long],
  orderId: Long,
  articleInfo: Article,
  additives: List[Additive]
)

object OrderedArticle {

  implicit def convertToModel(articleDbModel: OrderedArticleFullDBModel) : OrderedArticle = {
    OrderedArticle(articleDbModel.orderedArticle.id, articleDbModel.orderedArticle.orderId,
      Article.convertToModel(articleDbModel.articleInfo), articleDbModel.additives.map(Additive.convertToModel(_)))
  }

  implicit def convertToDBModel(article: OrderedArticle) : OrderedArticleFullDBModel = {
    OrderedArticleFullDBModel(OrderedArticleDBModel(article.id, article.articleInfo.id.get, article.orderId),
      article.additives.map(Additive.convertToDBModel(_)), Article.convertToDBModel(article.articleInfo))
  }
}
