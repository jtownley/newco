package domain

trait Item {
    def name: String
    def price: Double
}

case class Cart(itemSet: Set[Item]) 

case class SingleItem(override val name: String, override val price: Double) extends Item

case class Bundle(override val name: String, override val price: Double, items:Set[SingleItem]) extends Item
