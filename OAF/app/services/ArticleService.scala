package services

import com.oaf.dal.dal.ArticleDAO
import com.oaf.dal.enums.ArticleStatus
import forms.admin.{EditArticleData, CreateArticleData}
import models.User._
import models.{Article, User}
import org.mindrot.jbcrypt.BCrypt
import play.api.Logger
import play.api.db.slick._
import scala.concurrent.ExecutionContext.Implicits.global

object ArticleService {

  def findAll(implicit session: Session): List[Article] = {
    ArticleDAO.findAll.map(u => Article.convertToModel(u))
  }

  def findById(id: Long)(implicit session: Session): Option[Article] = {
    ArticleDAO.findById(id).map(u => u)
  }

  def update(id: Long, article: Article)(implicit session: Session): Unit = {
    ArticleDAO.update(id, article)
  }

  def delete(id: Long)(implicit session: Session): Unit = {
    ArticleDAO.delete(id)
  }

  def create(article: Article)(implicit session: Session): Long = {
    ArticleDAO.create(article)
  }


  def findAllArticlesByCompanyId(companyId: Long)(implicit session: Session): List[Article] = {
    ArticleDAO.findAllByCompanyId(companyId).map(u => Article.convertToModel(u))
  }

  def updateArticle(editArticleData: EditArticleData)(implicit session: Session): Unit = {
    val oldArticle = findById(editArticleData.articleId).getOrElse(throw new Exception("Article dosen't exist"))
    val newArticle = Article(Some(editArticleData.articleId), editArticleData.name, editArticleData.description,
      editArticleData.price, editArticleData.pictureLocation, editArticleData.weight, oldArticle.companyId, oldArticle.status)
    update(oldArticle.id.get, newArticle)
  }

  def createArticle(createArticleData: CreateArticleData)(implicit session: Session): Long = {
    val article = Article(None, createArticleData.name, createArticleData.description, createArticleData.price,
       createArticleData.pictureLocation, createArticleData.weight, createArticleData.companyId, ArticleStatus.Active)
    val articleId = create(article)

    articleId
  }
}
