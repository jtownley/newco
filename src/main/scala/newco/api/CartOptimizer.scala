package newco.api
import newco.domain.Types.Cart
import newco.domain.{Bundle, SingleItem}

class CartOptimizer(bundles: List[Bundle]){
    val itemBundles = bundles.toSet

    private def unBundle(cart: Cart) : List[SingleItem] = {
      cart.foldLeft(List[SingleItem]())((result, item) => {
        item match {
          case i: SingleItem => result :+ i
          case b: Bundle => result ++ b.items
        }}
      )
    }

    def optimize(cart: Cart) : Cart = {
        val singleItems = unBundle(cart)
        getBundle(singleItems)
    }

    def getBundle(singleItems: List[SingleItem]) : Cart = {
      val compatibleBundles = itemBundles.filter(itemBundle => itemBundle.items.forall(singleItems.contains(_)))

      if (compatibleBundles.isEmpty) {
        singleItems
      } else {
        val bundle = compatibleBundles.max[Bundle](Ordering.by(bundle => bundle.savings))
        List(bundle) ++ getBundle(singleItems.diff(bundle.items))
      }
    }
}