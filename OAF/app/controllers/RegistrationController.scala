package controllers

import forms.{LoginForm, RegistrationForm}
import jp.t2v.lab.play2.auth.{AuthElement, LoginLogout}
import play.api.Logger
import play.api.db.slick.DBAction
import play.api.mvc.{Action, Controller}
import services.{RegistrationService, UserService}
import play.api.Play.current
import play.api.db.slick._
import scala.concurrent.ExecutionContext.Implicits.global

import scala.concurrent.Future

class RegistrationController extends AuthConfigImpl with Controller with LoginLogout with AuthElement {

  def showRegistration = DBAction { implicit request =>
    Ok(views.html.registration.registration(RegistrationForm.getRegisterData))
  }

  def registration = DBAction { implicit request =>
    RegistrationForm.getRegisterData.bindFromRequest.fold(
      formWithErrors => {
        Logger.info("UsaoErr");
        Ok(views.html.registration.registration(formWithErrors))
      },
      registerData => {
        RegistrationService.create(registerData)
        Ok(views.html.registration.registrationConfirmation())
      }
    )
  }

  def confirmRegistration(hash: String) = DBAction { implicit request =>
    RegistrationService.confirmRegistration(hash)
    Ok("Login")
  }

  def logout = Action.async { implicit request =>
    gotoLogoutSucceeded
  }

  def showLogin = DBAction { implicit request =>
    Ok(views.html.registration.login(LoginForm.getLoginData))
  }

  def authenticate = Action.async { implicit request =>
    play.api.db.slick.DB.withTransaction { implicit session =>
      LoginForm.getLoginData.bindFromRequest.fold(
        errors => Future.successful(BadRequest(views.html.login(errors))),
        loginData => {
          UserService.findByUsername(loginData.username) map { user =>
            gotoLoginSucceeded(user.username)
          } getOrElse (Future.successful(BadRequest("Wrong login")))
        }
      )
    }
  }
}
