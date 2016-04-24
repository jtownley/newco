package newco.domain

object Types {
  type Cart = List[_ <: Item]
}

trait Item

case class SingleItem(name: String, price: Double) extends Item

case class Bundle(name: String, price: Double, items: List[SingleItem]) extends Item {
  lazy val savings = {
    if (items.isEmpty)
      0.00d
    else
      items.map(_.price).sum - price
  }
}
