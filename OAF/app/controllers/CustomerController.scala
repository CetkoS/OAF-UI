package controllers

import forms.customer.{SubmitOrderForm, AddToCartForm}
import play.api.Logger
import play.api.mvc.Action
import services.{CustomerService, AdditiveService, CompanyService, ArticleService}
import play.api.i18n.Lang
import play.api.Play.current

class CustomerController extends BaseController {

  def getAll = Action { implicit request =>
    val companies = CompanyService.findAll
    Ok(views.html.customer.customer(companies)).withNewSession
  }

  def backToCompanies(companyId: String) = Action { implicit request =>
    companyId match {
      case "0" => Redirect(routes.CustomerController.getAll())
      case _ => Redirect(routes.CustomerController.getCompany(companyId)).flashing("leaving-page-modal"->"true")
    }
  }

  def deletePendingOrder(companyId: String) = Action {implicit request =>
    CustomerService.deleteFullOrder(companyId.toLong,request.session.get("session-id").getOrElse(""))
    Redirect(routes.CustomerController.getAll()).withNewSession
  }

  def getCompany(companyId: String) = Action { implicit request =>
    val company = CompanyService.findById(companyId.toLong).get
    val articles = ArticleService.findAllArticlesByCompanyId(companyId.toLong)
    val orderFull = request.session.get("session-id") match {
      case None => None
      case Some(sessionId) => CustomerService.getFullOrderBySessionId(company.id.get, sessionId)
    }
    Ok(views.html.customer.company(company, articles, orderFull))
  }

  def getArticle(articleId: String) = Action { implicit request =>
    val article = ArticleService.findById(articleId.toLong).get
    val additives = AdditiveService.findAllAdditivesByCompanyId(article.companyId)
    val orderFull = request.session.get("session-id") match {
      case None => None
      case Some(sessionId) => CustomerService.getFullOrderBySessionId(article.companyId, sessionId)
    }
    Ok(views.html.customer.article(article, additives, orderFull))
  }

  def addToCart() = Action { implicit request =>
    AddToCartForm.getAddToCartData.bindFromRequest.fold(
      formWithErrors => {
        BadRequest("ERROR" + formWithErrors)
      },
      addToCartData => {
        val sessionId = request.session.get("session-id")
        sessionId match {
          case None => {
            val sessionId = java.util.UUID.randomUUID.toString
            val orderId = CustomerService.createPendingOrder(sessionId, addToCartData.companyId)
            CustomerService.createOrderedArticle(orderId, addToCartData)
            Redirect(routes.CustomerController.getCompany(addToCartData.companyId.toString)).withSession(request.session + ("session-id" -> sessionId)).flashing("success" -> "Article successfully added.")
          }
          case Some(sessionId) => {
            val order = CustomerService.getOrderBySessionId(addToCartData.companyId, sessionId)
            CustomerService.createOrderedArticle(order.get.id.get, addToCartData)
            Redirect(routes.CustomerController.getCompany(addToCartData.companyId.toString)).flashing("success" -> "Article successfully added.")
          }
        }
      }
    )
  }

  def getOrder(companyId: String) = Action {implicit request =>
    val sessionId = request.session.get("session-id").get
    val orderFull = CustomerService.getFullOrderBySessionId(companyId.toLong, sessionId).get
    Ok(views.html.customer.order(orderFull))
  }

  def deleteOrderedArticle(companyId: String) = Action {implicit request =>
    val articleId = request.body.asFormUrlEncoded.get("articleId")(0).toLong
    CustomerService.deleteOrderedArticle(articleId)
    Redirect(routes.CustomerController.getCompany(companyId))
  }

  def submitOrder() = Action { implicit  request =>
    SubmitOrderForm.getSubmitOrderData.bindFromRequest.fold(
      formWithErrors => {
        BadRequest("ERROR" + formWithErrors)
      },
      submitOrderData => {
        CustomerService.submitOrder(submitOrderData)
        Redirect(routes.CustomerController.getCompany(submitOrderData.companyId.toString())).flashing("success" -> "Your order is successfully sent!")
        }
    )
  }

  def changeLanguage(newLang: String) = Action {implicit request =>
    val lang = Lang.apply(newLang)
    Redirect(routes.CustomerController.getAll()).withLang(lang)
  }
}
