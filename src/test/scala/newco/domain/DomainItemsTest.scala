package newco.domain

import newco.helpers.TestHelpers
import newco.domain.Types._

class DomainItemsTest extends TestHelpers{
  "Cart" - {
    "should calculate price for items" in {
      val cart : Cart = getSingleItems(3)
      val expectedPrice = cart.map(_.price).sum

      assert(expectedPrice === cart.price)
    }
    "should calculate price for bundles" in {
      val cart : Cart = getBundle()
      val expectedPrice = cart.map(_.price).sum

      assert(expectedPrice === cart.price)
    }
    "should calculate price for bundles and items" in {
      val cart : Cart = getBundle() ++ getSingleItems()
      val expectedPrice = cart.map(_.price).sum

      assert(expectedPrice === cart.price)
    }
  }
}
