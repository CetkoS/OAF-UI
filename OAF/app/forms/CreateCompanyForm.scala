package forms

import play.api.data._
import play.api.data.Forms._
import Constraints._
import play.api.db.slick.Session
import play.api.data.format.Formats._

case class CreateCompanyData
(
  name: String,
  description: String,
  phoneNumber: String,
  addressLine: String,
  postalCode: String,
  city: String,
  country: String,
  ownerId: Long,
  longitude: Option[Double],
  latitude: Option[Double]
)

object CreateCompanyForm {
  def getCreateCompanyData = Form(
      mapping(
        "name" -> nonEmptyText.verifying(checkCompanyNameUnique),
        "description" -> nonEmptyText,
        "phoneNumber" -> nonEmptyText,
        "addressLine" -> nonEmptyText,
        "postalCode" -> nonEmptyText,
        "city" -> nonEmptyText,
        "country" -> nonEmptyText,
        "ownerId" -> longNumber,
        "longitude" -> optional(of[Double]),
        "latitude" -> optional(of[Double])
      )(CreateCompanyData.apply)(CreateCompanyData.unapply)
  )
}
