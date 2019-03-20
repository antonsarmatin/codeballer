package strategy

import Constants
import geom.Vector
import model.Ball

class Predictor {

    private val ticks = 10
    private val microtick = 10
    private val positions = mutableListOf<Vector>()

    fun predict(ball: Ball): List<Vector> {
        positions.clear()
        for (i in 1..ticks * microtick) {
            update(ball, (i / microtick).toDouble())
        }
        return positions
    }

    private fun update(ball: Ball, deltaTime: Double) {

        var velocity = Vector(ball.velocity_x, ball.velocity_y, ball.velocity_z)
        velocity = clamp(velocity, Constants.MAX_ENTITY_SPEED.toDouble())

        val position = Vector(ball.x, ball.y, ball.z).add(velocity.dx * deltaTime, velocity.dy * deltaTime, velocity.dz * deltaTime)
        position.dy -= Constants.GRAVITY * deltaTime * deltaTime / 2


        velocity.dy -= Constants.GRAVITY * deltaTime

        positions.add(position.copy())

    }

    private fun clamp(vector: Vector, maxLength: Double): Vector {
        return if (vector.squaredLength > maxLength * maxLength) {
            vector.setLength(maxLength)
        } else vector
    }

}