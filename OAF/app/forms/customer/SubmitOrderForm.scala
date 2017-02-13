package forms.customer

import play.api.data._
import play.api.data.Forms._

case class SubmitOrderData
(
  orderId: Long,
  companyId: Long,
  customerName: String,
  phoneNumber: String,
  paymentMethod: String,
  delivery: String,
  addressLine: Option[String]
)

object SubmitOrderForm {
  def getSubmitOrderData = Form(
      mapping(
        "orderId" -> longNumber,
        "companyId" -> longNumber,
        "customerName" -> nonEmptyText,
        "phoneNumber" -> nonEmptyText,
        "paymentMethod" -> nonEmptyText,
        "delivery" -> nonEmptyText,
        "addressLine" -> optional(text)
      )(SubmitOrderData.apply)(SubmitOrderData.unapply)
  )
}
