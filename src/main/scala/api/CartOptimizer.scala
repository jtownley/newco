package api
import domain.Types.Cart
import domain.{Bundle, Item, SingleItem}

class CartOptimizer(singleItems:Set[SingleItem], bundles: Set[Bundle]){
    val items = singleItems
    val itemBundles = bundles

    private def unBundle(cart: Cart) : Set[SingleItem] = {
      cart.foldLeft(Set[SingleItem]())((result, item) => {
        item match {
          case i: SingleItem => result + i
          case b: Bundle => result ++ b.items
        }}
      )
    }

    def optimize(cart: Cart) : Cart = {
        val unbundledCart = unBundle(cart)
        val compatibleBundles = itemBundles.filter(itemBundle => itemBundle.items.subsetOf(unbundledCart))

        if (compatibleBundles.isEmpty) {
          cart
        } else {
          Set(compatibleBundles.min[Bundle](Ordering.by(bundles => bundles.price))).asInstanceOf[Cart]
        }
    }
}