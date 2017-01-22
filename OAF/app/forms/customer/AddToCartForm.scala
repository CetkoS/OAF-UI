package forms.customer

import play.api.data._
import play.api.data.Forms._

case class AddToCartData
(
  articleId: Long,
  companyId: Long,
  additiveIds: List[Long]
)

object AddToCartForm {
  def getAddToCartData = Form(
      mapping(
        "articleId" -> longNumber,
        "companyId" -> longNumber,
        "additive" -> list(longNumber)
      )(AddToCartData.apply)(AddToCartData.unapply)
  )
}
