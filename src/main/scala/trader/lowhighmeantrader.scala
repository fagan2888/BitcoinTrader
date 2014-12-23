import market.Market
import defs._
import scala.collection.mutable.Queue

package trader {
  // A trader that buys immediately if the current value is less than the mean
  // by some percentage. Sells if the current value is higher than the mean by
  // some percentage.
  class LowHighMeanTrader(
      val m: Market,
      var cash: Double,
      val currency: String,
      windowSize:Int,
      buyPercent: Double,
      sellPercent: Double)
    extends SingleTrader {
    val HighThreshold: Double = 1 + sellPercent
    val LowThreshold: Double = 1 - buyPercent

    var bitcoins: Double = 0
    val pricesSeen: Queue[Double] = new Queue()
    var runningSum: Double = 0
    var nAveraged: Int = 0

    def update(): Unit = {
      val price = btcPrice
      runningSum += price
      pricesSeen.enqueue(price)
      if (nAveraged < windowSize) {
        nAveraged += 1
      } else {
        runningSum -= (pricesSeen.dequeue)
      }
    }

    def amountToSell: Double = {
      def isHigh =
        btcValue * windowSize > runningSum * HighThreshold
      // the first check makes sure that there's enough information to decide
      if (nAveraged == windowSize && bitcoins != 0 && isHigh) {
        return bitcoins
      }
      0.0
    }

    def amountToBuy: Double = {
      def isLow = btcPrice * windowSize < runningSum * LowThreshold
      if (nAveraged == windowSize && cash != 0 && isLow) {
        return maxBTCsCanBuy
      }
      0.0
    }

    def updateAfterSell(trans: Transaction): Unit = ()

    def updateAfterBuy(trans: Transaction): Unit = ()
  }

  class LowHighMeanTraderFactory(
      windowSize: Int,
      buyPercent: Double,
      sellPercent: Double)
    extends SingleTraderFactory {
    def newTrader(m: Market, cash: Double, currency: String): SingleTrader =
      new LowHighMeanTrader(m, cash, currency, windowSize, buyPercent,
          sellPercent)

    override def toString = "Low-High Mean Trader"
  }

  object LowHighMeanTraderFactory {
    def apply(windowSize: Int, buyPercent: Double, sellPercent: Double) =
      new LowHighMeanTraderFactory(windowSize, buyPercent, sellPercent)
  }
}
