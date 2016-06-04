package controllers

import forms.{LoginForm, RegistrationForm}
import play.api.Logger
import play.api.db.slick.DBAction
import services.{RegistrationService, UserService}
import play.api.Play.current
import play.api.db.slick._

class RegistrationController extends BaseController {

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

  def showLogin = DBAction { implicit request =>
    Ok(views.html.registration.login(LoginForm.getLoginData))
  }

  def authenticate = DBAction { implicit request =>
    LoginForm.getLoginData.bindFromRequest.fold(
      formWithErrors => {
        Logger.info("UsaoErr");
        Ok("Error" + formWithErrors)
      },
      loginData => {
        Ok("Successfuly login")
      }
    )
  }
}
