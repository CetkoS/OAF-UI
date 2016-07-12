package forms

import play.api.data._
import play.api.data.Forms._
import Constraints._
import play.api.db.slick.Session

case class EditCompanyData
(
  name: String,
  description: String,
  phoneNumber: String,
  logoUrl: String,
  addressLine: String,
  postalCode: String,
  city: String,
  country: String,
  companyId: Long,
  addressId: Long,
  ownerId: Long
)

object EditCompanyForm {
  def getEditCompanyData(implicit session: Session) = Form(
      mapping(
        "name" -> nonEmptyText,
        "description" -> nonEmptyText,
        "phoneNumber" -> nonEmptyText,
        "logoUrl" -> nonEmptyText,
        "addressLine" -> nonEmptyText,
        "postalCode" -> nonEmptyText,
        "city" -> nonEmptyText,
        "country" -> nonEmptyText,
        "companyId" -> longNumber,
        "addressId" -> longNumber,
        "ownerId" -> longNumber
  )(EditCompanyData.apply)(EditCompanyData.unapply)
  )
}
