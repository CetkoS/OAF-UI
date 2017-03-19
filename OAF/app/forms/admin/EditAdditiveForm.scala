package forms.admin

import play.api.data._
import play.api.data.Forms._
import forms.Constraints._
import play.api.db.slick.Session
import play.api.data.format.Formats._

case class EditAdditiveData
(
  additiveId: Long,
  name: String,
  description: Option[String],
  articleGroup: String
)

object EditAdditiveForm {
  def getEditAdditiveData = Form(
      mapping(
        "additiveId" -> longNumber,
        "name" -> nonEmptyText,
        "description" -> optional(text),
        "articleGroup" -> nonEmptyText
      )(EditAdditiveData.apply)(EditAdditiveData.unapply)
  )
}
