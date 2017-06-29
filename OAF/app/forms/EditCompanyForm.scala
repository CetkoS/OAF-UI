package forms

import play.api.data._
import play.api.data.Forms._
import Constraints._
import play.api.db.slick.Session
import play.api.data.format.Formats._

case class EditCompanyData
(
  name: String,
  description: String,
  phoneNumber: String,
  addressLine: String,
  postalCode: String,
  city: String,
  country: String,
  companyId: Long,
  addressId: Long,
  ownerId: Long,
  longitude: Option[Double],
  latitude: Option[Double]
)

object EditCompanyForm {
  def getEditCompanyData = Form(
      mapping(
        "name" -> nonEmptyText,
        "description" -> nonEmptyText,
        "phoneNumber" -> nonEmptyText,
        "addressLine" -> nonEmptyText,
        "postalCode" -> nonEmptyText,
        "city" -> nonEmptyText,
        "country" -> nonEmptyText,
        "companyId" -> longNumber,
        "addressId" -> longNumber,
        "ownerId" -> longNumber,
        "longitude" -> optional(of[Double]),
        "latitude" -> optional(of[Double])
  )(EditCompanyData.apply)(EditCompanyData.unapply)
  )
}
