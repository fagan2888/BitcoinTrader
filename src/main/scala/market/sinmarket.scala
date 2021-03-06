import math.cos

package market {
  // A market whose price goes up and down according to a sinusoid.
  object SinMarket extends MathMarket {
    private val A = 5 // amplitude
    private val b = 100 // offset

    def shapeFunction(t: Double): Double = b + A*cos(t)

    override def toString = "Sine Market"
  }
}
