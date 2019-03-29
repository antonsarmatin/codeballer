package strategy

import Constants
import geom.Vector
import model.Ball
import model.Rules

class Predictor(var rules: Rules) {

    private val microtick = 10

    fun predict(ball: Ball, ticks: Int): List<Prediction> {
        val prediction = mutableListOf<Prediction>()
        for (i in 1..ticks * microtick) {
            prediction.add(update(ball, i.toDouble() / microtick.toDouble()))
        }
        return prediction
    }

    private fun update(ball: Ball, deltaTime: Double): Prediction {

        var velocity = Vector(ball.velocity_x, ball.velocity_y, ball.velocity_z)
        velocity = clamp(velocity, Constants.MAX_ENTITY_SPEED.toDouble())

        val position = Vector(ball.x, ball.y, ball.z).add(velocity.dx * deltaTime, velocity.dy * deltaTime, velocity.dz * deltaTime)
//todo  нужно разобраться с отскоком мяча, чтобы корректно считать его y положение
//        position.dy -= Constants.GRAVITY * deltaTime * deltaTime / 2
//        position.dy = ball.y
        if (position.dy - Constants.GRAVITY * deltaTime * deltaTime / 2 > 0) {
            position.dy -= Constants.GRAVITY * deltaTime * deltaTime / 2
        } else {
            position.dy = 0.5
        }

        if (position.dz < -(rules.arena.depth / 2) - rules.arena.goal_depth)
            position.dz = -(rules.arena.depth / 2) - rules.arena.goal_depth

        velocity.dy -= Constants.GRAVITY * deltaTime

        return Prediction(position, velocity)
    }

    private fun clamp(vector: Vector, maxLength: Double): Vector {
        return if (vector.squaredLength > maxLength * maxLength) {
            vector.setLength(maxLength)
        } else vector
    }

    inner class Prediction(var position: Vector, var velocity: Vector) {


        fun copy(): Prediction {
            return Prediction(position.copy(), velocity.copy())
        }
    }

}