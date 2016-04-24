package newco.api

import newco.domain.Types.Cart
import newco.domain.{Bundle, Item, SingleItem}

class CartOptimizer(bundles: List[Bundle]) {
  val itemBundles = bundles.toSet


  def optimize(cart: Cart): Cart = {
    val singleItems = unBundle(cart)
    getBundle(singleItems)
  }

  private def getBundle(singleItems: List[SingleItem]): Cart = {
    val compatibleBundles = itemBundles.filter(itemBundle => itemBundle.items.forall(singleItems.contains(_)))

    if (compatibleBundles.isEmpty) {
      singleItems
    } else {
      compatibleBundles.map(bundle => {
        List(bundle) ++ getBundle(singleItems.diff(bundle.items))
      }).min[Cart](Ordering.by(cart => cartPrice(cart)))
    }
  }

  private def unBundle(cart: Cart): List[SingleItem] = {
    cart.foldLeft(List[SingleItem]())((result, item) => {
      item match {
        case i: SingleItem => result :+ i
        case b: Bundle => result ++ b.items
      }
    }
    )
  }
  private def cartPrice(cart: Cart): Double = {
    cart.map((item: Item) => item match {
      case singleItem: SingleItem => singleItem.price
      case bundle: Bundle => bundle.price
    }).sum
  }
}