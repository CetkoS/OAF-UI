package controllers

import java.io.File

import com.oaf.dal.enums.Role.Administrator
import forms.admin.{EditArticleForm, CreateArticleForm, CreateEmployeeForm, EditEmployeeForm}
import play.api.Logger
import play.api.Play.current
import play.api.data.Form
import play.api.db.slick.{DBAction, _}
import services.{CompanyService, ArticleService, UserService}

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

  def update() = StackAction(parse.multipartFormData, AuthorityKey -> Administrator) { implicit request =>
    EditArticleForm.getEditArticleData.bindFromRequest.fold(
      formWithErrors => {
        val articleId = request.body.asFormUrlEncoded.get("articleId").get(0)
        val article = ArticleService.findById(articleId.toLong).get
        BadRequest(views.html.admin.articleEdit(loggedIn, article, Some(formWithErrors.asInstanceOf[Form[AnyRef]])))
      },
      editArticleData => {
        var pictureUrl = ""
        request.body.file("picture").map { picture =>
          val company = CompanyService.findById(editArticleData.companyId).getOrElse(throw new IllegalArgumentException("Create article no company"))
          val folder = company.name.replaceAll("\\s", "") + "/"
          pictureUrl = s"images/$folder" + editArticleData.name.replaceAll("\\s", "")
          val pictureFile = new File("public/" + pictureUrl)
          pictureFile.delete()
          picture.ref.moveTo(pictureFile)
          Logger.info("Picture url " + pictureUrl)
        }
        ArticleService.updateArticle(editArticleData, pictureUrl)
        Redirect(routes.ArticleController.showEditPage(editArticleData.articleId.toString)).flashing("success" -> "Article successfully updated.")
      }
    )
  }

  def create() = StackAction(parse.multipartFormData, AuthorityKey -> Administrator) { implicit request =>
    CreateArticleForm.getCreateArticleData.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(views.html.admin.articleCreate(loggedIn, Some(formWithErrors.asInstanceOf[Form[AnyRef]])))
      },
      createArticleData => {
        var pictureUrl = ""
        request.body.file("picture").map { picture =>
          val company = CompanyService.findById(createArticleData.companyId).getOrElse(throw new IllegalArgumentException("Create article no company"))
          val folder = company.name.replaceAll("\\s", "") + "/"
          pictureUrl = s"images/$folder" + createArticleData.name.replaceAll("\\s", "")
          val pictureFile = new File("public/" + pictureUrl)
          picture.ref.moveTo(pictureFile)
        }
        ArticleService.createArticle(createArticleData, pictureUrl)
        Redirect(routes.ArticleController.getAll).flashing("success" -> "Article successfully created.")
      }
    )
  }

  def delete(articleId: String) = StackAction(AuthorityKey -> Administrator) { implicit request =>
    ArticleService.delete(articleId.toInt)
    Redirect(routes.ArticleController.getAll).flashing("success" -> "Article successfully deleted.")  }
}
