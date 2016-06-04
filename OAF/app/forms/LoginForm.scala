package forms

import play.api.data._
import play.api.data.Forms._
import Constraints._
import play.api.db.slick.Session

case class LoginData(username: String, password: String)

object LoginForm {
  def getLoginData(implicit session: Session) = Form(
      mapping(
        "username" -> nonEmptyText,
        "password" -> nonEmptyText
      )(LoginData.apply)(LoginData.unapply)
        verifying ("Username or password are incorrect", result => result match {
        case loginData => checkCredentials(loginData.username, loginData.password)
      })
  )
}
