package api
import domain.Types.Cart
import domain.{Bundle, Item, SingleItem}
import org.scalatest.FreeSpec

trait TestHelpers {
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
    Seq.range(start, end).map { cnt: Int => SingleItem(s"$cnt", price) }.toList
  }

  def getBundle(items : List[SingleItem] = getSingleItems(3), price: Option[Double] = None) = {
    val bundlePrice = price.getOrElse(items.map(item => item.price).sum - 0.01)
    List(Bundle(s"B_$getNext", bundlePrice, items))
  }
}

class CartOptimizerTest extends FreeSpec with TestHelpers{
  "CartOptimizer" - {
    "should" - {
      "initialize" - {
        "with Empty Sets" in {
          new CartOptimizer(List())
        }
        "with set of one SingleItems" in {
          val items = getSingleItems()
          new CartOptimizer(List())
        }
        "with set of one Bundle" in {
          val bundle = getBundle()
          new CartOptimizer( bundle)
        }
        "with mix of single Item and Bundle" in {
          val items = getSingleItems()
          val bundle = getBundle()
          new CartOptimizer(bundle)
        }
      }
      "optimize" - {
        "given" - {
          "Cart containing one item not in a bundle" in {
            val item = getSingleItems()
            val cartOptimizer = new CartOptimizer(List())
            val initialCart : Cart = item
            val expectedCart : Cart = item

            val actualCart = cartOptimizer.optimize(initialCart)
            assert(expectedCart === actualCart)
          }
          "Cart containing one item also in a bundle" in {
            val item = getSingleItems()
            val bundle = getBundle(item)
            val cartOptimizer = new CartOptimizer(bundle)
            val initialCart : Cart = item
            val expectedCart : Cart = bundle

            val actualCart = cartOptimizer.optimize(initialCart)
            assert(expectedCart === actualCart)
          }
          "Cart containing one item that is a bundle" in {
            val item = getSingleItems()
            val bundle = getBundle(item)
            val cartOptimizer = new CartOptimizer(bundle)
            val initialCart : Cart = bundle
            val expectedCart : Cart = bundle

            val actualCart = cartOptimizer.optimize(initialCart)
            assert(expectedCart === actualCart)
          }
          "Cart containing one item that is part of many bundles" in {
            val item = getSingleItems(price = 15.00)
            val bundleExpensive = getBundle(item, Some(12.99))
            val bundleCheap = getBundle(item, Some(11.99))
            val bundles = bundleExpensive ++ bundleCheap
            val cartOptimizer = new CartOptimizer(bundles)
            val initialCart : Cart = item
            val expectedCart : Cart = bundleCheap

            val actualCart = cartOptimizer.optimize(initialCart)
            assert(expectedCart === actualCart)
          }
          "Cart containing two identical items that are part of the same bundles" in {
            val item = getSingleItems(price = 15.00)
            val bundleExpensive = getBundle(item ++ item, Some(12.99))
            val bundleCheap = getBundle(item, Some(11.99))
            val bundles = bundleExpensive ++ bundleCheap
            val cartOptimizer = new CartOptimizer(bundles)
            val initialCart : Cart = item
            val expectedCart : Cart = bundleCheap

            val actualCart = cartOptimizer.optimize(initialCart)
            assert(expectedCart === actualCart)
          }
          "Cart containing one item that is part of a bundles and one that is not" in {
            val bundledItem = getSingleItems()
            val unBundledItem = getSingleItems()
            val bundles = getBundle(bundledItem)
            val items = bundledItem ++ unBundledItem
            val cartOptimizer = new CartOptimizer(bundles)
            val initialCart : Cart = items
            val expectedCart : Cart = bundles ++ unBundledItem

            val actualCart = cartOptimizer.optimize(initialCart)
            assert(expectedCart === actualCart)
          }
        }
      }
    }
  }
}
