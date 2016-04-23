package domain

object Types {
  type Cart = Set[_ <: Item]
}

trait Item

case class SingleItem(val name: String, val price: Double) extends Item

case class Bundle(val name: String, val price: Double, items:Set[SingleItem]) extends Item
