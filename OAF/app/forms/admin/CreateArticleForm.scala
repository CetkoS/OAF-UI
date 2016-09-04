package forms.admin

import play.api.data._
import play.api.data.Forms._
import forms.Constraints._
import play.api.db.slick.Session
import play.api.data.format.Formats._

case class CreateArticleData
(
  name: String,
  description: Option[String],
  price: Double,
  weight: Option[Double],
  pictureLocation: Option[String],
  companyId: Long
)

object CreateArticleForm {
  def getCreateArticleData = Form(
      mapping(
        "name" -> nonEmptyText,
        "description" -> optional(text),
        "price" -> of[Double],
        "weight" -> optional(of[Double]),
        "pictureLocation" -> optional(text),
        "companyId" -> longNumber
      )(CreateArticleData.apply)(CreateArticleData.unapply)
  )
}
