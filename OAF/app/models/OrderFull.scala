package models

case class OrderFull
(
  orderInfo: Order,
  orderedArticles: List[OrderedArticle],
  totalPrice: Double
)
