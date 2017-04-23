package forms.employee

import play.api.data._
import play.api.data.Forms._

case class OrderIdData
(
  orderId: Long
)

object OrderIdForm {
  def getOrderIdData = Form(
      mapping(
        "orderId" -> longNumber
      )(OrderIdData.apply)(OrderIdData.unapply)
  )
}
