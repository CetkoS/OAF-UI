package controllers

import com.oaf.dal.enums.Role.Administrator
import forms.admin.{EditArticleForm, CreateArticleForm, CreateEmployeeForm, EditEmployeeForm}
import play.api.Logger
import play.api.Play.current
import play.api.db.slick.{DBAction, _}
import services.{ArticleService, UserService}

class ArticleController extends BaseController {

  def getAll = StackAction(AuthorityKey -> Administrator) { implicit request =>
    val articles = loggedIn.companyId match {
      case Some(companyId) => Some(ArticleService.findAllArticlesByCompanyId(companyId))
      case _ => None
    }
    Ok(views.html.admin.articles(loggedIn, articles))
  }

  def showEditPage(articleId: String) = StackAction(AuthorityKey -> Administrator) { implicit request =>
    val article = ArticleService.findById(articleId.toInt)
        Ok(views.html.admin.articleEdit(loggedIn, article.get))
  }

  def showCreatePage() = StackAction(AuthorityKey -> Administrator) { implicit request =>
    Ok(views.html.admin.articleCreate(loggedIn))
  }

  def update() = StackAction(AuthorityKey -> Administrator) { implicit request =>
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

  def create() = StackAction(AuthorityKey -> Administrator) { implicit request =>
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

  def delete(articleId: String) = StackAction(AuthorityKey -> Administrator) { implicit request =>
    ArticleService.delete(articleId.toInt)
    Ok("Deleted")
  }

}
