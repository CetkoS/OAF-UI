package forms.admin

import play.api.data._
import play.api.data.Forms._
import forms.Constraints._
import play.api.db.slick.Session

case class CreateEmployeeData
(
  firstName: String,
  lastName: String,
  username: String,
  email: String,
  companyId: Long
)

object CreateEmployeeForm {
  def getCreateEmployeeData(implicit session: Session) = Form(
      mapping(
        "firstName" -> nonEmptyText,
        "lastName" -> nonEmptyText,
        "username" -> nonEmptyText.verifying(checkUsernameUnique),
        "email" -> email,
        "companyId" -> longNumber
      )(CreateEmployeeData.apply)(CreateEmployeeData.unapply)
  )
}
