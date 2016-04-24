package newco.domain

import newco.helpers.TestHelpers
import org.scalatest.FreeSpec

class DomainItemsTest extends FreeSpec with TestHelpers {
  "Bundle" - {
    "savings" - {
      "should list correct savings" - {
        "when empty" in {
          val bundle = Bundle("Empty", 2.00d, List())
          assert(0.00d === bundle.savings)
        }
        "when more then items" in {
          val bundle = Bundle("BadDeal", 2.00d, getSingleItems(price = 1.00d))
          assert(-1.00d === bundle.savings)
        }
        "when less then items" in {
          val bundle = Bundle("BadDeal", 1.00d, getSingleItems(price = 2.00d))
          assert(1.00d === bundle.savings)
        }
      }
    }
  }

}
