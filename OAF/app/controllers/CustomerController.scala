package controllers

import com.oaf.dal.enums.Role.Administrator
import forms.admin.{CreateArticleForm, EditArticleForm}
import play.api.data.Form
import play.api.mvc.Action
import services.{CompanyService, ArticleService}

class CustomerController extends BaseController {

  def getAll = Action { implicit request =>
    val companies = CompanyService.findAll
    Ok(views.html.customer.customer(companies))
  }
}
