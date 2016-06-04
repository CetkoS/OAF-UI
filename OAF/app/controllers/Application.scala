package controllers

import forms.AuthenticationForms
import jp.t2v.lab.play2.auth.LoginLogout
import play.api.mvc._
import scala.concurrent.{ExecutionContext, Future}

import scala.concurrent.ExecutionContext.Implicits.global


class Application extends Controller with LoginLogout with AuthConfigImpl {

  /** Alter the login page action to suit your application. */
  def login = Action { implicit request =>
    Ok(views.html.login(AuthenticationForms.loginForm))
  }

  /**
    * Return the `gotoLogoutSucceeded` method's result in the logout action.
    *
    * Since the `gotoLogoutSucceeded` returns `Future[Result]`,
    * you can add a procedure like the following.
    *
    *   gotoLogoutSucceeded.map(_.flashing(
    *     "success" -> "You've been logged out"
    *   ))
    */
  def logout = Action.async { implicit request =>
    // do something...
    gotoLogoutSucceeded
  }

  /**
    * Return the `gotoLoginSucceeded` method's result in the login action.
    *
    * Since the `gotoLoginSucceeded` returns `Future[Result]`,
    * you can add a procedure like the `gotoLogoutSucceeded`.
    */
  def authenticate = Action.async { implicit request =>
    AuthenticationForms.loginForm.bindFromRequest.fold(
      formWithErrors => Future.successful(BadRequest(views.html.login(formWithErrors))),
      user => gotoLoginSucceeded(user.username)// change thisssssssssssssss
    )
  }

  @deprecated("it will be deleted since 0.14.x. use authorizationFailed(RequestHeader, User, Option[Authority])")
  override def authorizationFailed(request: RequestHeader)(implicit context: ExecutionContext): Future[SimpleResult] = ???
}
