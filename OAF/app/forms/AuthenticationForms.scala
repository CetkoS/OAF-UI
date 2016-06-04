package forms

import play.api.data._
import play.api.data.Forms._

case class LoginData1(email: String, password: String)

object AuthenticationForms {
  val loginForm = Form[LoginData](
      mapping(
        "email" -> nonEmptyText,
        "password" -> nonEmptyText
      )(LoginData.apply)(LoginData.unapply))
}
