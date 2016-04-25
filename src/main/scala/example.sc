// Import stuff

import newco.domain.Types._
import newco.domain.{Bundle, SingleItem}
import newco.api.CartOptimizer

// Create some items

val item1 = SingleItem("Apple", 2.99d)
val item2 = SingleItem("Pear", 3.99d)

// Create some bundles

val bundle1 = Bundle("Apple-mazing", 4.00d, List(item1, item1))
val bundle2 = Bundle("Mix n' Match", 4.50d, List(item1, item2))

// Create an cart: can contain SingleItem and Bundle

val cart : Cart = List(item1, item1, item2)

// Create Api: Only requires knowledge of bundles

val optimizerApi = new CartOptimizer(List(bundle1, bundle2))
val optimizedCart = optimizerApi.optimize(cart)

println(optimizedCart)