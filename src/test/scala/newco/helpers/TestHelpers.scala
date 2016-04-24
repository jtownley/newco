package newco.helpers

import newco.domain.Types._
import newco.domain.{Bundle, SingleItem}
import org.scalatest.FreeSpec

trait TestHelpers extends FreeSpec{
  var counter = 1;

  def getNext : Int = {
    val id = counter
    counter = counter + 1
    id
  }

  def getPrice : Double = getNext.toDouble

  def getSingleItems(count: Int = 1, price: Double = getPrice) =  {
    val start = counter
    val end = counter + count
    counter = counter + count
    Seq.range(start, end).map { cnt: Int => SingleItem(s"S_$cnt", price) }.toList
  }

  def getBundle(items : List[SingleItem] = getSingleItems(3), price: Option[Double] = None) = {
    val bundlePrice = price.getOrElse(items.map(item => item.price).sum - 0.01)
    List(Bundle(s"B_$getNext", bundlePrice, items))
  }

  def sortCart(cart: Cart): Cart = {
    cart.sortWith((item1, item2) => item1.name > item2.name)
  }

  def assertCartEqual(cart1: Cart, cart2: Cart) {
    assert(sortCart(cart1) === sortCart(cart2))
  }

}