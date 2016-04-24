package newco.domain

object Types {
  type Cart = List[Item]
  implicit def advancedCart[Item](l: Cart): CartList[Item] = new CartList(l)

  class CartList[T](targetList: List[Item]) {
    def price : Double = targetList.map(item => item.price).sum
  }
}

trait Item{
  def name: String
  def price: Double
}

case class SingleItem(override val name: String, override val price: Double) extends Item

case class Bundle(override val name: String, override val price: Double, items: List[SingleItem]) extends Item
