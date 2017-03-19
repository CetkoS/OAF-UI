package services

import com.oaf.dal.dal.ArticleDAO
import com.oaf.dal.enums.{ArticleGroup, ArticleStatus}
import forms.admin.{EditArticleData, CreateArticleData}
import models.{Article, User}
import play.api.db.slick._
import scala.concurrent.ExecutionContext.Implicits.global
import play.api.Play.current

object ArticleService {

  def findAll: List[Article] = {
    play.api.db.slick.DB.withTransaction { implicit session =>
      ArticleDAO.findAll.map(u => Article.convertToModel(u))
    }
  }

  def findById(id: Long): Option[Article] = {
    play.api.db.slick.DB.withTransaction { implicit session =>
      ArticleDAO.findById(id).map(u => u)
    }
  }

  def findByNameAndCompany(name: String, companyId: Long): Option[Article] = {
    play.api.db.slick.DB.withTransaction { implicit session =>
      ArticleDAO.findByNameAndCompany(name, companyId).map(u => u)
    }
  }

  def update(id: Long, article: Article): Unit = {
    play.api.db.slick.DB.withTransaction { implicit session =>
      ArticleDAO.update(id, article)
    }
  }

  def delete(id: Long): Unit = {
    play.api.db.slick.DB.withTransaction { implicit session =>
      ArticleDAO.delete(id)
    }
  }

  def create(article: Article): Long = {
    play.api.db.slick.DB.withTransaction { implicit session =>
      ArticleDAO.create(article)
    }
  }


  def findAllArticlesByCompanyId(companyId: Long): List[Article] = {
    play.api.db.slick.DB.withTransaction { implicit session =>
      ArticleDAO.findAllByCompanyId(companyId).map(u => Article.convertToModel(u))
    }
  }

  def updateArticle(editArticleData: EditArticleData, pictureUrl: String): Unit = {
    val oldArticle = findById(editArticleData.articleId).getOrElse(throw new Exception("Article doesn't exist"))
    val newArticle = Article(Some(editArticleData.articleId), editArticleData.name, editArticleData.description, editArticleData.price, pictureUrl,
      editArticleData.weight, oldArticle.companyId, oldArticle.status, ArticleGroup.withName(editArticleData.group))
    update(oldArticle.id.get, newArticle)
  }

  def createArticle(createArticleData: CreateArticleData, pictureUrl: String): Long = {
    val article = Article(None, createArticleData.name, createArticleData.description, createArticleData.price, pictureUrl,
      createArticleData.weight, createArticleData.companyId, ArticleStatus.Active, ArticleGroup.withName(createArticleData.group))
    val articleId = create(article)

    articleId
  }
}
