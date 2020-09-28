package demo.corridor

import demo.math.Matrix2
import demo.math.Vector2
import demo.math.Vector2.Companion.maximum
import demo.math.Vector2.Companion.minimum
import java.util.concurrent.ThreadLocalRandom
import kotlin.math.max
import kotlin.math.min

class DogWalker(val variance: Double = 0.2, val maxAngle: Double = 2.0) {

    private val random = ThreadLocalRandom.current()

    fun walkies(start: Vector2, initialDir: Vector2, totalSteps: Int, stepSize: Double): Array<Vector2> {
        val maxAngle = clamp(maxAngle, high = Math.PI) / 2.0
        val variance = clamp(variance)
        val vertices = mutableListOf<Vector2>()
        var min = Vector2.POSITIVE_INFINITY
        var max = Vector2.NEGATIVE_INFINITY
        var next = start
        var orientation = initialDir
        for (it in 0..totalSteps) {
            min = minimum(min, next)
            max = maximum(max, next)
            vertices += next
            next += chooseNextDirection(maxAngle) * (orientation.normalize() * chooseNextStepSize(stepSize, variance))
            orientation = next - vertices.last()
        }
        val cog = (min + max) / 2.0
        return vertices.map { it - cog }.toTypedArray()
    }

    private fun chooseNextStepSize(stepSize: Double, variance: Double): Double {
        val min = stepSize * (1 - variance)
        val max = stepSize * (1 + variance)
        return if (min == max) min else random.nextDouble(min, max)
    }

    private fun chooseNextDirection(angle: Double) = Matrix2.rotation(if (angle > 0.0) random.nextDouble(-angle, angle) else 0.0)

    private fun clamp(value: Double, low: Double = 0.0, high: Double = 1.0) = max(low, min(high, value))
}