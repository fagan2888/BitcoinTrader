import market.Market
import defs._

package trader {
  // A trader buys and sells bitcoins in an attempt to make money
  trait Trader { 
    // Where the trader trades
    val m: Market

    /* How many times did the trader go to the market?
     * i.e. # times trade() was called */
    var nTradesTried: Int = 0

    /* Tell the trader to try to go to the market and trade. May not actually
     * trade because the market (e.g.) may be closed. */
    def tryToTrade(): Unit = {
      if (m.isOpen) {
        nTradesTried += 1
        trade()
      }
    }

    // Tells the trader to try trading some money. Assumes the market is open.
    def trade(): Unit

    // A log of past transactions
    def history: TraderHistory

    // Returns the amount of money left if the trader were to cash out now (or
    // right when the market has closed)
    def moneyLeft: Double
  }

  trait TraderFactory {
    def newTrader(m: Market, cash: Double, currency: String): Trader
  }
}
