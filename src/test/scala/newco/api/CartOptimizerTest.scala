package newco.api
import newco.domain.Item
import newco.domain.Types.Cart
import newco.helpers.TestHelpers
import org.scalatest.FreeSpec
import org.scalatest.exceptions.TestFailedException

class CartOptimizerTest extends FreeSpec with TestHelpers{
  "CartOptimizer" - {
    "should" - {
      "initialize" - {
        "with Empty Sets" in {
          new CartOptimizer(List())
        }
        "with set of one Bundle" in {
          val bundle = getBundle()
          new CartOptimizer( bundle)
        }
      }
      "optimize" - {
        "given cart containing" - {
          "one item not in a bundle" in {
            val item = getSingleItems()
            val cartOptimizer = new CartOptimizer(List())
            val initialCart : Cart = item
            val expectedCart : Cart = item

            val actualCart = cartOptimizer.optimize(initialCart)

            assertCartEqual(expectedCart, actualCart)
          }
          "one item also in a bundle" in {
            val item = getSingleItems()
            val bundle = getBundle(item)
            val cartOptimizer = new CartOptimizer(bundle)
            val initialCart : Cart = item
            val expectedCart : Cart = bundle

            val actualCart = cartOptimizer.optimize(initialCart)

            assertCartEqual(expectedCart, actualCart)
          }
          "one item that is a bundle" in {
            val item = getSingleItems()
            val bundle = getBundle(item)
            val cartOptimizer = new CartOptimizer(bundle)
            val initialCart : Cart = bundle
            val expectedCart : Cart = bundle

            val actualCart = cartOptimizer.optimize(initialCart)

            assertCartEqual(expectedCart, actualCart)
          }
          "one item that is part of many bundles" in {
            val item = getSingleItems(price = 15.00)
            val bundleExpensive = getBundle(item, Some(12.99))
            val bundleCheap = getBundle(item, Some(11.99))
            val bundles = bundleExpensive ++ bundleCheap
            val cartOptimizer = new CartOptimizer(bundles)
            val initialCart : Cart = item
            val expectedCart : Cart = bundleCheap

            val actualCart = cartOptimizer.optimize(initialCart)

            assertCartEqual(expectedCart, actualCart)
          }
          "two identical items that are part of the same bundles" in {
            val item = getSingleItems(price = 15.00)
            val bundleExpensive = getBundle(item ++ item, Some(12.99))
            val bundleCheap = getBundle(item ++ item, Some(11.99))
            val bundles = bundleExpensive ++ bundleCheap
            val cartOptimizer = new CartOptimizer(bundles)
            val initialCart : Cart = item
            val expectedCart : Cart = bundleCheap

            val actualCart = cartOptimizer.optimize(initialCart)

            assertCartEqual(expectedCart, actualCart)
          }
          "one item that is part of a bundles and one that is not" in {
            val bundledItem = getSingleItems()
            val unBundledItem = getSingleItems()
            val bundles = getBundle(bundledItem)
            val items = bundledItem ++ unBundledItem
            val cartOptimizer = new CartOptimizer(bundles)
            val initialCart : Cart = items
            val expectedCart : Cart = bundles ++ unBundledItem

            val actualCart = cartOptimizer.optimize(initialCart)

            assertCartEqual(expectedCart, actualCart)
          }
          "4 items that is part of a 2 bundles" in {
            val items1 = getSingleItems(2)
            val items2 = getSingleItems(2)
            val bundle1 = getBundle(items1)
            val bundle2 = getBundle(items2)
            val items = items1 ++ items2
            val cartOptimizer = new CartOptimizer(bundle1 ++ bundle2)
            val initialCart : Cart = items
            val expectedCart : Cart = bundle1 ++ bundle2

            val actualCart = cartOptimizer.optimize(initialCart)

            assertCartEqual(expectedCart, actualCart)
          }
          "3 items that could be part of a 2 bundles but saving is better then price for one bundle with higher price" in {
            val item1 = getSingleItems(price = 3.00)
            val item2 = getSingleItems(price = 4.00)
            val item3 = getSingleItems(price = 5.00)
            val bundle1 = getBundle(item1 ++ item3, price = Some(7.00d))
            val bundle2 = getBundle(item2 ++ item3, price = Some(7.50d) )
            val items = item1 ++ item2 ++ item3
            val cartOptimizer = new CartOptimizer(bundle1 ++ bundle2)
            val initialCart : Cart = items
            val expectedCart : Cart = item1 ++ bundle2

            val actualCart = cartOptimizer.optimize(initialCart)

            assertCartEqual(expectedCart, actualCart)
          }
//          "multiple items that could be part of a multiple bundles some of which are better deals if examined on there own" in {
//            val item1 = getSingleItems(price = 5.00)
//            val item2 = getSingleItems(price = 12.00)
//            val item3 = getSingleItems(price = 10.00)
//            val bundle1 = getBundle(item1 ++ item3, price = Some(13.50d))
//            val bundle2 = getBundle(item2 ++ item3 ++ item3, price = Some(30.00d))
//            val items = item1 ++ item1 ++ item2 ++ item3 ++ item3
//            val cartOptimizer = new CartOptimizer(bundle1 ++ bundle2)
//            val initialCart : Cart = items
//            val expectedCart : Cart = item2 ++ bundle1 ++ bundle1
//
//            val actualCart = cartOptimizer.optimize(initialCart)
//
//            assertCartEqual(expectedCart, actualCart)
//          }
        }
      }
    }
  }

  def assertCartEqual(cart1: Cart, cart2: Cart) {
    if (cart1.isEmpty && cart2.isEmpty) {
      return
    } else {
      if (cart2.contains(cart1.head)){
        assertCartEqual(cart1.tail, cart2.filterNot{item : Item => item == cart1.head})
      } else {
        fail
      }
    }

  }
}
