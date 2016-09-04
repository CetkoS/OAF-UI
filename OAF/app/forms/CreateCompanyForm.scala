package forms

import play.api.data._
import play.api.data.Forms._
import Constraints._
import play.api.db.slick.Session

case class CreateCompanyData
(
  name: String,
  description: String,
  phoneNumber: String,
  logoUrl: String,
  addressLine: String,
  postalCode: String,
  city: String,
  country: String,
  ownerId: Long
)

object CreateCompanyForm {
  def getCreateCompanyData = Form(
      mapping(
        "name" -> nonEmptyText,
        "description" -> nonEmptyText,
        "phoneNumber" -> nonEmptyText,
        "logoUrl" -> nonEmptyText,
        "addressLine" -> nonEmptyText,
        "postalCode" -> nonEmptyText,
        "city" -> nonEmptyText,
        "country" -> nonEmptyText,
        "ownerId" -> longNumber
      )(CreateCompanyData.apply)(CreateCompanyData.unapply)
  )
}
