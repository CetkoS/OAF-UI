package forms.admin

import play.api.data._
import play.api.data.Forms._
import forms.Constraints._
import play.api.db.slick.Session

case class EditEmployeeData
(
  firstName: String,
  lastName: String,
  username: String,
  email: String,
  companyId: Long,
  employeeId: Long
)

object EditEmployeeForm {
  def getEditEmployeeData = Form(
      mapping(
        "firstName" -> nonEmptyText,
        "lastName" -> nonEmptyText,
        "username" -> nonEmptyText,
        "email" -> email,
        "companyId" -> longNumber,
        "employeeId" -> longNumber
      )(EditEmployeeData.apply)(EditEmployeeData.unapply)
  )
}
