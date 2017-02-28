package forms.employee

import play.api.data._
import play.api.data.Forms._

case class AcceptOrderData
(
  orderId: Long,
  timeToBeReady: Long
)

object AcceptOrderForm {
  def getAcceptOrderData = Form(
      mapping(
        "orderId" -> longNumber,
        "timeToBeReady" -> longNumber
      )(AcceptOrderData.apply)(AcceptOrderData.unapply)
  )
}
