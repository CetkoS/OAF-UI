package forms.admin

import play.api.data._
import play.api.data.Forms._
import forms.Constraints._
import play.api.db.slick.Session
import play.api.data.format.Formats._

case class CreateAdditiveData
(
  name: String,
  description: Option[String],
  companyId: Long,
  articleGroup: String
)

object CreateAdditiveForm {
  def getCreateAdditiveData = Form(
      mapping(
        "name" -> nonEmptyText,
        "description" -> optional(text),
        "companyId" -> longNumber,
        "articleGroup"-> nonEmptyText
      )(CreateAdditiveData.apply)(CreateAdditiveData.unapply)
        verifying ("Additive name already exists.", result => result match {
        case additiveData => checkAdditiveNameUnique(additiveData.name, additiveData.companyId)
      })
  )
}
