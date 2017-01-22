package controllers

import forms.customer.AddToCartForm
import play.api.mvc.Action
import services.{CustomerService, AdditiveService, CompanyService, ArticleService}

class CustomerController extends BaseController {

  def getAll = Action { implicit request =>
    val companies = CompanyService.findAll
    Ok(views.html.customer.customer(companies)).withNewSession
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
}
