package newco.domain

object Types {
  type Cart = List[Item]
}

trait Item{
  def name: String
  def price: Double
}

case class SingleItem(override val name: String, override val price: Double) extends Item

case class Bundle(override val name: String, override val price: Double, items: List[SingleItem]) extends Item
