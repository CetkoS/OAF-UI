package controllers

import forms.admin.{EditArticleForm, CreateArticleForm, CreateEmployeeForm, EditEmployeeForm}
import play.api.Logger
import play.api.Play.current
import play.api.db.slick.{DBAction, _}
import services.{ArticleService, UserService}

class ArticleController extends BaseController {

  def getAll = DBAction { implicit request =>
    val user = UserService.findById(1) //@todo logged in user
    val articles = user.get.companyId match {
      case Some(companyId) => Some(ArticleService.findAllArticlesByCompanyId(companyId))
      case _ => None
    }
    Ok(views.html.admin.articles(user.get, articles))
  }

  def showEditPage(articleId: String) = DBAction { implicit request =>
    val loggedInUser = UserService.findById(1)
    val article = ArticleService.findById(articleId.toInt)
        Ok(views.html.admin.articleEdit(loggedInUser.get, article.get))
  }

  def showCreatePage() = DBAction { implicit request =>
    val loggedInUser = UserService.findById(1)
    Ok(views.html.admin.articleCreate(loggedInUser.get))
  }

  def update() = DBAction { implicit request =>
    EditArticleForm.getEditArticleData.bindFromRequest.fold(
      formWithErrors => {
        Logger.info("UsaoErr");
        Ok("Err" + formWithErrors)
      },
      editArticleData => {
        ArticleService.updateArticle(editArticleData)
        Ok("" + editArticleData)
      }
    )
  }

  def create() = DBAction { implicit request =>
    CreateArticleForm.getCreateArticleData.bindFromRequest.fold(
      formWithErrors => {
        Logger.info("UsaoErr");
        Ok("Err" + formWithErrors)
      },
      createArticleData => {
        ArticleService.createArticle(createArticleData)
        Ok("" + createArticleData)
      }
    )
  }

  def delete(articleId: String) = DBAction { implicit request =>
    ArticleService.delete(articleId.toInt)
    Ok("Deleted")
  }

}
