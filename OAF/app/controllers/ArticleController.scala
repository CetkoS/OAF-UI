package controllers

import com.oaf.dal.enums.Role.Administrator
import forms.admin.{EditArticleForm, CreateArticleForm, CreateEmployeeForm, EditEmployeeForm}
import play.api.Logger
import play.api.Play.current
import play.api.data.Form
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
        val articleId = request.body.asFormUrlEncoded.get("articleId")(0)
        val article = ArticleService.findById(articleId.toInt).get
        BadRequest(views.html.admin.articleEdit(loggedIn, article, Some(formWithErrors.asInstanceOf[Form[AnyRef]])))
      },
      editArticleData => {
        ArticleService.updateArticle(editArticleData)
        Redirect(routes.ArticleController.showEditPage(editArticleData.articleId.toString)).flashing("success" -> "Article successfully updated.")
      }
    )
  }

  def create() = StackAction(AuthorityKey -> Administrator) { implicit request =>
    CreateArticleForm.getCreateArticleData.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(views.html.admin.articleCreate(loggedIn, Some(formWithErrors.asInstanceOf[Form[AnyRef]])))
      },
      createArticleData => {
        ArticleService.createArticle(createArticleData)
        Redirect(routes.ArticleController.getAll).flashing("success" -> "Article successfully created.")
      }
    )
  }

  def delete(articleId: String) = StackAction(AuthorityKey -> Administrator) { implicit request =>
    ArticleService.delete(articleId.toInt)
    Redirect(routes.ArticleController.getAll).flashing("success" -> "Article successfully deleted.")  }
}
