package forms

import play.api.data._
import play.api.data.Forms._
import Constraints._
import play.api.db.slick.Session

case class RegistrationData
(
  firstName: String,
  lastName: String,
  username: String,
  email: String,
  password: String,
  repeatPassword: String
)

object RegistrationForm {
  def getRegisterData(implicit session: Session) = Form(
      mapping(
        "first_name" -> nonEmptyText,
        "last_name" -> nonEmptyText,
        "username" -> nonEmptyText.verifying(checkUsernameUnique),
        "email" -> email,
        "password" -> nonEmptyText.verifying(password),
        "repeat_password" -> nonEmptyText.verifying(password)
      )(RegistrationData.apply)(RegistrationData.unapply)
        verifying ("You must repeat correctly password!", result => result match {
        case registerData => checkPasswords(registerData.password, registerData.repeatPassword)
      })
  )
}
